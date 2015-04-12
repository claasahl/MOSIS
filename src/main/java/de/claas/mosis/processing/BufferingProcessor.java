package de.claas.mosis.processing;

import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.ProcessorAdapter;

import java.util.List;
import java.util.Vector;

/**
 * The class {@link de.claas.mosis.processing.BufferingProcessor}. It is a
 * partial implementation of the {@link de.claas.mosis.model.Processor}
 * interface which provides a sliding window. It is intended to buffer input
 * values (in an ordered fashion) and provide access to it.
 *
 * @param <I> type of incoming data. See {@link de.claas.mosis.model.Processor}
 *            for details.
 * @param <O> type of outgoing data. See {@link de.claas.mosis.model.Processor}
 *            for details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        purpose = "It is intended to buffer incoming data (in an ordered fashion) and provide access to it.",
        description = "This is a partial implementation of a module which provides a sliding window. It is intended to buffer incoming data (in an ordered fashion) and provide access to it. The size if the sliding window can be configured to any positive integer (including zero). Setting the length to zero will disable buffering and all input data are directly accessible.",
        author = "Claas Ahlrichs",
        noOutputData = "Depends on actual implementation (this is a partial implementation).")
public abstract class BufferingProcessor<I, O> extends ProcessorAdapter<I, O> {

    // TODO add parameter for "step width" / "overlap"
    @Parameter("Number of samples being buffered.")
    public static final String WINDOW_SIZE = "size of window";
    private List<List<I>> _Window;

    /**
     * Initializes the class with default values.
     */
    public BufferingProcessor() {
        addCondition(WINDOW_SIZE, new Condition.IsGreaterOrEqual(0d));
        addCondition(WINDOW_SIZE, new Condition.IsInteger());
        setParameter(WINDOW_SIZE, 0);
    }

    @Override
    public void setUp() {
        super.setUp();
        int size = getParameterAsInteger(WINDOW_SIZE);
        while (getBuffer().size() > size) {
            getBuffer().remove(0);
        }
    }

    @Override
    public void dismantle() {
        super.dismantle();
        getBuffer().clear();
    }

    /**
     * Returns the buffer.
     *
     * @return the buffer
     */
    protected List<List<I>> getBuffer() {
        if (_Window == null) {
            _Window = new Vector<>();
        }
        return _Window;
    }

    /**
     * Returns <code>true</code>, if the buffer has reached its limit.
     * Otherwise, <code>false</code> is returned.
     *
     * @return <code>true</code>, if the buffer has reached its limit
     */
    protected boolean isBufferFull() {
        return getBuffer().size() >= getParameterAsInteger(WINDOW_SIZE);
    }

    /**
     * Returns the removed data from the buffer (if any) and appends new data.
     * If the buffer has reached its limit then the very first element is
     * removed and returned by this method. If no data needs to be removed (that
     * is to say, if the buffer is not full) then <code>null</code> is
     * returned.
     *
     * @param data the new data
     * @return the removed data from the buffer (if any)
     */
    protected List<I> appendAndRemove(List<I> data) {
        if (isBufferFull() && getBuffer().isEmpty()) {
            return data;
        } else {
            List<I> removed = null;
            if (isBufferFull()) {
                removed = getBuffer().remove(0);
            }
            getBuffer().add(new Vector<>(data));
            return removed;
        }
    }
}
