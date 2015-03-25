package de.claas.mosis.io;

import java.io.*;

/**
 * The class {@link de.claas.mosis.io.PipedImpl}. It is intended to provide a
 * "pipe" between a pair of linked a {@link java.io.InputStream} and a {@link
 * java.io.OutputStream}, such that {@link de.claas.mosis.io.StreamHandler}
 * implementations can process them. This is mainly for debugging purposes.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class PipedImpl extends StreamHandlerImpl {

    private PipedInputStream _PipeIn;
    private PipedOutputStream _PipeOut;

    @Override
    public InputStream getInputStream() throws IOException {
        if (_PipeIn == null) {
            PipedInputStream pipe = new PipedInputStream();
            _PipeOut = new PipedOutputStream(pipe);
            return pipe;
        } else {
            PipedInputStream pipe = _PipeIn;
            _PipeIn = null;
            return pipe;
        }
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (_PipeOut == null) {
            _PipeIn = new PipedInputStream();
            return new PipedOutputStream(_PipeIn);
        } else {
            PipedOutputStream pipe = _PipeOut;
            _PipeOut = null;
            return pipe;
        }
    }

}
