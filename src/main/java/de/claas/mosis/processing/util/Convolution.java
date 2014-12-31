package de.claas.mosis.processing.util;

import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Configurable;
import de.claas.mosis.model.Relation;
import de.claas.mosis.processing.BufferingProcessor;

import java.util.List;
import java.util.regex.Pattern;

/**
 * The class {@link Convolution}. It is intended to provide the means to do a
 * one-dimensional convolution. This {@link BufferingProcessor} implementation
 * can be used to highlight (or suppress) certain features of input values.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class Convolution extends BufferingProcessor<Double, Double> {

    // TODO Make {two,multi}-dimensional?
    public static final String REQUIRES_FULL_BUFFER = "requires a full buffer";
    public static final String PORT_TO_USE = Delay.PORT_TO_USE;
    public static final String WEIGHTS = "weights";
    public static final String SEPARATOR = "separator";
    public static final String DEFAULT_VALUE = "default value";
    private double[] _Factors;

    /**
     * Initializes the class with default values.
     */
    public Convolution() {
        addCondition(REQUIRES_FULL_BUFFER, new Condition.IsBoolean());
        setParameter(REQUIRES_FULL_BUFFER, false);
        addCondition(PORT_TO_USE, new Condition.IsGreaterOrEqual(0d));
        addCondition(PORT_TO_USE, new Condition.IsInteger());
        setParameter(PORT_TO_USE, 0);
        addCondition(WEIGHTS, new Condition.IsNotNull());
        addCondition(SEPARATOR, new Condition.IsNotNull());
        setParameter(SEPARATOR, ",");
        addCondition(DEFAULT_VALUE, new Condition.IsNumeric());
        setParameter(DEFAULT_VALUE, 1);
        setParameter(WEIGHTS, "");
    }

    @Override
    public void setUp() {
        super.setUp();
        Relation r = new UpdateFactors();
        addRelation(r);
        r.compute(this, WEIGHTS, getParameter(WEIGHTS));
    }

    @Override
    public void dismantle() {
        super.dismantle();
        removeRelation(new UpdateFactors());
        _Factors = null;
    }

    @Override
    public void process(List<Double> in, List<Double> out) {
        appendAndRemove(in);
        if (getParameterAsBoolean(REQUIRES_FULL_BUFFER) && !isBufferFull()) {
            out.add(null);
        }

        int length = getParameterAsInteger(WINDOW_SIZE);
        double result = 0;
        for (int i = length - getBuffer().size(); i < length; i++) {
            int index = i - (length - getBuffer().size());
            double value = getBuffer().get(index).get(
                    getParameterAsInteger(PORT_TO_USE));
            result += _Factors[i] * value;
        }
        out.add(result);
    }

    /**
     * The class {@link UpdateFactors}. It is intended to update the
     * {@link Convolution#_Factors} array whenever the
     * {@link Convolution#WEIGHTS}, {@link Convolution#DEFAULT_VALUE} or
     * {@link Convolution#WINDOW_SIZE} parameter is changed.
     *
     * @author Claas Ahlrichs (claasahl@tzi.de)
     */
    private class UpdateFactors implements Relation {

        @Override
        public void compute(Configurable configurable, String parameter,
                            String value) {
            if (WEIGHTS.equals(parameter) || WINDOW_SIZE.equals(parameter)
                    || DEFAULT_VALUE.equals(parameter)) {
                // Preparations
                String separator = Pattern.quote(getParameter(SEPARATOR));
                String[] factors = (WEIGHTS.equals(parameter) ? value
                        : getParameter(WEIGHTS)).split(separator);
                int length = WINDOW_SIZE.equals(parameter) ? Integer
                        .parseInt(value) : getParameterAsInteger(WINDOW_SIZE);

                // Update
                _Factors = new double[length];
                for (int i = 0; i < length; i++) {
                    if (i < factors.length && !factors[i].trim().isEmpty()) {
                        _Factors[i] = Double.parseDouble(factors[i].trim());
                    } else {
                        _Factors[i] = getParameterAsDouble(DEFAULT_VALUE);
                    }
                }
            }
        }

    }

}
