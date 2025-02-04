package hr.algebra.everdell.controllers;

import hr.algebra.everdell.models.Resource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import lombok.Setter;

public class SingleResourceDialogController {
    @FXML
    public Spinner<Integer> spinner;
    @FXML
    public Label lblResourceType;

    @Setter
    Resource resourceType;

    @Setter
    int maxResources;

    public void initialize() {}

    public void setParameters(int maxResources, Resource resourceType) {
        lblResourceType.setText(resourceType.toString());
        this.maxResources = maxResources;

        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxResources));

        spinner.valueProperty().addListener((_, oldValue, newValue) -> {
            if(newValue > maxResources) {
                spinner.getValueFactory().setValue(oldValue);
            }
        });
    }

    public int getResource(){
        return spinner.getValue();
    }
}
