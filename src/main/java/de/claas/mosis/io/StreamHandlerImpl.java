package de.claas.mosis.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.claas.mosis.model.ConfigurableAdapter;

/**
 * The class {@link StreamHandlerImpl}. It is intended to provide a unified
 * interface for accessing stream-based resources. All {@link StreamHandler}
 * classes utilize this interface and can potentially make use of the herein
 * provided access to {@link InputStream} and {@link OutputStream} instances..
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public abstract class StreamHandlerImpl extends ConfigurableAdapter {

    /**
     * Returns a corresponding {@link InputStream}. It may be assumed that each
     * call to this method results in a new {@link InputStream} object.
     * <code>null</code> may be returned if the {@link InputStream} cannot be
     * created or is not supported.
     * 
     * @return the {@link InputStream}. <code>null</code> if {@link InputStream}
     *         cannot be created.
     * @throws IOException
     *             if something unexpected happens
     */
    public abstract InputStream getInputStream() throws IOException;

    /**
     * Returns a corresponding {@link OutputStream}. It may be assumed that each
     * call to this method results in a new {@link OutputStream} object.
     * <code>null</code> may be returned if the {@link OutputStream} cannot be
     * created or is not supported.
     * 
     * @return the {@link OutputStream}. <code>null</code> if stream cannot be
     *         created.
     * @throws IOException
     *             if something unexpected happens
     */
    public abstract OutputStream getOutputStream() throws IOException;

}
