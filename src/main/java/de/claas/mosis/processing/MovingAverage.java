package de.claas.mosis.processing;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;

import java.util.Arrays;
import java.util.List;

/**
 * The class {@link de.claas.mosis.processing.MovingAverage}. It is intended to
 * calculate a simple moving average. This {@link de.claas.mosis.processing.BufferingProcessor}
 * implementation allows varying modes of operation (see parameter {@link
 * #MODE}) for non-full buffers.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        category = Category.Other,
        author = {"Claas Ahlrichs"},
        description = "This is a realization of the BufferingProcessor which is used to calculate a moving average for its input data. The number of data samples that are considered for the moving average can be configured. By setting the mode, it can also be configured how the moving average is calculated in the initial phase where the buffer is only partially filled (i.e. the number of data samples in the buffer is smaller than the actual size of the buffer). In general, the options include: waiting for the buffer to fill up, using the current size of the buffer or using the actual size of the buffer.",
        purpose = "To calculate the moving average.")
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
