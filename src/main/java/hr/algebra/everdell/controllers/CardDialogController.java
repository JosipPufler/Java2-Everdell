package hr.algebra.everdell.controllers;

import hr.algebra.everdell.interfaces.CardInsertable;
import hr.algebra.everdell.interfaces.CardListable;
import hr.algebra.everdell.models.Card;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.List;

public class CardDialogController extends CardListable implements CardInsertable {
    @FXML
    private TilePane cardPane;

    @FXML
    private final Stage popUpStage = new Stage();

    private Card selectedCard;

    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    public void initialize() {
        cardPane.setHgap(5);
        cardPane.setVgap(5);
        popUpStage.setAlwaysOnTop(true);
    }

    public ImageView addCard(Card card){

        File file = new File(card.getImageFilePath());
        Image image = new Image(file.toURI().toString());
        ImageView iv = new ImageView(image);
        iv.setFitHeight(200);
        iv.setPreserveRatio(true);
        popUpStage.initStyle(StageStyle.UNDECORATED);
        popUpStage.setMaxHeight(700);

        iv.setOnMouseMoved(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getScreenX() + popUpStage.getWidth() > primaryScreenBounds.getMaxX()) {
                    popUpStage.setX(event.getScreenX() - popUpStage.getWidth() - 20);
                } else {
                    popUpStage.setX(event.getScreenX() + 20);
                }
                if (event.getScreenY() + popUpStage.getHeight() + 20 > primaryScreenBounds.getMaxY()) {
                    popUpStage.setY(event.getScreenY() - (event.getScreenY() + popUpStage.getHeight() - primaryScreenBounds.getMaxY()));
                } else {
                    popUpStage.setY(event.getScreenY() + 20);
                }
            }
        });

        iv.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                selectedCard = card;
            }
        });

        iv.hoverProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue){
                Image popUpImage = iv.getImage();
                ImageView popUpImageView = new ImageView(popUpImage);
                popUpImageView.setFitHeight(700);
                popUpImageView.setPreserveRatio(true);

                StackPane popUpPane = new StackPane();
                popUpPane.getChildren().add(popUpImageView);
                popUpStage.setScene(new Scene(popUpPane));
                popUpStage.show();
            } else {
                popUpStage.hide();
            }
        });

        return iv;
    }

    public Card getSelectedCard() {
        return selectedCard;
    }

    @Override
    public void insertCards(List<Card> cards) {
        cardPane.getChildren().addAll(addCards(cards));
    }

    @Override
    public void insertCard(Card card) {
        cardPane.getChildren().add(addCard(card));
    }
}
