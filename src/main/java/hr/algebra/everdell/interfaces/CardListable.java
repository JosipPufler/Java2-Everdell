package hr.algebra.everdell.interfaces;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class CardListable {
    @FXML
    public final Stage popUpStage = new Stage(StageStyle.UNDECORATED);

    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

    protected ImageView createCardImageView(Card card){
        File file = new File(card.getImageFilePath());
        Image image = new Image(file.toURI().toString());
        ImageView iv = new ImageView(image);
        iv.setId(card.getName());
        iv.setFitHeight(200);
        iv.setPreserveRatio(true);
        popUpStage.setMaxHeight(700);

        iv.setOnMouseMoved(event -> {
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
        });

        iv.hoverProperty().addListener((_, _, newValue) -> {
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

    protected List<ImageView> createImageViews(List<Card> cards){
        List<ImageView> imageViews = new ArrayList<>();
        for(Card card : cards){
            ImageView imageView = createCardImageView(card);
            if (imageView == null){
                return imageViews;
            }
            imageViews.add(imageView);
        }
        return imageViews;
    }
}
