package de.claas.mosis.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The class {@link StandardInputOutputImpl}. It is intended to provide access
 * the operating system's standard input and output streams, such that
 * {@link StreamHandler} implementations can process them.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class StandardInputOutputImpl extends StreamHandlerImpl {

    @Override
    public InputStream getInputStream() throws IOException {
        return System.in;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return System.out;
    }

}
