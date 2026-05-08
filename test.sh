cat > test.sh <<'EOF'
#!/bin/bash

# Direct Java/JUnit test script for macOS/Linux.
# Compiles with javac and runs tests with the JUnit jar in lib/.

DEFAULT_JAVAFX_PATH="$HOME/Downloads/javafx-sdk-21.0.11/lib"
JUNIT_PATH="lib/junit-platform-console-standalone.jar"

# Edit the default path above or set the JAVAFX_LIB environment variable.
if [ -n "$JAVAFX_LIB" ]; then
    JAVAFX_PATH="$JAVAFX_LIB"
else
    JAVAFX_PATH="$DEFAULT_JAVAFX_PATH"
fi

if [ ! -d "$JAVAFX_PATH" ]; then
    echo "JavaFX lib folder not found: $JAVAFX_PATH"
    echo "Edit test.sh or set JAVAFX_LIB to your JavaFX SDK lib folder."
    exit 1
fi

if [ ! -f "$JUNIT_PATH" ]; then
    echo "JUnit jar not found: $JUNIT_PATH"
    exit 1
fi

if [ -d "out" ]; then
    rm -rf out
fi

mkdir -p out

echo "Compiling main code and tests with javac..."
javac --module-path "$JAVAFX_PATH" \
      --add-modules javafx.controls,javafx.fxml,javafx.media \
      -cp "$JUNIT_PATH" \
      -d out \
      $(find src/main/java src/test -name "*.java")

if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

echo "Copying resources..."
cp -R src/main/resources/* out/

echo "Running tests..."
java -jar "$JUNIT_PATH" execute \
     --class-path out \
     --scan-class-path \
     --include-classname ".*Test"
EOF

chmod +x test.sh
