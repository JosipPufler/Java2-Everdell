package hr.algebra.everdell.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DialogUtils {
        public static Alert showDialog(String title,
                                      String contentText,
                                      Alert.AlertType alertType)
        {
            Alert alert = new Alert(alertType, contentText, ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.showAndWait();
            return alert;
        }
}
