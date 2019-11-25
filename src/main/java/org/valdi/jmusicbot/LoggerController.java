package org.valdi.jmusicbot;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggerController implements Initializable {
    private final JMusicBot main;

    @FXML
    private TextArea loggerArea;

    public LoggerController(JMusicBot main) {
        this.main = main;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TextAreaAppender.addTextArea(loggerArea);
        TextAreaAppender.refreshAll();
    }
}
