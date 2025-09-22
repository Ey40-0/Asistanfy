package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

// Implementacion de la interfaz en el controlador
public class MainCllr implements Initializable {

    // Se reciben los campos de FXML
    
    @FXML
    private Pane contentPane;
    @FXML
    private Pane navBar;
    
    // Variable static de tipo MainController
    private static MainCllr instance;
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    // Metodo abstracto
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this; // Guarda referencia al controlador principal
        showPanel("/views/LoginVw.fxml");
        
        navBar.setOnMousePressed((MouseEvent event) -> {
            Stage stage = (Stage) navBar.getScene().getWindow();
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        navBar.setOnMouseDragged((MouseEvent event) -> {
            Stage stage = (Stage) navBar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

    }

    // Getter de la clase
    public static MainCllr getInstance() {
        return instance;
    }

    // Metodo principal del AnchorPane
    public void showPanel(String rutaFXML) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource(rutaFXML));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(pane);
            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void close() {
        System.exit(0);
    }
    
    public void minimize() {
        javafx.stage.Stage stage = (javafx.stage.Stage) contentPane.getScene().getWindow();
        stage.setIconified(true);
    }
    
    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public static boolean validarRun(String run) {
        // Limpiar puntos, guiones, espacios y convertir a mayúsculas
        run = run.replace(".", "").replace("-", "").replace(" ", "").toUpperCase();

        if (!run.matches("\\d{7,8}[0-9K]")) {
            return false;
        }

        // Separar número y dígito verificador
        String numero = run.substring(0, run.length() - 1);
        char dv = run.charAt(run.length() - 1);

        return dv == calcularDV(numero);
    }

    // Función para calcular el dígito verificador (módulo 11)
    private static char calcularDV(String rut) {
        int suma = 0;
        int multiplicador = 2;

        for (int i = rut.length() - 1; i >= 0; i--) {
            suma += Character.getNumericValue(rut.charAt(i)) * multiplicador;
            multiplicador = (multiplicador == 7) ? 2 : multiplicador + 1;
        }

        int resto = 11 - (suma % 11);

        if (resto == 11) return '0';
        if (resto == 10) return 'K';
        return (char) (resto + '0');
    }
    
    public static String formatearRun(String run) {
        if (run == null || run.isEmpty()) return "";

        // Limpiar puntos, guiones, espacios y convertir a mayúsculas
        run = run.replace(".", "").replace("-", "").replace(" ", "").toUpperCase();

        // Validar antes de formatear
        if (!validarRun(run)) {
            return ""; // o puedes lanzar una excepción si prefieres
        }

        String numero = run.substring(0, run.length() - 1);
        char dv = run.charAt(run.length() - 1);

        return numero + "-" + dv;
    }

}