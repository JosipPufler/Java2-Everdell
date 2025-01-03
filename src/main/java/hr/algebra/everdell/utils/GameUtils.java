package hr.algebra.everdell.utils;

import hr.algebra.everdell.EverdellApplication;
import hr.algebra.everdell.controllers.CityController;
import hr.algebra.everdell.controllers.EverdellMainController;
import hr.algebra.everdell.controllers.PlayableCardController;
import hr.algebra.everdell.models.Card;
import hr.algebra.everdell.models.GameState;
import hr.algebra.everdell.models.Marker;
import hr.algebra.everdell.models.PlayerNumber;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class GameUtils {
    private GameUtils() {}

    static EverdellMainController mainController;
    static CityController cityController;
    static PlayableCardController handController;
    static PlayableCardController meadowController;

    private static Stage meadowStage;

    static final int PLAYER_ONE_STARTING_HAND_SIZE = 5;
    static final int PLAYER_TWO_STARTING_HAND_SIZE = PLAYER_ONE_STARTING_HAND_SIZE + 1;

    public static CityController showCity(EverdellMainController controller) throws IOException {
        mainController = controller;

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(controller.getClass().getResource("/hr/algebra/everdell/City.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 750);
        Stage cityStage = new Stage();
        cityStage.setTitle("Player " + GameState.getPlayerState().getPlayerNumber() + " city");
        cityStage.setScene(scene);
        cityStage.initStyle(StageStyle.UTILITY);
        cityStage.setX(0);
        cityStage.setY(0);
        cityStage.setOnCloseRequest(event -> {
            Platform.exit();
        });
        cityStage.show();
        cityController = fxmlLoader.getController();
        cityController.setParentController(controller);
        return cityController;
    }

    public static PlayableCardController showHand(EverdellMainController controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(controller.getClass().getResource("/hr/algebra/everdell/Hand.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 410);
        Stage handStage = new Stage();
        handStage.setTitle("Player " + GameState.getPlayerState().getPlayerNumber() + " hand");
        handStage.setScene(scene);
        handStage.initStyle(StageStyle.UTILITY);
        handStage.setX(0);
        handStage.setY(Screen.getPrimary().getVisualBounds().getMaxY() - 410);
        handStage.setOnCloseRequest(event -> {
            Platform.exit();
        });
        handStage.show();

        handController = fxmlLoader.getController();
        return handController;
    }

    public static PlayableCardController generateMeadow(EverdellMainController controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(controller.getClass().getResource("/hr/algebra/everdell/Meadow.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 410);
        meadowStage = new Stage();
        meadowStage.setTitle("Meadow");
        meadowStage.setScene(scene);
        meadowStage.initStyle(StageStyle.UTILITY);

        meadowController = fxmlLoader.getController();
        return meadowController;
    }

    public static void showMeadow() {
        meadowStage.show();
    }

    public static void removeCardFromCity(Card card) {
        cityController.removeCard(card);
        GameState.getPlayerState().cardsInPlay.remove(card);
    }

    public static void removeCardFromHand(Card card) {
        handController.removeCard(card);
        GameState.getPlayerState().cardsInHand.remove(card);
    }

    public static void removeCardFromMeadow(Card card) {
        meadowController.removeCard(card);
        GameState.getResourceManager().getMeadow().remove(card);
    }

    public static void addCardToCity(Card card, Boolean play) {
        if (GameState.getPlayerState().cardsInPlay.size() < GameState.getPlayerState().MAX_CARDS_IN_PLAY) {
            if (play)
                GameState.getPlayerState().playCard(card);
            else
                GameState.getPlayerState().cardsInPlay.add(card);
            cityController.insertCard(card);
        }
    }

    private static void addCardsToCity(List<Card> cards, Boolean play) {
        for (Card card : cards){
            addCardToCity(card, play);
        }
    }

    public static void addCardToHand(Card card) {
        if (GameState.getPlayerState().cardsInHand.size() < GameState.getPlayerState().MAX_CARDS_IN_HAND) {
            GameState.getPlayerState().cardsInHand.add(card);
            handController.insertCard(card);
        }
    }

    public static void addCardsToHand(List<Card> cards) {
        for (Card card : cards) {
            addCardToHand(card);
        }
    }

    public static void updatePlayer() {
        mainController.updateResourcePool();
        mainController.updateTitle("Player " + GameState.getPlayerState().getPlayerName() + " turn");
    }

    public static void updateMarkers(List<Marker> markers) {
        mainController.updateOpponentGroup(markers);
    }

    public static Group getMarkerGroup(){
        if (GameState.getPlayerState().getPlayerNumber() == PlayerNumber.ONE){
            return mainController.playerOneGroup;
        } else {
            return mainController.playerTwoGroup;
        }
    }

    public static void addCardToOpponentsHand(Card card) {
        if (GameState.getOpponentState().cardsInHand.size() < GameState.getOpponentState().MAX_CARDS_IN_HAND)
            GameState.getOpponentState().cardsInHand.add(card);
    }

    public static void addCardToOpponentsCity(Card card) {
        if (GameState.getOpponentState().cardsInPlay.size() < GameState.getOpponentState().MAX_CARDS_IN_PLAY)
            GameState.getOpponentState().cardsInPlay.add(card);
    }

    public static void setUpGame() {
        ResourceManager resourceManager = GameState.getResourceManager();
        addCardsToHand(resourceManager.tryDrawFromMainDeck(PLAYER_ONE_STARTING_HAND_SIZE));
        addCardsToOpponentsHand(resourceManager.tryDrawFromMainDeck(PLAYER_TWO_STARTING_HAND_SIZE));
        replenishMeadow();
        if (!EverdellApplication.solo)
            sendUpdate();
    }

    public static void replenishMeadow() {
        List<Card> cards = GameState.getResourceManager().replenishMeadow();
        meadowController.insertCards(cards);
    }

    public static void addCardsToOpponentsHand(List<Card> cards) {
        for (Card card : cards) {
            addCardToOpponentsHand(card);
        }
    }

    public static void switchPlayers(){
        handController.clearCards();
        cityController.clearCards();

        handController.insertCards(GameState.getPlayerState().cardsInHand);
        cityController.insertCards(GameState.getPlayerState().cardsInPlay);
        updatePlayer();
    }

    public static void sendUpdate() {
        NetworkingUtils.sendGameState();
    }

    public static void updateMeadow() {
        meadowController.clearCards();
        meadowController.insertCards(GameState.getResourceManager().getMeadow());
    }
}