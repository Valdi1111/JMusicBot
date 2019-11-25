package org.valdi.jmusicbot.file;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * {@link Resource} implementation for a {@link String}.
 *
 * @author Rsl1122
 */
public class StringResource implements Resource {

    private final String resourceName;
    private final String resource;

    StringResource(String resourceName, String resource) {
        this.resourceName = resourceName;
        this.resource = resource;
    }

    @Override
    public String getResourceName() {
        return resourceName;
    }

    @Override
    public InputStream asInputStream() {
        return new ByteArrayInputStream(resource.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public List<String> asLines() {
        return Arrays.asList(StringUtils.split(resource, "\r\n"));
    }

    @Override
    public String asString() {
        return resource;
    }
}