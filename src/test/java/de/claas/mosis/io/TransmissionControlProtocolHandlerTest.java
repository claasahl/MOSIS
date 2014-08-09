package de.claas.mosis.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Test;

/**
 * The JUnit test for class {@link TransmissionControlProtocolHandler}. It is
 * intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class TransmissionControlProtocolHandlerTest extends
	StreamHandlerImplTest {

    @Override
    public StreamHandlerImpl build() throws Exception {
	StreamHandlerImpl i = new TransmissionControlProtocolHandler();
	i.setParameter(TransmissionControlProtocolHandler.HOST, "google.de");
	i.setParameter(TransmissionControlProtocolHandler.PORT, "80");
	return i;
    }

    @Test
    public void assumptionsOnHost() throws Exception {
	assertEquals("google.de",
		_I.getParameter(TransmissionControlProtocolHandler.HOST));
    }

    @Test
    public void assumptionsOnPort() throws Exception {
	assertEquals("80",
		_I.getParameter(TransmissionControlProtocolHandler.PORT));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterHostMayNotBeNull() throws Exception {
	_I.setParameter(UserDatagramProtocolHandler.HOST, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterPortMayNotBeNull() throws Exception {
	_I.setParameter(UserDatagramProtocolHandler.PORT, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterPortMustBeAnInteger() throws Exception {
	try {
	    _I.setParameter(UserDatagramProtocolHandler.PORT, "1");
	    _I.setParameter(UserDatagramProtocolHandler.PORT, "12");
	} catch (Exception e) {
	    fail(e.toString());
	}
	_I.setParameter(UserDatagramProtocolHandler.PORT, "1.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterPortMustBeValidPort() throws Exception {
	try {
	    _I.setParameter(UserDatagramProtocolHandler.PORT, "0");
	    _I.setParameter(UserDatagramProtocolHandler.PORT, "65535");
	} catch (Exception e) {
	    fail(e.toString());
	}
	_I.setParameter(UserDatagramProtocolHandler.PORT, "65536");
    }

    @Test
    public void shouldConnectToHost() throws Exception {
	InputStream sI = _I.getInputStream();
	OutputStream sO = _I.getOutputStream();
	assertNotNull(sI);
	assertNotNull(sO);
	sI.close();
	sO.close();
    }

    @Test
    public void shouldReadFromSocket() throws Exception {
	ServerSocket srv = new ServerSocket(1234);
	_I.setParameter(TransmissionControlProtocolHandler.HOST, "localhost");
	_I.setParameter(TransmissionControlProtocolHandler.PORT, "1234");

	InputStream in = _I.getInputStream();
	Socket socket = srv.accept();
	OutputStream out = socket.getOutputStream();
	out.write("hello world".getBytes());
	out.flush();
	byte[] buffer = new byte[20];
	int length = in.read(buffer);
	assertTrue(length > 0);
	assertEquals("hello world", new String(buffer, 0, length));
	socket.close();
	srv.close();
    }

    @Test
    public void shouldWriteToSocket() throws Exception {
	ServerSocket srv = new ServerSocket(1234);
	_I.setParameter(TransmissionControlProtocolHandler.HOST, "localhost");
	_I.setParameter(TransmissionControlProtocolHandler.PORT, "1234");

	OutputStream out = _I.getOutputStream();
	Socket socket = srv.accept();
	out.write("hello world".getBytes());
	out.flush();
	byte[] buffer = new byte[20];
	int length = socket.getInputStream().read(buffer);
	assertTrue(length > 0);
	assertEquals("hello world", new String(buffer, 0, length));
	socket.close();
	srv.close();
    }

}
