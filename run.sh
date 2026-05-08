cat > run.sh <<'EOF'
#!/bin/bash

# Direct Java/JavaFX run script for macOS/Linux.
# Compiles with javac and runs the app with java.

DEFAULT_JAVAFX_PATH="$HOME/Downloads/javafx-sdk-21.0.11/lib"

# Edit the default path above or set the JAVAFX_LIB environment variable.
if [ -n "$JAVAFX_LIB" ]; then
    JAVAFX_PATH="$JAVAFX_LIB"
else
    JAVAFX_PATH="$DEFAULT_JAVAFX_PATH"
fi

if [ ! -d "$JAVAFX_PATH" ]; then
    echo "JavaFX lib folder not found: $JAVAFX_PATH"
    echo "Edit run.sh or set JAVAFX_LIB to your JavaFX SDK lib folder."
    exit 1
fi

if [ -d "out" ]; then
    rm -rf out
fi

mkdir -p out

echo "Compiling main code with javac..."
javac --module-path "$JAVAFX_PATH" \
      --add-modules javafx.controls,javafx.fxml,javafx.media \
      -d out \
      $(find src/main/java -name "*.java")

if [ $? -ne 0 ]; then
    echo "Compilation failed."
    exit 1
fi

echo "Copying resources..."
cp -R src/main/resources/* out/

echo "Running app..."
java --module-path "$JAVAFX_PATH" \
     --add-modules javafx.controls,javafx.fxml,javafx.media \
     -cp out \
     app.GameManagerApp
EOF

chmod +x run.sh
