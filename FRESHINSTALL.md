# Setup on new MacOS

First:
```
brew install openjdk@21

echo 'export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"' >> ~/.zshrc
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 21)' >> ~/.zshrc
source ~/.zshrc
```

Verify:
```
java -version
javac -version
```

We need:
```
java 21...
javac 21...
```

https://gluonhq.com/products/javafx/ 

Download javafx sdk 21 to:
~/Downloads/javafx-sdk-21.0.11

(make sure x86/ARM matches your device specs)

Set path:
```
export JAVAFX_LIB="$HOME/Downloads/javafx-sdk-21.0.11/lib"
echo 'export JAVAFX_LIB="$HOME/Downloads/javafx-sdk-21.0.11/lib"' >> ~/.zshrc
source ~/.zshrc

ls "$JAVAFX_LIB"
```

Then clone our repo:
```
cd ~/Downloads
git clone https://github.com/MrHitaDavid3/cs151-spring2026-project3.git
cd cs151-spring2026-project3
```

Make sure:
```bash
chmod +x test.sh
chmod +x run.sh
```

To run tests:

```bash
./test.sh
```

To run the app:

```bash
./run.sh
```
