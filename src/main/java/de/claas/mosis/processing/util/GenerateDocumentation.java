package de.claas.mosis.processing.util;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.io.FileImpl;
import de.claas.mosis.io.StreamHandler;
import de.claas.mosis.model.Processor;

/**
 * The class {@link GenerateDocumentation}.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class GenerateDocumentation extends StreamHandler<String> {

    // TODO Revise class
    private final Set<Class<?>> blacklist;
    private final List<Class<?>> classes;

    /**
     * Initializes the class with default values.
     */
    public GenerateDocumentation() {
	blacklist = new HashSet<Class<?>>();
	// blacklist.add(Processor.class);
	blacklist.add(GenerateOverview.class);
	blacklist.add(GenerateDocumentation.class);
	classes = new Vector<Class<?>>();
	setParameter(IMPL, FileImpl.class.getName());
	setParameter(FileImpl.FILE,
		"../../paper/chapter/modules-documentation.tex");
	setParameter(FileImpl.APPEND, false);
    }

    @Override
    public void process(List<String> in, List<String> out) {
	// Build list of available modules
	for (String classname : in) {
	    try {
		Class<?> clazz = Class.forName(classname);
		if (Processor.class.isAssignableFrom(clazz)
			&& !blacklist.contains(clazz)) {
		    classes.add(clazz);
		}
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
	    }
	}

	try {
	    // Print overview of all modules
	    PrintStream ps = new PrintStream(getOutputStream());
	    for (Category cat : Category.values()) {
		ps.format("\n\n\\section{%s Modules}\n", cat.name());
		for (Class<?> clazz : classes) {
		    Documentation doc = clazz
			    .getAnnotation(Documentation.class);
		    if (doc == null && Category.Other.equals(cat)
			    || doc != null && doc.category().equals(cat)) {
			printDocumentation(ps, clazz, doc);
		    }
		}
	    }
	    ps.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void printDocumentation(PrintStream ps, Class<?> clazz,
	    Documentation doc) {
	if (doc == null) {
	    return;
	}
	String authors = Arrays.toString(doc.author());
	authors = authors.substring(1, authors.length() - 1);
	String format = "\n\n\\label{mod:%s}\n"
		+ "\\begin{tabular}{rrp{0.2\\textwidth}rl}\n"
		+ "\t\\multicolumn{5}{c}{\\cellcolor{blue!25}\\textbf{\\LARGE{Module:}}} \\T\\B\\\\\n"
		+ "\t\\multicolumn{5}{c}{\\cellcolor{blue!25}\\textbf{\\LARGE{%s}}} \\T\\B\\\\\n"
		+ "\t\\textbf{Author(s):} & \\multicolumn{4}{p{0.8\\textwidth}}{%s}\\T\\B\\\\\n"
		+ "\t\\textbf{General:} & Is data source: & %s & Supports multiple inputs: & %s \\\\\n"
		+ "\t\t& Is data sink: & %s & Can handle missing data: & %s\\\\\n"
		+ "\t\t& Input data: & \\multicolumn{3}{l}{%s}\\\\\n"
		+ "\t\t& Output data: & \\multicolumn{3}{l}{%s}\\\\\n"
		+ "\t\\textbf{Purpose:} & \\multicolumn{4}{p{0.8\\textwidth}}{%s}\\T\\\\\n";
	ps.format(format, clazz.getName(), clazz.getName(), authors, doc
		.dataSource() ? "yes" : "no",
		doc.supportMultipleInputs() ? "yes" : "no",
		doc.dataSink() ? "yes" : "no",
		doc.canHandelMissingData() ? "yes" : "no", doc.inputData()
			.getName(), doc.outputData().getName(), doc.purpose());

	Map<String, Parameter> parameters = new HashMap<String, Parameter>();
	for (Field field : clazz.getFields()) {
	    Parameter parameter = field.getAnnotation(Parameter.class);
	    if (parameter != null) {
		parameters.put(field.getName(), parameter);
	    }
	}
	if (parameters.isEmpty()) {
	    ps.println("\t\\textbf{Parameter(s)}: &"
		    + " \\multicolumn{4}{p{0.8\\textwidth}}{\\textit{none}}\\T\\B\\\\");
	} else {
	    ps.println("\t\\textbf{Parameter(s)}: & \\multicolumn{4}{p{0.8\\textwidth}}{\n"
		    + "\t\t\\begin{itemize}");
	    for (String field : parameters.keySet()) {
		ps.format("\t\t\t\\awesomeitem{%s:} %s\n", field.replace("_",
			"\\_"), parameters.get(field).value());
	    }
	    ps.println("\t\t\\end{itemize}\n" + "\t\t}\\T\\B\\\\");
	}

	if (doc.noOutputData().length == 0) {
	    ps.println("\t\\textbf{Output Data}: &"
		    + " \\multicolumn{4}{p{0.8\\textwidth}}{Always generates output data.}\\T\\B\\\\");
	} else {
	    ps.println("\t\\textbf{Output Data}: & \\multicolumn{4}{p{0.8\\textwidth}}{\n"
		    + "\t\t\\begin{itemize}");
	    for (String reason : doc.noOutputData()) {
		ps.format("\t\t\t\\item %s\n", reason);
	    }
	    ps.println("\t\t\\end{itemize}\n" + "\t\t}\\T\\B\\\\");
	}

	format = "\t\\textbf{Description:} & \\multicolumn{4}{p{0.8\\textwidth}}{%s}\\\\\n"
		+ "\\end{tabular}\n";
	ps.format(format, doc.description());
    }
}
