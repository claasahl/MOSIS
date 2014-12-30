package de.claas.mosis.io.format;

import de.claas.mosis.io.FileImpl;
import de.claas.mosis.io.StreamHandler;
import de.claas.mosis.io.StreamHandlerImpl;
import de.claas.mosis.io.UrlImpl;
import de.claas.mosis.model.Processor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * The class {@link Serialization}. It is intended to (de-)serialize objects.
 * This {@link StreamHandler} allows to read and write serialized objects from
 * any of the {@link StreamHandlerImpl} implementations (e.g. {@link FileImpl}
 * or {@link UrlImpl}).
 *
 * @param <T> type of (incoming and outgoing) data. See {@link Processor} for
 *            details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class Serialization<T> extends StreamHandler<T> {

    private ObjectInputStream _Input;
    private ObjectOutputStream _Output;

    @Override
    protected ObjectInputStream getInputStream() throws IOException {
        if (_Input == null) {
            _Input = new ObjectInputStream(super.getInputStream());
        }
        return _Input;
    }

    @Override
    protected ObjectOutputStream getOutputStream() throws IOException {
        if (_Output == null) {
            _Output = new ObjectOutputStream(super.getOutputStream());
        }
        return _Output;
    }

    @Override
    public void dismantle() {
        super.dismantle();
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

    @SuppressWarnings("unchecked")
    @Override
    public void process(List<T> in, List<T> out) {
        try {
            if (isReadOnly(in)) {
                T obj = (T) getInputStream().readObject();
                out.add(obj);
            } else {
                for (T obj : in) {
                    getOutputStream().writeObject(obj);
                    getOutputStream().flush();
                }
                if (shouldFoward()) {
                    out.addAll(in);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
