package hr.algebra.everdell.models;

import hr.algebra.everdell.utils.GameUtils;

import java.io.Serializable;
import java.util.function.Supplier;

public class SpecialLocation extends Location implements Serializable, Runnable {
    Supplier<Boolean> function;

    public SpecialLocation(ResourceGroup resource, int numberOfCards, int numberOfPoints, Boolean open, Supplier<Boolean> function) {
        super(resource, numberOfCards, numberOfPoints, open);
        this.function = function;
    }

    @Override
    public Boolean place() {
        Boolean placed = function.get();
        if (placed) {
            super.place();
            GameUtils.updatePlayer();
            return placed;
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString().replace("location", "specialLocation");
    }

    @Override
    public void run() {
        function.get();
    }
}
