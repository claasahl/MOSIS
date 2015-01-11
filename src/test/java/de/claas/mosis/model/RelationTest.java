package de.claas.mosis.model;

import de.claas.mosis.util.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.*;

/**
 * The JUnit test for {@link de.claas.mosis.model.Relation} classes. It is
 * intended to collect and document a set of test cases that are applicable to
 * all {@link de.claas.mosis.model.Relation} classes. Please refer to the
 * individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@RunWith(Parameterized.class)
public class RelationTest {

    private final Class<Relation> _Clazz;
    private final Object[] _Args;

    /**
     * Initializes this JUnit test for an implementation of the {@link
     * de.claas.mosis.model.Relation} class.
     *
     * @param clazz implementation of {@link de.claas.mosis.model.Relation}
     *              class
     * @param args  arguments for instantiating the implementing class
     */
    public RelationTest(Class<Relation> clazz, Object[] args) {
        _Clazz = clazz;
        _Args = args;
    }

    @Parameters
    public static Collection<?> implementations() {
        List<Object> impl = new Vector<>();
        impl.add(new Object[]{Relation.LastChanged.class, new Object[]{}});
        impl.add(new Object[]{Relation.ParameterHistory.class,
                new Object[]{}});
        impl.add(new Object[]{Relation.UpdateVersion.class, new Object[]{}});
        return impl;
    }

    /**
     * Returns an instantiated {@link de.claas.mosis.model.Relation} class. The
     * concrete class of the instantiated object and its arguments are provided
     * by the {@link #_Clazz} field and {@link #_Args} field, respectively.
     *
     * @return an instantiated {@link de.claas.mosis.model.Relation} class
     * @see de.claas.mosis.util.Utils#instance(Class, Object...)
     */
    private Relation build() throws Exception {
        return Utils.instance(_Clazz, _Args);
    }

    @Test
    public void shouldOverrideEquals() throws Exception {
        Method m = _Clazz.getMethod("equals", Object.class);
        assertNotSame(Object.class, m.getDeclaringClass());
    }

    // TODO Require overriding hashCode? See documentation of equals method.

    /**
     * Implementations must override {@link #hashCode()}. This method must be
     * implemented as a consequence of overriding {@link #equals(Object)}. See
     * general documentation of {@link #equals(Object)} method for details.
     */
    // public void shouldOverrideHashCode() throws Exception {}
    @Test
    public void shouldImplementEquals() throws Exception {
        Relation r1 = build();
        Relation r2 = build();
        assertTrue(r1.equals(r1));
        assertTrue(r1.equals(r2));
        assertTrue(r2.equals(r1));
        assertFalse(r1 == null);
        assertFalse(r1.equals(new Object()));
    }

    /**
     * Implementation must properly realize {@link #hashCode()}. See general
     * documentation of {@link #hashCode()} method for details.
     */
    // public void shouldImplementHashCode() throws Exception {}
    @Test
    public void shouldHandleNullValues() throws Exception {
        Relation r = build();
        r.compute(new ConfigurableAdapter(), null, null);
    }

}
