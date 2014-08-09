package de.claas.mosis.io;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.List;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Configurable;
import de.claas.mosis.model.Processor;
import de.claas.mosis.model.Relation;

/**
 * The class {@link UserDatagramProtocolHandler}. It is intended to enable
 * communication with external entities through a {@link DatagramSocket} (UDP
 * connection). This {@link DataHandler} provide an alternative to the otherwise
 * (mostly) stream-based communication. This implementation allows the use of
 * UDP connections, such that {@link Processor} modules can process
 * {@link DatagramPacket} objects.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
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
	Relation r = new ChangeHostAndPort();
	addRelation(r);
	r.compute(this, HOST, getParameter(HOST));
	r.compute(this, PORT, getParameter(PORT));
    }

    @Override
    public void dismantle() {
	super.dismantle();
	removeRelation(new ChangeHostAndPort());
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
		if (shouldFoward()) {
		    out.addAll(in);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * The class {@link ChangeHostAndPort}. It is intended to (re-)set the
     * {@link DatagramSocket} of the {@link UserDatagramProtocolHandler} object
     * whenever the {@link UserDatagramProtocolHandler#HOST} parameter or
     * {@link UserDatagramProtocolHandler#PORT} is changed.
     * 
     * @author Claas Ahlrichs (claasahl@tzi.de)
     * 
     */
    private class ChangeHostAndPort implements Relation {

	@Override
	public void compute(Configurable configurable, String parameter,
		String value) {
	    if (HOST.equals(parameter) || PORT.equals(parameter)) {
		try {
		    if (_Socket != null) {
			_Socket.close();
		    }

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
	}

    }

}
