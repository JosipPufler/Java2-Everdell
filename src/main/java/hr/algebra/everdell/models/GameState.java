package hr.algebra.everdell.models;

import hr.algebra.everdell.EverdellApplication;
import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.utils.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.shape.Circle;
import lombok.Getter;

import java.sql.Array;
import java.util.ArrayList;

public class GameState {
    private GameState(){}

    static PlayerNumber playerNumber;
    static PlayerNumber opponentNumber;

    @Getter
    static ResourceManager resourceManager = ResourceManagerSingleton.getInstance();
    @Getter
    static PlayerState playerState = new PlayerState(PlayerNumber.ONE);
    @Getter
    static PlayerState opponentState = new PlayerState(PlayerNumber.TWO);

    public static void setPlayer(PlayerNumber player){
        if (PlayerNumber.ONE == player) {
            playerNumber = PlayerNumber.ONE;
            opponentNumber = PlayerNumber.TWO;
        } else if (PlayerNumber.TWO == player) {
            playerNumber = PlayerNumber.TWO;
            opponentNumber = PlayerNumber.ONE;
        } else {
            throw new IllegalArgumentException("Invalid player name: " + player);
        }
        playerState = new PlayerState(playerNumber);
        opponentState = new PlayerState(opponentNumber);
    }

    public static void loadGameState(GameStateTransferable gameStateTransferable){
        ArrayList<Card> cardsInHand = new ArrayList<>(gameStateTransferable.opponentState.cardsInHand);
        ArrayList<Card> cardsInPlay = new ArrayList<>(gameStateTransferable.opponentState.cardsInPlay);
        opponentState = gameStateTransferable.playerState;
        playerState = gameStateTransferable.opponentState;
        CardUtils.clearCardsFromHand();
        CardUtils.clearCardsFromCity();
        CardUtils.addCardsToHand(cardsInHand);
        CardUtils.addCardsToCity(cardsInPlay);
        resourceManager = gameStateTransferable.resourceManager;
        GameUtils.updatePlayer();
        GameUtils.updateMarkers(gameStateTransferable.markerGroup);
        GameUtils.updateMeadow();
        if (playerState.turnPriority){
            GameUtils.blockScreen(false);
        }else if (opponentState.turnPriority){
            GameUtils.blockScreen(true);
        }
    }

    public static void switchPlayers(GameAction gameAction){
        GameActionUtils.createAndSaveGameAction(getPlayerState(), gameAction);
        if (Boolean.TRUE.equals(EverdellApplication.solo)){
            if (!opponentState.getGameOver()){
                PlayerNumber tempNumber = playerNumber;
                playerNumber = opponentNumber;
                opponentNumber = tempNumber;


                PlayerState tempState = playerState;
                playerState = opponentState;
                opponentState = tempState;

                GameUtils.switchPlayers();
            } else if (opponentState.getGameOver() && playerState.getGameOver()){
                DialogUtils.showGameOverAlert();
            }
        } else {
            if (!opponentState.getGameOver()){
                GameUtils.blockScreen(true);
                GameUtils.sendUpdate();
            } else if (opponentState.getGameOver() && playerState.getGameOver()){
                GameUtils.sendUpdate();
                DialogUtils.showGameOverAlert();
            }
        }
    }

    public static GameStateTransferable packageGameState(){
        GameUtils.blockScreen(true);
        return new GameStateTransferable(getResourceManager(), getPlayerState(), getOpponentState(), GameUtils.getAllMarkers().stream().map(x -> {
            Circle circle = (Circle) x;
            return (Marker)circle.getUserData();
        }).toList());
    }

    public static GameStateTransferable packageGameStateWithAllMarkers(){
        if (!EverdellApplication.solo){
            GameUtils.blockScreen(true);
        }
        return new GameStateTransferable(getResourceManager(), getPlayerState(), getOpponentState(), GameUtils.getAllMarkers().stream().map(x -> {
            Circle circle = (Circle) x;
            if (circle.getUserData() instanceof Marker marker)
                return marker;
            else
                throw new IllegalArgumentException("Invalid marker type: " + circle.getUserData());
        }).toList());
    }
}
