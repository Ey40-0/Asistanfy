package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import models.Session;

public class GuideCllr {

    @FXML private AnchorPane contentPane;
    private static GuideCllr instance;

    @FXML
    public void initialize() {
        instance = this;
        if (contentPane == null) {
            return;
        }

        switch (Session.getInstance().getId_rol()) {
            case 0 -> btnNewTest();
            case 1 -> btnShowTeachers();
            case 2 -> btnRegister();
            default -> MainCllr.mostrarAlerta("Error", "Rol no reconocido.");
        }
    }

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

    public void btnShowTeachers() {
        loadPanel("/views/MiShowTeachersVw.fxml");
    }

    public void btnConfig() {
        loadPanel("/views/ConfigVw.fxml");
    }

    public void btnNotifications() {
        loadPanel("/views/MpNotificationsVw.fxml");
    }

    public void btnNewTest() {
        loadPanel("/views/MpAddTestVw.fxml");
    }

    public void btnViewTest() {
        loadPanel("/views/ShowTestsVw.fxml");
    }

    public void btnAddCourse() {
        loadPanel("/views/MaAddCourseVw.fxml");
    }

    public void btnAddMatter() {
        loadPanel("/views/MaAddMatterVw.fxml");
    }

    public void btnRegister() {
        loadPanel("/views/MaAddEmployeeVw.fxml");
    }

    public void btnLogout() {
        MainCllr.getInstance().showPanel("/views/LoginVw.fxml");
        Session.cerrarSesion();
        MainCllr.mostrarAlerta("Éxito", "Sesión cerrada correctamente.");
    }
}