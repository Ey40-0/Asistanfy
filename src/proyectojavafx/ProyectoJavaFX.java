package proyectojavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProyectoJavaFX extends Application {
    
    // definición del panel de inicio
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/main.fxml"));
        Scene scene = new Scene(root);
        scene.getRoot().setStyle("-fx-background-radius: 10; -fx-background-color: #2b2b2b;");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle("Mi App JavaFX");
        primaryStage.setScene(scene);
        primaryStage.setWidth(760);
        primaryStage.setHeight(500);
        primaryStage.show();
        primaryStage.getScene().setFill(Color.TRANSPARENT);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
