package hr.algebra.everdell.utils;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.models.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class XmlParseUtils {

    public static List<GameActionTransferable> parse(String xmlFileName) {
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

    private static List<GameActionTransferable> retrieveGameMoves(Document document) {
        List<GameActionTransferable> gameActions = new ArrayList<>();
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
                gameActions.add(new GameActionTransferable(new GameAction(playerNumber, actionType, marker, time), cardsInPlay, cardsInHand, resourceGroup, freeWorkers, boardResourceGroup, points, deckSize));
            }
            else if (actionType == GameActionType.PLAY_CARD){
                Card card = extractCard(element.getElementsByTagName(GameActionTag.CARD_PLAYED.getTag()).item(0).getTextContent());
                gameActions.add(new GameActionTransferable(new GameAction(playerNumber, actionType, card, time), cardsInPlay, cardsInHand, resourceGroup, freeWorkers, boardResourceGroup, points, deckSize));
            } else if (actionType == GameActionType.PREPARE_FOR_SEASON) {
                Season season = Season.valueOf(element.getElementsByTagName(GameActionTag.SEASON.getTag()).item(0).getTextContent());
                gameActions.add(new GameActionTransferable(new GameAction(playerNumber, actionType, season, time), cardsInPlay, cardsInHand, resourceGroup, freeWorkers, boardResourceGroup, points, deckSize));
            }
        }

        return gameActions;
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
}
