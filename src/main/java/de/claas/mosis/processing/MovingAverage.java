package de.claas.mosis.processing;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;

import java.util.Arrays;
import java.util.List;

/**
 * The class {@link MovingAverage}. It is intended to calculate a simple moving
 * average. This {@link BufferingProcessor} implementation allows varying modes
 * of operation (see parameter {@link #MODE}) for non-full buffers.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class MovingAverage extends BufferingProcessor<Double, Double> {

    @Parameter("Mode of operation. Defines how the moving average is calculated (if at all) when the buffer is not entirely filled.")
    public static final String MODE = "mode";
    public static final String MODE_USE_ACTUAL_SIZE = "use actual size";
    public static final String MODE_USE_BUFFER_SIZE = "use buffer size";
    public static final String MODE_WAIT_FOR_BUFFER = "wait for buffer";

    /**
     * Initializes the class with default values.
     */
    public MovingAverage() {
        List<String> whiteList = Arrays.asList(MODE_USE_ACTUAL_SIZE,
                MODE_USE_BUFFER_SIZE, MODE_WAIT_FOR_BUFFER);
        addCondition(MODE, new Condition.IsInList(whiteList));
        setParameter(MODE, MODE_USE_ACTUAL_SIZE);
    }

    @Override
    public void process(List<Double> in, List<Double> out) {
        appendAndRemove(in);
        double sum = 0;
        for (List<Double> values : getBuffer()) {
            for (Double value : values) {
                sum += value;
            }
        }

        String mode = getParameter(MODE);
        if (MODE_USE_ACTUAL_SIZE.equals(mode)
                || MODE_WAIT_FOR_BUFFER.equals(mode) && isBufferFull()) {
            out.add(sum / (double) getBuffer().size());
        } else if (MODE_USE_BUFFER_SIZE.equals(mode)) {
            out.add(sum / getParameterAsDouble(WINDOW_SIZE));
        }
    }

}
