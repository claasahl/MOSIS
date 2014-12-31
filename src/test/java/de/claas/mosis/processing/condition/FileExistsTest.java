package de.claas.mosis.processing.condition;

import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Condition.FileExists;
import de.claas.mosis.model.ConditionTest;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit test for class {@link FileExists}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link ConditionTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class FileExistsTest {

    /**
     * Returns an instantiated {@link FileExists} class. If appropriate, the
     * instance is configured with default values.
     *
     * @return an instantiated {@link FileExists} class
     */
    private Condition build() {
        return new Condition.FileExists();
    }

    @Test
    public void shouldExist() throws IOException {
        Condition c = build();
        File tempFile = File.createTempFile(getClass().getName(), "csv");
        assertTrue(c.complies(null, tempFile.getAbsolutePath()));
    }

    @Test
    public void shouldNotExist() {
        Condition c = build();
        assertFalse(c.complies(null, ""));
        assertFalse(c.complies(null, "hallo.txt"));
        assertFalse(c.complies(null, null));
    }

}
