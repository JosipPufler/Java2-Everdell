package hr.algebra.everdell.utils;

import hr.algebra.everdell.models.Card;
import hr.algebra.everdell.models.ResourceGroup;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Getter
public class ResourceManager implements Serializable {
    static final int MEADOW_SIZE = 8;
    static final int STARTING_PEBBLES = 20;
    static final int STARTING_RESIN = 25;
    static final int STARTING_BERRIES = 30;
    static final int STARTING_TWIGS = 30;
    ResourceGroup resourcePool = new ResourceGroup(STARTING_BERRIES, STARTING_TWIGS, STARTING_RESIN, STARTING_PEBBLES);

    private final List<Card> deck = new ArrayList<>();
    @Getter
    private final List<Card> meadow = new ArrayList<>();

    public ResourceManager(List<Card> cards) {
        deck.addAll(cards);
        Collections.shuffle(deck);
    }

    public List<Card> tryDrawFromMainDeck(int numberOfCards){
        List<Card> cardsDrawn = new ArrayList<>();
        if (numberOfCards > deck.size()) {
            numberOfCards = deck.size();
        }
        for (int i = 0; i < numberOfCards; i++) {
                cardsDrawn.add(deck.removeLast());
            }
        return cardsDrawn;
    }

    public int getDeckSize() {
        return deck.size();
    }

    public void tryTakeBerries(ResourceGroup depositingPool, int berries) {
        if (resourcePool.removeBerries(berries)) {
            depositingPool.addBerries(berries);
        } else {
            depositingPool.addBerries(resourcePool.getBerries());
            resourcePool.removeBerries(resourcePool.getBerries());
        }
    }

    public void tryTakeResin(ResourceGroup depositingPool, int resin) {
        if (resourcePool.removeResin(resin)) {
            depositingPool.addResin(resin);
        } else {
            depositingPool.addResin(resourcePool.getResin());
            resourcePool.removeResin(resourcePool.getResin());
        }
    }

    public void tryTakePebbles(ResourceGroup depositingPool, int pebbles) {
        if (resourcePool.removePebbles(pebbles)) {
            depositingPool.addPebbles(pebbles);
        } else {
            depositingPool.addPebbles(resourcePool.getPebbles());
            resourcePool.removePebbles(resourcePool.getPebbles());
        }
    }

    public void tryTakeTwigs(ResourceGroup depositingPool, int twigs) {
        if (resourcePool.removeTwigs(twigs)) {
            depositingPool.addTwigs(twigs);
        } else {
            depositingPool.addTwigs(resourcePool.getTwigs());
            resourcePool.removeTwigs(resourcePool.getTwigs());
        }
    }

    public void tryTakeGroup(ResourceGroup depositingPool, ResourceGroup resourcePool) {
        tryTakeBerries(depositingPool, resourcePool.getBerries());
        tryTakePebbles(depositingPool, resourcePool.getPebbles());
        tryTakeResin(depositingPool, resourcePool.getResin());
        tryTakeTwigs(depositingPool, resourcePool.getTwigs());
    }

    public void deposit(ResourceGroup deposit) {
        resourcePool.addBerries(deposit.getBerries());
        resourcePool.addResin(deposit.getResin());
        resourcePool.addPebbles(deposit.getPebbles());
        resourcePool.addResin(deposit.getResin());
    }

    public List<Card> tryDrawCardsFromMeadow(int numberOfCards) {
        List<Card> cardsDrawn = new ArrayList<>();
        Optional<Card> card = Optional.empty();
        if (numberOfCards > 0) {
            for (int i = 0; i < numberOfCards; i++) {
                card = DialogUtils.showCardChooseDialog(getMeadow(), "Choose cards");
                if (card.isPresent()){
                    cardsDrawn.add(card.get());
                    meadow.remove(card.get());
                }
            }
        }
        return cardsDrawn;
    }

    public List<Card> replenishMeadow(){
        int cardsToDraw = MEADOW_SIZE - meadow.size();
        List<Card> cards = tryDrawFromMainDeck(cardsToDraw);
        meadow.addAll(cards);
        return cards;
    }
}
