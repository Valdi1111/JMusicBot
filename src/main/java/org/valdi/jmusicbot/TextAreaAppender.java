package org.valdi.jmusicbot;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.util.ArrayList;
import java.util.Iterator;

import static org.apache.logging.log4j.core.config.Property.EMPTY_ARRAY;
import static org.apache.logging.log4j.core.layout.PatternLayout.createDefaultLayout;

@Plugin(name = "TextAreaAppender", category = "Core", elementType = "appender", printObject = true)
public class TextAreaAppender extends AbstractAppender {
    private static volatile ArrayList<TextArea> textAreas = new ArrayList<>();
    public static String logged = "";

    private TextAreaAppender(String name, Layout<?> layout, Filter filter, boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions, EMPTY_ARRAY);
    }

    @SuppressWarnings("unused")
    @PluginFactory
    public static TextAreaAppender createAppender(@PluginAttribute("name") String name,
                                                  @PluginAttribute("ignoreExceptions") boolean ignoreExceptions,
                                                  @PluginElement("Layout") Layout<?> layout,
                                                  @PluginElement("Filters") Filter filter) {
        if (name == null) {
            LOGGER.error("No name provided for TextAreaAppender");
            return null;
        }

        if (layout == null) {
            layout = createDefaultLayout();
        }
        return new TextAreaAppender(name, layout, filter, ignoreExceptions);
    }

    // Add the target TextArea to be populated and updated by the logging information.
    public static void addTextArea(final TextArea textArea) {
        TextAreaAppender.textAreas.add(textArea);
    }

    // Remove the target TextArea.
    public static void removeTextArea(final TextArea textArea) {
        TextAreaAppender.textAreas.remove(textArea);
    }

    @Override
    public void append(LogEvent event) {
        String message = new String(this.getLayout().toByteArray(event));

        // Append formatted message to text area using the Thread.
        try {
            Platform.runLater(() -> {
                try {
                    if (logged.isEmpty()) {
                        logged = message;
                    } else {
                        logged += message;
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }

                refreshAll();
            });
        } catch (IllegalStateException e) {
            // ignore case when the platform hasn't yet been initialized
        }
    }

    public static void refreshAll() {
        Iterator<TextArea> i = textAreas.iterator();
        while(i.hasNext()) {
            TextArea textArea = i.next();
            if (textArea == null) {
                i.remove();
                continue;
            }

            textArea.setText(logged);
        }
    }
}