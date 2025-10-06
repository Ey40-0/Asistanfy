package proyectojavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProyectoJavaFX extends Application {
    
    /**
     * Declaración del panel de inicio.
     * @param primaryStage
     * @throws Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/MainVw.fxml"));
        Scene scene = new Scene(root);
        scene.getRoot().setStyle("-fx-background-radius: 10; -fx-background-color: #2b2b2b;");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Mi App JavaFX");
        primaryStage.setScene(scene);
        primaryStage.setWidth(840);
        primaryStage.setHeight(520);
        primaryStage.show();
        primaryStage.getScene().setFill(Color.TRANSPARENT);
    }
    
    /**
     * Inicializa la app
     * @param args 
     */
    public static void main(String[] args) {
        launch(args);
    }
}
