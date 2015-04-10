package de.claas.mosis.io.generator;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.ProcessorAdapter;

import java.util.List;

/**
 * The class {@link de.claas.mosis.io.generator.Time}. It is intended to
 * generate time values. It returns the the difference, measured in
 * milliseconds, between the current time and midnight, January 1, 1970 UTC.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        dataSource = true,
        category = Category.InputOutput,
        author = {"Claas Ahlrichs"},
        description = "This implementation acts as a data source. By default it outputs the current time in milliseconds since January first 1970. The implementation will do so regardless of the input data, if any. The module can also be configured to return relative time values. In this mode, the first call to this module will output zero and all succeeding calls will output the time (in milliseconds) that elapsed since the first call.",
        purpose = "To provide access to time.")
public class Time extends ProcessorAdapter<Long, Long> {

    @Parameter("Whether the time should be in absolute numbers or relative to the first call.")
    public static final String MODE = "mode";
    public static final String MODE_ABSOLUTE = "absolute";
    public static final String MODE_RELATIVE = "relative";
    private long reference = -1;
// TODO init. condition

    @Override
    public void process(List<Long> in, List<Long> out) {
        if(reference == -1) {
            reference = MODE_RELATIVE.equals(getParameter(MODE)) ? System.currentTimeMillis() : 0;
        }
        out.add(System.currentTimeMillis() - reference);
    }

}
