package de.claas.mosis.flow;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit test for class {@link de.claas.mosis.flow.LinkAdapter}. It is
 * intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p>
 * Additional test cases can be found in {@link de.claas.mosis.flow.LinkTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class LinkAdapterTest {

    private LinkAdapter _L;

    @Before
    public void before() throws Exception {
        _L = new LinkAdapter();
    }

    @Test
    public void shouldAcceptEverything() throws Exception {
        assertTrue(_L.push(Arrays.<Object>asList(1)));
        assertTrue(_L.push(Arrays.<Object>asList(42.3)));
        assertTrue(_L.push(Arrays.<Object>asList("hello world")));
        assertTrue(_L.push(Arrays.asList((Object) null)));
        assertTrue(_L.push(Arrays.<Object>asList(-17L)));
        assertTrue(_L.push(Arrays.asList(new Object())));
    }

    @Test
    public void shouldNotBeLimited() throws Exception {
        for (int i = 0; i < 10000; i++) {
            assertTrue(_L.push(Arrays.<Object>asList(i)));
        }
        for (int i = 0; i < 10000; i++) {
            assertEquals(i, _L.poll());
        }
    }

}
