package de.claas.mosis.processing.debug;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.DecoratorProcessor;
import de.claas.mosis.model.Observer;
import de.claas.mosis.model.ProcessorAdapter;

import java.util.Collection;
import java.util.List;

/**
 * The class {@link de.claas.mosis.processing.debug.Logger}. It is intended for
 * debugging purposes. This {@link de.claas.mosis.model.DecoratorProcessor}
 * implementation will log calls to all methods of the the execution of its
 * wrapped {@link de.claas.mosis.model.Processor} object. The actual logging is
 * based on Java's logging implementation (see {@link java.util.logging.Logger}
 * and related classes / interfaces).
 * <p/>
 * The {@link java.util.logging.Logger}, that is being utilized by this module,
 * must have been registered by the {@link java.util.logging.LogManager}. The
 * {@link java.util.logging.Level} of the {@link java.util.logging.Logger} and
 * {@link java.util.logging.Handler} must be at least {@link
 * java.util.logging.Level#FINER}.
 *
 * @param <I> type of incoming data. See {@link de.claas.mosis.model.Processor}
 *            for details.
 * @param <O> type of outgoing data. See {@link de.claas.mosis.model.Processor}
 *            for details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class Logger<I, O> extends DecoratorProcessor<I, O> {

    @Parameter("Name of the logger, that was previous registered and configured by a java.util.logging.LogManager.")
    public static final String NAME = "name";
    private String className = null;
    private java.util.logging.Logger logger;

    public Logger() {
        setParameter(LOCAL + NAME, "de.claas.mosis");
        addCondition(LOCAL + NAME, new Condition.IsNotNull());
    }

    @Override
    public Collection<String> getParameters() {
        entering("getParameters", null);
        Collection<String> parameters = super.getParameters();
        exiting("getParameters", parameters);
        return parameters;
    }

    @Override
    public String getParameter(String parameter) {
        entering("getParameter", parameter);
        String value = super.getParameter(parameter);
        exiting("getParameter", value);
        return value;
    }

    @Override
    public void setParameter(String parameter, String value) {
        entering("setParameter", String.format("parameter:%s, value:%s", parameter, value));
        super.setParameter(parameter, value);
        exiting("setParameter", null);
    }

    @Override
    protected void addCondition(String parameter, Condition condition) {
        entering("addCondition", String.format("parameter:%s, condition:%s", parameter, condition));
        super.addCondition(parameter, condition);
        exiting("addCondition", null);
    }

    @Override
    protected void removeCondition(String parameter, Condition condition) {
        entering("removeCondition", String.format("parameter:%s, condition:%s", parameter, condition));
        super.removeCondition(parameter, condition);
        exiting("removeCondition", null);
    }

    @Override
    public void addObserver(Observer observer) {
        entering("addObserver", observer);
        super.addObserver(observer);
        exiting("addObserver", null);
    }

    @Override
    public void removeObserver(Observer observer) {
        entering("removeObserver", observer);
        super.removeObserver(observer);
        exiting("removeObserver", null);
    }

    @Override
    public void notifyObservers(String parameter) {
        entering("notifyObservers", parameter);
        super.notifyObservers(parameter);
        exiting("notifyObservers", null);
    }

    @Override
    public void setUp() {
        className = getParameter(CLASS);
        logger = java.util.logging.Logger.getLogger(getParameter(NAME));

        entering("setUp", null);
        super.setUp();
        exiting("setUp", null);
    }

    @Override
    public void dismantle() {
        entering("dismantle", null);
        super.dismantle();
        exiting("dismantle", null);

        logger = null;
    }

    @Override
    public void process(List<I> in, List<O> out) {
        entering("process", in);
        super.process(in, out);
        exiting("process", out);
    }

    @Override
    protected ProcessorAdapter<I, O> getProcessor() {
        entering("getProcessor", null);
        ProcessorAdapter<I, O> processor = super.getProcessor();
        exiting("getProcessor", processor);
        return processor;
    }

    /**
     * A convenience method for logging the entry from a method. The level
     * {@link java.util.logging.Level#FINER} is used. If a parameter is
     * specified, then the parameter's value is logged as well.
     *
     * @param method    the method which is being entered
     * @param parameter the method's parameter(s)
     */
    private void entering(String method, Object parameter) {
        if (logger != null && parameter != null)
            logger.entering(className, method, parameter);
        else if (logger != null)
            logger.entering(className, method);
    }

    /**
     * A convenience method for logging the return from a method. The level
     * {@link java.util.logging.Level#FINER} is used. If a return value is
     * specified, then the return value is logged as well.
     *
     * @param method      the method which is being exited
     * @param returnValue the method's return value
     */
    private void exiting(String method, Object returnValue) {
        if (logger != null && returnValue != null)
            logger.exiting(className, method, returnValue);
        else if (logger != null)
            logger.exiting(className, method);
    }
}
