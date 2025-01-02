package hr.algebra.everdell.models;

import lombok.Getter;

@Getter
public enum ConfigurationKey {

    HOST("host.name"),
    PLAYER_TWO_SERVER_PORT("player.two.server.port"),
    PLAYER_ONE_SERVER_PORT("player.one.server.port");

    private final String key;

    ConfigurationKey(String key) {
        this.key = key;
    }
}
