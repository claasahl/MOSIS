package de.claas.mosis.io.generator;

import java.util.List;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Configurable;
import de.claas.mosis.model.ProcessorAdapter;
import de.claas.mosis.model.Relation;

/**
 * The class {@link Random}. It is intended to generate random values based on a
 * {@link java.util.Random} object. The returned random numbers are within the
 * range defined by {@link #LOWER} (inclusive) and {@link #UPPER} (exclusive).
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class Random extends ProcessorAdapter<Double, Double> {

    @Parameter("Represents the seed of this random number generator.")
    public static final String SEED = "random_seed";
    @Parameter("Lower boundary / minimal random value (inclusive).")
    public static final String LOWER = "lower boundary";
    @Parameter("Upper boundary / maximal random value (exclusive).")
    public static final String UPPER = "upper boundary";
    private final java.util.Random _Random;

    /**
     * Initializes the class with default values.
     */
    public Random() {
	_Random = new java.util.Random();
	addCondition(SEED, new Condition.IsInteger());
	setParameter(SEED, System.currentTimeMillis());
	addCondition(LOWER, new Condition.IsNumeric());
	setParameter(LOWER, 0);
	addCondition(UPPER, new Condition.IsNumeric());
	setParameter(UPPER, 1);
    }

    @Override
    public void setUp() {
	super.setUp();
	Relation r = new ChangeSeed();
	addRelation(r);
	r.compute(this, SEED, getParameter(SEED));
    }

    @Override
    public void dismantle() {
	super.dismantle();
	removeRelation(new ChangeSeed());
    }

    @Override
    public void process(List<Double> in, List<Double> out) {
	Double random = _Random.nextDouble();
	Double upper = getParameterAsDouble(UPPER);
	Double lower = getParameterAsDouble(LOWER);
	out.add(random * (upper - lower) + lower);
    }

    /**
     * The class {@link ChangeSeed}. It is intended to (re-)set the seed of the
     * {@link java.util.Random} generator object whenever the
     * {@link Random#SEED} parameter is changed.
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * 
     */
    private class ChangeSeed implements Relation {

	@Override
	public void compute(Configurable configurable, String parameter,
		String value) {
	    if (SEED.equals(parameter)) {
		_Random.setSeed(Long.parseLong(value));
	    } else if (LOWER.equals(parameter) || UPPER.equals(parameter)) {
		double lower = getParameterAsDouble(LOWER);
		double upper = getParameterAsDouble(UPPER);
		if (lower >= upper) {
		    String msg = String
			    .format("General contract between LOWER (%f) and UPPER (%f) boundary (LOWER < UPPER) must not be broken.",
				    lower, upper);
		    throw new IllegalArgumentException(msg);
		}
	    }
	}
    }

}
