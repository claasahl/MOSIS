package de.claas.mosis.processing.debug;

import de.claas.mosis.model.DecoratorProcessor;

import java.util.List;
import java.util.Vector;

/**
 * The class {@link BreakOut}. It is intended for debugging purposes. This
 * {@link DecoratorProcessor} implementation provides access to the most recent
 * input (see {@link #getLastInput()}) and output (see {@link #getLastOutput()})
 * values.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class BreakOut extends DecoratorProcessor<Object, Object> {

    private List<Object> _Input;
    private List<Object> _Output;
    private int _Calls;

    /**
     * Returns the latest input values.
     *
     * @return the latest input values
     */
    public List<Object> getLastInput() {
        return _Input;
    }

    /**
     * Returns the latest output values.
     *
     * @return the latest output values
     */
    public List<Object> getLastOutput() {
        return _Output;
    }

    /**
     * Returns the number of calls of {@link #process(List, List)}.
     *
     * @return the number of calls of {@link #process(List, List)}
     */
    public int getCalls() {
        return _Calls;
    }

    @Override
    public void dismantle() {
        super.dismantle();
        _Input = null;
        _Output = null;
    }

    @Override
    public void process(List<Object> in, List<Object> out) {
        _Calls++;
        _Input = in == null ? null : new Vector<Object>(in);
        super.process(in, out);
        _Output = out == null ? null : new Vector<Object>(out);
    }

}
