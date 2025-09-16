package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.Session;
import models.Student;
import models.StudentC;

public class AddStudentsCllr {
    
    @FXML
    private TextField fldName;
    @FXML
    private TextField fldRun;
    
    StudentC stuc = new StudentC();
    
    @FXML
    public void initialize() {
        fldName.requestFocus();
        
        fldName.setOnAction(e -> {
            fldRun.requestFocus(); // pasa el foco al campo de contraseña
        });
        
        fldRun.setOnAction(e -> {
            addStudent(e); // Tu método para iniciar sesión
        });
    }
    
    @FXML
    public void addStudent(ActionEvent event) {
        
        // Recoger los datos del alumno
        String name = fldName.getText().trim();
        String run = fldRun.getText().trim();
        
        // Revisar campos vacios
        if (name.isEmpty() || run.isEmpty()) {
            MainCllr.mostrarAlerta("Campos vacios", "Por favor rellene todos los campos");
            return;
        }
        
        // Validar run (xxxxxxxx-x)
        if (!MainCllr.validarRun(run)) {
            MainCllr.mostrarAlerta("Tipo incorrecto", "Por favor ingrese un run válido.");
            return;
        }
        
        // Instanciar un alumno
        Student stu = new Student(0, run, name, Session.getInstance().getSelectedTest().getCurso());
        
        // Insertar el Alumno en la db
        if (stuc.insert(stu)) {
            System.out.println(stu.toString());
            /*fldName.setText("");
            fldRun.setText("");*/
            GuideCllr.getInstance().loadPanel("/views/ShowStudVw.fxml");
        } else {
            MainCllr.mostrarAlerta("Registro fallido", "Ese usuario ya existe o hubo un error.");
        }
      
    }
    
    @FXML
    public void volver(ActionEvent event) {
        MainCllr.getInstance().showPanel("/views/ShowTestsVw.fxml");
    }
    
}
