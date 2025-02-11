package hr.algebra.everdell.models;

import hr.algebra.everdell.utils.GameUtils;

import java.util.function.Supplier;

public class SpecialLocation extends Location{
    Supplier<Integer> function;

    public SpecialLocation(ResourceGroup resource, int numberOfCards, int numberOfPoints, Boolean open, Supplier<Integer> function) {
        super(resource, numberOfCards, numberOfPoints, open);
        this.function = function;
    }

    @Override
    public Boolean place() {
        if (super.place()) {
            function.get();
            GameUtils.updatePlayer();
            return true;
        }
        return false;
    }

    @Override
    public String toShorthandString() {
        return super.toShorthandString().replace("location", "specialLocation");
    }
}
