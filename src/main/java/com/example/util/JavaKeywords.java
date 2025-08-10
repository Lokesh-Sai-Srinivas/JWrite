package com.example.util;

import java.util.regex.Pattern;

/**
 * Enhanced utility class holding patterns for Java syntax highlighting.
 * Supports more precise VS Code-like syntax highlighting.
 */
public class JavaKeywords {

    // Java keywords
    private static final String[] KEYWORDS = new String[]{
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while",
            "true", "false", "null"
    };

    // Java types
    private static final String[] TYPES = new String[]{
            "String", "Integer", "Long", "Double", "Float", "Boolean",
            "Character", "Byte", "Short", "Object", "Exception", "RuntimeException",
            "List", "ArrayList", "LinkedList", "Set", "HashSet", "TreeSet",
            "Map", "HashMap", "TreeMap", "Queue", "PriorityQueue", "Deque",
            "ArrayDeque", "Stack", "Vector", "Collections", "Arrays", "Optional",
            "Stream", "Consumer", "Supplier", "Function", "Predicate", "BiFunction",
            "Runnable", "Callable", "Future", "CompletableFuture", "Thread", "Executor",
            "ExecutorService", "ScheduledExecutorService", "ThreadPoolExecutor"
    };

    // Java annotations
    private static final String[] ANNOTATIONS = new String[]{
            "@Override", "@Deprecated", "@SuppressWarnings", "@SafeVarargs",
            "@FunctionalInterface", "@Native", "@Target", "@Retention",
            "@Documented", "@Inherited", "@Repeatable", "@SpringBootApplication",
            "@Component", "@Service", "@Repository", "@Controller", "@Autowired",
            "@Value", "@Qualifier", "@Primary", "@Scope", "@Profile", "@Conditional",
            "@Bean", "@Configuration", "@Import", "@ComponentScan", "@EnableAutoConfiguration"
    };

    // Build patterns
    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String TYPE_PATTERN = "\\b(" + String.join("|", TYPES) + ")\\b";
    private static final String ANNOTATION_PATTERN = "(" + String.join("|", ANNOTATIONS) + ")";
    
    // Basic syntax patterns
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String CHAR_PATTERN = "'([^'\\\\]|\\\\.)'";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    private static final String NUMBER_PATTERN = "\\b\\d+(\\.\\d+)?([eE][+-]?\\d+)?[fFlL]?\\b";
    private static final String FUNCTION_PATTERN = "\\b\\w+(?=\\s*\\()";
    private static final String VARIABLE_PATTERN = "\\b[a-z][a-zA-Z0-9_]*\\b";
    private static final String CONSTANT_PATTERN = "\\b[A-Z][A-Z0-9_]*\\b";
    private static final String OPERATOR_PATTERN = "[+\\-*/%=<>!&|^~?:]|\\+\\+|\\-\\-|<=|>=|==|!=|&&|\\|\\||\\+=|\\-=|\\*=|/=|%=|&=|\\|=|\\^=|<<=|>>=|>>>=|->";

    // Main pattern for syntax highlighting
    public static final Pattern PATTERN = Pattern.compile(
            "(?<ANNOTATION>" + ANNOTATION_PATTERN + ")" +
            "|(?<KEYWORD>" + KEYWORD_PATTERN + ")" +
            "|(?<TYPE>" + TYPE_PATTERN + ")" +
            "|(?<FUNCTION>" + FUNCTION_PATTERN + ")" +
            "|(?<CONSTANT>" + CONSTANT_PATTERN + ")" +
            "|(?<VARIABLE>" + VARIABLE_PATTERN + ")" +
            "|(?<NUMBER>" + NUMBER_PATTERN + ")" +
            "|(?<STRING>" + STRING_PATTERN + ")" +
            "|(?<CHAR>" + CHAR_PATTERN + ")" +
            "|(?<COMMENT>" + COMMENT_PATTERN + ")" +
            "|(?<OPERATOR>" + OPERATOR_PATTERN + ")" +
            "|(?<PAREN>" + PAREN_PATTERN + ")" +
            "|(?<BRACE>" + BRACE_PATTERN + ")" +
            "|(?<BRACKET>" + BRACKET_PATTERN + ")" +
            "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")",
            Pattern.CASE_INSENSITIVE
    );
}
