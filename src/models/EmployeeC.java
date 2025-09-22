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

    public boolean register(Employee emp) {
        try (Connection con = new connect().getConectar()) {
            String checkQuery = "SELECT COUNT(*) FROM Empleado WHERE email = ?";
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setString(1, emp.getEmail());

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("Ese usuario ya existe");
                        return false; // ya existe
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
                        emp.setId(rs.getInt(1)); // <- setear el ID real generado
                    }
                }
            }
            return true; // insertado con exito
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // error al insertar
        }
    }

    public Employee login(String usuario, String contrasenia) {
        try (Connection con = new connect().getConectar()) {
            String query = "SELECT * FROM Empleado WHERE email = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, usuario);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String passDB = rs.getString("contrasenia");
                        if (contrasenia.equals(passDB)) {
                            // Devuelve un objeto Empleado con los datos de la db
                            return new Employee(
                                rs.getInt("id_emp"),
                                rs.getString("nombre"),
                                rs.getString("apellido"),
                                rs.getString("email"),
                                rs.getString("contrasenia"),
                                rs.getInt("id_rol"),
                                rs.getInt("activa")
                            );
                        } else {
                            return null; // contraseña incorrecta
                        }
                    } else {
                        return null; // usuario no existe
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

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
    
    public ObservableList<Employee> getProfesores() {
        ObservableList<Employee> todosEmpleados = this.getAllEmps();
        ObservableList<Employee> profesores = FXCollections.observableArrayList();

        for (Employee emp : todosEmpleados) {
            if (emp.getTipo() == 0) { // tipo 0 = profesor
                profesores.add(emp);
            }
        }
        return profesores;
    }
}