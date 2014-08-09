package de.claas.mosis.io.format;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.claas.mosis.io.PipedImpl;
import de.claas.mosis.io.StreamHandlerTest;

/**
 * The JUnit test for class {@link AbstractTextFormat}. It is intended to
 * collect and document a set of test cases for the tested class. Please refer
 * to the individual tests for more detailed information.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public abstract class AbstractTextFormatTest<S, T extends AbstractTextFormat<S>>
	extends StreamHandlerTest<S, T> {

    protected T _H;

    @Before
    public void before() throws Exception {
	super.before();
	_H = build();
	_H.setParameter(PlainText.IMPL, PipedImpl.class.getName());
	_H.setUp();
    }

    @After
    public void after() {
	super.after();
	_H.dismantle();
    }

    @Test
    public void assumptionsOnBufferSize() throws Exception {
	assertEquals("4096", _H.getParameter(AbstractTextFormat.BUFFER_SIZE));
    }

    @Test
    public void assumptionsOnMode() throws Exception {
	assertEquals(AbstractTextFormat.MODE_AUTO,
		_H.getParameter(AbstractTextFormat.MODE));
    }

    @Test
    public void assumptionsOnImpl() throws Exception {
	assertEquals(PipedImpl.class.getName(),
		_H.getParameter(AbstractTextFormat.IMPL));
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
    public void assumesThatMarkIsSupported() throws Exception {
	assertTrue(_H.getInputStream().markSupported());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterBufferSizeMayNotBeNull() throws Exception {
	_H.setParameter(AbstractTextFormat.BUFFER_SIZE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterBufferSizeMustBeAnInteger() throws Exception {
	try {
	    _H.setParameter(AbstractTextFormat.BUFFER_SIZE, "1");
	    _H.setParameter(AbstractTextFormat.BUFFER_SIZE, "12");
	} catch (Exception e) {
	    fail(e.toString());
	}
	_H.setParameter(AbstractTextFormat.BUFFER_SIZE, "1.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterBufferSizeMustBePostive() throws Exception {
	try {
	    _H.setParameter(AbstractTextFormat.BUFFER_SIZE, "1");
	    _H.setParameter(AbstractTextFormat.BUFFER_SIZE, "12");
	} catch (Exception e) {
	    fail(e.toString());
	}
	_H.setParameter(AbstractTextFormat.BUFFER_SIZE, "0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterLineSeparatorMayNotBeNull() throws Exception {
	_H.setParameter(AbstractTextFormat.LINE_SEPARATOR, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterCharsetNameMayNotBeNull() throws Exception {
	_H.setParameter(AbstractTextFormat.CHARSET_NAME, null);
    }

    @Test
    public void shouldReadLine() throws Exception {
	BufferedOutputStream sO = _H.getOutputStream();
	sO.write("hello\n".getBytes());
	sO.write("world\n".getBytes());
	sO.close();
	assertEquals("hello\n", _H.readLine(true));
	assertEquals("world", _H.readLine(false));
	assertNull(_H.readLine(true));
	assertNull(_H.readLine(false));
    }

    @Test
    public void shouldWriteLine() throws Exception {
	_H.writeLine("hello", true);
	_H.writeLine("world\n", false);
	BufferedReader sI = new BufferedReader(new InputStreamReader(
		_H.getInputStream()));
	assertEquals("hello", sI.readLine());
	assertEquals("world", sI.readLine());
    }

    @Test
    public void shouldReadEmptyLinesAndPreserveEndOfLine() throws Exception {
	BufferedOutputStream sO = _H.getOutputStream();
	sO.write("\n\r\r\n\n \r\r \n\n".getBytes());
	sO.close();
	assertEquals("\n\r", _H.readLine(true));
	assertEquals("\r\n", _H.readLine(true));
	assertEquals("\n", _H.readLine(true));

	assertEquals(" \r", _H.readLine(true));
	assertEquals("\r", _H.readLine(true));
	assertEquals(" \n", _H.readLine(true));
	assertEquals("\n", _H.readLine(true));
	assertNull(_H.readLine(true));
    }

    @Test
    public void shouldReadEmptyLines() throws Exception {
	BufferedOutputStream sO = _H.getOutputStream();
	sO.write("\n\r\r\n\n \r\r \n\n".getBytes());
	sO.close();
	assertEquals("", _H.readLine(false));
	assertEquals("", _H.readLine(false));
	assertEquals("", _H.readLine(false));

	assertEquals(" ", _H.readLine(false));
	assertEquals("", _H.readLine(false));
	assertEquals(" ", _H.readLine(false));
	assertEquals("", _H.readLine(false));
	assertNull(_H.readLine(false));
    }

    @Test
    public void shouldHandleInsufficientBufferSizeAndPreserveEndOfLine() throws Exception {
	_H.setParameter(AbstractTextFormat.BUFFER_SIZE, "10");
	BufferedOutputStream sO = _H.getOutputStream();
	sO.write("abc\r\ndef\nghijkl\n\rmnop".getBytes());
	sO.close();
	
	assertEquals("abc\r\n", _H.readLine(true));
	assertEquals("def\n", _H.readLine(true));
	assertEquals("ghijkl\n\r", _H.readLine(true));
	assertEquals("mnop", _H.readLine(true));
	assertNull(_H.readLine(true));
    }
    
    @Test
    public void shouldHandleInsufficientBufferSize() throws Exception {
	_H.setParameter(AbstractTextFormat.BUFFER_SIZE, "10");
	BufferedOutputStream sO = _H.getOutputStream();
	sO.write("abc\r\ndef\nghijkl\n\rmnop".getBytes());
	sO.close();
	
	assertEquals("abc", _H.readLine(false));
	assertEquals("def", _H.readLine(false));
	assertEquals("ghijkl", _H.readLine(false));
	assertEquals("mnop", _H.readLine(false));
	assertNull(_H.readLine(false));
    }
    
    
    @Test
    public void shouldHandleIncompleteEndOfLineAndPreserveEndOfLine() throws Exception {
	_H.setParameter(AbstractTextFormat.BUFFER_SIZE, "4");
	BufferedOutputStream sO = _H.getOutputStream();
	sO.write("abc\r\ndef\nghijkl\n\rmnop".getBytes());
	sO.close();
	
	assertEquals("abc\r\n", _H.readLine(true));
	assertEquals("def\n", _H.readLine(true));
	assertEquals("ghijkl\n\r", _H.readLine(true));
	assertEquals("mnop", _H.readLine(true));
	assertNull(_H.readLine(true));
    }
    
    @Test
    public void shouldHandleIncompleteEndOfLine() throws Exception {
	_H.setParameter(AbstractTextFormat.BUFFER_SIZE, "4");
	BufferedOutputStream sO = _H.getOutputStream();
	sO.write("abc\r\ndef\nghijkl\n\rmnop".getBytes());
	sO.close();
	
	assertEquals("abc", _H.readLine(false));
	assertEquals("def", _H.readLine(false));
	assertEquals("ghijkl", _H.readLine(false));
	assertEquals("mnop", _H.readLine(false));
	assertNull(_H.readLine(false));
    }
    
    @Test
    public void shouldChangeBufferSize() {
	assertEquals(4096, _H.getBuffer().length);
	_H.setParameter(AbstractTextFormat.BUFFER_SIZE, "4");
	assertEquals(4, _H.getBuffer().length);
	_H.setParameter(AbstractTextFormat.BUFFER_SIZE, "10");
	assertEquals(10, _H.getBuffer().length);
    }

}
