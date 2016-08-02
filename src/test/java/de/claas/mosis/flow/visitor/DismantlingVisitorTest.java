package de.claas.mosis.flow.visitor;

import de.claas.mosis.flow.*;
import de.claas.mosis.io.generator.Linear;
import de.claas.mosis.processing.debug.BreakOut;
import de.claas.mosis.processing.debug.Forward;
import de.claas.mosis.processing.debug.Null;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * The JUnit test for class {@link de.claas.mosis.flow.visitor.DismantlingVisitor}.
 * It is intended to collect and document a set of test cases for the tested
 * class. Please refer to the individual tests for more detailed information.
 * <p>
 * Additional test cases can be found in {@link de.claas.mosis.flow.VisitorTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class DismantlingVisitorTest {

    private Visitor _V;
    private PlainNode _P1, _P2;
    private CompositeNode _C;
    private BreakOut _B1, _B21, _B22, _B3;

    @Before
    public void before() {
        _B1 = new BreakOut();
        _B1.setParameter(BreakOut.CLASS, Linear.class.getName());
        _B1.setUp();
        _B21 = new BreakOut();
        _B21.setParameter(BreakOut.CLASS, Linear.class.getName());
        _B21.setUp();
        _B22 = new BreakOut();
        _B22.setParameter(BreakOut.CLASS, Forward.class.getName());
        _B22.setUp();
        _B3 = new BreakOut();
        _B3.setParameter(BreakOut.CLASS, Null.class.getName());
        _B3.setUp();

        _V = new DismantlingVisitor();
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

    @After
    public void after() {
        _B1.dismantle();
        _B21.dismantle();
        _B22.dismantle();
        _B3.dismantle();
    }

    @Test
    public void assumesThatModulesAreInitialized() {
        assertEquals(1, _B1.getCallsToSetUp());
        assertEquals(1, _B21.getCallsToSetUp());
        assertEquals(1, _B22.getCallsToSetUp());
        assertEquals(1, _B3.getCallsToSetUp());
    }

    @Test
    public void shouldNotHaveBeenCalled() {
        assertEquals(0, _B1.getCallsToDismantle());
        assertEquals(0, _B21.getCallsToDismantle());
        assertEquals(0, _B22.getCallsToDismantle());
        assertEquals(0, _B3.getCallsToDismantle());
    }

    @Test
    public void shouldDismantleNodes() {
        _V.visitPlainNode(_P1);
        _V.visitCompositeNode(_C);
        _V.visitPlainNode(_P2);

        assertEquals(1, _B1.getCallsToDismantle());
        assertEquals(1, _B21.getCallsToDismantle());
        assertEquals(1, _B22.getCallsToDismantle());
        assertEquals(1, _B3.getCallsToDismantle());
    }

    @Test
    public void shouldNotProcessNodes() {
        _V.visitPlainNode(_P1);
        _V.visitCompositeNode(_C);
        _V.visitPlainNode(_P2);

        assertEquals(0, _B1.getCallsToProcess());
        assertEquals(0, _B21.getCallsToProcess());
        assertEquals(0, _B22.getCallsToProcess());
        assertEquals(0, _B3.getCallsToProcess());
    }

    @Test
    public void shouldNotSetUpNodes() {
        _V.visitPlainNode(_P1);
        _V.visitCompositeNode(_C);
        _V.visitPlainNode(_P2);

        assertEquals(1, _B1.getCallsToSetUp());
        assertEquals(1, _B21.getCallsToSetUp());
        assertEquals(1, _B22.getCallsToSetUp());
        assertEquals(1, _B3.getCallsToSetUp());
    }

}
