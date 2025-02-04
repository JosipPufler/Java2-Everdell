package hr.algebra.everdell.utils;

import hr.algebra.everdell.models.GameState;
import hr.algebra.everdell.rmi.ChatRemoteService;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.rmi.RemoteException;
import java.util.List;

public class ChatUtils {
    public static void createAndRunChatTimeline(ChatRemoteService chatRemoteService,
                                                TextArea chatMessagesTextArea) {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, _ -> {
            try {
                List<String> chatMessages = chatRemoteService.getAllChatMessages();

                chatMessagesTextArea.clear();

                for (String chatMessage : chatMessages) {
                    chatMessagesTextArea.appendText(chatMessage + "\n");
                }
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public static void sendChatMessage(TextField chatMessagesTextField, TextArea chatMessagesTextArea,
                                       ChatRemoteService chatRemoteService) {
        String messageText = chatMessagesTextField.getText();
        try {
            chatRemoteService.sendChatMessage(GameState.getPlayerState().getPlayerNumber() + ": "
                    + messageText);

            List<String> chatMessages = chatRemoteService.getAllChatMessages();

            chatMessagesTextArea.clear();

            for (String chatMessage : chatMessages) {
                chatMessagesTextArea.appendText(chatMessage + "\n");
            }
            chatMessagesTextField.clear();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
