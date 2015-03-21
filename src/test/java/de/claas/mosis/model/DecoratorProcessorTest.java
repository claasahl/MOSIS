package de.claas.mosis.model;

import de.claas.mosis.io.generator.Linear;
import de.claas.mosis.processing.debug.*;
import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.*;

/**
 * The JUnit test for {@link de.claas.mosis.model.DecoratorProcessor} classes.
 * It is intended to collect and document a set of test cases that are
 * applicable to all {@link de.claas.mosis.model.DecoratorProcessor} classes.
 * Please refer to the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link de.claas.mosis.model.ProcessorTest}
 * and {@link de.claas.mosis.model.ProcessorAdapterTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@RunWith(Parameterized.class)
public class DecoratorProcessorTest {

    private final Class<DecoratorProcessor<Object, Object>> _Clazz;
    private DecoratorProcessor<Object, Object> _P1;
    private Processor<Object, Object> _P2;

    /**
     * Initializes this JUnit test for an implementation of the {@link
     * de.claas.mosis.model.DecoratorProcessor} class.
     *
     * @param clazz implementation of {@link de.claas.mosis.model.DecoratorProcessor}
     *              class
     */
    public DecoratorProcessorTest(
            Class<DecoratorProcessor<Object, Object>> clazz) {
        _Clazz = clazz;
    }

    @Parameters
    public static Collection<?> implementations() {
        List<Object> impl = new Vector<>();
        impl.add(new Object[]{DecoratorProcessor.class});
        impl.add(new Object[]{BreakOut.class});
        impl.add(new Object[]{Counter.class});
        impl.add(new Object[]{Sleep.class});
        impl.add(new Object[]{SystemOut.class});
        impl.add(new Object[]{Time.class});
        impl.add(new Object[]{Logger.class});
        return impl;
    }

    @Before
    public void before() throws Exception {
        _P1 = Utils.instance(_Clazz);
        _P1.setParameter(DecoratorProcessor.CLASS, Null.class.getName());
        _P1.setUp();
        _P2 = new SystemOut();
        _P2.setUp();
    }

    @After
    public void after() {
        _P1.dismantle();
        _P2.dismantle();
    }

    @Test
    public void assumptionsOnParameterClass() throws Exception {
        assertEquals(Null.class.getName(), _P1.getParameter(DecoratorProcessor.CLASS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterClassMayNotBeNull() throws Exception {
        Utils.updateParameter(_P1, DecoratorProcessor.CLASS, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterClassMustBeValidClass() throws Exception {
        try {
            Utils.updateParameters(_P1,
                    DecoratorProcessor.CLASS, Null.class.getName(),
                    DecoratorProcessor.CLASS, Forward.class.getName());
        } catch (Exception e) {
            fail(e.toString());
        }
        Utils.updateParameter(_P1, DecoratorProcessor.CLASS, "class.should.not.exist");
    }

    @Test
    public void shouldInitializeImplementationWithoutSetUp() throws Exception {
        DecoratorProcessor<Object, Object> p = Utils.instance(_Clazz);
        assertNull(p.getParameter(Linear.STEP));
        p.setParameter(DecoratorProcessor.CLASS, Linear.class.getName());
        assertNotNull(p.getParameter(Linear.STEP));
    }

    @Test
    public void shouldInstantiateNewClass() throws Exception {
        assertEquals(Null.class, _P1.getProcessor().getClass());
        Utils.updateParameter(_P1, DecoratorProcessor.CLASS, SystemOut.class.getName());
        assertNotNull(_P1.getProcessor());
        assertEquals(SystemOut.class, _P1.getProcessor().getClass());
    }

    @Test
    public void shouldIncludeDecoratedParameters() throws Exception {
        assertTrue(_P2.getParameters().size() > 0);

        int before = _P1.getParameters().size();
        Utils.updateParameter(_P1, DecoratorProcessor.CLASS, SystemOut.class.getName());
        int after = _P1.getParameters().size();
        assertTrue(after > before);
    }

    @Test
    public void shouldForwardParameter() {
        String unknown = Utils.unknownParameter(_P1);
        _P1.setParameter(unknown, "hello");
        assertEquals("hello", _P1.getProcessor().getParameter(unknown));
        _P1.getProcessor().setParameter(unknown, "world");
        assertEquals("world", _P1.getParameter(unknown));
    }

    @Test
    public void shouldNotForwardParameter() {
        String unknown = Utils.unknownParameter(_P1);
        _P1.setParameter(DecoratorProcessor.LOCAL + unknown, "hello");
        assertNull(_P1.getProcessor().getParameter(unknown));
        assertEquals("hello", _P1.getParameter(unknown));
    }

    @Test
    public void shouldForwardParameterAsBoolean() {
        String unknown = Utils.unknownParameter(_P1);
        _P1.setParameter(unknown, true);
        assertTrue(_P1.getProcessor().getParameterAsBoolean(unknown));
        _P1.getProcessor().setParameter(unknown, false);
        assertEquals(Boolean.toString(false), _P1.getParameter(unknown));
    }

    @Test
    public void shouldNotForwardParameterAsBoolean() {
        String unknown = Utils.unknownParameter(_P1);
        _P1.setParameter(DecoratorProcessor.LOCAL + unknown, true);
        assertNull(_P1.getProcessor().getParameterAsBoolean(unknown));
        assertTrue(_P1.getParameterAsBoolean(unknown));
    }

    @Test
    public void shouldForwardParameterAsInteger() {
        String unknown = Utils.unknownParameter(_P1);
        _P1.setParameter(unknown, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, (int) _P1.getProcessor().getParameterAsInteger(unknown));
        _P1.getProcessor().setParameter(unknown, Integer.MIN_VALUE);
        assertEquals(Integer.toString(Integer.MIN_VALUE), _P1.getParameter(unknown));
    }

    @Test
    public void shouldNotForwardParameterAsInteger() {
        String unknown = Utils.unknownParameter(_P1);
        _P1.setParameter(DecoratorProcessor.LOCAL + unknown, Integer.MAX_VALUE);
        assertNull(_P1.getProcessor().getParameterAsInteger(unknown));
        assertEquals(Integer.MAX_VALUE, (int) _P1.getParameterAsInteger(unknown));
    }

    @Test
    public void shouldForwardParameterAsLong() {
        String unknown = Utils.unknownParameter(_P1);
        _P1.setParameter(unknown, Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, (long) _P1.getProcessor().getParameterAsLong(unknown));
        _P1.getProcessor().setParameter(unknown, Long.MIN_VALUE);
        assertEquals(Long.toString(Long.MIN_VALUE), _P1.getParameter(unknown));
    }

    @Test
    public void shouldNotForwardParameterAsLong() {
        String unknown = Utils.unknownParameter(_P1);
        _P1.setParameter(DecoratorProcessor.LOCAL + unknown, Long.MAX_VALUE);
        assertNull(_P1.getProcessor().getParameterAsLong(unknown));
        assertEquals(Long.MAX_VALUE, (long) _P1.getParameterAsLong(unknown));
    }

    @Test
    public void shouldForwardParameterAsDouble() {
        String unknown = Utils.unknownParameter(_P1);
        _P1.setParameter(unknown, Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, _P1.getProcessor().getParameterAsDouble(unknown), 0.0001);
        _P1.getProcessor().setParameter(unknown, Double.MIN_VALUE);
        assertEquals(Double.toString(Double.MIN_VALUE), _P1.getParameter(unknown));
    }

    @Test
    public void shouldNotForwardParameterAsDouble() {
        String unknown = Utils.unknownParameter(_P1);
        _P1.setParameter(DecoratorProcessor.LOCAL + unknown, Double.MAX_VALUE);
        assertNull(_P1.getProcessor().getParameterAsDouble(unknown));
        assertEquals(Double.MAX_VALUE, _P1.getParameterAsDouble(unknown), 0.0001);
    }

    @Test
    public void shouldForwardObserver() {
        Observer observer = new Observer.BreakOut();
        Utils.updateParameter(_P1, DecoratorProcessor.CLASS, SystemOut.class.getName());
        int numberObservers = _P1.getObservers().size();
        _P1.addObserver(observer);
        assertEquals(numberObservers + 1, _P1.getProcessor().getObservers().size());
        assertTrue(_P1.getProcessor().getObservers().contains(observer));
    }

    @Test
    public void shouldForwardCondition() {
        String unknown = Utils.unknownParameter(_P1);
        Condition condition = new Condition.BreakOut();
        _P1.addCondition(unknown, condition);
        Utils.updateParameter(_P1, DecoratorProcessor.CLASS, SystemOut.class.getName());
        assertEquals(1, _P1.getProcessor().getConditions(unknown).size());
        assertTrue(_P1.getProcessor().getConditions(unknown).contains(condition));
    }

    @Test
    public void shouldNotForwardCondition() {
        String unknown = Utils.unknownParameter(_P1);
        Condition condition = new Condition.BreakOut();
        _P1.addCondition(DecoratorProcessor.LOCAL + unknown, condition);
        Utils.updateParameter(_P1, DecoratorProcessor.CLASS, SystemOut.class.getName());
        assertEquals(0, _P1.getProcessor().getConditions(unknown).size());
        assertTrue(_P1.getConditions(unknown).contains(condition));
    }

    @Test
    public void shouldBeDecoratedParameter() throws Exception {
        String unknown = Utils.unknownParameter(_P1);
        assertTrue(_P1.isDecoratedParameter(unknown));
        assertTrue(_P1.isDecoratedParameter(DecoratorProcessor.SHADOWED + unknown));
        assertTrue(_P1.isDecoratedParameter(DecoratorProcessor.SHADOWED + DecoratorProcessor.CLASS));
    }

    @Test
    public void shouldBeLocalParameter() throws Exception {
        String unknown = Utils.unknownParameter(_P1);
        assertFalse(_P1.isDecoratedParameter(DecoratorProcessor.CLASS));
        assertFalse(_P1.isDecoratedParameter(DecoratorProcessor.LOCAL + DecoratorProcessor.CLASS));
        assertFalse(_P1.isDecoratedParameter(DecoratorProcessor.LOCAL + unknown));
    }

    @Test
    public void shouldRememberParameters() {
        String unknown = Utils.unknownParameter(_P1);
        Utils.updateParameters(_P1, unknown, "hello",
                DecoratorProcessor.CLASS, Linear.class.getName());
        assertEquals("hello", _P1.getParameter(unknown));
        Utils.updateParameters(_P1, DecoratorProcessor.CLASS, SystemOut.class.getName());
        assertEquals("hello", _P1.getParameter(unknown));
    }

    @Test
    public void shouldRememberConditions() {
        String unknown = Utils.unknownParameter(_P1);
        Condition condition = new Condition.BreakOut();
        _P1.addCondition(unknown, condition);
        Utils.updateParameters(_P1, DecoratorProcessor.CLASS, Linear.class.getName());
        assertTrue(_P1.getConditions(unknown).contains(condition));
        Utils.updateParameters(_P1, DecoratorProcessor.CLASS, SystemOut.class.getName());
        assertTrue(_P1.getConditions(unknown).contains(condition));
    }

    @Test
    public void shouldRememberObservers() {
        Observer observer = new Observer.BreakOut();
        _P1.addObserver(observer);
        Utils.updateParameters(_P1, DecoratorProcessor.CLASS, Linear.class.getName());
        assertTrue(_P1.getObservers().contains(observer));
        Utils.updateParameters(_P1, DecoratorProcessor.CLASS, SystemOut.class.getName());
        assertTrue(_P1.getObservers().contains(observer));
    }

    @Test
    public void shouldNotifyObservers() {
        String unknown = Utils.unknownParameter(_P1);
        Observer.BreakOut observer = new Observer.BreakOut();
        _P1.addObserver(observer);
        _P1.notifyObservers(unknown);
        _P1.notifyObservers(DecoratorProcessor.LOCAL + unknown);
        _P1.notifyObservers(DecoratorProcessor.SHADOWED + unknown);
        Utils.updateParameters(_P1, DecoratorProcessor.CLASS, Linear.class.getName());
        _P1.getProcessor().notifyObservers(unknown);
        assertEquals(5, observer.getCalls());
        assertEquals(4, observer.getUpdates(unknown));
        assertEquals(1, observer.getUpdates(DecoratorProcessor.CLASS));
        assertNull(observer.getUpdates(DecoratorProcessor.LOCAL + unknown));
        assertNull(observer.getUpdates(DecoratorProcessor.SHADOWED + unknown));
    }

    @Test
    public void shouldCheckConditions() {
        String unknown = Utils.unknownParameter(_P1);
        Condition.BreakOut condition = new Condition.BreakOut();
        _P1.addCondition(unknown, condition);
        _P1.setParameter(unknown, "hello1");
        _P1.setParameter(DecoratorProcessor.SHADOWED + unknown, "hello2");
        _P1.setParameter(DecoratorProcessor.LOCAL + unknown, "hello3");
        Utils.updateParameters(_P1, DecoratorProcessor.CLASS, Linear.class.getName());
        _P1.getProcessor().setParameter(unknown, "hello4");

        assertEquals(6, condition.getCalls());
        assertEquals(6, condition.getUpdates(unknown));
        assertNull(condition.getUpdates(DecoratorProcessor.LOCAL + unknown));
        assertNull(condition.getUpdates(DecoratorProcessor.SHADOWED + unknown));
    }

    @Test
    public void shouldMergeConditions() {
        Linear tmp = new Linear();
        assertFalse(tmp.getConditions(Linear.B).isEmpty());
        Condition condition = new Condition.BreakOut();
        _P1.addCondition(Linear.B, condition);
        Utils.updateParameters(_P1, DecoratorProcessor.CLASS, Linear.class.getName());
        assertTrue(_P1.getConditions(Linear.B).size() > 1);
    }

    @Test
    public void shouldHideParameter() throws Exception {
        Utils.updateParameter(_P1, DecoratorProcessor.CLASS,
                DecoratorProcessor.class.getName());
        assertEquals(DecoratorProcessor.class.getName(),
                _P1.getParameter(DecoratorProcessor.CLASS));
        assertEquals("", _P1.getParameter(DecoratorProcessor.SHADOWED + DecoratorProcessor.CLASS));
    }

    @Test
    public void shouldHideCondition() throws Exception {
        String unknown = Utils.unknownParameter(_P1);
        Condition condition1 = new Condition.BreakOut();
        Condition condition2 = new Condition.IsBoolean();
        _P1.addCondition(unknown, condition2);
        _P1.addCondition(DecoratorProcessor.LOCAL + unknown, condition1);
        assertTrue(_P1.getConditions(unknown).contains(condition1));
        assertFalse(_P1.getConditions(unknown).contains(condition2));
        assertFalse(_P1.getConditions(DecoratorProcessor.SHADOWED + unknown).contains(condition1));
        assertTrue(_P1.getConditions(DecoratorProcessor.SHADOWED + unknown).contains(condition2));
        assertFalse(_P1.getProcessor().getConditions(unknown).contains(condition1));
        assertTrue(_P1.getProcessor().getConditions(unknown).contains(condition2));
    }

    @Test
    public void shouldCallProcess() throws Exception {
        Utils.updateParameters(_P1,
                DecoratorProcessor.CLASS, BreakOut.class.getName(),
                DecoratorProcessor.SHADOWED + BreakOut.CLASS,
                Null.class.getName());
        assertEquals(0, ((BreakOut) _P1.getProcessor()).getCallsToProcess());
        Utils.process(_P1);
        assertEquals(1, ((BreakOut) _P1.getProcessor()).getCallsToProcess());
        Utils.process(_P1);
        Utils.process(_P1);
        assertEquals(3, ((BreakOut) _P1.getProcessor()).getCallsToProcess());
    }

    @Test
    public void shouldCallSetUpAndDismantle() throws Exception {
        Utils.updateParameters(_P1,
                DecoratorProcessor.CLASS, BreakOut.class.getName(),
                DecoratorProcessor.SHADOWED + BreakOut.CLASS,
                Null.class.getName());
        assertEquals(0, ((BreakOut) _P1.getProcessor()).getCallsToDismantle());
        assertEquals(1, ((BreakOut) _P1.getProcessor()).getCallsToSetUp());
        Utils.process(_P1);
        assertEquals(0, ((BreakOut) _P1.getProcessor()).getCallsToDismantle());
        assertEquals(1, ((BreakOut) _P1.getProcessor()).getCallsToSetUp());
        _P1.dismantle();
        assertEquals(1, ((BreakOut) _P1.getProcessor()).getCallsToDismantle());
        assertEquals(1, ((BreakOut) _P1.getProcessor()).getCallsToSetUp());
        _P1.setUp();
        assertEquals(1, ((BreakOut) _P1.getProcessor()).getCallsToDismantle());
        assertEquals(2, ((BreakOut) _P1.getProcessor()).getCallsToSetUp());
    }

    @Test
    public void shouldForwardInputValues() throws Exception {
        Utils.updateParameters(_P1,
                DecoratorProcessor.CLASS, BreakOut.class.getName(),
                DecoratorProcessor.SHADOWED + BreakOut.CLASS,
                Null.class.getName());
        assertNull(((BreakOut) _P1.getProcessor()).getLastInput());
        Utils.process(_P1, 1, null, 3);
        assertEquals(Arrays.<Object>asList(1, null, 3),
                ((BreakOut) _P1.getProcessor()).getLastInput());
    }

}
