package de.claas.mosis.flow.iterator;

import de.claas.mosis.flow.Node;

import java.util.*;

/**
 * The class {@link de.claas.mosis.flow.iterator.RepeatedOneShotLevelOrder}. It
 * is intended to provide sequential access to all {@link
 * de.claas.mosis.flow.Node} objects in a {@link de.claas.mosis.flow.Graph}.
 * This {@link java.util.Iterator} is meant for a repeated iteration across all
 * objects in a {@link de.claas.mosis.flow.Graph}. In contrast to {@link
 * de.claas.mosis.flow.iterator.OneShotLevelOrder}, where a single iteration
 * across all {@link de.claas.mosis.flow.Node}s is performed, this process is
 * repeated.
 * <p>
 * <ol> <li>iteration: level 0 (data sources)</li> <li>iteration: level 1</li>
 * <li>iteration: level 2</li> <li>...</li> <li>iteration: level 0 (data
 * sources)</li> <li>iteration: level 1</li> <li>iteration: level 2</li>
 * <li>...</li> <li>iteration: level 0 (data sources)</li> <li>iteration: level
 * 1</li> <li>iteration: level 2</li> <li>...</li> </ol>
 *
 * @author Claas Ahlrichs (c.ahlrichs@neusta.de)
 */
public class RepeatedOneShotLevelOrder implements Iterator<Node> {

    private final Set<Node> _Visited;
    private final List<Node> _Nodes;
    private int _Index;

    /**
     * Initializes the class with the given parameter.
     *
     * @param sources the data sources to start with
     */
    public RepeatedOneShotLevelOrder(Set<Node> sources) {
        _Visited = new HashSet<>(sources);
        _Nodes = new Vector<>();
        _Index = 0;
        for (Node src : sources) {
            _Nodes.add(src);
        }
        populate(0);
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Node next() {
        if (_Index >= _Nodes.size()) {
            _Index = 0;
        }
        return _Nodes.get(_Index++);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    private void populate(int index) {
        if (index < 0 || index >= _Nodes.size()) {
            return;
        }

        for (Node successor : _Nodes.get(index).getSuccessors()) {
            if (!_Visited.contains(successor)) {
                _Visited.add(successor);
                _Nodes.add(successor);
            }
        }
        populate(index + 1);
    }

}
