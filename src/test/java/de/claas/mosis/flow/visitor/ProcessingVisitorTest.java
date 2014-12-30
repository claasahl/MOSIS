package de.claas.mosis.flow.visitor;

import de.claas.mosis.flow.*;
import de.claas.mosis.io.generator.Linear;
import de.claas.mosis.processing.debug.BreakOut;
import de.claas.mosis.processing.debug.Forward;
import de.claas.mosis.processing.debug.Null;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link ProcessingVisitor}. It is intended to collect
 * and document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class ProcessingVisitorTest {

    private ProcessingVisitor _V;
    private PlainNode _P1, _P2;
    private CompositeNode _C;
    private BreakOut _B1, _B21, _B22, _B3;

    @Before
    public void before() {
        _B1 = new BreakOut();
        _B1.setParameter(BreakOut.CLASS, Linear.class.getName());
        _B21 = new BreakOut();
        _B21.setParameter(BreakOut.CLASS, Linear.class.getName());
        _B22 = new BreakOut();
        _B22.setParameter(BreakOut.CLASS, Forward.class.getName());
        _B3 = new BreakOut();
        _B3.setParameter(BreakOut.CLASS, Null.class.getName());

        _V = new ProcessingVisitor();
        _P1 = new PlainNode(_B1);
        _P2 = new PlainNode(_B3);
        Node tmp1 = new PlainNode(_B21);
        Node tmp2 = new PlainNode(_B22);
        tmp1.addSuccessor(tmp2, new UnbiasedLink());
        Set<Node> tmp = new HashSet<>();
        tmp.add(tmp1);
        _C = new CompositeNode(tmp, tmp2);
        _P1.addSuccessor(_C, new UnbiasedLink());
        _C.addSuccessor(_P2, new UnbiasedLink());
    }

    @Test
    public void shouldNotHaveBeenCalled() {
        assertEquals(0, _B1.getCalls());
        assertEquals(0, _B21.getCalls());
        assertEquals(0, _B22.getCalls());
        assertEquals(0, _B3.getCalls());
    }

    @Test
    public void shouldBeEmpty() {
        assertTrue(_P1.getOutboundLink(_C).isEmpty());
        assertTrue(_C.getOutboundLink(_P2).isEmpty());

        assertTrue(_C.getInboundLink(_P1).isEmpty());
        assertTrue(_P2.getInboundLink(_C).isEmpty());
    }

    @Test
    public void shouldProcessNodes() {
        _V.visitPlainNode(_P1);
        _V.visitCompositeNode(_C);
        _V.visitPlainNode(_P2);

        assertEquals(1, _B1.getCalls());
        assertEquals(1, _B21.getCalls());
        assertEquals(1, _B22.getCalls());
        assertEquals(1, _B3.getCalls());
    }

    @Test
    public void shouldDistributeOutputData() {
        assertTrue(_P1.getOutboundLink(_C).isEmpty());
        _V.visitPlainNode(_P1);
        assertFalse(_P1.getOutboundLink(_C).isEmpty());
        assertTrue(_C.getOutboundLink(_P2).isEmpty());
        _V.visitCompositeNode(_C);
        assertFalse(_C.getOutboundLink(_P2).isEmpty());
        _V.visitPlainNode(_P2);
    }

    @Test
    public void shouldUtilizeInputData() {
        _V.visitPlainNode(_P1);
        assertFalse(_C.getInboundLink(_P1).isEmpty());
        _V.visitCompositeNode(_C);
        assertTrue(_C.getInboundLink(_P1).isEmpty());
        assertFalse(_P2.getInboundLink(_C).isEmpty());
        _V.visitPlainNode(_P2);
        assertTrue(_P2.getInboundLink(_C).isEmpty());
    }

    @Test
    public void shouldHandleMultipleInputs() {
        _P1.addSuccessor(_P2, new UnbiasedLink());
        _C.getOutboundLink(_P2).push(Arrays.<Object>asList(23L));
        _P1.getOutboundLink(_P2).push(Arrays.<Object>asList("hello"));
        _V.visitPlainNode(_P2);
        assertEquals(Arrays.asList(23L, "hello"), _B3.getLastInput());

        _P1.removeSuccessor(_P2);
        _C.removeSuccessor(_P2);
        _P2.addSuccessor(_C, new UnbiasedLink());
        _P1.getOutboundLink(_C).push(Arrays.<Object>asList("world"));
        _P2.getOutboundLink(_C).push(Arrays.<Object>asList(42.0));
        _V.visitCompositeNode(_C);
        assertEquals(Arrays.asList("world", 42.0), _B21.getLastInput());
    }

    @Test
    public void shouldHandleMultipleOutputs() {
        _P1.addSuccessor(_P2, new UnbiasedLink());
        _V.visitPlainNode(_P1);
        Link link1 = _P1.getOutboundLink(_C);
        Link link2 = _P1.getOutboundLink(_P2);
        while (!link1.isEmpty() && !link2.isEmpty()) {
            assertEquals(link1.poll(), link2.poll());
        }
        assertTrue(link1.isEmpty());
        assertTrue(link2.isEmpty());

        _P1.removeSuccessor(_P2);
        _P1.removeSuccessor(_C);
        _C.addSuccessor(_P1, new UnbiasedLink());
        _V.visitCompositeNode(_C);
        link1 = _C.getOutboundLink(_P2);
        link2 = _C.getOutboundLink(_P1);
        while (!link1.isEmpty() && !link2.isEmpty()) {
            assertEquals(link1.poll(), link2.poll());
        }
        assertTrue(link1.isEmpty());
        assertTrue(link2.isEmpty());
    }

    @Test
    public void shouldNotProcessWithoutInput() {
        // Regular modules should only be called when input values are present.
        // Data sources (no preceding modules) may always be called.
        _V.visitPlainNode(_P2);
        assertEquals(0, _B3.getCalls());
        _C.removeSuccessor(_P2);
        _V.visitPlainNode(_P2);
        assertEquals(1, _B3.getCalls());

        _V.visitCompositeNode(_C);
        assertEquals(0, _B21.getCalls());
        assertEquals(0, _B22.getCalls());
        _P1.removeSuccessor(_C);
        _V.visitCompositeNode(_C);
        assertEquals(1, _B21.getCalls());
        assertEquals(1, _B22.getCalls());
    }

}
