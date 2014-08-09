package de.claas.mosis.io.format;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.io.FileImpl;
import de.claas.mosis.io.StreamHandler;
import de.claas.mosis.io.StreamHandlerImpl;
import de.claas.mosis.io.UrlImpl;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Condition.IsGreaterThan;
import de.claas.mosis.model.Configurable;
import de.claas.mosis.model.Relation;

/**
 * The class {@link AbstractTextFormat}. It is intended to provide a common
 * (partial) implementation of the {@link StreamHandler} interface for
 * line-based data formated (e.g. CSV, ARFF, JSON, etc.). This
 * {@link StreamHandler} allows to read and write lines of text (i.e.
 * {@link String}) from any of the {@link StreamHandlerImpl} implementations
 * (e.g. {@link FileImpl} or {@link UrlImpl}).
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public abstract class AbstractTextFormat<T> extends StreamHandler<T> {

    @Parameter("Number of bytes used during input operations.")
    public static final String BUFFER_SIZE = "buffer size";
    @Parameter("Line separator used during output operations.")
    public static final String LINE_SEPARATOR = "line separator";
    @Parameter("Character set used during input / output operations.")
    public static final String CHARSET_NAME = "name of character set";
    private final Queue<String> _Queue;
    private byte[] _Buffer;
    private StringBuilder _StringBuilder;
    private int _Last;
    private BufferedInputStream _Input;
    private BufferedOutputStream _Output;

    /**
     * Initializes the class with default values.
     */
    public AbstractTextFormat() {
	_StringBuilder = new StringBuilder();
	_Queue = new LinkedList<>();
	setParameter(BUFFER_SIZE, 4096);
	addCondition(BUFFER_SIZE, new Condition.IsInteger());
	addCondition(BUFFER_SIZE, new Condition.IsGreaterThan(0d));
	setParameter(LINE_SEPARATOR, System.getProperty("line.separator"));
	addCondition(LINE_SEPARATOR, new Condition.IsNotNull());
	setParameter(CHARSET_NAME, "UTF-8");
	addCondition(CHARSET_NAME, new Condition.IsNotNull());
    }

    @Override
    protected BufferedInputStream getInputStream() throws IOException {
	if (_Input == null) {
	    _Input = new BufferedInputStream(super.getInputStream());
	}
	return _Input;
    }

    @Override
    protected BufferedOutputStream getOutputStream() throws IOException {
	if (_Output == null) {
	    _Output = new BufferedOutputStream(super.getOutputStream());
	}
	return _Output;
    }

    /**
     * Returns the data buffer (used for reading).
     * 
     * @return the data buffer
     */
    protected byte[] getBuffer() {
	return _Buffer;
    }

    @Override
    public void setUp() {
	super.setUp();
	Relation r = new bla();
	r.compute(this, BUFFER_SIZE, getParameter(BUFFER_SIZE));
	addRelation(r);
    }

    @Override
    public void dismantle() {
	super.dismantle();
	removeRelation(new bla());
	if (_Input != null) {
	    try {
		_Input.close();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } finally {
		_Input = null;
	    }
	}
	if (_Output != null) {
	    try {
		_Output.close();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } finally {
		_Output = null;
	    }
	}
    }

    /**
     * Returns the next line of plain text. It optionally preserves any
     * line-termination characters (i.e. 0x0A, 0x0D, 0x0A 0x0D, 0x0D 0x0A). If
     * the end of the stream has been reached, then <code>null</code> is
     * returned.
     * 
     * @param preserveLineEnding
     *            true, if line-termination characters should be preserved.
     *            false, if line-termination characters should not be returned.
     * @return the next line of plain text
     * @throws IOException
     *             if something unexpected happens
     */
    protected String readLine(boolean preserveLineEnding) throws IOException {
	int length = 0, startIndex;
	while (_Queue.isEmpty()
		&& (length = getInputStream().read(_Buffer)) != -1) {
	    startIndex = 0;
	    for (int endIndex = 0; endIndex < length; endIndex++) {
		int prev = endIndex == 0 ? _Last : _Buffer[endIndex - 1];
		int curr = _Buffer[endIndex];
		int eol = endOfLine(prev, curr);
		if (eol > 0) {
		    int tmp = preserveLineEnding ? eol : 0;
		    if (_StringBuilder.length() > 0 && endIndex == 0) {
			if (eol == 2) {
			    _StringBuilder.appendCodePoint(curr);
			}
			_Queue.add(_StringBuilder.substring(0,
				_StringBuilder.length() - eol + tmp));
			_StringBuilder = new StringBuilder();
		    } else if (_StringBuilder.length() > 0) {
			String s = new String(_Buffer, startIndex, endIndex
				- startIndex - 1 + tmp, getParameter(CHARSET_NAME));
			_StringBuilder.append(s);
			_Queue.add(_StringBuilder.toString());
			_StringBuilder = new StringBuilder();
		    } else {
			String s = new String(_Buffer, startIndex, endIndex
				- startIndex - 1 + tmp, getParameter(CHARSET_NAME));
			_Queue.add(s);
		    }
		    startIndex = endIndex + eol - 1;
		    endIndex += eol - 1;
		}
	    }
	    if (startIndex < length) {
		String s = new String(_Buffer, startIndex, length - startIndex, getParameter(CHARSET_NAME));
		_StringBuilder.append(s);
		_Last = _Buffer[length - 1];
	    } else {
		_Last = -1;
	    }
	}
	if (length == -1 && _StringBuilder.length() > 0) {
	    int tmp = preserveLineEnding ? 0 : endOfLine(_Last, -1);
	    _Queue.add(_StringBuilder.substring(0, _StringBuilder.length()
		    - tmp));
	    _StringBuilder = new StringBuilder();
	}
	return _Queue.isEmpty() ? null : _Queue.poll();
    }

    /**
     * Writes a line of plain text. It optionally appends any line-termination
     * characters (i.e. 0x0A, 0x0D, 0x0A 0x0D, 0x0D 0x0A).
     * 
     * @param line
     *            the line of plain text
     * @param appendEndEnding
     *            true, if line-termination characters should be appended.
     *            false, otherwise.
     * @throws IOException
     *             if something unexpected happens
     */
    protected void writeLine(String line, boolean appendEndEnding)
	    throws IOException {
	byte[] data = appendEndEnding ? (line + getParameter(LINE_SEPARATOR))
		.getBytes() : line.getBytes();
	getOutputStream().write(data);
	getOutputStream().flush();
    }

    /**
     * Returns the length (in bytes) if an <code>end of line</code> was found.
     * Otherwise, zero is returned. The combinations 0x0a0d, 0x0d0a, 0x0a and
     * 0x0d are accepted are <code>end of line</code> symbols.
     * 
     * @param prev
     *            the previous byte
     * @param curr
     *            the current byte
     * @return the length (in bytes) if an <code>end of line</code> was found
     */
    private static int endOfLine(int prev, int curr) {
	if (prev == 0x0A && curr == 0x0D || prev == 0x0D && curr == 0x0A) {
	    return 2;
	} else if (prev == 0x0A || prev == 0x0D) {
	    return 1;
	} else {
	    return 0;
	}
    }

    private class bla implements Relation {

	@Override
	public void compute(Configurable configurable, String parameter,
		String value) {
	    if (BUFFER_SIZE.equals(parameter)) {
		_Buffer = new byte[Integer.parseInt(value)];
	    }
	}

    }

}
