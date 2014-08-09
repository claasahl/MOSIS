package de.claas.mosis.io;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Test;

/**
 * The JUnit test for class {@link FileImpl}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class FileImplTest extends StreamHandlerImplTest {

    @Override
    public FileImpl build() throws Exception {
	File f = File.createTempFile("test", FileImpl.class.getName());
	FileImpl i = new FileImpl();
	i.setParameter(FileImpl.FILE, f.getAbsolutePath());
	return i;
    }

    @Test
    public void assumptionsOnFile() throws Exception {
	String name = _I.getParameter(FileImpl.FILE);
	assertTrue(new File(name).exists());
    }

    @Test
    public void assumptionsOnAppend() throws Exception {
	assertEquals("false", _I.getParameter(FileImpl.APPEND));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterFileMayNotBeNull() throws Exception {
	_I.setParameter(FileImpl.FILE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterAppendMayNotBeNull() throws Exception {
	_I.setParameter(FileImpl.APPEND, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterAppendMustBeBoolean() throws Exception {
	try {
	    _I.setParameter(FileImpl.APPEND, "true");
	    _I.setParameter(FileImpl.APPEND, "false");
	} catch (Exception e) {
	    fail(e.toString());
	}
	_I.setParameter(FileImpl.APPEND, "maybe");
    }

    @Test
    public void shouldAppendToFile() throws Exception {
	_I.setParameter(FileImpl.APPEND, "true");

	String name = _I.getParameter(FileImpl.FILE);
	FileOutputStream fout = new FileOutputStream(name);
	fout.write(new byte[] { 4 });
	fout.close();

	OutputStream out = _I.getOutputStream();
	out.write(new byte[] { 1, 2, 3 });
	out.close();

	FileInputStream in = new FileInputStream(name);
	byte[] b = new byte[4];
	assertEquals(4, in.read(b));
	assertArrayEquals(new byte[] { 4, 1, 2, 3 }, b);
	in.close();
    }

    @Test
    public void shouldWriteFile() throws Exception {
	OutputStream out = _I.getOutputStream();
	out.write(new byte[] { 1, 2, 3 });
	out.close();

	String name = _I.getParameter(FileImpl.FILE);
	FileInputStream in = new FileInputStream(name);
	byte[] b = new byte[4];
	assertEquals(3, in.read(b));
	assertArrayEquals(new byte[] { 1, 2, 3, 0 }, b);
	in.close();
    }

    @Test
    public void shouldReadFile() throws Exception {
	String name = _I.getParameter(FileImpl.FILE);
	FileOutputStream out = new FileOutputStream(name);
	out.write(new byte[] { 1, 2, 3 });
	out.close();

	InputStream in = _I.getInputStream();
	byte[] b = new byte[4];
	assertEquals(3, in.read(b));
	assertArrayEquals(new byte[] { 1, 2, 3, 0 }, b);
	out.close();
    }

}
