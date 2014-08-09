package de.claas.mosis.model;

import java.util.List;
import java.util.Vector;

public class ObservableAdapter implements Observable {

    private final List<Observer> _Observers = new Vector<Observer>();;

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
