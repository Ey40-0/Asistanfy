package controllers;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

public class AddEmployeesCllr {
    
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtFieldPassword;
    @FXML private Button btnToggle;
    @FXML private TextField txtMail;
    @FXML private CheckBox typeUser;
    @FXML private TextField fldSearch;
    @FXML private ListView<Employee> listProfesores;
    
    EmployeeC empc = new EmployeeC();
    private FilteredList<Employee> filteredEmployees;  
    private Employee selected;
    private boolean showing = false;
    
    @FXML
    public void initialize() {
        txtFieldPassword.textProperty().bindBidirectional(txtPassword.textProperty());

        txtNombre.setOnAction(e -> txtApellido.requestFocus());
        txtApellido.setOnAction(e -> txtPassword.requestFocus());
        txtPassword.setOnAction(e -> txtMail.requestFocus());

        // Listener de selección
        listProfesores.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            selected = newSel;
            if (selected != null) {
                showEmployeeData();
            } else {
                clearFields();
            }
        });

        // Listener para búsqueda
        fldSearch.textProperty().addListener((obs, oldValue, newValue) -> {
            String filtro = (newValue == null) ? "" : newValue.toLowerCase();

            filteredEmployees.setPredicate(emp -> {
                if (filtro.isEmpty()) return true;

                String nombreCompleto = emp.getNombre().toLowerCase() + " " + emp.getApellido().toLowerCase();
                String tipo = (emp.getTipo() == 0) ? "profesor" : "inspector";

                return nombreCompleto.contains(filtro) || tipo.contains(filtro);
            });
        });

        loadEmployees();
    }

    @FXML
    private void createEmployee() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String contrasenia = txtPassword.getText().trim();
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
        
        Employee emp = new Employee(0, nombre, apellido, mail, contrasenia, tipo, 1);

        if (empc.register(emp)) {
            //MainCllr.mostrarAlerta("Registro exitoso", "¡Usuario registrado correctamente!");
            clearFields();
            loadEmployees();
        } else {
            MainCllr.getInstance().mostrarAlerta("Registro fallido", "Ese usuario ya existe o hubo un error.");
        }
    }
    
        @FXML
    private void deleteEmployee() {
        if (selected != null) {
            empc.deleteEmployee(selected.getId());
        } else {
            MainCllr.mostrarAlerta("Error", "Por favor selecciona un profesor.");
        } 
        listProfesores.getSelectionModel().clearSelection();
        selected = null;
        loadEmployees();    
    }
    
    @FXML
    private void updateEmployee() {
        if (selected != null) {
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String contrasenia = txtPassword.getText().trim();
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

            Employee emp = new Employee(selected.getId(), nombre, apellido, mail, contrasenia, tipo, 1);
            if (empc.updateEmployee(emp)) {
                clearFields();
                loadEmployees();
            } else {
                MainCllr.getInstance().mostrarAlerta("Registro fallido", "Ese usuario ya existe o hubo un error.");
            }
            
        } else {
            MainCllr.mostrarAlerta("Error", "Por favor selecciona un profesor.");
        } 
        listProfesores.getSelectionModel().clearSelection();
        selected = null;
        loadEmployees(); 
    }
    
    private static boolean esValido(String correo) {
        try {
            InternetAddress email = new InternetAddress(correo);
            email.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }
    
    private void loadEmployees() {
        ObservableList<Employee> empleados = empc.getAllEmps();

        // Creamos la lista filtrable
        filteredEmployees = new FilteredList<>(empleados, e -> true);

        // Asignamos al ListView
        listProfesores.setItems(filteredEmployees);

        // Cell factory personalizado
        listProfesores.setCellFactory(lv -> new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee emp, boolean empty) {
                super.updateItem(emp, empty);
                if (empty || emp == null) {
                    setText(null);
                } else {
                    String tipo = (emp.getTipo() == 0) ? "Profesor" : "Inspector";
                    setText(emp.getNombre() + " " + emp.getApellido() + " - " + tipo);
                }
            }
        });
    }
    
    @FXML
    private void viewInformation() {
        if (selected != null && selected.getTipo() != 1) {
            try {
                // Guardar el ID del empleado seleccionado en la sesión
                Session.getInstance().setSelectedEmployeeId(selected.getId());

                // Cargar el nuevo panel
                GuideCllr.getInstance().loadPanel("/views/ShowTestsVw.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MainCllr.mostrarAlerta("Error", "Por favor selecciona un profesor para ver sus evaluaciones.");
        }
    }
    
    private void showEmployeeData() {
        if (selected != null) {
            txtNombre.setText(selected.getNombre());
            txtApellido.setText(selected.getApellido());
            txtMail.setText(selected.getEmail());
            txtPassword.setText(selected.getContrasenia());
            typeUser.setSelected(selected.getTipo() == 1);
        }
    }
    
    @FXML
    private void onToggleShow() {
        showing = !showing;
        if (showing) {
            // Mostrar el TextField con el texto claro
            txtFieldPassword.setVisible(true);
            txtFieldPassword.setManaged(true);
            txtPassword.setVisible(false);
            txtPassword.setManaged(false);
            btnToggle.setText("Ocultar");
            // opcional: colocar el foco y mover caret al final
            txtFieldPassword.requestFocus();
            txtFieldPassword.positionCaret(txtFieldPassword.getText().length());
        } else {
            // Volver a ocultar (PasswordField)
            txtFieldPassword.setVisible(false);
            txtFieldPassword.setManaged(false);
            txtPassword.setVisible(true);
            txtPassword.setManaged(true);
            btnToggle.setText("Mostrar");
            txtPassword.requestFocus();
            txtPassword.positionCaret(txtPassword.getText().length());
        }
    }
    
    private void clearFields() {
        txtNombre.clear();
        txtApellido.clear();
        txtPassword.clear();
        txtFieldPassword.clear(); // si es visible también
        txtMail.clear();
        typeUser.setSelected(false);
    }
}