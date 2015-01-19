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
 * The JUnit test for {@link de.claas.mosis.model.ObservableAdapter} classes. It
 * is intended to collect and document a set of test cases that are applicable
 * to all {@link de.claas.mosis.model.ObservableAdapter} classes. Please refer
 * to the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link de.claas.mosis.model.ConfigurableTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Ignore
@RunWith(Parameterized.class)
public class ObservableAdapterTest {

    private final Class<ObservableAdapter> _Clazz;
    private ObservableAdapter _C;

    /**
     * Initializes this JUnit test for an implementation of the {@link
     * de.claas.mosis.model.ObservableAdapter} class.
     *
     * @param clazz implementation of {@link de.claas.mosis.model.ObservableAdapter}
     *              class
     */
    public ObservableAdapterTest(Class<ObservableAdapter> clazz) {
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
    public void dummy() {
        fail();
    }

}
