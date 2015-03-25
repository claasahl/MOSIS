package de.claas.mosis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation {@link de.claas.mosis.annotation.Parameter}. It is intended to
 * simplify (and unify) the documentation of parameters from {@link
 * de.claas.mosis.model.Processor} classes. This information can then be
 * displayed at runtime or can also be utilized to generate a written
 * documentation of parameters for {@link de.claas.mosis.model.Processor}
 * classes.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Parameter {
    /**
     * Returns a description of the annotated parameter. A parameter will
     * typically be a public and static class field (e.g. <code>public static
     * final String XYZ</code>).
     *
     * @return a description of the annotated parameter
     */
    public String value();
}
