package hr.algebra.everdell.utils;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.models.GameState;

import java.util.List;

public class CardUtils {
    public static void clearCardsFromCity(){
        for (int i = GameState.getPlayerState().cardsInPlay.size()-1 ; i >=0 ; i--) {
            removeCardFromCity(GameState.getPlayerState().cardsInPlay.get(i));
        }
    }

    public static void clearCardsFromHand(){
        for (int i = GameState.getPlayerState().cardsInHand.size()-1 ; i >=0 ; i--) {
            removeCardFromHand(GameState.getPlayerState().cardsInHand.get(i));
        }
    }

    public static void removeCardFromCity(Card card) {
        GameUtils.getCityController().removeCard(card);
        GameState.getPlayerState().cardsInPlay.remove(card);
    }

    public static void removeCardFromHand(Card card) {
        GameUtils.getHandController().removeCard(card);
        GameState.getPlayerState().cardsInHand.remove(card);
    }

    public static void removeCardFromMeadow(Card card) {
        GameUtils.getMeadowController().removeCard(card);
        GameState.getResourceManager().getMeadow().remove(card);
    }

    public static void addCardToCity(Card card, Boolean play) {
        if (GameState.getPlayerState().cardsInPlay.size() < GameState.getPlayerState().MAX_CARDS_IN_PLAY) {
            if (play)
                GameState.getPlayerState().playCard(card, false);
            else
                GameState.getPlayerState().cardsInPlay.add(card);
            GameUtils.getCityController().insertCard(card);
        }
    }

    public static void addCardToHand(Card card) {
        if (GameState.getPlayerState().cardsInHand.size() < GameState.getPlayerState().MAX_CARDS_IN_HAND) {
            GameState.getPlayerState().cardsInHand.add(card);
            GameUtils.getHandController().insertCard(card);
        }
    }

    public static void addCardsToHand(List<Card> cards) {
        for (Card card : cards) {
            addCardToHand(card);
        }
    }

    public static void addCardsToCity(List<Card> cards) {
        for (Card card : cards) {
            addCardToCity(card, false);
        }
    }

    public static void addCardToOpponentsHand(Card card) {
        if (GameState.getOpponentState().cardsInHand.size() < GameState.getOpponentState().MAX_CARDS_IN_HAND)
            GameState.getOpponentState().cardsInHand.add(card);
    }

    public static void addCardToOpponentsCity(Card card) {
        if (GameState.getOpponentState().cardsInPlay.size() < GameState.getOpponentState().MAX_CARDS_IN_PLAY)
            GameState.getOpponentState().cardsInPlay.add(card);
    }

    public static void addCardsToOpponentsHand(List<Card> cards) {
        for (Card card : cards) {
            addCardToOpponentsHand(card);
        }
    }
}
