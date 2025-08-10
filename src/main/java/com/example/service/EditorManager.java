package com.example.service;

import com.example.util.JavaKeywords;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Enhanced editor manager with VS Code-like features.
 * Manages the editor tabs, syntax highlighting, auto-completion, and file I/O.
 */
public class EditorManager {

    private final TabPane tabPane;
    private final Label statusLabel;
    private final Map<Tab, File> openFiles = new HashMap<>();

    public EditorManager(TabPane tabPane, Label statusLabel) {
        this.tabPane = tabPane;
        this.statusLabel = statusLabel;
    }

    public void openFileInEditor(File file) {
        // Check if file is already open
        for (Map.Entry<Tab, File> entry : openFiles.entrySet()) {
            if (entry.getValue().equals(file)) {
                tabPane.getSelectionModel().select(entry.getKey());
                return;
            }
        }

        try {
            String content = Files.readString(file.toPath());
            CodeArea codeArea = createEnhancedCodeArea(content);

            Tab tab = new Tab(file.getName());
            tab.setContent(new VirtualizedScrollPane<>(codeArea));
            tab.setClosable(true);
            tab.setOnCloseRequest(e -> {
                openFiles.remove(tab);
                statusLabel.setText("Closed: " + file.getName());
            });

            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            openFiles.put(tab, file);
            statusLabel.setText("Opened: " + file.getName());
        } catch (IOException e) {
            statusLabel.setText("Error opening file: " + e.getMessage());
        }
    }

    public void createNewFile() {
        CodeArea codeArea = createEnhancedCodeArea("");
        
        Tab tab = new Tab("Untitled");
        tab.setContent(new VirtualizedScrollPane<>(codeArea));
        tab.setClosable(true);
        tab.setOnCloseRequest(e -> {
            openFiles.remove(tab);
            statusLabel.setText("Closed: Untitled");
        });

        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        statusLabel.setText("Created new file");
    }

    public void saveCurrentFile() {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        File file = openFiles.get(currentTab);
        if (currentTab != null && file != null) {
            CodeArea codeArea = (CodeArea) ((VirtualizedScrollPane<?>) currentTab.getContent()).getContent();
            try {
                Files.writeString(file.toPath(), codeArea.getText());
                statusLabel.setText("Saved: " + file.getName());
            } catch (IOException e) {
                statusLabel.setText("Error saving file: " + e.getMessage());
            }
        }
    }

    public void saveCurrentFileAs(File file) {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        if (currentTab != null) {
            CodeArea codeArea = (CodeArea) ((VirtualizedScrollPane<?>) currentTab.getContent()).getContent();
            try {
                Files.writeString(file.toPath(), codeArea.getText());
                statusLabel.setText("Saved new file: " + file.getName());
                currentTab.setText(file.getName());
                openFiles.put(currentTab, file);
            } catch (IOException e) {
                statusLabel.setText("Error saving file: " + e.getMessage());
            }
        }
    }

    public File getCurrentFile() {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        return openFiles.get(currentTab);
    }

    public void closeCurrentTab() {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        if (currentTab != null) {
            tabPane.getTabs().remove(currentTab);
            openFiles.remove(currentTab);
            statusLabel.setText("Tab closed");
        }
    }

    public void closeAllTabs() {
        tabPane.getTabs().clear();
        openFiles.clear();
        statusLabel.setText("All tabs closed");
    }

    public void formatCurrentDocument() {
        Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
        if (currentTab != null) {
            CodeArea codeArea = (CodeArea) ((VirtualizedScrollPane<?>) currentTab.getContent()).getContent();
            String formatted = formatJavaCode(codeArea.getText());
            codeArea.replaceText(formatted);
        }
    }

