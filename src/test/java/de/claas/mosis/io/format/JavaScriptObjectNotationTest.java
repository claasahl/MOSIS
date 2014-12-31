package de.claas.mosis.io.format;

import de.claas.mosis.io.DataHandler;
import de.claas.mosis.io.PipedImpl;
import de.claas.mosis.io.StreamHandler;
import de.claas.mosis.model.Data;
import de.claas.mosis.util.Utils;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link JavaScriptObjectNotation}. It is intended to
 * collect and document a set of test cases for the tested class. Please refer
 * to the individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class JavaScriptObjectNotationTest extends
        AbstractTextFormatTest<Data, JavaScriptObjectNotation> {

    @Override
    protected JavaScriptObjectNotation build() throws Exception {
        JavaScriptObjectNotation p = new JavaScriptObjectNotation();
        p.setParameter(PlainText.IMPL, PipedImpl.class.getName());
        return p;
    }

    @Test
    public void assumptionsOnImpl() throws Exception {
        assertEquals(PipedImpl.class.getName(),
                _H.getParameter(JavaScriptObjectNotation.IMPL));
    }

    @Override
    @Test
    public void shouldRead() throws Exception {
        Data data = read("{\"test\":\"hello\"}\r\n{\"test\":\"world\"}\r\n");
        assertEquals("hello", data.get("test"));
        assertEquals("world", Utils.process(_H, (Data) null).get("test"));
    }

    @Override
    @Test
    public void shouldWrite() throws Exception {
        Data data = new Data();
        data.put("hello", "world");
        assertEquals("{\"hello\":\"world\"}", write(data, new Data()));
        assertEquals("{}", _H.readLine(false));
    }

    @Override
    @Test
    public void shouldDetermineMode() throws Exception {
        _H.setParameter(StreamHandler.IMPL, PipedImpl.class.getName());
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_AUTO);

        BufferedOutputStream o = _H.getOutputStream();
        o.write("{\"test\":\"testing\"}\r\n".getBytes());
        o.flush();
        BufferedInputStream i = _H.getInputStream();
        Data data = new Data();
        data.put("test", "hello world");
        assertEquals("testing", Utils.process(_H).get("test"));
        assertEquals(data, Utils.process(_H, data, new Data()));
        assertEquals("{\"test\":\"hello world\"}", _H.readLine(false));
        assertEquals("{}", _H.readLine(false));
        assertEquals(0, i.available());
    }

    @Test
    public void shouldReadEmptyObject() throws Exception {
        Data data = read("{}\r\n");
        assertTrue(data.isEmpty());
    }

    @Test
    public void shouldWriteEmptyObject() throws Exception {
        Data data = new Data();
        assertEquals("{}", write(data));
    }

    @Test
    public void shouldReadNumber() throws Exception {
        Data data = read("{\"num1\":10,\"num2\":-23,\"num3\":23.42,\"num4\":1.5e-23}\r\n");
        assertEquals(new BigDecimal(10), data.get("num1"));
        assertEquals(new BigDecimal(-23), data.get("num2"));
        assertEquals(23.42, data.getAsNumber("num3").doubleValue(), 0.0001);
        assertEquals(1.5e-23, data.getAsNumber("num4").doubleValue(), 0.0001);
    }

    @Test
    public void shouldWriteNumber() throws Exception {
        Data data = new Data();
        data.put("num1", 10);
        data.put("num2", -23);
        data.put("num3", 23.42);
        data.put("num4", 1.5e-23);
        assertEquals(
                "{\"num1\":10,\"num2\":-23,\"num3\":23.42,\"num4\":1.5E-23}",
                write(data));
    }

    @Test
    public void shouldReadArray() throws Exception {
        Data data = read("{\"arr\":[\"abc\", -2.3, 12]}\r\n");
        List<Object> array = data.getAs("arr");
        assertEquals(3, array.size());
        assertEquals("abc", array.get(0));
        assertEquals(-2.3, ((BigDecimal) array.get(1)).doubleValue(), 0.0001);
        assertEquals(new BigDecimal(12), array.get(2));
    }

    @Test
    public void shouldWriteArray() throws Exception {
        List<Object> array = new Vector<>();
        array.add("abc");
        array.add(-2.3);
        array.add(12);
        Data data = new Data();
        data.put("arr", array);
        assertEquals("{\"arr\":[\"abc\",-2.3,12]}", write(data));
    }

    @Test
    public void shouldReadString() throws Exception {
        Data data = read("{\"s1\":\"hello\",\"s2\":\"world\"}\r\n");
        assertEquals("hello", data.get("s1"));
        assertEquals("world", data.get("s2"));
    }

    @Test
    public void shouldWriteString() throws Exception {
        Data data = new Data();
        data.put("s1", "hello");
        data.put("s2", "world");
        assertEquals("{\"s1\":\"hello\",\"s2\":\"world\"}", write(data));
    }

    @Test
    public void shouldReadNull() throws Exception {
        Data data = read("{\"null\":null}\r\n");
        assertNull(data.get("null"));
    }

    @Test
    public void shouldWriteNull() throws Exception {
        Data data = new Data();
        data.put("null", null);
        assertEquals("{\"null\":null}", write(data));
    }

    @Test
    public void shouldReadBoolean() throws Exception {
        Data data = read("{\"b1\":true,\"b2\":false}\r\n");
        assertTrue(data.<Boolean>getAs("b1"));
        assertFalse(data.<Boolean>getAs("b2"));
    }

    @Test
    public void shouldWriteBoolean() throws Exception {
        Data data = new Data();
        data.put("b1", true);
        data.put("b2", false);
        assertEquals("{\"b1\":true,\"b2\":false}", write(data));
    }

    @Test
    public void shouldReadNestedObject() throws Exception {
        Data data = read("{\"temp\":{\"string\":\"tree\"}}\r\n");
        assertTrue(data.containsKey("temp"));
        Data temp = data.getAs("temp");
        assertEquals("tree", temp.get("string"));
    }

    @Test
    public void shouldWriteNestedObject() throws Exception {
        Data temp = new Data();
        temp.put("string", "tree");
        Data data = new Data();
        data.put("temp", temp);
        assertEquals("{\"temp\":{\"string\":\"tree\"}}", write(data));
    }

    /**
     * A helper method to avoid code duplicates. The method sets up the
     * {@link DataHandler} for reading. It returns the first data object that
     * was read by the {@link DataHandler}. The given JSON data input is used as
     * input.
     *
     * @param json the JSON data (for input)
     * @return the first data object that was read by the {@link DataHandler}
     * @throws Exception when an error occurs while writing
     */
    private Data read(String json) throws Exception {
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_READ);
        BufferedOutputStream o = _H.getOutputStream();
        o.write(json.getBytes());
        o.flush();
        return Utils.process(_H);
    }

    /**
     * A helper method to avoid code duplicates. The method sets up the
     * {@link DataHandler} for writing. It returns the first JSON line that was
     * written by the {@link DataHandler}. The given data object is used as
     * output.
     *
     * @param data the data object (for output)
     * @return the first JSON line that was written by the {@link DataHandler}
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
