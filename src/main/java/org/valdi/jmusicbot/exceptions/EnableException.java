package org.valdi.jmusicbot.exceptions;

/**
 * Thrown when something goes wrong with Plan initialization.
 *
 * @author Rsl1122
 */
public class EnableException extends Exception {

    public EnableException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnableException(String message) {
        super(message);
    }
}
