package de.claas.mosis.io;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.*;
import de.claas.mosis.util.Utils;

import java.io.*;
import java.util.List;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * The class {@link StreamHandler}. It is a partial implementation of the
 * {@link DataHandler} class and forms the core of all stream-based input /
 * output handling {@link Processor}s. This {@link DataHandler} is intended to
 * enable external entities to communicate with the framework through
 * stream-based resources.
 * <p/>
 * Calls to {@link #getParameters()}, {@link #getParameter(String)} and
 * {@link #setParameter(String, String)} take the parameters of the
 * {@link StreamHandlerImpl} instance into account (i.e. they are forwarded to
 * the {@link StreamHandlerImpl} instance if the given parameter is defined for
 * it). As opposed to {@link DecoratorProcessor} classes, calls to
 * {@link #addCondition(String, Condition)},
 * {@link #removeCondition(String, Condition)}, etc. are not forwarded.
 *
 * @param <T> type of (incoming and outgoing) data. See {@link Processor} for
 *            details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public abstract class StreamHandler<T> extends DataHandler<T> {

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
        addRelation(new ImplCreator());
        setParameter(IMPL, StandardInputOutputImpl.class.getName());
    }

    @Override
    public List<String> getParameters() {
        List<String> params = super.getParameters();
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                _Input = null;
            }
        }
        if (_Output != null) {
            try {
                _Output.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                _Output = null;
            }
        }
    }

    /**
     * Returns the corresponding {@link StreamHandlerImpl}.
     *
     * @return the corresponding {@link StreamHandlerImpl}
     */
    protected StreamHandlerImpl getImpl() {
        return _Impl;
    }

    /**
     * Returns <code>true</code>, if the parameter belongs to this processor.
     * Otherwise (e.g. parameter belongs to {@link StreamHandlerImpl}),
     * <code>false</code> is returned.
     *
     * @param parameter the parameter
     * @return <code>true</code>, if the parameter belongs to this processor
     */
    private boolean isLocalParameter(String parameter) {
        return _Impl == null || !_Impl.getParameters().contains(parameter);
    }

    /**
     * Returns and caches an {@link InputStream}. Once an {@link InputStream} is
     * created (see {@link StreamHandlerImpl#getInputStream()} for details), it
     * is cached and succeeding calls to this method return the same instance.
     * Concrete implementations of the {@link StreamHandler} interface may wish
     * to override this method and return a more specialized {@link InputStream}
     * (e.g. {@link DataInputStream} or {@link ZipInputStream}).
     *
     * @return the {@link InputStream}
     * @throws IOException See {@link StreamHandlerImpl#getInputStream()} for details.
     */
    protected InputStream getInputStream() throws IOException {
        if (_Input == null) {
            _Input = _Impl.getInputStream();
        }
        return _Input;
    }

    /**
     * Returns and caches an {@link OutputStream}. Once an {@link OutputStream}
     * is created (see {@link StreamHandlerImpl#getOutputStream()} for details),
     * it is cached and succeeding calls to this method return the same
     * instance. Concrete implementations of the {@link StreamHandler} interface
     * may wish to override this method and return a more specialized
     * {@link OutputStream} (e.g. {@link DataOutputStream} or
     * {@link ZipOutputStream}).
     *
     * @return the {@link OutputStream}
     * @throws IOException See {@link StreamHandlerImpl#getInputStream()} for details.
     */
    protected OutputStream getOutputStream() throws IOException {
        if (_Output == null) {
            _Output = _Impl.getOutputStream();
        }
        return _Output;
    }

    /**
     * The class {@link ImplCreator}. It is intended to create
     * {@link StreamHandlerImpl} objects whenever the {@link StreamHandler#IMPL}
     * parameter is changed.
     *
     * @author Claas Ahlrichs (claasahl@tzi.de)
     */
    private class ImplCreator implements Relation {

        @Override
        public void compute(Configurable configurable, String parameter,
                            String value) {
            if (IMPL.equals(parameter)) {
                try {
                    _Impl = (StreamHandlerImpl) Utils.instance(Class
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
