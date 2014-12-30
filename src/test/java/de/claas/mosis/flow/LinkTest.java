package de.claas.mosis.flow;

import de.claas.mosis.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.*;

/**
 * The JUnit test for {@link Link} classes. It is intended to collect and
 * document a set of test cases that are applicable to all {@link Link} classes.
 * Please refer to the individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@RunWith(Parameterized.class)
public class LinkTest {

    private Class<Link> _Clazz;
    private Object[] _Args;
    private Link _L;

    /**
     * Initializes this JUnit test for an implementation of the {@link Link}
     * class.
     *
     * @param clazz implementation of {@link Link} class
     * @param args  arguments for instantiating the implementing class
     */
    public LinkTest(Class<Link> clazz, Object[] args) {
        _Clazz = clazz;
        _Args = args;
    }

    @Before
    public void before() throws Exception {
        _L = Utils.instance(_Clazz);
    }

    @Test
    public void shouldBeEmpty() throws Exception {
        assertTrue(_L.isEmpty());
        _L.push(new Vector<Object>());
        assertTrue(_L.isEmpty());
    }

    @Test
    public void shouldNotBeEmpty() throws Exception {
        assertTrue(_L.isEmpty());
        for (Object arg : _Args) {
            _L.push(Arrays.asList(arg));
            assertFalse(_L.isEmpty());
        }
    }

    @Test
    public void shouldAcceptObjects() throws Exception {
        for (Object arg : _Args) {
            assertTrue(_L.push(Arrays.asList(arg)));
        }
        assertTrue(_L.push(Arrays.asList(_Args)));
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotAllowNullValues() throws Exception {
        _L.push(null);
    }

    @Test
    public void shouldBuffer() throws Exception {
        assertTrue(_L.push(Arrays.asList(_Args)));
        for (Object arg : _Args) {
            assertEquals(arg, _L.poll());
        }
        assertTrue(_L.isEmpty());
    }

    @Parameters
    public static Collection<?> implementations() {
        List<Object> impl = new Vector<Object>();
        impl.add(new Object[]{
                LinkAdapter.class,
                new Object[]{null, -23, 1L, 42.3, "hello world", new Object()}});
        impl.add(new Object[]{
                UnbiasedLink.class,
                new Object[]{null, -23, 1L, 42.3, "hello world", new Object()}});
        impl.add(new Object[]{
                BiasedLink.class,
                new Object[]{-23, 1L, 42.3, "hello world", new Object()}});
        return impl;
    }

}
