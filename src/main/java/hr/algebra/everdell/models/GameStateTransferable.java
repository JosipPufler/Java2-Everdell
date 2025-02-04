package hr.algebra.everdell.models;

import hr.algebra.everdell.utils.ResourceManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@Data
@AllArgsConstructor
public class GameStateTransferable implements Serializable {
    ResourceManager resourceManager;
    PlayerState playerState;
    PlayerState opponentState;
    List<Marker> markerGroup;
}
