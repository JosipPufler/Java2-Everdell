package hr.algebra.everdell.models;

import java.awt.*;

public enum PlayerNumber {
    ONE(Color.RED),
    TWO(Color.BLUE);

    public Color getPlayerColor() {
        return playerColor;
    }

    final Color playerColor;
    PlayerNumber(Color color) {
        playerColor = color;
    }
}
