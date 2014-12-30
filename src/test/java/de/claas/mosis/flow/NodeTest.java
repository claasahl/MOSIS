package de.claas.mosis.flow;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link Node}. It is intended to collect and document
 * a set of test cases for the tested class. Please refer to the individual
 * tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public abstract class NodeTest {

    /**
     * Returns an instantiated {@link Node} class. If appropriate, the instance
     * is configured with default values.
     *
     * @return an instantiated {@link Node} class
     */
    protected abstract Node build();

    @Test
    public void shouldHaveSuccessor() {
        Node n1 = build();
        Node n2 = build();
        n1.addSuccessor(n2, new UnbiasedLink());
        assertTrue(n1.hasSuccessors());
        assertFalse(n2.hasSuccessors());
    }

    @Test
    public void shouldNotHaveSuccessor() {
        Node n = build();
        assertFalse(n.hasSuccessors());
    }

    @Test
    public void shouldHavePredecessor() {
        Node n1 = build();
        Node n2 = build();
        n1.addSuccessor(n2, new UnbiasedLink());
        assertTrue(n2.hasPredecessors());
        assertFalse(n1.hasPredecessors());
    }

    @Test
    public void shouldNotHavePredecessor() {
        Node n = build();
        assertFalse(n.hasPredecessors());
        assertTrue(n.getSuccessors().isEmpty());
    }

    @Test
    public void shouldHaveInboundData() {
        Node n1 = build();
        Node n2 = build();
        n1.addSuccessor(n2, new UnbiasedLink());
        n2.getInboundLink(n1).push(Arrays.<Object>asList("hello", "world"));
        assertTrue(n2.hasInboundData());
        assertFalse(n2.hasOutboundData());
    }

    @Test
    public void shouldNotHaveInboundData() {
        Node n = build();
        assertFalse(n.hasInboundData());
    }

    @Test
    public void shouldHaveOutboundData() {
        Node n1 = build();
        Node n2 = build();
        n1.addSuccessor(n2, new UnbiasedLink());
        n2.getInboundLink(n1).push(Arrays.<Object>asList("hello", "world"));
        assertFalse(n1.hasInboundData());
        assertTrue(n1.hasOutboundData());
    }

    @Test
    public void shouldNotHaveOutboundData() {
        Node n = build();
        assertFalse(n.hasOutboundData());
    }

    @Test
    public void shouldHaveLink() {
        Node n1 = build();
        Node n2 = build();
        Link l = new UnbiasedLink();
        n1.addSuccessor(n2, l);
        assertEquals(l, n1.getOutboundLink(n2));
        assertEquals(l, n2.getInboundLink(n1));
    }

    @Test
    public void shouldManageSuccessorsAndPredecessors() {
        Node n1 = build();
        Node n2 = build();
        Node n3 = build();
        n1.addSuccessor(n2, new UnbiasedLink());
        n1.addSuccessor(n3, new UnbiasedLink());
        assertFalse(n1.hasPredecessors());
        assertTrue(n1.hasSuccessors());
        assertTrue(n1.getSuccessors().contains(n2));
        assertTrue(n1.getSuccessors().contains(n3));
        assertTrue(n2.hasPredecessors());
        assertFalse(n2.hasSuccessors());
        assertTrue(n2.getPredecessors().contains(n1));
        assertTrue(n3.hasPredecessors());
        assertFalse(n3.hasSuccessors());
        assertTrue(n3.getPredecessors().contains(n1));

        n1.removeSuccessor(n2);
        assertFalse(n1.hasPredecessors());
        assertTrue(n1.hasSuccessors());
        assertTrue(n1.getSuccessors().contains(n3));
        assertFalse(n2.hasPredecessors());
        assertFalse(n2.hasSuccessors());
        assertTrue(n3.hasPredecessors());
        assertFalse(n3.hasSuccessors());
        assertTrue(n3.getPredecessors().contains(n1));

        n1.removeSuccessor(n3);
        assertFalse(n1.hasPredecessors());
        assertFalse(n1.hasSuccessors());
        assertFalse(n2.hasPredecessors());
        assertFalse(n2.hasSuccessors());
        assertFalse(n3.hasPredecessors());
        assertFalse(n3.hasSuccessors());
    }

}
