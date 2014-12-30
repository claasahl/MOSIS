package de.claas.mosis.io.generator;

import de.claas.mosis.model.ProcessorAdapterTest;
import de.claas.mosis.model.ProcessorTest;
import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link Random}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link ProcessorTest} and
 * {@link ProcessorAdapterTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class RandomTest {

    private Random _R;

    @Before
    public void before() throws Exception {
        _R = new Random();
        _R.setParameter(Random.SEED, "0");
        _R.setUp();
    }

    @After
    public void after() {
        _R.dismantle();
    }

    @Test
    public void assumptionsOnParameterSeed() {
        assertEquals("0", _R.getParameter(Random.SEED));
    }

    @Test
    public void assumptionsOnParameterLower() {
        assertEquals("0", _R.getParameter(Random.LOWER));
    }

    @Test
    public void assumptionsOnParameterUpper() {
        assertEquals("1", _R.getParameter(Random.UPPER));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterSeedMayNotBeNull() throws Exception {
        _R.setParameter(Random.SEED, Long.toString(System.currentTimeMillis()));
        _R.setParameter(Random.SEED, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterSeedMustBeAnInteger() throws Exception {
        _R.setParameter(Random.SEED, Long.toString(System.currentTimeMillis()));
        try {
            _R.setParameter(Random.SEED, "1");
            _R.setParameter(Random.SEED, "12");
        } catch (Exception e) {
            fail(e.toString());
        }
        _R.setParameter(Random.SEED, "1.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterLowerMayNotBeNull() throws Exception {
        _R.setParameter(Random.SEED, Long.toString(System.currentTimeMillis()));
        _R.setParameter(Random.LOWER, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterLowerMustBeNummeric() throws Exception {
        _R.setParameter(Random.SEED, Long.toString(System.currentTimeMillis()));
        _R.setParameter(Random.UPPER, Double.toString(Double.MAX_VALUE));
        try {
            _R.setParameter(Random.LOWER, "0.0");
            _R.setParameter(Random.LOWER, "-23");
            _R.setParameter(Random.LOWER, "42");
        } catch (Exception e) {
            fail(e.toString());
        }
        _R.setParameter(Random.LOWER, "maybe");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterUpperMayNotBeNull() throws Exception {
        _R.setParameter(Random.SEED, Long.toString(System.currentTimeMillis()));
        _R.setParameter(Random.UPPER, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterUpperMustBeNummeric() throws Exception {
        _R.setParameter(Random.SEED, Long.toString(System.currentTimeMillis()));
        _R.setParameter(Random.LOWER, Double.toString(Double.MIN_VALUE));
        try {
            _R.setParameter(Random.UPPER, "0.0");
            _R.setParameter(Random.UPPER, "-23");
            _R.setParameter(Random.UPPER, "42");
        } catch (Exception e) {
            fail(e.toString());
        }
        _R.setParameter(Random.UPPER, "maybe");
    }

    @Test
    public void shouldHaveRecentSeed() {
        _R.setParameter(Random.SEED, Long.toString(System.currentTimeMillis()));
        long seed = Long.parseLong(_R.getParameter(Random.SEED));
        assertTrue(seed + 5 > System.currentTimeMillis());
    }

    @Test
    public void shouldReturnRandomNumbers() {
        _R.setParameter(Random.SEED, Long.toString(System.currentTimeMillis()));
        Set<Double> numbers = new HashSet<>();
        assertTrue(numbers.add(Utils.process(_R)));
        assertTrue(numbers.add(Utils.process(_R)));
        assertTrue(numbers.add(Utils.process(_R)));
        assertTrue(numbers.add(Utils.process(_R)));
    }

    @Test
    public void shouldReturnPredictableRandomNumbers() {
        assertEquals(0.7309677, Utils.process(_R), 0.0001);
        assertEquals(0.2405364, Utils.process(_R), 0.0001);
        assertEquals(0.6374174, Utils.process(_R), 0.0001);
        assertEquals(0.5504300, Utils.process(_R), 0.0001);
    }

    @Test
    public void shouldReturnNumberWithinBoundaries() {
        _R.setParameter(Random.SEED, Long.toString(System.currentTimeMillis()));
        _R.setParameter(Random.LOWER, "0.5");
        _R.setParameter(Random.UPPER, "0.7");
        for (int i = 0; i < 1000; i++) {
            assertTrue(Utils.process(_R) >= 0.5);
            assertTrue(Utils.process(_R) <= 0.7);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldEnsureThatLowerIsBelowUpper() {
        _R.setParameter(Random.SEED, Long.toString(System.currentTimeMillis()));
        try {
            _R.setParameter(Random.LOWER, "0.5");
            _R.setParameter(Random.UPPER, "0.7");
        } catch (Exception e) {
            fail(e.toString());
        }
        _R.setParameter(Random.LOWER, "0.7");
        _R.setParameter(Random.UPPER, "0.5");
    }

}
