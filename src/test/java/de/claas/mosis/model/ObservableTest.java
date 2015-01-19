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
 * The JUnit test for {@link de.claas.mosis.model.Observable} classes. It is
 * intended to collect and document a set of test cases that are applicable to
 * all {@link de.claas.mosis.model.Observable} classes. Please refer to the
 * individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Ignore
@RunWith(Parameterized.class)
public class ObservableTest {

    private final Class<Observable> _Clazz;
    private Observable _O;

    /**
     * Initializes this JUnit test for an implementation of the {@link
     * de.claas.mosis.model.Observable} class.
     *
     * @param clazz implementation of {@link de.claas.mosis.model.Observable}
     *              class
     */
    public ObservableTest(Class<Observable> clazz) {
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
    public void dummy() {
        fail();
    }

}
