package de.claas.mosis.processing.debug;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.DecoratorProcessor;
import de.claas.mosis.model.ProcessorAdapter;
import de.claas.mosis.model.Relation;

import java.util.List;

/**
 * The class {@link de.claas.mosis.processing.debug.Logger}. It is intended for
 * debugging purposes. This {@link de.claas.mosis.model.DecoratorProcessor}
 * implementation will log calls to all methods of the the execution of its
 * wrapped {@link de.claas.mosis.model.Processor} object..
 *
 * @param <I> type of incoming data. See {@link de.claas.mosis.model.Processor}
 *            for details.
 * @param <O> type of outgoing data. See {@link de.claas.mosis.model.Processor}
 *            for details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class Logger<I, O> extends DecoratorProcessor<I, O> {

    @Parameter("A name for the logger. Find or create a logger for a named subsystem. This should be a dot-separated name and should normally be based on the package name or class name of the subsystem, such as de.class.mosis")
    public static final String NAME = "name";
    private java.util.logging.Logger logger;

    public Logger() {
        setParameter(NAME, "de.class.mosis");
        addCondition(NAME, new Condition.IsNotNull());
    }

    @Override
    public List<String> getParameters() {
        entering("getParameters");
        List<String> parameters = super.getParameters();
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
        entering("setParameter", parameter, value);
        super.setParameter(parameter, value);
        exiting("setParameter");
    }

    @Override
    protected void addCondition(String parameter, Condition condition) {
        entering("addCondition", parameter, condition);
        super.addCondition(parameter, condition);
        exiting("addCondition");
    }

    @Override
    protected void removeCondition(String parameter, Condition condition) {
        entering("removeCondition", parameter, condition);
        super.removeCondition(parameter, condition);
        exiting("removeCondition");
    }

    @Override
    protected void addRelation(Relation relation) {
        entering("addRelation", relation);
        super.addRelation(relation);
        exiting("addRelation");
    }

    @Override
    protected void removeRelation(Relation relation) {
        entering("removeRelation", relation);
        super.removeRelation(relation);
        exiting("removeRelation");
    }

    @Override
    public void setUp() {
        logger = java.util.logging.Logger.getLogger(getParameter(NAME));

        entering("setUp");
        super.setUp();
        exiting("setUp");
    }

    @Override
    public void dismantle() {
        entering("dismantle");
        super.dismantle();
        exiting("dismantle");

        logger = null;
    }

    @Override
    public void process(List<I> in, List<O> out) {
        entering("process", in, out);
        super.process(in, out);
        exiting("process", in, out);
    }

    @Override
    protected ProcessorAdapter<I, O> getProcessor() {
        entering("getProcessor");
        ProcessorAdapter<I, O> processor = super.getProcessor();
        exiting("getProcessor", processor);
        return processor;
    }

    private void entering(String method, Object... params) {
        if (logger != null && params.length > 0)
            logger.entering(getParameter(CLASS), method, params);
        else if (logger != null)
            logger.entering(getParameter(CLASS), method);
    }

    private void exiting(String method, Object... params) {
        if (logger != null && params.length > 0)
            logger.exiting(getParameter(CLASS), method, params);
        else if (logger != null)
            logger.exiting(getParameter(CLASS), method);
    }
}
