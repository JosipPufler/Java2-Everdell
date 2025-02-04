package hr.algebra.everdell.models;

import lombok.Getter;

@Getter
public enum GameActionTag {
    GAME_ACTION("GameAction"),
    PLAYER_NUMBER("PlayerNumber"),
    GAME_ACTION_TYPE("GameActionType"),
    GAME_ACTION_OBJECT("GameActionObject"),
    TIME("Time");

    private final String tag;

    GameActionTag(String tag) {
        this.tag = tag;
    }
}
