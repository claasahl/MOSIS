package de.claas.mosis.model;

import de.claas.mosis.io.QueueHandler;
import de.claas.mosis.io.UserDatagramProtocolHandler;
import de.claas.mosis.io.format.Serialization;
import de.claas.mosis.io.generator.Function;
import de.claas.mosis.io.generator.Linear;
import de.claas.mosis.io.generator.Random;
import de.claas.mosis.processing.MovingAverage;
import de.claas.mosis.processing.debug.*;
import de.claas.mosis.processing.util.Delay;
import de.claas.mosis.processing.util.Distance;
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
 * The JUnit test for {@link de.claas.mosis.model.ProcessorAdapter} classes. It
 * is intended to collect and document a set of test cases that are applicable
 * to all {@link de.claas.mosis.model.ProcessorAdapter} classes. Please refer to
 * the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link de.claas.mosis.model.ProcessorTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@RunWith(Parameterized.class)
public class ProcessorAdapterTest {

    private final Class<ProcessorAdapter<?, ?>> _Clazz;
    private ProcessorAdapter<?, ?> _P;

    /**
     * Initializes this JUnit test for an implementation of the {@link
     * de.claas.mosis.model.ProcessorAdapter} class.
     *
     * @param clazz implementation of {@link de.claas.mosis.model.ProcessorAdapter}
     *              class
     */
    public ProcessorAdapterTest(Class<ProcessorAdapter<?, ?>> clazz) {
        _Clazz = clazz;
    }

