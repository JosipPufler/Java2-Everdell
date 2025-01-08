package hr.algebra.everdell.threads;

import hr.algebra.everdell.models.GameAction;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SaveGameMoveThread extends GameMoveThread implements Runnable {

    private GameAction gameMove;

    @Override
    public void run() {
        saveTheLastGameMove(gameMove);
    }
}
