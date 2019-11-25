package org.valdi.jmusicbot;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.valdi.jmusicbot.prompt.GuiPrompt;

import java.net.URL;

public class Gui extends Application {
    private static JMusicBot main;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        URL url = main.getResourceUrl("fxml/main.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setControllerFactory(c -> new MainController(main));

        stage.setScene(new Scene(loader.load()));
        Image icon = new Image("images/icon_50.png");
        stage.getIcons().add(icon);
        stage.setTitle("JMusicBot");
        stage.setResizable(false);

        stage.setOnCloseRequest(e -> {
            main.preStopAll();
            Platform.exit();
            System.exit(0);
        });

        this.stage = stage;
        main.setPrompt(new GuiPrompt(stage, "JMusicBot"));
        main.startAll();
        stage.show();
    }

    public static void main(JMusicBot instance, String[] args) {
        main = instance;
        launch(args);
    }

    public Stage getStage() {
        return stage;
    }
}
