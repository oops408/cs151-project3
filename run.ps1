$javaFxPath = "C:\Users\ilike\Downloads\all\work\openjfx-21.0.11_windows-x64_bin-sdk\javafx-sdk-21.0.11\lib"
$junitPath = "lib\junit-platform-console-standalone.jar"

if (Test-Path out) {
    Remove-Item -Recurse -Force out
}

New-Item -ItemType Directory -Path out | Out-Null

Write-Host "Compiling main code..."
javac --module-path $javaFxPath --add-modules javafx.controls,javafx.fxml,javafx.media -d out (Get-ChildItem -Recurse src\main\java\*.java).FullName

Write-Host "Copying resources..."
Copy-Item -Recurse src\main\resources\* out\

Write-Host "Running app..."
java --module-path $javaFxPath --add-modules javafx.controls,javafx.fxml,javafx.media -cp out app.GameManagerApp