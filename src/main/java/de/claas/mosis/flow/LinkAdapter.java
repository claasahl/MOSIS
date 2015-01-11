package de.claas.mosis.flow;

import de.claas.mosis.model.ConfigurableAdapter;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * The class {@link de.claas.mosis.flow.LinkAdapter}. It is intended to provide
 * a common implementation of the {@link de.claas.mosis.flow.Link} interface. It
 * acts as a link between two {@link de.claas.mosis.model.Processor} objects and
 * accepts all objects (i.e. no objects are discarded).
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class LinkAdapter extends ConfigurableAdapter implements Link {

    private final Queue<Object> _Buffer;

    /**
     * Initializes the class with default values.
     */
    public LinkAdapter() {
        _Buffer = new LinkedList<>();
    }

    @Override
    public boolean push(List<Object> in) {
        return _Buffer.addAll(in);
    }

    @Override
    public Object poll() {
        if (isEmpty()) {
            throw new IllegalStateException();
        } else {
            return _Buffer.poll();
        }
    }

    @Override
    public boolean isEmpty() {
        return _Buffer.isEmpty();
    }

}
