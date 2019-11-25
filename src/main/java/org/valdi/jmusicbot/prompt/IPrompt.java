package org.valdi.jmusicbot.prompt;

public interface IPrompt {

    void alert(Level level, String header, String context, String message);

    String prompt(String header, String content);

    boolean isNoGui();

    enum Level {
        INFO, WARNING, ERROR;
    }
}
