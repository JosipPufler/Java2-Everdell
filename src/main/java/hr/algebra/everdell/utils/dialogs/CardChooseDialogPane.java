package hr.algebra.everdell.utils.dialogs;

import hr.algebra.everdell.controllers.CardDialogController;
import hr.algebra.everdell.models.Card;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DialogPane;

import java.io.IOException;
import java.util.List;

public class CardChooseDialogPane extends DialogPane {
    List<Card> cards;
    CardDialogController controller;

    public CardChooseDialogPane(List<Card> cards) {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/hr/algebra/everdell/CardChooseDialog.fxml"));
        loader.setRoot(this);

        try {
            loader.load();
            controller = loader.getController();
            controller.insertCards(cards);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Card getSelectedCard(){
        return controller.getSelectedCard();
    }
}
