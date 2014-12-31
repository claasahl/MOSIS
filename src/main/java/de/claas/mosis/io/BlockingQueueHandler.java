package de.claas.mosis.io;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Configurable;
import de.claas.mosis.model.Processor;
import de.claas.mosis.model.Relation;
import de.claas.mosis.util.Utils;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The class {@link BlockingQueueHandler}. It is intended to enable
 * communication with external entities through a {@link BlockingQueue}. This
 * {@link DataHandler} provide an alternative to the otherwise (mostly)
 * stream-based communication. This will typically be utilized in scenarios
 * where an external entity needs to push {@link Object}s directly into the
 * framework (and not through other means such as files or databases).
 *
 * @param <T> type of (incoming and outgoing) data. See {@link Processor} for
 *            details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class BlockingQueueHandler<T> extends DataHandler<T> {

    // TODO Add to test(s)
    @Parameter("Name of class from queue. An instance of this class backs this handler. Any class, implementing java.util.concurrent.BlockingQueue, can be used.")
    public static final String CLASS = "class (queue)";
    private BlockingQueue<T> _Queue;

    /**
     * Initializes the class with default values.
     */
    public BlockingQueueHandler() {
        addCondition(CLASS, new Condition.ClassExists());
        setParameter(CLASS, LinkedBlockingQueue.class.getName());
    }

    /**
     * Returns the (input / output) {@link Queue}. When in "reading" mode (see
     * {@link #isReadOnly(List)}), then the head of the queue is removed and
     * returned every time {@link #process(java.util.List, java.util.List)} is called. When in "writing"
     * mode (see {@link #isWriteOnly(List)}), then all incoming elements are
     * appended to the queue every time {@link #process(java.util.List, java.util.List)} is called.
     *
     * @return the (input / output) {@link Queue}
     */
    public BlockingQueue<T> getQueue() {
        return _Queue;
    }

    @Override
    public void setUp() {
        super.setUp();
        Relation r = new ImplCreator();
        addRelation(r);
        r.compute(this, CLASS, getParameter(CLASS));
    }

    @Override
    public void dismantle() {
        super.dismantle();
        removeRelation(new ImplCreator());
        _Queue = null;
    }

    @Override
    public void process(List<T> in, List<T> out) {
        if (isReadOnly(in)) {
            try {
                out.add(getQueue().take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            for (T obj : in) {
                try {
                    getQueue().put(obj);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (shouldForward()) {
                out.addAll(in);
            }
        }
    }

    /**
     * The class {@link ImplCreator}. It is intended to create {@link Queue}
     * objects whenever the {@link BlockingQueueHandler#CLASS} parameter is
     * changed.
     *
     * @author Claas Ahlrichs (claasahl@tzi.de)
     */
    private class ImplCreator implements Relation {

        @SuppressWarnings("unchecked")
        @Override
        public void compute(Configurable configurable, String parameter,
                            String value) {
            if (CLASS.equals(parameter)) {
                try {
                    _Queue = (BlockingQueue<T>) Utils.instance(Class
                            .forName(value));
                } catch (Exception e) {
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
