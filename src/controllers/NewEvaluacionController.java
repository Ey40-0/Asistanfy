package controllers;

import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.Asignatura;
import models.AsignaturaDAO;
import models.Curso;
import models.CursoDAO;
import models.Evaluacion;
import models.EvaluacionDAO;
import models.Sesion;

public class NewEvaluacionController {
    
    @FXML
    private TextField fieldDescription; 
    @FXML
    private DatePicker fieldDate;
    @FXML
    private ComboBox<Asignatura> fieldMatter;
    @FXML
    private ComboBox<Curso> fieldCourse;
    
    EvaluacionDAO evac = new EvaluacionDAO();
    CursoDAO curc = new CursoDAO();
    
    @FXML
    private void initialize() {
        fieldDescription.setOnAction(e -> fieldDate.requestFocus());
        
        // Cargar cursos (llamando estático, sin crear objeto)
        fieldCourse.getItems().addAll(CursoDAO.obtenerCursos());

        // Cargar asignaturas
        fieldMatter.getItems().addAll(AsignaturaDAO.obtenerAsignaturas());
    }
    
    public void createTest() {
        String description = fieldDescription.getText().trim();
        LocalDate date = fieldDate.getValue();
        Asignatura matter = fieldMatter.getValue();
        Curso course = fieldCourse.getValue();
        
        if (description.isEmpty() || date == null || matter == null || course == null) {
            MainController.getInstance().mostrarAlerta("Campos vacíos", "Por favor, rellene todos los campos.");
            return;
        }
        
        if (date.isBefore(LocalDate.now())) {
            MainController.getInstance().mostrarAlerta("Fecha invalida", "Por favor, ingrese una fecha válida");
            return;
        }
        
        Evaluacion mat = new Evaluacion(0, description, date, course, matter, Sesion.getInstance().getId());
        
        if (evac.insert(mat)) {
            MainController.getInstance().mostrarAlerta("Registro exitoso", "¡Evaluación registrada correctamente!");
            fieldDescription.clear();
            fieldDate.setValue(null);
            fieldMatter.setValue(null);
            fieldCourse.setValue(null);
            curc.addMatterToCourse(course, mat);
        } else {
            MainController.getInstance().mostrarAlerta("Prueba Duplicada", "Ya existe una evaluación con ese nombre en esas credenciales.");
        }
    }
    
}