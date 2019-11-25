package org.valdi.jmusicbot.prompt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class TerminalPrompt implements IPrompt {
    private final Scanner scanner;
    private final String title;

    public TerminalPrompt(String title) {
        this.scanner = new Scanner(System.in);
        this.title = title;
    }

    @Override
    public void alert(Level level, String header, String context, String message) {
        Logger log = LoggerFactory.getLogger(context);
        switch (level) {
            case WARNING:
                log.warn(header);
                log.warn(message);
                break;
            case ERROR:
                log.error(header);
                log.error(message);
                break;
            default:
                log.info(header);
                log.info(message);
                break;
        }
    }

    @Override
    public String prompt(String header, String content) {
        try {
            System.out.println(header);
            System.out.println(content);
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            }
            return null;
        } catch (Exception e) {
            alert(Level.ERROR, header, title, "Unable to read input from command line.");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean isNoGui() {
        return true;
    }

}
