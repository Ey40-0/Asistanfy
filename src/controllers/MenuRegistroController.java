package controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import models.Empleado;
import models.EmpleadoDAO;
import models.Sesion;

public class MenuRegistroController {
    
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
    
    EmpleadoDAO empc = new EmpleadoDAO();

    @FXML
    public void volver(ActionEvent event) {
        MainController.getInstance().showPanel("/views/menu_inicio.fxml");
    }
    
    @FXML
    public void registrarme(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String contrasenia = txtContrasenia.getText().trim();
        String mail = txtMail.getText().trim();     
        int tipo = typeUser.isSelected() ? 1 : 0;

        if (nombre.isEmpty() || apellido.isEmpty() || contrasenia.isEmpty() || mail.isEmpty()) {
            MainController.getInstance().mostrarAlerta("Campos vacíos", "Por favor, rellene todos los campos.");
            return;
        }
        
        if (!esValido(mail)) {
            MainController.getInstance().mostrarAlerta("Tipo incorrecto", "Email invalido, vuelva a intentar con otro.");
            return;
        }
        
        Empleado emp = new Empleado(0, nombre, apellido, contrasenia, mail, tipo, 1, null);

        if (empc.register(emp)) {
            // mostrarAlerta("Registro exitoso", "¡Usuario registrado correctamente!");
            
            System.out.println("Id: " + emp.getId());
            System.out.println("Nombre: " + emp.getNombre());
            if (emp.getCodigo() != null) {
                System.out.println("Codigo: " + emp.getCodigo());
            }
            System.out.println("Id_rol: " + emp.getTipo());
            
            Sesion.iniciarSesion(emp.getId(), emp.getTipo(), emp.getCodigo());
   
            // Redirigir según rol
            switch (tipo) {
                case 0 -> MainController.getInstance().showPanel("/views/menu_profesor.fxml");
                case 1 -> MainController.getInstance().showPanel("/views/menu_inspector.fxml");
                default -> MainController.getInstance().showPanel("/views/menu_admin.fxml");
            }
        } else {
            MainController.getInstance().mostrarAlerta("Registro fallido", "Ese usuario ya existe o hubo un error.");
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