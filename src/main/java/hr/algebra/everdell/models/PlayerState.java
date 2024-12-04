package hr.algebra.everdell.models;

import java.util.ArrayList;
import java.util.List;

public class PlayerState {
    public static String playerName;
    public final ResourceGroup resources = new ResourceGroup(0, 0, 0, 0);
    int points;
    public final List<Card> cardsInPlay = new ArrayList<>();
    public final List<Card> cardsInHand = new ArrayList<>();
    Season currentSeason = Season.Winter;
    int maxWorkers = 2;
    int freeWorkers = maxWorkers;

    public void addPoints(int points) {
        this.points += points;
    }

    public int calculatePoints(){
        int cardValue = cardsInPlay.stream().mapToInt(x -> x.calculatePoints(this)).sum();
        return cardValue + points;
    }
}