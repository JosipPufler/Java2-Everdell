package hr.algebra.everdell.interfaces;

import hr.algebra.everdell.models.Card;

import java.util.List;

public interface CardInsertable {
    void insertCards(List<Card> cards);
    void insertCard(Card card);
}
