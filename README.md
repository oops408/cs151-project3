# CS-151-06-Spring26 Project 3: Game Manager

## Overview
This project is a JavaFX Game Manager with two fully playable games: **Blackjack** and **Snake**. The program supports account creation, login, persistent high scores, a shared toolbar after login, different background music for each game, encrypted Blackjack save states, and encrypted account passwords.

The project intentionally uses basic Java, JavaFX, text files, and simple object-oriented design. It does **not** require Maven. The included PowerShell scripts compile the project directly with `javac` and run it with `java`.

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
- Unit tests cover important model and utility classes

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

### Package Organization Requirement
The three major components are organized as separate packages:
- `manager` for the Game Manager
- `blackjack` for Blackjack
- `snake` for Snake

Each game has its own self-contained package. File-related work is separated into `persistence`, and reusable helper code is separated into `utils`.

### Object-Oriented Design
- **Encapsulation:** model fields are private and changed through methods.
- **Inheritance:**
  - Blackjack uses abstract `BlackjackParticipant`, extended by `HumanPlayer`, `ComputerPlayer`, and `Dealer`.
  - Snake uses abstract `AbstractBoardEntity`, extended by `Food`.
- **Interfaces:**
  - Blackjack uses `HandValueStrategy`.
  - Snake uses `Movable`.
  - Shared controllers use `RenderableGame`.
- **Abstraction:** repeated setup, drawing, save/load, file storage, and round logic are separated into helper methods/classes.
- **Separation of logic and UI:** model classes hold the game state; controller classes handle JavaFX display and input.
- **Persistence classes:** accounts, high scores, and Blackjack save states are handled in the `persistence` package.

## Installation Instructions

### Requirements
- Java 21 or newer
- JavaFX SDK 21
- PowerShell
- JUnit Platform Console Standalone jar in `lib/`

### JavaFX Setup
The scripts use direct Java commands, not Maven.

The easiest option is to set a `JAVAFX_LIB` environment variable to your JavaFX SDK `lib` folder:

```powershell
$env:JAVAFX_LIB="C:\path\to\javafx-sdk-21\lib"
```

On the original development machine, the default path in the scripts already points to the local JavaFX SDK. A grader can either set `JAVAFX_LIB` or edit the first path in `run.ps1` and `test.ps1`.

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
3. Press Escape to pause or resume.
4. Eat food to grow and increase score.
5. The game ends when the snake hits a wall or itself.
6. Click **Restart** to play again.

## Contributions

| Member | Ownership Area |
|---|---|
| oops408 | Game Manager, login/account persistence, high score persistence, Blackjack model/save-load, Snake model/controller, JavaFX UI polish, tests, README, and final integration |

Commit history includes multiple focused commits for final fixes, such as direct Java script cleanup, Blackjack save/load improvements, Blackjack betting flow, and README updates.

## Video Link

**Submission video:** Add the final shared video link here before submitting.

The video should be under 25 minutes and visible to anyone with the link. Test the link in a private/incognito browser before submission.

## Notes for Final Submission
- Keep the repository private.
- Invite `telvinzhong` and `Shruthikatta`.
- Make sure the repository name or README clearly references `CS-151-06-Spring26`.
- Do not commit after the due date.
- Make sure `user_accounts.txt` and `high_scores.txt` are created when running the app.
- Make sure the final video link is added above before submitting.
