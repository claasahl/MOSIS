package de.claas.mosis.io.generator;

import de.claas.mosis.model.ProcessorAdapter;

import java.util.List;

/**
 * The class {@link de.claas.mosis.io.generator.Time}. It is intended to
 * generate time values. It returns the the difference, measured in
 * milliseconds, between the current time and midnight, January 1, 1970 UTC.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class Time extends ProcessorAdapter<Long, Long> {

    @Override
    public void process(List<Long> in, List<Long> out) {
        out.add(System.currentTimeMillis());
    }

}
