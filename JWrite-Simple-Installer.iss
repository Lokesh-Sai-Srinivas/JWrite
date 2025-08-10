; JWrite Simple Installer Script
; Created with Inno Setup 6

#define MyAppName "JWrite"
#define MyAppVersion "1.0.0"
#define MyAppPublisher "JWrite Development"
#define MyAppURL "https://github.com/jwrite"
#define MyAppExeName "JWrite.exe"
#define MyAppIconName "JWrite.ico"

[Setup]
AppId={{A1B2C3D4-E5F6-7890-ABCD-EF1234567890}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName={autopf}\{#MyAppName}
DefaultGroupName={#MyAppName}
AllowNoIcons=yes
LicenseFile=LICENSE.txt
InfoBeforeFile=INFO.txt
InfoAfterFile=AFTER_INFO.txt
OutputDir=installer
OutputBaseFilename=JWrite-Simple-Setup-{#MyAppVersion}
SetupIconFile=src\main\resources\styles\{#MyAppIconName}
Compression=lzma
SolidCompression=yes
WizardStyle=modern
PrivilegesRequired=admin
ArchitecturesAllowed=x64
ArchitecturesInstallIn64BitMode=x64

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked
Name: "quicklaunchicon"; Description: "{cm:CreateQuickLaunchIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked; OnlyBelowVersion: 6.1; Check: not IsAdminInstallMode
Name: "associatejava"; Description: "Associate .java files with JWrite"; GroupDescription: "File Associations"; Flags: unchecked

[Files]
; Main application executable
Source: "JWrite.exe"; DestDir: "{app}"; Flags: ignoreversion
; Application icon
Source: "src\main\resources\styles\{#MyAppIconName}"; DestDir: "{app}"; Flags: ignoreversion
; License and information files
Source: "LICENSE.txt"; DestDir: "{app}"; Flags: ignoreversion
Source: "README.md"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
Name: "{group}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; IconFilename: "{app}\{#MyAppIconName}"
Name: "{group}\{cm:UninstallProgram,{#MyAppName}}"; Filename: "{uninstallexe}"
Name: "{autodesktop}\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: desktopicon; IconFilename: "{app}\{#MyAppIconName}"
Name: "{userappdata}\Microsoft\Internet Explorer\Quick Launch\{#MyAppName}"; Filename: "{app}\{#MyAppExeName}"; Tasks: quicklaunchicon; IconFilename: "{app}\{#MyAppIconName}"

[Registry]
; File association for .java files
Root: HKCR; Subkey: ".java"; ValueType: string; ValueName: ""; ValueData: "JWriteJavaFile"; Flags: uninsdeletevalue; Tasks: associatejava
Root: HKCR; Subkey: "JWriteJavaFile"; ValueType: string; ValueName: ""; ValueData: "Java Source File"; Flags: uninsdeletekey; Tasks: associatejava
Root: HKCR; Subkey: "JWriteJavaFile\DefaultIcon"; ValueType: string; ValueName: ""; ValueData: "{app}\{#MyAppIconName}"; Flags: uninsdeletekey; Tasks: associatejava
Root: HKCR; Subkey: "JWriteJavaFile\shell\open\command"; ValueType: string; ValueName: ""; ValueData: """{app}\{#MyAppExeName}"" ""%1"""; Flags: uninsdeletekey; Tasks: associatejava

[Run]
Filename: "{app}\{#MyAppExeName}"; Description: "{cm:LaunchProgram,{#StringChange(MyAppName, '&', '&&')}}"; Flags: nowait postinstall skipifsilent 