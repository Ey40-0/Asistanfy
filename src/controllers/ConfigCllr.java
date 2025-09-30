package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Session;

public class ConfigCllr {
    
    @FXML private Label lbl_name;
     @FXML private Label TipoRol;
    
    @FXML
    public void initialize() {
        setData(); 
    }
    
    public void btnLogout() {
        MainCllr.getInstance().showPanel("/views/LoginVw.fxml");
        Session.cerrarSesion();
        MainCllr.mostrarAlerta("Éxito", "Sesión cerrada correctamente.");
    }
    
    public void setData() {
        lbl_name.setText(Session.getInstance().getEmployee().getNombre());
        System.out.println(Session.getInstance().getEmployee().getTipo());
        switch (Session.getInstance().getEmployee().getTipo()) {
            case 0 -> TipoRol.setText("Profesor");
            case 1 -> TipoRol.setText("Inspector");
            case 2 -> TipoRol.setText("Admin");
        }
    }
}
