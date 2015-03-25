package de.claas.mosis.model;

import de.claas.mosis.annotation.Documentation;

import java.util.List;

/**
 * The class {@link de.claas.mosis.model.ProcessorAdapter}. It is a partial
 * implementation of the {@link de.claas.mosis.model.Processor} interface. This
 * {@link de.claas.mosis.model.Processor} covers parameter related methods and
 * provides some convenience functions. A default implementation for
 * initialization and de-initialization methods (i.e. {@link #setUp()} and
 * {@link #dismantle()}) is also provided.
 *
 * @param <I> type of incoming data. See {@link de.claas.mosis.model.Processor}
 *            for details.
 * @param <O> type of outgoing data. See {@link de.claas.mosis.model.Processor}
 *            for details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        purpose = "This implementation covers handling of parameters.",
        description = "This is a partial implementation of the main interface (i.e. Processor). It is intended to provide a unified way for getting and setting parameters. One can use this as a basis for creating new modules.",
        author = "Claas Ahlrichs",
        noOutputData = "Refer to concrete implementations.")
public abstract class ProcessorAdapter<I, O> extends ConfigurableAdapter
        implements Processor<I, O> {

    private boolean isSetUp;

    /**
     * Returns <code>true</code>, if the given data satisfies the condition
     * <code>data != null && !data.isEmpty()</code>. Otherwise,
     * <code>false</code> is returned. This is a convenience method that is
     * meant to be used inside {@link de.claas.mosis.model.Processor#process(java.util.List,
     * java.util.List)} to see whether any data has been passed to it or not.
     *
     * @param data the data passed to {@link de.claas.mosis.model.Processor#process(java.util.List,
     *             java.util.List)}
     * @return <code>true</code>, if the given data satisfies the condition
     * <code>data != null && !data.isEmpty()</code>
     */
    protected static boolean hasData(List<?> data) {
        return data != null && !data.isEmpty();
    }

    @Override
    public void setUp() {
        isSetUp = true;
    }

    @Override
    public void dismantle() {
        isSetUp = false;
    }

    /**
     * Returns <code>true</code>, if the module is in an initialized state.
     * Otherwise, <code>false</code> is returned. The default implementation
     * assumes that an initialized state is reached after {@link #setUp()} was
     * invoked.
     *
     * @return <code>true</code>, if the module is in an initialized state
     */
    protected boolean isSetUp() {
        return isSetUp;
    }

    /**
     * Returns <code>false</code>, if the module is in a dismantled state.
     * Otherwise, <code>false</code> is returned. The default implementation
     * assumes that this state is reached after {@link #dismantle()} was invoked
     * or before {@link #setUp()} is called for the first time.
     *
     * @return <code>false</code>, if the module is in a dismantled state
     */
    protected boolean isDismantled() {
        return !isSetUp();
    }

}
