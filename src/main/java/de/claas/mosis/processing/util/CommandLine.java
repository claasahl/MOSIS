package de.claas.mosis.processing.util;

import java.util.List;

import de.claas.mosis.model.ProcessorAdapter;

public class CommandLine<I, O> extends ProcessorAdapter<I, O> {

    public static final String COMMAND = "(shell) command";

    public CommandLine() {

    }

    @Override
    public void process(List<I> in, List<O> out) {
	// TODO Auto-generated method stub

    }

}
