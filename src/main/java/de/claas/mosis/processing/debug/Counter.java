package de.claas.mosis.processing.debug;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.DecoratorProcessor;

import java.util.List;

/**
 * The class {@link de.claas.mosis.processing.debug.Counter}. It is intended for
 * debugging purposes. This {@link de.claas.mosis.model.DecoratorProcessor}
 * implementation counts the number of times that {@link
 * de.claas.mosis.model.Processor#process(java.util.List, java.util.List)} of
 * its wrapped {@link de.claas.mosis.model.Processor} object was called.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        category = Category.Decorator,
        author = {"Claas Ahlrichs"},
        description = "This is a realization of a DecoratorProcessor that is mostly intended for debugging purposes. This implementation counts the number of times that the process-method of the decorated module was called. Here, the count can be retrieved as a parameter.",
        purpose = "This implementation is intended for debugging purposes.")
public class Counter extends DecoratorProcessor<Object, Object> {

    @Parameter("Number of times the process-method has been invoked.")
    public static final String COUNTER = "counter";

    /**
     * Initializes the class with default values.
     */
    public Counter() {
        addCondition(LOCAL + COUNTER, new Condition.IsInteger());
        addCondition(LOCAL + COUNTER, new Condition.IsGreaterOrEqual(0d));
        setParameter(LOCAL + COUNTER, 0);
    }

    @Override
    public void process(List<Object> in, List<Object> out) {
        super.process(in, out);
        Integer count = getParameterAsInteger(COUNTER);
        setParameter(COUNTER, count + 1);
    }

}
