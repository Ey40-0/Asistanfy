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

public class MainCllr implements Initializable {

    @FXML
    private Pane contentPane;
    @FXML
    private Pane navBar;
    
    private static MainCllr instance;
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    /**
     * Inicializa el controlador principal, cargando el panel de login y configurando drag de ventana.
     * @param location URL de ubicación.
     * @param resources Recursos.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
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

    /**
     * Obtiene la instancia singleton del controlador principal.
     * @return La instancia de MainCllr.
     */
    public static MainCllr getInstance() {
        return instance;
    }

    /**
     * Muestra un panel FXML en el Pane principal.
     * @param rutaFXML La ruta del archivo FXML.
     */
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
    
    /**
     * Cierra la aplicación.
     */
    public void close() {
        System.exit(0);
    }
    
    /**
     * Minimiza la ventana.
     */
    public void minimize() {
        Stage stage = (Stage) contentPane.getScene().getWindow();
        stage.setIconified(true);
    }
    
    /**
     * Muestra una alerta informativa.
     * @param titulo Título de la alerta.
     * @param mensaje Mensaje de la alerta.
     */
    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    /**
     * Valida un RUN chileno (formato xxxxxxxx-x).
     * @param run El RUN a validar.
     * @return true si es válido, false en caso contrario.
     */
    public static boolean validarRun(String run) {
        run = run.replace(".", "").replace("-", "").replace(" ", "").toUpperCase();

        // Agregado: Verificar longitud exacta (8 dígitos + DV)
        if (!run.matches("\\d{8}[0-9K]")) {
            return false;
        }

        String numero = run.substring(0, run.length() - 1);
        char dv = run.charAt(run.length() - 1);

        return dv == calcularDV(numero);
    }

    /**
     * Calcula el dígito verificador de un RUN.
     * @param rut El número sin DV.
     * @return El DV calculado.
     */
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
    
    /**
     * Formatea un RUN válido a xxxxxxxx-x.
     * @param run El RUN a formatear.
     * @return El RUN formateado, o vacío si inválido.
     */
    public static String formatearRun(String run) {
        if (run == null || run.isEmpty()) return "";

        run = run.replace(".", "").replace("-", "").replace(" ", "").toUpperCase();

        if (!validarRun(run)) {
            return "";
        }

        String numero = run.substring(0, run.length() - 1);
        char dv = run.charAt(run.length() - 1);

        return numero + "-" + dv;
    }

}