package org.valdi.jmusicbot;

import org.valdi.jmusicbot.exceptions.EnableException;

/**
 * Represents a system that can be enabled and disabled.
 *
 * @author Rsl1122
 */
public interface SubSystem {

    /**
     * Performs enable actions for the subsystem.
     *
     * @throws EnableException If an error occurred during enable and it is fatal to the subsystem.
     */
    void enable() throws EnableException;

    /**
     * Performs disable actions for the subsystem
     */
    void disable();

}
