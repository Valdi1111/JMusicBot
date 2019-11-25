package org.valdi.jmusicbot.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * {@link Resource} implementation for something that is read via InputStream.
 *
 * @author Rsl1122
 */
public class JarResource implements Resource {
    private final String resourceName;
    private final StreamSupplier streamSupplier;

    public JarResource(String resourceName, StreamSupplier streamSupplier) {
        this.resourceName = resourceName;
        this.streamSupplier = streamSupplier;
    }

    @Override
    public InputStream asInputStream() throws IOException {
        InputStream stream = streamSupplier.get();
        if (stream == null) {
            throw new FileNotFoundException("a Resource was not found inside the jar (" + resourceName + "), " +
                    "Plan does not support /reload or updates using " +
                    "Plugin Managers, restart the server and see if the error persists.");
        }
        return stream;
    }

    @Override
    public List<String> asLines() throws IOException {
        List<String> lines = new ArrayList<>();
        try (
                InputStream inputStream = asInputStream();
                Scanner scanner = new Scanner(inputStream, "UTF-8")
        ) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        }
        return lines;
    }

    @Override
    public String asString() throws IOException {
        StringBuilder flat = new StringBuilder();
        try (
                InputStream inputStream = asInputStream();
                Scanner scanner = new Scanner(inputStream, "UTF-8")
        ) {
            while (scanner.hasNextLine()) {
                flat.append(scanner.nextLine()).append("\r\n");
            }
        }
        return flat.toString();
    }

    @Override
    public String getResourceName() {
        return resourceName;
    }

    public interface StreamSupplier {
        InputStream get() throws IOException;
    }
}