package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import models.Sesion;

public class MenuAutoController{
    
    @FXML
    private AnchorPane contentPane;
    
    // Variable static de tipo AutoController
    private static MenuAutoController instance;
    
    @FXML
    public void initialize() {
        instance = this;
        if (contentPane != null && Sesion.getInstance().getId_rol() == 0) {
            if (Sesion.getInstance().getCode() == null) {
                btnNotifications();
            } else {
                btnNewTest();
            }
        }
        
        if (contentPane != null && Sesion.getInstance().getId_rol() == 1) {
            btn_new();
        }
    }
    
    // Getter de la clase
    public static MenuAutoController getInstance() {
        return instance;
    }

    public void loadPanel(String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            AnchorPane pane = loader.load();

            contentPane.getChildren().clear();
            contentPane.getChildren().add(pane);

            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
            MainController.getInstance().mostrarAlerta("Error", "No se pudo cargar la pantalla: " + rutaFXML);
        }
    }
    
    public void btn_new() {
        loadPanel("/views/mi_profesores.fxml");
    }
    
    public void btnConfig() {
        loadPanel("/views/mi_configuracion.fxml");
    }
    
    public void btnNotifications() {  //  f(x) = 2x+3
        loadPanel("/views/mp_solicitudes.fxml");
    }
    
    public void btnNewTest() {
        if (Sesion.getInstance().getCode() != null) {
            loadPanel("/views/mp_newtest.fxml");
        } else {
            MainController.getInstance().mostrarAlerta("Error", "Dependes de un inspector.");
        }
    }
    
    public void btnViewTest() {
        loadPanel("/views/mp_tests.fxml");
    }
    
    public void btnLogout() {
        MainController.getInstance().showPanel("/views/menu_inicio.fxml");
        Sesion.cerrarSesion();
        MainController.getInstance().mostrarAlerta(null, "Sesión cerrada con exito");
    }
    
}