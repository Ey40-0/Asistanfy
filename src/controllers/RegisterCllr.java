package controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import models.Employee;
import models.EmployeeC;
import models.Session;

public class RegisterCllr {
    
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private PasswordField txtContrasenia;
    @FXML
    private TextField txtMail;
    @FXML
    private CheckBox typeUser;
    
    EmployeeC empc = new EmployeeC();

    @FXML
    public void volver(ActionEvent event) {
        MainCllr.getInstance().showPanel("/views/LoginVw.fxml");
    }
    
    @FXML
    public void registrarme(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String contrasenia = txtContrasenia.getText().trim();
        String mail = txtMail.getText().trim();     
        int tipo = typeUser.isSelected() ? 1 : 0;

        if (nombre.isEmpty() || apellido.isEmpty() || contrasenia.isEmpty() || mail.isEmpty()) {
            MainCllr.mostrarAlerta("Campos vacíos", "Por favor, rellene todos los campos.");
            return;
        }
        
        if (!esValido(mail)) {
            MainCllr.mostrarAlerta("Tipo incorrecto", "Email invalido, vuelva a intentar con otro.");
            return;
        }
        
        Employee emp = new Employee(0, nombre, apellido, contrasenia, mail, tipo, 1);

        if (empc.register(emp)) {
            // mostrarAlerta("Registro exitoso", "¡Usuario registrado correctamente!");
            
            System.out.println("Id: " + emp.getId());
            System.out.println("Nombre: " + emp.getNombre());
            System.out.println("Id_rol: " + emp.getTipo());
            
            Session.iniciarSesion(emp.getId(), emp.getTipo());
   
            // Redirigir según rol
            switch (tipo) {
                case 0 -> MainCllr.getInstance().showPanel("/views/menu_profesor.fxml");
                case 1 -> MainCllr.getInstance().showPanel("/views/menu_inspector.fxml");
                default -> MainCllr.getInstance().showPanel("/views/menu_admin.fxml");
            }
        } else {
            MainCllr.getInstance().mostrarAlerta("Registro fallido", "Ese usuario ya existe o hubo un error.");
        }
    }
    
    public static boolean esValido(String correo) {
        try {
            InternetAddress email = new InternetAddress(correo);
            email.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }
}