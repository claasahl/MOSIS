package de.claas.mosis.io.format;

import de.claas.mosis.model.Data;
import de.claas.mosis.util.Parser;

import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * The class {@link AttributeRelationFileFormat}.
 * 
 * <ol>
 * <li>arff-text = header data;</li>
 * <li>header = '@relation' string adecls;</li>
 * <li>adecls = adecl *(adecl);</li>
 * <li>adecl = '@attribute' string datatype;</li>
 * <li>datatype = 'numeric'/'integer'/'real'/'string'/'relational' adecls '@end'
 * string/date/'{' values '}';</li>
 * <li>date = 'date' [string];</li>
 * <li>data = '@data' ( +(pairs)/+(values) );</li>
 * <li>pairs = '{' pair *(',' pair) '}';</li>
 * <li>pair = integer value;</li>
 * <li>values = value *(',' value);</li>
 * <li>value = '?'/float/integer/string;</li>
 * <li>string = escaped/unescaped/keyword;</li>
 * <li>keyword = 'numeric'/'integer'/'real'/'string'/'relational'/'date';</li>
 * </ol>
 * <ol>
 * <li>integer = ['+'|'-'] +('0'..'9');</li>
 * <li>float = ['+'|'-'] [ *('0'..'9') '.'] +('0'..'9') [exponent];</li>
 * <li>exponent = ('e'/'E') ['+'/'-'] +('0'..'9');</li>
 * <li>escaped = ('"' *(~('"')) '"')/('\'' *(~('\'')) '\'');</li>
 * <li>unescaped = +( ~(' '/'\r'/'\t'/'\u000C'/'\n'/','/'{'/'}'/'\''/'\"') /
 * '\\' (','/'{'/'}'/'\''/'\"') );</li>
 * <li>LINE_COMMENT = '%' *~('\n'/'\r') ?'\r' '\n' {$channel=HIDDEN;};</li>
 * <li>WS = (' '/'\r'/'\t'/'\u000C'/'\n') {$channel=HIDDEN;};</li>
 * </ol>
 * 
 * @author Claas Ahlrichs (c.ahlrichs@neusta.de)
 * 
 *         TODO Implement
 * 
 */
public class AttributeRelationFileFormat extends AbstractTextFormat<Data> {

    private final Pattern Relation;
    private final Pattern Attribute;
    private final Pattern Numeric;
    private final Pattern Integer;
    private final Pattern Real;
    private final Pattern String;
    private final Pattern Relational;
    private final Pattern CurlyBracketLeft;
    private final Pattern CurlyBracketRight;
    private final Pattern Date;
    private final Pattern Data;
    private final Pattern Comma;
    private final Pattern End;
    private final Pattern Questionmark;
    private final Pattern Plus;
    private final Pattern Minus;
    private final Pattern Digit;
    private final Pattern Dot;
    private final Pattern Exp;
    private final Pattern Escaped;
    private final Pattern Unescaped;
    private final StringBuilder _ARFF;

    /**
     * Initializes the class with default values.
     */
    public AttributeRelationFileFormat() {
	Relation = Pattern.compile("@(relation|Relation|RELATION)");
	Attribute = Pattern.compile("@(attribute|Attribute|ATTRIBUTE)");
	Numeric = Pattern.compile("numeric|Numeric|NUMERIC");
	Integer = Pattern.compile("integer|Integer|INTEGER");
	Real = Pattern.compile("real|Real|REAL");
	String = Pattern.compile("string|String|STRING");
	Relational = Pattern.compile("relational|Relational|RELATIONAL");
	CurlyBracketLeft = Pattern.compile("\\{");
	CurlyBracketRight = Pattern.compile("\\}");
	Date = Pattern.compile("date|Date|DATE");
	Data = Pattern.compile("@(data|Data|DATA)");
	Comma = Pattern.compile(",");
	End = Pattern.compile("@(end|End|END)");
	Questionmark = Pattern.compile("\\?");
	Plus = Pattern.compile("-");
	Minus = Pattern.compile("\\+");
	Digit = Pattern.compile("[0-9]");
	Dot = Pattern.compile("\\.");
	Exp = Pattern.compile("[eE]");
	Escaped = Pattern.compile("[^\\s,\\{\\}'\"]|\\[,\\{\\}'\"]");
	Unescaped = Pattern.compile("\"[^\"]*\"|'[^']*'");
	_ARFF = new StringBuilder();
    }

