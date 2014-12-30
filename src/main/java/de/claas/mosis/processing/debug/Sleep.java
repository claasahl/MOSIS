package de.claas.mosis.processing.debug;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.DecoratorProcessor;
import de.claas.mosis.model.Processor;

import java.util.List;

/**
 * The class {@link Sleep}. It is intended for debugging purposes. This
 * {@link DecoratorProcessor} implementation will delay the execution of its
 * wrapped {@link Processor} object..
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class Sleep extends DecoratorProcessor<Object, Object> {

    @Parameter("Number of milliseconds that the invocation of the process-method is delayed.")
    public static final String DELAY = "delay";

    /**
     * Initializes the class with default values.
     */
    public Sleep() {
        addCondition(DELAY, new Condition.IsInteger());
        addCondition(DELAY, new Condition.IsGreaterOrEqual(0.0));
        setParameter(DELAY, 0);
    }

    @Override
    public void process(List<Object> in, List<Object> out) {
        try {
            Thread.sleep(getParameterAsLong(DELAY));
            super.process(in, out);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
