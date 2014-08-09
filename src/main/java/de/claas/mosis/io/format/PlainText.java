package de.claas.mosis.io.format;

import java.util.List;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.io.FileImpl;
import de.claas.mosis.io.StreamHandler;
import de.claas.mosis.io.StreamHandlerImpl;
import de.claas.mosis.io.UrlImpl;
import de.claas.mosis.model.Condition;

/**
 * The class {@link PlainText}. It is intended to read and write {@link String}
 * objects. This {@link StreamHandler} allows to read and write plain text (i.e.
 * {@link String}) from any of the {@link StreamHandlerImpl} implementations
 * (e.g. {@link FileImpl} or {@link UrlImpl}).
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
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
		if (shouldFoward()) {
		    out.addAll(in);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
