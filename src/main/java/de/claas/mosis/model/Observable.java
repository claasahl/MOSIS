package de.claas.mosis.model;

/**
 * The interface {@link de.claas.mosis.model.Observable}. It is intended to
 * provide a unified way for processing time series data. This represents a
 * generic module that can be utilized to perform an arbitrary computations,
 * transformations, etc. Multiple of these modules can be linked together in
 * order to perform more complex tasks (e.g. transform data from one format to
 * another, inspect network traffic or analyze OpenStreetMap data, etc.).
 * <p/>
 * TODO
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public interface Observable {

    /**
     * TODO
     *
     * @param observer
     */
    public void addObserver(Observer observer);

    /**
     * TODO
     * @param observer
     */
    public void removeObserver(Observer observer);

    /**
     * TODO
     * @param parameter
     */
    public void notifyObservers(String parameter);

}
