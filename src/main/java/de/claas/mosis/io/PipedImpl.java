package de.claas.mosis.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * The class {@link PipedImpl}. It is intended to provide a "pipe" between a
 * pair of linked a {@link InputStream} and a {@link OutputStream}, such that
 * {@link StreamHandler} implementations can process them. This is mainly for
 * debugging purposes.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
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
	    PipedOutputStream pipe = new PipedOutputStream(_PipeIn);
	    return pipe;
	} else {
	    PipedOutputStream pipe = _PipeOut;
	    _PipeOut = null;
	    return pipe;
	}
    }

}
