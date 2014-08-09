package de.claas.mosis.processing.util;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.io.FileImpl;
import de.claas.mosis.io.StreamHandler;
import de.claas.mosis.model.Processor;

/**
 * The class {@link GenerateOverview}.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class GenerateOverview extends StreamHandler<String> implements
	Comparator<Class<?>> {

    // TODO Revise class
    private final Set<Class<?>> blacklist;
    private final List<Class<?>> classes;

    /**
     * Initializes the class with default values.
     */
    public GenerateOverview() {
	blacklist = new HashSet<Class<?>>();
	// blacklist.add(Processor.class);
	blacklist.add(GenerateOverview.class);
	blacklist.add(GenerateDocumentation.class);
	classes = new Vector<Class<?>>();
	setParameter(IMPL, FileImpl.class.getName());
	setParameter(FileImpl.FILE, "../../paper/chapter/modules-overview.tex");
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
	    // Print (sorted) overview of all modules
	    Collections.sort(classes, this);
	    PrintStream ps = new PrintStream(getOutputStream());
	    ps.println("\\begin{table}\n"
		    + "\t\\begin{tabular}{p{0.6\\textwidth}p{0.1\\textwidth}p{0.1\\textwidth}p{0.2\\textwidth}}\n"
		    + "\t\t\\hline\n"
		    + "\t\t\\textbf{Module} & \\textbf{Src} & \\textbf{Snk} & \\textbf{Page}\\\\");
	    for (Class<?> c : classes) {
		Documentation doc = c.getAnnotation(Documentation.class);
		if (doc != null)
		    ps.format("\t\t%s & %s & %s & \\pageref{mod:%s}\\\\\n",
			    c.getName(), doc.dataSource() ? "yes" : "no",
			    doc.dataSink() ? "yes" : "no", c.getName());
		else
		    ps.format("\t\t%s & - & - & \\pageref{mod:%s}\\\\\n",
			    c.getName(), c.getName());
	    }
	    ps.println("\t\t\\hline\n"
		    + "\t\\end{tabular}\n"
		    + "\t\\centering\n"
		    + "\t\\caption{Lists all modules in the framework and the page they are described on.}\n"
		    + "\t\\label{tab:modules}\n" + "\\end{table}");
	    ps.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    @Override
    public int compare(Class<?> o1, Class<?> o2) {
	return o1.getName().compareTo(o2.getName());
    }

}
