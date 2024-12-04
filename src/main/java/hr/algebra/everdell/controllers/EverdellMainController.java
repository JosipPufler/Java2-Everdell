package hr.algebra.everdell.controllers;

import hr.algebra.everdell.models.Card;
import hr.algebra.everdell.models.PlayerState;
import hr.algebra.everdell.utils.ResourceManager;
import hr.algebra.everdell.utils.PlayerStateSingleton;
import hr.algebra.everdell.utils.ResourcePoolManagerFactory;
import hr.algebra.everdell.utils.dialogs.CardChooseDialogPane;
import hr.algebra.everdell.utils.dialogs.MultiResourceDialogPane;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class EverdellMainController {

    static final int STARTING_FOREST_DECK_SIZE = 11;
    static final int STARTING_EVENT_DECK_SIZE = 16;
    static final int STARTING_HAND_SIZE = 5;
    static final int MAX_HAND_SIZE = 8;

    static final ResourceManager resourceManager = ResourcePoolManagerFactory.getInstance();

    private static final PlayerState playerState = PlayerStateSingleton.getInstance();

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

    @FXML
    CityController cityController;

    @FXML
    Stage cityStage= new Stage();

    Text statsText = new Text();

    public void initialize() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/hr/algebra/everdell/City.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 750);
        cityStage.setTitle("Your city");
        cityStage.setScene(scene);
        cityStage.initStyle(StageStyle.UTILITY);
        cityStage.setX(0);
        cityStage.setY(0);
        cityStage.setOnCloseRequest(event -> {
            Platform.exit();
        });
        cityStage.show();

        cityController = fxmlLoader.getController();

        drawCardsFromMainDeck(STARTING_HAND_SIZE);
        cityController.insertCards(playerState.cardsInHand);

        stpYourStockpile.getChildren().add(statsText);
        updateResourcePool();
    }

    private void drawCardsFromMainDeck(int cardsToDraw) {
        List<Card> cardsDrawn = resourceManager.tryDrawFromMainDeck(cardsToDraw);
        playerState.cardsInHand.addAll(cardsDrawn);
        while (playerState.cardsInHand.size() > MAX_HAND_SIZE) {
            playerState.cardsInHand.removeLast();
        }
    }

    private void updateResourcePool() {
        btnBerries.setText(String.valueOf(resourceManager.getResourcePool().getBerries()));
        btnPebbles.setText(String.valueOf(resourceManager.getResourcePool().getPebbles()));
        btnTwigs.setText(String.valueOf(resourceManager.getResourcePool().getTwigs()));
        btnResin.setText(String.valueOf(resourceManager.getResourcePool().getResin()));
        lblDeck.setText(String.valueOf(resourceManager.getDeckSize()));

        statsText.setText(String.format("Your stockpile:\nPebbles: %s\nTwigs: %s\nResin: %s\nBerries: %s", playerState.resources.getPebbles(), playerState.resources.getTwigs(), playerState.resources.getResin(), playerState.resources.getBerries()));
    }

    public void locationSelected_3T(MouseEvent event) {
        resourceManager.tryTakeTwigs(playerState.resources, 3);
        updateResourcePool();
    }

    public void locationSelected_2T1C(MouseEvent event) {
        resourceManager.tryTakeTwigs(playerState.resources, 2);
        drawCardsFromMainDeck(1);
        updateResourcePool();
    }

    public void locationSelected_2R(MouseEvent event) {
        resourceManager.tryTakeResin(playerState.resources, 2);
        updateResourcePool();
    }

    public void locationSelected_1R1C(MouseEvent event) {
        resourceManager.tryTakeResin(playerState.resources, 1);
        drawCardsFromMainDeck(1);
        updateResourcePool();
    }

    public void locationSelected_2C1Pt(MouseEvent event) {
        playerState.addPoints(1);
        drawCardsFromMainDeck(2);
        updateResourcePool();
    }

    public void locationSelected_1P(MouseEvent event) {
        resourceManager.tryTakePebbles(playerState.resources, 1);
        updateResourcePool();

        Dialog<ButtonType> dialog = new Dialog<>();
        MultiResourceDialogPane multiResourceDialogPane = new MultiResourceDialogPane(5);
        dialog.setDialogPane(multiResourceDialogPane);
        dialog.showAndWait();
        if (dialog.getResult() == ButtonType.APPLY) {
            System.out.println(multiResourceDialogPane.getResourcePool());
        }
    }

    public void locationSelected_1B(MouseEvent event) {
        resourceManager.tryTakeBerries(playerState.resources, 1);
        updateResourcePool();


        Dialog<ButtonType> dialog = new Dialog<>();
        CardChooseDialogPane cardChooseDialogPane = new CardChooseDialogPane(playerState.cardsInHand);
        dialog.setDialogPane(cardChooseDialogPane);
        dialog.showAndWait();
        if (dialog.getResult() == ButtonType.OK) {
            System.out.println(cardChooseDialogPane.getSelectedCard().getName());
        }
    }

    public void locationSelected_1B1C(MouseEvent event) {
        resourceManager.tryTakeBerries(playerState.resources, 1);
        drawCardsFromMainDeck(1);
        updateResourcePool();
    }
}