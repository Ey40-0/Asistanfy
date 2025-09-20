package models;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import proyectojavafx.connect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StudentC {
    
    public boolean insert(Student stu, int idTest) {
        try (Connection con = new connect().getConectar()) {

            // Verificar si ya existe el alumno con esa evaluación
            String checkQuery = """
                SELECT COUNT(*) FROM detalle_eva_alumno dea
                JOIN alumnos a ON dea.id_alumno = a.id
                WHERE a.rut_alum = ? AND dea.id_eva = ?""";
            try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
                checkStmt.setString(1, stu.getRut());
                checkStmt.setInt(2, idTest);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("Ese usuario ya tiene esta evaluación asignada");
                        return false;
                    }
                }
            }

            // Insertar alumno si no existe
            String query = "INSERT INTO alumnos (rut_alum, nombre_alum, id_cur) VALUES (?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, stu.getRut());
                ps.setString(2, stu.getNombre());
                ps.setInt(3, stu.getCurso().getId());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        stu.setId(rs.getInt(1));
                    }
                }
            }

            // Asignar evaluación al alumno
            String assignQuery = "INSERT INTO detalle_eva_alumno (id_eva, id_alumno) VALUES (?, ?)";
            try (PreparedStatement assignStmt = con.prepareStatement(assignQuery)) {
                assignStmt.setInt(1, idTest);
                assignStmt.setInt(2, stu.getId());
                assignStmt.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Student> getStudentsByTest(int testId) {
        List<Student> students = new ArrayList<>();
        String sql = """
            SELECT a.id, a.rut_alum, a.nombre_alum, 
                   c.id_cur, c.nivel, a.justification
            FROM alumnos a
            JOIN curso c ON a.id_cur = c.id_cur
            JOIN detalle_eva_alumno dea ON dea.id_alumno = a.id
            WHERE dea.id_eva = ?;
        """;

        try (Connection con = new connect().getConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, testId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Course level = new Course(rs.getInt("id_cur"), rs.getString("nivel"));

                    Student stud = new Student(
                        rs.getInt("id"),
                        rs.getString("rut_alum"),
                        rs.getString("nombre_alum"),
                        level
                    );
                    stud.setJustification(rs.getBoolean("justification"));

                    students.add(stud);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return students;
    }
    
    public boolean updateJustification(int studentId, boolean justification) {
        String query = "UPDATE alumnos SET justification = ? WHERE id = ?";
        try (Connection con = new connect().getConectar();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setBoolean(1, justification);
            ps.setInt(2, studentId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
