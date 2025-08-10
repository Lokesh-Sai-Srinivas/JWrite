Write-Host "Starting JWrite - Java Code Editor..." -ForegroundColor Green

# Add Maven to PATH if it exists locally
$mavenPath = Join-Path $PSScriptRoot "apache-maven-3.9.6\bin"
if (Test-Path $mavenPath) {
    $env:PATH = "$mavenPath;$env:PATH"
    Write-Host "Using local Maven installation" -ForegroundColor Yellow
}

# Check if Maven is available
try {
    $mvnVersion = mvn --version 2>$null
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Maven found successfully" -ForegroundColor Green
        mvn javafx:run
    } else {
        throw "Maven not found"
    }
} catch {
    Write-Host "Error: Maven not found. Please ensure Maven is installed and in your PATH." -ForegroundColor Red
    Write-Host "You can download Maven from: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
}

Read-Host "Press Enter to exit" 