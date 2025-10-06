package models;

import controllers.MainCllr;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectojavafx.connect;

public class MatterC {
        /**
     * Obtiene todas las materias activas.
     * @return Lista observable de materias.
     */
    public static ObservableList<Matter> getMatters() {
        ObservableList<Matter> matters = FXCollections.observableArrayList();
        String sql = "SELECT * FROM matter WHERE is_active = 1";
        
        try (Connection con = new connect().getConectar();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                matters.add(new Matter(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matters;
   }
    
   /**
    * Añade una nueva materia si no existe.
    * @param mat La materia a añadir.
    * @return true si se añadió, false si ya existía o error.
    */
   public static boolean addNewMatter(Matter mat) {
       String checkQuery = "SELECT COUNT(id) FROM matter WHERE name = ? AND is_active = 1";
       String insertQuery = "INSERT INTO matter (name, is_active) VALUES (?, ?)";
       
       try (Connection con = new connect().getConectar()) {
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setString(1, mat.getNombre());
                
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        MainCllr.mostrarAlerta("Error al insertar", "El curso ya existe, no se puede añadir de nuevo.");
                        return false;
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, mat.getNombre());
                ps.setInt(2, 1);
                int filasAfectadas = ps.executeUpdate();

                if (filasAfectadas > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            mat.setId(rs.getInt(1));
                            System.out.println("Curso añadido con éxito con el ID: " + mat.getId());
                            return true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
       return false;
    }
   
   /**
    * Desactiva una materia (setea is_active=0), verificando dependencias.
    * @param idMatter ID de la materia.
    */
   public static void deleteMatter(int idMatter) {
        try (Connection con = new connect().getConectar()) {
            // Verificar dependencias (evaluaciones asociadas)
            String checkQuery = "SELECT COUNT(*) FROM evaluacion WHERE Asignatura_id = ?";
            try (PreparedStatement checkPs = con.prepareStatement(checkQuery)) {
                checkPs.setInt(1, idMatter);
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        MainCllr.mostrarAlerta("Error", "No se puede eliminar materia con evaluaciones asociadas.");
                        return;
                    }
                }
            }

            String query = "UPDATE matter SET is_active = 0 WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, idMatter);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
   /**
    * Actualiza una materia existente.
    * @param mat La materia actualizada.
    * @return true si se actualizó, false en error.
    */
   public static boolean updateMatter(Matter mat) {
       try (Connection con = new connect().getConectar()) {
            String query = "UPDATE matter SET name = ? WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, mat.getNombre());
                ps.setInt(2, mat.getId());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("Materia actualizada correctamente.");
                    return true;
                } else {
                    System.out.println("No se encontró la materia con ID: " + mat.getId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
       return false;
   }
   
   public static String matterDislike(int curId) {
       String mattName = "";
        String sql = """
            SELECT 
                            m.name AS materia,
                            COUNT(dea.id) AS total_inasistencias
                        FROM detalle_eva_alumno dea
                        JOIN evaluacion e ON dea.id_eva = e.id_eva
                        JOIN matter m ON e.Asignatura_id = m.id
                        JOIN alumnos a ON dea.id_alumno = a.id
                        JOIN curso c ON a.id_cur = c.id
                        WHERE c.id = ?   -- acá metís el curso seleccionado
                        GROUP BY m.id, m.name
                        ORDER BY total_inasistencias DESC
                        LIMIT 1;
        """;

        try (Connection con = new connect().getConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, curId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    mattName = rs.getString("materia");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mattName;
   }
}
