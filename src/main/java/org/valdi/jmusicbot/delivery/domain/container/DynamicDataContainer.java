package org.valdi.jmusicbot.delivery.domain.container;

import org.valdi.jmusicbot.delivery.domain.keys.Key;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * DataContainer implementation that delegates the method calls to other DataContainer implementations.
 *
 * @author Rsl1122
 */
public class DynamicDataContainer implements DataContainer {

    private final SupplierDataContainer supplierDataContainer;
    private final RawDataContainer rawDataContainer;

    public DynamicDataContainer() {
        supplierDataContainer = new SupplierDataContainer();
        rawDataContainer = new RawDataContainer();
    }

    public DynamicDataContainer(long timeToLive) {
        supplierDataContainer = new SupplierDataContainer(timeToLive);
        rawDataContainer = new RawDataContainer();
    }

    @Override
    public <T> void putRawData(Key<T> key, T obj) {
        rawDataContainer.putRawData(key, obj);
    }

    @Override
    public <T> void putSupplier(Key<T> key, Supplier<T> supplier) {
        supplierDataContainer.putSupplier(key, supplier);
    }

    @Override
    public <T> void putCachingSupplier(Key<T> key, Supplier<T> supplier) {
        supplierDataContainer.putCachingSupplier(key, supplier);
    }

    @Override
    public <T> boolean supports(Key<T> key) {
        return rawDataContainer.supports(key) || supplierDataContainer.supports(key);
    }

    @Override
    public <T> Optional<T> getValue(Key<T> key) {
        Optional<T> raw = rawDataContainer.getValue(key);
        if (raw.isPresent()) {
            return raw;
        } else {
            return supplierDataContainer.getValue(key);
        }
    }

    @Override
    public <T> T getUnsafe(Key<T> key) {
        if (rawDataContainer.supports(key)) {
            return rawDataContainer.getUnsafe(key);
        } else {
            return supplierDataContainer.getUnsafe(key);
        }
    }

    @Override
    public void putAll(DataContainer dataContainer) {
        if (dataContainer instanceof SupplierDataContainer) {
            supplierDataContainer.putAll(dataContainer);
        } else if (dataContainer instanceof RawDataContainer) {
            rawDataContainer.putAll(dataContainer);
        } else {
            rawDataContainer.putAll(dataContainer.getMap());
        }
    }

    @Override
    public void clear() {
        rawDataContainer.clear();
        supplierDataContainer.clear();
    }

    @Override
    public Map<Key, Object> getMap() {
        Map<Key, Object> map = supplierDataContainer.getMap();
        map.putAll(rawDataContainer.getMap());
        return map;
    }
}