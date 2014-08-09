package de.claas.mosis.processing.debug;

import java.util.List;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.DecoratorProcessor;
import de.claas.mosis.model.Processor;

/**
 * The class {@link SystemOut}. It is intended for debugging purposes. This
 * {@link DecoratorProcessor} implementation will print it's name to
 * {@link System#out} as well as the input and output values of the
 * {@link Processor#process(List, List)} method.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class SystemOut extends DecoratorProcessor<Object, Object> {

    @Parameter("Name / identifier of this module. This name is prepended before the input and output values are written to System.out.")
    public static final String NAME = "name";

    /**
     * Initializes the class with default values.
     */
    public SystemOut() {
	addCondition(NAME, new Condition.IsNotNull());
	setParameter(NAME, toString());
    }

    /**
     * Initializes the class with the given values.
     * 
     * @param name
     *            the parameter {@link #NAME}
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
