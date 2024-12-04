package hr.algebra.everdell;

import hr.algebra.everdell.models.PlayerState;
import hr.algebra.everdell.utils.PlayerStateSingleton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class EverdellApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        FXMLLoader fxmlLoader = new FXMLLoader(EverdellApplication.class.getResource("MainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Everdell");
        stage.setScene(scene);
        stage.setWidth(900);
        stage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - stage.getWidth());
        stage.setY(0);
        stage.setOnCloseRequest(_ -> Platform.exit());
        stage.show();
    }

    public static void main(String[] args) {
        PlayerState.playerName = args[0];
        launch();
    }

}