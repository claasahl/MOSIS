package de.claas.mosis.annotation;

/**
 * The enumeration {@link de.claas.mosis.annotation.Category}. It is intended to
 * provide a rather general list of categories to which {@link
 * de.claas.mosis.model.Processor} classes may belong. This information can
 * later be used to group related {@link de.claas.mosis.model.Processor} modules
 * at runtime and in written documentation.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public enum Category {
    InputOutput, DataFormat, Classification, Clustering, Mining, Other
}
