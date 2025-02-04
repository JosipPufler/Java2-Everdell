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
public class ReadGameMoveThread extends GameMoveThread implements Runnable {

    private Label label;

    @Override
    public void run() {
        try {
            List<?> gameMoves = loadGameMoveList();

            if(!gameMoves.isEmpty()) {
                GameAction gameMove = (GameAction) gameMoves.get(gameMoves.size() - 1);
                Platform.runLater(() -> {
                    label.setText(gameMove.getGameActionType().getGameActionText()
                            + ": "
                            + gameMove.getGameActionObject().toString()
                            + " at "
                            + gameMove.getDateTime().toString()
                            + ")");
                });
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
