package hr.algebra.everdell.controllers;

import hr.algebra.everdell.models.ResourceGroup;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiResourceDialogController {
    @FXML
    Spinner<Integer> berrySpinner;
    @FXML
    Spinner<Integer> twigSpinner;
    @FXML
    Spinner<Integer> resinSpinner;
    @FXML
    Spinner<Integer> pebbleSpinner;

    List<Spinner<Integer>> spinners;

    int maxResources;

    ResourceGroup referenceResourceGroup;

    public void initialize() {
        spinners = new ArrayList<>(Arrays.asList(twigSpinner, pebbleSpinner, resinSpinner, berrySpinner));
    }

    public void setParameters(int maxResources, ResourceGroup referenceResourceGroup) {
        this.maxResources = maxResources;
        this.referenceResourceGroup = referenceResourceGroup;

        resinSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, referenceResourceGroup.getResin()));
        pebbleSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, referenceResourceGroup.getPebbles()));
        berrySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, referenceResourceGroup.getBerries()));
        twigSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, referenceResourceGroup.getTwigs()));

        for(var spinner: spinners) {
            spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
                if(getResourcePool().sumAllResources() > maxResources) {
                    spinner.getValueFactory().setValue(oldValue);
                }
            });
        }
    }

    public ResourceGroup getResourcePool(){
        return new ResourceGroup(berrySpinner.getValue(), twigSpinner.getValue(), resinSpinner.getValue(), pebbleSpinner.getValue());
    }
}
