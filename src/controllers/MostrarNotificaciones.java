package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import models.Empleado;
import models.EmpleadoDAO;
import models.Sesion;
import models.SolicitudDAO;

/**
 *
 * @author PROG
 */
public class MostrarNotificaciones {
    @FXML
    private ListView<Empleado> listNotifications;
    
    private final EmpleadoDAO empc = new EmpleadoDAO();
    private final SolicitudDAO solic = new SolicitudDAO();
    
    @FXML
    public void initialize() {
        cargarNotificaciones();
    }
    
    private void cargarNotificaciones() {
        ObservableList<Empleado> solicitudes = solic.getAllSoli(Sesion.getInstance().getId());
        ObservableList<Empleado> notify = javafx.collections.FXCollections.observableArrayList();
        
        String codigoProfesor = Sesion.getInstance().getCode();

        // filtrar inspectores que no tienen el mismo código que el profesor
        for (Empleado emp : solicitudes) {
            if (emp.getTipo() == 1 && (codigoProfesor == null || !codigoProfesor.equals(emp.getCodigo()))) {
                notify.add(emp);
            }
        }

        listNotifications.setItems(notify);

        // Mostrar solo el mensaje en la lista
        listNotifications.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Empleado item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("El inspector " + item.getNombre() + " " + item.getApellido() + " te invita a su institucion.");
                }
            }
        });
    }
    
    /*
    public void vinculation() {
        Empleado selected = listNotifications.getSelectionModel().getSelectedItem();
        
        int idProfesor = Sesion.getInstance().getId();

        if (selected != null) {
            int idInspector = selected.getId();
            String newCode = empc.vincularCodigo(idInspector, idProfesor);
            // listNotifications.getItems().remove(selected);
            if (Sesion.getInstance().getCode() == null) {
                Sesion.getInstance().setCode(newCode);
            }

            cargarNotificaciones(); // refrescar lista
        } else {
            MainController.getInstance().mostrarAlerta("Error", "No seleccionaste ningún inspector.");
        }
    }
    */
}