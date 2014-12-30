package de.claas.mosis.util;

import de.claas.mosis.flow.Node;
import de.claas.mosis.model.Configurable;
import de.claas.mosis.model.Processor;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * The class {@link Utils}. It is intended to provide a set of convenience
 * methods. They should come handy when performing tests or rapid prototyping.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public abstract class Utils {

    /**
     * Returns the first output of the {@link Processor} or <code>null</code> if
     * no output was generated. The input values are directly passed into
     * {@link Processor#process(List, List)}. This method greatly simplifies
     * testing as well as rapid prototyping.
     *
     * @param p  the {@link Processor}
     * @param in the input data
     * @return the first output of the {@link Processor}
     */
    @SafeVarargs
    public static <I, O> O process(Processor<I, O> p, I... in) {
        @SuppressWarnings("unchecked")
        List<O> out = (List<O>) new Vector<Object>();
        p.process(in == null ? null : Arrays.asList(in), out);
        return (out.isEmpty() ? null : out.get(0));
    }

    /**
     * Returns all output values of the {@link Processor} or an empty
     * {@link List} if no output was generated. The input values are directly
     * passed into {@link Processor#process(List, List)}. This method greatly
     * simplifies testing as well as rapid prototyping.
     *
     * @param p  the {@link Processor}
     * @param in the input data
     * @return all outputs value of the {@link Processor}
     */
    @SafeVarargs
    public static <I, O> List<O> processAll(Processor<I, O> p, I... in) {
        @SuppressWarnings("unchecked")
        List<O> out = (List<O>) new Vector<Object>();
        p.process(in == null ? null : Arrays.asList(in), out);
        return out;
    }

    /**
     * Returns a {@link Set} of {@link Node}s that represent data sources (i.e.
     * do have not predecessors).
     *
     * @param representables the initial {@link Set} of {@link Node}s used to look for data
     *                       sources
     * @return a {@link Set} of {@link Node}s that represent data sources.
     */
    public static Set<Node> getSources(Set<Node> representables) {
        // TODO Once implemented, do not forget to test.
        throw new NotImplementedException();
    }

    /**
     * Returns a {@link Set} of {@link Node}s that represent data sinks (i.e. do
     * have not successors).
     *
     * @param representables the initial {@link Set} of {@link Node}s used to look for data
     *                       sinks
     * @return a {@link Set} of {@link Node}s that represent data sinks
     */
    public static Set<Node> getSinks(Set<Node> representables) {
        // TODO Once implemented, do not forget to test.
        throw new NotImplementedException();
    }

    /**
     * Instantiates and returns an instance of a class. This method uses
     * reflection to call the constructor that matches the input arguments.
     *
     * @param clazz the {@link Class} that the instance will have
     * @param args  the arguments that are used to create the
     * @return an instance of a class
     * @throws Exception if the class could not be instantiated (e.g. not matching
     *                   constructor was found)
     */
    @SuppressWarnings("unchecked")
    public static <T> T instance(Class<T> clazz, Object... args)
            throws Exception {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            Class<?>[] arguments = constructor.getParameterTypes();
            boolean takeMe = true;
            for (int i = 0; takeMe && i < arguments.length; i++) {
                takeMe &= i < args.length
                        && arguments[i].isAssignableFrom(args[i].getClass());
            }
            if (takeMe && arguments.length == args.length) {
                return (T) constructor.newInstance(args);
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Returns an unknown parameter (i.e. unknown to the {@link Configurable}
     * object).
     *
     * @param c the {@link Configurable} object
     * @return an unknown parameter
     */
    public static String unknownParameter(Configurable c) {
        String parameter = String.format("%f", Math.random());
        return c.getParameters().contains(parameter) ? unknownParameter(c)
                : parameter;
    }

}
