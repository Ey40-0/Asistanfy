package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddStudentsCllr {
    
    @FXML
    private TextField fldName;
    @FXML
    private TextField fldRun;
    
    @FXML
    public void initialize() {
        
    }
    
    public void addStudent() {
        
        // Recoger los datos del alumno
        String name = fldName.getText().trim();
        String run = fldRun.getText().trim();
        
        // Validar run (xx.xxx.xxx-x)
        if (!MainCllr.validarRun(run)) {
            MainCllr.mostrarAlerta("Tipo incorrecto", "Por favor ingrese un run válido.");
            return;
        }
        
        // Revisar campos vacios
        if (name.isEmpty() || run.isEmpty()) {
            MainCllr.mostrarAlerta("Campos vacios", "Por favor rellene todos los campos");
            return;
        }
        
        // Instanciar un alumno
        // Alumno stu = new Alumno(0, name, run, );
        fldName.setText("");
        fldRun.setText("");
        GuideCllr.getInstance().loadPanel("views/ShowStudVw.fxml");
    }
    
}
