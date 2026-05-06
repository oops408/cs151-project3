# Direct Java/JavaFX run script
# Compiles with javac and runs the app with java.

$defaultJavaFxPath = "C:\Users\ilike\Downloads\all\work\openjfx-21.0.11_windows-x64_bin-sdk\javafx-sdk-21.0.11\lib"

# Edit the default path above or set the JAVAFX_LIB environment variable.
if ($env:JAVAFX_LIB) {
    $javaFxPath = $env:JAVAFX_LIB
} else {
    $javaFxPath = $defaultJavaFxPath
}

if (!(Test-Path $javaFxPath)) {
    Write-Host "JavaFX lib folder not found: $javaFxPath"
    Write-Host "Edit run.ps1 or set JAVAFX_LIB to your JavaFX SDK lib folder."
    exit 1
}

if (Test-Path out) {
    Remove-Item -Recurse -Force out
}

New-Item -ItemType Directory -Path out | Out-Null

Write-Host "Compiling main code with javac..."
javac --module-path $javaFxPath --add-modules javafx.controls,javafx.fxml,javafx.media -d out (Get-ChildItem -Recurse src\main\java\*.java).FullName

if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed."
    exit $LASTEXITCODE
}

Write-Host "Copying resources..."
Copy-Item -Recurse src\main\resources\* out\

Write-Host "Running app..."
java --module-path $javaFxPath --add-modules javafx.controls,javafx.fxml,javafx.media -cp out app.GameManagerApp
