# CS-151-06-Spring26 Project 3: Game Manager

## Overview
This project is a JavaFX Game Manager with two fully playable games: **Blackjack** and **Snake**. The program supports account creation, login, persistent high scores, a shared toolbar after login, different background music for each game, encrypted Blackjack save states, and encrypted account passwords.

The project intentionally uses basic Java, JavaFX, text files, and simple object-oriented design. The included PowerShell scripts compile the project directly with `javac` and run it with `java`.

## Main Features
- JavaFX login and account creation screen
- `user_accounts.txt` stores usernames with encrypted passwords
- `high_scores.txt` stores high scores for both Blackjack and Snake in one shared file
- Main menu shows top 5 high scores for both games
- Main menu has buttons for Blackjack, Snake, and two disabled future-game buttons
- Toolbar remains visible after login on the main menu and both games
- Blackjack supports betting, hit, stand, bust, dealer rules, two computer players, save, and load
- Snake supports arrow-key movement, random direction, food, growth, score, pause, game over, and restart
- Two different local mp3 files are included under `src/main/resources/audio/`
- Save states and passwords are encrypted with a simple AES utility class
- Unit tests cover important model, persistence, utility, and OOP behavior

## Project Structure / Design
The project is split into small packages so each major part has a clear responsibility.

```text
cs151-project3/
├── lib/
│   └── junit-platform-console-standalone.jar   # JUnit 5 jar used to run tests
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── app/              # Entry point for the JavaFX program
│   │   │   ├── blackjack/        # Blackjack game package
│   │   │   │   ├── controller/   # Blackjack UI and button actions
│   │   │   │   └── model/        # Blackjack cards, players, hands, and game state
│   │   │   ├── common/           # Shared base classes and interfaces
│   │   │   ├── manager/          # Login, main menu, toolbar, users, and score records
│   │   │   ├── persistence/      # Text-file storage for accounts, scores, and saves
│   │   │   ├── snake/            # Snake game package
│   │   │   │   ├── controller/   # Snake UI, keyboard input, and drawing
│   │   │   │   └── model/        # Snake movement, food, position, and game state
│   │   │   └── utils/            # Encryption, music, and style helpers
│   │   └── resources/
│   │       ├── audio/
│   │       │   ├── blackjack.mp3
│   │       │   └── snake.mp3
│   │       └── styles/app.css
│   └── test/                     # JUnit tests
├── run.ps1                       # Direct javac/java run script
├── test.ps1                      # Direct javac/JUnit test script
├── README.md
├── VIDEO_SCRIPT.md
└── .gitignore
```

### Package Organization
The three major components are organized as separate packages:
- `manager` for the Game Manager
- `blackjack` for Blackjack
- `snake` for Snake

Each game has its own self-contained package. File-related work is separated into `persistence`, and reusable helper code is separated into `utils`.

### Object-Oriented Design
- **Encapsulation:** model fields are private and changed through methods.
- **Inheritance:**
  - Blackjack uses `Player`, extended by `HumanPlayer`, `ComputerPlayer`, and `Dealer`.
  - Snake uses `GameEntity`, extended by `Snake` and `Food`.
- **Interfaces:**
  - Blackjack uses `AutoPlayer`, implemented by `ComputerPlayer` and `Dealer`.
  - Snake uses `Movable`, implemented by `Snake`.
- **Abstraction:** repeated setup, drawing, save/load, file storage, and round logic are separated into helper methods/classes.
- **Separation of logic and UI:** model classes hold the game state; controller classes handle JavaFX display and input.
- **Persistence classes:** accounts and high scores are handled in the `persistence` package. Blackjack save-state creation/loading is handled by the Blackjack game logic, and save-state encryption is handled by `CryptoUtils`.

## Installation Instructions

### Requirements
- Java 21 or newer
- JavaFX SDK 21
- PowerShell
- JUnit Platform Console Standalone jar in `lib/`

### JavaFX Setup
The scripts use direct Java commands.

We set a `JAVAFX_LIB` environment variable to your JavaFX SDK `lib` folder:

```powershell
$env:JAVAFX_LIB="C:\path\to\javafx-sdk-21\lib"
```

Or edit the first path in `run.ps1` and `test.ps1`.

### Run the Program
```powershell
.\run.ps1
```

### Run Tests
```powershell
.\test.ps1
```

## Usage

### Login / Create Account
1. Start the application.
2. Enter a username and password.
3. Click **Create Account** if the user does not exist.
4. Click **Log In**.

Accounts are saved in `user_accounts.txt`. Passwords are encrypted before they are written.

### Main Menu
After logging in, the main menu shows:
- Blackjack top 5 scores
- Snake top 5 scores
- Open Blackjack button
- Open Snake button
- Two future-game buttons
- A persistent toolbar with a **Main Menu** button

### Blackjack
1. Click **Open Blackjack**.
2. Click **Start New Game**.
3. Enter a bet and click **Start Round**.
4. Use **Hit** or **Stand** for the human player.
5. Computer players and the dealer act automatically.
6. Use **Save State** to generate an encrypted saveStateString.
7. Copy the saveStateString.
8. Return using the toolbar, reopen Blackjack, and paste the string into the load box to restore the exact game state.

### Snake
1. Click **Open Snake**.
2. Use arrow keys to move.
3. Press Escape (esc) to pause or resume.
4. Eat food to grow and increase score.
5. The game ends when the snake hits a wall or itself.
6. Press **R** or use the start button to restart after game over. An overlay displays your final score for each round.

## Contributions

| Member | Ownership Area |
|---|---|
| Suparn Posina (oops408) | Full Game Manager login/account flow, persistent high score functionality, menu/toolbar integrations, final testing/debugging, music/encryption checking, UI polishing, pause/game over overlays, final demo verification |
| David Aguiniga (MrHitaDavid3) | Created all the model code/classes, such as the player class and the BlackjackGame class, also created the initial files/setup the first iteration of the blackjack game files. |
| TBD | TBD |
| TBD | TBD |

## Video Link

**Submission Video:** ADD HERE

Explains key design decisions, describes areas of ownership, and demos Game Manager, Blackjack, Snake, persistence, save/load, high scores, music, encryption, and tests.

## Notes
- Make sure `user_accounts.txt` and `high_scores.txt` are created when running the app.
- Music from https://www.fesliyanstudios.com/royalty-free-music/downloads-c/8-bit-music/
- 8 Bit Surf - by David Renda: blackjack.mp3
- 8 Bit Menu - by David Renda (slower): snake.mp3
