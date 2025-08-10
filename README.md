# JWrite - Java Code Editor

A lightweight Java editor with VS Code-like features, including syntax highlighting and basic file management.

## Features

### Implemented Features
- **Basic Interface**: Dark and light theme support
- **Syntax Highlighting**: Basic Java syntax highlighting
- **File Management**: Basic file operations (open, save, close)
- **Multi-tab Editing**: Support for editing multiple files
- **Basic UI**: Clean interface with file tree and editor

### Planned Features
- **Advanced Syntax Highlighting**: More comprehensive Java syntax support
- **Smart Auto-completion**: Automatic brace and quote generation
- **Code Execution**: Compile and run Java files
- **Keyboard Shortcuts**: More VS Code-like shortcuts
- **File Operations**: Advanced features like rename and delete
- **Context Menus**: Right-click file operations

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- (Optional) Git for version control

## Quick Start

### Option 1: Using the provided scripts (Windows)
```bash
# Using batch file (recommended)
start.bat

# Or using PowerShell
.\run.ps1
```

### Option 2: Clone and run
```bash
git clone https://github.com/Lokesh-Sai-Srinivas/JWrite.git
cd JWrite
start.bat
```

### Option 2: Using Maven directly
```bash
# Compile and run
mvn javafx:run

# Or compile first, then run
mvn clean compile
mvn javafx:run
```

### Option 3: Full build and run
```bash
mvn clean package
mvn javafx:run
```

## Project Structure

```
jwrite/
├── src/main/java/com/example/
│   ├── App.java                 # Main application entry point
│   ├── MainController.java      # Main UI controller
│   ├── service/
│   │   ├── EditorManager.java   # Code editing and syntax highlighting
│   │   └── FileManager.java     # File system operations
│   └── util/
│       └── JavaKeywords.java    # Java syntax patterns
├── src/main/resources/
│   ├── MainView.fxml           # UI layout
│   ├── dark-theme.css          # Dark theme styles
│   ├── light-theme.css         # Light theme styles
│   └── style-keywords.css      # Syntax highlighting styles
└── test/
    └── HelloWorld.java         # Test file for code execution
```

## Usage

### File Operations
1. **Open a Directory**: Use File → Open Directory or `Ctrl+O` to browse and open a project folder
2. **Create New File**: Use File → New File or `Ctrl+N` to create a new file
3. **Save Files**: Use File → Save (`Ctrl+S`) or File → Save As (`Ctrl+Shift+S`)
4. **Close Tabs**: Use File → Close Tab or `Ctrl+W`

### Editing Features
1. **Smart Indentation**: Press `Enter` for automatic indentation
2. **Auto-completion**: 
   - Type `{` for automatic brace completion
   - Type `(` for automatic parenthesis completion
   - Type `[` for automatic bracket completion
   - Type `"` for automatic quote completion
3. **Tab Key**: Inserts 4 spaces for consistent indentation
4. **Syntax Highlighting**: Real-time highlighting as you type

### File Management
1. **Context Menus**: Right-click in file tree for:
   - New File
   - New Folder
   - Rename
   - Delete
   - Refresh
2. **File Icons**: Visual indicators for different file types

### Code Execution
1. **Run Java Code**: Use Run → Run Java Code or `F5`
2. **Output Console**: View compilation and execution results
3. **Error Handling**: Comprehensive error reporting

### Keyboard Shortcuts

#### File Operations
- `Ctrl+N` - New File
- `Ctrl+O` - Open Directory
- `Ctrl+S` - Save
- `Ctrl+Shift+S` - Save As
- `Ctrl+W` - Close Tab

#### Editor Operations
- `Ctrl+F` - Find (coming soon)
- `Ctrl+H` - Replace (coming soon)
- `Ctrl+Shift+T` - Toggle Theme

#### Run Operations
- `F5` - Run Java Code
- `F6` - Run Java Code (alternative)

## VS Code-like Features

### Auto-completion
- **Braces**: Type `{` → `{\n    \n}`
- **Parentheses**: Type `(` → `()`
- **Brackets**: Type `[` → `[]`
- **Quotes**: Type `"` → `""`
- **Smart Skipping**: Press closing characters to skip over existing ones

### Smart Indentation
- **Automatic**: Maintains proper indentation on Enter
- **Brace-aware**: Extra indentation for opening braces
- **Control structures**: Proper indentation for if, for, while, else
- **Consistent spacing**: Always uses 4 spaces

### Syntax Highlighting
- **Keywords**: Java keywords in blue
- **Types**: Java types in teal
- **Functions**: Function names in yellow
- **Variables**: Variables in light blue
- **Constants**: Constants in bright blue
- **Numbers**: Numbers in green
- **Strings**: Strings in orange
- **Comments**: Comments in green
- **Annotations**: Annotations in purple

## Troubleshooting

### Maven not found
If Maven is not installed, you can download it from [Apache Maven](https://maven.apache.org/download.cgi) or use the included Maven distribution.

### JavaFX modules not found
The project uses the JavaFX Maven plugin which automatically handles JavaFX dependencies. If you encounter issues, ensure you're using Java 17+ and the latest Maven version.

### Compilation errors
If you encounter compilation errors, ensure:
- Java 17+ is installed and in your PATH
- Maven 3.6+ is installed and in your PATH
- All dependencies are properly downloaded

## Development

This project uses:
- **JavaFX 21**: UI framework
- **RichTextFX 0.11.2**: Advanced text editing
- **Maven**: Build management
- **Java 17**: Runtime environment

## License

This project is for educational and personal use.

## About JWrite

JWrite is designed to be a true VS Code copy for Java development, providing all the essential features developers expect from a modern code editor while maintaining simplicity and performance. 