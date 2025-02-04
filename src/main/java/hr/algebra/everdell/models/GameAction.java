package hr.algebra.everdell.models;

import hr.algebra.everdell.utils.LocalDateTimeXmlAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@XmlRootElement
@NoArgsConstructor
@XmlSeeAlso({Location.class, BaseCard.class})
public class GameAction implements Serializable {
    PlayerNumber playerNumber;
    GameActionType gameActionType;
    Object gameActionObject;
    LocalDateTime dateTime;

    @XmlElement
    @XmlJavaTypeAdapter(type=LocalDateTime.class, value= LocalDateTimeXmlAdapter.class)
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @XmlElement
    public GameActionType getGameActionType() {
        return gameActionType;
    }

    @XmlElement(type = Object.class)
    public Object getGameActionObject() {
        return gameActionObject;
    }

    @XmlElement
    public PlayerNumber getPlayerNumber() {
        return playerNumber;
    }

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
