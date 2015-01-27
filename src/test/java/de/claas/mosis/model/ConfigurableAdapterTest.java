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

import java.util.*;

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
    private TestObserver _Observer;

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
        _Observer = new TestObserver();
    }

    @Test
    public void shouldManageCondition() throws Exception {
        String param = Utils.unknownParameter(_C);
        _C.setParameter(param, "hello world");
        _C.addCondition(param, new Condition.IsBoolean());
        _C.setParameter(param, "true");
        _C.setParameter(param, "false");
        try {
            _C.setParameter(param, "hello world");
        } catch (Exception e) {
            // Exception is expected.
        }
        _C.removeCondition(param, new Condition.IsBoolean());
        _C.setParameter(param, "hello world");
    }

    @Test
    public void shouldAddConditionToParameter() throws Exception {
        String param1 = Utils.unknownParameter(_C);
        String param2 = Utils.unknownParameter(_C);
        try {
            _C.setParameter(param1, "hello world");
            _C.setParameter(param2, "hello world");
            _C.addCondition(param1, new Condition.IsBoolean());
            _C.setParameter(param1, "true");
            _C.setParameter(param1, "false");
            _C.setParameter(param2, "abc");
        } catch (Exception e) {
            fail(e.toString());
        }
        _C.removeCondition(param1, new Condition.IsBoolean());
        _C.setParameter(param1, "hello world");
    }

    @Test
    public void shouldManageObserver() throws Exception {
        String VERSION = Observer.UpdateVersion.Version;
        String param = Utils.unknownParameter(_C);
        assertFalse(_C.getParameters().contains(VERSION));

        _C.addObserver(new Observer.UpdateVersion());
        _C.setParameter(param, "hello");
        assertTrue(_C.getParameters().contains(VERSION));
        int version = _C.getParameterAsInteger(VERSION);
        _C.setParameter(param, "world");
        assertEquals(version + 1, (int) _C.getParameterAsInteger(VERSION));

        _C.removeObserver(new Observer.UpdateVersion());
        _C.setParameter(param, "hello world");
        assertEquals(version + 1, (int) _C.getParameterAsInteger(VERSION));
    }

    @Test
    public void shouldUpdateParameterAsBoolean() throws Exception {
        String param = Utils.unknownParameter(_C);
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
        _C.setParameter(param, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, (int) _C.getParameterAsInteger(param));
        _C.setParameter(param, Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, (int) _C.getParameterAsInteger(param));
        assertEquals(Integer.toString(Integer.MIN_VALUE),
                _C.getParameter(param));
        _C.setParameter(param, "42");
        assertEquals(42, (int) _C.getParameterAsInteger(param));
    }

    @Test
    public void shouldUpdateParameterAsLong() throws Exception {
        String param = Utils.unknownParameter(_C);
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
        _C.setParameter(param, Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, _C.getParameterAsDouble(param), 0.0001);
        _C.setParameter(param, Double.MIN_VALUE);
        assertEquals(Double.MIN_VALUE, _C.getParameterAsDouble(param), 0.0001);
        assertEquals(Double.toString(Double.MIN_VALUE), _C.getParameter(param));
        _C.setParameter(param, "42.3");
        assertEquals(42.3, _C.getParameterAsDouble(param), 0.0001);
    }

    @Test
    public void assumptionsOnTestObserver() {
        assertEquals(0, _Observer.callsToUpdate);
        assertTrue(_Observer.updatedConfigurables.isEmpty());
        assertTrue(_Observer.updatedParameters.isEmpty());
    }

    @Test
    public void shouldNotCallUpdate() {
        _C.notifyObservers("test");
        _C.notifyObservers(null);
        assertEquals(0, _Observer.callsToUpdate);
        assertTrue(_Observer.updatedConfigurables.isEmpty());
        assertTrue(_Observer.updatedParameters.isEmpty());
    }

    @Test
    public void shouldAddObserver() {
        _C.addObserver(_Observer);
        _C.notifyObservers("house");
        assertEquals(1, _Observer.callsToUpdate);
        assertTrue(_Observer.updatedParameters.containsKey("house"));
        assertEquals(1, (int) _Observer.updatedParameters.get("house"));
        assertTrue(_Observer.updatedConfigurables.containsKey(_C));
        assertEquals(1, (int) _Observer.updatedConfigurables.get(_C));
    }

    @Test
    public void shouldRemoveObserver() {
        _C.addObserver(_Observer);
        _C.addObserver(_Observer);
        _C.removeObserver(_Observer);
        _C.notifyObservers("keyboard");
        assertEquals(1, _Observer.callsToUpdate);
        assertTrue(_Observer.updatedParameters.containsKey("keyboard"));
        assertEquals(1, (int) _Observer.updatedParameters.get("keyboard"));
        assertTrue(_Observer.updatedConfigurables.containsKey(_C));
        assertEquals(1, (int) _Observer.updatedConfigurables.get(_C));
    }

    @Test
    public void shouldUpdateMultipleObserver() {
        _C.addObserver(_Observer);
        _C.addObserver(_Observer);
        _C.addObserver(_Observer);
        _C.notifyObservers("multi");
        assertEquals(3, _Observer.callsToUpdate);
        assertTrue(_Observer.updatedParameters.containsKey("multi"));
        assertEquals(3, (int) _Observer.updatedParameters.get("multi"));
        assertTrue(_Observer.updatedConfigurables.containsKey(_C));
        assertEquals(3, (int) _Observer.updatedConfigurables.get(_C));
    }

    @Test
    public void shouldAcceptNullValues() {
        _C.addObserver(null);
        _C.removeObserver(null);
        _C.notifyObservers(null);
    }


    private static final class TestObserver implements Observer {

        int callsToUpdate = 0;
        Map<String, Integer> updatedParameters = new HashMap<>();
        Map<Configurable, Integer> updatedConfigurables = new HashMap<>();

        @Override
        public void update(Configurable configurable, String parameter) {
            callsToUpdate++;
            if (updatedParameters.containsKey(parameter))
                updatedParameters.put(parameter, updatedParameters.get(parameter) + 1);
            else
                updatedParameters.put(parameter, 1);

            if (updatedConfigurables.containsKey(configurable))
                updatedConfigurables.put(configurable, updatedConfigurables.get(configurable) + 1);
            else
                updatedConfigurables.put(configurable, 1);
        }
    }


}
