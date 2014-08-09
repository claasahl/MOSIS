package de.claas.mosis.io.format;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import de.claas.mosis.io.FileImpl;
import de.claas.mosis.io.StreamHandler;
import de.claas.mosis.io.StreamHandlerImpl;
import de.claas.mosis.io.UrlImpl;
import de.claas.mosis.model.Data;
import de.claas.mosis.util.Parser;

/**
 * The class {@link JavaScriptObjectNotation}. It is intended to read and write
 * JSON data (JavaScript Object Notation). This {@link StreamHandler} allows to
 * read and write JSON data from any of the {@link StreamHandlerImpl}
 * implementations (e.g. {@link FileImpl} or {@link UrlImpl}).
 * 
 * This implementation is based on the definitions given in RFC 4627, which is
 * publicly available at http://www.ietf.org/rfc/rfc4180.txt. The therein
 * contained EBNF grammar was slightly modified and the final result is listed
 * below:
 * 
 * Terminal symbols:
 * <ol>
 * <li>JSON-text = object / array</li>
 * </ol>
 * <ol>
 * <li>begin-array = ws %x5B ws ; [ left square bracket</li>
 * <li>begin-object = ws %x7B ws ; { left curly bracket</li>
 * <li>end-array = ws %x5D ws ; ] right square bracket</li>
 * <li>end-object = ws %x7D ws ; } right curly bracket</li>
 * <li>name-separator = ws %x3A ws ; : colon</li>
 * <li>value-separator = ws %x2C ws ; , comma</li>
 * <li>ws = *(</li>
 * </ol>
 * <ol>
 * <li>%x20 / ; Space</li>
 * <li>%x09 / ; Horizontal tab</li>
 * <li>%x0A / ; Line feed or New line</li>
 * <li>%x0D ; Carriage return</li>
 * <li>)</li>
 * </ol>
 * <ol>
 * <li>value = false / null / true / object / array / number / string</li>
 * <li>false = %x66.61.6c.73.65 ; false</li>
 * <li>null = %x6e.75.6c.6c ; null</li>
 * <li>true = %x74.72.75.65 ; true</li>
 * </ol>
 * <ol>
 * <li>object = begin-object [ member *( value-separator member ) ] end-object</li>
 * <li>member = string name-separator value</li>
 * <li>array = begin-array [ value *( value-separator value ) ] end-array</li>
 * </ol>
 * <ol>
 * <li>number = [ minus ] integer [ frac ] [ exp ]</li>
 * <li>decimal-point = %x2E ; .</li>
 * <li>DIGIT1-9 = %x31-39 ; 1-9</li>
 * <li>DIGIT = %x30-39 ; 0-9</li>
 * <li>e = %x65 / %x45 ; e E</li>
 * <li>exp = e [ minus / plus ] +DIGIT</li>
 * <li>frac = decimal-point +DIGIT</li>
 * <li>integer = zero / ( DIGIT1-9 *DIGIT )</li>
 * <li>minus = %x2D ; -</li>
 * <li>plus = %x2B ; +</li>
 * <li>zero = %x30 ; 0</li>
 * <li></li>
 * <li>string = quotation-mark *character quotation-mark</li>
 * <li>character = unescaped / escaped</li>
 * <li>escaped = %x5C (</li>
 * </ol>
 * <ol>
 * <li>%x22 / ; " quotation mark U+0022</li>
 * <li>%x5C / ; \ reverse solidus U+005C</li>
 * <li>%x2F / ; / solidus U+002F</li>
 * <li>%x62 / ; b backspace U+0008</li>
 * <li>%x66 / ; f form feed U+000C</li>
 * <li>%x6E / ; n line feed U+000A</li>
 * <li>%x72 / ; r carriage return U+000D</li>
 * <li>%x74 / ; t tab U+0009</li>
 * <li>%x75 4HEXDIG ; uXXXX U+XXXX</li>
 * <li>)</li>
 * </ol>
 * <ol>
 * <li>quotation-mark = %x22 ; "</li>
 * <li>unescaped = %x20-21 / %x23-5B / %x5D-10FFFF</li>
 * </ol>
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class JavaScriptObjectNotation extends AbstractTextFormat<Data> {

    private final Pattern BeginArray;
    private final Pattern BeginObject;
    private final Pattern EndArray;
    private final Pattern EndObject;
    private final Pattern NameSeparator;
    private final Pattern ValueSeparator;
    private final Pattern False;
    private final Pattern Null;
    private final Pattern True;
    private final Pattern DecimalPoint;
    private final Pattern Digit1To9;
    private final Pattern Digit;
    private final Pattern E;
    private final Pattern Minus;
    private final Pattern Plus;
    private final Pattern Zero;
    private final Pattern QuotationMark;
    private final Pattern Unescaped;
    private final Pattern Escaped;
    private final StringBuilder _JSON;

    /**
     * Initializes the class with default values.
     */
    public JavaScriptObjectNotation() {
	BeginArray = Pattern.compile("\\s*\\x5B\\s*");
	BeginObject = Pattern.compile("\\s*\\x7B\\s*");
	EndArray = Pattern.compile("\\s*\\x5D\\s*");
	EndObject = Pattern.compile("\\s*\\x7D\\s*");
	NameSeparator = Pattern.compile("\\s*\\x3A\\s*");
	ValueSeparator = Pattern.compile("\\s*\\x2C\\s*");
	False = Pattern.compile("\\x66\\x61\\x6c\\x73\\x65");
	Null = Pattern.compile("\\x6e\\x75\\x6c\\x6c");
	True = Pattern.compile("\\x74\\x72\\x75\\x65");
	DecimalPoint = Pattern.compile("\\x2E");
	Digit1To9 = Pattern.compile("[\\x31-\\x39]");
	Digit = Pattern.compile("[\\x30-\\x39]");
	E = Pattern.compile("[\\x65\\x45]");
	Minus = Pattern.compile("\\x2D");
	Plus = Pattern.compile("\\x2B");
	Zero = Pattern.compile("\\x30");
	QuotationMark = Pattern.compile("\\x22");
	Unescaped = Pattern
		.compile("[\\x20-\\x21\\x23-\\x5B\\x5D-\\x{10FFFF}]");
	Escaped = Pattern
		.compile("\\x5C([\\x22\\x5C\\x2F\\x62\\x66\\x6E\\x72\\x74]|\\x75[0-9a-f]{4})");
	_JSON = new StringBuilder();
    }

    @Override
    public void process(List<Data> in, List<Data> out) {
	try {
	    if (isReadOnly(in)) {
		// Keep buffer updated
		String line = readLine(true);
		if (line != null) {
		    _JSON.append(line);
		}

		// Parse JSON
		if (_JSON.length() > 0) {
		    Data data = new Data();
		    List<Object> values = new Vector<>();
		    String processed = jsonText(new StringBuilder(_JSON), data,
			    values);
		    if (processed != null) {
			_JSON.replace(0, processed.length(), "");
			out.add(data);
			// TODO How to differentiate between arrays and objects?
			// TODO How should arrays be handled at all?
		    }
		}
	    } else {
		for (Data datum : in) {
		    writeLine(toJSON(datum), true);
		}
		if (shouldFoward()) {
		    out.addAll(in);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Returns the JSON data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid JSON data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * 
     * <b>JSON-text = object / array</b>
     * 
     * @param json
     *            the JSON data that is being processed
     * @param data
     *            if data was successfully processed and it was a JSON object,
     *            then the {@link Data} instance will be updated to represent
     *            the processed JSON object
     * @param values
     *            if data was successfully processed and it was a JSON array,
     *            then the list will be updated to represent the processed JSON
     *            array
     * @return the JSON data that was processed by this call
     */
    private String jsonText(StringBuilder json, Data data, List<Object> values) {
	String jsonText;

	// object
	if ((jsonText = object(json, data)) != null) {
	    return jsonText;
	}

	// array
	if ((jsonText = array(json, values)) != null) {
	    return jsonText;
	}
	return null;
    }

    /**
     * Returns the JSON data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid JSON data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * 
     * <b>object = begin-object [ member *( value-separator member ) ]
     * end-object</b>
     * 
     * @param json
     *            the JSON data that is being processed
     * @param data
     *            if data was successfully processed, then the {@link Data}
     *            instance will be updated to represent the processed JSON
     *            object
     * @return the JSON data that was processed by this call
     */
    private String object(StringBuilder json, Data data) {
	StringBuilder processed = new StringBuilder();
	String object;

	// begin-object
	if ((object = Parser.startsWith(json, BeginObject)) == null) {
	    return null;
	}
	processed.append(object);

	// [ member *( value-separator member ) ]
	if ((object = member(json, data)) != null) {
	    processed.append(object);
	    while ((object = Parser.startsWith(json, ValueSeparator)) != null) {
		processed.append(object);
		if ((object = member(json, data)) == null) {
		    Parser.unprocess(json, processed);
		    return null;
		}
		processed.append(object);
	    }
	}

	// end-object
	if ((object = Parser.startsWith(json, EndObject)) == null) {
	    Parser.unprocess(json, processed);
	    return null;
	}
	processed.append(object);
	return processed.toString();
    }

    /**
     * Returns the JSON data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid JSON data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * 
     * <b>array = begin-array [ value *( value-separator value ) ] end-array</b>
     * 
     * @param json
     *            the JSON data that is being processed
     * @param values
     *            if data was successfully processed, then the list will be
     *            updated to represent the processed JSON array
     * @return the JSON data that was processed by this call
     */
    private String array(StringBuilder json, List<Object> values) {
	StringBuilder processed = new StringBuilder();
	String array;

	// begin-array
	if ((array = Parser.startsWith(json, BeginArray)) == null) {
	    return null;
	}
	processed.append(array);

	// [ value *( value-separator value ) ]
	if ((array = value(json, values)) != null) {
	    processed.append(array);
	    while ((array = Parser.startsWith(json, ValueSeparator)) != null) {
		processed.append(array);
		if ((array = value(json, values)) == null) {
		    Parser.unprocess(json, processed);
		    return null;
		}
		processed.append(array);
	    }
	}

	// end-array
	if ((array = Parser.startsWith(json, EndArray)) == null) {
	    Parser.unprocess(json, processed);
	    return null;
	}
	processed.append(array);
	return processed.toString();
    }

    /**
     * Returns the JSON data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid JSON data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * 
     * <b>member = string name-separator value</b>
     * 
     * @param json
     *            the JSON data that is being processed
     * @param data
     *            if data was successfully processed, then the {@link Data}
     *            instance will be updated with the processed member / field
     * @return the JSON data that was processed by this call
     */
    private String member(StringBuilder json, Data data) {
	StringBuilder processed = new StringBuilder();
	List<Object> values = new Vector<>();
	StringBuilder name = new StringBuilder();
	String member;

	// string
	if ((member = string(json, name)) == null) {
	    return null;
	}
	processed.append(member);

	// name-separator
	if ((member = Parser.startsWith(json, NameSeparator)) == null) {
	    Parser.unprocess(json, processed);
	    return null;
	}
	processed.append(member);

	// value
	if ((member = value(json, values)) == null) {
	    Parser.unprocess(json, processed);
	    return null;
	}
	data.put(name.toString(), values.get(0));
	processed.append(member);
	return processed.toString();
    }

    /**
     * Returns the JSON data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid JSON data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * 
     * <b>string = quotation-mark *character quotation-mark</b>
     * 
     * @param json
     *            the JSON data that is being processed
     * @param text
     *            if data was successfully processed, then the
     *            {@link StringBuilder} instance will be updated with the text
     *            in between the quotes
     * @return the JSON data that was processed by this call
     */
    private String string(StringBuilder json, StringBuilder text) {
	StringBuilder processed = new StringBuilder();
	String string;

	// quotation-mark
	if ((string = Parser.startsWith(json, QuotationMark)) == null) {
	    return null;
	}
	processed.append(string);

	// *char
	while ((string = character(json, text)) != null) {
	    processed.append(string);
	}

	// quotation-mark
	if ((string = Parser.startsWith(json, QuotationMark)) == null) {
	    Parser.unprocess(json, processed);
	    return null;
	}
	processed.append(string);
	return processed.toString();
    }

    /**
     * Returns the JSON data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid JSON data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * 
     * <b>character = unescaped / escaped</b>
     * 
     * @param json
     *            the JSON data that is being processed * @param text if data
     *            was successfully processed, then the {@link StringBuilder}
     *            instance will be updated with the text in between the quotes
     * @return the JSON data that was processed by this call
     */
    private String character(StringBuilder json, StringBuilder text) {
	String character;

	// unescaped
	if ((character = Parser.startsWith(json, Unescaped)) != null) {
	    text.append(character);
	    return character;
	}

	// escaped
	if ((character = Parser.startsWith(json, Escaped)) != null) {
	    // TODO Handle escaped characters.
	    text.append(character);
	    return character;
	}
	return null;
    }

    /**
     * Returns the JSON data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid JSON data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * 
     * <b>value = false / null / true / object / array / number / string</b>
     * 
     * @param json
     *            the JSON data that is being processed
     * @param values
     *            if data was successfully processed, then the list will be
     *            updated the processed value
     * @return the JSON data that was processed by this call
     */
    private String value(StringBuilder json, List<Object> values) {
	String value;

	// false
	if ((value = Parser.startsWith(json, False)) != null) {
	    values.add(Boolean.FALSE);
	    return value;
	}

	// null
	if ((value = Parser.startsWith(json, Null)) != null) {
	    values.add(null);
	    return value;
	}

	// true
	if ((value = Parser.startsWith(json, True)) != null) {
	    values.add(Boolean.TRUE);
	    return value;
	}

	// object
	Data data = new Data();
	if ((value = object(json, data)) != null) {
	    values.add(data);
	    return value;
	}

	// array
	List<Object> array = new Vector<>();
	if ((value = array(json, array)) != null) {
	    values.add(array);
	    return value;
	}

	// number
	if ((value = number(json)) != null) {
	    values.add(new BigDecimal(value));
	    return value;
	}

	// string
	StringBuilder text = new StringBuilder();
	if ((value = string(json, text)) != null) {
	    values.add(text.toString());
	    return value;
	}
	return null;
    }

    /**
     * Returns the JSON data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid JSON data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * 
     * <b>number = [ minus ] integer [ frac ] [ exp ]</b>
     * 
     * @param json
     *            the JSON data that is being processed
     * @return the JSON data that was processed by this call
     */
    private String number(StringBuilder json) {
	StringBuilder processed = new StringBuilder();
	String number;

	// [ minus ]
	if ((number = Parser.startsWith(json, Minus)) != null) {
	    processed.append(number);
	}

	// integer
	if ((number = integer(json)) == null) {
	    Parser.unprocess(json, processed);
	    return null;
	}
	processed.append(number);

	// [ frac ]
	if ((number = frac(json)) != null) {
	    processed.append(number);
	}

	// [ exp ]
	if ((number = exp(json)) != null) {
	    processed.append(number);
	}
	return processed.toString();
    }

    /**
     * Returns the JSON data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid JSON data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * 
     * <b>integer = zero / ( DIGIT1-9 *DIGIT )</b>
     * 
     * @param json
     *            the JSON data that is being processed
     * @return the JSON data that was processed by this call
     */
    private String integer(StringBuilder json) {
	StringBuilder processed = new StringBuilder();
	String integer;

	// zero
	if ((integer = Parser.startsWith(json, Zero)) != null) {
	    return integer;
	}

	// ( DIGIT1-9 *DIGIT )
	if ((integer = Parser.startsWith(json, Digit1To9)) == null) {
	    return null;
	}
	do {
	    processed.append(integer);
	} while ((integer = Parser.startsWith(json, Digit)) != null);
	return processed.toString();
    }

    /**
     * Returns the JSON data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid JSON data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * 
     * <b>frac = decimal-point +DIGIT</b>
     * 
     * @param json
     *            the JSON data that is being processed
     * @return the JSON data that was processed by this call
     */
    private String frac(StringBuilder json) {
	StringBuilder processed = new StringBuilder();
	String frac;

	// decimal-point
	if ((frac = Parser.startsWith(json, DecimalPoint)) == null) {
	    return null;
	}
	processed.append(frac);

	// +DIGIT
	if ((frac = Parser.startsWith(json, Digit)) == null) {
	    Parser.unprocess(json, processed);
	    return null;
	}
	do {
	    processed.append(frac);
	} while ((frac = Parser.startsWith(json, Digit)) != null);
	return processed.toString();
    }

    /**
     * Returns the JSON data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid JSON data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * 
     * <b>exp = e [ minus / plus ] +DIGIT</b>
     * 
     * @param json
     *            the JSON data that is being processed
     * @return the JSON data that was processed by this call
     */
    private String exp(StringBuilder json) {
	StringBuilder processed = new StringBuilder();
	String exp;

	// e
	if ((exp = Parser.startsWith(json, E)) == null) {
	    return null;
	}
	processed.append(exp);

	// [ minus / plus ]
	if ((exp = Parser.startsWith(json, Minus)) != null) {
	    processed.append(exp);
	} else if ((exp = Parser.startsWith(json, Plus)) != null) {
	    processed.append(exp);
	}

	// +DIGIT
	if ((exp = Parser.startsWith(json, Digit)) == null) {
	    Parser.unprocess(json, processed);
	    return null;
	}
	do {
	    processed.append(exp);
	} while ((exp = Parser.startsWith(json, Digit)) != null);
	return processed.toString();
    }

    /**
     * Returns JSON data that represents a JSON object.
     * 
     * @param data
     *            the JSON object that should be returned as JSON data
     * @return JSON data that represents a JSON object
     */
    private String toJSON(Data data) {
	// TODO Avoid hard-coded terminals
	boolean first = true;
	StringBuilder processed = new StringBuilder();
	processed.append("{");
	for (String member : data.keySet()) {
	    if (first) {
		first = false;
	    } else {
		processed.append(",");
	    }
	    processed.append(toJSON(member));
	    processed.append(":");
	    processed.append(toJSON(data.get(member)));
	}
	processed.append("}");
	return processed.toString();
    }

    /**
     * Returns JSON data that represents a JSON array.
     * 
     * @param values
     *            the JSON array that should be returned as JSON data
     * @return JSON data that represents a JSON array
     */
    private String toJSON(List<Object> values) {
	boolean first = true;
	StringBuilder processed = new StringBuilder();
	processed.append("[");
	for (Object value : values) {
	    if (first) {
		first = false;
	    } else {
		processed.append(",");
	    }
	    processed.append(toJSON(value));
	}
	processed.append("]");
	return processed.toString();
    }

    /**
     * Returns JSON data that corresponds to the given object (e.g. JSON object,
     * JSON array, true, false, etc.).
     * 
     * @param o
     *            the object that should be returned as JSON data
     * @return JSON data that corresponds to the given object
     */
    @SuppressWarnings("unchecked")
    private String toJSON(Object o) {
	if (o == null) {
	    return null;
	} else if (o instanceof String) {
	    // TODO Escape characters
	    return String.format("%s%s%s", '"', o.toString(), '"');
	} else if (o instanceof Data) {
	    return toJSON((Data) o);
	} else if (o instanceof List) {
	    return toJSON((List<Object>) o);
	} else {
	    return o.toString();
	}
    }

}
