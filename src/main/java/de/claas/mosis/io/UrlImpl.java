package de.claas.mosis.io;

import de.claas.mosis.model.Condition;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * The class {@link UrlImpl}. It is intended to provide access to {@link URL}s,
 * such that {@link StreamHandler} implementations can process them.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class UrlImpl extends StreamHandlerImpl {

    public static final String URL = "url";
    private URLConnection _ConnIn;
    private URLConnection _ConnOut;

    /**
     * Initializes the class with default values.
     */
    public UrlImpl() {
        setParameter(URL, "");
        addCondition(URL, new Condition.IsNotNull());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (_ConnIn == null) {
            URL url = new URL(getParameter(URL));
            _ConnOut = url.openConnection();
            return _ConnOut.getInputStream();
        } else {
            URLConnection con = _ConnIn;
            _ConnIn = null;
            return con.getInputStream();
        }
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (_ConnOut == null) {
            URL url = new URL(getParameter(URL));
            _ConnIn = url.openConnection();
            _ConnIn.setDoOutput(true);
            return _ConnIn.getOutputStream();
        } else {
            URLConnection con = _ConnOut;
            con.setDoOutput(true);
            _ConnOut = null;
            return con.getOutputStream();
        }
    }

}
