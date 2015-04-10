package de.claas.mosis.io.format;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Data;
import de.claas.mosis.util.Parser;

import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

/**
 * The class {@link de.claas.mosis.io.format.CommaSeparatedValues}. It is
 * intended to read and write CSV data (comma separated value). This {@link
 * de.claas.mosis.io.StreamHandler} allows to read and write CSV data from any
 * of the {@link de.claas.mosis.io.StreamHandlerImpl} implementations (e.g.
 * {@link de.claas.mosis.io.FileImpl} or {@link de.claas.mosis.io.UrlImpl}).
 * <p/>
 * This implementation is based on the definitions given in RFC 4180, which is
 * publicly available at http://www.ietf.org/rfc/rfc4180.txt. The therein
 * contained ABNF grammar was slightly modified and the final result is listed
 * below:
 * <p/>
 * <ol> <li>csv-text = [header NEWLINE] record *(NEWLINE record) [NEWLINE]</li>
 * <li>header = name *(SEPARATOR name)</li> <li>record = field *(SEPARATOR
 * field)</li> <li>name = field</li> <li>field = escaped / unescaped</li>
 * <li>escaped = DQUOTE *(TEXTDATA / SEPARATOR / CR / LF / TWODQUOTES)
 * DQUOTE</li> <li>unescaped = *TEXTDATA</li> </ol> <ol> <li>SEPARATOR = %x2C;
 * this terminal is configurable</li> <li>CR = %x0D ;as per section 6.1 of RFC
 * 2234 [2]</li> <li>DQUOTE = %x22 ;as per section 6.1 of RFC 2234 [2]</li>
 * <li>LF = %x0A ;as per section 6.1 of RFC 2234 [2]</li> <li>NEWLINE = (CR LF)
 * / (LF CR) / CR / LF</li> <li>TEXTDATA = %x20-21 / %x23-2B / %x2D-7E</li>
 * </ol>
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        category = Category.DataFormat,
        author = {"Claas Ahlrichs"},
        description = "This is a realization of an AbstractTextFormat. It allows the framework to read and write data samples in the form of comma separated values (CSV). In addition to the configuration options from AbstractTextFormat (i.e. buffer size, line separator and character set) and StreamHandler (i.e. whether data is read from files, websites, etc.), this module can also be configured to include a header line. The separator between fields can be configured as well.",
        purpose = "To allow storage as CSV data and retrieval of CSV data.")
public class CommaSeparatedValues extends AbstractTextFormat<Data> {

    @Parameter("Separating character between two adjacent data fields.")
    public static final String SEPARATOR = "separator";
    @Parameter("Header used during input / output operations. Can be used selectively write data.")
//TODO Can be used selectively read and write data. ???
    public static final String HEADER = "header";
    @Parameter("Whether a header line is expected during input / output operations.")
    public static final String HAS_HEADER = "has header";
    private final Pattern CR;
    private final Pattern DQuote;
    private final Pattern TwoDQuotes;
    private final Pattern LF;
    private final Pattern NEWLINE;
    private final StringBuilder _CSV;
    private final List<String> _Attributes;
    private Pattern Textdata;
    private Pattern Separator;
    private boolean _HeaderRead;
    private boolean _HeaderWritten;

    /**
     * Initializes the class with default values.
     */
    public CommaSeparatedValues() {
        CR = Pattern.compile(" \\x0D");
        DQuote = Pattern.compile("[\\x22\\x27]");
        TwoDQuotes = Pattern.compile("\\x22\\x22");
        LF = Pattern.compile("\\x0A");
        NEWLINE = Pattern.compile("(\\x0D\\x0A)|(\\c0A\\x0D)|\\x0D|\\x0A");
        Textdata = Pattern.compile("[^\\x00-\\x1F\\x22\\x2C]");
        _CSV = new StringBuilder();
        _Attributes = new Vector<>();
        _HeaderWritten = false;
        _HeaderRead = false;

        addCondition(SEPARATOR, new Condition.IsNotNull());
        addCondition(SEPARATOR, new Condition.RegularExpression(null, "."));
        setParameter(SEPARATOR, ",");
        addCondition(HEADER, new Condition.IsNotNull());
        setParameter(HEADER, "");
        addCondition(HAS_HEADER, new Condition.IsBoolean());
        setParameter(HAS_HEADER, "true");
    }

    @Override
    public void setUp() {
        super.setUp();
        // TODO Avoid hard coded patterns
        Separator = Pattern.compile(Pattern.quote(getParameter(SEPARATOR)));
        int sep = getParameter(SEPARATOR).charAt(0);
        Textdata = Pattern.compile(String.format("[^\\x00-\\x1F\\x22\\x%02X]", sep));
    }

    @Override
    public void dismantle() {
        super.dismantle();
        Separator = null;
    }

    @Override
    public void process(List<Data> in, List<Data> out) {
        try {
            if (isReadOnly(in)) {
                // Keep buffer updated
                String line = readLine(true);
                if (line != null) {
                    _CSV.append(line);
                }
                if (!_HeaderRead && getParameterAsBoolean(HAS_HEADER)) {
                    line = readLine(true);
                    if (line != null) {
                        _CSV.append(line);
                    }
                }

                // Parse CSV
                if (_CSV.length() > 0) {
                    List<Data> data = new Vector<>();
                    String processed = csvText(new StringBuilder(_CSV),
                            !_HeaderRead && getParameterAsBoolean(HAS_HEADER),
                            _Attributes, data);
                    if (processed != null) {
                        _HeaderRead = true;
                        _CSV.replace(0, processed.length(), "");
                        out.addAll(data);

                        // Update HEADER
                        StringBuilder tmp = new StringBuilder();
                        boolean first = true;
                        for (String key : _Attributes) {
                            if (first) {
                                first = false;
                            } else {
                                tmp.append(getParameter(SEPARATOR));
                            }
                            tmp.append(key);
                        }
                        setParameter(HEADER, tmp.toString());
                    }
                }
            } else {
                if (!_HeaderWritten && getParameterAsBoolean(HAS_HEADER)) {
                    if (getParameter(HEADER).isEmpty()) {
                        StringBuilder processed = new StringBuilder();
                        boolean first = true;
                        for (String key : in.get(0).keySet()) {
                            if (first) {
                                first = false;
                            } else {
                                processed.append(getParameter(SEPARATOR));
                            }
                            processed.append(key);
                            _Attributes.add(key);
                        }
                        setParameter(HEADER, processed.toString());
                    }
                    writeLine(getParameter(HEADER), true);
                    _HeaderWritten = true;
                }
                if (_Attributes.isEmpty() && !getParameter(HEADER).isEmpty()) {
                    // TODO Replace by CSV parser (i.e. not just split)
                    Collections.addAll(_Attributes, getParameter(HEADER).split(getParameter(SEPARATOR)));
                }
                for (Data datum : in) {
                    writeLine(toCSV(datum, _Attributes), true);
                }
                if (shouldForward()) {
                    out.addAll(in);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the name of the corresponding attribute. If no corresponding
     * attribute is available, then a name is generated.
     *
     * @param attributes the available attribute names
     * @param index      index of attribute whose name is returned.
     * @return the name of the corresponding attribute.
     */
    private String attribute(List<String> attributes, int index) {
        if (index >= attributes.size()) {
            attributes.add(index, String.format("attribute-%d", index));
        }
        return attributes.get(index);
    }

    /**
     * Returns the CSV data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid CSV data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * <p/>
     * <b>csv-text = [header NEWLINE] record *(NEWLINE record) [NEWLINE]</b>
     *
     * @param csv        the CSV data that is being processed
     * @param header     true, if the first line of CSV data should be
     *                   interpreted as column headers. false, if CSV data does
     *                   not have such column headers
     * @param attributes if data was successfully processed, then the list will
     *                   be updated to represent the column headers (if no such
     *                   column headers exist, then they are automatically
     *                   generated)
     * @param data       if data was successfully processed, then the list will
     *                   be updated to represent the processed CSV data
     * @return the CSV data that was processed by this call
     */
    private String csvText(StringBuilder csv, boolean header,
                           List<String> attributes, List<Data> data) {
        StringBuilder processed = new StringBuilder();
        StringBuilder line = new StringBuilder();
        String lineText;
        Data record;
        String csvText;

        // [header CRLF]
        if (header && (csvText = header(csv, attributes)) != null) {
            processed.append(csvText);
            if ((csvText = Parser.startsWith(csv, NEWLINE)) == null) {
                Parser.unprocess(csv, processed);
                return null;
            }
            processed.append(csvText);
        }

        // record *(CRLF record)
        record = new Data();
        if (csv.length() == 0
                || (csvText = record(csv, attributes, record)) == null) {
            Parser.unprocess(csv, processed);
            return null;
        }
        processed.append(csvText);
        data.add(record);

        while ((lineText = Parser.startsWith(csv, NEWLINE)) != null) {
            line.append(lineText);
            record = new Data();
            if (csv.length() == 0
                    || (lineText = record(csv, attributes, record)) == null) {
                Parser.unprocess(csv, line);
                break;
            }
            line.append(lineText);
            processed.append(line);
            line.replace(0, line.length(), "");
            data.add(record);
        }

        // [CRLF]
        if ((csvText = Parser.startsWith(csv, NEWLINE)) != null) {
            processed.append(csvText);
        }
        return processed.toString();
    }

    /**
     * Returns the CSV data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid CSV data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * <p/>
     * <b>header = name *(SEPARATOR name)</b>
     *
     * @param csv        the CSV data that is being processed
     * @param attributes if data was successfully processed, then the list will
     *                   be updated to represent the column header
     * @return the CSV data that was processed by this call
     */
    private String header(StringBuilder csv, List<String> attributes) {
        StringBuilder processed = new StringBuilder();
        StringBuffer text;
        String header;

        // name
        text = new StringBuffer();
        if ((header = name(csv, text)) == null) {
            return null;
        }
        processed.append(header);
        attributes.add(text.toString());

        // *(SEPARATOR name)
        while ((header = Parser.startsWith(csv, Separator)) != null) {
            processed.append(header);
            text = new StringBuffer();
            if ((header = name(csv, text)) == null) {
                Parser.unprocess(csv, processed);
                return null;
            }
            processed.append(header);
            attributes.add(text.toString());
        }
        return processed.toString();
    }

    /**
     * Returns the CSV data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid CSV data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * <p/>
     * <b>record = field *(SEPARATOR field)</b>
     *
     * @param csv        the CSV data that is being processed
     * @param attributes the column headers. If there are more columns then
     *                   column headers, then the missing headers are
     *                   automatically generated and appended to this list.
     * @param data       if data was successfully processed, then the {@link
     *                   de.claas.mosis.model.Data} object will be updated to
     *                   represent the processed CSV data / record
     * @return the CSV data that was processed by this call
     */
    private String record(StringBuilder csv, List<String> attributes, Data data) {
        StringBuilder processed = new StringBuilder();
        int index = 0;
        StringBuffer text;
        String record;

        // field
        text = new StringBuffer();
        if ((record = field(csv, text)) == null) {
            return null;
        }
        processed.append(record);
        data.put(attribute(attributes, index++), text.toString());

        // *(SEPARATOR field)
        while ((record = Parser.startsWith(csv, Separator)) != null) {
            processed.append(record);
            text = new StringBuffer();
            if ((record = field(csv, text)) == null) {
                Parser.unprocess(csv, processed);
                return null;
            }
            processed.append(record);
            data.put(attribute(attributes, index++), text.toString());
        }
        return processed.toString();
    }

    /**
     * Returns the CSV data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid CSV data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * <p/>
     * <b>name = field</b>
     *
     * @param csv  the CSV data that is being processed
     * @param text if data was successfully processed, then the {@link
     *             java.lang.StringBuilder} instance will be updated with the
     *             text that is represented by the above rule
     * @return the CSV data that was processed by this call
     */
    private String name(StringBuilder csv, StringBuffer text) {
        return field(csv, text);
    }

    /**
     * Returns the CSV data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid CSV data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * <p/>
     * <b>field = escaped / unescaped</b>
     *
     * @param csv  the CSV data that is being processed
     * @param text if data was successfully processed, then the {@link
     *             StringBuilder} instance will be updated with the text that is
     *             represented by the above rule
     * @return the CSV data that was processed by this call
     */
    private String field(StringBuilder csv, StringBuffer text) {
        String field;

        // escaped / unescaped
        if ((field = escaped(csv, text)) != null) {
            return field;
        } else if ((field = unescaped(csv, text)) != null) {
            return field;
        }
        return null;
    }

    /**
     * Returns the CSV data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid CSV data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * <p/>
     * <b>escaped = DQUOTE *(TEXTDATA / SEPARATOR / CR / LF / 2DQUOTE)
     * DQUOTE</b>
     *
     * @param csv  the CSV data that is being processed
     * @param text if data was successfully processed, then the {@link
     *             java.lang.StringBuilder} instance will be updated with the
     *             text in between the quotes
     * @return the CSV data that was processed by this call
     */
    private String escaped(StringBuilder csv, StringBuffer text) {
        StringBuilder processed = new StringBuilder();
        String escaped;
        int length;

        // DQUOTE
        if ((escaped = Parser.startsWith(csv, DQuote)) == null) {
            return null;
        }
        processed.append(escaped);

        // *(TEXTDATA / SEPARATOR / CR / LF / 2DQUOTE)
        do {
            length = processed.length();
            if ((escaped = Parser.startsWith(csv, Textdata)) != null) {
                processed.append(escaped);
                text.append(escaped);
            } else if ((escaped = Parser.startsWith(csv, Separator)) != null) {
                processed.append(escaped);
                text.append(escaped);
            } else if ((escaped = Parser.startsWith(csv, CR)) != null) {
                processed.append(escaped);
                text.append(escaped);
            } else if ((escaped = Parser.startsWith(csv, LF)) != null) {
                processed.append(escaped);
                text.append(escaped);
            } else if ((escaped = Parser.startsWith(csv, TwoDQuotes)) != null) {
                processed.append(escaped);
                text.append(escaped.charAt(0));
            }
        } while (length != processed.length());

        // DQUOTE
        if ((escaped = Parser.startsWith(csv, DQuote)) == null) {
            Parser.unprocess(csv, processed);
            return null;
        }
        processed.append(escaped);
        return processed.toString();
    }

    /**
     * Returns the CSV data that was processed by this call. If no data was
     * processed (e.g. incomplete or invalid CSV data), then <code>null</code>
     * is returned. The data is processed as defined in rule:
     * <p/>
     * <b>unescaped = *TEXTDATA</b>
     *
     * @param csv  the CSV data that is being processed
     * @param text if data was successfully processed, then the {@link
     *             java.lang.StringBuilder} instance will be updated with the
     *             text that is represented by the above rule
     * @return the CSV data that was processed by this call
     */
    private String unescaped(StringBuilder csv, StringBuffer text) {
        StringBuilder processed = new StringBuilder();
        String unescaped;

        // *TEXTDATA
        while ((unescaped = Parser.startsWith(csv, Textdata)) != null) {
            processed.append(unescaped);
            text.append(unescaped);
        }
        return processed.toString();
    }

    /**
     * Returns CSV data that represents a CSV object.
     *
     * @param data       the CSV object that should be returned as CSV data
     * @param attributes the attributes that should be in the CSV data
     * @return CSV data that represents a CSV object
     */
    private String toCSV(Data data, List<String> attributes) {
        // TODO Escape characters
        // TODO return header
        boolean first = true;
        StringBuilder processed = new StringBuilder();
        for (String attribute : attributes) {
            if (first) {
                first = false;
                processed.append(data.getAsString(attribute));
            } else {
                processed.append(getParameter(SEPARATOR));
                processed.append(data.getAsString(attribute));
            }

        }
        return processed.toString();
    }
}
