package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Course;
import models.CourseC;

public class AddCourseCllr {
    
    @FXML
    private TextField fldName;
    @FXML
    private ListView<Course> listCourses;
    
    CourseC couc = new CourseC();
    
    @FXML
    public void initialize() {
        loadCourses();
    }
    
    public void register() {
        String name = fldName.getText().trim();
        
        if (name.isEmpty()) {
            MainCllr.mostrarAlerta("Campos vacios", "Por favor rellene todos los campos");
        }
        
        Course cou = new Course(0, name);
        if (couc.addNewCourse(cou)) {
            fldName.setText("");
            loadCourses();
        }
    }
    
    private void loadCourses() {
        // Llama al método en CourseC para obtener una lista
        listCourses.setItems(CourseC.getCourses());
        
        // El CellFactory asigna una configuración en la vista
        listCourses.setCellFactory(lv -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course cou, boolean empty) {
                super.updateItem(cou, empty);
                if (empty || cou == null) {
                    setText(null);
                } else {
                    setText(cou.getNombre());
                }
            }
        });
    }
    
}
