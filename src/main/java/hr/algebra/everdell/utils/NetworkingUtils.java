package hr.algebra.everdell.utils;

import hr.algebra.everdell.EverdellApplication;
import hr.algebra.everdell.models.ConfigurationKey;
import hr.algebra.everdell.models.GameState;
import hr.algebra.everdell.models.PlayerNumber;

public class NetworkingUtils {
    private NetworkingUtils(){}

    public static void sendGameState() {
        if(PlayerNumber.ONE.name().equals(GameState.getPlayerState().getPlayerNumber().name())) {
            EverdellApplication.sendRequestFromPlayer(
                    ConfigurationReader.getStringValueForKey(ConfigurationKey.HOST),
                    ConfigurationReader.getIntegerValueForKey(ConfigurationKey.PLAYER_TWO_SERVER_PORT));

        }
        else if(PlayerNumber.TWO.name().equals(GameState.getPlayerState().getPlayerNumber().name())) {
            EverdellApplication.sendRequestFromPlayer(
                    ConfigurationReader.getStringValueForKey(ConfigurationKey.HOST),
                    ConfigurationReader.getIntegerValueForKey(ConfigurationKey.PLAYER_ONE_SERVER_PORT));
        }
    }
}
