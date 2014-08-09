package de.claas.mosis.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.regex.Pattern;

import org.junit.Test;

/**
 * The JUnit test for class {@link Parser}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class ParserTest {

    private final static String Data = "123abcdef";
    private final static String ValidPattern1 = "[0-9]+";
    private final static String ValidPattern2 = "[a-z]+";

    @Test
    public void shouldRemoveMatchingPatternFromBuffer() {
	StringBuilder data = new StringBuilder(Data);
	assertEquals(Data.length(), data.length());
	String processed = Parser.startsWith(data,
		Pattern.compile(ValidPattern1));
	assertNotNull(processed);
	assertEquals(Data.length(), processed.length() + data.length());

	processed = Parser.startsWith(data, Pattern.compile(ValidPattern2));
	assertNotNull(processed);
	assertEquals(0, data.length());
    }

    @Test
    public void shouldNotRemoveNonMatchingPatternFromBuffer() {
	StringBuilder data = new StringBuilder(Data);
	assertEquals(Data.length(), data.length());
	String processed = Parser.startsWith(data,
		Pattern.compile(ValidPattern2));
	assertNull(processed);
	assertEquals(Data.length(), data.length());
    }

    @Test
    public void shouldHandleExtremeValues() {
	Pattern pattern = Pattern.compile("");
	assertEquals("", Parser.startsWith(new StringBuilder(), pattern));
	assertEquals("", Parser.startsWith(new StringBuilder("test"), pattern));
    }

    @Test
    public void shouldPrependDataToBuffer() {
	StringBuilder data = new StringBuilder("world");
	Parser.unprocess(data, new StringBuilder());
	assertEquals("world", data.toString());
	Parser.unprocess(data, new StringBuilder("hello "));
	assertEquals("hello world", data.toString());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowException1() {
	Parser.startsWith(null, Pattern.compile(ValidPattern1));
    }

    @Test(expected = NullPointerException.class)
    public void shouldAlsoThrowException() {
	Parser.unprocess(new StringBuilder(), null);
    }

}
