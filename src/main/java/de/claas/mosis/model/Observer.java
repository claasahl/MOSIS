package de.claas.mosis.model;

/**
 * The interface {@link de.claas.mosis.model.Observer}. It is intended to
 * provide a unified way for automatic computation of configuration parameters /
 * properties as well as a unified way for keeping track of changes among
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
     * TODO
     *
     * @param observable
     * @param parameter
     */
    public void update(Observable observable, String parameter);

}
