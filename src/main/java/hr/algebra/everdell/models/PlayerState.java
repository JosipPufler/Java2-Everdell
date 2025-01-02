package hr.algebra.everdell.models;

import hr.algebra.everdell.interfaces.Triggered;
import hr.algebra.everdell.utils.GameUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PlayerState {
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
    Season currentSeason = Season.Winter;
    @Getter
    int maxWorkers = 2;
    int freeWorkers = maxWorkers;
    int cardWorkers = 0;
    @Getter
    Boolean gameOver = false;
    public final int MAX_CARDS_IN_HAND = 8;
    public final int MAX_CARDS_IN_PLAY = 15;

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

    public Optional<Card> playCard(Card card){
        fireTrigger(TriggerType.CARD_BEFORE);
        if (card instanceof Critter<?>)
            fireTrigger(TriggerType.CRITTER_BEFORE);
        else if (card instanceof Construct)
            fireTrigger(TriggerType.CONSTRUCT_BEFORE);

        if (card.isUnique() && cardsInPlay.stream().anyMatch(x -> Objects.equals(x.getName(), card.getName())))
            return Optional.empty();
        if (numberOfCardsInPlay() >= MAX_CARDS_IN_PLAY)
            return Optional.empty();

        Optional<ResourceGroup> resourceGroup = calculateCost(card);
        if (resourceGroup.isPresent() && GameState.getPlayerState().resources.subtract(resourceGroup.get())){
            GameState.getResourceManager().deposit(resourceGroup.get());
            boolean played = card.play();

            fireTrigger(TriggerType.CARD_AFTER);
            if (card instanceof Critter<?>)
                fireTrigger(TriggerType.CRITTER_AFTER);
            else if (card instanceof Construct)
                fireTrigger(TriggerType.CONSTRUCT_AFTER);

            return Optional.of(card);
        } else {
            return Optional.empty();
        }
    }

    public Optional<ResourceGroup> calculateCost(Card card){
        Optional<Card> construct = cardsInPlay.stream().filter(x -> card instanceof Critter<?> && x.getClass() == ((Critter<?>)card).getAssociatedLocation() && !((Construct)x).isOccupied).findFirst();
        if (card instanceof Critter<?> && construct.isPresent()){
            ((Construct)construct.get()).setOccupied(true);
            return Optional.of(new ResourceGroup(0, 0, 0, 0));
        }
        else if (resources.compareTo(card.cost) > 0){
            return Optional.of(card.getCost());
        }
        return Optional.empty();
    }

    public void nextSeason(){
        if (gameOver)
            return;
        fireTrigger(TriggerType.SEASON_CHANGE);
        currentSeason = currentSeason.next();
        switch (currentSeason) {
            case Winter:
                gameOver = true;
                break;
            case Summer:
                maxWorkers = maxWorkers + 1;
                GameUtils.addCardsToHand(GameState.getResourceManager().tryDrawCardsFromMeadow(2));
                break;
            case Autumn:
                maxWorkers = maxWorkers + 2;
                cardsInPlay.stream().filter(x -> x.getType() == CardType.GREEN_PRODUCTION).forEach(Card::play);
                break;
            case Spring:
                maxWorkers = maxWorkers + 1;
                cardsInPlay.stream().filter(x -> x.getType() == CardType.GREEN_PRODUCTION).forEach(Card::play);
                break;
        }
        callWorkersHome();
        GameState.switchPlayers();
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

    public Boolean deployWorker(Boolean card){
        if (freeWorkers <= 0)
            return false;
        if (card){
            cardWorkers++;
        }
        freeWorkers--;
        GameUtils.updatePlayer();
        return true;
    }

    public int numberOfCardsInPlay(){
        return (int) cardsInPlay.stream().filter(Card::takesSpace).count();
    }
}