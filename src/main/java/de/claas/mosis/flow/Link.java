package de.claas.mosis.flow;

import de.claas.mosis.model.Configurable;
import de.claas.mosis.model.Processor;

import java.util.List;

/**
 * The interface {@link Link}. It is intended to represent a connection between
 * two {@link Processor} modules. Their purpose is to buffer and monitor the
 * flow of information from one {@link Processor} to the next {@link Processor}.
 * Concrete implementations may be configurable and may choose which data they
 * accept (i.e. want to forward / buffer).
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public interface Link extends Configurable {

    /**
     * Returns <code>true</code>, if all given values were accepted by this
     * link. Otherwise, <code>false</code> is returned. The link may choose to
     * accept or discard any of the given values.
     *
     * @param in the value(s)
     * @return <code>true</code>, if all given values were accepted by this link
     */
    public abstract boolean push(List<Object> in);

    /**
     * Returns the next value from this link.
     *
     * @return the next value from this link
     */
    public abstract Object poll();

    /**
     * Returns <code>true</code> if this link does not contains elements.
     * Otherwise <code>false</code>.
     *
     * @return <code>true</code> if this link does not contains elements
     */
    public abstract boolean isEmpty();

}