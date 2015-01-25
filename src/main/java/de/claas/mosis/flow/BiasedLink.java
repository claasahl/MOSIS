package de.claas.mosis.flow;

import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Configurable;
import de.claas.mosis.model.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * The class {@link de.claas.mosis.flow.BiasedLink}. It is intended to act as a
 * link between two {@link de.claas.mosis.flow.Node} objects that only allows
 * objects of a certain type to pass through. Other objects are discarded.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class BiasedLink extends LinkAdapter implements Observer {

    public static final String CLASS = "class";
    private final List<Object> _Accepted;
    private Class<?> _Class;

    /**
     * Initializes the class with default values.
     */
    public BiasedLink() {
        _Accepted = new ArrayList<>();
        addCondition(CLASS, new Condition.IsNotNull());
        addObserver(this);
        setParameter(CLASS, Object.class.getName());
    }

    @Override
    public boolean push(List<Object> in) {
        _Accepted.clear();
        for (Object o : in) {
            if (o == null || _Class.isAssignableFrom(o.getClass())) {
                _Accepted.add(o);
            }
        }
        return super.push(_Accepted);
    }

    @Override
    public void update(Configurable configurable, String parameter) {
        if (CLASS.equals(parameter)) {
            try {
                _Class = Class.forName(getParameter(CLASS));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
