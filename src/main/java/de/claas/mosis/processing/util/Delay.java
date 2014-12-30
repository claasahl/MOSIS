package de.claas.mosis.processing.util;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.processing.BufferingProcessor;

import java.util.List;

/**
 * The class {@link Delay}. It is intended to delay and forward incoming data.
 * This {@link BufferingProcessor} implementation buffers input values for a
 * predefined time and releases them afterwards.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class Delay<T> extends BufferingProcessor<T, T> {

    @Parameter("N-th input processor / module which values are delayed. Only values from the referenced processor / module are delayed and forwarded (everything else is discarded). E.g. when set to three then only the input values comping from the fourth processor / module (parent) are utilized.")
    public static final String PORT_TO_USE = "port to use";

    /**
     * Initializes the class with default values.
     */
    public Delay() {
        addCondition(PORT_TO_USE, new Condition.IsGreaterOrEqual(0d));
        addCondition(PORT_TO_USE, new Condition.IsInteger());
        setParameter(PORT_TO_USE, 0);
    }

    @Override
    public void process(List<T> in, List<T> out) {
        in = appendAndRemove(in);
        if (!hasData(in)) {
            out.add(null);
        } else {
            out.add(in.get(getParameterAsInteger(PORT_TO_USE)));
        }
    }

}
