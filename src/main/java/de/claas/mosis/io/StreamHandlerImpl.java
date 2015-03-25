package de.claas.mosis.io;

import de.claas.mosis.model.ConfigurableAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The class {@link de.claas.mosis.io.StreamHandlerImpl}. It is intended to
 * provide a unified interface for accessing stream-based resources. All {@link
 * de.claas.mosis.io.StreamHandler} classes utilize this interface and can
 * potentially make use of the herein provided access to {@link
 * java.io.InputStream} and {@link java.io.OutputStream} instances..
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public abstract class StreamHandlerImpl extends ConfigurableAdapter {

    /**
     * Returns a corresponding {@link java.io.InputStream}. It may be assumed
     * that each call to this method results in a new {@link
     * java.io.InputStream} object. <code>null</code> may be returned if the
     * {@link java.io.InputStream} cannot be created or is not supported.
     *
     * @return the {@link java.io.InputStream}. <code>null</code> if {@link
     * java.io.InputStream} cannot be created.
     * @throws java.io.IOException if something unexpected happens
     */
    public abstract InputStream getInputStream() throws IOException;

    /**
     * Returns a corresponding {@link java.io.OutputStream}. It may be assumed
     * that each call to this method results in a new {@link
     * java.io.OutputStream} object. <code>null</code> may be returned if the
     * {@link java.io.OutputStream} cannot be created or is not supported.
     *
     * @return the {@link java.io.OutputStream}. <code>null</code> if stream
     * cannot be created.
     * @throws java.io.IOException if something unexpected happens
     */
    public abstract OutputStream getOutputStream() throws IOException;

}
