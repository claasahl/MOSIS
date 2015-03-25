package de.claas.mosis.util;

import de.claas.mosis.model.Configurable;
import de.claas.mosis.model.Processor;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * The class {@link de.claas.mosis.util.Utils}. It is intended to provide a set
 * of convenience methods. They should come handy when performing tests or rapid
 * prototyping.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public abstract class Utils {

    /**
     * Returns the first output of the {@link de.claas.mosis.model.Processor} or
     * <code>null</code> if no output was generated. The input values are
     * directly passed into {@link de.claas.mosis.model.Processor#process(java.util.List,
     * java.util.List)}. This method greatly simplifies testing as well as rapid
     * prototyping.
     *
     * @param p  the {@link de.claas.mosis.model.Processor}
     * @param in the input data
     * @return the first output of the {@link de.claas.mosis.model.Processor}
     */
    @SafeVarargs
    public static <I, O> O process(Processor<I, O> p, I... in) {
        @SuppressWarnings("unchecked")
        List<O> out = new Vector<>();
        p.process(in == null ? null : Arrays.asList(in), out);
        return (out.isEmpty() ? null : out.get(0));
    }

    /**
     * Returns all output values of the {@link de.claas.mosis.model.Processor}
     * or an empty {@link java.util.List} if no output was generated. The input
     * values are directly passed into {@link de.claas.mosis.model.Processor#process(java.util.List,
     * java.util.List)}. This method greatly simplifies testing as well as rapid
     * prototyping.
     *
     * @param p  the {@link de.claas.mosis.model.Processor}
     * @param in the input data
     * @return all outputs value of the {@link de.claas.mosis.model.Processor}
     */
    @SafeVarargs
    public static <I, O> List<O> processAll(Processor<I, O> p, I... in) {
        @SuppressWarnings("unchecked")
        List<O> out = new Vector<>();
        p.process(in == null ? null : Arrays.asList(in), out);
        return out;
    }

    /**
     * Instantiates and returns an instance of a class. This method uses
     * reflection to call the constructor that matches the input arguments.
     *
     * @param clazz the {@link java.lang.Class} that the instance will have
     * @param args  the arguments that are used to create the
     * @return an instance of a class
     * @throws java.lang.Exception if the class could not be instantiated (e.g.
     *                             not matching constructor was found)
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
     * Returns an unknown parameter (i.e. unknown to the {@link
     * de.claas.mosis.model.Configurable} object).
     *
     * @param c the {@link de.claas.mosis.model.Configurable} object
     * @return an unknown parameter
     */
    public static String unknownParameter(Configurable c) {
        String parameter = String.format("%f", Math.random());
        return c.getParameters().contains(parameter) ? unknownParameter(c)
                : parameter;
    }

    /**
     * Modules (i.e. {@link de.claas.mosis.model.Processor}s) cannot be expected
     * to handle changes in parameters once they have been initialized (i.e.
     * {@link de.claas.mosis.model.Processor#setUp()} was called). As such, the
     * module needs to be re-initialized for changes can be expected to take
     * effect.
     *
     * @param processor the module which parameter needs changing
     * @param parameter the parameter that needs to be changes
     * @param value     the "new" value for the parameter
     */
    public static void updateParameter(Processor processor, String parameter, String value) {
        updateParameters(processor, parameter, value);
    }

    /**
     * Modules (i.e. {@link de.claas.mosis.model.Processor}s) cannot be expected
     * to handle changes in parameters once they have been initialized (i.e.
     * {@link de.claas.mosis.model.Processor#setUp()} was called). As such, the
     * module needs to be re-initialized for changes can be expected to take
     * effect.
     *
     * @param processor  the module which parameter needs changing
     * @param parameters pairs of parameters and their corresponding values
     */
    public static void updateParameters(Processor processor, String... parameters) {
        if (parameters != null && parameters.length % 2 == 0) {
            processor.dismantle();
            for (int i = 0; i < parameters.length; i += 2) {
                String parameter = parameters[i];
                String value = parameters[i + 1];
                processor.setParameter(parameter, value);
            }
            processor.setUp();
        }
    }
}
