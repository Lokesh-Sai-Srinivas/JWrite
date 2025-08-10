package com.example.service;

import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Enhanced file manager with VS Code-like file operations.
 * Manages file system interactions, file tree, and file operations.
 */
public class FileManager {

    private TreeView<File> treeView;
    private TreeItem<File> rootItem;

    public void setTreeView(TreeView<File> treeView) {
        this.treeView = treeView;
    }

    public TreeItem<File> createFileTree(File directory) {
        rootItem = new TreeItem<>(directory);
        rootItem.setExpanded(true);
        populateTree(directory, rootItem);
        return rootItem;
    }

    private void populateTree(File dir, TreeItem<File> parent) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                TreeItem<File> item = new TreeItem<>(file);
                parent.getChildren().add(item);
                if (file.isDirectory()) {
                    populateTree(file, item);
                }
            }
        }
    }

    public void refreshTree() {
        if (rootItem != null && rootItem.getValue() != null) {
            rootItem.getChildren().clear();
            populateTree(rootItem.getValue(), rootItem);
        }
    }

    public boolean createNewFile(File parentDirectory, String fileName) {
        try {
            File newFile = new File(parentDirectory, fileName);
            if (newFile.createNewFile()) {
                refreshTree();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createNewDirectory(File parentDirectory, String dirName) {
        File newDir = new File(parentDirectory, dirName);
        if (newDir.mkdir()) {
            refreshTree();
            return true;
        }
        return false;
    }

    public boolean deleteFile(File file) {
        if (file.delete()) {
            refreshTree();
            return true;
        }
        return false;
    }

    public boolean deleteDirectory(File directory) {
        if (deleteDirectoryRecursive(directory)) {
            refreshTree();
            return true;
        }
        return false;
    }

    private boolean deleteDirectoryRecursive(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectoryRecursive(file);
                } else {
                    file.delete();
                }
            }
        }
        return directory.delete();
    }

    public boolean renameFile(File file, String newName) {
        File newFile = new File(file.getParentFile(), newName);
        if (file.renameTo(newFile)) {
            refreshTree();
            return true;
        }
        return false;
    }

    public File getSelectedFile() {
        if (treeView != null && treeView.getSelectionModel().getSelectedItem() != null) {
            return treeView.getSelectionModel().getSelectedItem().getValue();
        }
        return null;
    }

    public File getRootDirectory() {
        if (rootItem != null) {
            return rootItem.getValue();
        }
        return null;
    }

    public static class FileTreeCell extends TreeCell<File> {
        @Override
        protected void updateItem(File file, boolean empty) {
            super.updateItem(file, empty);
            if (empty || file == null) {
                setText(null);
                setGraphic(null);
            } else {
                Label label = new Label(file.getName());
                Text icon = new Text(getFileIcon(file));
                icon.setFont(Font.font("System", FontWeight.NORMAL, 16));
                label.setGraphic(icon);
                setGraphic(label);
                setText(null);
            }
        }

        private String getFileIcon(File file) {
            if (file.isDirectory()) {
                return "📁";
            } else {
                String name = file.getName().toLowerCase();
                if (name.endsWith(".java")) {
                    return "☕";
                } else if (name.endsWith(".xml") || name.endsWith(".fxml")) {
                    return "📄";
                } else if (name.endsWith(".css")) {
                    return "🎨";
                } else if (name.endsWith(".js")) {
                    return "📜";
                } else if (name.endsWith(".html") || name.endsWith(".htm")) {
                    return "🌐";
                } else if (name.endsWith(".json")) {
                    return "📋";
                } else if (name.endsWith(".md")) {
                    return "📝";
                } else if (name.endsWith(".txt")) {
                    return "📄";
                } else if (name.endsWith(".jar") || name.endsWith(".war")) {
                    return "📦";
                } else if (name.endsWith(".gitignore") || name.endsWith(".git")) {
                    return "🔧";
                } else if (name.endsWith(".properties") || name.endsWith(".yml") || name.endsWith(".yaml")) {
                    return "⚙️";
                } else {
                    return "📄";
                }
            }
        }
    }
}
