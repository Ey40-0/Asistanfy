package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import models.EmployeeC;

public class ShowNotificationsCllr {
    @FXML
    private ListView<String> listNotifications;
    
    private final EmployeeC empc = new EmployeeC();
    
    /**
     * Inicializa el controlador, cargando notificaciones.
     */
    @FXML
    public void initialize() {
        cargarNotificaciones();
    }
    
    /**
     * Carga las notificaciones en el ListView (hardcoded por ahora).
     */
    private void cargarNotificaciones() {
        ObservableList<String> notify = javafx.collections.FXCollections.observableArrayList();

        if (listNotifications.getItems().isEmpty()) {
            notify.add("No tienes notificaciones");
        }

        listNotifications.setItems(notify);
    }
    
}