package de.claas.mosis.model;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The interface {@link Condition}. It is intended to provide a unified way for
 * checking the validity of configuration parameters and their corresponding
 * values. Implementing classes may want to verify that the parameter, value and
 * / or both satisfy certain criteria (e.g. such as a value being numeric or a
 * parameter being properly formated).
 * 
 * <b>Note:</b> It is highly recommended to override {@link #equals(Object)},
 * {@link #hashCode()} and {@link #toString()}. They are used for management
 * purposes (see {@link ConfigurableAdapter#addCondition(String, Condition)} and
 * {@link ConfigurableAdapter#removeCondition(String, Condition)} for details)
 * and to generate error messages (see
 * {@link ConfigurableAdapter#setParameter(String, String)} for details).
 * 
 * @see ConfigurableAdapter
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public interface Condition {

    /**
     * Returns <code>true</code>, if the combination of parameter and value
     * satisfy the condition. Otherwise, <code>false</code> is returned. One can
     * assume that this method does not throw a {@link NullPointerException} if
     * one or more parameters equal <code>null</code>.
     * 
     * @param parameter
     *            the parameter
     * @param value
     *            the value
     * @return <code>true</code>, if the combination of parameter and value
     *         satisfy the condition
     */
    public boolean complies(String parameter, String value);

    /**
     * The class {@link RegularExpression}. It is intended to verify that a
     * parameter and / or value match a regular expression.
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * @see Pattern
     * 
     */
    public static class RegularExpression implements Condition {

	private final String _RegExParameter;
	private final String _RegExValue;

	/**
	 * Initializes the class with the given parameters. The regular
	 * expressions are used to verify the validity of parameters and / or
	 * values. Parameters will not be checked if their corresponding regular
	 * expression is <code>null</code>.
	 * 
	 * @param regexParam
	 *            regular expression that a parameter should match. If
	 *            <code>null</code> then the parameter will not be checked.
	 * @param regexValue
	 *            regular expression that a value should match. If
	 *            <code>null</code> then the value will not be checked.
	 */
	public RegularExpression(String regexParam, String regexValue) {
	    _RegExParameter = regexParam;
	    _RegExValue = regexValue;
	}

	@Override
	public boolean complies(String parameter, String value) {
	    if (_RegExParameter != null
		    && (parameter == null || !parameter
			    .matches(_RegExParameter))) {
		return false;
	    }
	    if (_RegExValue != null
		    && (value == null || !value.matches(_RegExValue))) {
		return false;
	    }
	    return true;
	}

	@Override
	public boolean equals(Object o) {
	    return getClass().equals(o == null ? null : o.getClass())
		    && hashCode() == o.hashCode();
	}

	@Override
	public int hashCode() {
	    int hash = getClass().hashCode();
	    hash += _RegExParameter != null ? _RegExParameter.hashCode() : 0;
	    hash += _RegExValue != null ? _RegExValue.hashCode() : 0;
	    return hash;
	}

	@Override
	public String toString() {
	    String msg = "";
	    if (_RegExParameter != null) {
		String format = "parameter does not match '%s'";
		msg += String.format(format, _RegExParameter);
	    }
	    if (_RegExValue != null) {
		if (!msg.isEmpty()) {
		    msg += "and / or ";
		}
		String format = "value does not match '%s'";
		msg += String.format(format, _RegExValue);
	    }
	    return msg + ".";
	}

    }

    /**
     * The class {@link IsBoolean}. It is intended to verify that a value is
     * boolean.
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * 
     */
    public static class IsBoolean extends RegularExpression {

	public IsBoolean() {
	    super(null, "true|false");
	}

    }

    /**
     * The class {@link IsNumeric}. It is intended to verify that a value is
     * numeric.
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * 
     */
    public static class IsNumeric extends RegularExpression {

	public IsNumeric() {
	    super(null, "-?\\d+(.\\d+)?");
	}

    }

    /**
     * The class {@link IsInteger}. It is intended to verify that a value is an
     * integer (and not real-valued).
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * 
     */
    public static class IsInteger extends RegularExpression {

	public IsInteger() {
	    super(null, "-?\\d+");
	}

    }

    /**
     * The class {@link IsGreaterThan}. It is intended to verify that a value is
     * greater than a given threshold.
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * 
     */
    public static class IsGreaterThan implements Condition {

	private final double _Threshold;

	/**
	 * Initializes the class with the given parameters.
	 * 
	 * @param v
	 *            the threshold
	 */
	public IsGreaterThan(Double v) {
	    _Threshold = v;
	}

	@Override
	public boolean complies(String parameter, String value) {
	    return value == null ? false
		    : Double.parseDouble(value) > _Threshold;
	}

	@Override
	public boolean equals(Object o) {
	    return getClass().equals(o == null ? null : o.getClass())
		    && hashCode() == o.hashCode();
	}

	@Override
	public int hashCode() {
	    int hash = getClass().hashCode();
	    hash += new Double(_Threshold).hashCode();
	    return hash;
	}

	@Override
	public String toString() {
	    String format = "Parameter is not greater than %f";
	    return String.format(format, _Threshold);
	}

    }

    /**
     * The class {@link IsGreaterOrEqual}. It is intended to verify that a value
     * is greater or equal a given threshold.
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * 
     */
    public static class IsGreaterOrEqual implements Condition {

	private final double _Threshold;

	/**
	 * Initializes the class with the given parameters.
	 * 
	 * @param v
	 *            the threshold
	 */
	public IsGreaterOrEqual(Double v) {
	    _Threshold = v;
	}

	@Override
	public boolean complies(String parameter, String value) {
	    return value == null ? false
		    : Double.parseDouble(value) >= _Threshold;
	}

	@Override
	public boolean equals(Object o) {
	    return getClass().equals(o == null ? null : o.getClass())
		    && hashCode() == o.hashCode();
	}

	@Override
	public int hashCode() {
	    int hash = getClass().hashCode();
	    hash += new Double(_Threshold).hashCode();
	    return hash;
	}

	@Override
	public String toString() {
	    String format = "Parameter is not greater or equal %f";
	    return String.format(format, _Threshold);
	}

    }

    /**
     * The class {@link IsLessThan}. It is intended to verify that a value is
     * less than a given threshold.
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * 
     */
    public static class IsLessThan implements Condition {

	private final double _Threshold;

	/**
	 * Initializes the class with the given parameters.
	 * 
	 * @param v
	 *            the threshold
	 */
	public IsLessThan(Double v) {
	    _Threshold = v;
	}

	@Override
	public boolean complies(String parameter, String value) {
	    return value == null ? false
		    : Double.parseDouble(value) < _Threshold;
	}

	@Override
	public boolean equals(Object o) {
	    return getClass().equals(o == null ? null : o.getClass())
		    && hashCode() == o.hashCode();
	}

	@Override
	public int hashCode() {
	    int hash = getClass().hashCode();
	    hash += new Double(_Threshold).hashCode();
	    return hash;
	}

	@Override
	public String toString() {
	    String format = "Parameter is not less than %f";
	    return String.format(format, _Threshold);
	}

    }

    /**
     * The class {@link IsLessOrEqual}. It is intended to verify that a value is
     * less or equal a given threshold.
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * 
     */
    public static class IsLessOrEqual implements Condition {

	private final double _Threshold;

	/**
	 * Initializes the class with the given parameters.
	 * 
	 * @param v
	 *            the threshold
	 */
	public IsLessOrEqual(Double v) {
	    _Threshold = v;
	}

	@Override
	public boolean complies(String parameter, String value) {
	    return value == null ? false
		    : Double.parseDouble(value) <= _Threshold;
	}

	@Override
	public boolean equals(Object o) {
	    return getClass().equals(o == null ? null : o.getClass())
		    && hashCode() == o.hashCode();
	}

	@Override
	public int hashCode() {
	    int hash = getClass().hashCode();
	    hash += new Double(_Threshold).hashCode();
	    return hash;
	}

	@Override
	public String toString() {
	    String format = "Parameter is not less or equal %f";
	    return String.format(format, _Threshold);
	}

    }

    /**
     * The class {@link IsInList}. It is intended to verify that a value is in a
     * (white) list.
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * 
     */
    public static class IsInList implements Condition {

	private final List<String> _List;

	/**
	 * Initializes the class with the given parameters.
	 * 
	 * @param whiteList
	 *            the (white) list
	 */
	public IsInList(List<String> whiteList) {
	    _List = whiteList;
	}

	@Override
	public boolean complies(String parameter, String value) {
	    return _List.contains(value);
	}

	@Override
	public boolean equals(Object o) {
	    return getClass().equals(o == null ? null : o.getClass())
		    && hashCode() == o.hashCode();
	}

	@Override
	public int hashCode() {
	    int hash = getClass().hashCode();
	    hash += _List != null ? _List.hashCode() : 0;
	    return hash;
	}

	@Override
	public String toString() {
	    String format = "Parameter is not in list (%s).";
	    return String.format(format, _List);
	}

    }

    /**
     * The class {@link IsNotNull}. It is intended to verify that a value is not
     * <code>null</code>.
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * 
     */
    public static class IsNotNull implements Condition {

	@Override
	public boolean complies(String parameter, String value) {
	    return value != null;
	}

	@Override
	public boolean equals(Object o) {
	    return getClass().equals(o == null ? null : o.getClass())
		    && hashCode() == o.hashCode();
	}

	@Override
	public int hashCode() {
	    return getClass().hashCode();
	}

	@Override
	public String toString() {
	    return "Parameter is null.";
	}

    }

    /**
     * The class {@link ReadOnly}. It is intended to prevent modifications of a
     * parameter's value.
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * 
     */
    public static class ReadOnly implements Condition {

	@Override
	public boolean complies(String parameter, String value) {
	    return false;
	}

	@Override
	public boolean equals(Object o) {
	    return getClass().equals(o == null ? null : o.getClass())
		    && hashCode() == o.hashCode();
	}

	@Override
	public int hashCode() {
	    return getClass().hashCode();
	}

	@Override
	public String toString() {
	    return "Parameter is read-only.";
	}

    }

    /**
     * The class {@link FileExists}. It is intended to verify the existence of a
     * file (e.g. prior to reading from it). This assumes that the given value
     * does represent either an absolute or relative path to a file.
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * 
     */
    public static class FileExists implements Condition {

	@Override
	public boolean complies(String parameter, String value) {
	    return value == null ? false : new File(value).exists();
	}

	@Override
	public boolean equals(Object o) {
	    return getClass().equals(o == null ? null : o.getClass())
		    && hashCode() == o.hashCode();
	}

	@Override
	public int hashCode() {
	    return getClass().hashCode();
	}

	@Override
	public String toString() {
	    return "File does not exist.";
	}

    }

    /**
     * The class {@link WriteOnce}. It is intended to ensure that a parameter is
     * written only once. Afterwards the parameter cannot be changed anymore.
     * This condition should always be added last (i.e. after all other
     * conditions for a parameter were added). Failure to do so may result in
     * unexpected behavior (i.e. not being able to write, not even once).
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * 
     */
    public static class WriteOnce implements Condition {

	private int _NumOfWrites = 1;

	@Override
	public boolean complies(String parameter, String value) {
	    return _NumOfWrites-- > 0;
	}

	@Override
	public boolean equals(Object o) {
	    return getClass().equals(o == null ? null : o.getClass())
		    && hashCode() == o.hashCode();
	}

	@Override
	public int hashCode() {
	    int hash = getClass().hashCode();
	    hash += _NumOfWrites;
	    return hash;
	}

	@Override
	public String toString() {
	    return "Parameter may only be written once.";
	}

    }

    /**
     * The class {@link IsNot}. It is intended to negate the restriction(s) of
     * another {@link Condition}. This could be used to allow only non-numeric
     * values (i.e. <code>new Condition.IsNot(new Condition.IsNumeric())</code>)
     * or verify that a file will not be overridden (i.e.
     * <code>new Condition.IsNot(new Condition.IsNumeric())</code>).
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * 
     */
    public static class IsNot implements Condition {

	private final Condition _Condition;

	/**
	 * Initializes the class with the given parameters.
	 * 
	 * @param condition
	 *            the {@link Condition} that is negated
	 */
	public IsNot(Condition condition) {
	    _Condition = condition;
	}

	@Override
	public boolean complies(String parameter, String value) {
	    return !_Condition.complies(parameter, value);
	}

	@Override
	public boolean equals(Object o) {
	    return getClass().equals(o == null ? null : o.getClass())
		    && hashCode() == o.hashCode();
	}

	@Override
	public int hashCode() {
	    int hash = getClass().hashCode();
	    hash += _Condition != null ? _Condition.hashCode() : 0;
	    return hash;
	}

	@Override
	public String toString() {
	    return String.format(
		    "Parameter should not meet the condition: \"%s\"",
		    _Condition);
	}

    }

    /**
     * The class {@link ClassExists}. It is intended to verify the existence of
     * a class (e.g. prior to instantiating). This assumes that the given value
     * does represent a class's absolute class name and is accessible within the
     * current class path.
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * 
     */
    public static class ClassExists implements Condition {

	@Override
	public boolean complies(String parameter, String value) {
	    try {
		return Class.forName(value) != null;
	    } catch (Exception e) {
		return false;
	    }
	}

	@Override
	public boolean equals(Object o) {
	    return getClass().equals(o == null ? null : o.getClass())
		    && hashCode() == o.hashCode();
	}

	@Override
	public int hashCode() {
	    return getClass().hashCode();
	}

	@Override
	public String toString() {
	    return "Class does not exist.";
	}

    }

}
