package hr.algebra.everdell.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.util.List;

public class CustomListDialogController {
    @FXML
    VBox options;

    int selectedIndex;

    public int getChoice() {
        return selectedIndex;
    }

    public void setChoices(List<?> list) {
        for (int i = 0; i < list.size(); i++) {
            Label label = new Label(list.get(i).toString());
            label.setId(Integer.toString(i));
            int finalI = i;
            label.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent event) {
                    options.getChildren().forEach(option -> option.setStyle("-fx-background-color: white; -fx-text-fill: black;"));
                    label.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                    selectedIndex = finalI;
                }
            });
            options.getChildren().add(label);
        }
    }
}
