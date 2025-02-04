package hr.algebra.everdell.utils;

import hr.algebra.everdell.interfaces.Card;
import hr.algebra.everdell.models.Resource;
import hr.algebra.everdell.models.ResourceGroup;
import hr.algebra.everdell.utils.dialogs.CardChooseDialogPane;
import hr.algebra.everdell.utils.dialogs.CustomListDialogPane;
import hr.algebra.everdell.utils.dialogs.MultiResourceDialogPane;
import hr.algebra.everdell.utils.dialogs.SingleResourceDialogPane;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.util.List;
import java.util.Optional;

public class DialogUtils {
    private DialogUtils() {}

    public static Boolean showConfirmDialog(String title,
                                  String contentText,
                                  Alert.AlertType alertType)
    {
        Alert alert = new Alert(alertType, contentText, ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }

    public static Optional<ResourceGroup> showMultiResourceDialog(int maxResources,  ResourceGroup referenceResourceGroup, String title){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        MultiResourceDialogPane multiResourceDialogPane = new MultiResourceDialogPane(maxResources, referenceResourceGroup);
        dialog.setDialogPane(multiResourceDialogPane);
        dialog.showAndWait();
        if (dialog.getResult() == ButtonType.APPLY) {
            return Optional.of(multiResourceDialogPane.getResourcePool());
        }
        return Optional.empty();
    }

    public static Optional<Integer> showSingleResourceDialog(int maxResource, Resource resource, String title){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        SingleResourceDialogPane multiResourceDialogPane = new SingleResourceDialogPane(maxResource, resource);
        dialog.setDialogPane(multiResourceDialogPane);
        dialog.showAndWait();
        if (dialog.getResult() == ButtonType.APPLY) {
            return Optional.of(multiResourceDialogPane.getResource());
        }
        return Optional.empty();
    }

    public static Optional<Card> showCardChooseDialog(List<Card> cards, String title){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        CardChooseDialogPane cardChooseDialogPane = new CardChooseDialogPane(cards);
        dialog.setDialogPane(cardChooseDialogPane);
        dialog.setResizable(true);
        dialog.showAndWait();
        if (dialog.getResult() == ButtonType.OK) {
            return Optional.of(cardChooseDialogPane.getSelectedCard());
        }
        return Optional.empty();
    }

    public static Optional<Integer> showCustomListDialog(List<?> list, String title){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        CustomListDialogPane customListDialogPane = new CustomListDialogPane(list);
        dialog.setDialogPane(customListDialogPane);
        dialog.showAndWait();
        if (dialog.getResult() == ButtonType.OK) {
            return Optional.of(customListDialogPane.getChoice());
        }
        return Optional.empty();
    }

    public static void showAlert(Alert.AlertType alertType, String title, String contentText){
        Alert alert = new Alert(alertType, contentText, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
