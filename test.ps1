# Direct Java/JUnit test script for CS151 Project 3.
# No Maven is required. This script compiles with javac and runs tests with the JUnit jar in lib/.

$defaultJavaFxPath = "C:\Users\ilike\Downloads\all\work\openjfx-21.0.11_windows-x64_bin-sdk\javafx-sdk-21.0.11\lib"
$junitPath = "lib\junit-platform-console-standalone.jar"

# A grader can either edit the default path above or set the JAVAFX_LIB environment variable.
if ($env:JAVAFX_LIB) {
    $javaFxPath = $env:JAVAFX_LIB
} else {
    $javaFxPath = $defaultJavaFxPath
}

if (!(Test-Path $javaFxPath)) {
    Write-Host "JavaFX lib folder not found: $javaFxPath"
    Write-Host "Edit test.ps1 or set JAVAFX_LIB to your JavaFX SDK lib folder."
    exit 1
}

if (!(Test-Path $junitPath)) {
    Write-Host "JUnit jar not found: $junitPath"
    exit 1
}

if (Test-Path out) {
    Remove-Item -Recurse -Force out
}

New-Item -ItemType Directory -Path out | Out-Null

Write-Host "Compiling main code and tests with javac..."
javac --module-path $javaFxPath --add-modules javafx.controls,javafx.fxml,javafx.media -cp $junitPath -d out (Get-ChildItem -Recurse src\main\java\*.java, src\test\*.java).FullName

if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed."
    exit $LASTEXITCODE
}

Write-Host "Copying resources..."
Copy-Item -Recurse src\main\resources\* out\

Write-Host "Running tests..."
java -jar $junitPath execute --class-path out --scan-class-path --include-classname ".*Test"
