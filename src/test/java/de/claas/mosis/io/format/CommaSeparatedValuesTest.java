package de.claas.mosis.io.format;

import de.claas.mosis.io.DataHandler;
import de.claas.mosis.io.PipedImpl;
import de.claas.mosis.model.Data;
import de.claas.mosis.util.Utils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link CommaSeparatedValues}. It is intended to
 * collect and document a set of test cases for the tested class. Please refer
 * to the individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class CommaSeparatedValuesTest extends
        AbstractTextFormatTest<Data, CommaSeparatedValues> {

    @Override
    protected CommaSeparatedValues build() throws Exception {
        CommaSeparatedValues p = new CommaSeparatedValues();
        p.setParameter(PlainText.IMPL, PipedImpl.class.getName());
        return p;
    }

    @Test
    public void assumptionsOnSeparator() throws Exception {
        assertEquals(",", _H.getParameter(CommaSeparatedValues.SEPARATOR));
    }

    @Test
    public void assumptionsOnHeader() throws Exception {
        assertEquals("", _H.getParameter(CommaSeparatedValues.HEADER));
    }

    @Test
    public void assumptionsOnHasHeader() throws Exception {
        assertEquals("true", _H.getParameter(CommaSeparatedValues.HAS_HEADER));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterSeparatorMayNotBeNull() throws Exception {
        _H.setParameter(CommaSeparatedValues.SEPARATOR, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterHeaderMayNotBeNull() throws Exception {
        _H.setParameter(CommaSeparatedValues.HEADER, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterHasHeaderMayNotBeNull() throws Exception {
        _H.setParameter(CommaSeparatedValues.HAS_HEADER, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterHasHeaderMustBeBoolean() throws Exception {
        try {
            _H.setParameter(CommaSeparatedValues.HAS_HEADER, "true");
            _H.setParameter(CommaSeparatedValues.HAS_HEADER, "false");
        } catch (Exception e) {
            fail(e.toString());
        }
        _H.setParameter(CommaSeparatedValues.HAS_HEADER, "maybe");
    }

    @Override
    @Test
    public void shouldRead() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("\"attr1\",\"attr2\"\r\n");
        sb.append("\"test1\",\"world1\"\r\n");
        sb.append("\"test2\",\"world2\"\r\n");

        Data data = read(sb.toString());
        assertEquals("test1", data.get("attr1"));
        assertEquals("world1", data.get("attr2"));
        data = Utils.process(_H);
        assertEquals("test2", data.get("attr1"));
        assertEquals("world2", data.get("attr2"));
    }

    @Override
    @Test
    public void shouldWrite() throws Exception {
        Data data = new Data();
        data.put("attr1", "hello");
        data.put("attr2", "world");
        String header = write(data, new Data());
        assertEquals("attr1,attr2", header);
        assertEquals("hello,world", _H.readLine(false));
        assertEquals("null,null", _H.readLine(false));
    }

    @Override
    @Test
    public void shouldDetermineMode() throws Exception {
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_AUTO);

        BufferedOutputStream o = _H.getOutputStream();
        o.write("\"attr1\",\"attr2\"\r\n".getBytes());
        o.write("\"test1\",\"world1\"\r\n".getBytes());
        o.write("\"test2\",\"world2\"\r\n".getBytes());
        o.flush();

        Data data = new Data();
        data.put("attr1", "test1");
        data.put("attr2", "world1");
        assertEquals(data, Utils.process(_H));

        data.put("attr1", "test2");
        data.put("attr2", "world2");
        assertEquals(data, Utils.process(_H));

        data.put("attr1", "test3");
        data.put("attr2", "world3");
        assertEquals(data, Utils.process(_H, data, new Data()));
        assertEquals("attr1,attr2", _H.readLine(false));
        assertEquals("test3,world3", _H.readLine(false));
        assertEquals("null,null", _H.readLine(false));
        assertEquals(0, _H.getInputStream().available());
    }

    @Ignore
    public void shouldNotReadHeader() throws Exception {
        _H.setParameter(CommaSeparatedValues.HAS_HEADER, "false");
        Data data = read("attr1,attr2\n\"hello\",'world'\n");
        assertEquals("attr1", data.get("attribute-0"));
        assertEquals("attr2", data.get("attribute-1"));
        data = Utils.process(_H);
        assertEquals("hello", data.get("attribute-0"));
        assertEquals("world", data.get("attribute-1"));
    }

    @Test
    public void shouldWriteGivenHeader() throws Exception {
        _H.setParameter(CommaSeparatedValues.HEADER, "head1,head2");
        Data data = new Data();
        data.put("attr1", "hello");
        data.put("attr2", "world");
        data.put("head1", "happy");
        data.put("head2", "table");
        assertEquals("head1,head2", write(data, new Data()));
        assertEquals("happy,table", _H.readLine(false));
        assertEquals("null,null", _H.readLine(false));
    }

    @Ignore
    public void shouldReadEscapedElement() throws Exception {
        Data data = read("attr1,attr2,attr3\n\"hello\",\"wo\"\"r\"\"ld\",'te\"\"st'\n");
        assertEquals("hello", data.get("attr1"));
        assertEquals("wo\"r\"ld", data.get("attr2"));
        assertEquals("te\"\"st", data.get("attr3"));
    }

    @Ignore
    public void shouldWriteEscapedElement() throws Exception {
        Data data = new Data();
        data.put("attr1", "hello, world #1");
        data.put("attr2", "happy world #2");
        data.put("attr3", "  abc  ");
        assertEquals("attr1,attr2,attr3", write(data, new Data()));
        assertEquals("\"hello, world #1\",\"happy world #2\",\"  abc  \"",
                _H.readLine(false));
        assertEquals("null,null,null", _H.readLine(false));
    }

    @Test
    public void shouldReadUnescapedElement() throws Exception {
        Data data = read("attr1,attr2\nhello,world\n");
        assertEquals("hello", data.get("attr1"));
        assertEquals("world", data.get("attr2"));
    }

    @Test
    public void shouldReadWithDifferentSeperator() throws Exception {
        _H.setParameter(CommaSeparatedValues.SEPARATOR, "|");
        Data data = read("attr1|attr2\n\"hello\"|world\n");
        assertEquals("hello", data.get("attr1"));
        assertEquals("world", data.get("attr2"));
    }

    @Test
    public void shouldWriteWithDifferentSeperator() throws Exception {
        _H.setParameter(CommaSeparatedValues.SEPARATOR, ";");
        Data data = new Data();
        data.put("attr1", "hello");
        data.put("attr2", "world");
        assertEquals("attr1;attr2", write(data, new Data()));
        assertEquals("hello;world", _H.readLine(false));
        assertEquals("null;null", _H.readLine(false));
    }

    @Ignore
    public void shouldAllowWhitespace() throws Exception {
        Data data = read("attr1 , attr2  \n \" hello\"  ,  world   \n");
        assertEquals(" hello", data.get("attr1"));
        assertEquals("world", data.get("attr2"));
    }

    /**
     * A helper method to avoid code duplicates. The method sets up the
     * {@link DataHandler} for reading. It returns the first data object that
     * was read by the {@link DataHandler}. The given CSV data input is used as
     * input.
     *
     * @param csv the CSV data (for input)
     * @return the first data object that was read by the {@link DataHandler}
     * @throws Exception when an error occurs while writing
     */
    private Data read(String csv) throws Exception {
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_READ);
        BufferedOutputStream o = _H.getOutputStream();
        o.write(csv.getBytes());
        o.close();
        return Utils.process(_H);
    }

    /**
     * A helper method to avoid code duplicates. The method sets up the
     * {@link DataHandler} for writing. It returns the first CSV line that was
     * written by the {@link DataHandler}. The given data object is used as
     * output.
     *
     * @param data the data object (for output)
     * @return the first CSV line that was written by the {@link DataHandler}
     * @throws Exception when an error occurs while reading
     */
    private String write(Data... data) throws Exception {
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_WRITE);
        BufferedInputStream i = _H.getInputStream();
        assertEquals(0, i.available());
        Utils.process(_H, data);
        assertTrue(i.available() > 0);
        return _H.readLine(false);
    }

}
