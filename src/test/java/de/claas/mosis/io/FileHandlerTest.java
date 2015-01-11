package de.claas.mosis.io;

import de.claas.mosis.util.Utils;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertNotNull;

/**
 * The JUnit test for class {@link de.claas.mosis.io.FileHandler}. It is
 * intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class FileHandlerTest extends DataHandlerTest<File, FileHandler> {

    @Override
    protected FileHandler build() throws Exception {
        return new FileHandler();
    }

    @Override
    public void shouldRead() throws Exception {
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_READ);

        assertNotNull(Utils.process(_H));
        assertNotNull(Utils.process(_H));
        assertNotNull(Utils.process(_H));
    }

    @Override
    @Test(expected = IllegalArgumentException.class)
    public void shouldWrite() throws Exception {
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_WRITE);
    }

    @Override
    @Test(expected = IllegalArgumentException.class)
    public void shouldDetermineMode() throws Exception {
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_AUTO);
    }

}
