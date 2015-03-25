package de.claas.mosis.processing.observer;

import de.claas.mosis.model.ConfigurableAdapter;
import de.claas.mosis.model.Observer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit test for class {@link de.claas.mosis.model.Observer.BreakOut}. It
 * is intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link de.claas.mosis.model.ObserverTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class BreakOutTest {

    private ConfigurableAdapter _Dummy;

    /**
     * Returns an instantiated {@link de.claas.mosis.model.Observer.BreakOut}
     * class. If appropriate, the instance is configured with default values.
     *
     * @return an instantiated {@link de.claas.mosis.model.Observer.BreakOut}
     * class
     */
    private Observer.BreakOut build() {
        return new Observer.BreakOut();
    }

    @Before
    public void setUp() {
        _Dummy = new ConfigurableAdapter();
    }

    @Test
    public void assumeZeroCalls() {
        Observer.BreakOut o = build();
        assertEquals(0, o.getCalls());
    }

    @Test
    public void assumeZeroUpdates() {
        Observer.BreakOut o = build();
        assertEquals(0, o.getUpdates("random paramater"));
        assertEquals(0, o.getUpdates(_Dummy));
    }

    @Test
    public void assumeNoParameters() {
        Observer.BreakOut o = build();
        assertTrue(o.getParameters().isEmpty());
    }

    @Test
    public void assumeNoConfigurables() {
        Observer.BreakOut o = build();
        assertTrue(o.getConfigurables().isEmpty());
    }

    @Test
    public void shouldCountCalls() {
        Observer.BreakOut o = build();
        o.update(_Dummy, "parameter1");
        o.update(_Dummy, "parameter2");
        assertEquals(2, o.getCalls());
        o.update(_Dummy, "parameter1");
        o.update(_Dummy, "parameter2");
        assertEquals(4, o.getCalls());
    }

    @Test
    public void shouldCountUpdates() {
        Observer.BreakOut o = build();
        o.update(_Dummy, "parameter1");
        o.update(_Dummy, "parameter1");
        assertEquals(2, o.getUpdates("parameter1"));
        assertEquals(0, o.getUpdates("parameter2"));
        assertEquals(2, o.getUpdates(_Dummy));
        o.update(_Dummy, "parameter2");
        o.update(_Dummy, "parameter2");
        assertEquals(2, o.getUpdates("parameter1"));
        assertEquals(2, o.getUpdates("parameter2"));
        assertEquals(4, o.getUpdates(_Dummy));
    }

    @Test
    public void shouldReturnParameters() {
        Observer.BreakOut o = build();
        o.update(_Dummy, "parameter1");
        o.update(_Dummy, "parameter2");
        assertEquals(2, o.getParameters().size());
        o.update(_Dummy, "parameter1");
        o.update(_Dummy, "parameter2");
        assertEquals(2, o.getParameters().size());
    }


    @Test
    public void shouldReturnConfigurables() {
        Observer.BreakOut o = build();
        o.update(_Dummy, "parameter1");
        o.update(_Dummy, "parameter2");
        assertEquals(1, o.getConfigurables().size());
        o.update(_Dummy, "parameter1");
        o.update(_Dummy, "parameter2");
        assertEquals(1, o.getConfigurables().size());
    }

}
