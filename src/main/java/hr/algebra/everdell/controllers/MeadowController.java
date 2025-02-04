package hr.algebra.everdell.controllers;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.utils.CardUtils;
import hr.algebra.everdell.utils.GameUtils;
import javafx.scene.image.ImageView;

public class MeadowController extends PlayableCardController{
    @Override
    protected ImageView createCardImageView(Card card) {
        ImageView iv = super.createCardImageView(card);
        iv.setOnDragDone(event -> {
            if (event.isAccepted()) {
                CardUtils.removeCardFromMeadow(card);
                GameUtils.replenishMeadow();
            }
        });
        return iv;
    }
}
