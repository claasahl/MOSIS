package de.claas.mosis.flow.iterator;

import de.claas.mosis.flow.Node;

import java.util.*;


/**
 * The class {@link de.claas.mosis.flow.iterator.OneShotLevelOrder}. It is
 * intended to provide sequential access to all {@link de.claas.mosis.flow.Node}
 * objects in a {@link de.claas.mosis.flow.Graph}. This {@link
 * java.util.Iterator} is meant for a single iteration across all objects in a
 * {@link de.claas.mosis.flow.Graph}. The objects are returned based on their
 * shortest distance to a root (or data source). Data sources are returned first
 * (i.e. level 0), then their successors (i.e. level 1), then the successors'
 * successors (i.e. level 2), and so on (i.e. level 3,4,...). This is repeated
 * until the deepest {@link de.claas.mosis.flow.Node} ({@link
 * de.claas.mosis.flow.Node} with longest distance to a root) was returned.
 * There are no ordering constraints for {@link de.claas.mosis.flow.Node}
 * objects that have the same depth. They are essentially randomly returned.
 * <p>
 * <ol> <li>iteration: level 0 (data sources)</li> <li>iteration: level 1</li>
 * <li>iteration: level 2</li> <li>iteration: level 3</li> <li>...</li> </ol>
 *
 * @author Claas Ahlrichs (c.ahlrichs@neusta.de)
 */
public class OneShotLevelOrder implements Iterator<Node> {

    private final Set<Node> _Visited;
    private final Queue<Node> _Nodes;

    /**
     * Initializes the class with the given parameter.
     *
     * @param sources the data sources to start with
     */
    public OneShotLevelOrder(Set<Node> sources) {
        _Visited = new HashSet<>(sources);
        _Nodes = new LinkedList<>();
        for (Node src : sources) {
            _Nodes.add(src);
        }
    }

    @Override
    public boolean hasNext() {
        return !_Nodes.isEmpty();
    }

    @Override
    public Node next() {
        Node next = _Nodes.poll();
        for (Node successor : next.getSuccessors()) {
            if (!_Visited.contains(successor)) {
                _Visited.add(successor);
                _Nodes.add(successor);
            }
        }
        return next;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
