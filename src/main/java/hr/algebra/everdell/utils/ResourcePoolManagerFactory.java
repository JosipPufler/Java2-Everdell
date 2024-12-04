package hr.algebra.everdell.utils;

public class ResourcePoolManagerFactory {
    static ResourceManager instance;

    static {
        try {
            instance = new ResourceManager(DeckUtils.generateDeck());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ResourceManager getInstance() {
        return instance;
    }

    private ResourcePoolManagerFactory() {
    }
}
