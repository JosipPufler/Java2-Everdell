package hr.algebra.everdell.models;

import hr.algebra.everdell.utils.GameUtils;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Location implements Serializable {
    @Getter
    static final List<Location> locations = new ArrayList<>();

    public static void addLocations(Location location){
        locations.add(location);
    }

    public static void addLocations(List<Location> locationsToAdd){
        locations.addAll(locationsToAdd);
    }

    final String separator = ", ";
    int cards;
    int points;
    ResourceGroup resourceGroup;
    Boolean open;

    Boolean activated = false;

    public Location(ResourceGroup resource, int numberOfCards, int numberOfPoints, Boolean open) {
        resourceGroup = resource;
        cards = numberOfCards;
        points = numberOfPoints;
        this.open = open;
    }

    public Boolean isActivated(){
        return activated;
    }

    public void activate(PlayerState playerState, Boolean ignoreActivation) {
        if (isActivated() && !ignoreActivation)
            return;
        else if (!ignoreActivation)
            activated = true;
        List<Card> cardsDrawn = GameState.getResourceManager().tryDrawFromMainDeck(cards);
        GameUtils.addCardsToHand(cardsDrawn);
        playerState.addPoints(points);
        GameState.getResourceManager().tryTakeGroup(playerState.resources, resourceGroup);
        GameUtils.updatePlayer();
    }

    public void deactivate(){
        activated = false;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (cards > 0) {
            builder.append("Cards: ").append(cards).append(separator);
        }
        if (points > 0) {
            builder.append("Points: ").append(points).append(separator);
        }
        if (resourceGroup.getBerries() > 0) {
            builder.append("Berries: ").append(resourceGroup.getBerries()).append(separator);
        }
        if (resourceGroup.getResin() > 0){
            builder.append("Resin: ").append(resourceGroup.getResin()).append(separator);
        }
        if (resourceGroup.getPebbles() > 0){
            builder.append("Pebbles: ").append(resourceGroup.getPebbles()).append(separator);
        }
        if (resourceGroup.getTwigs() > 0){
            builder.append("Twigs: ").append(resourceGroup.getTwigs()).append(separator);
        }
        return builder.toString();
    }

    public String toShorthandString(){
        StringBuilder builder = new StringBuilder();
        builder.append("location_");
        if (cards > 0) {
            builder.append(cards).append("C");
        }
        if (points > 0) {
            builder.append(points).append("Pt");
        }
        if (resourceGroup.getBerries() > 0) {
            builder.append(resourceGroup.getBerries()).append("B");
        }
        if (resourceGroup.getResin() > 0){
            builder.append(resourceGroup.getResin()).append("R");
        }
        if (resourceGroup.getPebbles() > 0){
            builder.append(resourceGroup.getPebbles()).append("P");
        }
        if (resourceGroup.getTwigs() > 0){
            builder.append(resourceGroup.getTwigs()).append("T");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location location)) return false;
        return cards == location.cards && points == location.points && Objects.equals(resourceGroup, location.resourceGroup) && Objects.equals(activated, location.activated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cards, points, resourceGroup);
    }

    public Boolean isOpen(){
        return open;
    }
}
