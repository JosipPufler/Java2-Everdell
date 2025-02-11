package hr.algebra.everdell.utils;

import hr.algebra.everdell.interfaces.Placeable;
import hr.algebra.everdell.models.*;
import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.Objects;

public class UiUtils {
    private UiUtils() {}

    public static Boolean placeMarker(Marker marker, Boolean opponent, Group playerGroup, Group opponentGroup) {
        PlayerState playerState = GameState.getPlayerState();
        if (canPlaceMarker(marker.getLocation(), opponent, playerGroup, opponentGroup)){
            Circle circle = new Circle(marker.getX(), marker.getY() - 28, 10, Paint.valueOf(String.format("#%06x", marker.getPlayerNumber().getPlayerColor().getRGB() & 0xFFFFFF)));
            circle.setUserData(marker);
            circle.setId(playerState.getPlayerName() + '_' + marker.getName().split("_", 2)[1]);
            playerGroup.getChildren().add(circle);
            playerState.deployWorker(false);
            return true;
        }
        return false;
    }

    public static Boolean canPlaceMarker(Placeable location, Boolean opponent, Group playerGroup, Group opponentGroup) {
        if (location instanceof Event event && !event.canPlace())
            return false;

        PlayerState playerState;
        PlayerState opponentState;
        if (opponent) {
            playerState = GameState.getOpponentState();
            opponentState = GameState.getPlayerState();
        } else {
            playerState = GameState.getPlayerState();
            opponentState = GameState.getOpponentState();
        }
        String id = playerState.getPlayerName() + '_' + location.getName().split("_", 2)[1];
        String opponentId = opponentState.getPlayerName() + '_' + location.getName().split("_", 2)[1];

        return ((location.isOpen() && playerGroup.getChildren().stream().noneMatch(o -> Objects.equals(o.getId(), id)))
                || opponentGroup.getChildren().stream().noneMatch(o -> Objects.equals(o.getId(), opponentId))
                && playerGroup.getChildren().stream().noneMatch(o -> Objects.equals(o.getId(), id)))
                && playerState.getFreeWorkers() > 0;
    }
}
