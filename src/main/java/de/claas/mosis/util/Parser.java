package de.claas.mosis.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.claas.mosis.io.format.CommaSeparatedValues;
import de.claas.mosis.io.format.JavaScriptObjectNotation;

/**
 * The class {@link Parser}. It is intended to provide a set of convenience
 * methods for parsing text based data formats (e.g.
 * {@link JavaScriptObjectNotation}, {@link CommaSeparatedValues}, etc.).
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public abstract class Parser {

    /**
     * Returns the data that is represented by the pattern. If the data starts
     * with the pattern, then the part that corresponds to this pattern is
     * removed from the buffer and returned. If the data does not start with the
     * pattern, then <code>null</code> is returned.
     * 
     * @param data
     *            the data / buffer
     * @param pattern
     *            the pattern
     * @return the data that is represented by the pattern
     */
    public static String startsWith(StringBuilder data, Pattern pattern) {
	Matcher matcher = pattern.matcher(data);
	if (matcher.find() && matcher.start() == 0) {
	    String tmp = data.substring(matcher.start(), matcher.end());
	    data.replace(matcher.start(), matcher.end(), "");
	    return tmp;
	} else {
	    return null;
	}
    }

    /**
     * Rewinds previously processed data. The already processed data is
     * prepended to the data / buffer, such that it can be processed again.
     * 
     * @param data
     *            the data / buffer
     * @param processed
     *            the already processed data
     */
    public static void unprocess(StringBuilder data, StringBuilder processed) {
	if (processed.length() > 0) {
	    data.reverse().append(processed.reverse()).reverse();
	}
    }

}
