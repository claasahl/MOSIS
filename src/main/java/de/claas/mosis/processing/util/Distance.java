package de.claas.mosis.processing.util;

import java.util.List;

import de.claas.mosis.processing.ComparingProcessor;

/**
 * The class {@link Distance}. It is intended to calculate the distance between
 * two successive input samples. This {@link ComparingProcessor} implementation
 * will return the difference of two successive input values. If there are not
 * such input values then <code>null</code> is returned.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class Distance extends ComparingProcessor<Double, Double> {

    @Override
    public void process(List<Double> in, List<Double> out) {
	Double curr = in.get(getParameterAsInteger(PORT_TO_USE));
	Double prev = replace(curr);
	out.add(prev == null || curr == null ? null : curr - prev);
    }

}
