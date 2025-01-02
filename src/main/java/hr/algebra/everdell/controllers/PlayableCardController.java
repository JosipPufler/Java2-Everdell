package hr.algebra.everdell.controllers;

import hr.algebra.everdell.interfaces.CardInsertable;
import hr.algebra.everdell.interfaces.CardListable;
import hr.algebra.everdell.models.Card;
import hr.algebra.everdell.models.GameState;
import hr.algebra.everdell.utils.GameUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.TilePane;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PlayableCardController extends CardListable implements CardInsertable {
    @FXML
    protected TilePane handPane;

    public void initialize() {
        handPane.setHgap(5);
        handPane.setVgap(5);
        super.popUpStage.setAlwaysOnTop(true);
    }

    @Override
    public void insertCard(Card card) {
        handPane.getChildren().add(createCardImageView(card));
    }

    @Override
    public void insertCards(List<Card> cards) {
        handPane.getChildren().addAll(createImageViews(cards));
    }

    @Override
    protected ImageView createCardImageView(Card card) {
        ImageView imageView = super.createCardImageView(card);
        imageView.setOnDragDetected(event -> {
            Dragboard dragboard = imageView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent cc = new ClipboardContent();
            cc.putString(card.getClass().getName());
            dragboard.setContent(cc);
            dragboard.setDragView(imageView.getImage(), 7, 14);
            event.consume();
        });

        imageView.setOnDragDone(event -> {
            if (event.isAccepted()) {
                GameUtils.removeCardFromHand(card);
                GameState.switchPlayers();
            }
        });

        imageView.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });
        return imageView;
    }

    public void removeCard(Card card) {
        Optional<Node> view = handPane.getChildren().stream().filter(x -> Objects.equals(x.getId(), card.getName())).findFirst();
        view.ifPresent(node -> handPane.getChildren().remove(node));
    }

    public void clearCards(){
        handPane.getChildren().clear();
    }
}