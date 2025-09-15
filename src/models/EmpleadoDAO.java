package models;

import controllers.MainController;
import java.sql.Statement;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import proyectojavafx.connect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EmpleadoDAO {
    
    private static final String ALFABETO = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private String generarCodigo(int longitud) {
        StringBuilder sb = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++) {
            int index = RANDOM.nextInt(ALFABETO.length());
            sb.append(ALFABETO.charAt(index));
        }
        return sb.toString();
    }


    public boolean register(Empleado emp) {
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
            
            String codigo = null;
            if (emp.getTipo() == 1) {
                codigo = generarCodigo(8);
            }

            String query = "INSERT INTO empleado (nombre, apellido, email, contrasenia, id_rol, activa, codigo_vin) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, emp.getNombre());
                ps.setString(2, emp.getApellido());
                ps.setString(3, emp.getEmail());
                ps.setString(4, emp.getContrasenia());
                ps.setInt(5, emp.getTipo());
                ps.setInt(6, emp.getActiva());
                ps.setString(7, codigo);
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
    
    public Empleado login(String usuario, String contrasenia) {
        try (Connection con = new connect().getConectar()) {
            String query = "SELECT * FROM Empleado WHERE email = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, usuario);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String passDB = rs.getString("contrasenia");
                        if (contrasenia.equals(passDB)) {
                            // Devuelve un objeto Empleado con los datos de la db
                            return new Empleado(
                                rs.getInt("id_emp"),
                                rs.getString("nombre"),
                                rs.getString("apellido"),
                                rs.getString("contrasenia"),
                                rs.getString("email"),
                                rs.getInt("id_rol"),
                                rs.getInt("activa"),
                                rs.getString("codigo_vin")
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
    
    public ObservableList<Empleado> getAllEmps() {
        ObservableList<Empleado> empleados = FXCollections.observableArrayList();
        try (Connection con = new connect().getConectar()) {
            String query = "SELECT * FROM empleado WHERE activa = 1"; //! AND codigo_vin IS NULL
            try (PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Empleado emp = new Empleado(
                            rs.getInt("id_emp"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("email"),
                            rs.getString("contrasenia"),
                            rs.getInt("id_rol"),
                            rs.getInt("activa"),
                            rs.getString("codigo_vin")
                        );
                        empleados.add(emp);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
        }
        return empleados;
    }
    
    /*
    public String vincularCodigo(int idInspector, int idProfesor) {
        String updateSolicitud = "UPDATE solicitud SET estado = 'aceptada' WHERE emisor_id = ? AND receptor_id = ?";
        String obtenerCodigoInspector = "SELECT codigo_vin FROM empleado WHERE id_emp = ?";
        String obtenerCodigoProfesor = "SELECT codigo_vin FROM empleado WHERE id_emp = ?";
        String actualizarProfesor = "UPDATE empleado SET codigo_vin = ? WHERE id_emp = ?";

        try (Connection con = new connect().getConectar()) {

            // Actualizar la solicitud
            try (PreparedStatement psSolicitud = con.prepareStatement(updateSolicitud)) {
                psSolicitud.setInt(1, idInspector);
                psSolicitud.setInt(2, idProfesor);
                int filasSolicitud = psSolicitud.executeUpdate();
                if (filasSolicitud == 0) {
                    System.out.println("No se encontró la solicitud para actualizar.");
                    return null;
                }
            }

            // Obtener el código del inspector
            String codigoInspector = null;
            try (PreparedStatement psInspector = con.prepareStatement(obtenerCodigoInspector)) {
                psInspector.setInt(1, idInspector);
                try (ResultSet rs = psInspector.executeQuery()) {
                    if (rs.next()) {
                        codigoInspector = rs.getString("codigo_vin");
                    }
                }
            }
            if (codigoInspector == null || codigoInspector.isEmpty()) {
                System.out.println("El inspector no tiene código asignado.");
                return null;
            }

            // Verificar si el profesor ya tiene código
            String codigoProfesor = null;
            try (PreparedStatement psProfesor = con.prepareStatement(obtenerCodigoProfesor)) {
                psProfesor.setInt(1, idProfesor);
                try (ResultSet rs = psProfesor.executeQuery()) {
                    if (rs.next()) {
                        codigoProfesor = rs.getString("codigo_vin");
                    }
                }
            }
            if (codigoProfesor != null && !codigoProfesor.isEmpty()) {
                MainController.getInstance().mostrarAlerta("Error","El profesor ya tiene un código asignado. Para asignar otro elimine su vinculo con el actual.");
                return null;
            }

            // Asignar el código del inspector al profesor
            try (PreparedStatement psUpdate = con.prepareStatement(actualizarProfesor)) {
                psUpdate.setString(1, codigoInspector);
                psUpdate.setInt(2, idProfesor);
                psUpdate.executeUpdate();
                MainController.getInstance().mostrarAlerta("Asignada","Código del inspector asignado al profesor correctamente.");
                return codigoInspector; // <-- aquí devuelvo el código asignado
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    */

}