    @Parameters
    public static Collection<?> implementations() {
        List<Object> impl = new Vector<>();
        impl.add(new Object[]{Null.class});
        impl.add(new Object[]{SystemOut.class});
        impl.add(new Object[]{Time.class});
        impl.add(new Object[]{NoOperation.class});
        impl.add(new Object[]{BreakOut.class});
        impl.add(new Object[]{Forward.class});
        impl.add(new Object[]{Delay.class});
        impl.add(new Object[]{Distance.class});
        impl.add(new Object[]{Linear.class});
        impl.add(new Object[]{Function.class});
        impl.add(new Object[]{Serialization.class});
        impl.add(new Object[]{UserDatagramProtocolHandler.class});
        impl.add(new Object[]{QueueHandler.class});
        impl.add(new Object[]{DecoratorProcessor.class});
        impl.add(new Object[]{MovingAverage.class});
        impl.add(new Object[]{Random.class});
        impl.add(new Object[]{de.claas.mosis.io.generator.Time.class});
        impl.add(new Object[]{ToString.class});
        impl.add(new Object[]{Logger.class});
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
    public void shouldHaveData() throws Exception {
        assertFalse(ProcessorAdapter.hasData(null));
        assertFalse(ProcessorAdapter.hasData(Arrays.asList()));
        assertTrue(ProcessorAdapter.hasData(Arrays.asList(1)));
        assertTrue(ProcessorAdapter.hasData(Arrays.asList(42.3)));
    }

    @Test
    public void shouldGetAndSetAsBoolean() throws Exception {
        String parameter = String.format("%s%d", getClass(),
                System.currentTimeMillis());
        _P.addCondition(parameter, new Condition.IsBoolean());
        String[] value = new String[]{"true", "false"};
        Boolean[] exp = new Boolean[]{true, false};
        for (int i = 0; i < value.length; i++) {
            _P.setParameter(parameter, value[i]);
            assertEquals(exp[i], _P.getParameterAsBoolean(parameter));
            assertEquals(value[i], _P.getParameter(parameter));
        }

        for (int i = 0; i < value.length; i++) {
            _P.setParameter(parameter, exp[i]);
            assertEquals(exp[i], _P.getParameterAsBoolean(parameter));
            assertEquals(value[i].toLowerCase(), _P.getParameter(parameter)
                    .toLowerCase());
        }
    }

    @Test
    public void shouldGetAndSetAsInteger() throws Exception {
        String parameter = String.format("%s%d", getClass(),
                System.currentTimeMillis());
        _P.addCondition(parameter, new Condition.IsNumeric());
        String[] value = new String[]{"1", "0", "-23"};
        Integer[] exp = new Integer[]{1, 0, -23};
        for (int i = 0; i < value.length; i++) {
            _P.setParameter(parameter, value[i]);
            assertEquals(exp[i], _P.getParameterAsInteger(parameter));
            assertEquals(value[i], _P.getParameter(parameter));
        }

        for (int i = 0; i < value.length; i++) {
            _P.setParameter(parameter, exp[i]);
            assertEquals(exp[i], _P.getParameterAsInteger(parameter));
            assertEquals(value[i], _P.getParameter(parameter));
        }
    }

    @Test
    public void shouldGetAndSetAsLong() throws Exception {
        String parameter = String.format("%s%d", getClass(),
                System.currentTimeMillis());
        _P.addCondition(parameter, new Condition.IsNumeric());
        String max = String.format("%d", Long.MAX_VALUE);
        String[] value = new String[]{"1", "0", "-23", max};
        Long[] exp = new Long[]{1L, 0L, -23L, Long.MAX_VALUE};
        for (int i = 0; i < value.length; i++) {
            _P.setParameter(parameter, value[i]);
            assertEquals(exp[i], _P.getParameterAsLong(parameter));
            assertEquals(value[i], _P.getParameter(parameter));
        }

        for (int i = 0; i < value.length; i++) {
            _P.setParameter(parameter, exp[i]);
            assertEquals(exp[i], _P.getParameterAsLong(parameter));
            assertEquals(value[i], _P.getParameter(parameter));
        }
    }

    @Test
    public void shouldGetAndSetAsDouble() throws Exception {
        String parameter = String.format("%s%d", getClass(),
                System.currentTimeMillis());
        _P.addCondition(parameter, new Condition.IsNumeric());
        String[] value = new String[]{"1.1", "0.5", "-23.04"};
        Double[] exp = new Double[]{1.1, 0.5, -23.04};
        for (int i = 0; i < value.length; i++) {
            _P.setParameter(parameter, value[i]);
            assertEquals(exp[i], _P.getParameterAsDouble(parameter));
            assertEquals(value[i], _P.getParameter(parameter));
        }

        for (int i = 0; i < value.length; i++) {
            _P.setParameter(parameter, exp[i]);
            assertEquals(exp[i], _P.getParameterAsDouble(parameter));
            assertEquals(value[i], _P.getParameter(parameter));
        }
    }

    @Test
    public void shouldRetainNullAsBoolean() throws Exception {
        String parameter = String.format("%s%d", getClass(),
                System.currentTimeMillis());
        assertNull(_P.getParameter(parameter));
        assertNull(_P.getParameterAsBoolean(parameter));
    }

    @Test
    public void shouldRetainNullAsInteger() throws Exception {
        String parameter = String.format("%s%d", getClass(),
                System.currentTimeMillis());
        assertNull(_P.getParameter(parameter));
        assertNull(_P.getParameterAsInteger(parameter));
    }

    @Test
    public void shouldRetainNullAsLong() throws Exception {
        String parameter = String.format("%s%d", getClass(),
                System.currentTimeMillis());
        assertNull(_P.getParameter(parameter));
        assertNull(_P.getParameterAsLong(parameter));
    }

    @Test
    public void shouldRetainNullAsDouble() throws Exception {
        String parameter = String.format("%s%d", getClass(),
                System.currentTimeMillis());
        assertNull(_P.getParameter(parameter));
        assertNull(_P.getParameterAsDouble(parameter));
    }

    @Test
    public void shouldAddAndRemoveConditions() throws Exception {
        Condition condition = new Condition.IsBoolean();
        String parameter = String.format("%s%d", getClass(),
                System.currentTimeMillis());

        _P.addCondition(parameter, condition);
        _P.removeCondition(parameter, condition);
        _P.setParameter(parameter, "no boolean");

        _P.addCondition(parameter, new Condition.IsBoolean());
        _P.removeCondition(parameter, new Condition.IsBoolean());
        _P.setParameter(parameter, "non numeric");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCheckConditionBeforeSettingParameter() throws Exception {
        String parameter = String.format("%s%d", getClass(),
                System.currentTimeMillis());

        _P.setParameter(parameter, "no boolean");
        assertEquals("no boolean", _P.getParameter(parameter));
        _P.addCondition(parameter, new Condition.IsBoolean());
        assertEquals("no boolean", _P.getParameter(parameter));

        _P.setParameter(parameter, parameter);
        assertEquals(parameter, _P.getParameter(parameter));

        _P.setParameter(parameter, "no boolean");
    }

    @Test
    public void shouldCheckConditionOnlyIfParameterChanges() throws Exception {
        String parameter = String.format("%s%d", getClass(),
                System.currentTimeMillis());

        _P.setParameter(parameter, parameter);
        assertEquals(parameter, _P.getParameter(parameter));
        _P.addCondition(parameter, new Condition.IsBoolean());
        _P.setParameter(parameter, parameter);
        assertEquals(parameter, _P.getParameter(parameter));
    }

    @Test
    public void shouldAddAndRemoveRelations() throws Exception {
        Relation relation = new Relation.UpdateVersion();
        assertNull(_P.getParameter(Relation.UpdateVersion.Version));

        _P.addRelation(relation);
        _P.setParameter("some parameter 1", "some value 1");
        assertEquals((Integer) 1,
                _P.getParameterAsInteger(Relation.UpdateVersion.Version));
        _P.removeRelation(relation);
        _P.setParameter("some parameter 2", "some value 2");
        assertEquals((Integer) 1,
                _P.getParameterAsInteger(Relation.UpdateVersion.Version));

        _P.addRelation(new Relation.UpdateVersion());
        _P.setParameter("some parameter 3", "some value 3");
        assertEquals((Integer) 2,
                _P.getParameterAsInteger(Relation.UpdateVersion.Version));
        _P.removeRelation(new Relation.UpdateVersion());
        _P.setParameter("some parameter 4", "some value 4");
        assertEquals((Integer) 2,
                _P.getParameterAsInteger(Relation.UpdateVersion.Version));
    }

    @Test
    public void shouldApplyRelationAfterSettingParameter() throws Exception {
        String parameter = String.format("%s%d", getClass(),
                System.currentTimeMillis());
        Dummy dummy = new Dummy();

        assertEquals(0, dummy.AccessCount);
        _P.setParameter(parameter, "a");
        _P.addRelation(dummy);
        assertEquals("a", _P.getParameter(parameter));
        _P.setParameter(parameter, "b");
        assertEquals("b", _P.getParameter(parameter));
        assertEquals(1, dummy.AccessCount);
    }

    @Test
    public void shouldApplyRelationOnlyIfParameterChanges() throws Exception {
        String parameter = String.format("%s%d", getClass(),
                System.currentTimeMillis());
        Dummy dummy = new Dummy();

        assertEquals(0, dummy.AccessCount);
        _P.setParameter(parameter, parameter);
        assertEquals(parameter, _P.getParameter(parameter));
        _P.addRelation(dummy);
        _P.setParameter(parameter, parameter);
        assertEquals(parameter, _P.getParameter(parameter));
        assertEquals(0, dummy.AccessCount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowException() throws Exception {
        Condition condition = new Condition.IsBoolean();
        String parameter = String.format("%s%d", getClass(),
                System.currentTimeMillis());

        _P.addCondition(parameter, condition);
        _P.setParameter(parameter, "no boolean");
    }

    /**
     * The class {@link de.claas.mosis.model.ProcessorAdapterTest.Dummy}. It is
     * intended to verify how often a {@link de.claas.mosis.model.Relation} is
     * accessed and when it is accessed.
     *
     * @author Claas Ahlrichs (claasahl@tzi.de)
     */
    private static class Dummy implements Relation {

        public int AccessCount = 0;

        @Override
        public void compute(Configurable configurable, String parameter,
                            String value) {
            AccessCount++;
            if (!configurable.getParameter(parameter).equals(value)) {
                AccessCount = Integer.MIN_VALUE;
            }
        }

    }

}
