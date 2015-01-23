package de.claas.mosis.io;

import de.claas.mosis.util.Utils;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.io.FileHandler}. It is
 * intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class FileHandlerTest extends DataHandlerTest<File, FileHandler> {

    private final File tmp1 = new File("./" + FileHandler.class.getSimpleName() + "_1.tmp");
    private final File tmp2 = new File("./" + FileHandler.class.getSimpleName() + "_2.tmp");
    private final File tmp3 = new File("./" + FileHandler.class.getSimpleName() + "_3.tmp");

    @Override
    public void after() {
        super.after();
        tmp1.delete();
        tmp2.delete();
        tmp3.delete();
    }

    @Override
    protected FileHandler build() throws Exception {
        return new FileHandler();
    }

    @Test
    public void shouldNotExist() {
        assertFalse(tmp1.exists());
        assertFalse(tmp2.exists());
        assertFalse(tmp3.exists());
    }

    @Override
    public void shouldRead() throws Exception {
        Utils.updateParameter(_H, DataHandler.MODE, DataHandler.MODE_READ);

        assertNotNull(Utils.process(_H));
        assertNotNull(Utils.process(_H));
        assertNotNull(Utils.process(_H));
    }

    @Override
    public void shouldWrite() throws Exception {
        Utils.updateParameter(_H, DataHandler.MODE, DataHandler.MODE_WRITE);

        assertNull(Utils.process(_H, (File) null));
        assertEquals(Arrays.asList(tmp1, tmp2), Utils.processAll(_H, tmp1, tmp2));
        assertTrue(tmp1.exists());
        assertTrue(tmp2.exists());
        assertFalse(tmp3.exists());
        assertEquals(tmp3, Utils.process(_H, tmp3));
        assertTrue(tmp1.exists());
        assertTrue(tmp2.exists());
        assertTrue(tmp3.exists());
    }

    @Override
    public void shouldDetermineMode() throws Exception {
        Utils.updateParameter(_H, DataHandler.MODE, DataHandler.MODE_AUTO);

        assertNull(Utils.process(_H, (File) null));

        assertEquals(Arrays.asList(tmp1, tmp2), Utils.processAll(_H, tmp1, tmp2));
        assertTrue(tmp1.exists());
        assertTrue(tmp2.exists());
        assertFalse(tmp3.exists());

        assertNotNull(Utils.process(_H));
        assertNotNull(Utils.process(_H));
        assertNotNull(Utils.process(_H));
        assertTrue(tmp1.exists());
        assertTrue(tmp2.exists());
        assertFalse(tmp3.exists());

        assertEquals(tmp3, Utils.process(_H, tmp3));
        assertTrue(tmp1.exists());
        assertTrue(tmp2.exists());
        assertTrue(tmp3.exists());
    }

}