    /**
     * Simple Java code formatter: fixes indentation and brace placement.
     */
    private String formatJavaCode(String code) {
        String[] lines = code.split("\\r?\\n");
        StringBuilder sb = new StringBuilder();
        int indent = 0;
        boolean inBlockComment = false;
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                sb.append("\n");
                continue;
            }
            if (trimmed.startsWith("/*")) inBlockComment = true;
            if (trimmed.endsWith("*/")) inBlockComment = false;
            if (!inBlockComment && (trimmed.startsWith("}") || trimmed.startsWith("]") || trimmed.startsWith(")"))) {
                indent = Math.max(0, indent - 1);
            }
            for (int i = 0; i < indent; i++) sb.append("    ");
            sb.append(trimmed).append("\n");
            if (!inBlockComment && (trimmed.endsWith("{") || trimmed.endsWith("[") || trimmed.endsWith("("))) {
                indent++;
            }
        }
        return sb.toString();
    }

    private CodeArea createEnhancedCodeArea(String initialContent) {
        CodeArea codeArea = new CodeArea();
        
        // Add line numbers
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        
        // Set up real-time syntax highlighting with debouncing
        codeArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(100))
                .subscribe(ignore -> codeArea.setStyleSpans(0, computeEnhancedHighlighting(codeArea.getText())));

        // Add VS Code-like auto-indentation only (no auto-brackets/parentheses)
        codeArea.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                handleEnterKey(codeArea, event);
            } else if (event.getCode() == javafx.scene.input.KeyCode.TAB) {
                handleTabKey(codeArea, event);
            }
        });

        // Set initial content
        codeArea.replaceText(0, 0, initialContent);
        
        // Apply initial syntax highlighting
        codeArea.setStyleSpans(0, computeEnhancedHighlighting(initialContent));
        
        return codeArea;
    }

    private void handleEnterKey(CodeArea codeArea, javafx.scene.input.KeyEvent event) {
        String currentLine = getCurrentLine(codeArea);
        String indentation = getIndentation(currentLine);
        
        // Check if we need to add extra indentation
        String trimmedLine = currentLine.trim();
        if (trimmedLine.endsWith("{")) {
            indentation += "    "; // Add exactly 4 spaces for opening brace
        } else if (trimmedLine.endsWith(":")) {
            indentation += "    "; // Add exactly 4 spaces for colon (switch cases, etc.)
        } else if (trimmedLine.startsWith("if ") || trimmedLine.startsWith("for ") || 
                   trimmedLine.startsWith("while ") || trimmedLine.startsWith("else ")) {
            indentation += "    "; // Add exactly 4 spaces for control structures
        }
        
        codeArea.insertText(codeArea.getCaretPosition(), "\n" + indentation);
        event.consume();
    }

    private void handleTabKey(CodeArea codeArea, javafx.scene.input.KeyEvent event) {
        // Insert exactly 4 spaces (not tabs)
        codeArea.insertText(codeArea.getCaretPosition(), "    ");
        event.consume();
    }

    private String getCurrentLine(CodeArea codeArea) {
        int caretPosition = codeArea.getCaretPosition();
        String text = codeArea.getText();
        int lineStart = text.lastIndexOf('\n', caretPosition - 1) + 1;
        int lineEnd = text.indexOf('\n', caretPosition);
        if (lineEnd == -1) lineEnd = text.length();
        return text.substring(lineStart, lineEnd);
    }

    private String getIndentation(String line) {
        StringBuilder indentation = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == ' ' || c == '\t') {
                indentation.append(c);
            } else {
                break;
            }
        }
        return indentation.toString();
    }

    private static StyleSpans<Collection<String>> computeEnhancedHighlighting(String text) {
        Matcher matcher = JavaKeywords.PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        
        while (matcher.find()) {
            String styleClass = null;
            
            // Check each group in order of priority
            if (matcher.group("ANNOTATION") != null) {
                styleClass = "annotation";
            } else if (matcher.group("KEYWORD") != null) {
                styleClass = "keyword";
            } else if (matcher.group("TYPE") != null) {
                styleClass = "type";
            } else if (matcher.group("FUNCTION") != null) {
                styleClass = "function";
            } else if (matcher.group("CONSTANT") != null) {
                styleClass = "constant";
            } else if (matcher.group("VARIABLE") != null) {
                styleClass = "variable";
            } else if (matcher.group("NUMBER") != null) {
                styleClass = "number";
            } else if (matcher.group("STRING") != null) {
                styleClass = "string";
            } else if (matcher.group("CHAR") != null) {
                styleClass = "string"; // Use string style for chars
            } else if (matcher.group("COMMENT") != null) {
                styleClass = "comment";
            } else if (matcher.group("OPERATOR") != null) {
                styleClass = "operator";
            } else if (matcher.group("PAREN") != null) {
                styleClass = "paren";
            } else if (matcher.group("BRACE") != null) {
                styleClass = "brace";
            } else if (matcher.group("BRACKET") != null) {
                styleClass = "bracket";
            } else if (matcher.group("SEMICOLON") != null) {
                styleClass = "semicolon";
            }
            
            if (styleClass != null) {
                spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
                spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
                lastKwEnd = matcher.end();
            }
        }
        
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}
