package hr.algebra.everdell.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Getter
public class GameAction {
    GameActionType gameAction;
    Object gameActionObject;
    LocalDateTime dateTime;

    public GameAction(GameActionType gameAction, Object gameActionObject) {
        dateTime = LocalDateTime.now();
        this.gameAction = gameAction;
        if (gameAction.getClasses().contains(gameActionObject.getClass())) {
            this.gameActionObject = gameActionObject;
        } else {
            throw new ClassCastException(gameActionObject.getClass().getName());
        }
    }
}
