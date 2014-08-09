package de.claas.mosis.flow;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.claas.mosis.model.ConfigurableAdapter;
import de.claas.mosis.model.Processor;

/**
 * The class {@link LinkAdapter}. It is intended to provide a common
 * implementation of the {@link Link} interface. It acts as a link between two
 * {@link Processor} objects and accepts all objects (i.e. no objects are
 * discarded).
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class LinkAdapter extends ConfigurableAdapter implements Link {

    private final Queue<Object> _Buffer;

    /**
     * Initializes the class with default values.
     */
    public LinkAdapter() {
	_Buffer = new LinkedList<Object>();
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
