package de.claas.mosis.io;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Processor;
import de.claas.mosis.model.ProcessorAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * The class {@link DataHandler}. It is a partial implementation of the
 * {@link Processor} interface and forms the core of all input / output handling
 * {@link Processor}s. The basic idea is to give external entities the
 * possibility to communicate with modules in this framework (e.g. files, data
 * services, databases, etc.). Implementing classes may provide the ability to
 * communicate through stream-based resources (e.g. {@link StreamHandler}) or
 * directly pass objects to the framework (e.g. {@link QueueHandler}).
 *
 * @param <T> type of (incoming and outgoing) data. See {@link Processor} for
 *            details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public abstract class DataHandler<T> extends ProcessorAdapter<T, T> {

    @Parameter("Mode of operation. Defines whether values should be read or written.")
    public static final String MODE = "mode";
    public static final String MODE_AUTO = "auto";
    public static final String MODE_READ = "read";
    public static final String MODE_WRITE = "write";
    @Parameter("Whether input values should be forwarded (during write operations).")
    public static final String FORWARD_INPUTS = "forward input data";

    /**
     * Initializes the class with default values.
     */
    public DataHandler() {
        List<String> whiteList = Arrays
                .asList(MODE_AUTO, MODE_READ, MODE_WRITE);
        addCondition(MODE, new Condition.IsInList(whiteList));
        setParameter(MODE, MODE_AUTO);
        addCondition(FORWARD_INPUTS, new Condition.IsBoolean());
        setParameter(FORWARD_INPUTS, true);
    }

    /**
     * Returns <code>true</code>, if the current mode of operation is
     * "read only". Otherwise, <code>false</code> is returned. This is either
     * the case when the {@link #MODE} parameter is set to {@value #MODE_READ}
     * or {@link #MODE_AUTO} and no data is passed to the {@link #process(java.util.List, java.util.List)}
     * method.
     *
     * @param data the data passed to {@link #process(java.util.List, java.util.List)}
     * @return <code>true</code>, if the current mode of operation is
     * "read only"
     */
    protected boolean isReadOnly(List<?> data) {
        return MODE_READ.equals(getParameter(MODE))
                || MODE_AUTO.equals(getParameter(MODE)) && !hasData(data);
    }

    /**
     * Returns <code>true</code>, if the current mode of operation is
     * "write only". Otherwise, <code>false</code> is returned. This is either
     * the case when the {@link #MODE} parameter is set to {@value #MODE_WRITE}
     * or {@link #MODE_AUTO} and some data is passed to the
     * {@link #process(java.util.List, java.util.List)} method.
     *
     * @param data the data passed to {@link #process(java.util.List, java.util.List)}
     * @return <code>true</code>, if the current mode of operation is
     * "write only"
     */
    protected boolean isWriteOnly(List<?> data) {
        return MODE_WRITE.equals(getParameter(MODE))
                || MODE_AUTO.equals(getParameter(MODE)) && hasData(data);
    }

    /**
     * Returns <code>true</code>, if the input data should be forwarded (as
     * output data). Otherwise, <code>false</code> is returned.
     *
     * @return <code>true</code>, if the input data should be forwarded (as
     * output data)
     */
    protected boolean shouldFoward() {
        return getParameterAsBoolean(FORWARD_INPUTS);
    }

}