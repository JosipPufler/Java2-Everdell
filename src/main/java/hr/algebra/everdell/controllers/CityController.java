package hr.algebra.everdell.controllers;

import hr.algebra.everdell.interfaces.CardInsertable;
import hr.algebra.everdell.interfaces.CardListable;
import hr.algebra.everdell.models.Card;
import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;

import java.util.List;

public class CityController extends CardListable implements CardInsertable {
    @FXML
    private TilePane cityPane;

    public void initialize() {
        cityPane.setHgap(5);
        cityPane.setVgap(5);
        super.popUpStage.setAlwaysOnTop(true);
    }

    @Override
    public void insertCard(Card card){
        cityPane.getChildren().add(super.addCard(card));
    }

    @Override
    public void insertCards(List<Card> cards){
        cityPane.getChildren().addAll(super.addCards(cards));
    }
}
