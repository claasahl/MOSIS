package de.claas.mosis.model;

import de.claas.mosis.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit test for {@link de.claas.mosis.model.Observable} classes. It is
 * intended to collect and document a set of test cases that are applicable to
 * all {@link de.claas.mosis.model.Observable} classes. Please refer to the
 * individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@RunWith(Parameterized.class)
public class ObservableTest {

    private final Class<Observable> _Clazz;
    private Observable _O;
    private TestObserver _Observer;

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
        _Observer = new TestObserver();
    }

    @Test
    public void assumptionsOnTestObserver() {
        assertEquals(0, _Observer.callsToUpdate);
        assertTrue(_Observer.updatedObservers.isEmpty());
        assertTrue(_Observer.updatedParameters.isEmpty());
    }

    @Test
    public void shouldNotCallUpdate() {
        _O.notifyObservers("test");
        _O.notifyObservers(null);
        assertEquals(0, _Observer.callsToUpdate);
        assertTrue(_Observer.updatedObservers.isEmpty());
        assertTrue(_Observer.updatedParameters.isEmpty());
    }

    @Test
    public void shouldAddObserver() {
        _O.addObserver(_Observer);
        _O.notifyObservers("house");
        assertEquals(1, _Observer.callsToUpdate);
        assertTrue(_Observer.updatedParameters.containsKey("house"));
        assertEquals(1, (int) _Observer.updatedParameters.get("house"));
        assertTrue(_Observer.updatedObservers.containsKey(_O));
        assertEquals(1, (int) _Observer.updatedObservers.get(_O));
    }

    @Test
    public void shouldRemoveObserver() {
        _O.addObserver(_Observer);
        _O.addObserver(_Observer);
        _O.removeObserver(_Observer);
        _O.notifyObservers("keyboard");
        assertEquals(1, _Observer.callsToUpdate);
        assertTrue(_Observer.updatedParameters.containsKey("keyboard"));
        assertEquals(1, (int) _Observer.updatedParameters.get("keyboard"));
        assertTrue(_Observer.updatedObservers.containsKey(_O));
        assertEquals(1, (int) _Observer.updatedObservers.get(_O));
    }

    @Test
    public void shouldUpdateMultipleObserver() {
        _O.addObserver(_Observer);
        _O.addObserver(_Observer);
        _O.addObserver(_Observer);
        _O.notifyObservers("multi");
        assertEquals(3, _Observer.callsToUpdate);
        assertTrue(_Observer.updatedParameters.containsKey("multi"));
        assertEquals(3, (int) _Observer.updatedParameters.get("multi"));
        assertTrue(_Observer.updatedObservers.containsKey(_O));
        assertEquals(3, (int) _Observer.updatedObservers.get(_O));
    }

    @Test
    public void shouldAcceptNullValues() {
        _O.addObserver(null);
        _O.removeObserver(null);
        _O.notifyObservers(null);
    }


    private static final class TestObserver implements Observer {

        int callsToUpdate = 0;
        Map<String, Integer> updatedParameters = new HashMap<>();
        Map<Observable, Integer> updatedObservers = new HashMap<>();

        @Override
        public void update(Observable observable, String parameter) {
            callsToUpdate++;
            if (updatedParameters.containsKey(parameter))
                updatedParameters.put(parameter, updatedParameters.get(parameter) + 1);
            else
                updatedParameters.put(parameter, 1);

            if (updatedObservers.containsKey(observable))
                updatedObservers.put(observable, updatedObservers.get(observable) + 1);
            else
                updatedObservers.put(observable, 1);
        }

    }

}
