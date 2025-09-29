package controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import models.Session;

public class GuideCllr{
    
    @FXML
    private AnchorPane contentPane;
    
    private static GuideCllr instance;
    
    /**
     * Inicializa el controlador, cargando panel inicial según rol.
     */
    @FXML
    public void initialize() {
        instance = this;
        if (contentPane != null && Session.getInstance().getId_rol() == 0) {
            btnNewTest();
        }
        
        if (contentPane != null && Session.getInstance().getId_rol() == 1) {
            btnShowTeachers();
        }
        
        if (contentPane != null && Session.getInstance().getId_rol() == 2) {
            btnRegister();
        }
    }
    
    /**
     * Obtiene la instancia singleton del controlador.
     * @return La instancia de GuideCllr.
     */
    public static GuideCllr getInstance() {
        return instance;
    }

    /**
     * Carga un panel FXML en el AnchorPane principal.
     * @param rutaFXML La ruta del archivo FXML a cargar.
     */
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
    
    /**
     * Carga el panel para mostrar profesores.
     */
    public void btnShowTeachers() {
        loadPanel("/views/MiShowTeachersVw.fxml");
    }
    
    /**
     * Carga el panel de configuración.
     */
    public void btnConfig() {
        loadPanel("/views/ConfigVw.fxml");
        
    }
    
    /**
     * Carga el panel de notificaciones.
     */
    public void btnNotifications() {
        loadPanel("/views/MpNotificationsVw.fxml");
    }
    
    /**
     * Carga el panel para añadir nueva prueba.
     */
    public void btnNewTest() {
        loadPanel("/views/MpAddTestVw.fxml");
    }
    
    /**
     * Carga el panel para ver pruebas.
     */
    public void btnViewTest() {
        loadPanel("/views/ShowTestsVw.fxml");
    }
    
    /**
     * Carga el panel para añadir curso.
     */
    public void btnAddCourse() {
        loadPanel("/views/MaAddCourseVw.fxml");
    }
    
    /**
     * Carga el panel para añadir materia.
     */
    public void btnAddMatter() {
        loadPanel("/views/MaAddMatterVw.fxml");
    }
    
    /**
     * Carga el panel para registrar empleado.
     */
    public void btnRegister() {
        loadPanel("/views/MaAddEmployeeVw.fxml");
    }
    
    /**
     * Cierra la sesión y carga el panel de login.
     */
    public void btnLogout() {
        MainCllr.getInstance().showPanel("/views/LoginVw.fxml");
        Session.cerrarSesion();
        MainCllr.mostrarAlerta(null, "Sesión cerrada con exito");
    }
    
}