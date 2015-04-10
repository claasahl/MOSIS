package de.claas.mosis.processing.debug;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.model.DecoratorProcessor;

import java.util.List;
import java.util.Vector;

/**
 * The class {@link de.claas.mosis.processing.debug.BreakOut}. It is intended
 * for debugging purposes. This {@link de.claas.mosis.model.DecoratorProcessor}
 * implementation provides access to the most recent input (see {@link
 * #getLastInput()}) and output (see {@link #getLastOutput()}) values as well as
 * several other potentially relevant statistics.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        category = Category.Decorator,
        author = {"Claas Ahlrichs"},
        description = "This is a realization of a DecoratorProcessor that is mostly intended for debugging purposes. This implementation provides access to the most recent set of input and output data that were passed to the process-method. It also counts the number of calls to various methods (e.g. setUp, process and dismantle).",
        purpose = "This implementation is intended for debugging purposes.")
public class BreakOut extends DecoratorProcessor<Object, Object> {

    private List<Object> _Input;
    private List<Object> _Output;
    private int _CallsToSetup;
    private int _CallsToDismantle;
    private int _CallsToProcess;

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
     * Returns the number of calls of {@link #setUp()}.
     *
     * @return the number of calls of {@link #setUp()}
     */
    public int getCallsToSetUp() {
        return _CallsToSetup;
    }

    /**
     * Returns the number of calls of {@link #dismantle()}.
     *
     * @return the number of calls of {@link #dismantle()}
     */
    public int getCallsToDismantle() {
        return _CallsToDismantle;
    }

    /**
     * Returns the number of calls of {@link #process(List, List)}.
     *
     * @return the number of calls of {@link #process(List, List)}
     */
    public int getCallsToProcess() {
        return _CallsToProcess;
    }

    @Override
    public void setUp() {
        super.setUp();
        _CallsToSetup++;
    }

    @Override
    public void dismantle() {
        super.dismantle();
        _CallsToDismantle++;
        _Input = null;
        _Output = null;
    }

    @Override
    public void process(List<Object> in, List<Object> out) {
        _CallsToProcess++;
        _Input = in == null ? null : new Vector<>(in);
        super.process(in, out);
        _Output = out == null ? null : new Vector<>(out);
    }

}
