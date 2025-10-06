package controllers;

import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.Matter;
import models.Course;
import models.CourseC;
import models.MatterC;
import models.Test;
import models.TestC;
import models.Session;

public class AddTestCllr {
    
    @FXML
    private TextField fieldDescription; 
    @FXML
    private DatePicker fieldDate;
    @FXML
    private ComboBox<Matter> fieldMatter;
    @FXML
    private ComboBox<Course> fieldCourse;
    
    TestC evac = new TestC();
    CourseC curc = new CourseC();
    
    /**
     * Inicializa el controlador, cargando cursos y materias en los ComboBox.
     */
    @FXML
    public void initialize() {
        fieldDescription.setOnAction(e -> fieldDate.requestFocus());
        
        fieldCourse.getItems().addAll(CourseC.getCourses());

        fieldMatter.getItems().addAll(MatterC.getMatters());
    }
    
    /**
     * Crea una nueva prueba en la base de datos y la asocia al curso.
     */
    public void createTest() {
        String description = fieldDescription.getText().trim();
        LocalDate date = fieldDate.getValue();
        Matter matter = fieldMatter.getValue();
        Course course = fieldCourse.getValue();
        
        if (description.isEmpty() || date == null || matter == null || course == null) {
            MainCllr.mostrarAlerta("Campos vacíos", "Por favor, rellene todos los campos.");
            return;
        }
        
        if (date.isBefore(LocalDate.now())) {
            MainCllr.mostrarAlerta("Fecha invalida", "Por favor, ingrese una fecha válida");
            return;
        }
        
        Test test = new Test(0, description, date, course, matter, Session.getInstance().getEmployee().getId(), 1);
        
        if (evac.insert(test)) {
            MainCllr.mostrarAlerta("Registro exitoso", "¡Evaluación registrada correctamente!");
            fieldDescription.clear();
            fieldDate.setValue(null);
            fieldMatter.setValue(null);
            fieldCourse.setValue(null);
            curc.addTestToCourse(course, test);
        } else {
            MainCllr.mostrarAlerta("Prueba Duplicada", "Ya existe una evaluación con ese nombre en esas credenciales.");
        }
    }
    
    /**
     * Actualiza una prueba existente (no implementado aún).
     */
    private void updateTest() {
        
    }
}