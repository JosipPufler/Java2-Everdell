package hr.algebra.everdell.utils;

import hr.algebra.everdell.models.Card;
import hr.algebra.everdell.models.ResourceGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourceManager {
    static final int STARTING_MAIN_DECK_SIZE = 128;
    static final int STARTING_PEBBLES = 20;
    static final int STARTING_RESIN = 25;
    static final int STARTING_BERRIES = 30;
    static final int STARTING_TWIGS = 30;
    ResourceGroup resourcePool = new ResourceGroup(STARTING_BERRIES, STARTING_TWIGS, STARTING_RESIN, STARTING_PEBBLES);

    private static final List<Card> deck = new ArrayList<>();

    public ResourceManager(List<Card> cards) {
        deck.addAll(cards);
        Collections.shuffle(deck);
    }

    public void updatePool(ResourceGroup resourcePool) {
        this.resourcePool = resourcePool;
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

    public ResourceGroup getResourcePool() {
        return resourcePool;
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
}
