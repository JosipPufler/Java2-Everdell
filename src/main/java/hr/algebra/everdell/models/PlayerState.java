package hr.algebra.everdell.models;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.interfaces.GreenProduction;
import hr.algebra.everdell.interfaces.Triggered;
import hr.algebra.everdell.models.cards.constructs.Ruins;
import hr.algebra.everdell.utils.GameUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PlayerState implements Serializable {
    @Getter
    private final String playerName;
    @Getter
    private final PlayerNumber playerNumber;
    public final ResourceGroup resources = new ResourceGroup(0, 0, 0, 0);
    int points;
    public final List<Card> cardsInPlay = new ArrayList<>();
    public final List<Card> cardsInHand = new ArrayList<>();
    public final List<Location> locationsDeployed = new ArrayList<>();
    @Getter
    Season currentSeason = Season.WINTER;
    @Getter
    int maxWorkers = 2;
    int freeWorkers = maxWorkers;
    int cardWorkers = 0;
    public static final int MAX_CARDS_IN_HAND = 8;
    public static final int MAX_CARDS_IN_PLAY = 15;
    @Getter
    @Setter
    Boolean turnPriority = false;
    boolean lastWinter = false;

    public PlayerState(PlayerNumber playerNumber) {
        playerName = playerNumber.toString();
        this.playerNumber = playerNumber;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int calculatePoints(){
        int cardValue = cardsInPlay.stream().mapToInt(Card::calculatePoints).sum();
        return cardValue + points;
    }

    public Optional<Card> playCard(Card card, Boolean ignoreCost){
        fireTrigger(TriggerType.CARD_BEFORE);
        if (card instanceof Critter<?>)
            fireTrigger(TriggerType.CRITTER_BEFORE);
        else if (card instanceof Construct)
            fireTrigger(TriggerType.CONSTRUCT_BEFORE);

        if (couldPlayCard(card, ignoreCost)){
            if (!ignoreCost){
                Optional<ResourceGroup> resourceGroup = calculateCost(card);
                GameState.getResourceManager().deposit(resourceGroup.get());
                resources.subtract(resourceGroup.get());
            }
            boolean played = card.play();
            if (!played)
                return Optional.empty();
            fireTrigger(TriggerType.CARD_AFTER);
            if (card instanceof Critter<?>)
                fireTrigger(TriggerType.CRITTER_AFTER);
            else if (card instanceof Construct)
                fireTrigger(TriggerType.CONSTRUCT_AFTER);
            return Optional.of(card);
        }
        return Optional.empty();
    }

    public boolean couldPlayCard(Card card, Boolean ignoreCost){
        if ((card.isUnique() && cardsInPlay.stream().anyMatch(x -> Objects.equals(x.getName(), card.getName()))) || numberOfCardsInPlay() >= MAX_CARDS_IN_PLAY)
            return false;
        Optional<Card> construct = cardsInPlay.stream().filter(x -> card instanceof Critter<?> && x.getClass() == ((Critter<?>)card).getAssociatedLocation() && !((Construct)x).isOccupied).findFirst();
        return ignoreCost || (card instanceof Critter<?> && construct.isPresent()) || resources.compareTo(card.getCost()) > 0;
    }

    public Optional<ResourceGroup> calculateCost(Card card){
        Optional<Card> construct = cardsInPlay.stream().filter(x -> card instanceof Critter<?> && x.getClass() == ((Critter<?>)card).getAssociatedLocation() && !((Construct)x).isOccupied).findFirst();
        if (card instanceof Critter<?> && construct.isPresent()){
            ((Construct)construct.get()).setOccupied(true);
            return Optional.of(new ResourceGroup(0, 0, 0, 0));
        }
        else if (resources.compareTo(card.getCost()) > 0)
            return Optional.of(card.getCost());
        return Optional.empty();
    }

    public void setCurrentSeason(Season season){
        currentSeason = season;
        switch (season) {
            case WINTER:
                lastWinter = true;
                break;
            case SUMMER:
                maxWorkers = maxWorkers + 1;
                //CardUtils.addCardsToHand(GameState.getResourceManager().tryDrawCardsFromMeadow(2));
                break;
            case AUTUMN:
                //maxWorkers = maxWorkers + 2;
                cardsInPlay.stream().filter(GreenProduction.class::isInstance).forEach(x -> ((GreenProduction) x).Activate());
                break;
            case SPRING:
                //maxWorkers = maxWorkers + 1;
                cardsInPlay.stream().filter(GreenProduction.class::isInstance).forEach(x -> ((GreenProduction) x).Activate());
                break;
        }
        callWorkersHome();
    }

    public void nextSeason(){
        if (lastWinter)
            return;
        fireTrigger(TriggerType.SEASON_CHANGE);
        setCurrentSeason(currentSeason.next());
        GameState.switchPlayers(new GameAction(GameState.getPlayerState().getPlayerNumber(), GameActionType.PREPARE_FOR_SEASON, currentSeason));
    }

    public void fireTrigger(TriggerType triggerType){
        List<Card> cardList = new ArrayList<>(cardsInPlay);
        cardList.stream().filter(x -> x instanceof Triggered).filter(x -> ((Triggered) x).getTriggerType() == triggerType).forEach(x -> ((Triggered) x).trigger());
    }

    public Integer getFreeWorkers() {
        return freeWorkers;
    }

    public void returnWorkersFromCards(int workersReturned){
        if (freeWorkers + workersReturned > maxWorkers || cardWorkers < workersReturned){
            throw new RuntimeException("Too many workers");
        }
        freeWorkers += workersReturned;
        cardWorkers -= workersReturned;
        GameUtils.updatePlayer();
    }

    public void callWorkersHome(){
        freeWorkers = maxWorkers-cardWorkers;
        locationsDeployed.clear();
    }

    public Boolean deployWorker(Boolean isCard){
        if (freeWorkers <= 0)
            return false;
        if (isCard){
            cardWorkers++;
        }
        freeWorkers--;
        GameUtils.updatePlayer();
        return true;
    }

    public void returnWorkers(int numberOfWorkers){
        if (numberOfWorkers + freeWorkers <= maxWorkers){
            freeWorkers += numberOfWorkers;
        }
    }

    public int numberOfCardsInPlay(){
        return (int) cardsInPlay.stream().filter(Card::takesSpace).count();
    }

    public Boolean getGameOver() {
        long negativeCardsPlayable = 0;
        if (cardsInHand.stream().filter(Ruins.class::isInstance).count() > 0){
            long countRuins = cardsInHand.stream().filter(Ruins.class::isInstance).count();
            long countConstructs = cardsInPlay.stream().filter(Construct.class::isInstance).count();
            if (countRuins > countConstructs){
                negativeCardsPlayable = countRuins - countConstructs;
            }
        }
        long cardsPlayable = cardsInHand.stream().filter(x -> couldPlayCard(x, false)).count();
        cardsPlayable = cardsPlayable - negativeCardsPlayable;
        return lastWinter && freeWorkers == 0 && (cardsInHand.isEmpty() || cardsPlayable <= 0);
    }
}