package models;

import controllers.MainController;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import proyectojavafx.connect;

public class CursoDAO {

    public static List<Curso> obtenerCursos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT * FROM curso";

        try (Connection con = new connect().getConectar();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                cursos.add(new Curso(rs.getInt("id_cur"), rs.getString("nivel")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cursos;
    }
    
    public void addMatterToCourse(Curso cou, Evaluacion mat) {
        if (mat.getId() == 0) {
            MainController.getInstance().mostrarAlerta("Error", "Evaluacion todavia no ha sido creada.");
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
}
