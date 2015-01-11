package de.claas.mosis.flow;

import java.util.Collections;
import java.util.Set;

/**
 * The class {@link de.claas.mosis.flow.CompositeNode}. It is an implementation
 * of the {@link de.claas.mosis.flow.Node} class. This class is intended to
 * represent a node that is based around a set of {@link
 * de.claas.mosis.model.Processor} instances. The content of this node basically
 * represents an entire graph on its own. The only limitation is that there may
 * be only a single data sink. This stands in contrast to the {@link
 * de.claas.mosis.flow.PlainNode}, which represents a single {@link
 * de.claas.mosis.model.Processor} instance.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class CompositeNode extends Node {

    private final Set<Node> _Sources;
    private final Node _Sink;

    /**
     * Initializes the class with default parameter.
     *
     * @param sources the data source(s)
     * @param sink    the data sink
     */
    public CompositeNode(Set<Node> sources, Node sink) {
        if (sources == null || sink == null || sources.contains(null)) {
            throw new IllegalArgumentException(
                    "sources and sink may not be null nor may they contain null elements");
        }
        for (Node n : sources) {
            if (n.hasPredecessors()) {
                throw new IllegalArgumentException(
                        "sources may not have predecessors");
            }
        }
        if (sink.hasSuccessors()) {
            throw new IllegalArgumentException("sinks may not have successors");
        }
        _Sources = Collections.unmodifiableSet(sources);
        _Sink = sink;
    }

    /**
     * Returns the data source(s). These sources represent the entry point
     * (first level) of processing modules within this composition.
     *
     * @return the data source(s)
     */
    public Set<Node> getSources() {
        return _Sources;
    }

    /**
     * Returns the data sink. This sink represent the exit point (last level) of
     * processing modules within this composition.
     *
     * @return the data sink
     */
    public Node getSink() {
        return _Sink;
    }

    @Override
    public boolean visit(Visitor visitor) {
        return visitor.visitCompositeNode(this);
    }

}
