package de.claas.mosis.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import de.claas.mosis.annotation.Documentation;

/**
 * The class {@link ConfigurableAdapter}. It is intended to provide a common
 * implementation of the {@link Configurable} interface. It tracks and manages
 * configuration related parameters as well as their corresponding values.
 * Furthermore, it provides the option to get and set parameters of varying data
 * types (e.g. {@link Boolean}, {@link Integer}, etc.).
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
@Documentation(
	purpose = "This implementation covers handling of parameters.",
	description = "This is a partial implementation of the main interface (i.e. Processor). It is intended to provide a unified way for getting and setting parameters. One can use this as a basis for creating new modules.",
	author = "Claas Ahlrichs",
	noOutputData = "Refer to concrete implementations.")
public class ConfigurableAdapter extends ObservableAdapter implements
	Configurable, Observable {

    private final Map<String, String> _Parameters = new HashMap<String, String>();
    private final Map<String, List<Condition>> _Conditions = new HashMap<String, List<Condition>>();
    private final List<Relation> _Relations = new Vector<Relation>();

    @Override
    public List<String> getParameters() {
	return new Vector<String>(_Parameters.keySet());
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
	for (Relation relation : _Relations) {
	    relation.compute(this, parameter, value);
	}
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
     * Adds a {@link Condition} to a parameter. This {@link Condition} is
     * evaluated every time the parameter is modified.
     * 
     * @param parameter
     *            the parameter
     * @param condition
     *            the {@link Condition}
     */
    protected void addCondition(String parameter, Condition condition) {
	List<Condition> conditions = _Conditions.get(parameter);
	if (conditions == null) {
	    conditions = new Vector<Condition>();
	    _Conditions.put(parameter, conditions);
	}
	conditions.add(condition);
    }

    /**
     * Removes a {@link Condition} from a parameter.
     * 
     * @param parameter
     *            the parameter
     * @param condition
     *            the {@link Condition}
     */
    protected void removeCondition(String parameter, Condition condition) {
	List<Condition> conditions = _Conditions.get(parameter);
	if (conditions != null) {
	    conditions.remove(condition);
	}
    }

    /**
     * Adds a {@link Relation} to a parameter. This {@link Relation} is
     * evaluated every time the parameter is modified.
     * 
     * @param relation
     *            the {@link Relation}
     */
    protected void addRelation(Relation relation) {
	_Relations.add(relation);
    }

    /**
     * Removes a {@link Relation} from a parameter.
     * 
     * @param relation
     *            the {@link Relation}
     */
    protected void removeRelation(Relation relation) {
	_Relations.remove(relation);
    }

}
