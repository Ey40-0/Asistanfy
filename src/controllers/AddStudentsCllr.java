package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.Session;
import models.Student;
import models.StudentC;

public class AddStudentsCllr {
    
    @FXML private TextField fldName;
    @FXML private TextField fldRun;
    
    StudentC stuc = new StudentC();
    
    /**
     * Inicializa el controlador, cargando datos si hay estudiante seleccionado.
     */
    @FXML
    public void initialize() {
        if (Session.getInstance().getSelectedStud() != null) {
            fldName.setText(Session.getInstance().getSelectedStud().getNombre());
            fldRun.setText(Session.getInstance().getSelectedStud().getRut());
        } else {
            fldName.requestFocus();
            fldName.setOnAction(e -> { fldRun.requestFocus(); });
            fldRun.setOnAction(e -> { addStudent(e); });
        }
    }
    
    /**
     * Añade un nuevo estudiante asociado a la prueba seleccionada.
     * @param event Evento de acción.
     */
    @FXML
    public void addStudent(ActionEvent event) {
        
        String name = fldName.getText().trim();
        String run = fldRun.getText().trim();
        
        if (name.isEmpty() || run.isEmpty()) {
            MainCllr.mostrarAlerta("Campos vacios", "Por favor rellene todos los campos");
            return;
        }
        
        if (!MainCllr.validarRun(run)) {
            MainCllr.mostrarAlerta("Tipo incorrecto", "Por favor ingrese un run válido.");
            return;
        }
        
        Student stu = new Student(0, MainCllr.formatearRun(run), name, Session.getInstance().getSelectedTest().getCurso());
        if (stuc.insert(stu, Session.getInstance().getSelectedTest().getId())) {
            fldName.setText("");
            fldRun.setText("");
            GuideCllr.getInstance().loadPanel("/views/ShowStudVw.fxml");
        } else {
            MainCllr.mostrarAlerta("Registro fallido", "Ese usuario ya existe o hubo un error.");
        }
      
    }
    
    /**
     * Carga el panel para ver estudiantes.
     */
    public void btnView() {
        GuideCllr.getInstance().loadPanel("/views/ShowStudVw.fxml");
    }
    
    /**
     * Actualiza el estudiante seleccionado en la base de datos.
     */
    @FXML
    private void updateStudent() {
        String name = fldName.getText().trim();
        String run = fldRun.getText().trim();
            
        if (name.isEmpty()) {
            MainCllr.mostrarAlerta("Campos vacios", "Por favor rellene todos los campos.");
            return;
        }
        
        Student selected = Session.getInstance().getSelectedStud();
        if (selected == null) {
            MainCllr.mostrarAlerta("Error", "No hay alumno seleccionado para actualizar.");
            return;
        }
        
        if (!MainCllr.validarRun(run)) {
            MainCllr.mostrarAlerta("Tipo incorrecto", "Por favor ingrese un run válido.");
            return;
        }
            
        Student stu = new Student();
        stu.setId(selected.getId());
        stu.setNombre(name);
        stu.setRut(MainCllr.formatearRun(run));
        stu.setCurso(Session.getInstance().getSelectedTest().getCurso());

        if (stuc.updateStudent(stu)) {
            fldName.setText("");
            Session.getInstance().setSelectedStud(null); // Limpieza de sesión
        } else {
            MainCllr.mostrarAlerta("Error", "Ha ocurrido un error al actualizar.");
        }
    }

}