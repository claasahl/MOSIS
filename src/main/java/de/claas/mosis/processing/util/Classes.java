package de.claas.mosis.processing.util;

import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Configurable;
import de.claas.mosis.model.ProcessorAdapter;
import de.claas.mosis.model.Relation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * The class {@link de.claas.mosis.processing.util.Classes}. It is intended to
 * output all accessible classes within the class path. This information can be
 * used to enumerate all available modules (i.e. {@link
 * de.claas.mosis.model.Processor} implementations), automatically generate
 * human-readable documentation, etc.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        purpose = "It is intended to output all accessible classes within the class path.",
        description = "This module provides access to all accessible classes within the current class path. The returned list of classes has no particular order. The fully-qualified-classname is returned.",
        author = "Claas Ahlrichs",
        outputData = String.class)
public class Classes extends ProcessorAdapter<Object, String> {

    @Parameter("JAVA class path")
    public static final String CLASSPATH = "classpath";
    @Parameter("Separates class path entries")
    public static final String SEPARATOR = "separator";
    private final Set<String> packages;

    /**
     * Initializes the class with default values.
     */
    public Classes() {
        packages = new HashSet<>();
        addCondition(CLASSPATH, new Condition.IsNotNull());
        setParameter(CLASSPATH, System.getProperty("java.class.path"));
        addCondition(SEPARATOR, new Condition.IsNotNull());
        setParameter(SEPARATOR, System.getProperty("path.separator"));
        addRelation(new ClearPackages());
    }

    /**
     * Recursively enumerates available classes within a {@link
     * java.lang.Package}. The package can either be a regular directory or a
     * JAR file.
     *
     * @param classes     the enumerated classes
     * @param root        origin of currently enumerated {@link
     *                    java.lang.Package}
     * @param packageName name of currently enumerated {@link java.lang.Package}
     */
    private void addClasses(Set<String> classes, String root, String packageName) {
        String sep = "/";
        File file = new File(root
                + (packageName.isEmpty() ? "" : sep + packageName));
        if (file.isDirectory()) {
            if (file.listFiles() == null)
                return;

            // "root" is a regular directory
            for (File f : file.listFiles()) {
                if (isClassFile(f.getName())) {
                    String name = packageName + sep + f.getName();
                    name = name.substring(0, name.length() - 6);
                    name = name.replace(sep, ".");
                    classes.add(name);
                } else if (f.isDirectory()) {
                    String pckg;
                    if (packageName.isEmpty()) {
                        pckg = f.getName();
                    } else {
                        pckg = packageName + sep + f.getName();
                    }
                    addClasses(classes, root, pckg);
                }
            }
        } else if (file.exists()) {
            // "root" is likely a JAR file
            try {
                JarInputStream jar = new JarInputStream(new FileInputStream(
                        file));
                JarEntry entry;
                while ((entry = jar.getNextJarEntry()) != null) {
                    if (isClassFile(entry.getName())) {
                        String name = entry.getName();
                        name = name.substring(0, name.length() - 6);
                        name = name.replace(sep, ".");
                        classes.add(name);
                    }
                }
                jar.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns {@code true} if it is a class-file. Otherwise {@code false} is
     * returned.
     *
     * @param name the (presumably) class-file
     * @return {@code true} if it is a class-file
     */
    private boolean isClassFile(String name) {
        return name.toLowerCase().endsWith(".class");
    }

    @Override
    public void dismantle() {
        super.dismantle();
        packages.clear();
    }

    @Override
    public void process(List<Object> in, List<String> out) {
        if (packages.isEmpty()) {
            String classpath = getParameter(CLASSPATH);
            String separator = getParameter(SEPARATOR);
            for (String cp : classpath.split(separator)) {
                addClasses(packages, cp, "");
            }
        }
        out.addAll(packages);
    }

    /**
     * The class {@link de.claas.mosis.processing.util.Classes.ClearPackages}.
     * It is intended to clear the already processed {@link java.lang.Package}s
     * whenever the {@link #CLASSPATH} or {@link #SEPARATOR} parameter is
     * changed. This forces a (re-)processing of all {@link
     * java.lang.Package}s.
     *
     * @author Claas Ahlrichs (claasahl@tzi.de)
     */
    private class ClearPackages implements Relation {

        @Override
        public void compute(Configurable configurable, String parameter,
                            String value) {
            if (CLASSPATH.equals(parameter) || SEPARATOR.equals(parameter)) {
                packages.clear();
            }
        }

    }

}
