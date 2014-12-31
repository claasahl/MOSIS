package de.claas.mosis.flow.visitor;

import de.claas.mosis.flow.CompositeNode;
import de.claas.mosis.flow.PlainNode;
import de.claas.mosis.flow.Visitor;
import de.claas.mosis.model.Processor;

import java.util.HashSet;
import java.util.Set;

/**
 * The class {@link DismantlingVisitor}. It is an implementation of the
 * {@link Visitor} interface. It is intended to shutdown / clean up all
 * processing modules within a graph.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class DismantlingVisitor implements Visitor {

    private final Set<Processor> dismantled = new HashSet<>();

    @Override
    public boolean visitPlainNode(PlainNode node) {
        Processor p = node.getProcessor();
        if (!dismantled.contains(p)) {
            p.dismantle();
            dismantled.add(p);
        }
        return true;
    }

    @Override
    public boolean visitCompositeNode(CompositeNode node) {
        // TODO Auto-generated method stub
        return false;
    }

}
