package de.claas.mosis.io;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * The class {@link de.claas.mosis.io.UserDatagramProtocolHandler}. It is
 * intended to enable communication with external entities through a {@link
 * java.net.DatagramSocket} (UDP connection). This {@link
 * de.claas.mosis.io.DataHandler} provide an alternative to the otherwise
 * (mostly) stream-based communication. This implementation allows the use of
 * UDP connections, such that {@link de.claas.mosis.model.Processor} modules can
 * process {@link java.net.DatagramPacket} objects.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        category = Category.InputOutput,
        author = {"Claas Ahlrichs"},
        description = "This is a realization of a DataHandler which provides access to UDP-based services. The module can be used to retrieve and transmit UDP-based datagrams. Depending on the the mode, this implementation can either retrieve (read-only), send (write-only) or both (read-and-write). The server and port with which the communication is established are configurable. The buffer used for reading datagrams can also be configured (by default 2048 bytes are allocated).",
        purpose = "To allow storage in UDP datagrams and retrieval of UDP datagrams.")
public class UserDatagramProtocolHandler extends DataHandler<DatagramPacket> {

    @Parameter("Local hostname / IP to which the socket is bound.")
    public static final String HOST = "hostname";
    @Parameter("Local port to which the socket is bound.")
    public static final String PORT = "port";
    @Parameter("Number of bytes used during input operations.")
    public static final String BUFFER = "size of buffer";
    protected DatagramSocket _Socket;

    /**
     * Initializes the class with default values.
     */
    public UserDatagramProtocolHandler() {
        addCondition(HOST, new Condition.IsNotNull());
        setParameter(HOST, "localhost");
        addCondition(PORT, new Condition.IsInteger());
        addCondition(PORT, new Condition.IsGreaterOrEqual(0d));
        addCondition(PORT, new Condition.IsLessThan(Math.pow(2, 16)));
        setParameter(PORT, 12345);
        addCondition(BUFFER, new Condition.IsInteger());
        addCondition(BUFFER, new Condition.IsGreaterOrEqual(0d));
        setParameter(BUFFER, 2048);
    }

    @Override
    public void setUp() {
        super.setUp();
        try {
            String host = getParameter(HOST);
            Integer port = getParameterAsInteger(PORT);
            if (host != null && port != null) {
                _Socket = new DatagramSocket(new InetSocketAddress(
                        host, port));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public void dismantle() {
        super.dismantle();
        if (_Socket != null) {
            _Socket.close();
            _Socket = null;
        }
    }

    @Override
    public void process(List<DatagramPacket> in, List<DatagramPacket> out) {
        try {
            if (isReadOnly(in)) {
                int length = getParameterAsInteger(BUFFER);
                DatagramPacket p = new DatagramPacket(new byte[length], length);
                _Socket.receive(p);
                out.add(p);
            } else if (in != null) {
                for (DatagramPacket p : in) {
                    _Socket.send(p);
                }
                if (shouldForward()) {
                    out.addAll(in);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
