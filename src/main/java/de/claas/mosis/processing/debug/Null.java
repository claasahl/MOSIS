package de.claas.mosis.processing.debug;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.model.ProcessorAdapter;

import java.util.List;

/**
 * The class {@link de.claas.mosis.processing.debug.Null}. It is intended for
 * debugging purposes. This {@link de.claas.mosis.model.Processor}
 * implementation will always return <code>null</code> values, regardless of the
 * input.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        canHandelMissingData = true,
        dataSource = true,
        category = Category.Other,
        author = {"Claas Ahlrichs"},
        description = "Regardless of the input values, this implementation will always return exactly one null-value.",
        purpose = "This implementation is intended for debugging purposes.")
public class Null extends ProcessorAdapter<Object, Object> {

    @Override
    public void process(List<Object> in, List<Object> out) {
        out.add(null);
    }

}
