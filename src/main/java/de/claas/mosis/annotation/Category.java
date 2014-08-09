package de.claas.mosis.annotation;

import de.claas.mosis.model.Processor;

/**
 * The enumeration {@link Category}. It is intended to provide a rather general
 * list of categories to which {@link Processor} classes may belong. This
 * information can later be used to group related {@link Processor} modules at
 * runtime and in written documentation.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public enum Category {
    InputOutput, DataFormat, Classification, Clustering, Mining, Other;
}
