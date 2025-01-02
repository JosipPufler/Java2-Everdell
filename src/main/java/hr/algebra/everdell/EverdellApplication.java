package hr.algebra.everdell;

import hr.algebra.everdell.controllers.EverdellMainController;
import hr.algebra.everdell.models.ConfigurationKey;
import hr.algebra.everdell.models.GameState;
import hr.algebra.everdell.models.GameStateTransferable;
import hr.algebra.everdell.models.PlayerNumber;
import hr.algebra.everdell.utils.ConfigurationReader;
import hr.algebra.everdell.utils.DialogUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class EverdellApplication extends Application {
    public static Boolean solo = true;

    @Override
    public void start(Stage stage) throws IOException {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        FXMLLoader fxmlLoader = new FXMLLoader(EverdellApplication.class.getResource("MainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Everdell");
        stage.setScene(scene);
        stage.setWidth(900);
        stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - stage.getWidth());
        stage.setY(0);
        stage.setOnCloseRequest(_ -> Platform.exit());
        stage.show();
    }

    public static void main(String[] args) {
        GameState.setPlayer(PlayerNumber.valueOf(args[0]));
        solo = Boolean.valueOf(args[1]);
        if (!solo){
            if(PlayerNumber.TWO.name().equals(GameState.getPlayerState().getPlayerNumber().name())) {
                Thread serverThread = new Thread(() -> acceptRequestsFromPlayer(
                        ConfigurationReader.getIntegerValueForKey(ConfigurationKey.PLAYER_TWO_SERVER_PORT)));
                serverThread.start();
            }
            else if(PlayerNumber.ONE.name().equals(GameState.getPlayerState().getPlayerNumber().name())) {
                Thread serverThread = new Thread(() -> acceptRequestsFromPlayer(
                        ConfigurationReader.getIntegerValueForKey(ConfigurationKey.PLAYER_ONE_SERVER_PORT)));
                serverThread.start();
            }
        }
        launch();
    }

    private static void acceptRequestsFromPlayer(Integer port) {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.err.printf("Server listening on port: %d%n", serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.err.printf("Client connected from port %s%n", clientSocket.getPort());
                new Thread(() -> processSerializableClient(clientSocket)).start();
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processSerializableClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());){
            GameStateTransferable gameState = (GameStateTransferable)ois.readObject();
            Platform.runLater(() -> GameState.loadGameState(gameState));

            Boolean gameOver = GameState.getPlayerState().getGameOver() && GameState.getOpponentState().getGameOver();

            if (gameOver){
                EverdellMainController.disable(true);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game over");
                if (GameState.getPlayerState().calculatePoints() != GameState.getOpponentState().calculatePoints()){
                    alert.setHeaderText("We have a winner");
                    if (GameState.getPlayerState().calculatePoints() > GameState.getOpponentState().calculatePoints())
                        alert.setContentText("You are the winner");
                    else
                        alert.setContentText("You lost");
                }
                else
                    alert.setHeaderText("It's a tie");
            } else {
                EverdellMainController.disable(false);
            }

            System.out.println("Game state successfuly received: " + gameState);
            oos.writeObject("Success!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void sendRequestFromPlayer(String hostName, Integer port) {
        try (Socket clientSocket = new Socket(hostName, port)){
            System.err.printf("Client is connecting to %s:%d%n", clientSocket.getInetAddress(), clientSocket.getPort());

            sendSerializableRequest(clientSocket);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void sendSerializableRequest(Socket client) throws IOException, ClassNotFoundException {
        ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
        oos.writeObject(GameState.generatePackage());
        System.out.printf("Received from the server: ", ois.readObject());
    }
}