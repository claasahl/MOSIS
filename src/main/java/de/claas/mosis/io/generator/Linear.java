package de.claas.mosis.io.generator;

import java.util.List;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.ProcessorAdapter;

/**
 * The class {@link Linear}. It is intended to generate values of a linear
 * function. The function has the form <code>y = m * x + b</code>, where
 * <code>y</code> corresponds to the value that is returned.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class Linear extends ProcessorAdapter<Double, Double> {

    @Parameter("Slope of linear expression.")
    public static final String M = "m";
    @Parameter("Variable parameter X.")
    public static final String X = "x";
    @Parameter("Offset of linear expression (y intercept).")
    public static final String B = "b";
    @Parameter("Step with which parameter X is increased.")
    public static final String STEP = "step";

    /**
     * Initializes the class with default values.
     */
    public Linear() {
	addCondition(M, new Condition.IsNumeric());
	setParameter(M, 1.0);
	addCondition(X, new Condition.IsNumeric());
	setParameter(X, 0.0);
	addCondition(B, new Condition.IsNumeric());
	setParameter(B, 0.0);
	addCondition(STEP, new Condition.IsNumeric());
	setParameter(STEP, 1.0);
    }

    @Override
    public void process(List<Double> in, List<Double> out) {
	double m = getParameterAsDouble(M);
	double x = getParameterAsDouble(X);
	double b = getParameterAsDouble(B);
	double step = getParameterAsDouble(STEP);

	setParameter(X, x + step);
	out.add(m * x + b);
    }

}
