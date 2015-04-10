package de.claas.mosis.processing.util;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.processing.ComparingProcessor;

import java.util.List;

/**
 * The class {@link de.claas.mosis.processing.util.Distance}. It is intended to
 * calculate the distance between two successive input samples. This {@link
 * de.claas.mosis.processing.ComparingProcessor} implementation will return the
 * difference of two successive input values. If there are not such input values
 * then <code>null</code> is returned.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        category = Category.Other,
        author = {"Claas Ahlrichs"},
        description = "This is a realization of the ComparingProcessor which determines the distance between two successive input data. The module can be configured to use any port (i.e. if multiple modules output their data into this module then any one of them can be used). By default the first module is used.",
        purpose = "To calculate the distance between two successive input values.")
public class Distance extends ComparingProcessor<Double, Double> {

    @Override
    public void process(List<Double> in, List<Double> out) {
        Double curr = in.get(getParameterAsInteger(PORT_TO_USE));
        Double prev = replace(curr);
        out.add(prev == null || curr == null ? null : curr - prev);
    }

}
