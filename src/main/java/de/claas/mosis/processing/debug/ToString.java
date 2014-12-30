package de.claas.mosis.processing.debug;

import de.claas.mosis.model.Processor;
import de.claas.mosis.model.ProcessorAdapter;

import java.util.List;

/**
 * The class {@link ToString}. It is intended for debugging purposes. This
 * {@link Processor} implementation returns the input values as {@link String}
 * values. It performs no operation other than calling {@link Object#toString()}
 * on objects.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class ToString extends ProcessorAdapter<Object, String> {

    @Override
    public void process(List<Object> in, List<String> out) {
        for (Object data : in) {
            out.add(data == null ? null : data.toString());
        }
    }

}
