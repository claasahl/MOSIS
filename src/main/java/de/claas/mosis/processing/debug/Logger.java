package de.claas.mosis.processing.debug;

import de.claas.mosis.model.DecoratorProcessor;

/**
 * The class {@link de.claas.mosis.processing.debug.Logger}. It is intended for
 * debugging purposes. This {@link de.claas.mosis.model.DecoratorProcessor}
 * implementation will log calls to all methods of the the execution of its
 * wrapped {@link de.claas.mosis.model.Processor} object..
 *
 * @param <I> type of incoming data. See {@link de.claas.mosis.model.Processor}
 *            for details.
 * @param <O> type of outgoing data. See {@link de.claas.mosis.model.Processor}
 *            for details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class Logger<I, O> extends DecoratorProcessor<I, O> {

    // TODO Use JAVA-based logging mechanism to input and / or output values

}
