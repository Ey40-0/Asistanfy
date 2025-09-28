

package models;

import controllers.MainCllr;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import proyectojavafx.connect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CourseC {

    public static ObservableList<Course> getCourses() {
        ObservableList<Course> cursos = FXCollections.observableArrayList();
        String sql = "SELECT * FROM curso WHERE is_active = 1 ORDER BY nivel";

        try (Connection con = new connect().getConectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                cursos.add(new Course(rs.getInt("id"), rs.getString("nivel")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cursos;
    }
    
    public void addTestToCourse(Course cou, Test tes) {
        if (tes.getId() == 0) {
            MainCllr.getInstance().mostrarAlerta("Error", "Evaluación todavía no ha sido creada.");
            return;
        }

        try (Connection con = new connect().getConectar()) {
            // Verificar si ya existe la relación
            String checkSql = "SELECT COUNT(*) FROM detalle_eva_cur WHERE id_cur = ? AND id_eva = ?";
            try (PreparedStatement checkPs = con.prepareStatement(checkSql)) {
                checkPs.setInt(1, cou.getId());
                checkPs.setInt(2, tes.getId());
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        MainCllr.getInstance().mostrarAlerta("Advertencia", "La evaluación ya estaba asociada a este curso.");
                        return; // No insertar si ya existe
                    }
                }
            }

            // Insertar nueva relación
            String sql = "INSERT INTO detalle_eva_cur (id_cur, id_eva) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, cou.getId());
                ps.setInt(2, tes.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            MainCllr.getInstance().mostrarAlerta("Error", "No se pudo asociar la evaluación al curso.");
        }
    }

    public boolean addNewCourse(Course cou) {
        String checkQuery = "SELECT COUNT(id) FROM curso WHERE nivel = ? AND is_active = 1";
        String insertQuery = "INSERT INTO curso (nivel, is_active) VALUES (?, ?)";

        try (Connection con = new connect().getConectar()) {
            // 1. Verificar si el curso ya existe
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setString(1, cou.getNombre());
                
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // El curso ya existe, salir del método
                        System.out.println("El curso ya existe, no se puede añadir de nuevo.");
                        return false; // Importante: salir del método aquí
                    }
                }
            }

            // 2. Insertar el curso si no existe
            try (PreparedStatement ps = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, cou.getNombre());
                ps.setInt(2, 1);
                int filasAfectadas = ps.executeUpdate();

                if (filasAfectadas > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            cou.setId(rs.getInt(1));
                            System.out.println("Curso añadido con éxito con el ID: " + cou.getId());
                            return true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void deleteCourse(int idCourse) {
        try (Connection con = new connect().getConectar()) {
            String query = "UPDATE curso SET is_active = 0 WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, idCourse);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean updateCourse(Course cou) {
       try (Connection con = new connect().getConectar()) {
            String query = "UPDATE curso SET nivel = ? WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, cou.getNombre());
                ps.setInt(2, cou.getId());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("Curso actualizado correctamente.");
                    return true;
                } else {
                    System.out.println("No se encontró el curso con ID: " + cou.getId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
       return false;
   }
}
