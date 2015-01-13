package de.claas.mosis.model;

/**
 * The interface {@link de.claas.mosis.model.Observable}. It is intended to
 * provide a unified way for tracking changes in configuration parameters /
 * properties.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public interface Observable {

    /**
     * Add an {@link de.claas.mosis.model.Observer} to the observer list. The
     * observer is registered for all parameters / properties. The same observer
     * object may be added more than once, and will be called as many times as
     * it is added. If observer is {@code null}, no exception is thrown and no
     * action is taken.
     *
     * @param observer the {@link de.claas.mosis.model.Observer} to be added
     */
    public void addObserver(Observer observer);

    /**
     * Remove an {@link de.claas.mosis.model.Observer} from the observer list.
     * This removes a {@link de.claas.mosis.model.Observer} that was registered
     * for all parameters / properties. If observer was added more than once to
     * the same event source, it will be notified one less time after being
     * removed. If observer is {@code null}, or was never added, no exception is
     * thrown and no action is taken.
     *
     * @param observer the {@link de.claas.mosis.model.Observer} to be removed
     */
    public void removeObserver(Observer observer);

    /**
     * Notifies all observers that have been registered to track updates. The
     * notification implies that the given parameter has changed its value (i.e.
     * old and new value are different as well as non-{@code null}).
     *
     * @param parameter the parameter
     */
    public void notifyObservers(String parameter);

}
