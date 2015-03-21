package de.claas.mosis.io;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Configurable;
import de.claas.mosis.model.Observer;
import de.claas.mosis.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

/**
 * The class {@link de.claas.mosis.io.StreamHandler}. It is a partial
 * implementation of the {@link de.claas.mosis.io.DataHandler} class and forms
 * the core of all stream-based input / output handling {@link
 * de.claas.mosis.model.Processor}s. This {@link de.claas.mosis.io.DataHandler}
 * is intended to enable external entities to communicate with the framework
 * through stream-based resources.
 * <p/>
 * Calls to {@link #getParameters()}, {@link #getParameter(String)} and {@link
 * #setParameter(String, String)} take the parameters of the {@link
 * de.claas.mosis.io.StreamHandlerImpl} instance into account (i.e. they are
 * forwarded to the {@link de.claas.mosis.io.StreamHandlerImpl} instance if the
 * given parameter is defined for it). As opposed to {@link
 * de.claas.mosis.model.DecoratorProcessor} classes, calls to {@link
 * #addCondition(String, Condition)}, {@link #removeCondition(String,
 * Condition)}, etc. are not forwarded.
 *
 * @param <T> type of (incoming and outgoing) data. See {@link
 *            de.claas.mosis.model.Processor} for details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public abstract class StreamHandler<T> extends DataHandler<T> implements Observer {

    @Parameter("Name of class from StreamHandlerImpl. An instance of this class backs this handler.  Any class, implementing de.claas.mosis.io.StreamHandlerImpl, can be used.")
    public static final String IMPL = "impl";
    private StreamHandlerImpl _Impl;
    private InputStream _Input;
    private OutputStream _Output;

    /**
     * Initializes the class with default values.
     */
    public StreamHandler() {
        addCondition(IMPL, new Condition.ClassExists());
        addObserver(this);
        setParameter(IMPL, StandardInputOutputImpl.class.getName());
    }

    @Override
    public Collection<String> getParameters() {
        Collection<String> params = super.getParameters();
        if (_Impl != null) {
            params.addAll(_Impl.getParameters());
        }
        return params;
    }

    @Override
    public String getParameter(String parameter) {
        if (_Impl != null && !isLocalParameter(parameter)) {
            return _Impl.getParameter(parameter);
        } else {
            return super.getParameter(parameter);
        }
    }

    @Override
    public void setParameter(String parameter, String value) {
        if (_Impl != null && !isLocalParameter(parameter)) {
            _Impl.setParameter(parameter, value);
        } else {
            super.setParameter(parameter, value);
        }
    }

    @Override
    public void dismantle() {
        super.dismantle();
        if (_Input != null) {
            try {
                _Input.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                _Input = null;
            }
        }
        if (_Output != null) {
            try {
                _Output.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                _Output = null;
            }
        }
    }

    /**
     * Returns the corresponding {@link de.claas.mosis.io.StreamHandlerImpl}.
     *
     * @return the corresponding {@link de.claas.mosis.io.StreamHandlerImpl}
     */
    protected StreamHandlerImpl getImpl() {
        return _Impl;
    }

    /**
     * Returns <code>true</code>, if the parameter belongs to this processor.
     * Otherwise (e.g. parameter belongs to {@link de.claas.mosis.io.StreamHandlerImpl}),
     * <code>false</code> is returned.
     *
     * @param parameter the parameter
     * @return <code>true</code>, if the parameter belongs to this processor
     */
    private boolean isLocalParameter(String parameter) {
        return _Impl == null || !_Impl.getParameters().contains(parameter);
    }

    /**
     * Returns and caches an {@link java.io.InputStream}. Once an {@link
     * java.io.InputStream} is created (see {@link de.claas.mosis.io.StreamHandlerImpl#getInputStream()}
     * for details), it is cached and succeeding calls to this method return the
     * same instance. Concrete implementations of the {@link
     * de.claas.mosis.io.StreamHandler} interface may wish to override this
     * method and return a more specialized {@link java.io.InputStream} (e.g.
     * {@link java.io.DataInputStream} or {@link java.util.zip.ZipInputStream}).
     *
     * @return the {@link java.io.InputStream}
     * @throws java.io.IOException See {@link de.claas.mosis.io.StreamHandlerImpl#getInputStream()}
     *                             for details.
     */
    protected InputStream getInputStream() throws IOException {
        if (_Input == null) {
            _Input = _Impl.getInputStream();
        }
        return _Input;
    }

    /**
     * Returns and caches an {@link java.io.OutputStream}. Once an {@link
     * java.io.OutputStream} is created (see {@link de.claas.mosis.io.StreamHandlerImpl#getOutputStream()}
     * for details), it is cached and succeeding calls to this method return the
     * same instance. Concrete implementations of the {@link
     * de.claas.mosis.io.StreamHandler} interface may wish to override this
     * method and return a more specialized {@link java.io.OutputStream} (e.g.
     * {@link java.io.DataOutputStream} or {@link java.util.zip.ZipOutputStream}).
     *
     * @return the {@link java.io.OutputStream}
     * @throws java.io.IOException See {@link de.claas.mosis.io.StreamHandlerImpl#getInputStream()}
     *                             for details.
     */
    protected OutputStream getOutputStream() throws IOException {
        if (_Output == null) {
            _Output = _Impl.getOutputStream();
        }
        return _Output;
    }

    @Override
    public void update(Configurable configurable, String parameter) {
        if (IMPL.equals(parameter)) {
            try {
                Class<?> clazz = Class.forName(getParameter(IMPL));
                _Impl = (StreamHandlerImpl) Utils.instance(clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
