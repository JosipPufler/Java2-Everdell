package hr.algebra.everdell.interfaces;

import hr.algebra.everdell.models.TriggerType;

public interface Triggered {
    TriggerType getTriggerType();
    void trigger();
}
