package de.claas.mosis.annotation;

import de.claas.mosis.model.Processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation {@link Documentation}. It is intended to simplify (and unify)
 * the documentation of {@link Processor} classes. This information can then be
 * displayed at runtime or can also be utilized to generate a written
 * documentation of {@link Processor} classes.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Documentation {

    /**
     * Returns the annotated class's purpose. This is expected to return a
     * boiled down and concise description of the annotated class.
     *
     * @return the annotated class's purpose
     */
    public String purpose();

    /**
     * Returns a description of the annotated class. This description may be
     * lengthy and may use markdown (see
     * http://daringfireball.net/projects/markdown/) commands.
     *
     * @return a description of the annotated class
     */
    public String description();

    /**
     * Returns a set of arbitrary keywords related to the annotated class. These
     * keywords can later be used to search for {@link Processor} modules (e.g.
     * at runtime).
     *
     * @return a set of arbitrary keywords related to the annotated class
     */
    public String[] keywords() default {};

    /**
     * Returns the author(s) of the annotated class. The author's full name and
     * e-mail address will typically contained within this field.
     *
     * @return the author(s) of the annotated class
     */
    public String[] author();

    /**
     * Returns a reference to a publication on which the annotated class is
     * based. This typically contain a BibTeX entry or DOI to the referenced
     * document.
     *
     * @return a reference to a publication on which the annotated class is
     * based
     */
    public String reference() default "";

    /**
     * Returns the {@link Category} of the annotated class. This information can
     * later be used group related {@link Processor} classes (i.e. at runtime or
     * for written documentation).
     *
     * @return the {@link Category} of the annotated class
     */
    public Category category() default Category.Other;

    /**
     * Returns <code>true</code> if the annotated class can act as a data
     * source. Otherwise, <code>false</code> is returned. A {@link Processor},
     * that can act as data source, is characterized by the fact that it does
     * not necessarily require any input values in order to generate output
     * values. They will typically not have any preceding {@link Processor}
     * modules before them.
     *
     * @return <code>true</code>, if the annotated class can act as a data
     * source
     */
    public boolean dataSource() default false;

    /**
     * Returns <code>true</code> if the annotated class can act as a data sink.
     * Otherwise, <code>false</code> is returned. A {@link Processor}, that can
     * act as data sink, is characterized by the fact that it does not
     * necessarily generate output values. They will typically not have any
     * succeeding {@link Processor} modules after them.
     *
     * @return <code>true</code>, if the annotated class can act as a data sink
     */
    public boolean dataSink() default false;

    /**
     * Returns <code>true</code> if the annotated class does support multiple
     * input values. Otherwise, <code>false</code> is returned. Here <i>multiple
     * input values</i> refers to the fact that the input list of a
     * {@link Processor} (see
     * {@link Processor#process(java.util.List, java.util.List)} may contain
     * more than a single entry.
     *
     * @return <code>true</code>, if the annotated class does support multiple
     * input values
     */
    public boolean supportMultipleInputs() default false;

    /**
     * Returns reasons why the annotated class might not return output values.
     * These reasons are intended to state conditions (as opposed to normal
     * working condition) under which the annotated {@link Processor} does not
     * produce any output values.
     *
     * @return reasons why the annotated class might not return output values
     */
    public String[] noOutputData() default {};

    /**
     * Returns <code>true</code> if the annotated class can handle missing input
     * values. Otherwise, <code>false</code> is returned. Here <i>missing input
     * values</i> refers to the fact that some {@link Processor} modules may not
     * always produce output values or may not produce <code>null</code> values.
     * Succeeding modules will have to recover from (or handle) missing input
     * value one way or another.
     *
     * @return <code>true</code>, if the annotated class can handle missing
     * input values
     */
    public boolean canHandelMissingData() default false;

    /**
     * Returns the type of input values that the annotated class expects. When
     * linking {@link Processor} modules at runtime, this information can be
     * used to ensure compatibility among the linked modules. Thus not allowing
     * to directly link a {@link Processor} that produces {@link String} values
     * to a {@link Processor} that expects {@link Double} values.
     *
     * @return the type of input values that the annotated class expects.
     */
    public Class<?> inputData() default Object.class;

    /**
     * Returns the type of output values that the annotated class produces. When
     * linking {@link Processor} modules at runtime, this information can be
     * used to ensure compatibility among the linked modules. Thus not allowing
     * to directly link a {@link Processor} that produces {@link String} values
     * to a {@link Processor} that expects {@link Double} values.
     *
     * @return the type of output values that the annotated class produces.
     */
    public Class<?> outputData() default Object.class;
}
