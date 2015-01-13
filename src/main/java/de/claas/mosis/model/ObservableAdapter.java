package de.claas.mosis.model;

import java.util.List;
import java.util.Vector;

/**
 * The class {@link de.claas.mosis.model.ObservableAdapter}. It is intended to
 * provide a common implementation of the {@link de.claas.mosis.model.Observable}
 * interface. It tracks changes of configuration related parameters.
 * <p/>
 * TODO
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class ObservableAdapter implements Observable {

    private final List<Observer> _Observers = new Vector<>();

    @Override
    public void addObserver(Observer observer) {
        _Observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        _Observers.remove(observer);
    }

    @Override
    public void notifyObservers(String parameter) {
        for (Observer observer : _Observers) {
            observer.update(this, parameter);
        }
    }

}
