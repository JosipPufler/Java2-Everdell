package hr.algebra.everdell.models;

import lombok.Getter;

@Getter
public enum GameActionTag {
    GAME_ACTIONS("GameActions"),

    GAME_ACTION("GameAction"),
    ATT_PLAYER_NUMBER("player-number"),
    ATT_GAME_ACTION_TYPE("game-action-type"),
    ATT_TIME("time"),

    CARD_PLAYED("CardPlayed"),
    SEASON("Season"),
    LOCATION("Location"),
    NAME("Name"),
    X("X"),
    Y("Y"),

    PLAYER_STATE("PlayerState"),

    RESOURCE_GROUP("ResourceGroup"),
    BERRIES("Berries"),
    RESIN("Resin"),
    PEBBLES("Pebbles"),
    TWIGS("Twigs"),
    POINTS("Points"),

    CARDS_IN_HAND("CardsInHand"),
    CARDS_IN_PLAY("CardsInPlay"),
    CARD_IN_PLAY("CardInPlay"),
    CARD_IN_HAND("CardInHand"),

    BOARD_RESOURCES("BoardResources"),
    BOARD_BERRIES("BoardBerries"),
    BOARD_RESIN("BoardResin"),
    BOARD_PEBBLES("BoardPebbles"),
    BOARD_TWIGS("BoardTwigs"),
    DECK_SIZE("DeckSize"),

    FREE_WORKERS("FreeWorkers"),;

    private final String tag;

    GameActionTag(String tag) {
        this.tag = tag;
    }
}
