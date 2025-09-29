package models;

import controllers.MainCllr;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import proyectojavafx.connect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StudentC {
    
    /**
     * Inserta un estudiante y lo asocia a una prueba, verificando duplicados por RUT.
     * @param stu El estudiante.
     * @param idTest ID de la prueba.
     * @return true si se insertó/asociado, false en error o duplicado.
     */
    public boolean insert(Student stu, int idTest) {
        try (Connection con = new connect().getConectar()) {
            // Verificar si el alumno ya existe por rut_alum
            int idAlumno = 0;
            String checkStudentQuery = "SELECT id, id_cur FROM alumnos WHERE rut_alum = ?";
            try (PreparedStatement checkStudentStmt = con.prepareStatement(checkStudentQuery)) {
                checkStudentStmt.setString(1, stu.getRut());
                try (ResultSet rs = checkStudentStmt.executeQuery()) {
                    if (rs.next()) {
                        idAlumno = rs.getInt("id");
                        int existingCourseId = rs.getInt("id_cur");
                        // Verificar que el curso sea el mismo
                        if (existingCourseId != stu.getCurso().getId()) {
                            MainCllr.mostrarAlerta("Error al insertar","El alumno ya está registrado en un curso diferente.");
                            return false;
                        }
                        stu.setId(idAlumno);
                    }
                }
            }

            // Si el alumno no existe, insertarlo en la tabla alumnos
            if (idAlumno == 0) {
                String insertStudentQuery = "INSERT INTO alumnos (rut_alum, nombre_alum, id_cur) VALUES (?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(insertStudentQuery, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, stu.getRut());
                    ps.setString(2, stu.getNombre());
                    ps.setInt(3, stu.getCurso().getId());
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            idAlumno = rs.getInt(1);
                            stu.setId(idAlumno);
                        }
                    }
                }
            }

            // Verificar si la evaluación pertenece al curso correcto
            String checkCourseQuery = "SELECT COUNT(*) FROM detalle_eva_cur WHERE id_eva = ? AND id_cur = ?";
            try (PreparedStatement checkCourseStmt = con.prepareStatement(checkCourseQuery)) {
                checkCourseStmt.setInt(1, idTest);
                checkCourseStmt.setInt(2, stu.getCurso().getId());
                try (ResultSet rs = checkCourseStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        System.out.println("La evaluación no está asociada al curso especificado.");
                        return false;
                    }
                }
            }

            // Verificar si el alumno ya tiene la evaluación
            String checkEvalQuery = "SELECT COUNT(*) FROM detalle_eva_alumno WHERE id_alumno = ? AND id_eva = ?";
            try (PreparedStatement checkEvalStmt = con.prepareStatement(checkEvalQuery)) {
                checkEvalStmt.setInt(1, idAlumno);
                checkEvalStmt.setInt(2, idTest);
                try (ResultSet rs = checkEvalStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("El alumno ya tiene esta evaluación.");
                        return false;
                    }
                }
            }

            // Asignar evaluación al alumno
            String assignQuery = "INSERT INTO detalle_eva_alumno (id_eva, id_alumno) VALUES (?, ?)";
            try (PreparedStatement assignStmt = con.prepareStatement(assignQuery)) {
                assignStmt.setInt(1, idTest);
                assignStmt.setInt(2, idAlumno);
                assignStmt.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene estudiantes por ID de prueba.
     * @param testId ID de la prueba.
     * @return Lista de estudiantes.
     */
    public List<Student> getStudentsByTest(int testId) {
        List<Student> students = new ArrayList<>();
        String sql = """
            SELECT a.id, a.rut_alum, a.nombre_alum, 
                   c.id, c.nivel, a.justification
            FROM alumnos a
            JOIN curso c ON a.id_cur = c.id
            JOIN detalle_eva_alumno dea ON dea.id_alumno = a.id
            WHERE dea.id_eva = ?;
        """;

        try (Connection con = new connect().getConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, testId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Course level = new Course(rs.getInt("id"), rs.getString("nivel"));

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
    
    /**
     * Actualiza la justificación de un estudiante.
     * @param studentId ID del estudiante.
     * @param justification Valor de justificación.
     * @return true si se actualizó, false en error.
     */
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
    
    /**
     * Actualiza un estudiante, verificando unicidad de RUT.
     * @param stu El estudiante actualizado.
     * @return true si se actualizó, false si RUT duplicado o error.
     */
    public boolean updateStudent(Student stu) {
        try (Connection con = new connect().getConectar()) {
            String checkQuery = "SELECT id FROM alumnos WHERE rut_alum = ? AND id != ?";
            try (PreparedStatement checkPs = con.prepareStatement(checkQuery)) {
                checkPs.setString(1, stu.getRut());
                checkPs.setInt(2, stu.getId());
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Ya existe otro alumno activo con el RUT: " + stu.getRut());
                        return false;
                    }
                }
            }

            String query = "UPDATE alumnos SET rut_alum = ?, nombre_alum = ? WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, stu.getRut());
                ps.setString(2, stu.getNombre());
                ps.setInt(3, stu.getId());
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("Alumno actualizado correctamente.");
                    return true;
                } else {
                    System.out.println("No se encontró el alumno con ID: " + stu.getId());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
}