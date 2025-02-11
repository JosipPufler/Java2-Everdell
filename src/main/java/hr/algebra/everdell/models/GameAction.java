package hr.algebra.everdell.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Getter
public class GameAction implements Serializable {
    PlayerNumber playerNumber;
    GameActionType gameActionType;
    Object gameActionObject;

    @Getter
    LocalDateTime dateTime;

    public GameAction(PlayerNumber playerNumber, GameActionType gameActionType, Object gameActionObject) {
        this(playerNumber, gameActionType, gameActionObject, LocalDateTime.now());
    }

    public GameAction(PlayerNumber playerNumber, GameActionType gameActionType, Object gameActionObject, LocalDateTime dateTime) {
        this.playerNumber = playerNumber;
        this.dateTime = dateTime;
        this.gameActionType = gameActionType;
        if (gameActionType.getClasses().stream().anyMatch(x -> x.isInstance(gameActionObject))) {
            this.gameActionObject = gameActionObject;
        } else {
            throw new ClassCastException(gameActionObject.getClass().getName());
        }
    }
}
