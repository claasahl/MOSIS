package de.claas.mosis.flow;

import de.claas.mosis.flow.iterator.OneShotLevelOrder;
import de.claas.mosis.flow.visitor.DismantlingVisitor;
import de.claas.mosis.flow.visitor.ProcessingVisitor;
import de.claas.mosis.flow.visitor.SettingUpVisitor;
import de.claas.mosis.model.Processor;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * The class {@link de.claas.mosis.flow.Graph}. It is intended to provide a
 * simple way of managing and arranging {@link de.claas.mosis.model.Processor}
 * modules within a graph. This class's main functionalities are to link {@link
 * de.claas.mosis.model.Processor} modules together and provide access to the
 * data sources (i.e. roots) of the graph.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class Graph {

    // TODO provide option to choose appropriate type of Link
    // TODO provide option to link two node objects (e.g. CompositeNode, PlainNode)

    private final Map<Processor<?, ?>, Node> _Nodes;
    private final Set<Node> _Sources;

    /**
     * Initializes the class with default values.
     */
    public Graph() {
        _Nodes = new HashMap<>();
        _Sources = new HashSet<>();
    }

    /**
     * Adds a directional link between both processors.
     *
     * @param src the source
     * @param dst the destination
     */
    public void addLink(Processor<?, ?> src, Processor<?, ?> dst) {
        Node s = getNode(src);
        Node d = getNode(dst);
        s.addSuccessor(d, new UnbiasedLink());

        _Sources.remove(s);
        _Sources.remove(d);
        if (!s.hasPredecessors() && s.hasSuccessors()) {
            _Sources.add(s);
        }
    }

    /**
     * Removes a directional link between both processors.
     *
     * @param src the source
     * @param dst the destination
     */
    public void removeLink(Processor<?, ?> src, Processor<?, ?> dst) {
        Node s = getNode(src);
        Node d = getNode(dst);
        s.removeSuccessor(d);

        _Sources.remove(s);
        _Sources.remove(d);
        if (!s.hasPredecessors() && s.hasSuccessors()) {
            _Sources.add(s);
        }
        if (!d.hasPredecessors() && d.hasSuccessors()) {
            _Sources.add(d);
        }
    }

    /**
     * Returns the corresponding {@link Node} for this {@link Processor}.
     *
     * @param p the {@link de.claas.mosis.model.Processor}
     * @return the {@link de.claas.mosis.flow.Node}
     */
    protected Node getNode(Processor<?, ?> p) {
        Node n = _Nodes.get(p);
        if (n == null) {
            n = new PlainNode(p);
            _Nodes.put(p, n);
        }
        return n;
    }

    /**
     * Returns all roots of this graph (i.e. nodes without predecessors).
     *
     * @return all roots of this graph
     */
    public Set<Node> getSources() {
        return _Sources;
    }

    /**
     * Returns an instantiated {@link java.lang.Iterable} object of the given
     * {@link java.lang.Class} for this graph. This is a convenience method and
     * turns the {@link java.util.Iterator} object into an {@link
     * java.lang.Iterable} object.
     *
     * @param clazz the {@link java.lang.Iterable} class that should be
     *              instantiated
     * @return an instantiated {@link java.lang.Iterable} object of the given
     * {@link Class} for this graph
     * @throws java.lang.ReflectiveOperationException See {@link #iterator(Class)}
     *                                                for details.
     * @see #iterator(Class)
     */
    public Iterable<Node> iterable(Class<? extends Iterator<Node>> clazz)
            throws ReflectiveOperationException {
        final Iterator<Node> iterator = iterator(clazz);
        return new Iterable<Node>() {

            @Override
            public Iterator<Node> iterator() {
                return iterator;
            }
        };
    }

    /**
     * Returns an instantiated {@link java.util.Iterator} object of the given
     * {@link java.lang.Class} for this graph. It is assumed that the {@link
     * java.util.Iterator} class has a constructor that accepts a {@link
     * java.util.Set} of {@link de.claas.mosis.flow.Node} objects.
     *
     * @param clazz the {@link java.util.Iterator} class that should be
     *              instantiated
     * @return an instantiated {@link java.util.Iterator} object of the given
     * {@link java.lang.Class} for this graph
     * @throws ReflectiveOperationException If the {@link java.util.Iterator}
     *                                      class could not be properly
     *                                      instantiated.
     */
    public Iterator<Node> iterator(Class<? extends Iterator<Node>> clazz)
            throws ReflectiveOperationException {
        Constructor<? extends Iterator<Node>> constructor = clazz
                .getConstructor(Set.class);
        return constructor.newInstance(getSources());
    }

    /**
     * Initializes processing modules and starts data processing. The order of
     * processing modules is determined by the given {@link java.util.Iterator}
     * class. If the data source is no longer providing data or there are no
     * more processing modules that need processing, then processing modules are
     * also dismantled.
     *
     * @param clazz the {@link java.util.Iterator} class
     * @throws java.lang.ReflectiveOperationException See {@link #iterator(Class)}
     *                                                for details.
     * @see #iterator(Class)
     */
    public void process(Class<? extends Iterator<Node>> clazz)
            throws ReflectiveOperationException {
        visit(new SettingUpVisitor(), iterable(OneShotLevelOrder.class));
        visit(new ProcessingVisitor(), iterable(clazz));
        visit(new DismantlingVisitor(), iterable(OneShotLevelOrder.class));
    }

    private void visit(Visitor visitor, Iterable<Node> iterator)
            throws ReflectiveOperationException {
        for (Node node : iterator) {
            if (!node.visit(visitor)) {
                break;
            }
        }
    }

}
