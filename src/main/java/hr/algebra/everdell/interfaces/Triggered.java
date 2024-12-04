package hr.algebra.everdell.interfaces;

import hr.algebra.everdell.models.PlayerState;
import hr.algebra.everdell.models.TriggerType;
import hr.algebra.everdell.utils.ResourceManager;

public interface Triggered {
    TriggerType getTriggerType();
    void trigger(PlayerState playerState, PlayerState opponentState, ResourceManager resourceManager);
}
