package hr.algebra.everdell.models.cards.constructs;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.interfaces.Destination;
import hr.algebra.everdell.models.*;
import hr.algebra.everdell.utils.CardUtils;
import hr.algebra.everdell.utils.DialogUtils;
import hr.algebra.everdell.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Inn extends Construct implements Destination {
    final List<PlayerNumber> workers = new ArrayList<>();
    int workerSlots = 1;
    Boolean isOpen = true;

    public Inn() {
        super(new ResourceGroup(0, 2, 1, 0),
                CardType.RED_DESTINATION,
                Inn.class.getSimpleName(),
                FileUtils.getRelativeCardImagePath(Inn.class.getSimpleName()),
                false,
                2,
                3);
    }

    @Override
    public int getNumberOfWorkerSlots() {
        return workerSlots;
    }

    @Override
    public int getNumberOfDeployedWorkers() {
        return workers.size();
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public List<PlayerNumber> getWorkers() {
        return workers;
    }

    @Override
    public Boolean placeWorker() {
        if (!GameState.getResourceManager().getMeadow().isEmpty()){
            Optional<Card> card = DialogUtils.showCardChooseDialog(GameState.getResourceManager().getMeadow(), "Choose card from the meadow");
            if (card.isPresent()){
                Optional<ResourceGroup> discount;

                if (card.get().getCost().sumAllResources() > 3){
                    discount = DialogUtils.showMultiResourceDialog(3, card.get().getCost(), "Discount for " + card.get().getName());
                }else {
                    discount = Optional.of(card.get().getCost());
                }
                ResourceGroup cost = card.get().getCost();
                if (discount.isPresent() && cost.subtract(discount.get()) && cost.compareTo(GameState.getPlayerState().resources) < 0){
                    GameState.getPlayerState().playCard(card.get());
                    CardUtils.removeCardFromMeadow(card.get());
                }
            }
        }
        workers.add(GameState.getPlayerState().getPlayerNumber());
        return true;
    }
}
