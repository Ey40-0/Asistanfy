package models;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import proyectojavafx.connect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EmployeeC {

    /**
     * Registra un nuevo empleado si no existe.
     * @param emp El empleado a registrar.
     * @return true si se registró, false si ya existía o error.
     */
    public boolean register(Employee emp) {
        try (Connection con = new connect().getConectar()) {
            String checkQuery = "SELECT COUNT(*) FROM Empleado WHERE email = ? AND activa = 1";
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setString(1, emp.getEmail());

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("Ese usuario ya existe");
                        return false;
                    }
                }
            }

            String query = "INSERT INTO empleado (nombre, apellido, email, contrasenia, id_rol, activa) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, emp.getNombre());
                ps.setString(2, emp.getApellido());
                ps.setString(3, emp.getEmail());
                ps.setString(4, emp.getContrasenia());
                ps.setInt(5, emp.getTipo());
                ps.setInt(6, emp.getActiva());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        emp.setId(rs.getInt(1));
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Autentica un empleado por email y contraseña.
     * @param usuario Email.
     * @param contrasenia Contraseña.
     * @return El empleado si es válido, null en caso contrario.
     */
    public Employee login(String usuario, String contrasenia) {
        try (Connection con = new connect().getConectar()) {
            String query = "SELECT * FROM Empleado WHERE email = ? AND activa = 1";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, usuario);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String passDB = rs.getString("contrasenia");
                        if (contrasenia.equals(passDB)) {
                            return new Employee(
                                rs.getInt("id_emp"),
                                rs.getString("nombre"),
                                rs.getString("apellido"),
                                rs.getString("contrasenia"),
                                rs.getString("email"),
                                rs.getInt("id_rol"),
                                rs.getInt("activa")
                            );
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene todos los empleados activos (excepto admin).
     * @return Lista observable de empleados.
     */
    public ObservableList<Employee> getAllEmps() {
        ObservableList<Employee> empleados = FXCollections.observableArrayList();
        try (Connection con = new connect().getConectar()) {
            String query = "SELECT * FROM empleado WHERE activa = 1 AND id_rol != 2";
            try (PreparedStatement ps = con.prepareStatement(query);
                 ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Employee emp = new Employee(
                            rs.getInt("id_emp"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("email"),
                            rs.getString("contrasenia"),
                            rs.getInt("id_rol"),
                            rs.getInt("activa")
                        );
                        empleados.add(emp);
                    }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return empleados;
    }
    
    /**
     * Obtiene solo los profesores activos.
     * @return Lista observable de profesores.
     */
    public ObservableList<Employee> getProfesores() {
        ObservableList<Employee> todosEmpleados = this.getAllEmps();
        ObservableList<Employee> profesores = FXCollections.observableArrayList();

        for (Employee emp : todosEmpleados) {
            if (emp.getTipo() == 0) {
                profesores.add(emp);
            }
        }
        return profesores;
    }
    
    /**
     * Desactiva un empleado (setea activa=0), verificando dependencias.
     * @param idEmployee ID del empleado.
     */
    public void deleteEmployee(int idEmployee) { 
        try (Connection con = new connect().getConectar()) {
            // Verificar dependencias (evaluaciones asociadas)
            String checkQuery = "SELECT COUNT(*) FROM evaluacion WHERE Empleado_id = ? AND is_active = 1";
            try (PreparedStatement checkPs = con.prepareStatement(checkQuery)) {
                checkPs.setInt(1, idEmployee);
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("No se puede eliminar empleado con evaluaciones activas.");
                        // Opcional: MainCllr.mostrarAlerta(...)
                        return;
                    }
                }
            }

            String query = "UPDATE empleado SET activa = 0 WHERE id_emp = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, idEmployee);
                ps.executeUpdate();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Actualiza un empleado, verificando unicidad de email.
     * @param emp El empleado actualizado.
     * @return true si se actualizó, false si email duplicado o error.
     */
    public boolean updateEmployee(Employee emp) {
        try (Connection con = new connect().getConectar()) {
            // Verificar unicidad de nuevo email
            String checkQuery = "SELECT COUNT(*) FROM Empleado WHERE email = ? AND id_emp != ? AND activa = 1";
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setString(1, emp.getEmail());
                checkStmt.setInt(2, emp.getId());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("Email ya existe en otro empleado.");
                        return false;
                    }
                }
            }

            String query = """
                UPDATE empleado SET
                    nombre = ?,
                    apellido = ?,
                    email = ?,
                    contrasenia = ?,
                    id_rol = ?
                WHERE id_emp = ?""";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, emp.getNombre());
                ps.setString(2, emp.getApellido());
                ps.setString(3, emp.getEmail());
                ps.setString(4, emp.getContrasenia());
                ps.setInt(5, emp.getTipo());
                ps.setInt(6, emp.getId());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("Empleado actualizado correctamente.");
                    return true;
                } else {
                    System.out.println("No se encontró el empleado con ID: " + emp.getId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}