    @Override
    public void process(List<Data> in, List<Data> out) {
	try {
	    if (isReadOnly(in)) {
		// Keep buffer updated
		String line = readLine(true);
		if (line != null) {
		    _ARFF.append(line);
		}

		// Parse ARFF
		if (_ARFF.length() > 0) {
		    List<Data> data = new Vector<>();
		    String processed = arffText(new StringBuilder(_ARFF), data);
		    if (processed != null) {
			_ARFF.replace(0, processed.length(), "");
			out.addAll(data);
		    }
		}
	    } else {
		for (Data datum : in) {
		    writeLine(toARFF(datum), true);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Returns the ARFF data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid ARFF data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * 
     * <b>arff-text = header data</b>
     * 
     * @param arff
     *            the ARFF data that is being processed
     * @param data
     *            if data was successfully processed, then the list will be
     *            updated to represent the processed ARFF data
     * @return the ARFF data that was processed by this call
     */
    private String arffText(StringBuilder arff, List<Data> data) {
	StringBuilder processed = new StringBuilder();
	String arffText;

	// header
	if ((arffText = header(arff)) == null) {
	    return null;
	}
	processed.append(arffText);

	// data
	if ((arffText = data(arff)) == null) {
	    Parser.unprocess(arff, processed);
	    return null;
	}
	processed.append(arffText);
	return processed.toString();
    }

    // header = '@relation' string adecls
    private String header(StringBuilder arff) {
	StringBuilder processed = new StringBuilder();
	String header;

	// '@relation'
	if ((header = Parser.startsWith(arff, Relation)) == null) {
	    return null;
	}
	processed.append(header);

	// string
	if ((header = string(arff)) == null) {
	    Parser.unprocess(arff, processed);
	    return null;
	}
	processed.append(header);

	// adecls
	if ((header = adecls(arff)) == null) {
	    Parser.unprocess(arff, processed);
	    return null;
	}
	processed.append(header);
	return processed.toString();
    }

    // adecls = adecl *(adecl)
    private String adecls(StringBuilder arff) {
	StringBuilder processed = new StringBuilder();
	String adecls;

	// adecl
	if ((adecls = adecl(arff)) == null) {
	    return null;
	}
	processed.append(adecls);

	// *(adecl)
	while ((adecls = adecl(arff)) != null) {
	    processed.append(adecls);
	}
	return processed.toString();
    }

    // adecl = '@attribute' string datatype
    private String adecl(StringBuilder arff) {
	StringBuilder processed = new StringBuilder();
	String adecl;

	// '@attribute'
	if ((adecl = Parser.startsWith(arff, Attribute)) == null) {
	    return null;
	}
	processed.append(adecl);

	// string
	if ((adecl = string(arff)) == null) {
	    Parser.unprocess(arff, processed);
	    return null;
	}
	processed.append(adecl);

	// datatype
	if ((adecl = datatype(arff)) == null) {
	    Parser.unprocess(arff, processed);
	    return null;
	}
	processed.append(adecl);
	return processed.toString();
    }

    // datatype = 'numeric'/'integer'/'real'/'string'/'relational' adecls
    // '@end' string/date/'{' values '}'
    private String datatype(StringBuilder arff) {
	StringBuilder processed = new StringBuilder();
	String datatype;

	// 'numeric'
	if ((datatype = Parser.startsWith(arff, Numeric)) != null) {
	    processed.append(datatype);
	    return processed.toString();
	}

	// 'integer'
	if ((datatype = Parser.startsWith(arff, Integer)) != null) {
	    processed.append(datatype);
	    return processed.toString();
	}

	// 'real'
	if ((datatype = Parser.startsWith(arff, Real)) != null) {
	    processed.append(datatype);
	    return processed.toString();
	}

	// 'string'
	if ((datatype = Parser.startsWith(arff, String)) != null) {
	    processed.append(datatype);
	    return processed.toString();
	}

	// 'relational'
	if ((datatype = Parser.startsWith(arff, Relational)) != null) {
	    processed.append(datatype);

	    // adecls
	    if ((datatype = adecls(arff)) == null) {
		Parser.unprocess(arff, processed);
		return null;
	    }
	    processed.append(datatype);

	    // '@end'
	    if ((datatype = Parser.startsWith(arff, End)) == null) {
		Parser.unprocess(arff, processed);
		return null;
	    }
	    processed.append(datatype);

	    // string
	    if ((datatype = string(arff)) == null) {
		Parser.unprocess(arff, processed);
		return null;
	    }
	    processed.append(datatype);
	    return processed.toString();
	}

	// date
	if ((datatype = date(arff)) != null) {
	    processed.append(datatype);
	    return processed.toString();
	}

	// '{'
	if ((datatype = Parser.startsWith(arff, CurlyBracketLeft)) != null) {
	    processed.append(datatype);

	    // values
	    if ((datatype = values(arff)) == null) {
		Parser.unprocess(arff, processed);
		return null;
	    }
	    processed.append(datatype);

	    // '}'
	    if ((datatype = Parser.startsWith(arff, CurlyBracketRight)) == null) {
		Parser.unprocess(arff, processed);
		return null;
	    }
	    processed.append(datatype);
	    return processed.toString();
	}
	return null;
    }

    // date = 'date' [string]
    private String date(StringBuilder arff) {
	StringBuilder processed = new StringBuilder();
	String date;

	// 'date'
	if ((date = Parser.startsWith(arff, Date)) == null) {
	    return null;
	}
	processed.append(date);

	// [string]
	if ((date = string(arff)) != null) {
	    processed.append(date);
	}
	return processed.toString();
    }

    // data = '@data' ( +(pairs)/+(values) )
    private String data(StringBuilder arff) {
	StringBuilder processed = new StringBuilder();
	String data;

	// '@data'
	if ((data = Parser.startsWith(arff, Data)) == null) {
	    return null;
	}
	processed.append(data);

	// +(pairs)
	if ((data = pairs(arff)) != null) {
	    processed.append(data);
	    while ((data = pairs(arff)) != null) {
		processed.append(data);
	    }
	    return processed.toString();
	}

	// +(values)
	if ((data = values(arff)) != null) {
	    processed.append(data);
	    while ((data = values(arff)) != null) {
		processed.append(data);
	    }
	    return processed.toString();
	}
	return null;
    }

    // pairs = '{' pair *(',' pair) '}'
    private String pairs(StringBuilder arff) {
	StringBuilder processed = new StringBuilder();
	String pairs;

	// '{'
	if ((pairs = Parser.startsWith(arff, CurlyBracketLeft)) == null) {
	    return null;
	}
	processed.append(pairs);

	// pair
	if ((pairs = pair(arff)) == null) {
	    Parser.unprocess(arff, processed);
	    return null;
	}
	processed.append(pairs);

	// *(',' pair)
	while ((pairs = Parser.startsWith(arff, Comma)) != null) {
	    processed.append(pairs);
	    if ((pairs = pair(arff)) == null) {
		Parser.unprocess(arff, processed);
		return null;
	    }
	    processed.append(pairs);
	}

	// '}'
	if ((pairs = Parser.startsWith(arff, CurlyBracketRight)) == null) {
	    Parser.unprocess(arff, processed);
	    return null;
	}
	return processed.toString();
    }

    // pair = integer value
    private String pair(StringBuilder arff) {
	StringBuilder processed = new StringBuilder();
	String pair;

	// integer
	if ((pair = integer(arff)) == null) {
	    return null;
	}
	processed.append(pair);

	// value
	if ((pair = value(arff)) == null) {
	    Parser.unprocess(arff, processed);
	    return null;
	}
	processed.append(pair);
	return processed.toString();
    }

    // values = value *(',' value)
    private String values(StringBuilder arff) {
	StringBuilder processed = new StringBuilder();
	String values;

	// value
	if ((values = value(arff)) == null) {
	    return null;
	}
	processed.append(values);

	// *(',' value)
	while ((values = Parser.startsWith(arff, Comma)) != null) {
	    processed.append(values);
	    if ((values = value(arff)) == null) {
		Parser.unprocess(arff, processed);
		return null;
	    }
	    processed.append(values);
	}
	return processed.toString();
    }

    // value = '?'/float/integer/string
    private String value(StringBuilder arff) {
	StringBuilder processed = new StringBuilder();
	String value;

	// '?'
	if ((value = Parser.startsWith(arff, Questionmark)) != null) {
	    processed.append(value);
	    return processed.toString();
	}

	// float
	if ((value = floatNumber(arff)) != null) {
	    processed.append(value);
	    return processed.toString();
	}

	// integer
	if ((value = integer(arff)) != null) {
	    processed.append(value);
	    return processed.toString();
	}

	// string
	if ((value = string(arff)) != null) {
	    processed.append(value);
	    return processed.toString();
	}
	return null;
    }

    // string = escaped/unescaped/keyword
    // FIXME?
    private String string(StringBuilder arff) {
	StringBuilder processed = new StringBuilder();
	String string;

	// escaped
	if ((string = Parser.startsWith(arff, Escaped)) != null) {
	    processed.append(string);
	    return processed.toString();
	}

	// unescaped
	if ((string = Parser.startsWith(arff, Unescaped)) != null) {
	    processed.append(string);
	    return processed.toString();
	}

	// keyword
	if ((string = keyword(arff)) != null) {
	    processed.append(string);
	    return processed.toString();
	}
	return null;
    }

    // keyword = 'numeric'/'integer'/'real'/'string'/'relational'/'date'
    private String keyword(StringBuilder arff) {
	StringBuilder processed = new StringBuilder();
	String keyword;

	// 'numeric'
	if ((keyword = Parser.startsWith(arff, Numeric)) != null) {
	    processed.append(keyword);
	    return processed.toString();
	}

	// 'integer'
	if ((keyword = Parser.startsWith(arff, Integer)) != null) {
	    processed.append(keyword);
	    return processed.toString();
	}

	// 'real'
	if ((keyword = Parser.startsWith(arff, Real)) != null) {
	    processed.append(keyword);
	    return processed.toString();
	}

	// 'string'
	if ((keyword = Parser.startsWith(arff, String)) != null) {
	    processed.append(keyword);
	    return processed.toString();
	}

	// 'relational'
	if ((keyword = Parser.startsWith(arff, Relational)) != null) {
	    processed.append(keyword);
	    return processed.toString();
	}

	// date
	if ((keyword = date(arff)) != null) {
	    processed.append(keyword);
	    return processed.toString();
	}
	return null;
    }

    // integer = ['+'|'-'] +('0'..'9')
    private String integer(StringBuilder arff) {
	StringBuilder processed = new StringBuilder();
	String integer;

	// ['+'|'-']
	if ((integer = Parser.startsWith(arff, Plus)) != null) {
	    processed.append(integer);
	} else if ((integer = Parser.startsWith(arff, Minus)) != null) {
	    processed.append(integer);
	}

	// +('0'..'9')
	if ((integer = Parser.startsWith(arff, Digit)) == null) {
	    Parser.unprocess(arff, processed);
	    return null;
	}
	while ((integer = Parser.startsWith(arff, Digit)) != null) {
	    processed.append(integer);
	}
	processed.append(integer);
	return processed.toString();
    }

    // float = ['+'|'-'] [ *('0'..'9') '.'] +('0'..'9') [exponent]
    private String floatNumber(StringBuilder arff) {
	StringBuilder processed = new StringBuilder();
	String floatNumber;

	// ['+'|'-']
	if ((floatNumber = Parser.startsWith(arff, Plus)) != null) {
	    processed.append(floatNumber);
	} else if ((floatNumber = Parser.startsWith(arff, Minus)) != null) {
	    processed.append(floatNumber);
	}

	// [ *('0'..'9') '.']
	StringBuilder tmp = new StringBuilder();
	while ((floatNumber = Parser.startsWith(arff, Digit)) != null) {
	    tmp.append(floatNumber);
	}
	if ((floatNumber = Parser.startsWith(arff, Dot)) == null) {
	    Parser.unprocess(arff, tmp);
	} else {
	    tmp.append(floatNumber);
	    processed.append(tmp);
	}

	// +('0'..'9')
	if ((floatNumber = Parser.startsWith(arff, Digit)) == null) {
	    Parser.unprocess(arff, processed);
	    return null;
	}
	while ((floatNumber = Parser.startsWith(arff, Digit)) != null) {
	    processed.append(floatNumber);
	}

	// [exponent]
	if ((floatNumber = exponent(arff)) != null) {
	    processed.append(floatNumber);
	}
	return processed.toString();
    }

    // exponent = ('e'/'E') ['+'/'-'] +('0'..'9')
    private String exponent(StringBuilder arff) {
	StringBuilder processed = new StringBuilder();
	String exponent;

	// ('e'/'E')
	if ((exponent = Parser.startsWith(arff, Exp)) == null) {
	    return null;
	}

	// ['+'/'-']
	if ((exponent = Parser.startsWith(arff, Plus)) != null) {
	    processed.append(exponent);
	} else if ((exponent = Parser.startsWith(arff, Minus)) != null) {
	    processed.append(exponent);
	}

	// +('0'..'9')
	if ((exponent = Parser.startsWith(arff, Digit)) == null) {
	    Parser.unprocess(arff, processed);
	    return null;
	}
	while ((exponent = Parser.startsWith(arff, Digit)) != null) {
	    processed.append(exponent);
	}
	return processed.toString();
    }

    /**
     * Returns ARFF data that represents a ARFF object.
     * 
     * @param data
     *            the ARFF object that should be returned as ARFF data
     * @return ARFF data that represents a ARFF object
     */
    private String toARFF(Data data) {
	return null;
    }

}
