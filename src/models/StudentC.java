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
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class StudentC {
    
    /**
     * Inserta un estudiante y lo asocia a una prueba, verificando duplicados por RUT.
     * @param stu El estudiante.
     * @param idTest ID de la prueba.
     * @return true si se insertó/asociado, false en error o duplicado.
     */
    public boolean insert(Student stu, int idTest) {
        try (Connection con = new connect().getConectar()) {
            int idAlumnoExistente = 0;
            int cursoExistente = 0;

            // Verificar si el alumno ya existe por RUT
            String buscarAlumno = "SELECT id, id_cur FROM alumnos WHERE rut_alum = ?";
            try (PreparedStatement ps = con.prepareStatement(buscarAlumno)) {
                ps.setString(1, stu.getRut());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idAlumnoExistente = rs.getInt("id");
                        cursoExistente = rs.getInt("id_cur");
                    }
                }
            }

            // Obtener la materia de la evaluación
            int idAsignaturaEvaluacion = 0;
            String obtenerAsignatura = "SELECT Asignatura_id FROM evaluacion WHERE id_eva = ?";
            try (PreparedStatement ps = con.prepareStatement(obtenerAsignatura)) {
                ps.setInt(1, idTest);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idAsignaturaEvaluacion = rs.getInt("Asignatura_id");
                    } else {
                        MainCllr.mostrarAlerta("Error", "Evaluación no encontrada.");
                        return false;
                    }
                }
            }

            // Si ya existe un alumno con el mismo RUT
            if (idAlumnoExistente != 0) {
                // Obtener todas las materias de evaluaciones del alumno existente
                String materiasAlumno = """
                    SELECT DISTINCT e.Asignatura_id 
                    FROM evaluacion e 
                    JOIN detalle_eva_alumno dea ON e.id_eva = dea.id_eva 
                    WHERE dea.id_alumno = ?
                """;

                List<Integer> materias = new ArrayList<>();
                try (PreparedStatement ps = con.prepareStatement(materiasAlumno)) {
                    ps.setInt(1, idAlumnoExistente);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            materias.add(rs.getInt("Asignatura_id"));
                        }
                    }
                }

                if (cursoExistente == stu.getCurso().getId()) {
                    // Mismo curso
                    if (materias.contains(idAsignaturaEvaluacion)) {
                        // Misma materia -> NO insertar
                        MainCllr.mostrarAlerta("Duplicado", "El alumno ya está registrado con esta materia en este curso.");
                        return false;
                    } else {
                        // Forzar nuevo registro de alumno
                        idAlumnoExistente = 0; // Forzar nuevo registro de alumno
                    }
                } else {
                    // Curso distinto (¿Repitió? ¿Subió de nivel?)
                    // Preguntar al usuario si quiere crear un nuevo registro
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Curso diferente");
                    alert.setHeaderText("El alumno ya está registrado en otro curso (" + cursoExistente + ").");
                    alert.setContentText("¿Desea agregarlo nuevamente para el curso " + stu.getCurso().getId() + "?");

                    // Botones personalizados
                    ButtonType buttonSi = new ButtonType("Si", ButtonBar.ButtonData.YES);
                    ButtonType buttonNo = new ButtonType("No", ButtonBar.ButtonData.NO);
                    alert.getButtonTypes().setAll(buttonSi, buttonNo);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isEmpty() || result.get() == buttonNo) {
                        MainCllr.mostrarAlerta("Cancelado", "El usuario canceló la operación.");
                        return false;
                    }

                    // Insertar nuevo registro
                    idAlumnoExistente = 0; // Forzar nuevo
                }
            }

            // Insertar nuevo alumno si es necesario
            if (idAlumnoExistente == 0) {
                String insertAlumno = "INSERT INTO alumnos (rut_alum, nombre_alum, id_cur) VALUES (?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(insertAlumno, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, stu.getRut());
                    ps.setString(2, stu.getNombre());
                    ps.setInt(3, stu.getCurso().getId());
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            int newId = rs.getInt(1);
                            stu.setId(newId);
                        } else {
                            MainCllr.mostrarAlerta("Error", "No se pudo obtener el ID del nuevo alumno.");
                            return false;
                        }
                    }
                }
            }

            // Verificar si evaluación está asociada al curso del alumno
            String verificarCursoEvaluacion = "SELECT COUNT(*) FROM detalle_eva_cur WHERE id_eva = ? AND id_cur = ?";
            try (PreparedStatement ps = con.prepareStatement(verificarCursoEvaluacion)) {
                ps.setInt(1, idTest);
                ps.setInt(2, stu.getCurso().getId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        MainCllr.mostrarAlerta("Error", "La evaluación no está asociada al curso del alumno.");
                        return false;
                    }
                }
            }

            // Verificar si ya tiene esta evaluación
            String verificarDuplicado = "SELECT COUNT(*) FROM detalle_eva_alumno WHERE id_alumno = ? AND id_eva = ?";
            try (PreparedStatement ps = con.prepareStatement(verificarDuplicado)) {
                ps.setInt(1, stu.getId());
                ps.setInt(2, idTest);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        MainCllr.mostrarAlerta("Error", "Este alumno ya tiene asignada esta evaluación.");
                        return false;
                    }
                }
            }

            // Asignar evaluación al alumno
            String asignar = "INSERT INTO detalle_eva_alumno (id_eva, id_alumno) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(asignar)) {
                ps.setInt(1, idTest);
                ps.setInt(2, stu.getId());
                ps.executeUpdate();
            }

            // MainCllr.mostrarAlerta("Éxito", "Alumno registrado y evaluación asignada correctamente.");
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            MainCllr.mostrarAlerta("Error de BD", "Fallo al registrar alumno: " + e.getMessage());
            return false;
        }
    }

    // Método auxiliar: Valida si las materias son distintas (el alumno no tiene evaluaciones previas en esta materia)
    private boolean areMateriasDistintas(int idAlumno, int idTest, Connection con) throws SQLException {
        // Obtener la materia de la prueba actual
        String getTestMateriaQuery = "SELECT e.id_asig FROM evaluaciones e WHERE e.id = ?"; // Asume tabla 'evaluaciones' con campo 'id_asig' para materia
        int testMateriaId = 0;
        try (PreparedStatement stmt = con.prepareStatement(getTestMateriaQuery)) {
            stmt.setInt(1, idTest);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    testMateriaId = rs.getInt("id_asig");
                }
            }
        }

        if (testMateriaId == 0) {
            // Si no se puede obtener la materia, asumir que son distintas por defecto
            return true;
        }

        // Verificar si el alumno ya tiene evaluaciones en esta materia (obtener materias de sus evaluaciones previas)
        String checkStudentMateriasQuery = """
            SELECT DISTINCT e.id_asig 
            FROM evaluaciones e 
            INNER JOIN detalle_eva_alumno dea ON e.id = dea.id_eva 
            WHERE dea.id_alumno = ? AND e.id_asig = ?
            """;
        try (PreparedStatement stmt = con.prepareStatement(checkStudentMateriasQuery)) {
            stmt.setInt(1, idAlumno);
            stmt.setInt(2, testMateriaId);
            try (ResultSet rs = stmt.executeQuery()) {
                // Si hay resultados, significa que ya tiene en esta materia (NO son distintas)
                return !rs.next(); // Retorna true si NO hay resultados (materias distintas)
            }
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
    
    
    public String studentMostLeft(int curId) {
        String student = "N/A"; // valor por defecto
        String sql = """
            SELECT a.nombre_alum as nombre, a.rut_alum, COUNT(*) AS total_inasistencias
            FROM alumnos a
            JOIN detalle_eva_alumno dea ON dea.id_alumno = a.id
            JOIN evaluacion e ON dea.id_eva = e.id_eva
            WHERE a.id_cur = ?
              AND YEAR(e.fecha) = YEAR(CURDATE())
            GROUP BY a.rut_alum, a.nombre_alum
            ORDER BY total_inasistencias DESC
            LIMIT 1;
        """;

        try (Connection con = new connect().getConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, curId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // chequeo si hay resultado
                    student = rs.getString("nombre");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return student;
    }
    
    public ObservableList<Student> getAllStudents() {
        ObservableList<Student> students = FXCollections.observableArrayList();
        String sql = """
            -- Mostrar todos los alumnos
            SELECT a.*, c.id AS id_curso, c.nivel AS nombre_curso
            FROM alumnos a
            JOIN (
                SELECT rut_alum, MIN(id) AS min_id
                FROM alumnos
                GROUP BY rut_alum
            ) AS uniq 
                ON a.rut_alum = uniq.rut_alum 
                AND a.id = uniq.min_id
            JOIN curso c 
                ON a.id_cur = c.id;
        """;

        try (Connection con = new connect().getConectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Course level = new Course(
                    rs.getInt("id_curso"),
                    rs.getString("nombre_curso")
                );

                Student stud = new Student(
                    rs.getInt("id"),
                    rs.getString("rut_alum"),
                    rs.getString("nombre_alum"),
                    level
                );

                stud.setJustification(rs.getBoolean("justification"));

                students.add(stud);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return students;
    }
    
    public static int countLefts(int stuId) {
        int total = 0; // valor por defecto
        String sql = """
            SELECT 
                a.nombre_alum AS nombre,
                a.rut_alum,
                COUNT(*) AS total_inasistencias
            FROM alumnos a
            JOIN detalle_eva_alumno dea ON dea.id_alumno = a.id
            JOIN evaluacion e ON dea.id_eva = e.id_eva
            WHERE a.id = ? 
              AND YEAR(e.fecha) = YEAR(CURDATE())
            GROUP BY a.rut_alum, a.nombre_alum;
        """;

        try (Connection con = new connect().getConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, stuId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // chequeo si hay resultado
                    total = rs.getInt("inasistencias");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
    
    public static int countJustificated(int stuId) {
        int total = 0; // valor por defecto
        String sql = """
            SELECT 
                a.nombre_alum AS nombre,
                a.rut_alum,
                a.justification,
                COUNT(*) AS total_inasistencias
            FROM alumnos a
            JOIN detalle_eva_alumno dea ON dea.id_alumno = a.id
            JOIN evaluacion e ON dea.id_eva = e.id_eva
            WHERE a.id = ?
              AND YEAR(e.fecha) = YEAR(CURDATE())
            GROUP BY a.rut_alum, a.nombre_alum, a.justification;
        """;

        try (Connection con = new connect().getConectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, stuId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) { // chequeo si hay resultado
                    total = rs.getInt("inasistencias");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
}