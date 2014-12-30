package de.claas.mosis.model;

/**
 * The interface {@link Relation}. It is intended to provide a unified way for
 * automatic computation of configuration parameters. Implementing classes may
 * want to automatically adjust one or more parameters depending on the value of
 * another parameter (e.g. adding a parameter containing the time and date of
 * the last change or calculating another parameter based on a new value). Thus,
 * related parameter are kept synchronized.
 * <p/>
 * <b>Note:</b> It is highly recommended to override {@link #equals(Object)} and
 * {@link #hashCode()}. It is used for management purposes (see
 * {@link ConfigurableAdapter#addRelation(Relation)} and
 * {@link ConfigurableAdapter#removeRelation(Relation)} for details).
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * @see ConfigurableAdapter
 * @deprecated Will be replaced by Observer design pattern in future release
 */
public interface Relation {

    /**
     * Computes related parameters. Given the parameter and its value, one or
     * more parameters may be (re-)computed.
     *
     * @param configurable the {@link Configurable}
     * @param parameter    the parameter
     * @param value        the value
     */
    public void compute(Configurable configurable, String parameter,
                        String value);

    /**
     * The class {@link LastChanged}. It is intended to add/update a parameter
     * with the current date and time.
     *
     * @author Claas Ahlrichs (claasahl@tzi.de)
     */
    public static class LastChanged implements Relation {

        public final static String LastChanged = "LastChanged";

        @Override
        public void compute(Configurable configurable, String parameter,
                            String value) {
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
     * The class {@link UpdateVersion}. It is intended to add/update a parameter
     * with the version (i.e. total number of changes that have been made).
     *
     * @author Claas Ahlrichs (claasahl@tzi.de)
     */
    public static class UpdateVersion implements Relation {

        public final static String Version = "Version";

        @Override
        public void compute(Configurable configurable, String parameter,
                            String value) {
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
     * The class {@link ParameterHistory}. It is intended to track changes of
     * parameters. The parameter history is stored in the form
     * "history-<parameter's name>".
     *
     * @author Claas Ahlrichs (claasahl@tzi.de)
     */
    public class ParameterHistory implements Relation {

        public final static String Prefix = "history-";

        @Override
        public void compute(Configurable configurable, String parameter,
                            String value) {
            if (parameter == null || !parameter.startsWith(Prefix)) {
                String p = Prefix + parameter;
                String history = configurable.getParameter(p);
                history = history == null ? "" : history;
                history += String.format("%s@%d;", value,
                        System.currentTimeMillis());
                configurable.setParameter(p, history);
            }
        }

        @Override
        public boolean equals(Object o) {
            return getClass().equals(o == null ? null : o.getClass());
        }

    }

}
