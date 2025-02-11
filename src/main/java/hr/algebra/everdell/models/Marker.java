package hr.algebra.everdell.models;

import hr.algebra.everdell.interfaces.Placeable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Setter
@Getter
public class Marker implements Serializable {
    double x;
    double y;
    String name;
    Placeable location;
    PlayerNumber playerNumber;

    public Marker(double x, double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
        location = null;
        playerNumber = GameState.getPlayerState().getPlayerNumber();
    }

    public Marker(double x, double y, String name, PlayerNumber playerNumber) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.playerNumber = playerNumber;
        location = null;
    }

    public Marker(double x, double y, String name, Placeable location) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.playerNumber = GameState.getPlayerState().getPlayerNumber();
        this.location = location;
    }
}
