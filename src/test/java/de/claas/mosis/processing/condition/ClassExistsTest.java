package de.claas.mosis.processing.condition;

import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Condition.ClassExists;
import de.claas.mosis.model.Processor;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit test for class {@link de.claas.mosis.model.Condition.ClassExists}.
 * It is intended to collect and document a set of test cases for the tested
 * class. Please refer to the individual tests for more detailed information.
 * <p>
 * Additional test cases can be found in {@link de.claas.mosis.model.ConditionTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class ClassExistsTest {

    /**
     * Returns an instantiated {@link de.claas.mosis.model.Condition.ClassExists}
     * class. If appropriate, the instance is configured with default values.
     *
     * @return an instantiated {@link de.claas.mosis.model.Condition.ClassExists}
     * class
     */
    private Condition build() {
        return new Condition.ClassExists();
    }

    @Test
    public void shouldExist() throws IOException {
        Condition c = build();
        assertTrue(c.complies(null, Processor.class.getName()));
        assertTrue(c.complies(null, ClassExists.class.getName()));
    }

    @Test
    public void shouldNotExist() {
        Condition c = build();
        assertFalse(c.complies(null, ""));
        assertFalse(c.complies(null, "class.does.not.exist"));
        assertFalse(c.complies(null, null));
    }

}
