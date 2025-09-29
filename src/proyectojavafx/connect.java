package proyectojavafx;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Conexión a la base de datos.
 * @author PROG
 */
public class connect {
    private static Connection conectar;
    private final String usuario = "root";
    private final String contrasenia = "root";
    private final String bd = "Controller";
    private final String ip = "localhost";
    private final String puerto = "3306";

    private final String cadena = "jdbc:mysql://" + ip + ":" + puerto + "/" + bd + "?useSSL=false&serverTimezone=UTC";

    public Connection getConectar() {
        try {
            if (conectar == null || conectar.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conectar = DriverManager.getConnection(cadena, usuario, contrasenia);
            }
        } catch (ClassNotFoundException | SQLException e) {
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error de conexión");
            alerta.setHeaderText("No se pudo conectar a la base de datos");
            alerta.setContentText(e.toString());
            alerta.showAndWait();
        }
        return conectar;
    }
   
}
