package de.claas.mosis.model;

import java.util.Collection;
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
     * @param parameter    the parameter, which has changed
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

    /**
     * The class {@link de.claas.mosis.model.Observer.BreakOut}. It is intended
     * for debugging purposes. This {@link de.claas.mosis.model.Observer}
     * implementation provides access to the observed parameters (see {@link
     * #getParameters()}) and {@link de.claas.mosis.model.Configurable}s (see
     * {@link #getConfigurables()} ) values as well as several other potentially
     * relevant statistics.
     *
     * @author Claas Ahlrichs (claasahl@tzi.de)
     */
    public class BreakOut implements Observer {

        private int calls = 0;
        private Map<String, Integer> parameters = new HashMap<>();
        private Map<Configurable, Integer> configurables = new HashMap<>();

        /**
         * Returns the set of parameters that were observed. This corresponds to
         * those parameters that have changed their value.
         *
         * @return the set of parameters that were observed
         */
        public Collection<String> getParameters() {
            return parameters.keySet();
        }

        /**
         * Returns the set of {@link de.claas.mosis.model.Configurable}s that
         * were observed. This corresponds to those {@link
         * de.claas.mosis.model.Configurable} objects that are associated with
         * the parameters returned by {@link #getParameters()}.
         *
         * @return the set of {@link de.claas.mosis.model.Configurable}s that
         * were observed
         */
        public Collection<Configurable> getConfigurables() {
            return configurables.keySet();
        }

        /**
         * Returns the number of updates / observations for a parameter. This
         * will usually correspond to the number of times that a parameter
         * changed it's value.
         *
         * @param parameter the parameter
         * @return the number of updates / observations for a parameter
         */
        public int getUpdates(String parameter) {
            return parameters.containsKey(parameter) ? parameters.get(parameter) : 0;
        }

        /**
         * Returns the number of updates / observations for a {@link
         * de.claas.mosis.model.Configurable}. This will usually correspond to
         * the number of times that parameters within a {@link
         * de.claas.mosis.model.Configurable} object were changed.
         *
         * @param configurable the {@link de.claas.mosis.model.Configurable}
         * @return the number of updates / observations for a {@link
         * de.claas.mosis.model.Configurable}
         */
        public int getUpdates(Configurable configurable) {
            return configurables.containsKey(configurable) ? configurables.get(configurable) : 0;
        }

        /**
         * Returns the number of calls of {@link #update(Configurable,
         * String)}.
         *
         * @return the number of calls of {@link #update(Configurable, String)}
         */
        public int getCalls() {
            return calls;
        }

        @Override
        public void update(Configurable configurable, String parameter) {
            calls++;
            if (parameters.containsKey(parameter))
                parameters.put(parameter, parameters.get(parameter) + 1);
            else
                parameters.put(parameter, 1);

            if (configurables.containsKey(configurable))
                configurables.put(configurable, configurables.get(configurable) + 1);
            else
                configurables.put(configurable, 1);
        }

        @Override
        public boolean equals(Object o) {
            return getClass().equals(o == null ? null : o.getClass());
        }
    }

}
