package org.valdi.jmusicbot.delivery.domain.keys;

import java.util.Objects;

/**
 * Identifier used for storing and fetching data from DataContainers.
 *
 * @param <T> Type of the object returned by the Value identified by this Key.
 * @author Rsl1122
 */
public class Key<T> {

    private final Type<T> type;
    private final String keyName;

    /**
     * Create a new key.
     * <p>
     * Example usage:
     * {@code Key<String> key = new Key(String.class, "identifier");}
     * <p>
     * (In Keys class) {@code public static final Key<String> IDENTIFIER = new Key(String.class, "identifier");}
     * {@code Key<String> key = Keys.IDENTIFIER;}
     *
     * @param type    Class with type of the Object returned by the Value identified by this Key.
     * @param keyName Name (identifier) of the Key.
     */
    public Key(Class<T> type, String keyName) {
        this(Type.ofClass(type), keyName);
    }

    public Key(Type<T> type, String keyName) {
        this.type = type;
        this.keyName = keyName;
    }

    /**
     * Get the type of the key.
     *
     * @return specified in constructor.
     */
    public Type<T> getType() {
        return type;
    }

    /**
     * Get the name (identifier) of the Key.
     *
     * @return For example "nickname"
     */
    public String getKeyName() {
        return keyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key<?> key = (Key<?>) o;
        return Objects.equals(type, key.type) &&
                Objects.equals(keyName, key.keyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, keyName);
    }

    /**
     * Cast an object to the type of the key.
     *
     * @param object Object to cast.
     * @return The object with the type of T.
     */
    public T typeCast(Object object) {
        // Since Type can have a List<Subtype>, Class can not be obtained in order to use Class while casting.
        // This could be done since Google Gson does it, but I don't know how they do it since
        // the implementation is hidden.
        return (T) object;
    }
}