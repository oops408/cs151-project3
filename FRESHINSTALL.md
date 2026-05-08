# Setup on new MacOS

brew install openjdk@21

echo 'export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"' >> ~/.zshrc
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 21)' >> ~/.zshrc
source ~/.zshrc

java -version
javac -version

We need:
java 21...
javac 21...

https://gluonhq.com/products/javafx/ 

Download javafx sdk 21:
~/Downloads/javafx-sdk-21.0.11

Set path:
export JAVAFX_LIB="$HOME/Downloads/javafx-sdk-21.0.11/lib"
echo 'export JAVAFX_LIB="$HOME/Downloads/javafx-sdk-21.0.11/lib"' >> ~/.zshrc
source ~/.zshrc

ls "$JAVAFX_LIB"

cd ~/Downloads
git clone https://github.com/MrHitaDavid3/cs151-spring2026-project3.git
cd cs151-spring2026-project3

Make sure:
chmod +x test.sh
chmod +x run.sh

To run tests:

```bash
./test.sh
```

To run the app:

```bash
./run.sh
```
