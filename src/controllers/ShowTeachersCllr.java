package controllers;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import models.Empleado;
import models.EmpleadoDAO;
import models.Sesion;

public class ShowTeachersCllr {

    @FXML
    private ListView<Empleado> listProfesores;

    private final EmpleadoDAO empc = new EmpleadoDAO();
    
    private static ShowTeachersCllr instance;

    @FXML
    public void initialize() {
        instance = this;
        cargarProfesores();
    }
     
    // Getter de la clase
    public static ShowTeachersCllr getInstance() {
        return instance;
    }

    private void cargarProfesores() {
        ObservableList<Empleado> empleados = empc.getAllEmps();
        ObservableList<Empleado> profesores = javafx.collections.FXCollections.observableArrayList();

        for (Empleado emp : empleados) {
            if (emp.getTipo() == 0 /*! && emp.getCodigo() == null*/) { // tipo 0 = profesor
                profesores.add(emp);
            }
        }

        listProfesores.setItems(profesores);

        // Para mostrar nombres legibles en la lista
        listProfesores.setCellFactory(lv -> new ListCell<Empleado>() {
            @Override
            protected void updateItem(Empleado emp, boolean empty) {
                super.updateItem(emp, empty);
                if (empty || emp == null) {
                    setText(null);
                } else {
                    setText(emp.getNombre() + " " + emp.getApellido());
                }
            }
        });
    }
    
    public void viewInformation() {
        Empleado seleccionado = listProfesores.getSelectionModel().getSelectedItem();
        
        if (seleccionado != null) {
            try {
                GuideCllr.getInstance().btnViewTest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MainCllr.getInstance().mostrarAlerta("Error", "Por favor selecciona un profesor para enviar la solicitud");
        }
    }
    
    public int getInfoEmpleado() {
        Empleado selected = listProfesores.getSelectionModel().getSelectedItem();
        if (selected != null) {
            return selected.getId();
        } else {
            return -1; // o cualquier valor que indique "ninguna selección"
        }
    }

}

