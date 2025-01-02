package hr.algebra.everdell.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GameAction {
    GameActionType gameAction;
    LocalDateTime dateTime;
}
