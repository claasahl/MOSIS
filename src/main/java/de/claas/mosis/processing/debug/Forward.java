package de.claas.mosis.processing.debug;

import de.claas.mosis.model.Processor;
import de.claas.mosis.model.ProcessorAdapter;

import java.util.List;

/**
 * The class {@link Forward}. It is intended for debugging purposes. This
 * {@link Processor} implementation returns the input values directly. It
 * performs no operation other than forwarding the received input values.
 *
 * @param <I> type of data. See {@link Processor} for details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class Forward<I> extends ProcessorAdapter<I, I> {

    @Override
    public void process(List<I> in, List<I> out) {
        out.addAll(in);
    }

}
