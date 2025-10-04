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
    
    /**
     * Inicializa el controlador, configurando bindings y listeners.
     */
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

    /**
     * Crea un nuevo empleado en la base de datos.
     */
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
            clearFields();
            loadEmployees();
        } else {
            MainCllr.mostrarAlerta("Registro fallido", "Ese usuario ya existe o hubo un error.");
        }
    }
    
    /**
     * Elimina el empleado seleccionado (setea activa=0).
     */
    @FXML
    private void deleteEmployee() {
        if (selected != null) {
            empc.deleteEmployee(selected.getId());
            listProfesores.getSelectionModel().clearSelection();
            selected = null;
            loadEmployees();
        } else {
            MainCllr.mostrarAlerta("Error", "Por favor selecciona un profesor.");
        } 
    }
    
    /**
     * Actualiza el empleado seleccionado en la base de datos.
     */
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
                MainCllr.mostrarAlerta("Registro fallido", "Ese usuario ya existe o hubo un error.");
            }
            
        } else {
            MainCllr.mostrarAlerta("Error", "Por favor selecciona un profesor.");
        } 
        listProfesores.getSelectionModel().clearSelection();
        selected = null;
        loadEmployees(); 
    }
    
    /**
     * Valida si un email es válido usando InternetAddress.
     * @param correo El email a validar.
     * @return true si es válido, false en caso contrario.
     */
    private static boolean esValido(String correo) {
        try {
            InternetAddress email = new InternetAddress(correo);
            email.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }
    
    /**
     * Carga la lista de empleados desde la base de datos.
     */
    private void loadEmployees() {
        ObservableList<Employee> empleados = empc.getAllEmps();

        filteredEmployees = new FilteredList<>(empleados, e -> true);

        listProfesores.setItems(filteredEmployees);

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
    
    /**
     * Muestra la información del empleado seleccionado (carga panel de tests).
     */
    @FXML
    private void viewInformation() {
        if (selected != null && selected.getTipo() != 1) {
            try {
                Session.getInstance().setSelectedEmployeeId(selected.getId());
                GuideCllr.getInstance().loadPanel("/views/ShowTestsVw.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MainCllr.mostrarAlerta("Error", "Por favor selecciona un profesor para ver sus evaluaciones.");
        }
    }
    
    /**
     * Muestra los datos del empleado seleccionado en los campos.
     */
    private void showEmployeeData() {
        if (selected != null) {
            txtNombre.setText(selected.getNombre());
            txtApellido.setText(selected.getApellido());
            txtMail.setText(selected.getEmail());
            txtPassword.setText("");
            typeUser.setSelected(selected.getTipo() == 1);
        }
    }
    
    /**
     * Alterna entre mostrar y ocultar la contraseña.
     */
    @FXML
    private void onToggleShow() {
        showing = !showing;
        if (showing) {
            txtFieldPassword.setVisible(true);
            txtFieldPassword.setManaged(true);
            txtPassword.setVisible(false);
            txtPassword.setManaged(false);
            btnToggle.setText("Ocultar");
            txtFieldPassword.requestFocus();
            txtFieldPassword.positionCaret(txtFieldPassword.getText().length());
        } else {
            txtFieldPassword.setVisible(false);
            txtFieldPassword.setManaged(false);
            txtPassword.setVisible(true);
            txtPassword.setManaged(true);
            btnToggle.setText("Mostrar");
            txtPassword.requestFocus();
            txtPassword.positionCaret(txtPassword.getText().length());
        }
    }
    
    /**
     * Limpia todos los campos de entrada.
     */
    private void clearFields() {
        txtNombre.clear();
        txtApellido.clear();
        txtPassword.clear();
        txtFieldPassword.clear();
        txtMail.clear();
        typeUser.setSelected(false);
    }
}