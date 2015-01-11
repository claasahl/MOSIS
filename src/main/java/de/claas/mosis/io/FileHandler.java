package de.claas.mosis.io;

import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * The class {@link de.claas.mosis.io.FileHandler}. It is intended to enable
 * enumeration of file system objects (such aus files and directories). This
 * {@link de.claas.mosis.io.DataHandler} provides an alternative to the
 * otherwise (mostly) stream-based communication.
 * <p/>
 * As opposed to other {@link de.claas.mosis.io.DataHandler} implementation,
 * this realization is read-only.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class FileHandler extends DataHandler<File> {

    @Parameter("The root directory which contents are to be returned (recursively).")
    public static final String ROOT = "root";
    private Stack<File> files;

    /**
     * Initializes the class with default values.
     */
    public FileHandler() {
        addCondition(ROOT, new Condition.IsNotNull());
        setParameter(ROOT, File.listRoots()[0].getAbsolutePath());
        addCondition(MODE, new Condition.WriteOnce());
        setParameter(MODE, MODE_READ);
    }

    @Override
    public void setUp() {
        super.setUp();
        files = new Stack<>();
        files.add(new File(getParameter(ROOT)));
    }

    @Override
    public void dismantle() {
        super.dismantle();
        files = null;
    }

    @Override
    public void process(List<File> in, List<File> out) {
        if (!files.isEmpty()) {
            while (!files.peek().exists()) {
                files.pop();
            }
            File exists = files.pop();
            out.add(exists);

            File[] list = exists.listFiles();
            if (list != null) {
                files.addAll(Arrays.asList(list));
            }
        }
    }

}
