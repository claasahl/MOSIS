package de.claas.mosis.processing.debug;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.DecoratorProcessor;

import java.util.List;

/**
 * The class {@link de.claas.mosis.processing.debug.Sleep}. It is intended for
 * debugging purposes. This {@link de.claas.mosis.model.DecoratorProcessor}
 * implementation will delay the execution of its wrapped {@link
 * de.claas.mosis.model.Processor} object.
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
        addCondition(LOCAL + DELAY, new Condition.IsInteger());
        addCondition(LOCAL + DELAY, new Condition.IsGreaterOrEqual(0.0));
        setParameter(LOCAL + DELAY, 0);
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
