package controllers;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import models.Empleado;
import models.EmpleadoDAO;
import models.Sesion;
import models.SolicitudDAO;

public class MostrarProfesores {

    @FXML
    private ListView<Empleado> listProfesores;

    private final EmpleadoDAO empc = new EmpleadoDAO();
    private final SolicitudDAO solic = new SolicitudDAO();
    
    private static MostrarProfesores instance;

    @FXML
    public void initialize() {
        instance = this;
        cargarProfesores();
    }
     
    // Getter de la clase
    public static MostrarProfesores getInstance() {
        return instance;
    }

    private void cargarProfesores() {
        ObservableList<Empleado> empleados = empc.getAllEmps();
        ObservableList<Empleado> profesores = javafx.collections.FXCollections.observableArrayList();

        for (Empleado emp : empleados) {
            if (emp.getTipo() == 0 /*! && emp.getCodigo() == null*/) { // tipo 0 = profesor
                profesores.add(emp);
            }
        }

        listProfesores.setItems(profesores);

        // Para mostrar nombres legibles en la lista
        listProfesores.setCellFactory(lv -> new ListCell<Empleado>() {
            @Override
            protected void updateItem(Empleado emp, boolean empty) {
                super.updateItem(emp, empty);
                if (empty || emp == null) {
                    setText(null);
                } else {
                    setText(emp.getNombre() + " " + emp.getApellido());
                }
            }
        });
    }
    
    public void viewInformation() {
        Empleado seleccionado = listProfesores.getSelectionModel().getSelectedItem();
        
        if (seleccionado != null) {
            try {
                MenuAutoController.getInstance().btnViewTest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MainController.getInstance().mostrarAlerta("Error", "Por favor selecciona un profesor para enviar la solicitud");
        }
    }
    
    public int getInfoEmpleado() {
        Empleado selected = listProfesores.getSelectionModel().getSelectedItem();
        if (selected != null) {
            return selected.getId();
        } else {
            return -1; // o cualquier valor que indique "ninguna selección"
        }
    }

    /*
    @FXML
    private void enviarSolicitud() throws SQLException {
        Empleado seleccionado = listProfesores.getSelectionModel().getSelectedItem();
        
        if (seleccionado != null) {
            System.out.println("Id emisor: " + Sesion.getInstance().getId());
            System.out.println("Id receptor: " + seleccionado.getId());
            
            String msg = "Solicitud enviada al profesor: " + seleccionado.getNombre() + " " + seleccionado.getApellido();
            if (solic.sendSol(Sesion.getInstance().getId(), seleccionado.getId())) {
                MainController.getInstance().mostrarAlerta("Enviado", msg);
            } else {
                MainController.getInstance().mostrarAlerta("Incongruencia", "Ya existe una solicitud entre estos usuarios.");
            }
            
        } else {
            MainController.getInstance().mostrarAlerta("Error", "Por favor selecciona un profesor para enviar la solicitud");
        }
    }
    */

}

