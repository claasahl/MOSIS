package de.claas.mosis.flow.iterator;

import de.claas.mosis.flow.Node;
import de.claas.mosis.flow.PlainNode;
import de.claas.mosis.io.generator.Linear;
import de.claas.mosis.processing.debug.Null;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The (abstract) JUnit test for class {@link java.util.Iterator} implementation
 * within the framework. It is intended to collect and document a set of test
 * cases for the tested class. Please refer to the individual tests for more
 * detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public abstract class IteratorTest {

    protected Node _N1;
    protected Node _N2;
    protected Node _N3;
    protected Node _N4;

    /**
     * Returns an instantiated {@link Iterator} class. If appropriate, the
     * instance is configured with default values as well as the given
     * parameters.
     *
     * @param sources the sources
     * @return an instantiated {@link Iterator} class
     */
    public abstract Iterator<Node> build(Collection<Node> sources);

    @Before
    public void before() {
        _N1 = new PlainNode(new Linear());
        _N2 = new PlainNode(new Null());
        _N3 = new PlainNode(new Null());
        _N4 = new PlainNode(new Null());
    }

    @Test
    public abstract void shouldHandleMultipleInputs();

    @Test
    public abstract void shouldHandleMultipleOutputs();

    @Test
    public abstract void shouldHandleLoops();

    @Test
    public abstract void shouldIterateEachLevel();

    /**
     * A helper method to avoid code duplicates. The method iterates as long as
     * possible and compare the returned {@link Node} instances to those
     * instances that are expected in the particular level / at a particular
     * depth from the root {@link Node}.
     *
     * @param iterator the {@link Iterator}
     * @param level    the levels in which the {@link Node}s are expected to be
     *                 iterated over
     * @param hasNext  whether the iterator is expected to return more values
     *                 than listed in the levels
     */
    protected void compare(Iterator<Node> iterator, List<List<Node>> level,
                           boolean hasNext) {
        for (; iterator.hasNext() && !level.isEmpty(); ) {
            assertTrue(level.get(0).remove(iterator.next()));
            if (level.get(0).isEmpty()) {
                level.remove(0);
            }
        }
        assertEquals(hasNext, iterator.hasNext());
    }

}
