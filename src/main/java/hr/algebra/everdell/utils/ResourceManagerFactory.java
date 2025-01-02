package hr.algebra.everdell.utils;

public class ResourceManagerFactory {
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

    private ResourceManagerFactory() {
    }
}
