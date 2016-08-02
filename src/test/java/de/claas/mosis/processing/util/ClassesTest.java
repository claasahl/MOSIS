package de.claas.mosis.processing.util;

import de.claas.mosis.io.generator.Function;
import de.claas.mosis.processing.debug.Forward;
import de.claas.mosis.processing.debug.Null;
import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.processing.util.Classes}. It
 * is intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p>
 * Additional test cases can be found in {@link de.claas.mosis.model.ProcessorTest}
 * and {@link de.claas.mosis.model.ProcessorAdapterTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class ClassesTest {

    private Classes _P;

    @Before
    public void before() throws Exception {
        _P = new Classes();
        _P.setUp();
    }

    @After
    public void after() {
        _P.dismantle();
    }

    @Test
    public void assumptionsOnParameterClassPath() {
        assertNotNull(_P.getParameter(Classes.CLASSPATH));
        assertFalse(_P.getParameter(Classes.CLASSPATH).isEmpty());
        assertEquals(System.getProperty("java.class.path"),
                _P.getParameter(Classes.CLASSPATH));
    }

    @Test
    public void assumptionsOnParameterSeparator() {
        assertNotNull(_P.getParameter(Classes.SEPARATOR));
        assertFalse(_P.getParameter(Classes.SEPARATOR).isEmpty());
        assertEquals(System.getProperty("path.separator"),
                _P.getParameter(Classes.SEPARATOR));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterClassPathMayNotBeNull() {
        Utils.updateParameter(_P, Classes.CLASSPATH, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterSeparatorMayNotBeNull() {
        Utils.updateParameter(_P, Classes.SEPARATOR, null);
    }

    @Test
    public void shouldReturnManyClasses() {
        assertTrue(Utils.processAll(_P).size() > 300);
    }

    @Test
    public void shouldAlsoReturnProcessorClasses() {
        List<String> classes = Utils.processAll(_P);
        assertTrue(classes.contains(Null.class.getName()));
        assertTrue(classes.contains(Classes.class.getName()));
        assertTrue(classes.contains(Forward.class.getName()));
        assertTrue(classes.contains(Function.class.getName()));
    }

}
