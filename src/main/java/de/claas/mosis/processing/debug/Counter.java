package de.claas.mosis.processing.debug;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.DecoratorProcessor;
import de.claas.mosis.model.Processor;

import java.util.List;

/**
 * The class {@link Counter}. It is intended for debugging purposes. This
 * {@link DecoratorProcessor} implementation counts the number of times that
 * {@link Processor#process(java.util.List, java.util.List)} of its wrapped {@link Processor} object was
 * called.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class Counter extends DecoratorProcessor<Object, Object> {

    @Parameter("Number of times the process-method has been invoked.")
    public static final String COUNTER = "counter";

    /**
     * Initializes the class with default values.
     */
    public Counter() {
        addCondition(COUNTER, new Condition.IsInteger());
        addCondition(COUNTER, new Condition.IsGreaterOrEqual(0d));
        setParameter(COUNTER, 0);
    }

    @Override
    public void process(List<Object> in, List<Object> out) {
        super.process(in, out);
        Integer count = getParameterAsInteger(COUNTER);
        setParameter(COUNTER, count + 1);
    }

}
