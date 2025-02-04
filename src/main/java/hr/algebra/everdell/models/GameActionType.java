package hr.algebra.everdell.models;

import hr.algebra.everdell.interfaces.Destination;
import lombok.Getter;

import java.util.List;

@Getter
public enum GameActionType {
    PLACE_WORKER(List.of(Location.class, Destination.class), "Placed a worker"),
    PLAY_CARD(List.of(BaseCard.class), "Played a card"),
    PREPARE_FOR_SEASON(List.of(Season.class), "Prepared for Season"),;

    private final List<Class<?>> classes;
    private final String gameActionText;

    GameActionType(List<Class<?>> classes, String gameActionText) {
        this.classes = classes;
        this.gameActionText = gameActionText;
    }
}
