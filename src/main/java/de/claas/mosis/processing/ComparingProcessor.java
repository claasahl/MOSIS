package de.claas.mosis.processing;

import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.ProcessorAdapter;
import de.claas.mosis.processing.util.Delay;

/**
 * The class {@link de.claas.mosis.processing.ComparingProcessor}. It is a
 * partial implementation of the {@link de.claas.mosis.model.Processor}
 * interface which is buffers a single sample. The idea is to provide the
 * ability to compare an incoming sample to the previous sample without having
 * to use {@link de.claas.mosis.processing.BufferingProcessor}.
 *
 * @param <I> type of incoming data. See {@link de.claas.mosis.model.Processor}
 *            for details.
 * @param <O> type of outgoing data. See {@link de.claas.mosis.model.Processor}
 *            for details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        purpose = "It provides the ability to compare incoming data samples to previous ones.",
        description = "This is a partial implementation of the main interface (i.e. Processor) which buffers a single sample. The idea is to provide the ability to compare an incoming sample to the previous sample without having to use a BufferingProcessor module. By default it compares input data from the first port (i.e. if multiple modules output their data into this implementation then data from the first one is used only). However, this number can be configured to any inbound module.",
        author = "Claas Ahlrichs",
        noOutputData = "Refer to concrete implementations.")
public abstract class ComparingProcessor<I, O> extends ProcessorAdapter<I, O> {

    // TODO Remove port
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
