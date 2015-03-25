package de.claas.mosis.flow;

import de.claas.mosis.flow.visitor.DismantlingVisitor;
import de.claas.mosis.flow.visitor.ProcessingVisitor;
import de.claas.mosis.flow.visitor.SettingUpVisitor;
import de.claas.mosis.io.generator.Linear;
import de.claas.mosis.processing.debug.Forward;
import de.claas.mosis.processing.debug.ToString;
import de.claas.mosis.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * The JUnit test for {@link de.claas.mosis.flow.Visitor} classes. It is
 * intended to collect and document a set of test cases that are applicable to
 * all {@link de.claas.mosis.flow.Visitor} classes. Please refer to the
 * individual tests for more detailed information.
 * <p/>
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@RunWith(Parameterized.class)
public class VisitorTest {

    private final Class<Visitor> _Clazz;
    private Visitor _V;
    private PlainNode _P;
    private CompositeNode _C;

    /**
     * Initializes this JUnit test for an implementation of the {@link
     * de.claas.mosis.flow.Visitor} class.
     *
     * @param clazz implementation of {@link de.claas.mosis.flow.Visitor} class
     */
    public VisitorTest(Class<Visitor> clazz) {
        _Clazz = clazz;
    }

    @Parameters
    public static Collection<?> implementations() {
        List<Object> impl = new Vector<>();
        impl.add(new Object[]{SettingUpVisitor.class});
        impl.add(new Object[]{DismantlingVisitor.class});
        impl.add(new Object[]{ProcessingVisitor.class});
        return impl;
    }

    @Before
    public void before() throws Exception {
        _V = Utils.instance(_Clazz);

        _P = new PlainNode(new Forward<>());
        Node tmp1 = new PlainNode(new Linear());
        Node tmp2 = new PlainNode(new ToString());
        tmp1.addSuccessor(tmp2, new UnbiasedLink());
        Set<Node> tmp = new HashSet<>();
        tmp.add(tmp1);
        _C = new CompositeNode(tmp, tmp2);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionPlainNode() {
        _V.visitPlainNode(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionCompositeNode() {
        _V.visitCompositeNode(null);
    }

    @Test
    public void shouldHandlePlainNodes() {
        assertTrue(_V.visitPlainNode(_P));
    }

    @Test
    public void shouldHandleCompositeNodes() {
        assertTrue(_V.visitCompositeNode(_C));
    }

}
