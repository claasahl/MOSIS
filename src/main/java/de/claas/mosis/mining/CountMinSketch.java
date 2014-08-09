package de.claas.mosis.mining;

import java.util.List;
import java.util.Random;

import de.claas.mosis.model.ProcessorAdapter;

// TODO Implement
// TODO Test
// TODO Document
public class CountMinSketch extends ProcessorAdapter<Object, Object> {

    private final int depth;
    private final int width;
    private final double confidence;
    private final double epsilon;
    private int size;
    private int[] hash;
    private int[][] count;

    public CountMinSketch(int depth, int width, long seed) {
	this.depth = depth;
	this.width = width;
	this.confidence = 1.0 - 1.0 / Math.pow(2.0, depth);
	this.epsilon = 2.0 / (double) width;
	initTable(seed);
    }

    public CountMinSketch(double eps, double confidence, long seed) {
	this.confidence = confidence;
	this.epsilon = eps;
	this.depth = (int) Math.ceil(-Math.log(1 - confidence) / Math.log(2));
	this.width = (int) Math.ceil(2.0 / eps);
	initTable(seed);
    }

    private void initTable(long seed) {
	this.hash = new int[depth];
	this.count = new int[depth][width];
	Random r = new Random(seed);
	for (int d = 0; d < depth; d++)
	    hash[d] = r.nextInt();
    }

    public double getConfidence() {
	return confidence;
    }

    public double getRelativeError() {
	return epsilon;
    }

    private int hash(int item, int d) {
	int hash = this.hash[d] * item;
	hash += hash >> 32;
	hash &= (1L << 31) - 1;
	return hash % width;
    }

    public void add(int item, int count) {
	if (count < 0)
	    throw new IllegalArgumentException();
	for (int d = 0; d < depth; d++)
	    this.count[d][hash(item, d)] += count;
	size += count;
    }

    public int getCount(int item) {
	int count = Integer.MAX_VALUE;
	for (int d = 0; d < depth; d++)
	    count = Math.min(this.count[d][hash(item, d)], count);
	return count;
    }

    @Override
    public void process(List<Object> in, List<Object> out) {
	// TODO Auto-generated method stub

    }

}
