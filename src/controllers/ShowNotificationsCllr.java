package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import models.Employee;
import models.EmployeeC;
import models.Session;

/**
 *
 * @author PROG
 */
public class ShowNotificationsCllr {
    @FXML
    private ListView<String> listNotifications;
    
    private final EmployeeC empc = new EmployeeC();
    
    @FXML
    public void initialize() {
        cargarNotificaciones();
    }
    
    private void cargarNotificaciones() {
        ObservableList<String> notify = javafx.collections.FXCollections.observableArrayList();

        // filtrar inspectores que no tienen el mismo código que el profesor
        if (listNotifications.getItems().isEmpty()) {
            notify.add("No tienes notificaciones");
        }

        listNotifications.setItems(notify);
    }
    
}