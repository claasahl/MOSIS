package de.claas.mosis.model;

import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit test for {@link Processor} classes. It is intended to collect and
 * document a set of test cases that are applicable to all {@link Processor}
 * classes. Please refer to the individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@RunWith(Parameterized.class)
public class ProcessorTest {

    private final Class<Processor<?, ?>> _Clazz;
    private Processor<?, ?> _P;

    /**
     * Initializes this JUnit test for an implementation of the
     * {@link Processor} class.
     *
     * @param clazz implementation of {@link Processor} class
     */
    public ProcessorTest(Class<Processor<?, ?>> clazz) {
        _Clazz = clazz;
    }

    @Parameters
    public static Collection<?> implementations() {
        List<Object> impl = new Vector<>();
        impl.addAll(ProcessorAdapterTest.implementations());
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
    public void shouldHaveEmptyConstructor() throws Exception {
        boolean hasEmptyConstructor = false;
        for (Constructor<?> constructor : _Clazz.getConstructors()) {
            if (constructor.getParameterTypes().length == 0) {
                hasEmptyConstructor = true;
                break;
            }
        }
        assertTrue(hasEmptyConstructor);
    }

    @Ignore
    public void shouldHaveAnnotationForClass() throws Exception {
        assertNotNull(_P.getClass().getAnnotation(Documentation.class));
    }

    @Ignore
    public void shouldHaveAnnotationForParameters() throws Exception {
        for (String param : _P.getParameters()) {
            boolean found = false;
            for (Field field : _P.getClass().getFields()) {
                found |= param.equals(field.get(_P))
                        && field.getAnnotation(Parameter.class) != null;
            }
            assertTrue("Parameter (" + param + ") is not annotated.", found);
        }
    }

}
