package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import models.Employee;
import models.EmployeeC;
import models.Session;

public class ShowTeachersCllr {

    @FXML
    private ListView<Employee> listProfesores;

    private final EmployeeC empc = new EmployeeC();
    
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
        ObservableList<Employee> empleados = empc.getAllEmps();
        ObservableList<Employee> profesores = javafx.collections.FXCollections.observableArrayList();

        for (Employee emp : empleados) {
            if (emp.getTipo() == 0 /*! && emp.getCodigo() == null*/) { // tipo 0 = profesor
                profesores.add(emp);
            }
        }

        listProfesores.setItems(profesores);

        // Para mostrar nombres legibles en la lista
        listProfesores.setCellFactory(lv -> new ListCell<Employee>() {
            @Override
            protected void updateItem(Employee emp, boolean empty) {
                super.updateItem(emp, empty);
                if (empty || emp == null) {
                    setText(null);
                } else {
                    setText(emp.getNombre() + " " + emp.getApellido());
                }
            }
        });
    }
    
    // En ShowTeachersCllr.java
    public void viewInformation() {
        Employee seleccionado = listProfesores.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            try {
                // Guardar el ID del empleado seleccionado en la sesión
                Session.getInstance().setSelectedEmployeeId(seleccionado.getId());

                // Cargar el nuevo panel
                GuideCllr.getInstance().loadPanel("/views/ShowTestsVw.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MainCllr.mostrarAlerta("Error", "Por favor selecciona un profesor para ver sus evaluaciones.");
        }
    }
    
    public int getInfoEmpleado() {
        Employee selected = listProfesores.getSelectionModel().getSelectedItem();
        if (selected != null) {
            return selected.getId();
        } else {
            return -1; // o cualquier valor que indique "ninguna selección"
        }
    }

}

