package hr.algebra.everdell.utils;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.models.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class XmlUtils {
    private XmlUtils() {}

    private static ResourceManager resourceManager = ResourceManagerSingleton.getInstance();

    private static final String DTD = "dtd/gameActions.dtd";
    private static final String DOCTYPE = "DOCTYPE";
    public static final String XML_FILE_NAME = "xml/gameActions.xml";

    public static void saveGameAction(PlayerState playerState, GameAction gameAction) {
        GameActionTransferable gameActionTransferable = new GameActionTransferable(gameAction, playerState.cardsInPlay, playerState.cardsInHand, playerState.resources, playerState.getFreeWorkers(), resourceManager.getResourcePool(), playerState.calculatePoints(), resourceManager.getDeckSize());

        if (!Files.exists(Path.of(XML_FILE_NAME))) {
            try {
                Document document = createDocument(GameActionTag.GAME_ACTIONS.getTag());
                appendGameActionElement(gameActionTransferable, document);
                saveDocument(document, XML_FILE_NAME);
            } catch (ParserConfigurationException | TransformerException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                List<GameActionTransferable> gameActions = XmlParseUtils.parse(XML_FILE_NAME);
                gameActions.add(gameActionTransferable);
                Document document = createDocument(GameActionTag.GAME_ACTIONS.getTag());
                for (GameActionTransferable ga : gameActions) {
                    appendGameActionElement(ga, document);
                }
                saveDocument(document, XML_FILE_NAME);
            } catch (ParserConfigurationException | TransformerException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Document createDocument(String element) throws ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        DOMImplementation dom = builder.getDOMImplementation();
        DocumentType docType = dom.createDocumentType(DOCTYPE, null, DTD);
        return dom.createDocument(null, element, docType);
    }

    private static void appendGameActionElement(GameActionTransferable gameActionTransferable, Document document) {
        GameAction gameAction = gameActionTransferable.getAction();

        Element element = document.createElement(GameActionTag.GAME_ACTION.getTag());
        document.getDocumentElement().appendChild(element);
        element.setAttribute(GameActionTag.ATT_TIME.getTag(), gameAction.getDateTime().toString());
        element.setAttribute(GameActionTag.ATT_GAME_ACTION_TYPE.getTag(), gameAction.getGameActionType().name());
        element.setAttribute(GameActionTag.ATT_PLAYER_NUMBER.getTag(), gameAction.getPlayerNumber().name());

        if (gameAction.getGameActionType() == GameActionType.PLAY_CARD){
            element.appendChild(createElement(document, GameActionTag.CARD_PLAYED.getTag(), gameAction.getGameActionObject().getClass().getCanonicalName()));
        } else if (gameAction.getGameActionType() == GameActionType.PREPARE_FOR_SEASON){
            element.appendChild(createElement(document, GameActionTag.SEASON.getTag(), ((Season)gameAction.getGameActionObject()).name()));
        } else if (gameAction.getGameActionType() == GameActionType.PLACE_WORKER){
            Element location = document.createElement(GameActionTag.LOCATION.getTag());
            element.appendChild(location);
            location.appendChild(createElement(document, GameActionTag.NAME.getTag(), ((Marker)gameAction.getGameActionObject()).getName()));
            location.appendChild(createElement(document, GameActionTag.X.getTag(), String.valueOf(((Marker)gameAction.getGameActionObject()).getX())));
            location.appendChild(createElement(document, GameActionTag.Y.getTag(), String.valueOf(((Marker)gameAction.getGameActionObject()).getY())));
        }

        Element playerState = document.createElement(GameActionTag.PLAYER_STATE.getTag());

        Element resources = document.createElement(GameActionTag.RESOURCE_GROUP.getTag());
        Element cardsInHand = document.createElement(GameActionTag.CARDS_IN_HAND.getTag());
        Element cardsInPlay = document.createElement(GameActionTag.CARDS_IN_PLAY.getTag());
        Element boardResources = document.createElement(GameActionTag.BOARD_RESOURCES.getTag());
        element.appendChild(playerState);

        playerState.appendChild(createElement(document, GameActionTag.FREE_WORKERS.getTag(), String.valueOf(gameActionTransferable.getFreeWorkers())));
        playerState.appendChild(resources);
        playerState.appendChild(cardsInHand);
        playerState.appendChild(cardsInPlay);
        playerState.appendChild(boardResources);

        for (Card card : gameActionTransferable.getCardsInPlay()){
            cardsInPlay.appendChild(createElement(document, GameActionTag.CARD_IN_PLAY.getTag(), card.getClass().getCanonicalName()));
        }
        for (Card card : gameActionTransferable.getCardsInHand()){
            cardsInHand.appendChild(createElement(document, GameActionTag.CARD_IN_HAND.getTag(), card.getClass().getCanonicalName()));
        }

        resources.appendChild(createElement(document, GameActionTag.BERRIES.getTag(), String.valueOf(gameActionTransferable.getResourceGroup().getBerries())));
        resources.appendChild(createElement(document, GameActionTag.RESIN.getTag(), String.valueOf(gameActionTransferable.getResourceGroup().getResin())));
        resources.appendChild(createElement(document, GameActionTag.PEBBLES.getTag(), String.valueOf(gameActionTransferable.getResourceGroup().getPebbles())));
        resources.appendChild(createElement(document, GameActionTag.TWIGS.getTag(), String.valueOf(gameActionTransferable.getResourceGroup().getTwigs())));
        resources.appendChild(createElement(document, GameActionTag.POINTS.getTag(), String.valueOf(gameActionTransferable.getPoints())));

        boardResources.appendChild(createElement(document, GameActionTag.BOARD_BERRIES.getTag(), String.valueOf(gameActionTransferable.getBoardResourceGroup().getBerries())));
        boardResources.appendChild(createElement(document, GameActionTag.BOARD_RESIN.getTag(), String.valueOf(gameActionTransferable.getBoardResourceGroup().getResin())));
        boardResources.appendChild(createElement(document, GameActionTag.BOARD_PEBBLES.getTag(), String.valueOf(gameActionTransferable.getBoardResourceGroup().getPebbles())));
        boardResources.appendChild(createElement(document, GameActionTag.BOARD_TWIGS.getTag(), String.valueOf(gameActionTransferable.getBoardResourceGroup().getTwigs())));
        boardResources.appendChild(createElement(document, GameActionTag.DECK_SIZE.getTag(), String.valueOf(gameActionTransferable.getDeckSize())));

    }

    private static Node createElement(Document document, String tagName, String data) {
        Element element = document.createElement(tagName);
        Text text = document.createTextNode(data);
        element.appendChild(text);
        return element;
    }

    private static void saveDocument(Document document, String filename) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, document.getDoctype().getSystemId());
        transformer.transform(new DOMSource(document), new StreamResult(new File(filename)));
    }

    public static void replay(Label label){
        List<GameActionTransferable> gameActions = XmlParseUtils.parse(XML_FILE_NAME);
        final AtomicInteger counter = new AtomicInteger(0);
        GameUtils.clearMarkerGroups();
        Timeline replay = new Timeline(new KeyFrame(Duration.ZERO, _ -> {

            GameActionTransferable gameActionTransferable = gameActions.get(counter.get());

            CardUtils.clearCardsFromCity();
            CardUtils.clearCardsFromHand();

            CardUtils.addCardsToCity(gameActionTransferable.getCardsInPlay());
            CardUtils.addCardsToHand(gameActionTransferable.getCardsInHand());
            GameState.getPlayerState().resources.replace(gameActionTransferable.getResourceGroup());

            ResourceManagerSingleton.getInstance().getResourcePool().replace(gameActionTransferable.getBoardResourceGroup());

            PlayerState currentPlayer;
            if (gameActionTransferable.getAction().getPlayerNumber() == PlayerNumber.ONE){
                currentPlayer = GameState.getPlayerState();
            } else
                currentPlayer = GameState.getOpponentState();

            if (gameActionTransferable.getAction().getGameActionType() == GameActionType.PLACE_WORKER){
                Marker marker = (Marker) gameActionTransferable.getAction().getGameActionObject();
                List<Marker> allMarkers = new ArrayList<>(GameUtils.getAllMarkers().stream().map(x -> (Marker) x.getUserData()).toList());
                allMarkers.add(marker);
                GameUtils.updateMarkers(allMarkers);
                currentPlayer.deployWorker(false);
                label.setText(gameActionTransferable.getAction().getPlayerNumber().toString() + " placed a worker on " + ((Marker) gameActionTransferable.getAction().getGameActionObject()).getName() + " on " + gameActionTransferable.getAction().getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            } else if (gameActionTransferable.getAction().getGameActionType() == GameActionType.PLAY_CARD){
                label.setText(gameActionTransferable.getAction().getPlayerNumber().toString() + " played card " + ((Card) gameActionTransferable.getAction().getGameActionObject()).getName() + " on " + gameActionTransferable.getAction().getDateTime());
            } else if (gameActionTransferable.getAction().getGameActionType() == GameActionType.PREPARE_FOR_SEASON){
                List<Marker> allMarkers = new ArrayList<>(GameUtils.getAllMarkers().stream().map(x -> (Marker) x.getUserData()).toList());
                List<Marker> filteredMarkers = allMarkers.stream().filter(x -> x.getPlayerNumber() == gameActionTransferable.getAction().getPlayerNumber()).toList();
                GameUtils.updateMarkers(filteredMarkers);
                currentPlayer.setCurrentSeason((Season) gameActionTransferable.getAction().getGameActionObject());

                label.setText(gameActionTransferable.getAction().getPlayerNumber().toString() + " prepared for season " + ((Season) gameActionTransferable.getAction().getGameActionObject()).name() + " on " + gameActionTransferable.getAction().getDateTime());
            }

            GameUtils.updatePlayer();

            counter.set(counter.get() + 1);
        }), new KeyFrame(Duration.seconds(3)));
        replay.setCycleCount(gameActions.size());
        replay.play();
        replay.setOnFinished(event -> DialogUtils.showAlert(Alert.AlertType.INFORMATION, "Replay over", "You have reached the end of the replay"));
    }
}
