package hr.algebra.everdell.threads;

import hr.algebra.everdell.models.GameAction;
import hr.algebra.everdell.models.PlayerState;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SaveGameActionThread extends GameActionThread implements Runnable {

    private PlayerState playerState;
    private GameAction gameMove;

    @Override
    public void run() {
        saveTheLastGameMove(playerState, gameMove);
    }
}
