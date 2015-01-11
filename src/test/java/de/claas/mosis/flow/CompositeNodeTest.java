package de.claas.mosis.flow;

import de.claas.mosis.processing.debug.NoOperation;
import de.claas.mosis.processing.debug.Null;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit test for class {@link de.claas.mosis.flow.CompositeNode}. It is
 * intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class CompositeNodeTest extends NodeTest {

    private Node _Source;
    private Node _Sink;

    @Override
    protected CompositeNode build() {
        _Source = new PlainNode(new Null());
        _Sink = new PlainNode(new NoOperation());
        _Source.addSuccessor(_Sink, new UnbiasedLink());
        HashSet<Node> sources = new HashSet<>();
        sources.add(_Source);
        return new CompositeNode(sources, _Sink);
    }

    @Test
    public void shouldHaveDataSource() {
        CompositeNode n = build();
        assertTrue(n.getSources().contains(_Source));
    }

    @Test
    public void shouldHaveDataSink() {
        CompositeNode n = build();
        assertEquals(_Sink, n.getSink());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowNullValues() {
        new CompositeNode(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotContainNullValues() {
        HashSet<Node> tmp = new HashSet<>();
        tmp.add(null);
        new CompositeNode(tmp, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotHavePredecessors() {
        Node n1 = build();
        Node n2 = build();
        n1.addSuccessor(n2, new UnbiasedLink());
        Set<Node> sources = new HashSet<>();
        sources.add(n2);
        new CompositeNode(sources, n2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotHaveSuccessors() {
        Node n1 = build();
        Node n2 = build();
        n1.addSuccessor(n2, new UnbiasedLink());
        Set<Node> sources = new HashSet<>();
        sources.add(n1);
        new CompositeNode(sources, n1);
    }

}
