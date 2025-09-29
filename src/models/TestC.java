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
    
    /**
     * Inserta una nueva prueba, verificando duplicados y reactivando si inactiva.
     * @param eval La prueba a insertar.
     * @return true si se insertó/reactivó, false en duplicado activo o error.
     */
    public boolean insert(Test eval) {
        String checkSql = "SELECT id_eva FROM evaluacion WHERE descripcion = ? AND fecha = ? AND asignatura_id = ? AND empleado_id = ? AND is_active = 1";

        try (Connection con = new connect().getConectar()) {
            try (PreparedStatement checkPs = con.prepareStatement(checkSql)) {
                checkPs.setString(1, eval.getTitulo());
                checkPs.setDate(2, java.sql.Date.valueOf(eval.getFecha()));
                checkPs.setInt(3, eval.getAsignatura().getId());
                checkPs.setInt(4, eval.getIdProfesor());

                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Ya existe una evaluación activa con esos datos para este profesor.");
                        return false;
                    }
                }
            }

            String checkInactiveSql = "SELECT id_eva FROM evaluacion WHERE descripcion = ? AND fecha = ? AND asignatura_id = ? AND empleado_id = ? AND is_active = 0";
            try (PreparedStatement checkInactivePs = con.prepareStatement(checkInactiveSql)) {
                checkInactivePs.setString(1, eval.getTitulo());
                checkInactivePs.setDate(2, java.sql.Date.valueOf(eval.getFecha()));
                checkInactivePs.setInt(3, eval.getAsignatura().getId());
                checkInactivePs.setInt(4, eval.getIdProfesor());

                try (ResultSet rs = checkInactivePs.executeQuery()) {
                    if (rs.next()) {
                        String updateSql = "UPDATE evaluacion SET is_active = 1 WHERE id_eva = ?";
                        try (PreparedStatement updatePs = con.prepareStatement(updateSql)) {
                            updatePs.setInt(1, rs.getInt("id_eva"));
                            updatePs.executeUpdate();
                            eval.setId(rs.getInt("id_eva"));
                            return true;
                        }
                    }
                }
            }

            String insertSql = "INSERT INTO evaluacion (descripcion, fecha, asignatura_id, empleado_id, is_active) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
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
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    /**
     * Obtiene evaluaciones activas por profesor.
     * @param profesorId ID del profesor.
     * @return Lista de evaluaciones.
     */
    public List<Test> getEvaluacionesByProfesor(int profesorId) {
        List<Test> evaluaciones = new ArrayList<>();
        String sql = """
            SELECT e.id_eva, e.descripcion, e.fecha,
                   e.is_active, a.id, a.name,
                   c.id, c.nivel
            FROM evaluacion e
            INNER JOIN matter a ON e.Asignatura_id = a.id
            INNER JOIN detalle_eva_cur det ON e.id_eva = det.id_eva
            INNER JOIN curso c ON det.id_cur = c.id
            WHERE e.Empleado_id = ? AND e.is_active = 1
            ORDER BY e.fecha DESC;
        """;

        try (Connection con = new connect().getConectar();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, profesorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Matter asig = new Matter(rs.getInt("a.id"), rs.getString("name"));
                    Course curso = new Course(rs.getInt("c.id"), rs.getString("nivel"));

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
    
    /**
     * Desactiva una prueba, eliminando relaciones dependientes.
     * @param test La prueba a desactivar.
     */
    public void deleteTest(Test test) {
        try (Connection con = new connect().getConectar()) {
            // Eliminar relaciones en cascada
            String deleteDetailsQuery = "DELETE FROM detalle_eva_alumno WHERE id_eva = ?";
            try (PreparedStatement delDetailsPs = con.prepareStatement(deleteDetailsQuery)) {
                delDetailsPs.setInt(1, test.getId());
                delDetailsPs.executeUpdate();
            }

            String deleteCurQuery = "DELETE FROM detalle_eva_cur WHERE id_eva = ?";
            try (PreparedStatement delCurPs = con.prepareStatement(deleteCurQuery)) {
                delCurPs.setInt(1, test.getId());
                delCurPs.executeUpdate();
            }

            String query = "UPDATE evaluacion SET is_active = 0 WHERE id_eva = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, test.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}