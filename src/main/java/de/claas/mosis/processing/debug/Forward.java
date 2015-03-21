package de.claas.mosis.processing.debug;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.model.ProcessorAdapter;

import java.util.List;

/**
 * The class {@link de.claas.mosis.processing.debug.Forward}. It is intended for
 * debugging purposes. This {@link de.claas.mosis.model.Processor}
 * implementation returns the input values directly. It performs no operation
 * other than forwarding the received input values.
 *
 * @param <I> type of data. See {@link de.claas.mosis.model.Processor} for
 *            details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        supportMultipleInputs = true,
        canHandelMissingData = true,
        category = Category.Other,
        author = {"Claas Ahlrichs"},
        description = "This implementation forwards all input values as output values.",
        purpose = "This implementation is intended for debugging purposes.")
public class Forward<I> extends ProcessorAdapter<I, I> {

    @Override
    public void process(List<I> in, List<I> out) {
        out.addAll(in);
    }

}
