package de.claas.mosis.processing.relation;

import de.claas.mosis.model.ConfigurableAdapter;
import de.claas.mosis.model.Relation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.model.Relation.LastChanged}.
 * It is intended to collect and document a set of test cases for the tested
 * class. Please refer to the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link de.claas.mosis.model.RelationTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class LastChangedTest {

    private ConfigurableAdapter _Dummy;

    /**
     * Returns an instantiated {@link de.claas.mosis.model.Relation.LastChanged}
     * class. If appropriate, the instance is configured with default values.
     *
     * @return an instantiated {@link de.claas.mosis.model.Relation.LastChanged}
     * class
     */
    private Relation build() {
        return new Relation.LastChanged();
    }

    @Before
    public void setUp() {
        _Dummy = new ConfigurableAdapter();
    }

    @Test
    public void shouldAddParameter() {
        Relation r = build();
        assertFalse(_Dummy.getParameters().contains(
                Relation.LastChanged.LastChanged));
        r.compute(_Dummy, null, null);
        assertTrue(_Dummy.getParameters().contains(
                Relation.LastChanged.LastChanged));
    }

    @Test
    public void shouldUpdateParameter() throws Exception {
        Relation r = build();
        r.compute(_Dummy, null, null);
        String d1 = _Dummy.getParameter(Relation.LastChanged.LastChanged);
        Thread.sleep(10);
        r.compute(_Dummy, null, null);
        String d2 = _Dummy.getParameter(Relation.LastChanged.LastChanged);
        assertNotSame(d1, d2);
        assertTrue(10 <= Long.parseLong(d2) - Long.parseLong(d1));
    }

    @Test
    public void shouldNotUpdateParameter() {
        Relation r = build();
        r.compute(_Dummy, null, null);
        String d1 = _Dummy.getParameter(Relation.LastChanged.LastChanged);
        r.compute(_Dummy, Relation.LastChanged.LastChanged, null);
        String d2 = _Dummy.getParameter(Relation.LastChanged.LastChanged);
        assertEquals(d1, d2);
    }

}
