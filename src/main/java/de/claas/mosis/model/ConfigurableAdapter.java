package de.claas.mosis.model;

import de.claas.mosis.annotation.Documentation;

import java.util.*;

/**
 * The class {@link de.claas.mosis.model.ConfigurableAdapter}. It is intended to
 * provide a common implementation of the {@link de.claas.mosis.model.Configurable}
 * interface. It tracks and manages configuration related parameters as well as
 * their corresponding values. Furthermore, it provides the option to get and
 * set parameters of varying data types (e.g. {@link java.lang.Boolean}, {@link
 * java.lang.Integer}, etc.).
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        purpose = "This implementation covers handling of parameters.",
        description = "This is a partial implementation of the main interface (i.e. Processor). It is intended to provide a unified way for getting and setting parameters. One can use this as a basis for creating new modules.",
        author = "Claas Ahlrichs",
        noOutputData = "Refer to concrete implementations.")
public class ConfigurableAdapter implements Configurable {

    private final Map<String, String> _Parameters = new HashMap<>();
    private final Map<String, List<Condition>> _Conditions = new HashMap<>();
    private final List<Observer> _Observers = new ArrayList<>();

    @Override
    // TODO make collection!?
    public List<String> getParameters() {
        return new ArrayList<>(_Parameters.keySet());
    }

    @Override
    public String getParameter(String parameter) {
        return _Parameters.get(parameter);
    }

    /**
     * See {@link #getParameter(String)} for details.
     *
     * @see #getParameter(String)
     */
    protected Boolean getParameterAsBoolean(String parameter) {
        String value = getParameter(parameter);
        return value == null ? null : Boolean.valueOf(value);
    }

    /**
     * See {@link #getParameter(String)} for details.
     *
     * @see #getParameter(String)
     */
    protected Integer getParameterAsInteger(String parameter) {
        String value = getParameter(parameter);
        return value == null ? null : Integer.valueOf(value);
    }

    /**
     * See {@link #getParameter(String)} for details.
     *
     * @see #getParameter(String)
     */
    protected Long getParameterAsLong(String parameter) {
        String value = getParameter(parameter);
        return value == null ? null : Long.valueOf(value);
    }

    /**
     * See {@link #getParameter(String)} for details.
     *
     * @see #getParameter(String)
     */
    protected Double getParameterAsDouble(String parameter) {
        String value = getParameter(parameter);
        return value == null ? null : Double.valueOf(value);
    }

    @Override
    public void setParameter(String parameter, String value) {
        // Did the value change?
        if (getParameter(parameter) != null
                && getParameter(parameter).equals(value)) {
            // Did not change.
            return;
        }

        // Any conditions to be satisfied by the new value?
        if (_Conditions.get(parameter) != null) {
            for (Condition condition : _Conditions.get(parameter)) {
                if (!condition.complies(parameter, value)) {
                    String format = "Parameter (%s) and value (%s) do not satisfy condition (%s).";
                    String msg = String.format(format, parameter, value,
                            condition);
                    throw new IllegalArgumentException(msg);
                }
            }
        }

        // Set value and notify observers (if any)
        _Parameters.put(parameter, value);
        notifyObservers(parameter);
    }

    /**
     * See {@link #setParameter(String, String)} for details.
     *
     * @see #setParameter(String, String)
     */
    protected void setParameter(String parameter, Boolean value) {
        setParameter(parameter, Boolean.toString(value));
    }

    /**
     * See {@link #setParameter(String, String)} for details.
     *
     * @see #setParameter(String, String)
     */
    protected void setParameter(String parameter, Integer value) {
        setParameter(parameter, Integer.toString(value));
    }

    /**
     * See {@link #setParameter(String, String)} for details.
     *
     * @see #setParameter(String, String)
     */
    protected void setParameter(String parameter, Long value) {
        setParameter(parameter, Long.toString(value));
    }

    /**
     * See {@link #setParameter(String, String)} for details.
     *
     * @see #setParameter(String, String)
     */
    protected void setParameter(String parameter, Double value) {
        setParameter(parameter, Double.toString(value));
    }

    /**
     * Adds a {@link de.claas.mosis.model.Condition} to a parameter. This {@link
     * de.claas.mosis.model.Condition} is evaluated every time the parameter is
     * modified. If condition is {@code null}, no exception is thrown and no
     * action is taken.
     *
     * @param parameter the parameter
     * @param condition the {@link de.claas.mosis.model.Condition}
     */
    protected void addCondition(String parameter, Condition condition) {
        if (!_Parameters.containsKey(parameter))
            _Parameters.put(parameter, null);

        List<Condition> conditions = _Conditions.get(parameter);
        if (conditions == null) {
            conditions = new Vector<>();
            _Conditions.put(parameter, conditions);
        }
        conditions.add(condition);
    }

    /**
     * Removes a {@link de.claas.mosis.model.Condition} from a parameter. If
     * condition is {@code null}, or was never added, no exception is thrown and
     * no action is taken.
     *
     * @param parameter the parameter
     * @param condition the {@link de.claas.mosis.model.Condition}
     */
    protected void removeCondition(String parameter, Condition condition) {
        List<Condition> conditions = _Conditions.get(parameter);
        if (conditions != null) {
            conditions.remove(condition);
        }
    }

    /**
     * Returns all registered conditions for a parameter. It is save to assume
     * that every condition (added by calling {@link #addCondition(String,
     * Condition)} and not removed by calling {@link #removeCondition(String,
     * Condition)}) is contained in the list returned by this method. This
     * method will always return a list, even if it is empty. Thus
     * <code>null</code> will never be returned.
     * <p/>
     * The list may be edited and clear. It does not affect the internal status
     * of this {@link de.claas.mosis.model.Configurable}.
     *
     * @param parameter the parameter
     * @return all registered conditions for a paramater
     */
    protected Collection<Condition> getConditions(String parameter) {
        if (_Conditions.containsKey(parameter)
                && _Conditions.get(parameter) != null)
            return new ArrayList<>(_Conditions.get(parameter));
        else
            return new ArrayList<>();
    }

    /**
     * Adds an {@link de.claas.mosis.model.Observer} to the observer list. The
     * observer is registered for all parameters / properties. The same observer
     * object may be added more than once, and will be called as many times as
     * it is added. If observer is {@code null}, no exception is thrown and no
     * action is taken.
     *
     * @param observer the {@link de.claas.mosis.model.Observer} to be added
     */
    protected void addObserver(Observer observer) {
        _Observers.add(observer);
    }

    /**
     * Removes an {@link de.claas.mosis.model.Observer} from the observer list.
     * This removes an {@link de.claas.mosis.model.Observer} that was registered
     * for all parameters / properties. If the observer was added more than once
     * to the same event source, it will be notified one less time after being
     * removed. If observer is {@code null}, or was never added, no exception is
     * thrown and no action is taken.
     *
     * @param observer the {@link de.claas.mosis.model.Observer} to be removed
     */
    protected void removeObserver(Observer observer) {
        _Observers.remove(observer);
    }

    /**
     * Returns all registered observers. It is save to assume that every
     * observer (added by calling {@link #addObserver(Observer)} and not removed
     * by calling {@link #removeObserver(Observer)}) is contained in the list
     * returned by this method. This method will always return a list, even if
     * it is empty. Thus <code>null</code> will never be returned.
     * <p/>
     * The list may be edited and clear. It does not affect the internal status
     * of this {@link de.claas.mosis.model.Configurable}.
     *
     * @return all registered observers
     */
    protected Collection<Observer> getObservers() {
        return new ArrayList<>(_Observers);
    }


    /**
     * Notifies all observers that have been registered to track updates. The
     * notification implies that the given parameter has changed its value (i.e.
     * old and new value are different as well as non-{@code null}). If {@code
     * null} is passed into this method, no exception is thrown and no action is
     * taken.
     *
     * @param parameter the parameter, which value has changed
     */
    protected void notifyObservers(String parameter) {
        for (Observer observer : _Observers) {
            observer.update(this, parameter);
        }
    }

    /**
     * Updates (or replaces) the internal state based on another {@link
     * de.claas.mosis.model.ConfigurableAdapter}. The internal state can
     * optionally be cleared before any updates are performed, thus effectively
     * overwriting the previous state. While updating, all relevant conditions
     * are evaluated and may result in an exception if parameters do not adhere
     * to their constraints, resulting in an undefined internal state.
     *
     * @param configurable the {@link de.claas.mosis.model.ConfigurableAdapter}
     *                     which state is used during the update
     * @param clear        whether the internal state is cleared first or not
     */
    protected void update(ConfigurableAdapter configurable, boolean clear) {
        if (clear) {
            _Parameters.clear();
            _Conditions.clear();
            _Observers.clear();
        }
        for (String parameter : configurable.getParameters()) {
            for (Condition condition : configurable.getConditions(parameter))
                addCondition(parameter, condition);
            setParameter(parameter, configurable.getParameter(parameter));
        }
        for (Observer observer : configurable.getObservers())
            addObserver(observer);
    }
}
