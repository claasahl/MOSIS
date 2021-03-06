package de.claas.mosis.processing.debug;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.DecoratorProcessor;

import java.util.List;

/**
 * The class {@link de.claas.mosis.processing.debug.Time}. It is intended for
 * debugging purposes. This {@link de.claas.mosis.model.DecoratorProcessor}
 * implementation measures the time (measured in milliseconds) required to
 * execute {@link de.claas.mosis.model.Processor#process(java.util.List,
 * java.util.List)} of the wrapped {@link de.claas.mosis.model.Processor}
 * object. Furthermore it provides the time that the wrapped {@link
 * de.claas.mosis.model.Processor} object was first called and the time it was
 * last called (measured in milliseconds, between the current time and midnight,
 * January 1, 1970 UTC).
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        category = Category.Decorator,
        author = {"Claas Ahlrichs"},
        description = "This is a realization of a DecoratorProcessor that is mostly intended for debugging purposes. This implementation measures the number of milliseconds that the decorated module requires for processing. It can be used to analyze processing time of modules and optimize time efficiency.",
        purpose = "This implementation is intended for debugging purposes.")
public class Time extends DecoratorProcessor<Object, Object> {

    @Parameter("Number of milliseconds the last invocation of the process-method took.")
    public static final String TIME = "time";
    @Parameter("Total number of milliseconds spend in process-method (since first call of process-method and not just last invocation).")
    public static final String TOTAL_TIME = "total time";
    @Parameter("Timestamp of first invocation of process-method.")
    public static final String FIRST_CALL = "first call";
    @Parameter("Timestamp of last invocation of process-method.")
    public static final String LAST_CALL = "last call";

    /**
     * Initializes the class with default values.
     */
    public Time() {
        setParameter(LOCAL + TIME, "");
        addCondition(LOCAL + TIME, new Condition.IsInteger());
        addCondition(LOCAL + TIME, new Condition.IsGreaterOrEqual(0d));
        setParameter(LOCAL + TOTAL_TIME, 0);
        addCondition(LOCAL + TOTAL_TIME, new Condition.IsInteger());
        addCondition(LOCAL + TOTAL_TIME, new Condition.IsGreaterOrEqual(0d));
        setParameter(LOCAL + FIRST_CALL, "");
        addCondition(LOCAL + FIRST_CALL, new Condition.IsInteger());
        addCondition(LOCAL + FIRST_CALL, new Condition.IsGreaterOrEqual(0d));
        setParameter(LOCAL + LAST_CALL, "");
        addCondition(LOCAL + LAST_CALL, new Condition.IsInteger());
        addCondition(LOCAL + LAST_CALL, new Condition.IsGreaterOrEqual(0d));
    }

    @Override
    public void process(List<Object> in, List<Object> out) {
        long started = System.currentTimeMillis();
        super.process(in, out);
        long ended = System.currentTimeMillis();
        long total = getParameterAsLong(TOTAL_TIME);
        if (getParameter(FIRST_CALL).isEmpty()) {
            setParameter(FIRST_CALL, started);
        }
        setParameter(LAST_CALL, started);
        setParameter(TIME, ended - started);
        setParameter(TOTAL_TIME, total + ended - started);
    }

}
