package hr.algebra.everdell.threads;

import hr.algebra.everdell.models.GameActionTransferable;
import hr.algebra.everdell.models.GameAction;
import hr.algebra.everdell.models.PlayerState;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.ResourceManagerSingleton;
import hr.algebra.everdell.utils.XmlUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class GameActionThread {

    protected static Boolean FILE_ACCESS_IN_PROGRESS = false;

    public synchronized void saveTheLastGameMove(PlayerState playerState, GameAction gameAction) {
        while(FILE_ACCESS_IN_PROGRESS) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        List<GameActionTransferable> finalGameMoveList = new ArrayList<>();

        if (Files.exists(Path.of(FileUtils.GAME_MOVES_FILE_NAME))) {
            try {
                List<GameActionTransferable> gameMoves = loadGameMoveList();
                finalGameMoveList.addAll(gameMoves);
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

        finalGameMoveList.add(new GameActionTransferable(gameAction, playerState.cardsInPlay, playerState.cardsInHand, playerState.resources, playerState.getFreeWorkers(), ResourceManagerSingleton.getInstance().getResourcePool(), playerState.calculatePoints(), ResourceManagerSingleton.getInstance().getDeckSize()));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FileUtils.GAME_MOVES_FILE_NAME))) {
            oos.writeObject(finalGameMoveList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XmlUtils.saveGameAction(playerState, gameAction);

        FILE_ACCESS_IN_PROGRESS = false;

        notifyAll();
    }

    public synchronized List<GameActionTransferable> loadGameMoveList() throws IOException, ClassNotFoundException {

        while(Boolean.TRUE.equals(FILE_ACCESS_IN_PROGRESS)) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        FILE_ACCESS_IN_PROGRESS = true;

        List<GameActionTransferable> gameMoveList = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FileUtils.GAME_MOVES_FILE_NAME))) {
            if (ois.available() > 0) {
                gameMoveList = new ArrayList<>((List<GameActionTransferable>) ois.readObject());
            }
        } catch (EOFException e){
            System.err.println("Unexpected end of file");
        }

        FILE_ACCESS_IN_PROGRESS = false;

        notifyAll();

        return gameMoveList;
    }
}
