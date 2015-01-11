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
 * The JUnit test for {@link de.claas.mosis.model.Condition} classes. It is
 * intended to collect and document a set of test cases that are applicable to
 * all {@link de.claas.mosis.model.Condition} classes. Please refer to the
 * individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@RunWith(Parameterized.class)
public class ConditionTest {

    private final Class<Condition> _Clazz;
    private final Object[] _Args;

    /**
     * Initializes this JUnit test for an implementation of the {@link
     * de.claas.mosis.model.Condition} class.
     *
     * @param clazz implementation of {@link de.claas.mosis.model.Condition}
     *              class
     * @param args  arguments for instantiating the implementing class
     */
    public ConditionTest(Class<Condition> clazz, Object[] args) {
        _Clazz = clazz;
        _Args = args;
    }

    @Parameters
    public static Collection<?> implementations() {
        List<String> list = new Vector<>();
        list.add("a");
        list.add("c");
        List<Object> impl = new Vector<>();
        impl.add(new Object[]{Condition.ClassExists.class, new Object[0]});
        impl.add(new Object[]{Condition.FileExists.class, new Object[0]});
        impl.add(new Object[]{Condition.IsBoolean.class, new Object[0]});
        impl.add(new Object[]{Condition.IsGreaterOrEqual.class,
                new Object[]{42.0}});
        impl.add(new Object[]{Condition.IsGreaterThan.class,
                new Object[]{23.0}});
        impl.add(new Object[]{Condition.IsInList.class, new Object[]{list}});
        impl.add(new Object[]{Condition.IsInteger.class, new Object[0]});
        impl.add(new Object[]{Condition.IsLessOrEqual.class,
                new Object[]{23.0}});
        impl.add(new Object[]{Condition.IsLessThan.class,
                new Object[]{42.0}});
        impl.add(new Object[]{Condition.IsNotNull.class, new Object[0]});
        impl.add(new Object[]{Condition.IsNot.class,
                new Object[]{new Condition.IsNumeric()}});
        impl.add(new Object[]{Condition.IsNumeric.class, new Object[0]});
        impl.add(new Object[]{Condition.ReadOnly.class, new Object[0]});
        impl.add(new Object[]{Condition.RegularExpression.class,
                new Object[]{"abc", "abc"}});
        impl.add(new Object[]{Condition.WriteOnce.class, new Object[0]});
        return impl;
    }

    /**
     * Returns an instantiated {@link de.claas.mosis.model.Condition} class. The
     * concrete class of the instantiated object and its arguments are provided
     * by the {@link #_Clazz} field and {@link #_Args} field, respectively.
     *
     * @return an instantiated {@link de.claas.mosis.model.Condition} class
     * @see de.claas.mosis.util.Utils#instance(Class, Object...)
     */
    private Condition build() throws Exception {
        return Utils.instance(_Clazz, _Args);
    }

    @Test
    public void shouldOverrideEquals() throws Exception {
        Method m = _Clazz.getMethod("equals", Object.class);
        assertNotSame(Object.class, m.getDeclaringClass());
    }

    @Test
    public void shouldImplementEquals() throws Exception {
        Condition c1 = build();
        Condition c2 = build();
        assertTrue(c1.equals(c1));
        assertTrue(c1.equals(c2));
        assertTrue(c2.equals(c1));
        assertFalse(c1 == null);
        assertFalse(c1.equals(new Object()));
    }

    /**
     * Implementations must override {@link #hashCode()}. This method must be
     * implemented as a consequence of overriding {@link #equals(Object)}. See
     * general documentation of {@link #equals(Object)} method for details.
     */
    @Test
    public void shouldOverrideHashCode() throws Exception {
        Method m = _Clazz.getMethod("hashCode");
        assertNotSame(Object.class, m.getDeclaringClass());
    }

    /**
     * Implementation must properly realize {@link #hashCode()}. See general
     * documentation of {@link #hashCode()} method for details.
     */
    @Test
    public void shouldImplementHashCode() throws Exception {
        Condition c1 = build();
        Condition c2 = build();
        assertEquals(c1.hashCode(), c2.hashCode());
        assertEquals(c1.hashCode(), c1.hashCode());
        assertEquals(c2.hashCode(), c2.hashCode());
    }

    @Test
    public void shouldOverrideToString() throws Exception {
        Method m = _Clazz.getMethod("toString");
        assertNotSame(Object.class, m.getDeclaringClass());
    }

    @Test
    public void shouldReturnNonEmptyString() throws Exception {
        Condition c = build();
        assertNotNull(c.toString());
        assertFalse(c.toString().isEmpty());
    }

    @Test
    public void shouldHandleNullValues() throws Exception {
        Condition c = build();
        c.complies(null, null);
    }
}
