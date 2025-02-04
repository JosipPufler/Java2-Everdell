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
    private static ChatRemoteService chatRemoteService;

    public void initialize() throws IOException {
        cityController = GameUtils.showCity(this);
        GameUtils.showHand(this);
        GameUtils.generateMeadow(this);

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
                Group playerGroup, opponentGroup;
                if (GameState.getPlayerState().getPlayerNumber() == PlayerNumber.ONE){
                    playerGroup = playerOneGroup;
                    opponentGroup = playerTwoGroup;
                }else{
                    playerGroup = playerTwoGroup;
                    opponentGroup = playerOneGroup;
                }
                if (location.getUserData() instanceof Location markerLocation && UiUtils.placeMarker(new Marker(event.getSceneX(), event.getSceneY(), markerLocation.toShorthandString(), markerLocation), false, playerGroup, opponentGroup)){
                    markerLocation.activate(GameState.getPlayerState(), true);
                    updateResourcePool();
                    Location.addLocations(markerLocation);
                    GameState.switchPlayers(new GameAction(GameState.getPlayerState().getPlayerNumber(), GameActionType.PLACE_WORKER, markerLocation));
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
        } else
            tabPane.getTabs().remove(chatTab);
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
        statsText.setText(String.format("Your stats:" + "%nPoints: %s" + "%nPebbles: %s" + "%nTwigs: %s" + "%nResin: %s" + "%nBerries: %s" + "%nWorkers: %s/%s" + "%nSeason: %s", playerState.calculatePoints(), playerState.resources.getPebbles(), playerState.resources.getTwigs(), playerState.resources.getResin(), playerState.resources.getBerries(), playerState.getFreeWorkers(), playerState.getMaxWorkers(), playerState.getCurrentSeason()));
    }

    public void changeSeason () {
        if (GameState.getPlayerState().getPlayerNumber() == PlayerNumber.ONE)
            playerOneGroup.getChildren().clear();
        else
            playerTwoGroup.getChildren().clear();
        cityController.returnWorkers(GameState.getPlayerState());
        GameState.getPlayerState().nextSeason();
        locations.forEach(x -> {
            if (x.getUserData() instanceof Location location)
                location.deactivate();
        });
        updateResourcePool();
    }

    public void updateOpponentGroup(List<Marker> markers){
        if (GameState.getPlayerState().getPlayerNumber() == PlayerNumber.ONE){
            playerTwoGroup.getChildren().clear();
            for (Marker marker : markers){
                Circle circle = new Circle(marker.getX(), marker.getY(), 10, Paint.valueOf(String.format("#%06x", PlayerNumber.TWO.getPlayerColor().getRGB() & 0xFFFFFF)));
                circle.setUserData(marker.getLocation());
                circle.setId(GameState.getOpponentState().getPlayerName() + '_' + marker.getName().split("_", 2)[1]);
                playerTwoGroup.getChildren().add(circle);
            }
        } else {
            playerOneGroup.getChildren().clear();
            for (Marker marker : markers){
                Circle circle = new Circle(marker.getX(), marker.getY(), 10, Paint.valueOf(String.format("#%06x", PlayerNumber.ONE.getPlayerColor().getRGB() & 0xFFFFFF)));
                circle.setUserData(marker.getLocation());
                circle.setId(GameState.getOpponentState().getPlayerName() + '_' + marker.getName().split("_", 2)[1]);
                playerOneGroup.getChildren().add(circle);
            }
        }
    }

    public void showMeadow (){
        GameUtils.showMeadow();
    }

    public void updateTitle(String title){
        ((Stage) stpYourStockpile.getScene().getWindow()).setTitle(title);
    }

    public void sendChatMessage() {
        ChatUtils.sendChatMessage(tfMessage, taChat, chatRemoteService);
    }

    public void generateDocumentation() {
        DocumentationUtils.generateDocumentation();
    }

    public void blockScreen(Boolean show){
        blockPane.setVisible(show);
    }

    public void saveGameState(ActionEvent actionEvent) {
        GameActionUtils.saveGameToFile();
    }

    public List<Location> getLocationsDeployable(){
        List<Location> availableLocations = new ArrayList<>();
        for (Ellipse ellipse : locations){
            if (ellipse.getUserData() instanceof Location location)
                availableLocations.add(location);
        }
        return availableLocations;
    }
}