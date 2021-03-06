package de.claas.mosis.model;

import de.claas.mosis.annotation.Documentation;

import java.util.List;

/**
 * The interface {@link de.claas.mosis.model.Processor}. It is intended to
 * provide a unified way for processing time series data. This represents a
 * generic module that can be utilized to perform an arbitrary computations,
 * transformations, etc. Multiple of these modules can be linked together in
 * order to perform more complex tasks (e.g. transform data from one format to
 * another, inspect network traffic or analyze OpenStreetMap data, etc.).
 * <p>
 * The life-cycle is such that {@link #setUp()} is called first. Then {@link
 * #process(java.util.List, java.util.List)} is invoked as often as required and
 * finally the module is dismantled with {@link #dismantle()}. At this point,
 * the set-up method may be invoked again. Thus starting the cycle over again.
 *
 * @param <I> type of incoming data (e.g. {@link java.lang.Integer})
 * @param <O> type of outgoing data (e.g. {@link java.lang.Double})
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        purpose = "It is intended to provide a unified interface for processing time series data as well as getting and setting parameters.",
        description = "This represents the interface to which all modules within the framework adhere. Here all basic functionalities that a module must implement are listed and described. Most notably, it provides a common interface for processing time series data as well as handling of parameters. When adding (new) modules to the framework, this interface must be implemented either directly or indirectly by another (partial) implementation (e.g. ProcessorAdapter, BufferingProcessor, ComparingProcessor, etc.). It is encouraged to use these modules as reference implementations.",
        author = "Claas Ahlrichs",
        noOutputData = "Refer to concrete implementations.")
public interface Processor<I, O> extends Configurable {

    /**
     * Processes incoming data and provides output data in return. The incoming
     * data can be thought of as a single sample of one or more time series. The
     * result may be calculated either directly or over several of such samples
     * (e.g. once enough data has been collected). This method is not required
     * to provide any output values nor is it required to interpret incoming
     * data. However, at least one of the two should be done. It can be safely
     * assumed that all parameters will never be <code>null</code>.
     *
     * @param in  inbound data. This data may stem from (multiple) time series.
     *            When multiple input values are received, then multiple
     *            processing modules provided data. Multiple input values do not
     *            reflect a bulk of data from a single processing module.
     * @param out outbound data. The amount of output values does not affect how
     *            the data is being processing by succeeding modules. Multiple
     *            output values are not processed in a bulk.
     */
    public void process(List<I> in, List<O> out);

    /**
     * Sets up the processing module. This method should be used to perform any
     * initialization (e.g. based on configuration parameters) that may the
     * required for the module to properly work. It can be safely assumed that
     * this method is invoked before any processing is done (i.e. call of {@link
     * #process(java.util.List, java.util.List)}).
     * <p>
     * One cannot assume that a module reacts to changes im a parameter
     * immediately. However, the module must have reacted to changes in a
     * parameter after this method has been called. Nonetheless, a module may
     * adapt to changes in parameters as soon as they occur. But when using
     * arbitrary modules, one must "restart" a module to ensure that changes in
     * parameters take effect.
     */
    public void setUp();

    /**
     * Dismantles the processing module. This method should be used to perform
     * any clean-up and dismantling that may be required for the module to
     * properly shutdown. It can be safely assumed that this method is always
     * invoked after {@link #setUp()} was called.
     */
    public void dismantle();

}
