package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import models.Alumno;
import models.AlumnoDAO;
import models.Curso;
import models.CursoDAO;

public class AlumnosController {
    
    // Tomar campos del FXML
    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldRun;
    @FXML
    private ComboBox<Curso> fieldCourse;

    // Crear instancia de AlumnoDAO (DataAccessObject)
    AlumnoDAO almc = new AlumnoDAO();
    
    private void initialize() {
        // Cargar cursos (llamando estático, sin crear objeto)
        fieldCourse.getItems().addAll(CursoDAO.obtenerCursos());
    }
        
    public void createStudent() {
        
        // Asignar valores a variables
        String name = fieldName.getText().trim();
        String run = fieldName.getText().trim();
        Curso course = fieldCourse.getValue();
        
        // Validar run (xx.xxx.xxx-x)
        if (!MainController.validarRun(run)) {
            MainController.getInstance().mostrarAlerta("Tipo incorrecto", "Por favor ingrese un run válido.");
        }
        
        // Instanciar alumno
        Alumno stu = new Alumno(0, name, run, course);
        
        // Añadirlo a la base de datos
        if (almc.insert(stu)) {
            fieldName.setText("");
            fieldRun.setText("");
            fieldCourse.setValue(null);   
        }
    }   
    
}
