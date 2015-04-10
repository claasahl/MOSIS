package de.claas.mosis.io;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.util.Utils;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The class {@link de.claas.mosis.io.BlockingQueueHandler}. It is intended to
 * enable communication with external entities through a {@link
 * java.util.concurrent.BlockingQueue}. This {@link de.claas.mosis.io.DataHandler}
 * provide an alternative to the otherwise (mostly) stream-based communication.
 * This will typically be utilized in scenarios where an external entity needs
 * to push {@link java.lang.Object}s directly into the framework (and not
 * through other means such as files or databases).
 *
 * @param <T> type of (incoming and outgoing) data. See {@link
 *            de.claas.mosis.model.Processor} for details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        category = Category.InputOutput,
        author = {"Claas Ahlrichs"},
        description = "This is a realization of a DataHandler which allows external programs to communicate with the framework. It allows external entities to push and pull data into / from the framework thus circumventing the otherwise (mostly) stream-based interface options. The actual implementation of the utilized queue can be configured to any BlockingQueue (by default a LinkedBlockingQueue us used). In contrast to the QueueHandler, this module will block during reading and writing operations when necessary. This can be especially useful when the external entity wants to provide data samples at its own pace rather than adhering to the timing requirements of the framework.",
        purpose = "To allow storage and retrieval of objects within a (blocking) queue.")
public class BlockingQueueHandler<T> extends DataHandler<T> {

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
     * Returns the (input / output) {@link java.util.Queue}. When in "reading"
     * mode (see {@link #isReadOnly(java.util.List)}), then the head of the
     * queue is removed and returned every time {@link #process(java.util.List,
     * java.util.List)} is called. When in "writing" mode (see {@link
     * #isWriteOnly(java.util.List)}, then all incoming elements are appended to
     * the queue every time {@link #process(java.util.List, java.util.List)} is
     * called.
     *
     * @return the (input / output) {@link java.util.Queue}
     */
    public BlockingQueue<T> getQueue() {
        return _Queue;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setUp() {
        super.setUp();
        try {
            _Queue = (BlockingQueue) Utils.instance(Class
                    .forName(getParameter(CLASS)));
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
}
