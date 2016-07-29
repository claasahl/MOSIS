package de.claas.mosis.model;

import org.junit.Test;

import java.lang.annotation.ElementType;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.model.Data}. It is intended to
 * collect and document a set of test cases for the tested class. Please refer
 * to the individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class DataTest {

    private final static String KeyString = "keyString";
    private final static String ValueString = "hello world";
    private final static String KeyDouble = "keyDouble";
    private final static double ValueDouble = 23.4;
    private final static String KeyLong = "keyLong";
    private final static long ValueLong = System.currentTimeMillis();
    private final static String KeyEnum = "keyEnum";
    private final static Class<? extends Enum<?>> ClassEnum = ElementType.class;
    private final static ElementType ValueEnum = ElementType.LOCAL_VARIABLE;
    private final static String KeyNull = "keyNull";

    /**
     * Returns an instantiated {@link de.claas.mosis.model.Data} class. If
     * appropriate, the instance is configured with default values.
     *
     * @return an instantiated {@link de.claas.mosis.model.Data} class
     */
    private Data build() {
        Data d = new Data();
        d.put(KeyDouble, ValueDouble);
        d.put(KeyEnum, ValueEnum);
        d.put(KeyLong, ValueLong);
        d.put(KeyNull, null);
        d.put(KeyString, ValueString);
        return d;
    }

    @Test
    public void shouldCastValues() {
        Data d = build();
        assertNull(d.getAs(KeyNull));
        assertEquals(ValueDouble, d.<Double>getAs(KeyDouble), 0.0001);
        assertEquals(ValueEnum, d.getAs(KeyEnum));
        assertEquals(ValueLong, (long)d.getAs(KeyLong));
        assertEquals(ValueString, d.getAs(KeyString));

    }

    @Test(expected = ClassCastException.class)
    public void shouldNotCastValue() {
        Data d = build();
        String tmp = d.getAs(KeyDouble);
        assertEquals(Double.toString(ValueDouble), tmp);
    }

    @Test
    public void shouldReturnAsString() {
        Data d = build();
        assertEquals("null", d.getAsString(KeyNull));
        assertEquals(Double.toString(ValueDouble), d.getAsString(KeyDouble));
        assertEquals(ValueEnum.name(), d.getAsString(KeyEnum));
        assertEquals(Long.toString(ValueLong), d.getAsString(KeyLong));
        assertEquals(ValueString, d.getAsString(KeyString));
    }

    @Test
    public void shouldReturnAsNumber() {
        Data d = build();
        assertNull(d.getAsNumber(KeyNull));
        assertEquals(ValueDouble, d.getAsNumber(KeyDouble));
        assertEquals(ValueLong, d.getAsNumber(KeyLong));

        d.put("a", "123.45");
        assertEquals(123.45, d.getAsNumber("a").doubleValue(), 0.0001);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldNotReturnAsNumber() {
        Data d = build();
        d.getAsNumber(KeyString);
    }

    @Test
    public void shouldReturnAsNominal() {
        Data d = build();
        assertNull(d.getAsNumber(KeyNull));
        assertEquals(ValueEnum, d.getAsNominal(KeyEnum, ClassEnum));

        d.put(KeyEnum, ValueEnum.ordinal());
        assertEquals(ValueEnum, d.getAsNominal(KeyEnum, ClassEnum));

        d.put(KeyEnum, Integer.toString(ValueEnum.ordinal()));
        assertEquals(ValueEnum, d.getAsNominal(KeyEnum, ClassEnum));

        d.put(KeyEnum, ValueEnum.name());
        assertEquals(ValueEnum, d.getAsNominal(KeyEnum, ClassEnum));
    }

    @Test
    public void shouldNotReturnAsNominal() {
        Data d = build();
        try {
            d.getAsNominal(KeyDouble, ClassEnum);
            fail(String.format(
                    "Double value (%f) should not be interpreted as nominal.",
                    ValueDouble));
        } catch (Exception e) {
            // Exception is expected
        }
        try {
            d.getAsNominal(KeyLong, ClassEnum);
            fail(String.format(
                    "Long value (%f) should not be interpreted as nominal.",
                    ValueDouble));
        } catch (Exception e) {
            // Exception is expected
        }
    }

}
