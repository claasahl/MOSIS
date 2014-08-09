package de.claas.mosis.io.format;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import org.junit.Test;

import de.claas.mosis.io.DataHandler;
import de.claas.mosis.io.PipedImpl;
import de.claas.mosis.io.StreamHandler;
import de.claas.mosis.model.Data;
import de.claas.mosis.util.Utils;

/**
 * The JUnit test for class {@link AttributeRelationFileFormat}. It is intended
 * to collect and document a set of test cases for the tested class. Please
 * refer to the individual tests for more detailed information.
 * 
 * TODO Add format-specific tests
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class AttributeRelationFileFormatTest extends
	AbstractTextFormatTest<Data, AttributeRelationFileFormat> {

    @Override
    protected AttributeRelationFileFormat build() throws Exception {
	AttributeRelationFileFormat p = new AttributeRelationFileFormat();
	p.setParameter(PlainText.IMPL, PipedImpl.class.getName());
	return p;
    }

    @Test
    public void assumptionsOnImpl() throws Exception {
	assertEquals(PipedImpl.class.getName(),
		_H.getParameter(CommaSeparatedValues.IMPL));
    }

    @Override
    @Test
    public void shouldRead() throws Exception {
	fail();
	Data data = read("some data here");
	assertEquals("some value", data.get("some attr."));
    }

    @Override
    @Test
    public void shouldWrite() throws Exception {
	fail();
	Data data = new Data();
	data.put("hello", "world");
	assertEquals("i don't know", write(data, new Data()));
	assertEquals("some value", _H.readLine(false));
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

    /**
     * A helper method to avoid code duplicates. The method sets up the
     * {@link DataHandler} for reading. It returns the first data object that
     * was read by the {@link DataHandler}. The given ARFF data input is used as
     * input.
     * 
     * @param arff
     *            the ARFF data (for input)
     * @return the first data object that was read by the {@link DataHandler}
     * @throws Exception
     *             when an error occurs while writing
     */
    private Data read(String arff) throws Exception {
	_H.setParameter(DataHandler.MODE, DataHandler.MODE_READ);
	BufferedOutputStream o = _H.getOutputStream();
	o.write(arff.getBytes());
	o.flush();
	return Utils.process(_H);
    }

    /**
     * A helper method to avoid code duplicates. The method sets up the
     * {@link DataHandler} for writing. It returns the first ARFF line that was
     * written by the {@link DataHandler}. The given data object is used as
     * output.
     * 
     * @param data
     *            the data object (for output)
     * @return the first ARFF line that was written by the {@link DataHandler}
     * @throws Exception
     *             when an error occurs while reading
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
