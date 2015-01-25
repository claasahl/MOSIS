package de.claas.mosis.io.generator;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Configurable;
import de.claas.mosis.model.Observer;
import de.claas.mosis.model.ProcessorAdapter;

import java.util.List;

/**
 * The class {@link de.claas.mosis.io.generator.Random}. It is intended to
 * generate random values based on a {@link java.util.Random} object. The
 * returned random numbers are within the range defined by {@link #LOWER}
 * (inclusive) and {@link #UPPER} (exclusive).
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class Random extends ProcessorAdapter<Double, Double> implements Observer {

    @Parameter("Represents the seed of this random number generator.")
    public static final String SEED = "random_seed";
    @Parameter("Lower boundary / minimal random value (inclusive).")
    public static final String LOWER = "lower boundary";
    @Parameter("Upper boundary / maximal random value (exclusive).")
    public static final String UPPER = "upper boundary";
    private final java.util.Random random;
    private double lower, upper;

    /**
     * Initializes the class with default values.
     */
    public Random() {
        random = new java.util.Random();
        addCondition(SEED, new Condition.IsInteger());
        setParameter(SEED, System.currentTimeMillis());
        addCondition(LOWER, new Condition.IsNumeric());
        setParameter(LOWER, 0);
        addCondition(UPPER, new Condition.IsNumeric());
        setParameter(UPPER, 1);
        addObserver(this);
    }

    @Override
    public void setUp() {
        super.setUp();
        random.setSeed(getParameterAsLong(SEED));
        lower = getParameterAsDouble(LOWER);
        upper = getParameterAsDouble(UPPER);
    }

    @Override
    public void process(List<Double> in, List<Double> out) {
        double random = this.random.nextDouble();
        upper = getParameterAsDouble(UPPER);
        lower = getParameterAsDouble(LOWER);
        out.add(random * (upper - lower) + lower);
    }

    @Override
    public void update(Configurable configurable, String parameter) {
        if (LOWER.equals(parameter) || UPPER.equals(parameter)) {
            double upper = getParameterAsDouble(UPPER);
            double lower = getParameterAsDouble(LOWER);
            if (upper < lower) {
                String msg = "General contract between LOWER (%f) and UPPER" +
                        " (%f) boundary (LOWER < UPPER) must not be broken.";
                throw new IllegalArgumentException(String.format(msg, lower, upper));
            }
        }
    }
}
