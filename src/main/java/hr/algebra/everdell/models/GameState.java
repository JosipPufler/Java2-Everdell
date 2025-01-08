package hr.algebra.everdell.models;

import hr.algebra.everdell.EverdellApplication;
import hr.algebra.everdell.utils.GameUtils;
import hr.algebra.everdell.utils.ResourceManager;
import hr.algebra.everdell.utils.ResourceManagerFactory;
import javafx.scene.shape.Circle;
import lombok.Getter;

public class GameState {
    private GameState(){}

    static PlayerNumber playerNumber;
    static PlayerNumber opponentNumber;

    @Getter
    static ResourceManager resourceManager = ResourceManagerFactory.getInstance();
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
        opponentState = gameStateTransferable.playerState;
        playerState = gameStateTransferable.opponentState;
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

    public static void switchPlayers(){
        if (EverdellApplication.solo){
            PlayerNumber tempNumber = playerNumber;
            playerNumber = opponentNumber;
            opponentNumber = tempNumber;


            PlayerState tempState = playerState;
            playerState = opponentState;
            opponentState = tempState;

            GameUtils.switchPlayers();
        } else {
            GameUtils.blockScreen(true);
            GameUtils.sendUpdate();
        }
    }

    public static GameStateTransferable packageGameState(){
        GameUtils.blockScreen(true);
        return new GameStateTransferable(getResourceManager(), getPlayerState(), getOpponentState(), GameUtils.getMarkerGroup().getChildren().stream().map(x -> {
            Circle circle = (Circle) x;
            return new Marker(circle.getCenterX(), circle.getCenterY(), circle.getId(), (Location) circle.getUserData());
        }).toList());
    }
}
