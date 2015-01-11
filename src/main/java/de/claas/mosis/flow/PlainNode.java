package de.claas.mosis.flow;

import de.claas.mosis.model.Processor;

/**
 * The class {@link de.claas.mosis.flow.PlainNode}. It an implementation of the
 * {@link de.claas.mosis.flow.Node} class. This class intended to represent a
 * node that is based around a single {@link de.claas.mosis.model.Processor}.
 * This stands in contrast to the {@link de.claas.mosis.flow.CompositeNode},
 * which represents a multiple {@link de.claas.mosis.model.Processor} instances
 * at once.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class PlainNode extends Node {

    private final Processor<?, ?> _Processor;

    /**
     * Initializes the class with the given parameter.
     *
     * @param p the {@link de.claas.mosis.model.Processor} of this node. See
     *          {@link #getProcessor()} for details.
     */
    public PlainNode(Processor<?, ?> p) {
        if (p == null) {
            throw new IllegalArgumentException("processor may not be null");
        }
        _Processor = p;
    }

    /**
     * Returns the {@link de.claas.mosis.model.Processor} of this node. This
     * type of {@link de.claas.mosis.flow.Node} is defined by the {@link
     * de.claas.mosis.model.Processor} that it represents.
     *
     * @return the {@link de.claas.mosis.model.Processor} of this node
     */
    public Processor<?, ?> getProcessor() {
        return _Processor;
    }

    @Override
    public boolean visit(Visitor visitor) {
        return visitor.visitPlainNode(this);
    }

}
