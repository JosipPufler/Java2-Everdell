package hr.algebra.everdell.interfaces;

import hr.algebra.everdell.models.Card;
import hr.algebra.everdell.models.PlayerState;
import hr.algebra.everdell.utils.PlayerStateSingleton;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class CardListable {
    @FXML
    public final Stage popUpStage = new Stage();

    private final PlayerState playerState = PlayerStateSingleton.getInstance();
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    protected ImageView addCard(Card card){
        if ((playerState.cardsInPlay.contains(card) && card.isUnique()) || playerState.cardsInPlay.size() >= 15){
            return null;
        }

        playerState.cardsInPlay.add(card);

        File file = new File(card.getImageFilePath());
        Image image = new Image(file.toURI().toString());
        ImageView iv = new ImageView(image);
        iv.setFitHeight(200);
        iv.setPreserveRatio(true);
        popUpStage.initStyle(StageStyle.UNDECORATED);
        popUpStage.setMaxHeight(700);

        iv.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                if (event.getScreenX() + popUpStage.getWidth()> primaryScreenBounds.getMaxX()) {
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

    protected List<ImageView> addCards(List<Card> cards){
        List<ImageView> imageViews = new ArrayList<>();
        for(Card card : cards){
            ImageView imageView = addCard(card);
            if (imageView == null){
                return imageViews;
            }
            imageViews.add(imageView);
        }
        return  imageViews;
    }
}
