package de.claas.mosis.io;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.util.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * The class {@link de.claas.mosis.io.QueueHandler}. It is intended to enable
 * communication with external entities through a {@link java.util.Queue}. This
 * {@link de.claas.mosis.io.DataHandler} provide an alternative to the otherwise
 * (mostly) stream-based communication. This will typically be utilized in
 * scenarios where an external entity needs to push {@link java.lang.Object}s
 * directly into the framework (and not through other means such as files or
 * databases).
 *
 * @param <T> type of (incoming and outgoing) data. See {@link
 *            de.claas.mosis.model.Processor} for details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class QueueHandler<T> extends DataHandler<T> {

    @Parameter("Name of class from queue. An instance of this class backs this handler. Any class, implementing java.util.Queue, can be used.")
    public static final String CLASS = "class (queue)";
    private Queue<T> _Queue;

    /**
     * Initializes the class with default values.
     */
    public QueueHandler() {
        addCondition(CLASS, new Condition.ClassExists());
        setParameter(CLASS, LinkedList.class.getName());
    }

    /**
     * Returns the (input / output) {@link java.util.Queue}. When in "reading"
     * mode (see {@link #isReadOnly(List)}), then the head of the queue is
     * removed and returned every time {@link #process(java.util.List,
     * java.util.List)} is called. When in "writing" mode (see {@link
     * #isWriteOnly(List)}), then all incoming elements are appended to the
     * queue every time {@link #process(java.util.List, java.util.List)} is
     * called.
     *
     * @return the (input / output) {@link java.util.Queue}
     */
    public Queue<T> getQueue() {
        return _Queue;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setUp() {
        super.setUp();
        try {
            _Queue = (Queue) Utils.instance(Class.forName(getParameter(CLASS)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismantle() {
        super.dismantle();
        _Queue = null;
    }

    @Override
    public void process(List<T> in, List<T> out) {
        if (isReadOnly(in)) {
            if (!getQueue().isEmpty()) {
                out.add(getQueue().poll());
            }
        } else {
            for (T obj : in) {
                getQueue().offer(obj);
            }
            if (shouldForward()) {
                out.addAll(in);
            }
        }
    }
}
