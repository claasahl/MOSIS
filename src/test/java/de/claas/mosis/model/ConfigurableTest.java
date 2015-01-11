package de.claas.mosis.model;

import de.claas.mosis.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.*;

/**
 * The JUnit test for {@link de.claas.mosis.model.Configurable} classes. It is
 * intended to collect and document a set of test cases that are applicable to
 * all {@link de.claas.mosis.model.Configurable} classes. Please refer to the
 * individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@RunWith(Parameterized.class)
public class ConfigurableTest {

    private final Class<Configurable> _Clazz;
    private Configurable _C;

    /**
     * Initializes this JUnit test for an implementation of the {@link
     * de.claas.mosis.model.Configurable} class.
     *
     * @param clazz implementation of {@link de.claas.mosis.model.Configurable}
     *              class
     */
    public ConfigurableTest(Class<Configurable> clazz) {
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
        _C = Utils.instance(_Clazz);
    }

    @Test
    public void shouldInitializeAllParameters() throws Exception {
        for (String parameter : _C.getParameters()) {
            assertNotNull(_C.getParameter(parameter));
        }
    }

    @Test
    public void shouldReturnNullForUnknownParameter() throws Exception {
        String parameter = Utils.unknownParameter(_C);
        assertNull(_C.getParameter(parameter));
    }

    @Test
    public void shouldReturnParameters() throws Exception {
        assertNotNull(_C.getParameters());
    }

    @Test
    public void shouldUpdateParameters() throws Exception {
        String parameter = Utils.unknownParameter(_C);
        assertFalse(_C.getParameters().contains(parameter));
        _C.setParameter(parameter, "hello world");
        assertTrue(_C.getParameters().contains(parameter));
    }

    @Test
    public void shouldUpdateParameter() throws Exception {
        String parameter = Utils.unknownParameter(_C);
        _C.setParameter(parameter, "hello");
        assertEquals("hello", _C.getParameter(parameter));
        _C.setParameter(parameter, "hello world");
        assertEquals("hello world", _C.getParameter(parameter));
    }

}
