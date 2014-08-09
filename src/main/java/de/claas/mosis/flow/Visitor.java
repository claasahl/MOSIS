package de.claas.mosis.flow;

import java.util.Iterator;

import de.claas.mosis.flow.iterator.OneShotLevelOrder;

/**
 * The interface {@link Visitor}. It is intended to provide a unified way for
 * implementing functionality across a graph of {@link Node}s. This represents a
 * generic approach that can be used to perform checks, process data or other
 * arbitrary operations.
 * 
 * The idea is such that implementations of the {@link Iterator} interface (e.g.
 * {@link OneShotLevelOrder}) are used to iterate over a graph of processing
 * modules (i.e. {@link Node} implementations). Each {@link Node} is visited by
 * a {@link Visitor} implementation.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public interface Visitor {

    /**
     * Return <code>true</code>, if the next {@link Node} may be visited (i.e.
     * given {@link Node} was successfully visited). Otherwise
     * <code>false</code> is returned.
     * 
     * @param node
     *            the {@link Node} being visited
     * @return <code>true</code>, if the next {@link Node} may be visited
     */
    public boolean visitPlainNode(PlainNode node);

    /**
     * Return <code>true</code>, if the next {@link Node} may be visited (i.e.
     * given {@link Node} was successfully visited). Otherwise
     * <code>false</code> is returned.
     * 
     * @param node
     *            the {@link Node} being visited
     * @return <code>true</code>, if the next {@link Node} may be visited
     */
    public boolean visitCompositeNode(CompositeNode node);

}
