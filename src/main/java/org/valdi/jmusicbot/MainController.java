package org.valdi.jmusicbot;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private final JMusicBot main;

    public MainController(JMusicBot main) {
        this.main = main;
    }

    @FXML
    private Menu menuFile;
    @FXML
    private Menu menuEdit;
    @FXML
    private Menu menuHelp;

    @FXML
    private Button activateButton;
    @FXML
    private Button closeButton;

    private static final String START = "Start";
    private static final String STOP = "Stop";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MenuItem showLogger = new MenuItem("Show logger");
        showLogger.setOnAction(action -> {
            try {
                URL url = main.getResourceUrl("fxml/logger.fxml");
                FXMLLoader loader = new FXMLLoader(url);
                loader.setControllerFactory(c -> new LoggerController(main));

                Stage stage = new Stage();
                stage.initOwner(closeButton.getScene().getWindow());
                Image icon = new Image("images/icon_50.png");
                stage.getIcons().add(icon);
                stage.setTitle("JMusicBot logger");
                stage.setScene(new Scene(loader.load()));
                stage.setResizable(false);
                stage.show();

                stage.setOnCloseRequest(e -> {
                    TextArea loggerArea = (TextArea) stage.getScene().lookup("#loggerArea");
                    TextAreaAppender.removeTextArea(loggerArea);

                    stage.close();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        menuHelp.getItems().add(showLogger);
    }

    @FXML
    public void onStartStop() {
        switch (activateButton.getText()) {
            case STOP: {
                activateButton.setText(START);
                // TODO deactivate bot
                break;
            }
            case START: {
                activateButton.setText(STOP);
                // TODO activate bot
                break;
            }
        }
    }

    @FXML
    public void onClose() {
        Platform.runLater(() -> {
            main.stopAll();
            Platform.exit();
            System.exit(0);
        });
    }
}
