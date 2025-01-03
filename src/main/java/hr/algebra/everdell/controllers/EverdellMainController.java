package hr.algebra.everdell.controllers;

import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.*;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    final List<Ellipse> locations = new ArrayList<>();
    public Group playerOneGroup = new Group();
    public Group playerTwoGroup = new Group();
    Text statsText = new Text();

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

    CityController cityController;
    PlayableCardController handController;
    PlayableCardController meadowController;

    public static void disable(boolean b) {
    }

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
                if (userData instanceof Location && placeMarker(new Marker(event.getSceneX(), event.getSceneY(), ((Location) userData).toShorthandString(), (Location) userData), false)){
                    ((Location) userData).activate(GameState.getPlayerState(), true);
                    updateResourcePool();
                    Location.addLocations((Location) userData);
                    GameState.switchPlayers();
                }
            });
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

    public Boolean placeMarker(Marker marker, Boolean opponent) {
        PlayerState playerState, opponentState;
        if (opponent) {
            playerState = GameState.getOpponentState();
            opponentState = GameState.getPlayerState();
        } else {
            playerState = GameState.getPlayerState();
            opponentState = GameState.getOpponentState();
        }
        Group playerGroup, opponentGroup;
        if (playerState.getPlayerNumber() == PlayerNumber.ONE){
            playerGroup = playerOneGroup;
            opponentGroup = playerTwoGroup;
        }
        else {
            playerGroup = playerTwoGroup;
            opponentGroup = playerOneGroup;
        }
        String id = playerState.getPlayerName() + '_' + marker.name.split("_", 2)[1];
        String opponentId = opponentState.getPlayerName() + '_' + marker.name.split("_", 2)[1];
        if ((marker.location.isOpen()
                || opponentGroup.getChildren().stream().noneMatch(o -> Objects.equals(o.getId(), opponentId)))
                && playerGroup.getChildren().stream().noneMatch(o -> Objects.equals(o.getId(), id))
                && playerState.getFreeWorkers() > 0){
            Circle circle = new Circle(marker.x, marker.y, 10, Paint.valueOf(String.format("#%06x", playerState.getPlayerNumber().getPlayerColor().getRGB() & 0xFFFFFF)));
            circle.setUserData(marker.location);
            playerGroup.getChildren().add(circle);
            circle.setId(id);
            playerState.deployWorker(false);
            return true;
        }
        return false;
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
}