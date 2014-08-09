package de.claas.mosis.processing.relation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.claas.mosis.model.ConfigurableAdapter;
import de.claas.mosis.model.Relation;
import de.claas.mosis.model.Relation.ParameterHistory;
import de.claas.mosis.model.RelationTest;

/**
 * The JUnit test for class {@link ParameterHistory}. It is intended to collect
 * and document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * 
 * Additional test cases can be found in {@link RelationTest}.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class ParameterHistoryTest {

    private static final String P = "paramXY";
    private static final String H = Relation.ParameterHistory.Prefix
	    + "paramXY";
    private ConfigurableAdapter _Dummy;

    /**
     * Returns an instantiated {@link ParameterHistory} class. If appropriate,
     * the instance is configured with default values.
     * 
     * @return an instantiated {@link ParameterHistory} class
     */
    private Relation build() {
	return new Relation.ParameterHistory();
    }

    @Before
    public void setUp() {
	_Dummy = new ConfigurableAdapter();
    }

    @Test
    public void shouldAddParameter() {
	Relation r = build();
	assertFalse(_Dummy.getParameters().contains(H));
	r.compute(_Dummy, P, "eins");
	assertTrue(_Dummy.getParameters().contains(H));
    }

    @Test
    public void shouldUpdateParameter() {
	Relation r = build();
	r.compute(_Dummy, P, "hello");
	assertTrue(_Dummy.getParameters().contains(H));
	assertTrue(_Dummy.getParameter(H).contains("hello@"));
	assertFalse(_Dummy.getParameter(H).contains("world@"));

	r.compute(_Dummy, P, "world");
	assertTrue(_Dummy.getParameter(H).contains("hello@"));
	assertTrue(_Dummy.getParameter(H).contains("world@"));
    }

    @Test
    public void shouldNotUpdateParameter() {
	Relation r = build();
	r.compute(_Dummy, H, "hello world");
	assertEquals(0, _Dummy.getParameters().size());
    }

    @Test
    public void shouldHandleNullValues() {
	Relation r = build();
	r.compute(_Dummy, P, null);
	assertTrue(_Dummy.getParameter(H).contains("null@"));
    }

}
