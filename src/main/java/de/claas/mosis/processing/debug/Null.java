package de.claas.mosis.processing.debug;

import java.util.List;

import de.claas.mosis.model.Processor;
import de.claas.mosis.model.ProcessorAdapter;

/**
 * The class {@link Null}. It is intended for debugging purposes. This
 * {@link Processor} implementation will always return <code>null</code> values,
 * regardless of the input.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class Null extends ProcessorAdapter<Object, Object> {

    @Override
    public void process(List<Object> in, List<Object> out) {
	out.add(null);
    }

}
