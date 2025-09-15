package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectojavafx.connect;

public class SolicitudDAO {

    public ObservableList<Empleado> getAllSoli(int idProfesor) {
        ObservableList<Empleado> solicitudes = FXCollections.observableArrayList();
        try (Connection con = new connect().getConectar()) {
            String query = "SELECT e.* FROM empleado e " +
                           "JOIN solicitud s ON e.id_emp = s.emisor_id " +
                           "WHERE s.receptor_id = ? and s.estado = 'en espera'";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, idProfesor);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Empleado emp = new Empleado(
                            rs.getInt("id_emp"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("contrasenia"),
                            rs.getString("email"),
                            rs.getInt("id_rol"),
                            rs.getInt("activa"),
                            rs.getString("codigo_vin")
                        );
                        solicitudes.add(emp);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitudes;
    }

    public boolean sendSol(int idInspector, int idProfesor) throws SQLException {
        try (Connection con = new connect().getConectar()) {

            // Verifica que no exista una solicitud previa
            String checkQuery = "SELECT COUNT(*) FROM solicitud WHERE emisor_id = ? AND receptor_id = ?";
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, idInspector);
                checkStmt.setInt(2, idProfesor);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("Ya existe una solicitud entre estos usuarios.");
                        return false; // ya existe
                    }
                }
            }

            // Inserta nueva solicitud con estado = 'en espera'
            String insertQuery = "INSERT INTO solicitud (emisor_id, receptor_id, estado, created_at) VALUES (?, ?, 'en espera', NOW())";
            try (PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, idInspector);
                insertStmt.setInt(2, idProfesor);
                insertStmt.executeUpdate();
            }

            System.out.println("Solicitud enviada con éxito.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /*
    public boolean aceptarSolicitud(int idSolicitud) {
        try (Connection con = new connect().getConectar()) {

            // 1. Obtener IDs del emisor y receptor
            String getSol = "SELECT emisor_id, receptor_id FROM solicitud WHERE id_sol = ?";
            int idInspector = 0;
            int idProfesor = 0;

            try (PreparedStatement ps = con.prepareStatement(getSol)) {
                ps.setInt(1, idSolicitud);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idInspector = rs.getInt("emisor_id");
                        idProfesor = rs.getInt("receptor_id");
                    } else {
                        System.out.println("Solicitud no encontrada.");
                        return false;
                    }
                }
            }

            // 2. Obtener código del inspector
            String getCodigo = "SELECT codigo_vin FROM empleado WHERE id_emp = ?";
            String codigoVin = null;

            try (PreparedStatement ps = con.prepareStatement(getCodigo)) {
                ps.setInt(1, idInspector);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        codigoVin = rs.getString("codigo_vin");
                    }
                }
            }

            if (codigoVin == null || codigoVin.isEmpty()) {
                System.out.println("Inspector no tiene código asignado.");
                return false;
            }

            // 3. Actualizar código del profesor
            String updateProfesor = "UPDATE empleado SET codigo_vin = ? WHERE id_emp = ?";
            try (PreparedStatement ps = con.prepareStatement(updateProfesor)) {
                ps.setString(1, codigoVin);
                ps.setInt(2, idProfesor);
                ps.executeUpdate();
            }

            // 4. Cambiar estado de la solicitud a 'aceptada'
            String updateSol = "UPDATE solicitud SET estado = 'aceptada', updated_at = NOW() WHERE id_sol = ?";
            try (PreparedStatement ps = con.prepareStatement(updateSol)) {
                ps.setInt(1, idSolicitud);
                ps.executeUpdate();
            }

            System.out.println("Solicitud aceptada y código replicado.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    */
}