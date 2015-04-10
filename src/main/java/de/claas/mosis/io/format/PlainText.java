package de.claas.mosis.io.format;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;

import java.util.List;

/**
 * The class {@link de.claas.mosis.io.format.PlainText}. It is intended to read
 * and write {@link java.lang.String} objects. This {@link
 * de.claas.mosis.io.StreamHandler} allows to read and write plain text (i.e.
 * {@link java.lang.String}) from any of the {@link de.claas.mosis.io.StreamHandlerImpl}
 * implementations (e.g. {@link de.claas.mosis.io.FileImpl} or {@link
 * de.claas.mosis.io.UrlImpl}).
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        category = Category.DataFormat,
        author = {"Claas Ahlrichs"},
        description = "This is a realization of an AbstractTextFormat. It allows the framework to read and write plain text. The module can be configured to enforce a line separator after writing a data sample. It can also be configured to prepend a prefix before any writing operation.",
        purpose = "To allow storage in (plain) text and retrieval of (plain) text.")
public class PlainText extends AbstractTextFormat<String> {

    @Parameter("Prefix that is prepended during output operations.")
    public static final String PREFIX = "prefix";
    @Parameter("Whether 'new line' character(s) should be appended during output operations.")
    public static final String APPEND_NEWLINE = "append newline";

    /**
     * Initializes the class with default values.
     */
    public PlainText() {
        addCondition(PREFIX, new Condition.IsNotNull());
        setParameter(PREFIX, "");
        addCondition(APPEND_NEWLINE, new Condition.IsBoolean());
        setParameter(APPEND_NEWLINE, "true");
    }

    @Override
    public void process(List<String> in, List<String> out) {
        try {
            if (isReadOnly(in)) {
                String line = readLine(false);
                if (line != null) {
                    out.add(line);
                }
            } else {
                for (String line : in) {
                    writeLine(getParameter(PREFIX), false);
                    writeLine(line, getParameterAsBoolean(APPEND_NEWLINE));
                }
                if (shouldForward()) {
                    out.addAll(in);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
