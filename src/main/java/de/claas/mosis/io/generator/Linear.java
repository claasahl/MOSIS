package de.claas.mosis.io.generator;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.ProcessorAdapter;

import java.util.List;

/**
 * The class {@link de.claas.mosis.io.generator.Linear}. It is intended to
 * generate values of a linear function. The function has the form <code>y = m *
 * x + b</code>, where <code>y</code> corresponds to the value that is
 * returned.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        dataSource = true,
        category = Category.InputOutput,
        author = {"Claas Ahlrichs"},
        description = "This implementation acts as a data source for a predefined (and linear) sequence of numbers. The generated numbers follow the pattern 'y = m*x + b', where 'y' corresponds to the returned number, 'm' defines the slope, 'b' represents the offset (for 'x=0') and 'x' sets the starting point. Any variable on the right hand-side of the expression can be configured. Every call to this module will increase the value of 'x' by one (default value) and return the 'y'-value. However, the step width (for 'x') between two successive calls can also be configured to any real number.",
        purpose = "To provide access to a predefined sequence of numbers.")
public class Linear extends ProcessorAdapter<Double, Double> {

    @Parameter("Slope of linear expression.")
    public static final String M = "m";
    @Parameter("Variable parameter X.")
    public static final String X = "x";
    @Parameter("Offset of linear expression (y intercept).")
    public static final String B = "b";
    @Parameter("Step with which parameter X is increased.")
    public static final String STEP = "step";
    double m, b, x, step;

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
    public void setUp() {
        m = getParameterAsDouble(M);
        x = getParameterAsDouble(X);
        b = getParameterAsDouble(B);
        step = getParameterAsDouble(STEP);
    }

    @Override
    public void process(List<Double> in, List<Double> out) {       
        out.add(m * x + b);
        x = x + step;
    }

}
