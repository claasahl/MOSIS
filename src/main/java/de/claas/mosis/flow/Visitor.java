package de.claas.mosis.flow;

/**
 * The interface {@link de.claas.mosis.flow.Visitor}. It is intended to provide
 * a unified way for implementing functionality across a graph of {@link
 * de.claas.mosis.flow.Node}s. This represents a generic approach that can be
 * used to perform checks, process data or other arbitrary operations.
 * <p>
 * The idea is such that implementations of the {@link java.util.Iterator}
 * interface (e.g. {@link de.claas.mosis.flow.iterator.OneShotLevelOrder}) are
 * used to iterate over a graph of processing modules (i.e. {@link
 * de.claas.mosis.flow.Node} implementations). Each {@link
 * de.claas.mosis.flow.Node} is visited by a {@link de.claas.mosis.flow.Visitor}
 * implementation.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public interface Visitor {

    /**
     * Return <code>true</code>, if the next {@link de.claas.mosis.flow.Node}
     * may be visited (i.e. given {@link de.claas.mosis.flow.Node} was
     * successfully visited). Otherwise <code>false</code> is returned.
     *
     * @param node the {@link de.claas.mosis.flow.Node} being visited
     * @return <code>true</code>, if the next {@link de.claas.mosis.flow.Node}
     * may be visited
     */
    public boolean visitPlainNode(PlainNode node);

    /**
     * Return <code>true</code>, if the next {@link de.claas.mosis.flow.Node}
     * may be visited (i.e. given {@link de.claas.mosis.flow.Node} was
     * successfully visited). Otherwise <code>false</code> is returned.
     *
     * @param node the {@link de.claas.mosis.flow.Node} being visited
     * @return <code>true</code>, if the next {@link de.claas.mosis.flow.Node}
     * may be visited
     */
    public boolean visitCompositeNode(CompositeNode node);

}
