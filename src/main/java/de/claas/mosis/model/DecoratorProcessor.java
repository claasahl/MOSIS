package de.claas.mosis.model;

import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.util.Utils;

import java.util.Collection;
import java.util.List;

/**
 * The class {@link de.claas.mosis.model.DecoratorProcessor}. It is an
 * implementation of the {@link de.claas.mosis.model.Processor} interface. This
 * {@link de.claas.mosis.model.Processor} is intended to wrap another {@link
 * de.claas.mosis.model.Processor} object and meant forward all method calls to
 * the wrapped object. Consequently, the default behaviour is that all
 * parameters and conditions are forwarded as well. Subclasses may want to
 * override some (or all methods), thus extending the original {@link
 * de.claas.mosis.model.Processor}'s functionality and responsibility.
 *
 * @param <I> type of incoming data. See {@link de.claas.mosis.model.Processor}
 *            for details.
 * @param <O> type of outgoing data. See {@link de.claas.mosis.model.Processor}
 *            for details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        purpose = "It is intended to wrap another module in order to extend its functionality and / or intercept calls to it.",
        description = "This implementation is meant to be overridden in one way or another. It is intended to wrap another module and to forward all method calls to it. Subclasses may want to override some (or all methods) to add functionality and behavior to existing modules. Most decorators within the framework use this default implementation to realize their specific functionality. When new decorators are added to the framework, then this module is likely to provide all the required default behavior. It is encouraged to use concrete decorators as reference implementations (e.g. Time or Logger).",
        author = "Claas Ahlrichs",
        noOutputData = "This implementation will forward output data from the module that it wraps. Refer to the wrapped module for more details.")
public class DecoratorProcessor<I, O> extends ProcessorAdapter<I, O> implements Observer {

    @Parameter("Name of class from decorated processor / module. An instance of this class backs this decorator. Any class, implementing de.claas.mosis.model.ProcessorAdapter, can be used.")
    public static final String CLASS = "class of processor";
    public static final String SHADOWED = "wrapped.";
    protected static final String LOCAL = "local.";
    private ConfigurableAdapter _Configuration = new ConfigurableAdapter();
    private ProcessorAdapter<I, O> _Processor;

    /**
     * Initializes the class with default values.
     */
    public DecoratorProcessor() {
        setParameter(LOCAL + CLASS, "");
        addCondition(LOCAL + CLASS, new Condition.ClassExists());
        addObserver(this);
    }

    @Override
    public Collection<String> getParameters() {
        Collection<String> params = super.getParameters();
        if (_Processor != null) {
            params.addAll(_Processor.getParameters());
        } else {
            params.addAll(_Configuration.getParameters());
        }
        return params;
    }

    @Override
    public String getParameter(String parameter) {
        if (isDecoratedParameter(parameter)) {
            parameter = fixParameter(parameter);
            if (_Processor != null) {
                return _Processor.getParameter(parameter);
            } else {
                return _Configuration.getParameter(parameter);
            }
        } else {
            parameter = fixParameter(parameter);
            return super.getParameter(parameter);
        }
    }

    @Override
    public void setParameter(String parameter, String value) {
        if (isDecoratedParameter(parameter)) {
            parameter = fixParameter(parameter);
            _Configuration.setParameter(parameter, value);
            if (_Processor != null) {
                _Processor.setParameter(parameter, value);
            }
        } else {
            parameter = fixParameter(parameter);
            super.setParameter(parameter, value);
        }
    }

    @Override
    protected void addCondition(String parameter, Condition condition) {
        if (isDecoratedParameter(parameter)) {
            parameter = fixParameter(parameter);
            _Configuration.addCondition(parameter, condition);
            if (_Processor != null) {
                _Processor.addCondition(parameter, condition);
            }
        } else {
            parameter = fixParameter(parameter);
            super.addCondition(parameter, condition);
        }
    }

    @Override
    protected void removeCondition(String parameter, Condition condition) {
        if (isDecoratedParameter(parameter)) {
            parameter = fixParameter(parameter);
            _Configuration.removeCondition(parameter, condition);
            if (_Processor != null) {
                _Processor.removeCondition(parameter, condition);
            }
        } else {
            parameter = fixParameter(parameter);
            super.removeCondition(parameter, condition);
        }
    }

    @Override
    protected Collection<Condition> getConditions(String parameter) {
        if (isDecoratedParameter(parameter)) {
            parameter = fixParameter(parameter);
            if (_Processor != null) {
                return _Processor.getConditions(parameter);
            } else {
                return _Configuration.getConditions(parameter);
            }
        } else {
            parameter = fixParameter(parameter);
            return super.getConditions(parameter);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        super.addObserver(observer);
        _Configuration.addObserver(observer);
        if (_Processor != null) {
            _Processor.addObserver(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        super.removeObserver(observer);
        _Configuration.removeObserver(observer);
        if (_Processor != null) {
            _Processor.removeObserver(observer);
        }
    }

    @Override
    protected Collection<Observer> getObservers() {
        Collection<Observer> observers = super.getObservers();
        if (_Processor != null) {
            // Observers in this decorator, the temp. configuration and
            // the decorated module are (mostly) synchronized). We should not
            // consider those observers that are in all of these lists.
            Collection<Observer> tmp = _Processor.getObservers();
            tmp.removeAll(_Configuration.getObservers());
            observers.addAll(tmp);
        }
        return observers;
    }

    @Override
    public void notifyObservers(String parameter) {
        if (isDecoratedParameter(parameter)) {
            parameter = fixParameter(parameter);
            if (_Processor != null) {
                _Processor.notifyObservers(parameter);
            } else {
                _Configuration.notifyObservers(parameter);
            }
        } else {
            parameter = fixParameter(parameter);
            super.notifyObservers(parameter);
        }
    }

    @Override
    public void setUp() {
        super.setUp();
        if (_Processor != null) {
            _Processor.setUp();
        }
    }

    @Override
    public void dismantle() {
        super.dismantle();
        if (_Processor != null) {
            _Processor.dismantle();
        }
    }

    @Override
    public void process(List<I> in, List<O> out) {
        if (_Processor != null) {
            _Processor.process(in, out);
        }
    }

    @Override
    protected void update(ConfigurableAdapter configurable, boolean clear) {
        if (clear)
            _Configuration = new ConfigurableAdapter();
        super.update(configurable, clear);
    }

    /**
     * Returns the wrapped / decorated {@link de.claas.mosis.model.Processor}.
     *
     * @return the wrapped / decorated {@link de.claas.mosis.model.Processor}
     */
    protected ProcessorAdapter<I, O> getProcessor() {
        return _Processor;
    }

    /**
     * Returns <code>true</code>, if the parameter belongs to the decorated
     * processor (as opposed to this processor). Otherwise, <code>false</code>
     * is returned.
     *
     * @param parameter the parameter
     * @return <code>true</code>, if the parameter belongs to the decorated
     * processor
     */
    protected boolean isDecoratedParameter(String parameter) {
        return parameter != null && !parameter.startsWith(LOCAL) &&
                !super.getParameters().contains(parameter);
    }

    /**
     * Returns the parameter without the prefix. The prefix {@value #LOCAL} or
     * {@value #SHADOWED} is going to be removed, as it is only used to indicate
     * whether the parameter should be stored within the decorated module or the
     * decorating module.
     * <p/>
     * This function is closely related to {@link #isDecoratedParameter(String)}.
     *
     * @param parameter the parameter
     * @return the parameter, but may have removed any prefix
     */
    private String fixParameter(String parameter) {
        if (parameter != null && parameter.startsWith(LOCAL))
            parameter = parameter.replaceFirst(LOCAL, "");
        if (parameter != null && parameter.startsWith(SHADOWED))
            parameter = parameter.replaceFirst(SHADOWED, "");
        return parameter;
    }

    @Override
    public void update(Configurable configurable, String parameter) {
        if (CLASS.equals(parameter) && this.equals(configurable)) {
            try {
                // Create module that is being "decorated"
                Class<?> clazz = Class.forName(getParameter(CLASS));
                _Processor = (ProcessorAdapter<I, O>) Utils.instance(clazz);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Update configuration of "decorated" module
                if (_Processor != null)
                    _Processor.update(_Configuration, false);
            }
        }
    }
}
