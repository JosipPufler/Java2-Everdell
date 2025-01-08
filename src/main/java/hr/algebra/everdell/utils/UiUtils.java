package hr.algebra.everdell.utils;

import hr.algebra.everdell.models.GameState;
import hr.algebra.everdell.models.Marker;
import hr.algebra.everdell.models.PlayerState;
import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.Objects;

public class UiUtils {
    private UiUtils() {}

    public static Boolean placeMarker(Marker marker, Boolean opponent, Group playerGroup, Group opponentGroup) {
        PlayerState playerState, opponentState;
        if (opponent) {
            playerState = GameState.getOpponentState();
            opponentState = GameState.getPlayerState();
        } else {
            playerState = GameState.getPlayerState();
            opponentState = GameState.getOpponentState();
        }
        String id = playerState.getPlayerName() + '_' + marker.name.split("_", 2)[1];
        String opponentId = opponentState.getPlayerName() + '_' + marker.name.split("_", 2)[1];
        if ((marker.location.isOpen()
                || opponentGroup.getChildren().stream().noneMatch(o -> Objects.equals(o.getId(), opponentId)))
                && playerGroup.getChildren().stream().noneMatch(o -> Objects.equals(o.getId(), id))
                && playerState.getFreeWorkers() > 0){
            Circle circle = new Circle(marker.x, marker.y - 28, 10, Paint.valueOf(String.format("#%06x", playerState.getPlayerNumber().getPlayerColor().getRGB() & 0xFFFFFF)));
            circle.setUserData(marker.location);
            circle.setId(id);
            playerGroup.getChildren().add(circle);
            playerState.deployWorker(false);
            return true;
        }
        return false;
    }
}
