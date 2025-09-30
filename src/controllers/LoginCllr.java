package controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Employee;
import models.EmployeeC;
import models.Session;

public class LoginCllr {
    
    @FXML
    private TextField str_user;
    @FXML
    private PasswordField str_contrasenia;
    
    EmployeeC empc = new EmployeeC();
    
    /**
     * Inicializa el controlador, configurando foco en campos.
     */
    @FXML
    public void initialize() {
        str_user.setOnAction(e -> {str_contrasenia.requestFocus();});
        str_contrasenia.setOnAction(e -> {btn_siguiente(e);});
    }
    
    /**
     * Procesa el login del usuario.
     * @param event Evento de acción.
     */
    @FXML
    public void btn_siguiente(ActionEvent event) {
        String usuario = str_user.getText().trim();
        String contrasenia = str_contrasenia.getText().trim();

        if (usuario.isEmpty() || contrasenia.isEmpty()) {
            MainCllr.mostrarAlerta("Campos vacíos", "Por favor, rellene todos los campos.");
            return;
        }

        Employee emp = empc.login(usuario, contrasenia);
        if (emp != null) {
             
            System.out.println(emp.toString());
            
            Session.iniciarSesion(emp);
            
            switch (emp.getTipo()) {
                case 0 -> MainCllr.getInstance().showPanel("/views/TeacherVw.fxml");
                case 1 -> MainCllr.getInstance().showPanel("/views/InspectorVw.fxml");
                default -> MainCllr.getInstance().showPanel("/views/AdminVw.fxml");
            }
        } else {
            cleanInputs();
            MainCllr.mostrarAlerta("Error", "Usuario o contraseña incorrecta.");
        }
    }
    
    /**
     * Limpia los campos de usuario y contraseña.
     */
    private void cleanInputs() {
        str_user.setText("");
        str_contrasenia.setText("");
    }
}