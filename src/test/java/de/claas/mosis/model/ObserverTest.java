package de.claas.mosis.model;

import de.claas.mosis.util.Utils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.fail;

/**
 * The JUnit test for {@link de.claas.mosis.model.Observer} classes. It is
 * intended to collect and document a set of test cases that are applicable to
 * all {@link de.claas.mosis.model.Observer} classes. Please refer to the
 * individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Ignore
@RunWith(Parameterized.class)
public class ObserverTest {

    private final Class<Observer> _Clazz;
    private Observer _O;

    /**
     * Initializes this JUnit test for an implementation of the {@link
     * de.claas.mosis.model.Observer} class.
     *
     * @param clazz implementation of {@link de.claas.mosis.model.Observer}
     *              class
     */
    public ObserverTest(Class<Observer> clazz) {
        _Clazz = clazz;
    }

    @Parameters
    public static Collection<?> implementations() {
        List<Object> impl = new Vector<>();
        impl.addAll(ConfigurableAdapterTest.implementations());
        return impl;
    }

    @Before
    public void before() throws Exception {
        _O = Utils.instance(_Clazz);
    }

    @Test
    public void shouldNotThrowException() {
        fail();
    }

}
