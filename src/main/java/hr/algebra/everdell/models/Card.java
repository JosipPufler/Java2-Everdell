package hr.algebra.everdell.models;

import hr.algebra.everdell.utils.ResourceManager;

public abstract class Card{
    CardType type;
    ResourceGroup cost;
    String name;
    String imageFilePath;
    Boolean unique;
    int inherentPointValue;
    int numberInDeck;

    public Card(ResourceGroup cost, CardType type, String name, String imageFilePath, Boolean unique, Integer inherentPointValue, int numberInDeck) {
        this.cost = cost;
        this.type = type;
        this.name = name;
        this.imageFilePath = imageFilePath;
        this.unique = unique;
        this.inherentPointValue = inherentPointValue;
        this.numberInDeck = numberInDeck;
    }

    public Boolean isUnique() {
        return unique;
    }

    public Boolean getUnique() {
        return unique;
    }

    public int getNumberInDeck() {
        return numberInDeck;
    }

    public int calculatePoints(PlayerState playerState) {
        return inherentPointValue;
    }


    public String getImageFilePath() {
        return imageFilePath;
    }

    public String getName(){
        return name;
    }

    public CardType getType() {
        return type;
    }

    public ResourceGroup getCost()
    {
        return cost;
    }

    public Boolean CanPlay(ResourceGroup stockpile){
        return stockpile.compareTo(cost) > 0;
    }

    public abstract void playEffect(PlayerState playerState, PlayerState opponentState, ResourceManager resourceManager);

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Card other = (Card) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
