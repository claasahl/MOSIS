package de.claas.mosis.processing.relation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.claas.mosis.model.ConfigurableAdapter;
import de.claas.mosis.model.Relation;
import de.claas.mosis.model.Relation.UpdateVersion;
import de.claas.mosis.model.RelationTest;

/**
 * The JUnit test for class {@link UpdateVersion}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * 
 * Additional test cases can be found in {@link RelationTest}.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class UpdateVersionTest {

    private ConfigurableAdapter _Dummy;

    /**
     * Returns an instantiated {@link UpdateVersion} class. If appropriate, the
     * instance is configured with default values.
     * 
     * @return an instantiated {@link UpdateVersion} class
     */
    private Relation build() {
	return new Relation.UpdateVersion();
    }

    @Before
    public void setUp() {
	_Dummy = new ConfigurableAdapter();
    }

    @Test
    public void shouldAddParameter() {
	Relation r = build();
	assertFalse(_Dummy.getParameters().contains(
		Relation.UpdateVersion.Version));
	r.compute(_Dummy, null, null);
	assertTrue(_Dummy.getParameters().contains(
		Relation.UpdateVersion.Version));
    }

    @Test
    public void shouldUpdateParameter() {
	Relation r = build();
	r.compute(_Dummy, null, null);
	String v1 = _Dummy.getParameter(Relation.UpdateVersion.Version);
	r.compute(_Dummy, null, null);
	String v2 = _Dummy.getParameter(Relation.UpdateVersion.Version);
	assertEquals(Integer.parseInt(v1) + 1, Integer.parseInt(v2));
    }

    @Test
    public void shouldNotUpdateParameter() {
	Relation r = build();
	r.compute(_Dummy, null, null);
	String v1 = _Dummy.getParameter(Relation.UpdateVersion.Version);
	r.compute(_Dummy, Relation.UpdateVersion.Version, null);
	String v2 = _Dummy.getParameter(Relation.UpdateVersion.Version);
	assertEquals(v1, v2);
    }

}
