package de.claas.mosis.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The interface {@link de.claas.mosis.model.Observer}. It is intended to
 * provide an unified way for automatic computation of configuration parameters
 * / properties as well as an unified way for keeping track of changes among
 * configuration parameters. Implementing classes may want to automatically
 * adjust one or more parameters depending on the value of another parameter
 * (e.g. adding a parameter containing the time and date of the last change or
 * calculating another parameter based on a new value). Thus, related parameter
 * are kept synchronized.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public interface Observer {

    /**
     * Notifies an {@link de.claas.mosis.model.Observer} that a configuration
     * parameter / property of an {@link de.claas.mosis.model.Configurable} has
     * changed (i.e. old and new value are different as well as non-{@code
     * null}).
     *
     * @param configurable the {@link de.claas.mosis.model.Configurable}
     * @param parameter  the parameter, which has changed
     */
    public void update(Configurable configurable, String parameter);

    /**
     * The class {@link de.claas.mosis.model.Observer.LastChanged}. It is
     * intended to add/update a parameter with the current date and time.
     *
     * @author Claas Ahlrichs (claasahl@tzi.de)
     */
    public static class LastChanged implements Observer {

        public final static String LastChanged = "LastChanged";

        @Override
        public void update(Configurable configurable, String parameter) {
            if (!LastChanged.equals(parameter)) {
                long d = System.currentTimeMillis();
                String date = String.format("%d", d);
                configurable.setParameter(LastChanged, date);
            }
        }

        @Override
        public boolean equals(Object o) {
            return getClass().equals(o == null ? null : o.getClass());
        }
    }

    /**
     * The class {@link de.claas.mosis.model.Observer.UpdateVersion}. It is
     * intended to add/update a parameter with the version (i.e. total number of
     * changes that have been made).
     *
     * @author Claas Ahlrichs (claasahl@tzi.de)
     */
    public static class UpdateVersion implements Observer {

        public final static String Version = "Version";

        @Override
        public void update(Configurable configurable, String parameter) {
            if (!Version.equals(parameter)) {
                String old = configurable.getParameter(Version);
                old = old == null ? "0" : old;
                int version = Integer.parseInt(old);
                configurable.setParameter(Version, Integer.toString(++version));
            }
        }

        @Override
        public boolean equals(Object o) {
            return getClass().equals(o == null ? null : o.getClass());
        }
    }

    /**
     * The class {@link de.claas.mosis.model.Observer.ParameterHistory}. It is
     * intended to track changes of parameters. The parameter history is stored
     * in the form "history-<parameter's name>".
     *
     * @author Claas Ahlrichs (claasahl@tzi.de)
     */
    public class ParameterHistory implements Observer {

        public final static String Prefix = "history-";

        @Override
        public void update(Configurable configurable, String parameter) {
            if (parameter == null || !parameter.startsWith(Prefix)) {
                String p = Prefix + parameter;
                String v = configurable.getParameter(parameter);
                String history = configurable.getParameter(p);
                history = history == null ? "" : history;
                history += String.format("%s@%d;", v,
                        System.currentTimeMillis());
                configurable.setParameter(p, history);
            }
        }

        @Override
        public boolean equals(Object o) {
            return getClass().equals(o == null ? null : o.getClass());
        }
    }

    // TODO Documentation (Mainly for testing purposes)
    // TODO Write separate test for this observer
    public class BreakOut implements Observer {

        int callsToUpdate = 0;
        Map<String, Integer> updatedParameters = new HashMap<>();
        Map<Configurable, Integer> updatedConfigurables = new HashMap<>();

        @Override
        public void update(Configurable configurable, String parameter) {
            callsToUpdate++;
            if (updatedParameters.containsKey(parameter))
                updatedParameters.put(parameter, updatedParameters.get(parameter) + 1);
            else
                updatedParameters.put(parameter, 1);

            if (updatedConfigurables.containsKey(configurable))
                updatedConfigurables.put(configurable, updatedConfigurables.get(configurable) + 1);
            else
                updatedConfigurables.put(configurable, 1);
        }

        // TODO Equals method ...
    }

}
