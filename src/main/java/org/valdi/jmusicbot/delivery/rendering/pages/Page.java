package org.valdi.jmusicbot.delivery.rendering.pages;

import org.valdi.jmusicbot.exceptions.ParseException;

/**
 * Interface for parsing page HTML.
 *
 * @author Rsl1122
 */
public interface Page {

    String toHtml() throws ParseException;

}