package de.claas.mosis.flow;

import de.claas.mosis.model.Processor;
import de.claas.mosis.processing.debug.Null;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link Graph}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class GraphTest {

    /**
     * Returns an instantiated {@link Graph} class. If appropriate, the instance
     * is configured with default values as well as the given parameters.
     *
     * @return an instantiated {@link Graph} class
     */
    private Graph build() {
        return new Graph();
    }

    @Test
    public void shouldNotHaveSources() {
        Graph g = build();
        assertTrue(g.getSources().isEmpty());
    }

    @Test
    public void shouldMaintainSources() {
        Graph g = build();
        Processor<?, ?> p1 = new Null();
        Processor<?, ?> p2 = new Null();
        Processor<?, ?> p3 = new Null();
        g.addLink(p1, p2);
        assertEquals(1, g.getSources().size());
        assertTrue(g.getSources().contains(g.getNode(p1)));

        g.addLink(p2, p3);
        assertEquals(1, g.getSources().size());
        assertTrue(g.getSources().contains(g.getNode(p1)));

        g.removeLink(p1, p2);
        assertEquals(1, g.getSources().size());
        assertTrue(g.getSources().contains(g.getNode(p2)));

        g.addLink(p1, p3);
        assertEquals(2, g.getSources().size());
        assertTrue(g.getSources().contains(g.getNode(p1)));
        assertTrue(g.getSources().contains(g.getNode(p2)));
    }

    @Test
    public void shouldManagePredecessorsAndSuccessors() {
        Graph g = build();
        Processor<?, ?> p1 = new Null();
        Processor<?, ?> p2 = new Null();
        g.addLink(p1, p2);
        Node n1 = g.getNode(p1);
        Node n2 = g.getNode(p2);
        assertTrue(n1.hasSuccessors());
        assertFalse(n1.hasPredecessors());
        assertFalse(n2.hasSuccessors());
        assertTrue(n2.hasPredecessors());
        assertTrue(n1.getSuccessors().contains(n2));

        g.removeLink(p1, p2);
        assertFalse(n1.hasSuccessors());
        assertFalse(n1.hasPredecessors());
        assertFalse(n2.hasSuccessors());
        assertFalse(n2.hasPredecessors());
    }

}
