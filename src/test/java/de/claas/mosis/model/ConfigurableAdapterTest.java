package de.claas.mosis.model;

import de.claas.mosis.flow.BiasedLink;
import de.claas.mosis.flow.LinkAdapter;
import de.claas.mosis.flow.UnbiasedLink;
import de.claas.mosis.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.*;

/**
 * The JUnit test for {@link de.claas.mosis.model.ConfigurableAdapter} classes.
 * It is intended to collect and document a set of test cases that are
 * applicable to all {@link de.claas.mosis.model.ConfigurableAdapter} classes.
 * Please refer to the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link de.claas.mosis.model.ConfigurableTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@RunWith(Parameterized.class)
public class ConfigurableAdapterTest {

    private final Class<ConfigurableAdapter> _Clazz;
    private ConfigurableAdapter _C;
    private Observer.BreakOut _Observer;

    /**
     * Initializes this JUnit test for an implementation of the {@link
     * de.claas.mosis.model.ConfigurableAdapter} class.
     *
     * @param clazz implementation of {@link de.claas.mosis.model.ConfigurableAdapter}
     *              class
     */
    public ConfigurableAdapterTest(Class<ConfigurableAdapter> clazz) {
        _Clazz = clazz;
    }

    @Parameters
    public static Collection<?> implementations() {
        List<Object> impl = new Vector<>();
        impl.add(new Object[]{ConfigurableAdapter.class});
        impl.add(new Object[]{LinkAdapter.class});
        impl.add(new Object[]{UnbiasedLink.class});
        impl.add(new Object[]{BiasedLink.class});
        impl.addAll(ProcessorAdapterTest.implementations());
        return impl;
    }

    @Before
    public void before() throws Exception {
        _C = Utils.instance(_Clazz);
        _Observer = new Observer.BreakOut();
    }

    @Test
    public void shouldIncludeAllParameters() {
        int size = _C.getParameters().size();
        String unknown = Utils.unknownParameter(_C);
        _C.setParameter(unknown, "hello world");
        assertEquals(size + 1, _C.getParameters().size());
        assertTrue(_C.getParameters().contains(unknown));
    }

    @Test
    public void shouldIncludeAllObservers() {
        assertFalse(_C.getObservers().contains(_Observer));
        _C.addObserver(_Observer);
        assertTrue(_C.getObservers().contains(_Observer));
        _C.removeObserver(_Observer);
        assertFalse(_C.getObservers().contains(_Observer));
    }

    @Test
    public void shouldIncludeAllConditions() {
        String unknown = Utils.unknownParameter(_C);
        Condition tmp = new Condition.IsBoolean();
        assertTrue(_C.getConditions(unknown).isEmpty());
        assertFalse(_C.getConditions(unknown).contains(tmp));
        _C.addCondition(unknown, tmp);
        assertTrue(_C.getConditions(unknown).contains(tmp));
        _C.removeCondition(unknown, tmp);
        assertFalse(_C.getConditions(unknown).contains(tmp));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotSatisfyCondition() {
        String unknown = Utils.unknownParameter(_C);
        _C.setParameter(unknown, "hello");
        _C.addCondition(unknown, new Condition.IsBoolean());
        _C.setParameter(unknown, "world");
    }

    @Test
    public void shouldSatisfyCondition() {
        String unknown = Utils.unknownParameter(_C);
        _C.setParameter(unknown, "hello");
        _C.addCondition(unknown, new Condition.IsBoolean());
        _C.setParameter(unknown, "false");
        assertFalse(_C.getParameterAsBoolean(unknown));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotSatisfyConditionWhenUpdating() {
        String unknown = Utils.unknownParameter(_C);
        _C.setParameter(unknown, "hello");

        ConfigurableAdapter configurable = new ConfigurableAdapter();
        configurable.addCondition(unknown, new Condition.IsBoolean());
        configurable.update(_C, false);
    }

    @Test
    public void shouldNotNotifyObserver() {
        _C.notifyObservers("test");
        _C.notifyObservers(null);
        assertEquals(0, _Observer.getCalls());
        assertTrue(_Observer.getParameters().isEmpty());
        assertTrue(_Observer.getConfigurables().isEmpty());
    }

    @Test
    public void shouldNotifyObserver() {
        _C.addObserver(_Observer);
        _C.notifyObservers("house");
        assertEquals(1, _Observer.getCalls());
        assertTrue(_Observer.getParameters().contains("house"));
        assertEquals(1, _Observer.getUpdates("house"));
    }

    @Test
    public void shouldNotifyObservers() {
        _C.addObserver(_Observer);
        _C.addObserver(_Observer);
        _C.addObserver(_Observer);
        _C.notifyObservers("multi");
        assertEquals(3, _Observer.getCalls());
        assertTrue(_Observer.getParameters().contains("multi"));
        assertEquals(3, _Observer.getUpdates("multi"));
    }

    @Test
    public void shouldNotifyObserversWhenSettingParameter() {
        String unknown = Utils.unknownParameter(_C);
        _C.addObserver(_Observer);
        _C.setParameter(unknown, "hello");
        assertEquals(1, _Observer.getCalls());
        assertTrue(_Observer.getParameters().contains(unknown));
        assertEquals(1, _Observer.getUpdates(unknown));
    }

    @Test
    public void shouldNotifyObserversWhenUpdating() {
        _C.addObserver(_Observer);

        String unknown = Utils.unknownParameter(_C);
        ConfigurableAdapter configurable = new ConfigurableAdapter();
        configurable.setParameter(unknown, "hello");
        assertEquals(0, _Observer.getCalls());

        _C.update(configurable, false);
        assertEquals(1, _Observer.getCalls());
        assertTrue(_Observer.getParameters().contains(unknown));
        assertEquals(1, _Observer.getUpdates(unknown));
    }

    @Test
    public void shouldNotNotifyObserversWhenUpdating() {
        String unknown = Utils.unknownParameter(_C);
        ConfigurableAdapter configurable = new ConfigurableAdapter();
        configurable.setParameter(unknown, "hello");
        configurable.addObserver(_Observer);
        _C.update(configurable, false);
        assertEquals(0, _Observer.getCalls());
    }

    @Test
    public void shouldResetEverything() {
        String unknown = Utils.unknownParameter(_C);
        _C.setParameter(unknown, "hello");
        _C.addCondition(unknown, new Condition.ClassExists());
        _C.addObserver(_Observer);
        _C.update(new ConfigurableAdapter(), true);
        assertTrue(_C.getParameters().isEmpty());
        assertTrue(_C.getConditions(unknown).isEmpty());
        assertTrue(_C.getObservers().isEmpty());
    }

    @Test
    public void shouldOverwriteEverything() {
        Condition.IsNotNull notNull = new Condition.IsNotNull();
        Condition.IsGreaterThan greaterThan = new Condition.IsGreaterThan(0d);
        ConfigurableAdapter configurable = new ConfigurableAdapter();
        configurable.addObserver(new Observer.BreakOut());
        configurable.addCondition("hello", notNull);
        configurable.setParameter("hello", "world");
        configurable.addCondition("number", greaterThan);
        configurable.setParameter("number", 42);

        String unknown = Utils.unknownParameter(_C);
        _C.addCondition(unknown, new Condition.IsNotNull());
        _C.setParameter(unknown, unknown);
        _C.addObserver(_Observer);

        _C.update(configurable, true);
        assertEquals(2, _C.getParameters().size());
        assertTrue(_C.getParameters().contains("hello"));
        assertTrue(_C.getParameters().contains("number"));
        assertEquals("world", _C.getParameter("hello"));
        assertEquals("42", _C.getParameter("number"));
        assertEquals(1, _C.getConditions("hello").size());
        assertTrue(_C.getConditions("hello").contains(notNull));
        assertEquals(1, _C.getConditions("number").size());
        assertTrue(_C.getConditions("number").contains(greaterThan));
        assertEquals(1, _C.getObservers().size());
    }

    @Test
    public void shouldMergeConfigurables() {
        Condition.IsNotNull notNull = new Condition.IsNotNull();
        Condition.IsGreaterThan greaterThan = new Condition.IsGreaterThan(0d);
        ConfigurableAdapter configurable = new ConfigurableAdapter();
        configurable.addObserver(new Observer.BreakOut());
        configurable.addCondition("hello", notNull);
        configurable.setParameter("hello", "false");
        configurable.addCondition("number", greaterThan);
        configurable.setParameter("number", 42);

        Condition.IsBoolean isBoolean = new Condition.IsBoolean();
        String unknown = Utils.unknownParameter(_C);
        _C.setParameter(unknown, unknown);
        _C.addCondition("hello", isBoolean);
        _C.setParameter("hello", "true");
        _C.addObserver(_Observer);
        int numberParameters = _C.getParameters().size();
        int numberObservers = _C.getObservers().size();

        _C.update(configurable, false);
        assertEquals(numberParameters + 1, _C.getParameters().size());
        assertTrue(_C.getParameters().contains(unknown));
        assertTrue(_C.getParameters().contains("hello"));
        assertTrue(_C.getParameters().contains("number"));
        assertEquals(unknown, _C.getParameter(unknown));
        assertEquals("false", _C.getParameter("hello"));
        assertEquals("42", _C.getParameter("number"));
        assertEquals(0, _C.getConditions(unknown).size());
        assertEquals(2, _C.getConditions("hello").size());
        assertTrue(_C.getConditions("hello").contains(notNull));
        assertTrue(_C.getConditions("hello").contains(isBoolean));
        assertEquals(1, _C.getConditions("number").size());
        assertTrue(_C.getConditions("number").contains(greaterThan));
        assertEquals(numberObservers + 1, _C.getObservers().size());
    }

    @Test
    public void assumptionsOnTestObserver() {
        assertEquals(0, _Observer.getCalls());
        assertTrue(_Observer.getParameters().isEmpty());
        assertTrue(_Observer.getConfigurables().isEmpty());
    }

    @Test
    public void shouldUpdateParameter() throws Exception {
        String param = Utils.unknownParameter(_C);
        assertNull(_C.getParameter(param));
        _C.setParameter(param, "world");
        assertEquals("world", _C.getParameter(param));
        _C.setParameter(param, "hello");
        assertEquals("hello", _C.getParameter(param));
    }

    @Test
    public void shouldUpdateParameterAsBoolean() throws Exception {
        String param = Utils.unknownParameter(_C);
        assertNull(_C.getParameter(param));
        _C.setParameter(param, true);
        assertTrue(_C.getParameterAsBoolean(param));
        _C.setParameter(param, false);
        assertFalse(_C.getParameterAsBoolean(param));

        assertEquals("false", _C.getParameter(param));
        _C.setParameter(param, "true");
        assertTrue(_C.getParameterAsBoolean(param));
    }

    @Test
    public void shouldUpdateParameterAsInteger() throws Exception {
        String param = Utils.unknownParameter(_C);
        assertNull(_C.getParameter(param));
        _C.setParameter(param, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, (int) _C.getParameterAsInteger(param));
        _C.setParameter(param, Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, (int) _C.getParameterAsInteger(param));

        assertEquals(Integer.toString(Integer.MIN_VALUE), _C.getParameter(param));
        _C.setParameter(param, "42");
        assertEquals(42, (int) _C.getParameterAsInteger(param));
    }

    @Test
    public void shouldUpdateParameterAsLong() throws Exception {
        String param = Utils.unknownParameter(_C);
        assertNull(_C.getParameter(param));
        _C.setParameter(param, Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, (long) _C.getParameterAsLong(param));
        _C.setParameter(param, Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, (long) _C.getParameterAsLong(param));

        assertEquals(Long.toString(Long.MIN_VALUE), _C.getParameter(param));
        _C.setParameter(param, "42");
        assertEquals(42, (long) _C.getParameterAsLong(param));
    }

    @Test
    public void shouldUpdateParameterAsDouble() throws Exception {
        String param = Utils.unknownParameter(_C);
        assertNull(_C.getParameter(param));
        _C.setParameter(param, Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, _C.getParameterAsDouble(param), 0.0001);
        _C.setParameter(param, Double.MIN_VALUE);
        assertEquals(Double.MIN_VALUE, _C.getParameterAsDouble(param), 0.0001);

        assertEquals(Double.toString(Double.MIN_VALUE), _C.getParameter(param));
        _C.setParameter(param, "42.3");
        assertEquals(42.3, _C.getParameterAsDouble(param), 0.0001);
    }

    @Test
    public void shouldAcceptNullValues() {
        _C.addObserver(null);
        _C.removeObserver(null);
        _C.notifyObservers(null);

        String unknown = Utils.unknownParameter(_C);
        _C.addCondition(unknown, null);
        _C.removeCondition(unknown, null);
    }

}
