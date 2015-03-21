package de.claas.mosis.io.format;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.io.StreamHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * The class {@link de.claas.mosis.io.format.Serialization}. It is intended to
 * (de-)serialize objects. This {@link de.claas.mosis.io.StreamHandler} allows
 * to read and write serialized objects from any of the {@link
 * de.claas.mosis.io.StreamHandlerImpl} implementations (e.g. {@link
 * de.claas.mosis.io.FileImpl} or {@link de.claas.mosis.io.UrlImpl}).
 *
 * @param <T> type of (incoming and outgoing) data. See {@link
 *            de.claas.mosis.model.Processor} for details.
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        category = Category.DataFormat,
        author = {"Claas Ahlrichs"},
        description = "This implementation represents a data format for serialization and deserialization of objects.",
        purpose = "To allow serialization and deserialization.")
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
                e.printStackTrace();
            } finally {
                _Input = null;
            }
        }
        if (_Output != null) {
            try {
                _Output.close();
            } catch (IOException e) {
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
                if (shouldForward()) {
                    out.addAll(in);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
