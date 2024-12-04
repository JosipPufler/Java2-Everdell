package hr.algebra.everdell.controllers;

import hr.algebra.everdell.models.ResourceGroup;
import hr.algebra.everdell.utils.ResourceManager;
import hr.algebra.everdell.utils.ResourcePoolManagerFactory;
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

    final ResourceManager resourceManager = ResourcePoolManagerFactory.getInstance();

    public void initialize() {
        berrySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resourceManager.getResourcePool().getBerries()));
        resinSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resourceManager.getResourcePool().getResin()));
        pebbleSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resourceManager.getResourcePool().getPebbles()));
        twigSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, resourceManager.getResourcePool().getTwigs()));

        spinners = new ArrayList<>(Arrays.asList(twigSpinner, pebbleSpinner, resinSpinner, berrySpinner));

        for(var spinner: spinners) {
            spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
               if(getResourcePool().sumAllResources() > maxResources) {
                    spinner.getValueFactory().setValue(oldValue);
               }
            });
        }
    }

    public void setMaxResources(int maxResources) {
        this.maxResources = maxResources;
    }

    public ResourceGroup getResourcePool(){
        return new ResourceGroup(berrySpinner.getValue(), twigSpinner.getValue(), resinSpinner.getValue(), pebbleSpinner.getValue());
    }
}
