package de.claas.mosis.processing.debug;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.model.ProcessorAdapter;

import java.util.List;

/**
 * The class {@link de.claas.mosis.processing.debug.ToString}. It is intended
 * for debugging purposes. This {@link de.claas.mosis.model.Processor}
 * implementation returns the input values as {@link java.lang.String} values.
 * It performs no operation other than calling {@link java.lang.Object#toString()}
 * on objects.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        supportMultipleInputs = true,
        canHandelMissingData = true,
        category = Category.Other,
        author = {"Claas Ahlrichs"},
        description = "This implementation converts all input values into String-objects. Null values are simply forwarded.",
        purpose = "This implementation is intended for debugging purposes.")
public class ToString extends ProcessorAdapter<Object, String> {

    @Override
    public void process(List<Object> in, List<String> out) {
        for (Object data : in) {
            out.add(data == null ? null : data.toString());
        }
    }

}
