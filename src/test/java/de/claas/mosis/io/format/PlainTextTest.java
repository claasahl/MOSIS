package de.claas.mosis.io.format;

import de.claas.mosis.io.DataHandler;
import de.claas.mosis.io.PipedImpl;
import de.claas.mosis.util.Utils;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.io.format.PlainText}. It is
 * intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class PlainTextTest extends AbstractTextFormatTest<String, PlainText> {

    @Override
    protected PlainText build() throws Exception {
        PlainText p = new PlainText();
        p.setParameter(PlainText.IMPL, PipedImpl.class.getName());
        return p;
    }

    @Test
    public void assumptionsOnImpl() throws Exception {
        assertEquals(PipedImpl.class.getName(), _H.getParameter(PlainText.IMPL));
    }

    @Test
    public void assumptionsOnLineSeparator() throws Exception {
        assertEquals(System.getProperty("line.separator"),
                _H.getParameter(AbstractTextFormat.LINE_SEPARATOR));
    }

    @Test
    public void assumptionsOnCharsetName() throws Exception {
        assertEquals("UTF-8", _H.getParameter(AbstractTextFormat.CHARSET_NAME));
    }

    @Test
    public void assumptionsOnPrefix() throws Exception {
        assertEquals("", _H.getParameter(PlainText.PREFIX));
    }

    @Test
    public void assumptionsOnAppendNewline() throws Exception {
        assertEquals("true", _H.getParameter(PlainText.APPEND_NEWLINE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterPrefixMayNotBeNull() throws Exception {
        Utils.updateParameter(_H, PlainText.PREFIX, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterAppendNewlineMayNotBeNull() throws Exception {
        Utils.updateParameter(_H, PlainText.APPEND_NEWLINE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterHasHeaderMustBeBoolean() throws Exception {
        try {
            Utils.updateParameters(_H,
                    PlainText.APPEND_NEWLINE, "true",
                    PlainText.APPEND_NEWLINE, "false");
        } catch (Exception e) {
            fail(e.toString());
        }
        Utils.updateParameter(_H, PlainText.APPEND_NEWLINE, "maybe");
    }

    @Override
    @Test
    public void shouldRead() throws Exception {
        String text = read("hello world #1\nhello world #2\n");
        assertEquals("hello world #1", text);
        assertEquals("hello world #2", Utils.process(_H));
    }

    @Override
    @Test
    public void shouldWrite() throws Exception {
        String text = write("hello world #1", "hello world #2", null);
        assertEquals("hello world #1", text);
        assertEquals("hello world #2", _H.readLine(false));
        assertEquals("null", _H.readLine(false));
    }

    @Override
    @Test
    public void shouldDetermineMode() throws Exception {
        Utils.updateParameter(_H, PlainText.MODE, PlainText.MODE_AUTO);

        String newline = System.getProperty("line.separator");
        BufferedOutputStream o = _H.getOutputStream();
        o.write(("hello world #1" + newline).getBytes());
        o.write(("hello world #2" + newline).getBytes());
        o.flush();
        assertEquals("hello world #1", Utils.process(_H));
        assertEquals("hello world #2", Utils.process(_H));
        assertEquals("hello world #3", Utils.process(_H, "hello world #3", ""));
        assertEquals("hello world #3", _H.readLine(false));
        assertEquals("", _H.readLine(false));
        assertEquals(0, _H.getInputStream().available());
    }

    @Test
    public void shouldNotAppendNewline() throws Exception {
        Utils.updateParameter(_H, PlainText.APPEND_NEWLINE, "false");
        String data = "hello world #1\nhello world #2\rhello world #3";
        assertEquals("hello world #1", write(data, ""));
        assertEquals("hello world #2", _H.readLine(false));
        assertEquals("hello world #3", _H.readLine(false));
    }

    /**
     * A helper method to avoid code duplicates. The method sets up the {@link
     * de.claas.mosis.io.DataHandler} for reading. It returns the first data
     * object that was read by the {@link de.claas.mosis.io.DataHandler}. The
     * given (plain) text data input is used as input.
     *
     * @param text the (plain) text data (for input)
     * @return the first data object that was read by the {@link
     * de.claas.mosis.io.DataHandler}
     * @throws java.lang.Exception when an error occurs while writing
     */
    private String read(String text) throws Exception {
        Utils.updateParameter(_H, DataHandler.MODE, DataHandler.MODE_READ);
        BufferedOutputStream o = _H.getOutputStream();
        o.write(text.getBytes());
        o.close();
        return Utils.process(_H);
    }

    /**
     * A helper method to avoid code duplicates. The method sets up the {@link
     * de.claas.mosis.io.DataHandler} for writing. It returns the first (plain)
     * text line that was written by the {@link de.claas.mosis.io.DataHandler}.
     * The given data object is used as output.
     *
     * @param text the data object (for output)
     * @return the first (plain) text line that was written by the {@link
     * de.claas.mosis.io.DataHandler}
     * @throws java.lang.Exception when an error occurs while reading
     */
    private String write(String... text) throws Exception {
        Utils.updateParameter(_H, DataHandler.MODE, DataHandler.MODE_WRITE);
        BufferedInputStream i = _H.getInputStream();
        assertEquals(0, i.available());
        Utils.process(_H, text);
        assertTrue(i.available() > 0);
        _H.getOutputStream().close();
        return _H.readLine(false);
    }

}
