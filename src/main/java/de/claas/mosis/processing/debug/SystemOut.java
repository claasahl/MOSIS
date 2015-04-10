package de.claas.mosis.processing.debug;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.DecoratorProcessor;

import java.util.List;

/**
 * The class {@link de.claas.mosis.processing.debug.SystemOut}. It is intended
 * for debugging purposes. This {@link de.claas.mosis.model.DecoratorProcessor}
 * implementation will print it's name to {@link java.lang.System#out} as well
 * as the input and output values of the {@link de.claas.mosis.model.Processor#process(java.util.List,
 * java.util.List)} method.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        category = Category.Decorator,
        author = {"Claas Ahlrichs"},
        description = "This is a realization of a  DecoratorProcessor that is mostly intended for debugging purposes. This implementation outputs the input and output data of the decorated module. A name can be configured which would also be outputted.",
        purpose = "This implementation is intended for debugging purposes.")
public class SystemOut extends DecoratorProcessor<Object, Object> {

    @Parameter("Name / identifier of this module. This name is prepended before the input and output values are written to System.out.")
    public static final String NAME = "name";

    /**
     * Initializes the class with default values.
     */
    public SystemOut() {
        addCondition(LOCAL + NAME, new Condition.IsNotNull());
        setParameter(LOCAL + NAME, toString());
    }

    /**
     * Initializes the class with the given values.
     *
     * @param name the parameter {@link #NAME}
     */
    public SystemOut(String name) {
        setParameter(NAME, name);
    }

    @Override
    public void process(List<Object> in, List<Object> out) {
        System.out.format("%s: %s\n", getParameter(NAME), in);
        super.process(in, out);
        System.out.format("%s: %s\n", getParameter(NAME), out);
    }

}
