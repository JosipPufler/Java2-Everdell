package hr.algebra.everdell.utils;

import hr.algebra.everdell.models.PlayerState;

public class PlayerStateSingleton {
    private static PlayerState instance;
    private static final Object mutex = new Object();

    private PlayerStateSingleton() {}

    public static PlayerState getInstance() {
        if (instance == null) {
            synchronized (mutex) {
                if (instance == null) {
                    instance = new PlayerState();
                }
            }
        }
        return instance;
    }
}
