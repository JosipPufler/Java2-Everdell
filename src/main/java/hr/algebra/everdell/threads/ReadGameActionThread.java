package hr.algebra.everdell.threads;

import hr.algebra.everdell.models.GameAction;
import javafx.application.Platform;
import javafx.scene.control.Label;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ReadGameActionThread extends GameActionThread implements Runnable {

    private Label label;

    @Override
    public void run() {
        try {
            List<?> gameActions = loadGameMoveList();

            if(!gameActions.isEmpty()) {
                GameAction gameAction = (GameAction) gameActions.get(gameActions.size() - 1);
                Platform.runLater(() -> {
                    label.setText(gameAction.getGameActionType().getGameActionText()
                            + ": "
                            + gameAction.getGameActionObject().toString()
                            + " at "
                            + gameAction.getDateTime().toString()
                            + ")");
                });
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
