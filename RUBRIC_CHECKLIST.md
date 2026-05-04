# Rubric Checklist

Total assignment value: 108 points.

## Game Manager - 15 points
- [x] Login screen with Log In and Create Account buttons
- [x] Usernames/passwords saved in `user_accounts.txt`
- [x] Passwords encrypted instead of plain text
- [x] Previous accounts persist after closing and reopening
- [x] High scores for both games stored in one `high_scores.txt`
- [x] Default score of 1000 for both games under default name
- [x] Main menu has visually separate high-score and launcher areas
- [x] Top 5 scores displayed highest to lowest
- [x] Blackjack button
- [x] Snake button
- [x] Two future-game buttons
- [x] Persistent toolbar after login
- [x] Toolbar has Main Menu button

## Blackjack - 25 points
- [x] Fully playable basic Blackjack
- [x] 1 human player
- [x] 2 automated players
- [x] 1 dealer
- [x] Betting before each round
- [x] Hit
- [x] Stand
- [x] Bust
- [x] Dealer hits on 17 or lower and stands otherwise
- [x] Computer players use different simple decision thresholds
- [x] Turn order: Human -> Bot 1 -> Bot 2 -> Dealer
- [x] Money updated after each round
- [x] Round reset keeps new balances
- [x] Save state string includes hands, turn, balances, bets, and deck order
- [x] Save state is encrypted
- [x] Load restores the exact state closely enough for uninterrupted play
- [x] Blackjack menu has new game and load game
- [x] UI shows whose turn it is
- [x] UI shows cards, balances, bets, and status messages
- [x] Dealer hidden card shown as hidden until round end
- [x] Mouse-only gameplay buttons

## Snake - 25 points
- [x] Arrow key movement
- [x] Continuous movement
- [x] Random starting direction
- [x] Starting square near center
- [x] Score updates in real time
- [x] Food randomly spawns
- [x] Food can spawn on edges
- [x] Snake grows after eating food
- [x] Wall collision game over
- [x] Self collision game over
- [x] Game-over overlay with final score
- [x] Restart option
- [x] Escape pauses/resumes
- [x] Pause stops movement
- [x] Board has visible border

## Object-Oriented Programming - 15 points
- [x] Inheritance in Blackjack
- [x] Interface in Blackjack
- [x] Inheritance in Snake
- [x] Interface in Snake
- [x] Private fields in model classes
- [x] Game state modified through methods
- [x] Logic separated from JavaFX UI
- [x] File-related code handled by persistence classes
- [x] Unit tests included for major logic classes

## Individual Contribution and GitHub - 20 points
- [ ] Private repository
- [ ] Invite instructors
- [ ] Repo name references `CS-151-06-Spring26`
- [ ] All members make visible commits
- [ ] README includes overview, design, installation, usage, contributions
- [ ] Video under 25 minutes
- [ ] Video link embedded in README

## Extra Credit / Additional Points - 8 points
- [x] Different local mp3 files included for both games
- [x] Encryption for passwords
- [x] Encryption for save states
- [x] At least two team members are different from Project 2 group
