package de.claas.mosis.flow.iterator;

import de.claas.mosis.flow.Node;

import java.util.*;


/**
 * The class {@link de.claas.mosis.flow.iterator.InfiniteLevelOrder}. It is
 * intended to provide sequential access to all {@link de.claas.mosis.flow.Node}
 * objects in a {@link de.claas.mosis.flow.Graph}. This {@link
 * java.util.Iterator} is meant for a repeated iteration (i.e. never ending)
 * across all objects in a {@link de.claas.mosis.flow.Graph}. Just like {@link
 * de.claas.mosis.flow.iterator.OneShotLevelOrder}, the objects are returned
 * based on their shortest distance to a root (or data source). First only data
 * sources are returned (i.e. level 0). Afterwards, their successors are
 * returned (i.e. level 1) as well as the data sources (i.e. level 0). Thus not
 * only objects with a particular depth (objects on the same level) are
 * returned, but instead all objects up to a certain depth are returned (e.g.
 * objects from level 0 to level 3). There are no ordering constraints for
 * {@link de.claas.mosis.flow.Node} objects that have the same depth. They are
 * essentially randomly returned.
 * <p>
 * <ol> <li>iteration: level 0 (data sources)</li> <li>iteration: level 1 and
 * level 0</li> <li>iteration: level 2, level 1 and level 0</li> <li>iteration:
 * level 3, level 2, level 1 and level 0</li> <li>...</li> </ol>
 *
 * @author Claas Ahlrichs (c.ahlrichs@neusta.de)
 */
public class InfiniteLevelOrder implements Iterator<Node> {

    private final Set<Node> _Visited;
    private final List<Node> _Nodes;
    private final List<List<Node>> _Levels;
    private int _Level;
    private int _Index;

    /**
     * Initializes the class with the given parameter.
     *
     * @param sources the data sources to start with
     */
    public InfiniteLevelOrder(Set<Node> sources) {
        _Visited = new HashSet<>();
        _Nodes = new LinkedList<>();
        _Levels = new LinkedList<>();
        _Level = 0;
        _Index = 0;
        initLevels(0, sources);
    }

    private void initLevels(int level, Collection<Node> nodes) {
        for (Node node : nodes) {
            if (!_Visited.contains(node)) {
                getNodes(level).add(node);
                _Visited.add(node);
                initLevels(level + 1, node.getSuccessors());
            }
        }
    }

    private List<Node> getNodes(int level) {
        if (level >= _Levels.size()) {
            _Levels.add(level, new LinkedList<Node>());
        }
        return _Levels.get(level);
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Node next() {
        if (_Index >= _Nodes.size()) {
            _Index = 0;
            if (_Level < _Levels.size()) {
                _Nodes.addAll(0, getNodes(_Level++));
            }
        }
        return _Nodes.get(_Index++);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
