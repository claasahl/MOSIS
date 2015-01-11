package de.claas.mosis.processing.debug;

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
public class Null extends ProcessorAdapter<Object, Object> {

    @Override
    public void process(List<Object> in, List<Object> out) {
        out.add(null);
    }

}
