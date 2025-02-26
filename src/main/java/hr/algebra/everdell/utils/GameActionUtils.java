package hr.algebra.everdell.utils;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.threads.SaveGameActionThread;
import javafx.scene.control.Alert;

import java.io.*;

public class GameActionUtils {
    private static String SAVE_GAME_FILE_NAME = "game/savedGame.dat";

    private GameActionUtils() {}

    public static void createAndSaveGameAction(PlayerState playerState, GameAction gameAction) {
        SaveGameActionThread saveGameMoveThread = new SaveGameActionThread(playerState, gameAction);
        Thread thread = new Thread(saveGameMoveThread);
        thread.start();
    }

    public static void saveGameToFile() {
        try (ObjectOutputStream objectOutputStream =
                     new ObjectOutputStream(new FileOutputStream(SAVE_GAME_FILE_NAME))) {
            objectOutputStream.writeObject(GameState.packageGameStateWithAllMarkers());

            DialogUtils.showAndWaitAlert(Alert.AlertType.INFORMATION,
                    "Spremanje igre",
                    "Igra je uspješno spremljena!");

        } catch (IOException e) {
            DialogUtils.showAndWaitAlert(Alert.AlertType.ERROR, "Pogreška kod spremanja",
                    "Došlo je do pogreške kod spremanja igre!");
            throw new RuntimeException(e);
        }
    }

    public static void loadGameFromFile() {
        GameStateTransferable loadedGameState = null;
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(SAVE_GAME_FILE_NAME))) {
            loadedGameState = (GameStateTransferable) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        GameState.loadGameState(loadedGameState);
    }
}
