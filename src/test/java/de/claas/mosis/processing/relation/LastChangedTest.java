package de.claas.mosis.processing.relation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.claas.mosis.model.ConfigurableAdapter;
import de.claas.mosis.model.Relation;
import de.claas.mosis.model.Relation.LastChanged;
import de.claas.mosis.model.RelationTest;

/**
 * The JUnit test for class {@link LastChange}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * 
 * Additional test cases can be found in {@link RelationTest}.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class LastChangedTest {

    private ConfigurableAdapter _Dummy;

    /**
     * Returns an instantiated {@link LastChanged} class. If appropriate, the
     * instance is configured with default values.
     * 
     * @return an instantiated {@link LastChanged} class
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
