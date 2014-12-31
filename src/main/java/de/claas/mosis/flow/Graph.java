package de.claas.mosis.flow;

import de.claas.mosis.flow.iterator.OneShotLevelOrder;
import de.claas.mosis.flow.visitor.DismantlingVisitor;
import de.claas.mosis.flow.visitor.ProcessingVisitor;
import de.claas.mosis.flow.visitor.SettingUpVisitor;
import de.claas.mosis.model.Processor;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * The class {@link Graph}. It is intended to provide a simple way of managing
 * and arranging {@link Processor} modules within a graph. This class's main
 * functionalities are to link {@link Processor} modules together and provide
 * access to the data sources (i.e. roots) of the graph.
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
     * @param p the {@link Processor}
     * @return the {@link Node}
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
     * Returns an instantiated {@link Iterable} object of the given
     * {@link Class} for this graph. This is a convenience method and turns the
     * {@link Iterator} object into an {@link Iterable} object.
     *
     * @param clazz the {@link Iterable} class that should be instantiated
     * @return an instantiated {@link Iterable} object of the given
     * {@link Class} for this graph
     * @throws ReflectiveOperationException See {@link #iterator(Class)} for details.
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
     * Returns an instantiated {@link Iterator} object of the given
     * {@link Class} for this graph. It is assumed that the {@link Iterator}
     * class has a constructor that accepts a {@link Set} of {@link Node}
     * objects.
     *
     * @param clazz the {@link Iterator} class that should be instantiated
     * @return an instantiated {@link Iterator} object of the given
     * {@link Class} for this graph
     * @throws ReflectiveOperationException If the {@link Iterator} class could not be properly
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
     * processing modules is determined by the given {@link Iterator} class. If
     * the data source is no longer providing data or there are no more
     * processing modules that need processing, then processing modules are also
     * dismantled.
     *
     * @param clazz the {@link Iterator} class
     * @throws ReflectiveOperationException
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
