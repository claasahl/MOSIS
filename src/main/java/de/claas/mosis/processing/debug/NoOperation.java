package de.claas.mosis.processing.debug;

import de.claas.mosis.model.Processor;
import de.claas.mosis.model.ProcessorAdapter;

import java.util.List;

/**
 * The class {@link NoOperation}. It is intended for debugging purposes. This
 * {@link Processor} implementation will never return values, regardless of the
 * input.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class NoOperation extends ProcessorAdapter<Object, Object> {

    @Override
    public void process(List<Object> in, List<Object> out) {
    }

}
