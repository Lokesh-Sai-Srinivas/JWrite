# JWrite Deployment Guide

This guide explains how to create professional installers and deploy JWrite like VS Code.

## 🎯 **What We're Building**

1. **JWrite.exe** - Standalone executable with JWrite.ico branding
2. **Professional Installer** - Wizard-based installer with automatic JRE management
3. **Simple Installer** - Basic installer for quick deployment
4. **Complete Package** - Ready for distribution to end users

## 🚀 **Quick Start - Build EXE Only**

For a quick standalone executable:

```powershell
.\build-exe-only.ps1
```

This will:
- Build the fat JAR with Maven
- Download Launch4j automatically
- Create JWrite.exe with JWrite.ico branding
- Set up proper version information

## 🏗️ **Full Professional Installer**

For a complete VS Code-like installer experience:

```powershell
.\build-professional-installer.ps1
```

This will:
- Build the fat JAR
- Create JWrite.exe
- Download and install Inno Setup
- Create professional installer with wizard
- Include automatic JRE download
- Set up file associations
- Create shortcuts and uninstaller

## 📁 **Generated Files**

After building, you'll have:

```
jfxcodeeditor/
├── JWrite.exe                    # Standalone executable
├── installer/
│   ├── JWrite-Setup-1.0.0.exe   # Professional installer
│   └── JWrite-Simple-Setup-1.0.0.exe  # Simple installer
├── launch4j/                     # Launch4j tool
├── maven/                        # Local Maven installation
└── Various configuration files
```

## 🔧 **Installation Features**

### **Professional Installer:**
- ✨ Modern wizard interface
- 🌐 Automatic JRE 17 download
- 📁 File associations (.java files)
- 🖥️ Desktop and start menu shortcuts
- 🗑️ Proper uninstallation
- 🎨 JWrite.ico branding throughout
- 🔒 Administrator privileges required

### **Simple Installer:**
- 📦 Basic installation
- 📁 File associations
- 🖥️ Shortcuts
- 🎨 JWrite.ico branding
- 🔒 Administrator privileges required

## 📋 **Prerequisites**

- **Java 17+** installed on the system
- **Windows 10/11** (64-bit)
- **PowerShell** execution policy allowing scripts
- **Internet connection** for tool downloads

## 🛠️ **Tools Used**

### **Launch4j**
- Converts JAR to Windows EXE
- Adds JWrite.ico as application icon
- Sets version information
- Configures JRE requirements

### **Inno Setup**
- Creates professional Windows installers
- Wizard interface with custom pages
- File associations and shortcuts
- Proper uninstallation

### **Maven**
- Builds the Java project
- Creates fat JAR with dependencies
- Manages project lifecycle

## 📖 **Configuration Files**

### **JWrite-launch4j-config.xml**
- Launch4j configuration
- Icon: `src/main/resources/styles/JWrite.ico`
- Output: `JWrite.exe`
- JRE requirements: Java 17+

### **JWrite-Professional-Installer.iss**
- Full Inno Setup script
- Automatic JRE download
- File associations
- Professional wizard

### **JWrite-Simple-Installer.iss**
- Basic Inno Setup script
- Simple installation
- File associations
- Standard wizard

## 🎨 **Branding with JWrite.ico**

The JWrite.ico file is used throughout:

- **Application icon** in Windows
- **File associations** for .java files
- **Start menu shortcuts**
- **Desktop shortcuts**
- **Installer branding**
- **Taskbar icon**

## 🔄 **Build Process**

1. **Maven Build**
   ```bash
   mvn clean package shade:shade
   ```

2. **Launch4j Conversion**
   ```bash
   launch4j.exe JWrite-launch4j-config.xml
   ```

3. **Inno Setup Compilation**
   ```bash
   ISCC.exe JWrite-Professional-Installer.iss
   ```

## 📦 **Distribution**

### **For End Users:**
- **JWrite-Setup-1.0.0.exe** - Professional installer
- **JWrite-Simple-Setup-1.0.0.exe** - Simple installer

### **For Developers:**
- **JWrite.exe** - Standalone executable
- **Source code** - Full project

## 🧪 **Testing**

1. **Test EXE:**
   ```bash
   .\JWrite.exe
   ```

2. **Test Installer:**
   ```bash
   .\installer\JWrite-Setup-1.0.0.exe
   ```

3. **Verify Installation:**
   - Check Program Files
   - Verify shortcuts
   - Test file associations
   - Run application

## 🚨 **Troubleshooting**

### **Common Issues:**

1. **Java not found:**
   - Install Java 17+ from https://adoptium.net/
   - Ensure JAVA_HOME is set

2. **Maven build fails:**
   - Check internet connection
   - Verify Java version
   - Clear Maven cache

3. **Launch4j fails:**
   - Check JAR file exists
   - Verify Launch4j download
   - Check configuration XML

4. **Inno Setup fails:**
   - Install Inno Setup 6
   - Check script syntax
   - Verify file paths

## 📚 **Advanced Customization**

### **Custom Installer Pages:**
Edit the .iss files to add:
- Custom welcome pages
- License agreements
- Additional options
- Post-install actions

### **JRE Bundling:**
Modify the installer to:
- Bundle specific JRE version
- Include multiple JRE options
- Custom JRE download URLs

### **File Associations:**
Add support for:
- Additional file types
- Custom file handlers
- Shell extensions

## 🌟 **Professional Features**

- **Digital signatures** (optional)
- **Auto-updates** (future enhancement)
- **Multi-language support** (future enhancement)
- **Silent installation** support
- **Custom branding** options

## 📞 **Support**

For deployment issues:
1. Check the troubleshooting section
2. Verify all prerequisites
3. Review build logs
4. Test on clean system

---

**Happy Deploying! 🚀**

JWrite is now ready to be distributed like a professional application with VS Code-like installation experience. 