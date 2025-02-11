package hr.algebra.everdell.utils;

import hr.algebra.everdell.controllers.EverdellMainController;
import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.interfaces.Placeable;
import hr.algebra.everdell.models.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.w3c.dom.*;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
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
        CompactGameAction compactGameAction = new CompactGameAction(gameAction, playerState.cardsInPlay, playerState.cardsInHand, playerState.resources, playerState.getFreeWorkers(), resourceManager.getResourcePool(), playerState.calculatePoints(), resourceManager.getDeckSize());

        if (!Files.exists(Path.of(XML_FILE_NAME))) {
            try {
                Document document = createDocument(GameActionTag.GAME_ACTIONS.getTag());
                appendGameActionElement(compactGameAction, document);
                saveDocument(document, XML_FILE_NAME);
            } catch (ParserConfigurationException | TransformerException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                List<CompactGameAction> gameActions = parse(XML_FILE_NAME);
                gameActions.add(compactGameAction);
                Document document = createDocument(GameActionTag.GAME_ACTIONS.getTag());
                for (CompactGameAction ga : gameActions) {
                    appendGameActionElement(ga, document);
                }
                saveDocument(document, XML_FILE_NAME);
            } catch (ParserConfigurationException | TransformerException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static List<CompactGameAction> parse(String xmlFileName) {
        Document document = null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) throws SAXException {
                    System.err.println("Warning: " + exception);
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception;
                }
            });
            document = builder.parse(new File(xmlFileName));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }

        return retrieveGameMoves(document);
    }

    private static Document createDocument(String element) throws ParserConfigurationException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        DOMImplementation dom = builder.getDOMImplementation();
        DocumentType docType = dom.createDocumentType(DOCTYPE, null, DTD);
        return dom.createDocument(null, element, docType);
    }

    private static void appendGameActionElement(CompactGameAction compactGameAction, Document document) {
        GameAction gameAction = compactGameAction.getAction();

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

        playerState.appendChild(createElement(document, GameActionTag.FREE_WORKERS.getTag(), String.valueOf(compactGameAction.getFreeWorkers())));
        playerState.appendChild(resources);
        playerState.appendChild(cardsInHand);
        playerState.appendChild(cardsInPlay);
        playerState.appendChild(boardResources);

        for (Card card : compactGameAction.getCardsInPlay()){
            cardsInPlay.appendChild(createElement(document, GameActionTag.CARD_IN_PLAY.getTag(), card.getClass().getCanonicalName()));
        }
        for (Card card : compactGameAction.getCardsInHand()){
            cardsInHand.appendChild(createElement(document, GameActionTag.CARD_IN_HAND.getTag(), card.getClass().getCanonicalName()));
        }

        resources.appendChild(createElement(document, GameActionTag.BERRIES.getTag(), String.valueOf(compactGameAction.getResourceGroup().getBerries())));
        resources.appendChild(createElement(document, GameActionTag.RESIN.getTag(), String.valueOf(compactGameAction.getResourceGroup().getResin())));
        resources.appendChild(createElement(document, GameActionTag.PEBBLES.getTag(), String.valueOf(compactGameAction.getResourceGroup().getPebbles())));
        resources.appendChild(createElement(document, GameActionTag.TWIGS.getTag(), String.valueOf(compactGameAction.getResourceGroup().getTwigs())));
        resources.appendChild(createElement(document, GameActionTag.POINTS.getTag(), String.valueOf(compactGameAction.getPoints())));

        boardResources.appendChild(createElement(document, GameActionTag.BOARD_BERRIES.getTag(), String.valueOf(compactGameAction.getBoardResourceGroup().getBerries())));
        boardResources.appendChild(createElement(document, GameActionTag.BOARD_RESIN.getTag(), String.valueOf(compactGameAction.getBoardResourceGroup().getResin())));
        boardResources.appendChild(createElement(document, GameActionTag.BOARD_PEBBLES.getTag(), String.valueOf(compactGameAction.getBoardResourceGroup().getPebbles())));
        boardResources.appendChild(createElement(document, GameActionTag.BOARD_TWIGS.getTag(), String.valueOf(compactGameAction.getBoardResourceGroup().getTwigs())));
        boardResources.appendChild(createElement(document, GameActionTag.DECK_SIZE.getTag(), String.valueOf(compactGameAction.getDeckSize())));

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

    private static List<CompactGameAction> retrieveGameMoves(Document document) {
        List<CompactGameAction> gameActions = new ArrayList<>();
        Element documentElement = document.getDocumentElement();
        NodeList nodes = documentElement.getElementsByTagName(GameActionTag.GAME_ACTION.getTag());
        for (int i = 0; i < nodes.getLength(); i++) {
            Element element = (Element) nodes.item(i);
            List<Card> cardsInPlay = new ArrayList<>();
            List<Card> cardsInHand = new ArrayList<>();

            LocalDateTime time = LocalDateTime.parse(element.getAttribute(GameActionTag.ATT_TIME.getTag()));
            GameActionType actionType = GameActionType.valueOf(element.getAttribute(GameActionTag.ATT_GAME_ACTION_TYPE.getTag()));
            PlayerNumber playerNumber = PlayerNumber.valueOf(element.getAttribute(GameActionTag.ATT_PLAYER_NUMBER.getTag()));

            Element playerState = (Element) element.getElementsByTagName(GameActionTag.PLAYER_STATE.getTag()).item(0);

            Element cardsInHandElement = (Element) playerState.getElementsByTagName(GameActionTag.CARDS_IN_HAND.getTag()).item(0);
            Element cardsInPlayElement = (Element) playerState.getElementsByTagName(GameActionTag.CARDS_IN_PLAY.getTag()).item(0);
            NodeList cardsInPlayList = cardsInPlayElement.getElementsByTagName(GameActionTag.CARD_IN_PLAY.getTag());
            NodeList cardsInHandList = cardsInHandElement.getElementsByTagName(GameActionTag.CARD_IN_HAND.getTag());

            extractCards(cardsInPlay, cardsInPlayList);
            extractCards(cardsInHand, cardsInHandList);

            Element resourceGroupElement = (Element)playerState.getElementsByTagName(GameActionTag.RESOURCE_GROUP.getTag()).item(0);

            int freeWorkers = Integer.parseInt(playerState.getElementsByTagName(GameActionTag.FREE_WORKERS.getTag()).item(0).getTextContent());

            int berries = Integer.parseInt(resourceGroupElement.getElementsByTagName(GameActionTag.BERRIES.getTag()).item(0).getTextContent());
            int pebbles = Integer.parseInt(resourceGroupElement.getElementsByTagName(GameActionTag.PEBBLES.getTag()).item(0).getTextContent());
            int resin = Integer.parseInt(resourceGroupElement.getElementsByTagName(GameActionTag.RESIN.getTag()).item(0).getTextContent());
            int twigs = Integer.parseInt(resourceGroupElement.getElementsByTagName(GameActionTag.TWIGS.getTag()).item(0).getTextContent());
            int points = Integer.parseInt(resourceGroupElement.getElementsByTagName(GameActionTag.POINTS.getTag()).item(0).getTextContent());

            Element boardResourcesElement = (Element)playerState.getElementsByTagName(GameActionTag.BOARD_RESOURCES.getTag()).item(0);

            int boardBerries = Integer.parseInt(boardResourcesElement.getElementsByTagName(GameActionTag.BOARD_BERRIES.getTag()).item(0).getTextContent());
            int boardPebbles = Integer.parseInt(boardResourcesElement.getElementsByTagName(GameActionTag.BOARD_PEBBLES.getTag()).item(0).getTextContent());
            int boardResin = Integer.parseInt(boardResourcesElement.getElementsByTagName(GameActionTag.BOARD_RESIN.getTag()).item(0).getTextContent());
            int boardTwigs = Integer.parseInt(boardResourcesElement.getElementsByTagName(GameActionTag.BOARD_TWIGS.getTag()).item(0).getTextContent());
            int deckSize = Integer.parseInt(boardResourcesElement.getElementsByTagName(GameActionTag.DECK_SIZE.getTag()).item(0).getTextContent());


            ResourceGroup resourceGroup = new ResourceGroup(berries, twigs, resin, pebbles);
            ResourceGroup boardResourceGroup = new ResourceGroup(boardBerries, boardTwigs, boardResin, boardPebbles);

            if (actionType == GameActionType.PLACE_WORKER){
                Element location = (Element) element.getElementsByTagName(GameActionTag.LOCATION.getTag()).item(0);
                double x = Double.parseDouble(location.getElementsByTagName(GameActionTag.X.getTag()).item(0).getTextContent());
                double y = Double.parseDouble(location.getElementsByTagName(GameActionTag.Y.getTag()).item(0).getTextContent());
                String name = location.getElementsByTagName(GameActionTag.NAME.getTag()).item(0).getTextContent();
                Marker marker = new Marker(x, y, name, playerNumber);
                gameActions.add(new CompactGameAction(new GameAction(playerNumber, actionType, marker, time), cardsInPlay, cardsInHand, resourceGroup, freeWorkers, boardResourceGroup, points, deckSize));
            }
            else if (actionType == GameActionType.PLAY_CARD){
                Card card = extractCard(element.getElementsByTagName(GameActionTag.CARD_PLAYED.getTag()).item(0).getTextContent());
                gameActions.add(new CompactGameAction(new GameAction(playerNumber, actionType, card, time), cardsInPlay, cardsInHand, resourceGroup, freeWorkers, boardResourceGroup, points, deckSize));
            } else if (actionType == GameActionType.PREPARE_FOR_SEASON) {
                Season season = Season.valueOf(element.getElementsByTagName(GameActionTag.SEASON.getTag()).item(0).getTextContent());
                gameActions.add(new CompactGameAction(new GameAction(playerNumber, actionType, season, time), cardsInPlay, cardsInHand, resourceGroup, freeWorkers, boardResourceGroup, points, deckSize));
            }
        }

        return gameActions;
    }

    private static void extractCards(List<Card> cardsInHand, NodeList cardsInHandList){
        try {
            for (int index = 0; index < cardsInHandList.getLength(); index++) {
                String className = cardsInHandList.item(index).getTextContent();
                Class<?> aClass = Class.forName(className);
                BaseCard o = (BaseCard) aClass.getDeclaredConstructor().newInstance();
                cardsInHand.add(o);
            }
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
        IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static Card extractCard(String className){
        try {
            Class<?> aClass = Class.forName(className);
            BaseCard o = (BaseCard) aClass.getDeclaredConstructor().newInstance();
            return o;
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
                 IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void replay(EverdellMainController mainController, Label label){
        List<CompactGameAction> gameActions = parse(XML_FILE_NAME);
        final AtomicInteger counter = new AtomicInteger(0);

        Timeline replay = new Timeline(new KeyFrame(Duration.ZERO, _ -> {

            CompactGameAction compactGameAction = gameActions.get(counter.get());

            CardUtils.clearCardsFromCity();
            CardUtils.clearCardsFromHand();

            CardUtils.addCardsToCity(compactGameAction.getCardsInPlay());
            CardUtils.addCardsToHand(compactGameAction.getCardsInHand());
            GameState.getPlayerState().resources.replace(compactGameAction.getResourceGroup());

            ResourceManagerSingleton.getInstance().getResourcePool().replace(compactGameAction.getBoardResourceGroup());

            PlayerState currentPlayer;
            if (compactGameAction.getAction().getPlayerNumber() == PlayerNumber.ONE){
                currentPlayer = GameState.getPlayerState();
            } else
                currentPlayer = GameState.getOpponentState();

            if (compactGameAction.getAction().getGameActionType() == GameActionType.PLACE_WORKER){
                Marker marker = (Marker)compactGameAction.getAction().getGameActionObject();
                List<Marker> allMarkers = new ArrayList<>(GameUtils.getAllMarkers().stream().map(x -> (Marker) x.getUserData()).toList());
                allMarkers.add(marker);
                GameUtils.updateMarkers(allMarkers);
                currentPlayer.deployWorker(false);
                label.setText(compactGameAction.getAction().getPlayerNumber().toString() + " placed a worker on " + ((Marker) compactGameAction.getAction().getGameActionObject()).getName() + " on " + compactGameAction.getAction().getDateTime());
            } else if (compactGameAction.getAction().getGameActionType() == GameActionType.PLAY_CARD){
                label.setText(compactGameAction.getAction().getPlayerNumber().toString() + " played card " + ((Card)compactGameAction.getAction().getGameActionObject()).getName() + " on " + compactGameAction.getAction().getDateTime());
            } else if (compactGameAction.getAction().getGameActionType() == GameActionType.PREPARE_FOR_SEASON){
                List<Marker> allMarkers = new ArrayList<>(GameUtils.getAllMarkers().stream().map(x -> (Marker) x.getUserData()).toList());
                List<Marker> filteredMarkers = allMarkers.stream().filter(x -> x.getPlayerNumber() == compactGameAction.getAction().getPlayerNumber()).toList();
                GameUtils.updateMarkers(filteredMarkers);
                currentPlayer.setCurrentSeason((Season)compactGameAction.getAction().getGameActionObject());

                label.setText(compactGameAction.getAction().getPlayerNumber().toString() + " prepared for season " + ((Season)compactGameAction.getAction().getGameActionObject()).name() + " on " + compactGameAction.getAction().getDateTime());
            }

            GameUtils.updatePlayer();

            counter.set(counter.get() + 1);
        }), new KeyFrame(Duration.seconds(3)));
        replay.setCycleCount(gameActions.size());
        replay.play();
    }
}
