package com.example;

import com.example.service.EditorManager;
import com.example.service.FileManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Enhanced main controller with VS Code-like features for JWrite.
 * Handles user interactions, keyboard shortcuts, and coordinates between services.
 */
public class MainController {

    @FXML
    private BorderPane rootPane;
    @FXML
    private TreeView<File> fileTreeView;
    @FXML
    private TabPane editorTabPane;
    @FXML
    private TextArea outputConsole;
    @FXML
    private Label statusLabel;

    private FileManager fileManager;
    private EditorManager editorManager;

    @FXML
    public void initialize() {
        this.fileManager = new FileManager();
        this.editorManager = new EditorManager(editorTabPane, statusLabel);
        
        setupFileTreeView();
        setupKeyboardShortcuts();
        setupContextMenus();
        
        // Apply dark theme only
        applyDarkTheme();
        
        statusLabel.setText("Welcome to JWrite! Open a directory to begin.");
    }
    
    private void setupFileTreeView() {
        fileManager.setTreeView(fileTreeView);
        fileTreeView.setCellFactory(tv -> new FileManager.FileTreeCell());
        fileTreeView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        File selectedFile = newValue.getValue();
                        if (selectedFile.isFile()) {
                            editorManager.openFileInEditor(selectedFile);
                        }
                    }
                });
    }

    private void setupKeyboardShortcuts() {
        Scene scene = rootPane.getScene();
        if (scene != null) {
            // File operations
            scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN),
                this::handleNewFile
            );
            
            scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN),
                this::handleOpenDirectory
            );
            
            scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN),
                this::handleSaveFile
            );
            
            scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN),
                this::handleSaveFileAs
            );
            
            // Editor operations
            scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN),
                this::handleCloseTab
            );
            
            scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F4, KeyCombination.CONTROL_DOWN),
                this::handleCloseTab
            );
            
            // Tab navigation
            scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.TAB, KeyCombination.CONTROL_DOWN),
                this::handleNextTab
            );
            
            scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.TAB, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN),
                this::handlePreviousTab
            );
            
            // Run operations
            scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F5),
                this::handleRunCode
            );
            
            scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F6),
                this::handleRunCode
            );
            
            // Find and replace
            scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN),
                this::handleFind
            );
            
            scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN),
                this::handleReplace
            );
            
            // File tree operations
            scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F5, KeyCombination.SHIFT_DOWN),
                this::handleRefreshFileTree
            );
            
            // Quick actions
            scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F1),
                this::handleShowShortcuts
            );
            
            scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F12),
                this::handleGoToFile
            );

            // Format Document
            scene.getAccelerators().put(
                new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN),
                this::handleFormatDocument
            );
        }
    }

    private void setupContextMenus() {
        fileTreeView.setContextMenu(createFileContextMenu());
    }

    private ContextMenu createFileContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        
        MenuItem newFile = new MenuItem("New File");
        newFile.setOnAction(e -> handleNewFileFromContext());
        
        MenuItem newFolder = new MenuItem("New Folder");
        newFolder.setOnAction(e -> handleNewFolderFromContext());
        
        MenuItem rename = new MenuItem("Rename");
        rename.setOnAction(e -> handleRenameFromContext());
        
        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(e -> handleDeleteFromContext());
        
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction(e -> fileManager.refreshTree());
        
        contextMenu.getItems().addAll(newFile, newFolder, new SeparatorMenuItem(), 
                                    rename, delete, new SeparatorMenuItem(), refresh);
        
        return contextMenu;
    }
    
    private void applyDarkTheme() {
        Scene scene = rootPane.getScene();
        if (scene != null) {
            scene.getStylesheets().clear();
            
            // Add base styles for syntax highlighting
            String keywordsPath = "/com/example/style-keywords.css";
            scene.getStylesheets().add(getClass().getResource(keywordsPath).toExternalForm());
            
            // Add dark theme
            String darkThemePath = "/com/example/dark-theme.css";
            scene.getStylesheets().add(getClass().getResource(darkThemePath).toExternalForm());
            
            // Force refresh of the scene to ensure styles are applied
            scene.getRoot().setStyle("-fx-background-color: #1E1E1E;");
        }
    }

    @FXML
    private void handleOpenDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open Project Directory");
        File dir = directoryChooser.showDialog(rootPane.getScene().getWindow());
        if (dir != null) {
            TreeItem<File> rootItem = fileManager.createFileTree(dir);
            fileTreeView.setRoot(rootItem);
            statusLabel.setText("Opened: " + dir.getAbsolutePath());
        }
    }

    @FXML
    private void handleNewFile() {
        editorManager.createNewFile();
    }

    @FXML
    private void handleSaveFile() {
        editorManager.saveCurrentFile();
    }

    @FXML
    private void handleSaveFileAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File As...");
        File file = fileChooser.showSaveDialog(rootPane.getScene().getWindow());
        if (file != null) {
            editorManager.saveCurrentFileAs(file);
        }
    }

    @FXML
    private void handleCloseTab() {
        editorManager.closeCurrentTab();
    }

    @FXML
    private void handleNextTab() {
        TabPane tabPane = editorTabPane;
        int tabCount = tabPane.getTabs().size();
        if (tabCount > 0) {
            int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
            int nextIndex = (currentIndex + 1) % tabCount;
            tabPane.getSelectionModel().select(nextIndex);
        }
    }

    @FXML
    private void handlePreviousTab() {
        TabPane tabPane = editorTabPane;
        int tabCount = tabPane.getTabs().size();
        if (tabCount > 0) {
            int currentIndex = tabPane.getSelectionModel().getSelectedIndex();
            int prevIndex = (currentIndex - 1 + tabCount) % tabCount;
            tabPane.getSelectionModel().select(prevIndex);
        }
    }

    @FXML
    private void handleRefreshFileTree() {
        fileManager.refreshTree();
        statusLabel.setText("File tree refreshed");
    }

    @FXML
    private void handleShowShortcuts() {
        showShortcutsDialog();
    }

    @FXML
    private void handleGoToFile() {
        // TODO: Implement go to file functionality
        statusLabel.setText("Go to file functionality coming soon...");
    }

    @FXML
    private void handleExit() {
        Platform.exit();
    }

    @FXML
    private void handleFind() {
        // TODO: Implement find functionality
        statusLabel.setText("Find functionality coming soon...");
    }

    @FXML
    private void handleReplace() {
        // TODO: Implement replace functionality
        statusLabel.setText("Replace functionality coming soon...");
    }

    @FXML
    private void handleFormatDocument() {
        editorManager.formatCurrentDocument();
        statusLabel.setText("Document formatted");
    }

    private void showShortcutsDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("JWrite Keyboard Shortcuts");
        alert.setHeaderText("Available Keyboard Shortcuts");
        
        String shortcuts = """
            File Operations:
            • Ctrl+N - New File
            • Ctrl+O - Open Directory
            • Ctrl+S - Save
            • Ctrl+Shift+S - Save As
            • Ctrl+W - Close Tab
            
            Editor Operations:
            • Ctrl+F - Find
            • Ctrl+H - Replace
            • Ctrl+Tab - Next Tab
            • Ctrl+Shift+Tab - Previous Tab
            
            Run Operations:
            • F5 - Run Java Code
            • F6 - Run Java Code (alternative)
            
            File Tree:
            • Shift+F5 - Refresh File Tree
            • F1 - Show Shortcuts
            • F12 - Go to File
            """;
        
        alert.setContentText(shortcuts);
        alert.showAndWait();
    }

    private void handleNewFileFromContext() {
        File selectedFile = fileManager.getSelectedFile();
        File parentDir = selectedFile != null && selectedFile.isDirectory() ? 
                        selectedFile : fileManager.getRootDirectory();
        
        if (parentDir != null) {
            TextInputDialog dialog = new TextInputDialog("NewFile.java");
            dialog.setTitle("Create New File");
            dialog.setHeaderText("Enter file name:");
            dialog.setContentText("File name:");
            
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(fileName -> {
                if (fileManager.createNewFile(parentDir, fileName)) {
                    statusLabel.setText("Created: " + fileName);
                } else {
                    statusLabel.setText("Failed to create file");
                }
            });
        }
    }

    private void handleNewFolderFromContext() {
        File selectedFile = fileManager.getSelectedFile();
        File parentDir = selectedFile != null && selectedFile.isDirectory() ? 
                        selectedFile : fileManager.getRootDirectory();
        
        if (parentDir != null) {
            TextInputDialog dialog = new TextInputDialog("NewFolder");
            dialog.setTitle("Create New Folder");
            dialog.setHeaderText("Enter folder name:");
            dialog.setContentText("Folder name:");
            
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(folderName -> {
                if (fileManager.createNewDirectory(parentDir, folderName)) {
                    statusLabel.setText("Created folder: " + folderName);
                } else {
                    statusLabel.setText("Failed to create folder");
                }
            });
        }
    }

    private void handleRenameFromContext() {
        File selectedFile = fileManager.getSelectedFile();
        if (selectedFile != null) {
            TextInputDialog dialog = new TextInputDialog(selectedFile.getName());
            dialog.setTitle("Rename");
            dialog.setHeaderText("Enter new name:");
            dialog.setContentText("New name:");
            
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newName -> {
                if (fileManager.renameFile(selectedFile, newName)) {
                    statusLabel.setText("Renamed to: " + newName);
                } else {
                    statusLabel.setText("Failed to rename");
                }
            });
        }
    }

    private void handleDeleteFromContext() {
        File selectedFile = fileManager.getSelectedFile();
        if (selectedFile != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Delete " + selectedFile.getName() + "?");
            alert.setContentText("This action cannot be undone.");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean success = selectedFile.isDirectory() ? 
                                fileManager.deleteDirectory(selectedFile) : 
                                fileManager.deleteFile(selectedFile);
                
                if (success) {
                    statusLabel.setText("Deleted: " + selectedFile.getName());
                } else {
                    statusLabel.setText("Failed to delete");
                }
            }
        }
    }

    @FXML
    private void handleRunCode() {
        File currentFile = editorManager.getCurrentFile();
        if (currentFile == null) {
            showError("No file is currently open.");
            return;
        }

        if (!currentFile.getName().endsWith(".java")) {
            showError("Only .java files can be executed.");
            return;
        }

        // Save the file before running
        editorManager.saveCurrentFile();
        outputConsole.clear();
        statusLabel.setText("Running " + currentFile.getName() + "...");

        // Run the compilation and execution in a background thread
        new Thread(() -> {
            try {
                String fileDir = currentFile.getParent();
                String className = extractClassName(currentFile);
                
                if (className == null) {
                    showError("Could not find a valid class declaration in the file.");
                    return;
                }

                // Compile the Java file
                ProcessBuilder compileBuilder = new ProcessBuilder("javac", "-encoding", "UTF-8", currentFile.getName());
                compileBuilder.directory(new File(fileDir));
                compileBuilder.redirectErrorStream(true);
                
                // Read compilation output
                Process compileProcess = compileBuilder.start();
                StringBuilder compileOutput = new StringBuilder();
                
                try (var reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(compileProcess.getInputStream(), "UTF-8"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        compileOutput.append(line).append("\n");
                        final String outputLine = line;
                        Platform.runLater(() -> outputConsole.appendText(outputLine + "\n"));
                    }
                }

                int compileExitCode = compileProcess.waitFor();
                
                if (compileExitCode != 0) {
                    Platform.runLater(() -> {
                        outputConsole.appendText("\n--- Compilation failed with exit code: " + compileExitCode + " ---\n");
                        statusLabel.setText("Compilation failed");
                    });
                    return;
                }
                
                Platform.runLater(() -> {
                    outputConsole.appendText("\n--- Compilation successful. Running program... ---\n");
                    statusLabel.setText("Running program...");
                });

                // Run the compiled class
                ProcessBuilder runBuilder = new ProcessBuilder("java", "-Dfile.encoding=UTF-8", "-cp", ".", className);
                runBuilder.directory(new File(fileDir));
                runBuilder.redirectErrorStream(true);
                
                Process runProcess = runBuilder.start();
                
                // Read program output in a separate thread to prevent deadlocks
                new Thread(() -> {
                    try (var reader = new java.io.BufferedReader(
                            new java.io.InputStreamReader(runProcess.getInputStream(), "UTF-8"))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            final String outputLine = line;
                            Platform.runLater(() -> outputConsole.appendText(outputLine + "\n"));
                        }
                    } catch (IOException e) {
                        Platform.runLater(() -> outputConsole.appendText("Error reading program output: " + e.getMessage() + "\n"));
                    }
                }).start();
                
                // Wait for the process to complete
                int runExitCode = runProcess.waitFor();
                
                Platform.runLater(() -> {
                    if (runExitCode == 0) {
                        statusLabel.setText("Program completed successfully");
                    } else {
                        statusLabel.setText("Program exited with code: " + runExitCode);
                    }
                });

            } catch (IOException | InterruptedException e) {
                Platform.runLater(() -> {
                    outputConsole.appendText("\nError: " + e.getMessage() + "\n");
                    statusLabel.setText("Execution failed");
                });
            }
        }).start();
    }
    
    private String extractClassName(File javaFile) throws IOException {
        String fileContent = new String(Files.readAllBytes(javaFile.toPath()), "UTF-8");
        
        // First try to find public class
        Pattern pattern = Pattern.compile("public\\s+class\\s+(\\w+)");
        Matcher matcher = pattern.matcher(fileContent);
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        // If no public class, try any class
        pattern = Pattern.compile("class\\s+(\\w+)");
        matcher = pattern.matcher(fileContent);
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        return null;
    }
    
    private void showError(String message) {
        Platform.runLater(() -> {
            outputConsole.appendText("Error: " + message + "\n");
            statusLabel.setText(message);
        });
    }
}
