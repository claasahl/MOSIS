package de.claas.mosis.model;

import java.util.List;
import java.util.regex.Pattern;

import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.util.Utils;

/**
 * The class {@link DecoratorProcessor}. It is an implementation of the
 * {@link Processor} interface. This {@link Processor} is intended to wrap
 * another {@link Processor} object and meant forward all method calls to the
 * wrapped object. Subclasses may want to override some (or all methods), thus
 * extending the original {@link Processor}'s functionality and responsibility.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 * @param <I>
 *            type of incoming data. See {@link Processor} for details.
 * @param <O>
 *            type of outgoing data. See {@link Processor} for details.
 */
@Documentation(
	purpose = "It is intended to wrap another module in order to extend its functionality and / or intercept calls to it.",
	description = "This implementation is meant to be overridden in one way or another. It is intended to wrap another module and forwards all method calls to it. Subclasses may want to override some (or all methods) to add functionality and behavior.",
	author = "Claas Ahlrichs",
	noOutputData = "This implementation will forward output data from the module that it wraps. Refer to the wrapped module for more details.")
public class DecoratorProcessor<I, O> extends ProcessorAdapter<I, O> {

    @Parameter("Name of class from decorated processor / module. An instance of this class backs this decorator. Any class, implementing de.claas.mosis.model.ProcessorAdapter, can be used.")
    public static final String CLASS = "class of processor";
    public static final String SHADOWED = "wrapped.";
    private ProcessorAdapter<I, O> _Processor;

    /**
     * Initializes the class with default values.
     */
    public DecoratorProcessor() {
	setParameter(CLASS, "");
	addCondition(CLASS, new Condition.ClassExists());
	addRelation(new ProcessorCreator());
    }

    @Override
    public List<String> getParameters() {
	List<String> params = super.getParameters();
	if (_Processor != null) {
	    params.addAll(_Processor.getParameters());
	}
	return params;
    }

    @Override
    public String getParameter(String parameter) {
	if (_Processor != null && !isLocalParameter(parameter)) {
	    return _Processor.getParameter(parameter.replaceFirst(
		    Pattern.quote(SHADOWED), ""));
	} else {
	    return super.getParameter(parameter);
	}
    }

    @Override
    public void setParameter(String parameter, String value) {
	if (_Processor != null && !isLocalParameter(parameter)) {
	    _Processor.setParameter(
		    parameter.replaceFirst(Pattern.quote(SHADOWED), ""), value);
	} else {
	    super.setParameter(parameter, value);
	}
    }

    @Override
    protected void addCondition(String parameter, Condition condition) {
	if (_Processor != null && !isLocalParameter(parameter)) {
	    _Processor.addCondition(parameter, condition);
	} else {
	    super.addCondition(parameter, condition);
	}
    }

    @Override
    protected void removeCondition(String parameter, Condition condition) {
	if (_Processor != null && !isLocalParameter(parameter)) {
	    _Processor.removeCondition(parameter, condition);
	} else {
	    super.removeCondition(parameter, condition);
	}
    }

    @Override
    protected void addRelation(Relation relation) {
	if (_Processor != null) {
	    _Processor.addRelation(relation);
	} else {
	    super.addRelation(relation);
	}
    }

    @Override
    protected void removeRelation(Relation relation) {
	if (_Processor != null) {
	    _Processor.removeRelation(relation);
	} else {
	    super.removeRelation(relation);
	}
    }

    @Override
    public void setUp() {
	super.setUp();
	if(_Processor != null) {
	    _Processor.setUp();
	}
    }

    @Override
    public void dismantle() {
	super.dismantle();
	if (_Processor != null) {
	    _Processor.dismantle();
	    _Processor = null;
	}
    }

    @Override
    public void process(List<I> in, List<O> out) {
	_Processor.process(in, out);
    }

    /**
     * Returns the wrapped / decorated {@link Processor}.
     * 
     * @return the wrapped / decorated {@link Processor}
     */
    protected ProcessorAdapter<I, O> getProcessor() {
	return _Processor;
    }

    /**
     * Returns <code>true</code>, if the parameter belongs to this processor (as
     * opposed to the processor that is being decorated). Otherwise,
     * <code>false</code> is returned.
     * 
     * @param parameter
     *            the parameter
     * @return <code>true</code>, if the parameter belongs to this processor
     */
    protected boolean isLocalParameter(String parameter) {
	return super.getParameters().contains(parameter)
		&& !parameter.startsWith(SHADOWED);
    }

    /**
     * The class {@link ProcessorCreator}. It is intended to create
     * {@link Processor} objects whenever the {@link DecoratorProcessor#CLASS}
     * parameter is changed.
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * 
     */
    private class ProcessorCreator implements Relation {

	@SuppressWarnings("unchecked")
	@Override
	public void compute(Configurable configurable, String parameter,
		String value) {
	    if (CLASS.equals(parameter)) {
		if (_Processor != null) {
		    _Processor.dismantle();
		    _Processor = null;
		}
		try {
		    _Processor = (ProcessorAdapter<I, O>) Utils.instance(Class
			    .forName(value));
		    if (_Processor != null) {
			_Processor.setUp();
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}

	@Override
	public boolean equals(Object obj) {
	    return obj == null ? false : getClass().equals(obj.getClass());
	}

    }

}
