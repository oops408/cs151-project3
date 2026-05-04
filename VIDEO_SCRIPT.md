# Project 3 Video Script Outline

Keep the video under 25 minutes. A good target is 12-18 minutes.

## 1. Introduction - 1 minute
- State project name: CS151 Game Manager.
- State that it includes a Game Manager, Blackjack, and Snake.
- Briefly mention persistent accounts, high scores, toolbar, music, and encryption.

## 2. Package Design - 2 minutes
Show the package tree.

Explain:
- `manager` controls login, main menu, toolbar, and score display.
- `blackjack` contains Blackjack classes.
- `snake` contains Snake classes.
- `persistence` handles files.
- `utils` contains reusable helpers like encryption and music.

## 3. Object-Oriented Design - 3 minutes
Show these examples:
- `BlackjackParticipant` is inherited by `HumanPlayer`, `ComputerPlayer`, and `Dealer`.
- `HandValueStrategy` is an interface used to calculate hand values.
- `AbstractBoardEntity` is inherited by `Food`.
- `Movable` is an interface used by Snake.
- Game logic classes do not contain JavaFX UI code.

## 4. Game Manager Demo - 3 minutes
Demo:
- Create account.
- Log in.
- Show top 5 scores.
- Show Blackjack, Snake, and future game buttons.
- Show persistent toolbar.

## 5. Blackjack Demo - 5 minutes
Demo:
- Start new game.
- Show human, two bots, and dealer.
- Show betting, hit, stand, bust/status messages.
- Show dealer hidden card.
- Finish a round and show balance update.
- Save state, copy string, load it back.
- Mention the save state is encrypted.

## 6. Snake Demo - 4 minutes
Demo:
- Arrow key movement.
- Food collection and growth.
- Score update.
- Escape pause/resume.
- Game over.
- Restart.
- High score recording.

## 7. Persistence and Encryption - 2 minutes
Show:
- `user_accounts.txt`
- `high_scores.txt`
- Save state text

Explain that passwords and Blackjack save states are not readable plain text. “The save string is encrypted, so it cannot be easily read or modified.”

## 8. Testing - 2 minutes
Show JUnit tests and run:

```bash
.\test.ps1
```

Mention tests for:
- Blackjack hand values
- Deck draw behavior
- Save/load state
- Snake movement/pause
- Encryption

## 9. Contributions - 1 minute
Each member explains their area of ownership.

## 10. Closing - 30 seconds
Restate that the project satisfies the main rubric items and uses a simple CS151-level design.
