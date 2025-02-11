package hr.algebra.everdell.utils;

import lombok.Getter;

public class ResourceManagerSingleton {
    @Getter
    private static ResourceManager instance;

    static {
        try {
            instance = new ResourceManager(DeckUtils.generateDeck());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ResourceManagerSingleton() {}
}
