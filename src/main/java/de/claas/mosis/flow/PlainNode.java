package de.claas.mosis.flow;

import de.claas.mosis.model.Processor;

/**
 * The class {@link PlainNode}. It an implementation of the {@link Node} class.
 * This class intended to represent a node that is based around a single
 * {@link Processor}. This stands in contrast to the {@link CompositeNode},
 * which represents a multiple {@link Processor} instances at once.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class PlainNode extends Node {

    private final Processor<?, ?> _Processor;

    /**
     * Initializes the class with the given parameter.
     * 
     * @param p
     *            the {@link Processor} of this node. See
     *            {@link #getProcessor()} for details.
     */
    public PlainNode(Processor<?, ?> p) {
	if(p == null) {
	    throw new IllegalArgumentException("processor may not be null");
	}
	_Processor = p;
    }

    /**
     * Returns the {@link Processor} of this node. This type of {@link Node} is
     * defined by the {@link Processor} that it represents.
     * 
     * @return the {@link Processor} of this node
     */
    public Processor<?, ?> getProcessor() {
	return _Processor;
    }

    @Override
    public boolean visit(Visitor visitor) {
	return visitor.visitPlainNode(this);
    }

}
