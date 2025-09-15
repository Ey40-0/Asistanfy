package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import models.Sesion;

public class GuideCllr{
    
    @FXML
    private AnchorPane contentPane;
    
    // Variable static de tipo AutoController
    private static GuideCllr instance;
    
    @FXML
    public void initialize() {
        instance = this;
        if (contentPane != null && Sesion.getInstance().getId_rol() == 0) {
            btnNewTest();
        }
        
        if (contentPane != null && Sesion.getInstance().getId_rol() == 1) {
            btn_new();
        }
    }
    
    // Getter de la clase
    public static GuideCllr getInstance() {
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
            MainCllr.mostrarAlerta("Error", "No se pudo cargar la pantalla: " + rutaFXML);
        }
    }
    
    public void btn_new() {
        loadPanel("/views/MiShowTeachersVw.fxml");
    }
    
    public void btnConfig() {
        loadPanel("/views/ConfigVw.fxml");
    }
    
    public void btnNotifications() {  //  f(x) = 2x+3
        loadPanel("/views/MpNotificationsVw.fxml");
    }
    
    public void btnNewTest() {
        loadPanel("/views/MpAddTestVw.fxml");
    }
    
    public void btnViewTest() {
        loadPanel("/views/ShowTests.fxml");
    }
    
    public void btnLogout() {
        MainCllr.getInstance().showPanel("/views/LoginVw.fxml");
        Sesion.cerrarSesion();
        MainCllr.mostrarAlerta(null, "Sesión cerrada con exito");
    }
    
}