package de.claas.mosis.flow;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * The class {@link de.claas.mosis.flow.Node}. It is intended to represent the
 * general concept of a node within a {@link de.claas.mosis.flow.Graph}. This
 * class manages all inbound and outbound links to / from other nodes. Concrete
 * realizations may choose to represent either a single or multiple {@link
 * de.claas.mosis.model.Processor} objects.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public abstract class Node {
    private final Map<Node, Link> _Predecessors;
    private final Map<Node, Link> _Successors;
    private final Collection<Node> _P;
    private final Collection<Node> _S;

    /**
     * Initializes the class with default values.
     */
    public Node() {
        _Predecessors = new HashMap<>();
        _Successors = new HashMap<>();
        _P = new Vector<>();
        _S = new Vector<>();
    }

    /**
     * Adds a successor to this node with the given type of {@link
     * de.claas.mosis.flow.Link}. This also updates the predecessor list of the
     * given node.
     *
     * @param successor successor to add
     * @param link      link to use
     */
    public void addSuccessor(Node successor, Link link) {
        _Successors.put(successor, link);
        successor._Predecessors.put(this, link);
        _S.add(successor);
        successor._P.add(this);
    }

    /**
     * Returns <code>true</code>, if the given successor was successfully
     * removed. Otherwise, <code>false</code> is returned.
     *
     * @param successor successor to remove
     * @return <code>true</code>, if the given successor was successfully
     * removed
     */
    public boolean removeSuccessor(Node successor) {
        return successor._Predecessors.remove(this) != null
                && _Successors.remove(successor) != null
                && successor._P.remove(this) && _S.remove(successor);
    }

    /**
     * Returns <code>true</code>, if this node has predecessors. Otherwise,
     * <code>false</code> is returned.
     *
     * @return <code>true</code>, if this node has predecessors
     */
    public boolean hasPredecessors() {
        return !_Predecessors.isEmpty();
    }

    /**
     * Returns <code>true</code>, if this node has successors. Otherwise,
     * <code>false</code> is returned.
     *
     * @return <code>true</code>, if this node has successors
     */
    public boolean hasSuccessors() {
        return !_Successors.isEmpty();
    }

    /**
     * Returns <code>true</code>, if this node has inbound data. Otherwise,
     * <code>false</code> is returned. Inbound data is present, when at least
     * one inbound link contains data. Data sources are considered to always
     * have inbound data.
     *
     * @return <code>true</code>, if this node has inbound data
     */
    public boolean hasInboundData() {
        boolean inboundData = false;
        for (Node predecessor : _Predecessors.keySet()) {
            inboundData |= !getInboundLink(predecessor).isEmpty();
        }
        return inboundData;
    }

    /**
     * Returns <code>true</code>, if this node has outbound data. Otherwise,
     * <code>false</code> is returned. Outbound data is present, when at least
     * one outbound link (still) contains data.
     *
     * @return <code>true</code>, if this node has outbound data
     */
    public boolean hasOutboundData() {
        boolean outboundData = false;
        for (Node successor : _Successors.keySet()) {
            outboundData |= !getOutboundLink(successor).isEmpty();
        }
        return outboundData;
    }

    /**
     * Returns all successor nodes.
     *
     * @return all successor nodes
     */
    public Collection<Node> getSuccessors() {
        return _S;
    }

    /**
     * Returns all predecessor nodes.
     *
     * @return all predecessor nodes
     */
    public Collection<Node> getPredecessors() {
        return _P;
    }

    /**
     * Returns the corresponding {@link de.claas.mosis.flow.Link} for given
     * {@link de.claas.mosis.flow.Node}. The returned link serves input data, if
     * present.
     *
     * @param node the node
     * @return the corresponding {@link de.claas.mosis.flow.Link} for given
     * {@link de.claas.mosis.flow.Node}
     */
    public Link getInboundLink(Node node) {
        return _Predecessors.get(node);
    }

    /**
     * Return the corresponding {@link de.claas.mosis.flow.Link} for given
     * {@link de.claas.mosis.flow.Node}. The returned link receives output data,
     * if present.
     *
     * @param node the node
     * @return the corresponding {@link de.claas.mosis.flow.Link} for given
     * {@link de.claas.mosis.flow.Node}
     */
    public Link getOutboundLink(Node node) {
        return _Successors.get(node);
    }

    /**
     * Returns <code>true</code>, if the next {@link de.claas.mosis.flow.Node}
     * may be visited (by the given {@link de.claas.mosis.flow.Visitor}).
     * Otherwise <code>false</code> is returned.
     *
     * @param visitor the visitor
     * @return <code>true</code>, if the next {@link de.claas.mosis.flow.Node}
     * may be visited
     */
    public abstract boolean visit(Visitor visitor);

}
