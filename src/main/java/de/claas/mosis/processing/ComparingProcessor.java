package de.claas.mosis.processing;

import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Processor;
import de.claas.mosis.model.ProcessorAdapter;
import de.claas.mosis.processing.util.Delay;

/**
 * The class {@link ComparingProcessor}. It is a partial implementation of the
 * {@link Processor} interface which is buffers a single sample. The idea is to
 * provide the ability to compare an incoming sample to the previous sample
 * without having to use {@link BufferingProcessor}.
 *
 * @param <I> type of incoming data. See {@link Processor} for details.
 * @param <O> type of outgoing data. See {@link Processor} for details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        purpose = "It provides the ability to compare incoming data samples to previous ones.",
        description = "It is a partial implementation of the main interface (i.e. Processor) which buffers a single sample. The idea is to provide the ability to compare an incoming sample to the previous sample without having to use a BufferingProcessor module.",
        author = "Claas Ahlrichs",
        noOutputData = "Refer to concrete implementations.")
public abstract class ComparingProcessor<I, O> extends ProcessorAdapter<I, O> {

    @Parameter("N-th input processor / module which values are compare (zero-indexed). E.g. when set to three then only the input values comping from the fourth processor / module (parent) are utilized and compared.")
    public static final String PORT_TO_USE = Delay.PORT_TO_USE;
    private I _LastValue;

    /**
     * Initializes the class with default values.
     */
    public ComparingProcessor() {
        addCondition(PORT_TO_USE, new Condition.IsGreaterOrEqual(0d));
        addCondition(PORT_TO_USE, new Condition.IsInteger());
        setParameter(PORT_TO_USE, 0);
    }

    /**
     * Returns the previous sample and replaces it with the new sample.
     *
     * @param data the new sample
     * @return the previous sample
     */
    protected I replace(I data) {
        I tmp = _LastValue;
        _LastValue = data;
        return tmp;
    }

}
