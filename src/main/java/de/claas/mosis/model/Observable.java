package de.claas.mosis.model;

public interface Observable {
    
    public void addObserver(Observer observer);
    
    public void removeObserver(Observer observer);
    
    public void notifyObservers(String parameter);

}
