package hr.algebra.everdell.utils;

import hr.algebra.everdell.EverdellApplication;
import hr.algebra.everdell.controllers.CityController;
import hr.algebra.everdell.controllers.EverdellMainController;
import hr.algebra.everdell.controllers.PlayableCardController;
import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.models.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

public class GameUtils {
    private GameUtils() {}
    static EverdellMainController mainController;
    @Getter
    static CityController cityController;
    @Getter
    static PlayableCardController handController;
    @Getter
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
        cityStage.setOnCloseRequest(_ -> Platform.exit());
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
        handStage.setOnCloseRequest(_ -> Platform.exit());
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

    public static void updatePlayer() {
        mainController.updateResourcePool();
        mainController.updateTitle("Player " + GameState.getPlayerState().getPlayerName() + " turn");
    }

    public static void updateMarkers(List<Marker> markers) {
        mainController.updateOpponentGroup(markers);
    }

    public static List<Location> getDeployableLocations() {
        return mainController.getLocationsDeployable();
    }

    public static Group getMarkerGroup(){
        if (GameState.getPlayerState().getPlayerNumber() == PlayerNumber.ONE){
            return mainController.playerOneGroup;
        } else {
            return mainController.playerTwoGroup;
        }
    }

    public static void setUpGame() {
        ResourceManager resourceManager = GameState.getResourceManager();
        CardUtils.addCardsToHand(resourceManager.tryDrawFromMainDeck(PLAYER_ONE_STARTING_HAND_SIZE));
        CardUtils.addCardsToOpponentsHand(resourceManager.tryDrawFromMainDeck(PLAYER_TWO_STARTING_HAND_SIZE));
        replenishMeadow();
        if (!EverdellApplication.solo)
            sendUpdate();
    }

    public static void replenishMeadow() {
        List<Card> cards = GameState.getResourceManager().replenishMeadow();
        meadowController.insertCards(cards);
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

    public static void blockScreen(boolean block) {
        GameState.getPlayerState().setTurnPriority(!block);
        GameState.getOpponentState().setTurnPriority(block);
        mainController.blockScreen(block);
    }
}