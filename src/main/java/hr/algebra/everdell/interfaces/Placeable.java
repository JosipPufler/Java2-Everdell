package hr.algebra.everdell.interfaces;

import hr.algebra.everdell.models.PlayerState;

public interface Placeable {
    Boolean isOpen();
    String getName();
    Boolean place();
}
