package de.claas.mosis.model;

import java.util.List;

import de.claas.mosis.flow.Link;

/**
 * The interface {@link Configurable}. It is intended to provide a unified
 * interface for configuration purposes. The interface allows to get, set and
 * enumerate parameters of any object that wishes to expose such configuration
 * options (e.g. {@link Processor} and {@link Link}).
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public interface Configurable {

    /**
     * Returns all available parameters. It is save to assume that every
     * parameter (no matter if it is optional or mandatory) is contained in the
     * list returned by this method. Every parameter in the list can be
     * retrieved and modified with {@link #getParameter(String)} and
     * {@link #setParameter(String, String)} respectively. This method will
     * always return a list, even if it is empty. Thus <code>null</code> will
     * never be returned.
     * 
     * @return all available parameters
     */
    public List<String> getParameters();

    /**
     * Returns the value of a parameter. The returned value may also be
     * <code>null</code> in which case the parameter does not exist or has been
     * explicitly set to <code>null</code>.
     * 
     * @param parameter
     *            the parameter
     * @return the value of a parameter
     */
    public String getParameter(String parameter);

    /**
     * Sets the value of a parameter. The value may also be <code>null</code>.
     * 
     * @param parameter
     *            the parameter
     * @param value
     *            the value
     * @throws IllegalArgumentException
     *             if the parameter may not be set to the given value
     */
    public void setParameter(String parameter, String value);

}
