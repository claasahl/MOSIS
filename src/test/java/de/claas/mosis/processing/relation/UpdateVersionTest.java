package de.claas.mosis.processing.relation;

import de.claas.mosis.model.ConfigurableAdapter;
import de.claas.mosis.model.Observer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.model.Observer.UpdateVersion}.
 * It is intended to collect and document a set of test cases for the tested
 * class. Please refer to the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link de.claas.mosis.model.ObserverTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class UpdateVersionTest {

    private ConfigurableAdapter _Dummy;

    /**
     * Returns an instantiated {@link de.claas.mosis.model.Observer.UpdateVersion}
     * class. If appropriate, the instance is configured with default values.
     *
     * @return an instantiated {@link de.claas.mosis.model.Observer.UpdateVersion}
     * class
     */
    private Observer build() {
        return new Observer.UpdateVersion();
    }

    @Before
    public void setUp() {
        _Dummy = new ConfigurableAdapter();
    }

    @Test
    public void shouldAddParameter() {
        Observer o = build();
        assertFalse(_Dummy.getParameters().contains(
                Observer.UpdateVersion.Version));
        o.update(_Dummy, null);
        assertTrue(_Dummy.getParameters().contains(
                Observer.UpdateVersion.Version));
    }

    @Test
    public void shouldUpdateParameter() {
        Observer o = build();
        o.update(_Dummy, null);
        String v1 = _Dummy.getParameter(Observer.UpdateVersion.Version);
        o.update(_Dummy, null);
        String v2 = _Dummy.getParameter(Observer.UpdateVersion.Version);
        assertEquals(Integer.parseInt(v1) + 1, Integer.parseInt(v2));
    }

    @Test
    public void shouldNotUpdateParameter() {
        Observer o = build();
        o.update(_Dummy, null);
        String v1 = _Dummy.getParameter(Observer.UpdateVersion.Version);
        o.update(_Dummy, Observer.UpdateVersion.Version);
        String v2 = _Dummy.getParameter(Observer.UpdateVersion.Version);
        assertEquals(v1, v2);
    }

}
