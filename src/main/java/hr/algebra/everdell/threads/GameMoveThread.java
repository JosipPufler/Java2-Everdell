package hr.algebra.everdell.threads;

import hr.algebra.everdell.models.GameAction;
import hr.algebra.everdell.utils.FileUtils;
import hr.algebra.everdell.utils.XmlUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class GameMoveThread {

    protected static Boolean FILE_ACCESS_IN_PROGRESS = false;

    public synchronized void saveTheLastGameMove(GameAction gameAction) {

        while(FILE_ACCESS_IN_PROGRESS) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        List<GameAction> finalGameMoveList = new ArrayList<>();

        if (Files.exists(Path.of(FileUtils.GAME_MOVES_FILE_NAME))) {
            try {
                List<GameAction> gameMoves = (List<GameAction>) loadGameMoveList();
                finalGameMoveList.addAll(gameMoves);
            } catch (IOException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

        finalGameMoveList.add(gameAction);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FileUtils.GAME_MOVES_FILE_NAME))) {
            oos.writeObject(finalGameMoveList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        XmlUtils.saveGameAction(gameAction);

        FILE_ACCESS_IN_PROGRESS = false;

        notifyAll();
    }

    public synchronized List<GameAction> loadGameMoveList() throws IOException, ClassNotFoundException {

        while(FILE_ACCESS_IN_PROGRESS) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        FILE_ACCESS_IN_PROGRESS = true;

        List<GameAction> gameMoveList = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FileUtils.GAME_MOVES_FILE_NAME))) {
            if (ois.available() > 0) {
                gameMoveList = new ArrayList<>((List<GameAction>) ois.readObject());
            }
        } catch (EOFException e){
            System.err.println("Unexpected end of file");
        }

        FILE_ACCESS_IN_PROGRESS = false;

        notifyAll();

        return gameMoveList;
    }
}
