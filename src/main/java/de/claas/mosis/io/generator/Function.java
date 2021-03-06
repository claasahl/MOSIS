package de.claas.mosis.io.generator;

import de.claas.mosis.annotation.Category;
import de.claas.mosis.annotation.Documentation;
import de.claas.mosis.annotation.Parameter;
import de.claas.mosis.model.Condition;
import de.claas.mosis.model.ProcessorAdapter;

import java.util.*;

/**
 * The class {@link de.claas.mosis.io.generator.Function}. It is intended to
 * generate values of arbitrary arithmetic expressions. The idea is to provide a
 * text-based expression, which is then parsed and interpreted. This class is
 * mainly intended for debugging purposes.
 * <p>
 * Valid arithmetic expressions are of the form:</br>
 * <p>
 * <ol> <li>expression = term { ("+" | "-") term }.</li> <li>term = factor {
 * ("*" | "/") factor }.</li> <li>factor = value | ["cos" | "sin"] "("
 * expression ")" | factor "^" expression.</li> <li>value = number |
 * variable.</li> <li>number = ["+" | "-"] digit { digit } ["." digit { digit
 * }].</li> <li>digit = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" |
 * "9".</li> <li>variable = "x" { digit }.</li> </ol>
 * <p>
 * TODO There seems to be a difference between the grammar above and the impl.
 * <p>
 * The above grammar is given in EBNF (Extended Backus-Naur Form).
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
@Documentation(
        dataSource = true,
        category = Category.InputOutput,
        author = {"Claas Ahlrichs"},
        description = "This is a realization of a ProcessingAdapter and it returns a predefined sequence of numbers (similarly to Linear). This implementation returns one by default. However, the mathematical expression that is used to generate the sequence can be configured. The input data from one or multiple inbound modules are used as input values for the expression. This module is especially powerful when used in combination with Linear. Not just linear expressions, but also functions (e.g. 'sin', 'cos', etc.) as well as exponential expressions can be utilized.",
        purpose = "To provide access to a predefined sequence of numbers.")
public class Function extends ProcessorAdapter<Double, Double> {

    @Parameter("Mathematical function / expression used to generate output values.")
    public static final String FUNCTION = "function";
    private final Queue<String> _Token;
    private final Stack<Double> _Result;
    private final List<Double> _Variables;

    /**
     * Initializes the class with default values.
     */
    public Function() {
        _Token = new LinkedList<>();
        _Result = new Stack<>();
        _Variables = new Vector<>();

        addCondition(FUNCTION, new IsValidExpression());
        setParameter(FUNCTION, "1");
    }

    @Override
    public void dismantle() {
        super.dismantle();
        _Token.clear();
        _Result.clear();
        _Variables.clear();
    }

    @Override
    public void process(List<Double> in, List<Double> out) {
        String expression = getParameter(FUNCTION);
        in = in == null ? new Vector<Double>() : in;
        out.add(interpret(expression, in));
    }

    /**
     * Returns the result after interpreting a mathematical expression. Any
     * variables within the expression are replaced by actual values based on
     * the given expression and data values.
     *
     * @param expression the mathematical expression
     * @param data       the data for variables
     * @return the result after interpreting a mathematical expression
     */
    public double interpret(String expression, List<Double> data) {
        _Token.clear();
        _Token.addAll(tokenize(expression));
        _Result.clear();
        _Variables.clear();
        _Variables.addAll(data);
        expression();
        if (!_Token.isEmpty()) {
            // TODO What is this?
            throw new IllegalArgumentException("also my bad ...");
        }
        return _Result.pop();
    }

    /**
     * Returns the set of tokens that make up the given mathematical expression.
     * These tokens are interpreted by methods {@link #expression()}, {@link
     * #term()} and so on.
     *
     * @param expression the mathematical expression
     * @return the set of tokens that make up the given mathematical expression
     */
    private List<String> tokenize(String expression) {
        expression = expression.replaceAll("\\s", "");
        String regex = "[()*/+\\-x^0-9\\.]|cos|sin";
        Stack<String> token = new Stack<>();
        token.add(expression);
        while (token.peek().length() > 1) {
            String tmp = token.pop();
            if (tmp.substring(0, 1).matches(regex)) {
                token.add(tmp.substring(0, 1));
                token.add(tmp.substring(1));
            } else if (tmp.substring(0, 3).matches(regex)) {
                token.add(tmp.substring(0, 3));
                token.add(tmp.substring(3));
            } else {
                // TODO What is this?
                System.err.println("kdsjhkjsh");
            }
        }
        return token;
    }

    /**
     * Returns <code>true</code>, if the next token is among the given options.
     * Otherwise, <code>false</code> is returned.
     *
     * @param options the options
     * @return <code>true</code>, if the next token is contained within the
     * given options
     */
    private boolean nextTokenEquals(String... options) {
        String token = _Token.peek();
        for (String option : options) {
            if (token != null && token.equals(option)) {
                return true;
            }
        }
        return false;
    }

    private void expression() {
        term();
        while (nextTokenEquals("+", "-")) {
            String op = _Token.poll();
            term();
            double t2 = _Result.pop();
            double t1 = _Result.pop();
            if ("+".equals(op)) {
                _Result.add(t1 + t2);
            } else if ("-".equals(op)) {
                _Result.add(t1 - t2);
            }
        }
    }

    private void term() {
        factor();
        while (nextTokenEquals("*", "/")) {
            String op = _Token.poll();
            factor();
            double t2 = _Result.pop();
            double t1 = _Result.pop();
            if ("*".equals(op)) {
                _Result.add(t1 * t2);
            } else if ("/".equals(op)) {
                _Result.add(t1 / t2);
            }
        }
    }

    private void factor() {
        number();
        variable();
        if (nextTokenEquals("sin", "cos")) {
            String op = _Token.poll();
            if (!nextTokenEquals("(")) {
                throw new IllegalArgumentException("my bad....");
            }
            expression();
            if ("sin".equals(op)) {
                _Result.add(Math.sin(_Result.pop()));
            } else if ("cos".equals(op)) {
                _Result.add(Math.cos(_Result.pop()));
            }
        }
        if (nextTokenEquals("(")) {
            _Token.poll();
            expression();
            if (nextTokenEquals(")")) {
                _Token.poll();
            } else {
                // TODO What is this?
                throw new IllegalArgumentException("my bad....");
            }
        }
        if (nextTokenEquals("^")) {
            _Token.poll();
            expression();
            double t2 = _Result.pop();
            double t1 = _Result.pop();
            _Result.add(Math.pow(t1, t2));
        }
    }

    private void number() {
        if (nextTokenEquals("0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "+", "-")) {
            boolean isPositive = true;
            while (nextTokenEquals("+", "-")) {
                String sign = _Token.poll();
                if (sign.equals("-")) {
                    isPositive = !isPositive;
                }
            }

            StringBuilder sb = new StringBuilder();
            while (nextTokenEquals("0", "1", "2", "3", "4", "5", "6", "7", "8",
                    "9", ".")) {
                sb.append(_Token.poll());
            }
            double num = Double.parseDouble(sb.toString());
            _Result.add(isPositive ? num : -num);
        }
    }

    private void variable() {
        if (nextTokenEquals("x")) {
            _Token.poll();
            StringBuilder sb = new StringBuilder();
            while (nextTokenEquals("0", "1", "2", "3", "4", "5", "6", "7", "8",
                    "9")) {
                sb.append(_Token.poll());
            }
            if (_Variables.isEmpty()) {
                _Result.add(0.0);
            } else if (sb.toString().isEmpty()) {
                _Result.add(_Variables.get(0));
            } else {
                int num = Integer.parseInt(sb.toString());
                _Result.add(_Variables.get(num));
            }
        }
    }

    /**
     * The class {@link de.claas.mosis.io.generator.Function.IsValidExpression}.
     * It is intended to ensure the validity of mathematical expressions (as
     * defined in {@link de.claas.mosis.io.generator.Function}) whenever the
     * {@link #FUNCTION} parameter is changed.
     *
     * @author Claas Ahlrichs (claasahl@tzi.de)
     */
    private class IsValidExpression implements Condition {

        @Override
        public boolean complies(String parameter, String value) {
            try {
                interpret(value, new Vector<Double>());
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public String toString() {
            return "Parameter is not a valid mathematical expression.";
        }

    }

}
