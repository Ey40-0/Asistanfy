package models;

import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import proyectojavafx.connect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestC {
    
    public boolean insert(Test eval) {
        String sql = "INSERT INTO evaluacion (descripcion, fecha, asignatura_id, empleado_id, is_active) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = new connect().getConectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, eval.getTitulo());
            ps.setDate(2, java.sql.Date.valueOf(eval.getFecha()));
            ps.setInt(3, eval.getAsignatura().getId());
            ps.setInt(4, eval.getIdProfesor());
            ps.setInt(5, eval.getIs_active());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        eval.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            // Manejo del error de duplicado
            if (e.getErrorCode() == 1062) { // MySQL: entrada duplicada
                System.out.println("Ya existe una evaluación con ese nombre en esas credenciales.");
            } else {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    public List<Test> getEvaluacionesByProfesor(int profesorId) {
        List<Test> evaluaciones = new ArrayList<>();
        String sql = """
            SELECT e.id_eva, e.descripcion, e.fecha,
                   e.is_active, a.id_asign, a.nombre AS asig_name,
                   c.id_cur, c.nivel AS curso_level
            FROM evaluacion e
            INNER JOIN asignatura a ON e.Asignatura_id = a.id_asign
            INNER JOIN detalle_eva_cur det ON e.id_eva = det.id_eva
            INNER JOIN curso c ON det.id_cur = c.id_cur
            WHERE e.Empleado_id = ?
            ORDER BY e.fecha DESC;
        """;

        try (Connection con = new connect().getConectar();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, profesorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Matter asig = new Matter(rs.getInt("id_asign"), rs.getString("asig_name"));
                    Course curso = new Course(rs.getInt("id_cur"), rs.getString("curso_level"));

                    Test eval = new Test(
                        rs.getInt("id_eva"),
                        rs.getString("descripcion"),
                        rs.getDate("fecha").toLocalDate(),
                        curso,
                        asig,
                        profesorId,
                        rs.getInt("is_active")
                    );

                    evaluaciones.add(eval);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return evaluaciones;
    }
    
}
