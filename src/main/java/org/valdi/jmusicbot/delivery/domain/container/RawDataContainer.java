package org.valdi.jmusicbot.delivery.domain.container;

import org.valdi.jmusicbot.delivery.domain.keys.Key;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * DataContainer that stores everything as raw object value.
 *
 * @author Rsl1122
 */
public class RawDataContainer implements DataContainer {

    private final Map<Key, Object> map;

    /**
     * Create a RawDataContainer.
     */
    public RawDataContainer() {
        map = new HashMap<>();
    }

    @Override
    public <T> void putRawData(Key<T> key, T obj) {
        if (obj == null) {
            return;
        }
        map.put(key, obj);
    }

    @Override
    public <T> void putSupplier(Key<T> key, Supplier<T> supplier) {
        if (supplier == null) {
            return;
        }
        putRawData(key, supplier.get());
    }

    @Override
    public <T> void putCachingSupplier(Key<T> key, Supplier<T> supplier) {
        putSupplier(key, supplier);
    }

    @Override
    public <T> boolean supports(Key<T> key) {
        return map.containsKey(key);
    }

    @Override
    public <T> Optional<T> getValue(Key<T> key) {
        try {
            return Optional.ofNullable(key.typeCast(map.get(key)));
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }

    @Override
    public <T> T getUnsafe(Key<T> key) {
        Object value = map.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Unsupported Key: " + key.getKeyName());
        }
        return key.typeCast(value);
    }

    @Override
    public void putAll(DataContainer dataContainer) {
        if (dataContainer instanceof RawDataContainer) {
            putAll(((RawDataContainer) dataContainer).map);
        } else {
            putAll(dataContainer.getMap());
        }
    }

    void putAll(Map<Key, Object> map) {
        this.map.putAll(map);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Map<Key, Object> getMap() {
        return map;
    }
}