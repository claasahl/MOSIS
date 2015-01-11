package de.claas.mosis.model;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * The class {@link de.claas.mosis.model.Data}. It is intended to provide a common data exchange
 * format. The main purpose is to simplify reading of streams and other
 * resources. It provides a unified interface for accessing and setting values
 * of various types (e.g. string, numeric, nominal).
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class Data extends LinkedHashMap<String, Object> {

    private static final long serialVersionUID = 3112587100018955036L;

    /**
     * Returns the key's value and casts it to a proper type. Any object is
     * (dynamically) casted to the stated type {@code T}.
     *
     * @param key the key, which value it to be returned
     * @return the key's value
     */
    @SuppressWarnings("unchecked")
    public <T> T getAs(String key) {
        return (T) get(key);
    }

    /**
     * Returns the key's value as {@link java.lang.String}. Any object is returned as
     * {@link java.lang.String} using the {@link #toString()} method. If the value is
     * {@code null}, then "null" (as a string) is returned.
     *
     * @param key the key, which value it to be returned
     * @return the key's value as {@link java.lang.String}
     */
    public String getAsString(String key) {
        Object value = get(key);
        return value == null ? "null" : value.toString();
    }

    /**
     * Returns the key's value as {@link java.lang.Number}. Any instance of {@link java.lang.Number}
     * is recognized (e.g. {@link java.lang.Double}, {@link java.lang.Long}, etc.). Strings are
     * parsed and returned as number. If the value equals {@code null}, then
     * {@code null} is returned. If the value could not be interpreted as
     * number, then a {@link java.lang.NumberFormatException} is thrown.
     *
     * @param key the key, which value it to be returned
     * @return the key's value as {@link java.lang.Double}
     */
    public Number getAsNumber(String key) {
        Object value = get(key);
        if (value == null) {
            return null;
        } else if (value instanceof Number) {
            return (Number) value;
        } else if (value instanceof String) {
            return new BigDecimal(value.toString());
        } else {
            throw new NumberFormatException();
        }
    }

    /**
     * Returns the key's value as {@link java.lang.Enum}. Any {@link java.lang.Enum} is returned as
     * such. Any {@link java.lang.Number} or {@link java.lang.String} representation of a number is
     * interpreted as the enumeration's index / constant and the corresponding
     * {@link java.lang.Enum} is returned. Any {@link java.lang.String} is interpreted as the
     * enumeration's name and the corresponding {@link java.lang.Enum} is returned. If the
     * value equals {@code null}, then {@code null} is returned.
     *
     * @param key the key, which value it to be returned
     * @return the key's value as {@link java.lang.Enum}
     */
    @SuppressWarnings("unchecked")
    public <T extends Enum<?>> T getAsNominal(String key, Class<T> nominal) {
        Object value = get(key);
        if (value != null && nominal.isAssignableFrom(value.getClass())) {
            return (T) value;
        } else if (value != null && value instanceof Number) {
            int index = ((Number) value).intValue();
            return nominal.getEnumConstants()[index];
        } else if (value != null && value instanceof String) {
            for (T constant : nominal.getEnumConstants()) {
                if (value.toString().equals(constant.name())) {
                    return constant;
                }
            }
            int index = new BigDecimal(value.toString()).intValue();
            return nominal.getEnumConstants()[index];
        } else {
            return null;
        }
    }

}
