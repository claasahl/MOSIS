package de.claas.mosis.processing.util;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.processing.BufferingProcessor;

import java.util.List;

/**
 * The class {@link de.claas.mosis.processing.util.Delay}. It is intended to
 * delay and forward incoming data. This {@link de.claas.mosis.processing.BufferingProcessor}
 * implementation buffers input values for a predefined time and releases them
 * afterwards.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        canHandelMissingData = true,
        category = Category.Other,
        author = {"Claas Ahlrichs"},
        description = "This is a realization of the BufferingProcessor which allows the delayed forwarding of input data. The delay can be configured to any positive integer (including zero). Setting the delay to zero forwards input data without a delay. This implementation can be used (similarly to Forward) to avoid misalignments when multiple paths of varying lengths within a graph of modules need to be merged.",
        purpose = "To delay and forward input values.")
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
