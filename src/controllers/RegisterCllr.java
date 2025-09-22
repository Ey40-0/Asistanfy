package controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
    
    @FXML
    private ListView<Employee> listProfesores;
    
    EmployeeC empc = new EmployeeC();
    
    @FXML
    public void initialize() {
        txtNombre.setOnAction(e -> {txtApellido.requestFocus();});
        txtApellido.setOnAction(e -> {txtContrasenia.requestFocus();});
        txtApellido.setOnAction(e -> {txtMail.requestFocus();});
        loadTeachers();
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
            //MainCllr.mostrarAlerta("Registro exitoso", "¡Usuario registrado correctamente!");
            txtNombre.setText("");
            txtApellido.setText("");
            txtContrasenia.setText("");
            txtMail.setText("");
            typeUser.setSelected(false);
            loadTeachers();
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
    
    public void loadTeachers() {
        // Obtiene la lista de profesores de forma centralizadas
        listProfesores.setItems(empc.getAllEmps());
        
        // El CellFactory, que es una lógica de presentación,
        // está aquí porque es específico de esta vista.
        listProfesores.setCellFactory(lv -> new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee emp, boolean empty) {
                super.updateItem(emp, empty);
                if (empty || emp == null) {
                    setText(null);
                } else {
                    if (emp.getTipo() == 0) {
                        setText(emp.getNombre() + " " + emp.getApellido() + " - Profesor");
                    } else {
                        setText(emp.getNombre() + " " + emp.getApellido() + " - Inspector");
                    }
                }
            }
        });
    }
    
    public void viewInformation() {
        Employee seleccionado = listProfesores.getSelectionModel().getSelectedItem();

        if (seleccionado != null && seleccionado.getTipo() != 10.) {
            try {
                // Guardar el ID del empleado seleccionado en la sesión
                Session.getInstance().setSelectedEmployeeId(seleccionado.getId());

                // Cargar el nuevo panel
                GuideCllr.getInstance().loadPanel("/views/ShowTestsVw.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MainCllr.mostrarAlerta("Error", "Por favor selecciona un profesor para ver sus evaluaciones.");
        }
    }
    
    public int getInfoEmpleado() {
        Employee selected = listProfesores.getSelectionModel().getSelectedItem();
        if (selected != null) {
            return selected.getId();
        } else {
            return -1; // o cualquier valor que indique "ninguna selección"
        }
    }
}