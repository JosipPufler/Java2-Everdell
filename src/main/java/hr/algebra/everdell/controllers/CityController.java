package hr.algebra.everdell.controllers;

import hr.algebra.everdell.interfaces.CardInsertable;
import hr.algebra.everdell.interfaces.CardListable;
import hr.algebra.everdell.interfaces.Destination;
import hr.algebra.everdell.models.Card;
import hr.algebra.everdell.models.GameState;
import hr.algebra.everdell.models.PlayerState;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CityController extends CardListable implements CardInsertable {
    @FXML
    private TilePane cityPane;
    EverdellMainController mainController;

    public void initialize() {
        cityPane.setHgap(5);
        cityPane.setVgap(5);
        super.popUpStage.setAlwaysOnTop(true);
        cityPane.setOnDragDropped(event -> {
            String cardName = event.getDragboard().getString();
            System.out.println(cardName);
            try {
                Card card = (Card) Class.forName(cardName).getConstructor().newInstance();
                if (GameState.getPlayerState().playCard(card).isPresent()){
                    updateMainController();
                    event.setDropCompleted(true);
                } else {
                    event.setDropCompleted(false);
                }
                System.out.println(GameState.getPlayerState().cardsInPlay.size());
                System.out.println(GameState.getOpponentState().cardsInPlay.size());

            } catch (Exception e) {
                event.setDropCompleted(false);
                e.printStackTrace();
            }
        });

        cityPane.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
        });
    }

    @Override
    public void insertCard(Card card){
        ImageView cardImageView = createCardImageView(card);
        cardImageView.setUserData(card);
        if (card instanceof Destination){
            Pane container = new Pane();
            cardImageView.setOnMouseClicked(event -> {
                if (GameState.getPlayerState().getFreeWorkers() > 0 && ((Destination) card).tryPlaceWorker()){
                    GameState.getPlayerState().deployWorker(true);
                    container.getChildren().add(new Circle(event.getX(), event.getY(), 5, Paint.valueOf(String.format("#%06x", GameState.getPlayerState().getPlayerNumber().getPlayerColor().getRGB() & 0xFFFFFF))));
                    GameState.switchPlayers();
                }
            });
            container.getChildren().add(cardImageView);
            cityPane.getChildren().add(container);
        } else {
            cityPane.getChildren().add(cardImageView);
        }
    }

    @Override
    public void insertCards(List<Card> cards){
        cityPane.getChildren().addAll(createImageViews(cards));
    }

    public void setParentController(EverdellMainController parentController){
        mainController = parentController;
    }

    public void updateMainController(){
        mainController.updateResourcePool();
        mainController.updateTitle("Player " + GameState.getPlayerState().getPlayerName() + " turn");
    }

    public void removeCard(Card card){
        Optional<Node> view = cityPane.getChildren().stream().filter(x -> Objects.equals(x.getId(), card.getName())).findFirst();
        view.ifPresent(node -> cityPane.getChildren().remove(node));
    }

    public void clearCards(){
        cityPane.getChildren().clear();
    }

    public void returnWorkers(PlayerState playerState){
        for (Node card : cityPane.getChildren()) {
            if (card instanceof Pane){
                Optional<Node> first = ((Pane) card).getChildren().stream().filter(x -> x instanceof ImageView).findFirst();
                if (first.isPresent()){
                    Destination userDataCard = (Destination)first.get().getUserData();
                    int workers = userDataCard.clearWorkers(playerState);
                    if (workers == 0)
                        return;
                    for (int i = ((Pane) card).getChildren().size()-1; i >= 0; i--){
                        if (((Pane) card).getChildren().get(i) instanceof Circle && workers > 0){
                            ((Pane) card).getChildren().remove(i);
                            workers--;
                        } else {
                            return;
                        }
                    }
                }
            }
        }

    }
}
