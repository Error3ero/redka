package expression.exceptions;

import expression.*;
import expression.parser.CharSource;
import expression.parser.StringSource;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ExpressionParser implements TripleParser, ListParser {
    private String[] tokens;
    private int pos;

    @Override
    public TripleExpression parse(String expression) {
//        System.err.println(expression);
        this.tokens = new TokenParser(expression).parseTokens();
        this.pos = 0;
//        System.err.println(Arrays.toString(tokens));
        return (TripleExpression) this.getExpression(0);
    }

    @Override
    public ListExpression parse(String expression, List<String> variables) {
//        System.err.println(expression);
        this.tokens = new TokenParser(expression, variables).parseTokens();
        this.pos = 0;
        return (ListExpression) this.getExpression(0);
    }

    public ExpressionParser() {

    }

    private Expression getExpression(int priority) {
        Expression first = priority < 2 ? getExpression(priority + 1) : bitwise();
        while (pos < tokens.length) {
            String operator = tokens[pos];
            if (!switch (priority) {
                case 0 -> operator.equals("|");
                case 1 -> operator.equals("^");
                case 2 -> operator.equals("&");
                default -> throw error("Unexpected bitwise operation priority: " + priority);
            }) {
                break;
            } else {
                pos++;
            }
            Expression second = priority < 2 ? getExpression(priority + 1) : bitwise();
            first = switch (mapBinary(operator)) {
                case XOR -> new Xor(first, second);
                case OR -> new Or(first, second);
                case AND -> new And(first, second);
                default -> throw error("Not an bitwise operator");
            };
        }
        return first;
    }

    private Expression bitwise() {
        Expression first = term();
        while (pos < tokens.length) {
            String operator = tokens[pos];
            if (!(operator.equals("+") || operator.equals("-"))) {
                break;
            } else {
                pos++;
            }
            Expression second = term();
            first = switch (mapBinary(operator)) {
                case ADD -> new CheckedAdd(first, second);
                case SUBTRACT -> new CheckedSubtract(first, second);
                default -> throw error("Not an addition operator");
            };
        }
        return first;
    }

    private Expression term() {
        Expression first = factor();
        while (pos < tokens.length) {
            String operator = tokens[pos];
            if (!(operator.equals("*") || operator.equals("/"))) {
                break;
            } else {
                pos++;
            }
            Expression second = factor();
            first = switch (mapBinary(operator)) {
                case MULTIPLY -> new CheckedMultiply(first, second);
                case DIVIDE -> new CheckedDivide(first, second);
                default -> throw error("Not an multiplication operator");
            };
        }
        return first;
    }

    private Expression factor() {
        String operator = tokens[pos];
        if (operator.equals("l") || operator.equals("p") || operator.equals("_") || operator.equals("~")) {
            pos++;
            if (operator.equals("_") && isInteger(tokens[pos], true)) { // isInteger calls twice
                return unary(true);
            }
            return switch (mapUnary(operator)) {
                case POW2 -> new CheckedPow2(factor());
                case LOG2 -> new CheckedLog2(factor());
                case NOT -> new Not(factor());
                case NEGATE -> new CheckedNegate(factor());
                default -> throw error("Not an unary operator");
            };
        }
        return unary(false);
    }

    private Expression unary(boolean isNegative) {
        String token = tokens[pos];
        Expression expression;
        pos++;
        if (token.equals("(")) {
            expression = getExpression(0);
            pos++;
            return expression;
        }
        if (isInteger(token, isNegative)) {
            expression = new Const(Integer.parseInt(isNegative ? "-" + token : token));
        } else if (isVariable(token)) {
            expression = isNegative ? new Negate(getVariable(token)) : getVariable(token);
        } else if (token.equals(")")) {
//            if (tokens[pos - 2].equals("(")) {
//                throw error("empty in-brackets expression");
//            } else {
            throw error("No last argument'");
//            }
        } else {
            if (isBinary(token)) {
                throw error("No first argument'");
            }
            throw error("Unexpected token: " + token);
        }
        return expression;
    }

    private boolean isInteger(String s, boolean isNegative) {
        if (isNegative && s.equals("2147483648")) {
            return true;
        }
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            if (isNegative) {
                throw error("Constant overflow 1");
            }
            throw error("Constant overflow 2");
        }
        return true;
    }

    private boolean isVariable(String s) {
        return s.equals("x") || s.equals("y") || s.equals("z") || s.charAt(0) == '$';
    }

    private Variable getVariable(String s) {
        if (s.equals("x") || s.equals("y") || s.equals("z")) {
            return new Variable(s);
        }
        return new Variable(Integer.parseInt(s.substring(1)));
    }

    private boolean isBinary(String op) {
        try {
            OperatorType operator = mapBinary(op);
            return operator == OperatorType.ADD
                    || operator == OperatorType.SUBTRACT
                    || operator == OperatorType.MULTIPLY
                    || operator == OperatorType.DIVIDE
                    || operator == OperatorType.XOR
                    || operator == OperatorType.OR
                    || operator == OperatorType.AND;
        } catch (IllegalStateException e) {
            if (e.getMessage().equals("Not a binary operator")) {
                return false;
            }
            throw e;
        }
    }

    private OperatorType mapBinary(String o) {
        return switch (o) {
            case "+" -> OperatorType.ADD;
            case "-" -> OperatorType.SUBTRACT;
            case "*" -> OperatorType.MULTIPLY;
            case "/" -> OperatorType.DIVIDE;
            case "^" -> OperatorType.XOR;
            case "|" -> OperatorType.OR;
            case "&" -> OperatorType.AND;
            default -> throw new IllegalStateException("Not a binary operator");
        };
    }

    private OperatorType mapUnary(String o) {
        return switch (o) {
            case "_" -> OperatorType.NEGATE;
            case "~" -> OperatorType.NOT;
            case "l" -> OperatorType.LOG2;
            case "p" -> OperatorType.POW2;
            default -> throw new IllegalStateException("Not an unary operator");
        };
    }

    private IllegalArgumentException error(String message) {
        return new IllegalArgumentException(message);
    }

    private static class TokenParser {
        private final CharSource source;
        private final HashMap<Character, Integer> var;
        private final boolean listExpressions;

        protected TokenParser(String string) {
            this.source = new StringSource(string);
            this.var = null;
            this.listExpressions = false;
        }

        protected TokenParser(String string, List<String> variables) {
            this.source = new StringSource(string);
            this.var = new HashMap<>();
            for (int i = 0; i < variables.size(); i++) {
                var.put(variables.get(i).charAt(0), i);
            }
            this.listExpressions = true;
        }

        protected String[] parseTokens() {
            LinkedList<String> tokens = new LinkedList<>();
            TokenType last = TokenType.START;
            Stack<Character> brackets = new Stack<>();
            StringBuilder stock = new StringBuilder();
            boolean powLogParam = false;
            while (source.hasNext()) {
                char c = source.next();
                switch (c) {
                    case '-': {
                        if (powLogParam) {
                            throw error("No space between PowLog argument");
                        }
                        if (tokens.isEmpty() && !source.hasNext() && stock.isEmpty()) {
                            throw error("Bare " + c);
                        }
                        if (last == TokenType.DIGIT) {
                            put(tokens, stock);
                        }
                        if (last == TokenType.DIGIT || last == TokenType.SPACE ||
                                last == TokenType.VARIABLE || last == TokenType.CLOSING) {
                            put(tokens, '-');
                            last = TokenType.BINARY;
                            break;
                        }
                        put(tokens, '_');
                        last = TokenType.NEGATE;
                        break;
                    }
                    case '~':
                        if (powLogParam) {
                            throw error("No space between PowLog argument");
                        }
                        if (last == TokenType.DIGIT || last == TokenType.SPACE ||
                                last == TokenType.VARIABLE || last == TokenType.CLOSING) {
                            throw error("No binary operator");
                        }
                        if (last == TokenType.NOT) {
                            tokens.removeLast();
                            last = TokenType.UNARY;
                            break;
                        }
                        put(tokens, "~");
                        last = TokenType.NOT;
                        break;
                    case 'l', 'p':
                        if (powLogParam) {
                            throw error("No space between PowLog argument");
                        }
                        if (last == TokenType.DIGIT || last == TokenType.SPACE ||
                                last == TokenType.VARIABLE || last == TokenType.CLOSING) {
                            throw error("No operator");
                        }
                        checkUnary(c);
                        put(tokens, c);
                        last = mapType(c);
                        powLogParam = true;
                        break;
                    case '+', '/', '*', '&', '^', '|':
                        if (powLogParam) {
                            throw error("No space between PowLog argument");
                        }
                        if (tokens.isEmpty() && !source.hasNext() && stock.isEmpty()) {
                            throw error("Bare " + c);
                        }
                        if (last == TokenType.START) {
                            throw error("No first argument");
                        }
                        if (last == TokenType.BINARY) {
                            throw error("No middle argument");
                        }
                        if (last == TokenType.DIGIT) {
                            put(tokens, stock);
                        }
                        put(tokens, c);
                        last = TokenType.BINARY;
                        break;
                    case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
                        if (powLogParam) {
                            throw error("No space between PowLog argument");
                        }
                        if (last == TokenType.SPACE || last == TokenType.VARIABLE || last == TokenType.CLOSING) {
                            throw error("No operator");
                        }
                        last = TokenType.DIGIT;
                        stock.append(c);
                        break;
                    case 'x', 'y', 'z':
                        if (powLogParam) {
                            throw error("No space between PowLog argument");
                        }
                        if (last == TokenType.DIGIT || last == TokenType.SPACE ||
                                last == TokenType.VARIABLE || last == TokenType.CLOSING) {
                            throw error("No operator");
                        }
                        put(tokens, c);
                        last = TokenType.VARIABLE;
                        break;
                    case '$':
                        if (!listExpressions) {
                            throw error("Unexpected char: $");
                        }
                        if (powLogParam) {
                            throw error("No space between PowLog argument");
                        }
                        if (last == TokenType.DIGIT || last == TokenType.SPACE ||
                                last == TokenType.VARIABLE || last == TokenType.CLOSING) {
                            throw error("No operator");
                        }
                        put(tokens, readVar());
                        last = TokenType.VARIABLE;
                        break;
                    case '(', '[', '{':

                        if (last == TokenType.DIGIT || last == TokenType.SPACE || last == TokenType.VARIABLE) {
                            throw error("No operator");
                        }
                        if (powLogParam) {
                            powLogParam = false;
                        }
                        put(tokens, '(');
                        last = TokenType.OPENING;
                        brackets.add(c);
                        break;
                    case ')', '}', ']':
                        if (powLogParam) {
                            throw error("No space between PowLog argument");
                        }
                        if (last == TokenType.OPENING) {
                            if (tokens.getLast().equals(openingParenthesis(c))) {
                                throw error("empty in-brackets expression");
                            } else {
                                throw error("incorrect brackets sequence: " + tokens.getLast() + c);
                            }
                        }
                        if (last == TokenType.DIGIT) {
                            put(tokens, stock);
                        }
                        put(tokens, ')');
                        last = TokenType.CLOSING;
                        closeBrackets(brackets, c);
                        break;
                    default:
                        if (!Character.isSpaceChar(c) && Character.getType(c) != 15) {
                            if (c == '@') {
                                if (source.getPos() == 1) {
                                    throw error("Start symbol: " + c);
                                } else if (source.getPos() == source.size()) {
                                    throw error("End symbol: " + c);
                                }
                                throw error("Middle symbol: " + c);
                            }
                            if (tokens.isEmpty() && !source.hasNext() && stock.isEmpty()) {
                                throw error("Bare " + c);
                            }
                            throw error("Unexpected char: " + c);
                        } else if (last == TokenType.DIGIT) {
                            put(tokens, stock);
                            last = TokenType.SPACE;
                        } else if (powLogParam) {
                            powLogParam = false;
                        }
                }
            }
            if (last == TokenType.DIGIT) {
                put(tokens, stock);
                last = TokenType.END;
            }
            if (last == TokenType.VARIABLE || last == TokenType.CLOSING) {
                last = TokenType.END;
            }
            if (!brackets.isEmpty()) {
                throw error("No closing parenthesis");
            }
            if (last != TokenType.END) {
                throw error("No last argument");
            }
            return toArray(tokens);
        }

        private void checkUnary(char o) {
            StringBuilder operator = new StringBuilder();
            operator.append(o);
            for (int i = 0; i < 3; i++) {
                operator.append(source.next());
            }
            switch (o) {
                case 'l':
                    if (!operator.toString().equals("log2")) {
                        throw new IllegalStateException("Unknown operator: " + operator);
                    }
                    break;
                case 'p':
                    if (!operator.toString().equals("pow2")) {
                        throw new IllegalStateException("Unknown operator: " + operator);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected unary check: " + o);
            }
        }

        private String readVar() {
            StringBuilder var = new StringBuilder();
            loop: do {
                char c = source.checkNext();
                switch (c) {
                    case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9':
                        var.append(c);
                        source.next();
                        break;
                    default:
                        break loop;
                }
            } while (source.hasNext());
            if (var.isEmpty()) {
                throw error("no variable number");
            }
            return "$" + var;
        }

        private TokenType mapType(char c) {
            return switch (c) {
                case 'l' -> TokenType.LOG;
                case 'p' -> TokenType.POW;
                default -> throw new IllegalStateException("Unexpected char to type mapping: " + c);
            };
        }

        private void put(LinkedList<String> list, StringBuilder sb) {
            list.add(sb.toString());
            clear(sb);
        }

        private void put(LinkedList<String> list, String str) {
            list.add(str);
        }

        private void put(LinkedList<String> list, char c) {
            put(list, Character.toString(c));
        }

        private void clear(StringBuilder sb) {
            sb.delete(0, sb.length());
        }

        private String[] toArray(LinkedList<String> list) {
            String[] arr = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                arr[i] = list.get(i);
            }
            return arr;
        }

        private String openingParenthesis(char c) {
            return switch (c) {
                case ')' -> "(";
                case '}' -> "{";
                case ']' -> "[";
                default -> throw new IllegalArgumentException("not a closing parenthesis: " + c);
            };
        }

        private void closeBrackets(Stack<Character> brackets, char c) {
            if (brackets.isEmpty()) {
                throw error("No opening parenthesis: '" + c + "'");
            }
            switch (c) {
                case ')':
                    if (brackets.pop() != '(') {
                        throw error("Last opened parenthesis is not: '('");
                    }
                    break;
                case ']':
                    if (brackets.pop() != '[') {
                        throw error("Last opened parenthesis is not: '['");
                    }
                    break;
                case '}':
                    if (brackets.pop() != '{') {
                        throw error("Last opened parenthesis is not: '{'");
                    }
                    break;
                default:
                    throw new IllegalStateException("not a bracket: " + c);
            }
        }

        private IllegalArgumentException error(String message) {
            return new IllegalArgumentException(message);
        }

        enum TokenType {
            START, OPENING, CLOSING, BINARY, UNARY, NEGATE, NOT, LOG, POW, DIGIT, VARIABLE, END, SPACE
        }
    }

    enum OperatorType {
        ADD, SUBTRACT, MULTIPLY, DIVIDE, NEGATE, NOT, AND, OR, XOR, LOG2, POW2
    }

}