package de.claas.mosis.flow.iterator;

import de.claas.mosis.flow.Node;
import de.claas.mosis.flow.UnbiasedLink;

import java.util.*;

/**
 * The JUnit test for class {@link RepeatedOneShotLevelOrder}. It is intended to
 * collect and document a set of test cases for the tested class. Please refer
 * to the individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class RepeatedOneShotLevelOrderTest extends IteratorTest {

    @Override
    public Iterator<Node> build(Collection<Node> sources) {
        return new RepeatedOneShotLevelOrder(new HashSet<>(sources));
    }

    @Override
    public void shouldHandleMultipleInputs() {
        _N2.addSuccessor(_N1, new UnbiasedLink());
        _N3.addSuccessor(_N1, new UnbiasedLink());
        _N4.addSuccessor(_N1, new UnbiasedLink());

        List<List<Node>> level = new Vector<>();
        level.add(new Vector<Node>(Arrays.asList(_N2, _N3, _N4)));
        level.add(new Vector<Node>(Arrays.asList(_N1)));
        compare(build(Arrays.asList(_N2, _N3, _N4)), level, true);
    }

    @Override
    public void shouldHandleMultipleOutputs() {
        _N1.addSuccessor(_N2, new UnbiasedLink());
        _N1.addSuccessor(_N3, new UnbiasedLink());
        _N1.addSuccessor(_N4, new UnbiasedLink());

        List<List<Node>> level = new Vector<>();
        level.add(new Vector<Node>(Arrays.asList(_N1)));
        level.add(new Vector<Node>(Arrays.asList(_N2, _N3, _N4)));
        compare(build(Arrays.asList(_N1)), level, true);
    }

    @Override
    public void shouldHandleLoops() {
        _N1.addSuccessor(_N2, new UnbiasedLink());
        _N2.addSuccessor(_N1, new UnbiasedLink());

        List<List<Node>> level = new Vector<>();
        level.add(new Vector<Node>(Arrays.asList(_N1)));
        level.add(new Vector<Node>(Arrays.asList(_N2)));
        compare(build(Arrays.asList(_N1)), level, true);
    }

    @Override
    public void shouldIterateEachLevel() {
        _N1.addSuccessor(_N2, new UnbiasedLink());
        _N1.addSuccessor(_N3, new UnbiasedLink());
        _N2.addSuccessor(_N4, new UnbiasedLink());
        _N3.addSuccessor(_N4, new UnbiasedLink());

        List<List<Node>> level = new Vector<>();
        level.add(new Vector<Node>(Arrays.asList(_N1)));
        level.add(new Vector<Node>(Arrays.asList(_N2, _N3)));
        level.add(new Vector<Node>(Arrays.asList(_N4)));
        compare(build(Arrays.asList(_N1)), level, true);
    }

}
