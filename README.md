# CS-151 Spring 2026 Project 3  
Game Manager with Blackjack and Snake 

---

## Overview
This project implements a game manager system with two playable games: Blackjack and Snake. The application is built using Java and JavaFX and follows object-oriented design principles. It supports user login, persistent storage, and high score tracking across sessions.

---

## Design and Structure
This project implements a game manager system with two playable games: Blackjack and Snake. The application is built using Java and JavaFX and follows object-oriented design principles. It supports user login, persistent storage, and high score tracking across sessions.

src/
├── manager/        # login, launcher, toolbar
├── blackjack/      # blackjack game logic, UI, controller
├── snake/          # snake game logic, UI, controller
├── utils/          # file handling and helpers
└── test/           # unit tests

Key design points:
- Separation of logic, UI, and control
- Modular structure for each game
- File operations handled outside core logic

---

## Features

### Game Manager


### Blackjack
 

### Snake


---

## Object-Oriented Design
- Inheritance and interfaces used in both games  
- Encapsulation: all model fields are private  
- Abstraction: controllers manage game flow  
- Game logic separated from UI  

---

## Testing
Unit tests cover core logic and key components.

---

## Installation

**Requirements:**
- Java 17 or higher  
- JavaFX SDK  

**Steps:**
```bash
git clone <repo-link>
cd <repo-name>

