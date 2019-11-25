package org.valdi.jmusicbot.prompt;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.util.Optional;

public class GuiPrompt implements IPrompt {
    private final Stage parent;
    private final String title;

    public GuiPrompt(Stage parent, String title) {
        this.parent = parent;
        this.title = title;
    }

    @Override
    public void alert(Level level, String header, String context, String message) {
        Alert.AlertType option;
        switch (level) {
            case WARNING:
                option = Alert.AlertType.WARNING;
                break;
            case ERROR:
                option = Alert.AlertType.ERROR;
                break;
            default:
                option = Alert.AlertType.INFORMATION;
                break;
        }

        Alert alert = new Alert(option);
        alert.initOwner(parent);
        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.setResizable(false);
        alert.showAndWait();
    }

    @Override
    public String prompt(String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.initOwner(parent);
        dialog.setTitle(title);
        dialog.setResizable(false);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        while(!result.isPresent()) {
            result = dialog.showAndWait();
        }
        return result.get();
    }

    @Override
    public boolean isNoGui() {
        return false;
    }

}
