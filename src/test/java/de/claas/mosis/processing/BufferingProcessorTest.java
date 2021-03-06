package de.claas.mosis.processing;

import de.claas.mosis.processing.util.Convolution;
import de.claas.mosis.processing.util.Delay;
import de.claas.mosis.util.Utils;
import org.junit.After;
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
 * The JUnit test for {@link de.claas.mosis.processing.BufferingProcessor}
 * classes. It is intended to collect and document a set of test cases that are
 * applicable to all {@link de.claas.mosis.processing.BufferingProcessor}
 * classes. Please refer to the individual tests for more detailed information.
 * <p>
 * Additional test cases can be found in {@link de.claas.mosis.model.ProcessorTest}
 * and {@link de.claas.mosis.model.ProcessorAdapterTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@RunWith(Parameterized.class)
public class BufferingProcessorTest {

    private final Class<BufferingProcessor<Object, Object>> _Clazz;
    private BufferingProcessor<Object, Object> _P;

    /**
     * Initializes this JUnit test for an implementation of the {@link
     * de.claas.mosis.processing.BufferingProcessor} class.
     *
     * @param clazz implementation of {@link de.claas.mosis.processing.BufferingProcessor}
     *              class
     */
    public BufferingProcessorTest(
            Class<BufferingProcessor<Object, Object>> clazz) {
        _Clazz = clazz;
    }

    @Parameters
    public static Collection<?> implementations() {
        List<Object> impl = new Vector<>();
        impl.add(new Object[]{Convolution.class});
        impl.add(new Object[]{Delay.class});
        impl.add(new Object[]{MovingAverage.class});
        return impl;
    }

    @Before
    public void before() throws Exception {
        _P = Utils.instance(_Clazz);
        _P.setUp();
    }

    @After
    public void after() {
        _P.dismantle();
    }

    @Test
    public void assumptionsOnWindowSize() throws Exception {
        assertEquals("0", _P.getParameter(BufferingProcessor.WINDOW_SIZE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterWindowSizeMayNotBeNull() throws Exception {
        Utils.updateParameter(_P, BufferingProcessor.WINDOW_SIZE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterWindowSizeMustBeAnInteger() throws Exception {
        try {
            Utils.updateParameters(_P,
                    BufferingProcessor.WINDOW_SIZE, "1",
                    BufferingProcessor.WINDOW_SIZE, "12");
        } catch (Exception e) {
            fail(e.toString());
        }
        Utils.updateParameter(_P, BufferingProcessor.WINDOW_SIZE, "1.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterWindowSizeMustBePositive() throws Exception {
        try {
            Utils.updateParameters(_P,
                    BufferingProcessor.WINDOW_SIZE, "0",
                    BufferingProcessor.WINDOW_SIZE, "12");
        } catch (Exception e) {
            fail(e.toString());
        }
        Utils.updateParameter(_P, BufferingProcessor.WINDOW_SIZE, "-1");
    }

    @Test
    public void shouldBufferEnoughData() throws Exception {
        Utils.updateParameter(_P, BufferingProcessor.WINDOW_SIZE, "5");
        for (int i = 0; i < 10; i++) {
            List<Object> data;
            if (i < 5) {
                data = Arrays.asList(new Object[]{i});
                assertNull(_P.appendAndRemove(data));
            } else {
                data = Arrays.asList(new Object[]{i});
                assertEquals(i - 5, _P.appendAndRemove(data).get(0));
            }
        }
    }

    @Test
    public void shouldBeFull() throws Exception {
        Utils.updateParameter(_P, BufferingProcessor.WINDOW_SIZE, "5");
        for (int i = 0; i < 10; i++) {
            if (i < 5) {
                assertFalse(_P.isBufferFull());
            } else {
                assertTrue(_P.isBufferFull());
            }
            _P.appendAndRemove(Arrays.asList(new Object[]{i}));
        }
    }

    @Test
    public void shouldNotBuffer() throws Exception {
        assertEquals(Arrays.<Object>asList(1.0),
                _P.appendAndRemove(Arrays.<Object>asList(1.0)));
        assertEquals(Arrays.<Object>asList(5.0),
                _P.appendAndRemove(Arrays.<Object>asList(5.0)));
        assertEquals(Arrays.<Object>asList(1.3),
                _P.appendAndRemove(Arrays.<Object>asList(1.3)));
    }

    @Test
    public void shouldBuffer() throws Exception {
        Utils.updateParameter(_P, BufferingProcessor.WINDOW_SIZE, "5");
        assertNull(_P.appendAndRemove(Arrays.<Object>asList(1.0)));
        assertNull(_P.appendAndRemove(Arrays.<Object>asList(1.0)));
        assertNull(_P.appendAndRemove(Arrays.<Object>asList(1.0)));
        assertNull(_P.appendAndRemove(Arrays.<Object>asList(1.0)));
        assertNull(_P.appendAndRemove(Arrays.<Object>asList(1.0)));
        assertNotNull(_P.appendAndRemove(Arrays.<Object>asList(1.0)));
        assertTrue(_P.isBufferFull());
    }

    @Test
    public void shouldBeInOrder() throws Exception {
        Utils.updateParameter(_P, BufferingProcessor.WINDOW_SIZE, "5");
        assertNull(_P.appendAndRemove(Arrays.<Object>asList(1.0)));
        assertNull(_P.appendAndRemove(Arrays.<Object>asList(2.0)));
        assertNull(_P.appendAndRemove(Arrays.<Object>asList(3.0)));
        assertNull(_P.appendAndRemove(Arrays.<Object>asList(4.0)));
        assertNull(_P.appendAndRemove(Arrays.<Object>asList(5.0)));
        assertEquals(Arrays.<Object>asList(1.0),
                _P.appendAndRemove(Arrays.<Object>asList(6.0)));
        assertEquals(Arrays.<Object>asList(2.0),
                _P.appendAndRemove(Arrays.<Object>asList(7.0)));
        assertEquals(Arrays.<Object>asList(3.0),
                _P.appendAndRemove(Arrays.<Object>asList(8.0)));
        assertEquals(Arrays.<Object>asList(4.0),
                _P.appendAndRemove(Arrays.<Object>asList(9.0)));
        assertEquals(Arrays.<Object>asList(5.0),
                _P.appendAndRemove(Arrays.<Object>asList(10.0)));
        assertEquals(Arrays.<Object>asList(6.0),
                _P.appendAndRemove(Arrays.<Object>asList(11.0)));
    }

    @Test
    public void shouldHandleNullValues() throws Exception {
        Utils.updateParameter(_P, BufferingProcessor.WINDOW_SIZE, "5");
        assertNull(_P.appendAndRemove(Arrays.<Object>asList(1.0)));
        assertNull(_P.appendAndRemove(Arrays.asList(null, null)));
        assertNull(_P.appendAndRemove(Arrays.asList(null, null)));
        assertNull(_P.appendAndRemove(Arrays.asList(null, null)));
        assertNull(_P.appendAndRemove(Arrays.asList(null, null)));
        assertEquals(Arrays.<Object>asList(1.0),
                _P.appendAndRemove(Arrays.<Object>asList(6.0)));
        assertEquals(Arrays.asList(null, null),
                _P.appendAndRemove(Arrays.asList(null, null)));
    }

    @Test
    public void shouldCopyInputs() throws Exception {
        List<Object> in = new Vector<>();
        Utils.updateParameter(_P, BufferingProcessor.WINDOW_SIZE, "5");
        in.add(23);
        _P.appendAndRemove(in);
        in.add(42);
        _P.appendAndRemove(in);
        assertEquals(1, _P.getBuffer().get(0).size());
        assertEquals(2, _P.getBuffer().get(1).size());
    }

    @Test
    public void shouldBufferAllInputs() throws Exception {
        Utils.updateParameter(_P, BufferingProcessor.WINDOW_SIZE, "5");
        _P.appendAndRemove(Arrays.<Object>asList(1));
        _P.appendAndRemove(Arrays.<Object>asList(2, 3));
        _P.appendAndRemove(Arrays.<Object>asList(3, 4, 5));

        assertEquals(Arrays.<Object>asList(1), _P.getBuffer().get(0));
        assertEquals(Arrays.<Object>asList(2, 3), _P.getBuffer().get(1));
        assertEquals(Arrays.<Object>asList(3, 4, 5), _P.getBuffer().get(2));
    }

}
