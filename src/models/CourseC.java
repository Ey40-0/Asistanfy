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
        String sql = "SELECT * FROM curso ORDER BY nivel";

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
    
    public void addMatterToCourse(Course cou, Test mat) {
        if (mat.getId() == 0) {
            MainCllr.getInstance().mostrarAlerta("Error", "Evaluacion todavia no ha sido creada.");
            return;
        }

        try (Connection con = new connect().getConectar()) {
            String sql = "INSERT INTO detalle_eva_cur (id_cur, id_eva) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, cou.getId());
                ps.setInt(2, mat.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean addNewCourse(Course cou) {
        String checkQuery = "SELECT COUNT(id) FROM curso WHERE nivel = ?";
        String insertQuery = "INSERT INTO curso (nivel) VALUES (?)";

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
}
