package org.valdi.jmusicbot.file;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Resource decorator to cache result of asString method call in {@link ResourceCache}.
 *
 * @author Rsl1122
 */
public class StringCachingResource implements Resource {

    private final Resource implementation;

    StringCachingResource(Resource implementation) {
        this.implementation = implementation;
    }

    @Override
    public String getResourceName() {
        return implementation.getResourceName();
    }

    @Override
    public InputStream asInputStream() throws IOException {
        return implementation.asInputStream();
    }

    @Override
    public List<String> asLines() throws IOException {
        return implementation.asLines();
    }

    @Override
    public String asString() throws IOException {
        String got = implementation.asString();
        ResourceCache.cache(implementation.getResourceName(), got);
        return got;
    }
}