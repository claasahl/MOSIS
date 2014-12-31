package de.claas.mosis.flow.visitor;

import de.claas.mosis.flow.*;
import de.claas.mosis.flow.iterator.OneShotLevelOrder;
import de.claas.mosis.model.Processor;

import java.util.List;
import java.util.Vector;

/**
 * The class {@link ProcessingVisitor}. It is an implementation of the
 * {@link Visitor} interface. It is intended to invoke the processing
 * capabilities of the underlying {@link Processor} modules for all nodes within
 * a graph. This implementation also takes care of forwarding output data to the
 * corresponding successors.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class ProcessingVisitor implements Visitor {

    private final List<List<Object>> _In = new Vector<>();
    private final List<Object> _Out = new Vector<>();
    private int level = 0;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public boolean visitPlainNode(PlainNode node) {
        List<Object> in = getInputs();
        _Out.clear();
        boolean inboundData = !node.hasPredecessors() || node.hasInboundData();
        while (inboundData) {
            inboundData = fetchInputs(node, in);
            ((Processor) node.getProcessor()).process(in, _Out);
            distributeOutputs(node, _Out);
            in.clear();
        }
        return (!node.hasPredecessors() && !_Out.isEmpty()) || node.hasPredecessors();
    }

    @Override
    public boolean visitCompositeNode(CompositeNode node) {
        List<Object> in = getInputs();
        _Out.clear();
        level++;

        boolean inboundData = !node.hasPredecessors() || node.hasInboundData();
        while (inboundData) {
            inboundData = fetchInputs(node, in);
            OneShotLevelOrder iterator = new OneShotLevelOrder(
                    node.getSources());
            while (iterator.hasNext()) {
                Node n = iterator.next();
                if (node.getSources().contains(n)) {
                    _In.add(in);
                }
                n.visit(this);
            }
            distributeOutputs(node, _Out);
        }
        level--;
        return true;
    }

    private List<Object> getInputs() {
        while (_In.size() <= level) {
            _In.add(new Vector<>());
        }
        return _In.get(level);
    }

    /**
     * Fetches input values from all predecessor nodes of a given node. Returns
     * an indicator of whether there are still more input values to be fetched.
     *
     * @param node the node
     * @param in   the input values
     * @return an indicator of whether there are still more input values to be
     * fetched
     */
    private boolean fetchInputs(Node node, List<Object> in) {
        boolean inboundData = false;
        for (Node predecessor : node.getPredecessors()) {
            Link link = node.getInboundLink(predecessor);
            if (!link.isEmpty()) {
                in.add(link.poll());
            }
            inboundData |= !link.isEmpty() && predecessor != node;
        }
        return inboundData;
    }

    /**
     * Distributes output values to all successor nodes of a given node.
     *
     * @param node the node
     * @param out  the output values
     */
    private void distributeOutputs(Node node, List<Object> out) {
        for (Node successor : node.getSuccessors()) {
            node.getOutboundLink(successor).push(out);
        }
    }

}
