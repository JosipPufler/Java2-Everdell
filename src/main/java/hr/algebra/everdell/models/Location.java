package hr.algebra.everdell.models;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.interfaces.Placeable;
import hr.algebra.everdell.utils.CardUtils;
import hr.algebra.everdell.utils.GameUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@XmlRootElement
public class Location implements Serializable, Placeable {
    @Getter
    static final List<Location> locations = new ArrayList<>();

    public static void addLocations(Location location){
        locations.add(location);
    }

    static final String SEPARATOR = ", ";
    @XmlElement
    int cards;
    @XmlElement
    int points;
    @XmlElement
    ResourceGroup resourceGroup;
    Boolean open;

    public Location(ResourceGroup resource, int numberOfCards, int numberOfPoints, Boolean open) {
        resourceGroup = resource;
        cards = numberOfCards;
        points = numberOfPoints;
        this.open = open;
    }

    public Boolean place() {
        List<Card> cardsDrawn = GameState.getResourceManager().tryDrawFromMainDeck(cards);
        CardUtils.addCardsToHand(cardsDrawn);
        GameState.getPlayerState().addPoints(points);
        GameState.getResourceManager().tryTakeGroup(GameState.getPlayerState().resources, resourceGroup);
        GameUtils.updatePlayer();
        return true;
    }

    @Override
    public String toString() {
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
        return cards == location.cards && points == location.points && Objects.equals(resourceGroup, location.resourceGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cards, points, resourceGroup);
    }

    @Override
    public Boolean isOpen(){
        return open;
    }

    @Override
    public String getName() {
        return toString();
    }
}
