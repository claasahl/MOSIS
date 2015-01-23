package de.claas.mosis.io;

import de.claas.mosis.util.Utils;
import org.junit.Test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.io.UserDatagramProtocolHandler}.
 * It is intended to collect and document a set of test cases for the tested
 * class. Please refer to the individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class UserDatagramProtocolHandlerTest extends
        DataHandlerTest<DatagramPacket, UserDatagramProtocolHandler> {

    /**
     * A helper method to avoid code duplicates. Returns a datagram with random
     * content. The datagram is either addressed to the given socket (as if it
     * is being send there) or it is addressed according to the parameters of
     * the given handler.
     *
     * @param s the socket
     * @param h the handler
     * @return a datagram with random content
     */
    private static DatagramPacket createDatagram(DatagramSocket s,
                                                 UserDatagramProtocolHandler h) {
        byte[] buf = new byte[100];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) (Math.random() * 255);
        }
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        if (s != null) {
            packet.setSocketAddress(s.getLocalSocketAddress());
        } else {
            packet.setSocketAddress(new InetSocketAddress(h
                    .getParameter(UserDatagramProtocolHandler.HOST), Integer
                    .parseInt(h.getParameter(UserDatagramProtocolHandler.PORT))));
        }
        return packet;
    }

    /**
     * A helper method to avoid code duplicates. It is intended to ensure the
     * equality of two datagrams.
     *
     * @param expected the expected datagram
     * @param actual   the actual datagram
     */
    private static void assertDatagramEquals(DatagramPacket expected,
                                             DatagramPacket actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getLength(), actual.getLength());
        for (int i = 0; i < expected.getLength(); i++) {
            assertEquals(expected.getData()[expected.getOffset() + i],
                    actual.getData()[actual.getOffset() + i]);
        }
    }

    @Override
    protected UserDatagramProtocolHandler build() throws Exception {
        return new UserDatagramProtocolHandler();
    }

    @Test
    public void assumptionsOnHost() throws Exception {
        assertEquals("localhost",
                _H.getParameter(UserDatagramProtocolHandler.HOST));
    }

    @Test
    public void assumptionsOnPort() throws Exception {
        assertEquals("12345", _H.getParameter(UserDatagramProtocolHandler.PORT));
    }

    @Test
    public void assumptionsOnBuffer() throws Exception {
        assertEquals("2048",
                _H.getParameter(UserDatagramProtocolHandler.BUFFER));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterHostMayNotBeNull() throws Exception {
        Utils.updateParameter(_H, UserDatagramProtocolHandler.HOST, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterPortMayNotBeNull() throws Exception {
        Utils.updateParameter(_H, UserDatagramProtocolHandler.PORT, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterPortMustBeAnInteger() throws Exception {
        try {
            Utils.updateParameter(_H, UserDatagramProtocolHandler.PORT, "1");
            Utils.updateParameter(_H, UserDatagramProtocolHandler.PORT, "12");
        } catch (Exception e) {
            fail(e.toString());
        }
        Utils.updateParameter(_H, UserDatagramProtocolHandler.PORT, "1.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterPortMustBeValidPort() throws Exception {
        try {
            Utils.updateParameter(_H, UserDatagramProtocolHandler.PORT, "0");
            Utils.updateParameter(_H, UserDatagramProtocolHandler.PORT, "65535");
        } catch (Exception e) {
            fail(e.toString());
        }
        Utils.updateParameter(_H, UserDatagramProtocolHandler.PORT, "65536");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterBufferMayNotBeNull() throws Exception {
        Utils.updateParameter(_H, UserDatagramProtocolHandler.BUFFER, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterBufferMustBeAnInteger() throws Exception {
        try {
            Utils.updateParameter(_H, UserDatagramProtocolHandler.BUFFER, "1");
            Utils.updateParameter(_H, UserDatagramProtocolHandler.BUFFER, "12");
        } catch (Exception e) {
            fail(e.toString());
        }
        Utils.updateParameter(_H, UserDatagramProtocolHandler.BUFFER, "1.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterBufferMustBePositive() throws Exception {
        try {
            Utils.updateParameter(_H, UserDatagramProtocolHandler.BUFFER, "0");
            Utils.updateParameter(_H, UserDatagramProtocolHandler.BUFFER, "12");
        } catch (Exception e) {
            fail(e.toString());
        }
        Utils.updateParameter(_H, UserDatagramProtocolHandler.BUFFER, "-1");
    }

    @Override
    public void shouldRead() throws Exception {
        DatagramSocket s = new DatagramSocket(new InetSocketAddress(
                "localhost", 90));
        Utils.updateParameters(_H,
                DataHandler.MODE, DataHandler.MODE_READ,
                UserDatagramProtocolHandler.HOST, "localhost",
                UserDatagramProtocolHandler.PORT, "81");

        DatagramPacket p = createDatagram(null, _H);
        s.send(p);
        assertDatagramEquals(p, Utils.process(_H));
        s.close();
    }

    @Override
    public void shouldWrite() throws Exception {
        DatagramSocket s = new DatagramSocket(new InetSocketAddress(
                "localhost", 100));
        Utils.updateParameters(_H,
                DataHandler.MODE, DataHandler.MODE_WRITE,
                UserDatagramProtocolHandler.HOST, "localhost",
                UserDatagramProtocolHandler.PORT, "82");

        DatagramPacket p = createDatagram(s, null);
        p.setLength(50);
        DatagramPacket p2 = createDatagram(s, null);
        assertEquals(p, Utils.process(_H, p, createDatagram(s, null)));
        s.receive(p2);
        assertDatagramEquals(p, p2);
        assertNull(Utils.process(_H, (DatagramPacket[]) null));
        s.close();
    }

    @Override
    public void shouldDetermineMode() throws Exception {
        DatagramSocket s = new DatagramSocket(new InetSocketAddress(
                "localhost", 110));
        Utils.updateParameters(_H, DataHandler.MODE, DataHandler.MODE_AUTO,
                UserDatagramProtocolHandler.HOST, "localhost",
                UserDatagramProtocolHandler.PORT, "83");

        DatagramPacket p = createDatagram(s, null);
        p.setLength(42);
        DatagramPacket p2 = createDatagram(s, null);
        assertEquals(p, Utils.process(_H, p, createDatagram(s, null)));
        s.receive(p2);
        assertDatagramEquals(p, p2);

        p = createDatagram(null, _H);
        p.setLength(65);
        s.send(p);
        assertDatagramEquals(p, Utils.process(_H));
        s.close();
    }

}
