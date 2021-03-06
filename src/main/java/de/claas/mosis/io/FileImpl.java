package de.claas.mosis.io;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;

import java.io.*;

/**
 * The class {@link de.claas.mosis.io.FileImpl}. It is intended to provide
 * access to files, such that {@link de.claas.mosis.io.StreamHandler}
 * implementations can process them.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class FileImpl extends StreamHandlerImpl {

    @Parameter("The file that is to be accessed / processed.")
    public static final String FILE = "filename";
    public static final String APPEND = "append to file";

    /**
     * Initializes the class with default values.
     */
    public FileImpl() {
        addCondition(FILE, new Condition.IsNotNull());
        setParameter(FILE, "values.ser");
        addCondition(APPEND, new Condition.IsBoolean());
        setParameter(APPEND, false);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(getParameter(FILE));
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new FileOutputStream(getParameter(FILE),
                getParameterAsBoolean(APPEND));
    }

}
