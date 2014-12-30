package de.claas.mosis.flow.iterator;

import de.claas.mosis.flow.Graph;
import de.claas.mosis.flow.Node;

import java.util.*;

/**
 * The class {@link RepeatedOneShotLevelOrder}. It is intended to provide
 * sequential access to all {@link Node} objects in a {@link Graph}. This
 * {@link Iterator} is meant for a repeated iteration across all objects in a
 * {@link Graph}. In contrast to {@link OneShotLevelOrder}, where a single
 * iteration across all {@link Node}s is performed, this process is repeated.
 * <p/>
 * <ol>
 * <li>iteration: level 0 (data sources)</li>
 * <li>iteration: level 1</li>
 * <li>iteration: level 2</li>
 * <li>...</li>
 * <li>iteration: level 0 (data sources)</li>
 * <li>iteration: level 1</li>
 * <li>iteration: level 2</li>
 * <li>...</li>
 * <li>iteration: level 0 (data sources)</li>
 * <li>iteration: level 1</li>
 * <li>iteration: level 2</li>
 * <li>...</li>
 * </ol>
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

        for (Node succ : _Nodes.get(index).getSuccessors()) {
            if (!_Visited.contains(succ)) {
                _Visited.add(succ);
                _Nodes.add(succ);
            }
        }
        populate(index + 1);
    }

}
