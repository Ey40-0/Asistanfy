package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Matter;

public class AddMatterCllr {
    
    @FXML
    private TextField fldName;
    @FXML
    private ListView<Matter> listMatters;
    
    @FXML
    public void initialize() {
        loadMatters();
    }
    
    public void register() {
        String name = fldName.getText().trim();
        
        if (name.isEmpty()) {
            MainCllr.mostrarAlerta("Campos vacios", "Por favor rellene todos los campos");
        }
        
        Matter mat = new Matter(0, name);
        if (Matter.addNewMatter(mat)) {
            fldName.setText("");
            loadMatters();
        }
    }
    
    private void loadMatters() {
        // Llama al método centralizado en EmployeeC para obtener la lista de profesores
        listMatters.setItems(Matter.getMatters());
        
        // El CellFactory sigue aquí porque es una configuración de la vista
        listMatters.setCellFactory(lv -> new ListCell<Matter>() {
            @Override
            protected void updateItem(Matter mat, boolean empty) {
                super.updateItem(mat, empty);
                if (empty || mat == null) {
                    setText(null);
                } else {
                    setText(mat.getNombre());
                }
            }
        });
    }
}
