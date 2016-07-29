package de.claas.mosis.flow.visitor;

import de.claas.mosis.flow.CompositeNode;
import de.claas.mosis.flow.PlainNode;
import de.claas.mosis.flow.Visitor;
import de.claas.mosis.flow.iterator.OneShotLevelOrder;
import de.claas.mosis.model.Processor;

import java.util.HashSet;
import java.util.Set;

/**
 * The class {@link de.claas.mosis.flow.visitor.SettingUpVisitor}. It is an
 * implementation of the {@link de.claas.mosis.flow.Visitor} interface. It is
 * intended to initialize all processing modules within a graph.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class SettingUpVisitor implements Visitor {

    private final Set<Processor<?, ?>> setup = new HashSet<>();

    @Override
    public boolean visitPlainNode(PlainNode node) {
        Processor<?, ?> p = node.getProcessor();
        if (!setup.contains(p)) {
            p.setUp();
            setup.add(p);
        }
        return true;
    }

    @Override
    public boolean visitCompositeNode(CompositeNode node) {
        OneShotLevelOrder levelOrder = new OneShotLevelOrder(node.getSources());
        while (levelOrder.hasNext()) {
            if (!levelOrder.next().visit(this)) {
                break;
            }
        }
        return true;
    }

}
