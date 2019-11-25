package org.valdi.jmusicbot.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Interface for accessing plugin resources in jar or plugin files.
 *
 * @author Rsl1122
 */
public interface Resource {

    /**
     * Get the name of this Resource.
     *
     * @return Relative file path given to {@link PlanFiles}.
     */
    String getResourceName();

    /**
     * Get the resource as an InputStream.
     *
     * @return InputStream of the resource, not closed automatically.
     * @throws IOException If the resource is unavailable.
     */
    InputStream asInputStream() throws IOException;

    /**
     * Get the resource as lines.
     *
     * @return Lines of the resource file.
     * @throws IOException If the resource is unavailable.
     */
    List<String> asLines() throws IOException;

    /**
     * Get the resource as a String with each line separated by CRLF newline characters {@code \r\n}.
     *
     * @return Flat string with each line separated by {@code \r\n}.
     * @throws IOException If the resource is unavailable.
     */
    String asString() throws IOException;

}