package hr.algebra.everdell.controllers;

import hr.algebra.everdell.EverdellApplication;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.rmi.ChatRemoteService;
import hr.algebra.everdell.rmi.ChatServer;
import hr.algebra.everdell.utils.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class EverdellMainController {
    @FXML
    public Ellipse location_2T1C;
    @FXML
    public Ellipse location_3T;
    @FXML
    public Ellipse location_2R;
    @FXML
    public Ellipse location_1R1C;
    @FXML
    public Ellipse location_2C1Pt;
    @FXML
    public Ellipse location_1P;
    @FXML
    public Ellipse location_1B1C;
    @FXML
    public Ellipse location_1B;
    @FXML
    public TextField tfMessage;
    @FXML
    public TextArea taChat;
    @FXML
    public Tab chatTab;
    @FXML
    TabPane tabPane;
    @FXML
    Pane blockPane;
    @FXML
    AnchorPane anchorPane;
    @FXML
    Button btnBerries;
    @FXML
    Button btnTwigs;
    @FXML
    Button btnResin;
    @FXML
    Button btnPebbles;
    @FXML
    Label lblDeck;
    @FXML
    StackPane stpYourStockpile;

    final List<Ellipse> locations = new ArrayList<>();
    public Group playerOneGroup = new Group();
    public Group playerTwoGroup = new Group();
    Text statsText = new Text();
    CityController cityController;
    PlayableCardController handController;
    PlayableCardController meadowController;
    private static ChatRemoteService chatRemoteService;

    public void initialize() throws IOException {
        cityController = GameUtils.showCity(this);
        handController = GameUtils.showHand(this);
        meadowController = GameUtils.generateMeadow(this);

        location_2T1C.setUserData(new Location(new ResourceGroup(0, 2, 0, 0), 1, 0, true));
        location_3T.setUserData(new Location(new ResourceGroup(0, 3, 0, 0), 0, 0, false));
        location_2R.setUserData(new Location(new ResourceGroup(0, 0, 2, 0), 0, 0, false));
        location_1R1C.setUserData(new Location(new ResourceGroup(0, 0, 1, 0), 1, 0, true));
        location_2C1Pt.setUserData(new Location(new ResourceGroup(0, 0, 0, 0), 2, 1, true));
        location_1P.setUserData(new Location(new ResourceGroup(0, 0, 0, 1), 0, 0, false));
        location_1B1C.setUserData(new Location(new ResourceGroup(1, 0, 0, 0), 1, 0, false));
        location_1B.setUserData(new Location(new ResourceGroup(1, 0, 0, 0), 0, 0, true));
        locations.addAll(List.of(location_2T1C, location_3T, location_2R, location_1R1C, location_2C1Pt, location_1P, location_1B1C, location_1B));

        for (Ellipse location : locations){
            location.setOnMouseClicked(event -> {
                Object userData = location.getUserData();
                Group playerGroup, opponentGroup;
                if (GameState.getPlayerState().getPlayerNumber() == PlayerNumber.ONE){
                    playerGroup = playerOneGroup;
                    opponentGroup = playerTwoGroup;
                }else{
                    playerGroup = playerTwoGroup;
                    opponentGroup = playerOneGroup;
                }
                System.out.println(event.getSceneX());
                System.out.println(event.getSceneY());
                if (userData instanceof Location && UiUtils.placeMarker(new Marker(event.getSceneX(), event.getSceneY(), ((Location) userData).toShorthandString(), (Location) userData), false, playerGroup, opponentGroup)){
                    ((Location) userData).activate(GameState.getPlayerState(), true);
                    updateResourcePool();
                    Location.addLocations((Location) userData);
                    GameState.switchPlayers();
                }
            });
        }
        if (!EverdellApplication.solo) {
            try {
                Registry registry = LocateRegistry.getRegistry(ChatServer.CHAT_HOST_NAME, ChatServer.RMI_PORT);
                chatRemoteService = (ChatRemoteService) registry.lookup(ChatRemoteService.CHAT_REMOTE_OBJECT_NAME);
            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
            ChatUtils.createAndRunChatTimeline(chatRemoteService, taChat);
        } else {
            tabPane.getTabs().remove(chatTab);
        }
        anchorPane.getChildren().add(playerOneGroup);
        anchorPane.getChildren().add(playerTwoGroup);
        stpYourStockpile.getChildren().add(statsText);
        GameUtils.setUpGame();
        updateResourcePool();
    }

    public void updateResourcePool() {
        ResourceManager resourceManager = GameState.getResourceManager();
        PlayerState playerState = GameState.getPlayerState();
        btnBerries.setText(String.valueOf(resourceManager.getResourcePool().getBerries()));
        btnPebbles.setText(String.valueOf(resourceManager.getResourcePool().getPebbles()));
        btnTwigs.setText(String.valueOf(resourceManager.getResourcePool().getTwigs()));
        btnResin.setText(String.valueOf(resourceManager.getResourcePool().getResin()));
        lblDeck.setText(String.valueOf(resourceManager.getDeckSize()));
        System.out.println(resourceManager.getDeckSize());
        statsText.setText(String.format("Your stats:\nPoints: %s\nPebbles: %s\nTwigs: %s\nResin: %s\nBerries: %s\nWorkers: %s/%s\nSeason: %s", playerState.calculatePoints(), playerState.resources.getPebbles(), playerState.resources.getTwigs(), playerState.resources.getResin(), playerState.resources.getBerries(), playerState.getFreeWorkers(), playerState.getMaxWorkers(), playerState.getCurrentSeason()));
    }

    public void changeSeason (MouseEvent event) {
        if (GameState.getPlayerState().getPlayerNumber() == PlayerNumber.ONE)
            playerOneGroup.getChildren().clear();
        else
            playerTwoGroup.getChildren().clear();
        cityController.returnWorkers(GameState.getPlayerState());
        GameState.getPlayerState().nextSeason();
        locations.forEach(x -> {
            Object userData = x.getUserData();
            if (userData instanceof Location){
                ((Location) userData).deactivate();
            }
        });
        updateResourcePool();
    }

    public void updateOpponentGroup(List<Marker> markers){
        if (GameState.getPlayerState().getPlayerNumber() == PlayerNumber.ONE){
            playerTwoGroup.getChildren().clear();
            for (Marker marker : markers){
                Circle circle = new Circle(marker.x, marker.y, 10, Paint.valueOf(String.format("#%06x", PlayerNumber.TWO.getPlayerColor().getRGB() & 0xFFFFFF)));
                circle.setUserData(marker.location);
                circle.setId(GameState.getOpponentState().getPlayerName() + '_' + marker.name.split("_", 2)[1]);
                playerTwoGroup.getChildren().add(circle);
            }
        } else {
            playerOneGroup.getChildren().clear();
            for (Marker marker : markers){
                Circle circle = new Circle(marker.x, marker.y, 10, Paint.valueOf(String.format("#%06x", PlayerNumber.ONE.getPlayerColor().getRGB() & 0xFFFFFF)));
                circle.setUserData(marker.location);
                circle.setId(GameState.getOpponentState().getPlayerName() + '_' + marker.name.split("_", 2)[1]);
                playerOneGroup.getChildren().add(circle);
            }
        }
    }

    public void showMeadow (MouseEvent event){
        GameUtils.showMeadow();
    }

    public void updateTitle(String title){
        ((Stage) stpYourStockpile.getScene().getWindow()).setTitle(title);
    }

    public void sendChatMessage() {
        ChatUtils.sendChatMessage(tfMessage, taChat, chatRemoteService);
    }

    public void generateDocumentation(ActionEvent actionEvent) {
        DocumentationUtils.generateDocumentation();
    }

    public void blockScreen(Boolean show){
        blockPane.setVisible(show);
    }

    public void saveGameState(ActionEvent actionEvent) {

    }
}