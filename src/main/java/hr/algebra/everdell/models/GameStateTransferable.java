package hr.algebra.everdell.models;

import hr.algebra.everdell.utils.ResourceManager;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class GameStateTransferable implements Serializable {
    ResourceManager resourceManager;
    PlayerState playerState;
    PlayerState opponentState;

    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public PlayerState getOpponentState() {
        return opponentState;
    }
}
