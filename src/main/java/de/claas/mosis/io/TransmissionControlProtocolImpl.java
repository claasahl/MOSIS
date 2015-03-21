package de.claas.mosis.io;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The class {@link TransmissionControlProtocolImpl}. It is
 * intended to provide access to TCP enabled services and data sources, such
 * that {@link de.claas.mosis.io.StreamHandler} implementations can process
 * them.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class TransmissionControlProtocolImpl extends StreamHandlerImpl {

    @Parameter("Remote hostname / IP to which the socket connects.")
    public static final String HOST = "hostname";
    @Parameter("Remote port to which the socket connects.")
    public static final String PORT = "port";
    @Parameter("Whether a connection (to an external entity) should be established or a connection (from an external entity) should be awaited.")
    public static final String AWAIT_CONNECTION = "await connection";
    protected ServerSocket _ServerSocket;
    protected Socket _Socket;
    protected InputStream _SocketIn;
    protected OutputStream _SocketOut;

    /**
     * Initializes the class with default values.
     */
    public TransmissionControlProtocolImpl() {
        addCondition(HOST, new Condition.IsNotNull());
        setParameter(HOST, "localhost");
        addCondition(PORT, new Condition.IsInteger());
        addCondition(PORT, new Condition.IsGreaterOrEqual(0d));
        addCondition(PORT, new Condition.IsLessThan(Math.pow(2, 16)));
        setParameter(PORT, 12345);
        addCondition(AWAIT_CONNECTION, new Condition.IsBoolean());
        setParameter(AWAIT_CONNECTION, false);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (_SocketIn == null) {
            createSocket();
            _SocketOut = _Socket.getOutputStream();
            return _Socket.getInputStream();
        } else {
            InputStream socketIn = _SocketIn;
            _SocketIn = null;
            return socketIn;
        }
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (_SocketOut == null) {
            createSocket();
            _SocketIn = _Socket.getInputStream();
            return _Socket.getOutputStream();
        } else {
            OutputStream socketOut = _SocketOut;
            _SocketOut = null;
            return socketOut;
        }
    }

    /**
     * Creates a new {@link java.net.Socket} that can be exploited by {@link
     * #getInputStream()} and {@link #getOutputStream()}. Any previously opened
     * socket is closed.
     *
     * @throws java.io.IOException if {@link java.net.Socket} cannot be closed
     *                             or opened
     */
    protected void createSocket() throws IOException {
        // Close existing sockets / connections
        if (_Socket != null) {
            _Socket.close();
        }
        if (_ServerSocket != null) {
            _ServerSocket.close();
        }

        if (getParameterAsBoolean(AWAIT_CONNECTION)) {
            String host = getParameter(HOST);
            Integer port = getParameterAsInteger(PORT);
            if (host != null && port != null) {
                _ServerSocket = new ServerSocket(port, 1, InetAddress.getByName(host));
                _Socket = _ServerSocket.accept();
            } else {
                _ServerSocket = null;
                _Socket = null;
            }
        } else {
            String host = getParameter(HOST);
            Integer port = getParameterAsInteger(PORT);
            if (host != null && port != null) {
                _Socket = new Socket(host, port);
            } else {
                _Socket = null;
            }
        }
    }

}
