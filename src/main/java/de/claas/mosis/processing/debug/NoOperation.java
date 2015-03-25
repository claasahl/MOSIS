package de.claas.mosis.processing.debug;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.model.ProcessorAdapter;

import java.util.List;

/**
 * The class {@link de.claas.mosis.processing.debug.NoOperation}. It is intended
 * for debugging purposes. This {@link de.claas.mosis.model.Processor}
 * implementation will never return values, regardless of the input.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        noOutputData = {"It will never return a value."},
        category = Category.Other,
        author = {"Claas Ahlrichs"},
        description = "This implementation does nothing.",
        purpose = "This implementation is intended for debugging purposes.")
public class NoOperation extends ProcessorAdapter<Object, Object> {

    @Override
    public void process(List<Object> in, List<Object> out) {
    }

}
