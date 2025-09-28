package controllers;

import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.Matter;
import models.Course;
import models.CourseC;
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
    
    @FXML
    public void initialize() {
        fieldDescription.setOnAction(e -> fieldDate.requestFocus());
        
        // Cargar cursos (llamando estático, sin crear objeto)
        fieldCourse.getItems().addAll(CourseC.getCourses());

        // Cargar asignaturas
        fieldMatter.getItems().addAll(Matter.getMatters());
    }
    
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
        
        Test test = new Test(0, description, date, course, matter, Session.getInstance().getId(), 1);
        
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
    
    private void updateTest() {
        
    }
}