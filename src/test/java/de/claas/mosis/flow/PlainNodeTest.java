package de.claas.mosis.flow;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.claas.mosis.processing.debug.Null;

/**
 * The JUnit test for class {@link PlainNode}. It is intended to collect and document
 * a set of test cases for the tested class. Please refer to the individual
 * tests for more detailed information.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class PlainNodeTest extends NodeTest {
    
    @Override
    protected PlainNode build() {
	return new PlainNode(new Null());
    }

    @Test
    public void shouldHaveProcessor() {
	PlainNode n = build();
	assertEquals(Null.class, n.getProcessor().getClass());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void shouldNotAllowNullValue() {
	new PlainNode(null);
    }

}
