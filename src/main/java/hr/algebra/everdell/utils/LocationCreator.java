package hr.algebra.everdell.utils;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.models.GameState;
import hr.algebra.everdell.models.ResourceGroup;
import hr.algebra.everdell.models.Season;
import hr.algebra.everdell.models.SpecialLocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class LocationCreator {
    private LocationCreator(){}

    public static SpecialLocation createHavenSpecialLocation(){
        return new SpecialLocation(new ResourceGroup(0, 0, 0, 0), 0, 0, true, (Supplier<Boolean> & Serializable)() -> {
            int discarded = 0;
            while (true){
                Optional<Card> chooseCardToDiscard = DialogUtils.showCardChooseDialog(GameState.getPlayerState().cardsInHand, "Choose card to discard");
                if (chooseCardToDiscard.isPresent()) {
                    discarded++;
                    CardUtils.removeCardFromHand(chooseCardToDiscard.get());
                } else
                    break;
            }
            Optional<ResourceGroup> resourceGroup = DialogUtils.showMultiResourceDialog(discarded / 2, GameState.getResourceManager().getResourcePool(), "Choose resources");
            resourceGroup.ifPresent(group -> GameState.getPlayerState().resources.merge(group));
            return true;
        });
    }

    public static SpecialLocation createJourneySpecialLocation(int points){
        return new SpecialLocation(new ResourceGroup(0, 0, 0, 0), 0, points, true, (Supplier<Boolean> & Serializable)() -> {
            if (GameState.getPlayerState().cardsInHand.size() >= points && GameState.getPlayerState().getCurrentSeason() == Season.AUTUMN){
                List<Card> cardList = new ArrayList<>();
                List<Card> cardsInHand = new ArrayList<>(GameState.getPlayerState().cardsInHand);
                for (int i = 0 ; i<points; i++){
                    Optional<Card> cardToDiscard = DialogUtils.showCardChooseDialog(cardsInHand, "Choose card to discard");
                    if (cardToDiscard.isPresent()){
                        cardList.add(cardToDiscard.get());
                        cardsInHand.remove(cardToDiscard.get());
                    } else
                        return false;
                }
                for (Card card : cardList){
                    CardUtils.removeCardFromHand(card);
                }
                return true;
            } else
                return false;
        });
    }
}
