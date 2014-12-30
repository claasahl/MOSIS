package de.claas.mosis.flow;

import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Configurable;
import de.claas.mosis.model.Relation;

import java.util.ArrayList;
import java.util.List;

/**
 * The class {@link BiasedLink}. It is intended to act as a link between two
 * {@link Node} objects that only allows objects of a certain type to pass
 * through. Other objects are discarded.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class BiasedLink extends LinkAdapter {

    public static final String CLASS = "class";
    private final List<Object> _Accepted;
    private Class<?> _Class;

    /**
     * Initializes the class with default values.
     */
    public BiasedLink() {
        _Accepted = new ArrayList<>();
        addCondition(CLASS, new Condition.IsNotNull());
        addRelation(new ClassCreator());
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
        return super.push(in);
    }

    /**
     * The class {@link ClassCreator}. It is intended to create {@link Class}
     * objects whenever the {@link BiasedLink#CLASS} parameter is changed.
     *
     * @author Claas Ahlrichs (claasahl@tzi.de)
     */
    private class ClassCreator implements Relation {

        @Override
        public void compute(Configurable configurable, String parameter,
                            String value) {
            if (CLASS.equals(parameter)) {
                try {
                    _Class = Class.forName(value);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public boolean equals(Object obj) {
            return obj != null && getClass().equals(obj.getClass());
        }
    }

}
