package hr.algebra.everdell.models;

import java.util.function.BooleanSupplier;

public class Event extends Location{
    BooleanSupplier condition;

    public Event(ResourceGroup resource, int numberOfCards, int numberOfPoints, BooleanSupplier condition) {
        super(resource, numberOfCards, numberOfPoints, false);
        this.condition = condition;
    }

    public Boolean canPlace(){
        return condition.getAsBoolean();
    }
}
