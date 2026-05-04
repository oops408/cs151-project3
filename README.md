# CS-151-06-Spring26 Project 3: Game Manager

## Overview
This project is a JavaFX Game Manager with two fully playable games: **Blackjack** and **Snake**. The program supports account creation, login, persistent high scores, a shared toolbar after login, simple music, encrypted save states, and organized packages for object-oriented design.

The design intentionally stays readable for a CS151 Java programming course. The project avoids overly complex frameworks and keeps the main logic in plain Java classes.

## Main Features
- JavaFX login and account creation screen
- `user_accounts.txt` stores usernames with encrypted passwords
- `high_scores.txt` stores high scores for both Blackjack and Snake in one file
- Main menu shows top 5 scores for both games
- Main menu has buttons for Blackjack, Snake, and two disabled future-game buttons
- Toolbar remains visible after login on the main menu and both games
- Blackjack supports betting, hit, stand, bust, dealer rules, two computer players, save, and load
- Snake supports arrow-key movement, random direction, food, growth, score, pause, game over, and restart
- Two different local mp3 files are included under `src/main/resources/audio/`
- Save states and passwords are encrypted with a simple symmetric AES utility
- Unit tests cover important model and utility classes

#### Game Manager
- Login / Create Account (persistent via file)
- Toolbar accessible across all scenes
- Main menu with:
  - Top 5 high scores for both games
  - Launch options for Blackjack and Snake

#### Blackjack
- 1 human player, 2 AI players, 1 dealer
- Core rules:
  - Hit / Stand
  - Bust (>21)
  - Dealer hits on soft 17
- Turn-based gameplay
- Betting system with balances
- Save & Load system using encrypted save strings
- Visual card display and status messages

#### Snake
- Arrow key movement
- Continuous motion
- Food spawning (including edges)
- Snake grows with score increase
- Collision detection (wall and self)
- Pause / Resume (Escape key)
- Game over screen with restart
- Score persistence

#### Persistence
- `user_accounts.txt` stores usernames and encrypted passwords
- `high_scores.txt` stores scores for both games
- AES encryption used for:
  - Passwords
  - Blackjack save states

#### Testing
- JUnit 5 tests for:
  - Blackjack logic
  - Snake logic
  - Encryption utilities
- All tests pass using automated script

---

## Project Structure/Design
The project is split into small packages so each major part has a clear responsibility.

```text
cs151-project3/
├── lib/
│   └── junit-platform-console-standalone.jar   # JUnit 5 jar used to run tests
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── app/              # Entry point (launches JavaFX app)
│   │   │   ├── blackjack/        # Blackjack game
│   │   │   │   ├── controller/   # Handles UI + user interaction
│   │   │   │   └── model/        # Core game logic (cards, players, rules)
│   │   │   ├── common/           # Shared interfaces / base classes
│   │   │   ├── manager/          # Login, main menu, toolbar, high scores
│   │   │   ├── persistence/      # File I/O (accounts, scores, save states)
│   │   │   ├── snake/            # Snake game
│   │   │   │   ├── controller/   # Handles keyboard input + rendering
│   │   │   │   └── model/        # Core snake logic (movement, collisions)
│   │   │   └── utils/            # Helpers (encryption, audio, styles)
│   │   └── resources/
│   │       ├── audio/            # Game music files
│   │       │   ├── blackjack.mp3 # Blackjack background music
│   │       │   └── snake.mp3     # Snake background music
│   │       └── styles.css        # JavaFX UI styling
│   └── test/
│       ├── blackjack/            # Tests for Blackjack logic
│       │   └── model/
│       ├── persistence/          # Tests for encryption/file logic
│       └── snake/                # Tests for Snake logic
│           └── model/
├── run.ps1                       # Compile + run application
├── test.ps1                      # Compile + run all tests
├── README.md                     # Project documentation
├── VIDEO_SCRIPT.md               # Demo presentation script
└── .gitignore                    # Ignore build/output files
```

### Package Organization Requirement
The assignment asks how the three major components should be organized. This project uses:
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
  - Shared game controllers implement `RenderableGame`.
- **Separation of logic and UI:** game state classes do not depend on JavaFX.
- **Persistence classes:** accounts, high scores, and Blackjack save states are handled outside the game logic.

## Installation Instructions
### Requirements
- Java 21 or newer
- JavaFX SDK 21
- PowerShell
- JUnit Platform Console Standalone jar located in `lib/`

### Scripts
This project uses PowerShell scripts.

- `run.ps1` compiles the main source files, copies resources, and launches the JavaFX app.
- `test.ps1` compiles the main source files and test files, copies resources, and runs JUnit tests.

### Run the Program
```bash
.\run.ps1
```

### Run Tests
```bash
.\test.ps1
```

## Usage
### Login / Create Account
1. Start the application.
2. Enter a username and password.
3. Click **Create Account** if the user does not exist.
4. Click **Log In**.

Accounts are saved in `user_accounts.txt`.

### Main Menu
After logging in, the main menu shows:
- Blackjack top 5 scores
- Snake top 5 scores
- Open Blackjack button
- Open Snake button
- Two future-game buttons

### Blackjack
1. Click **Open Blackjack**.
2. Click **Start New Game**.
3. Use **Hit** or **Stand** for the human player.
4. Computer players and the dealer act automatically.
5. Use **Save State** to generate an encrypted saveStateString.
6. Copy the saveStateString.
7. Return to the Blackjack menu and paste the string into the load box to restore the game.

### Snake
1. Click **Open Snake**.
2. Use arrow keys to move.
3. Press Escape to pause or resume.
4. Eat food to grow and increase score.
5. The game ends when the snake hits a wall or itself.
6. Click **Restart** to play again.

## Contributions
Replace these with your real group members before submission.

| Member | Ownership Area |
|---|---|
| Student 1 | Game Manager, login, high scores |
| Student 2 | Blackjack model and save/load |
| Student 3 | Snake game and UI |
| Student 4 | Testing, README, polish, video |

All group members should make commits. Avoid one giant final commit. Use focused commits like:
- `Add account repository`
- `Implement Blackjack betting logic`
- `Add Snake pause and restart`
- `Add high score persistence`
- `Polish README and video script`

## Video Link
Paste your video link here before submitting:

`TODO: Add video link here`

Make sure the video is visible to anyone with the link. Test it in a private/incognito browser.

## Notes for Final Submission
- Keep the repository private.
- Invite `telvinzhong` and `Shruthikatta`.
- Make sure the repo name references `CS-151-06-Spring26`.
- Do not commit after the due date.
- Make sure `user_accounts.txt` and `high_scores.txt` are created when running the app.
- Keep the video under 25 minutes.
