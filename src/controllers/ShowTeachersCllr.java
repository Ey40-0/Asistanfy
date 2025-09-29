package controllers;

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

    /**
     * Inicializa el controlador, cargando profesores.
     */
    @FXML
    public void initialize() {
        instance = this;
        cargarProfesores();
    }
     
    /**
     * Obtiene la instancia singleton del controlador.
     * @return La instancia de ShowTeachersCllr.
     */
    public static ShowTeachersCllr getInstance() {
        return instance;
    }
    
    /**
     * Carga la lista de profesores desde la base de datos.
     */
    private void cargarProfesores() {
        listProfesores.setItems(empc.getProfesores());
        
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
    
    /**
     * Muestra la información del profesor seleccionado (carga panel de tests).
     */
    public void viewInformation() {
        Employee seleccionado = listProfesores.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            try {
                Session.getInstance().setSelectedEmployeeId(seleccionado.getId());

                GuideCllr.getInstance().loadPanel("/views/ShowTestsVw.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MainCllr.mostrarAlerta("Error", "Por favor selecciona un profesor para ver sus evaluaciones.");
        }
    }
    
    /**
     * Obtiene el ID del empleado seleccionado.
     * @return El ID, o -1 si no hay selección.
     */
    public int getInfoEmpleado() {
        Employee selected = listProfesores.getSelectionModel().getSelectedItem();
        if (selected != null) {
            return selected.getId();
        } else {
            return -1;
        }
    }

}