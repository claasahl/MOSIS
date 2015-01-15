package de.claas.mosis.model;

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
     * parameter / property of an {@link de.claas.mosis.model.Observable} has
     * changed (i.e. old and new value are different as well as non-{@code
     * null}).
     *
     * @param observable the {@link de.claas.mosis.model.Observable}
     * @param parameter  the parameter, which has changed
     */
    public void update(Observable observable, String parameter);

}
