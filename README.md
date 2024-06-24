# Lawnlayer Project

**Lawnlayer** is an engaging and challenging game where players control a ball to convert dirt areas into grass while avoiding enemies. The game features various power-ups and a flood fill algorithm to dynamically update the game map.

## Features

- **Dynamic Map:** A 32x64 grid representing different elements such as concrete, grass, and trails.
- **Enemies:** Two types of enemies, Beetles and Worms, each inheriting from the Enemies class.
- **Power-Ups:** Two types of power-ups to enhance gameplay.
  - **Power-Up 1:** Slows down enemies for 10 seconds.
  - **Power-Up 2:** Allows the player to recover from enemy attacks.
- **Flood Fill Algorithm:** Converts dirt to grass dynamically as the player progresses.

## Class Inheritance

- **Enemies Class:** Beetle and Worm inherit from this class.
- **Sprite Class:** Both Enemies and Ball inherit from this class, allowing movement over specific elements.

## How to Run

### Prerequisites

- Java Development Kit (JDK) installed
- Gradle installed

### Installation

1. **Clone the repository:**
    ```bash
    git clone https://github.com/username/lawnlayer.git
    cd lawnlayer
    ```

2. **Build the project with Gradle:**
    ```bash
    ./gradlew build
    ```

3. **Run the game:**
    ```bash
    ./gradlew run
    ```

## Game Mechanics

### Array Representation

- `"X"` = Concrete
- `"O"` = Green Trail
- `"R"` = Red Trail
- `"G"` = Grass
- `"P1"` = Power Up 1
- `"P2"` = Power Up 2

### Power-Ups

- **Power-Up 1:** Slows down enemies for 10 seconds, spawns 10 seconds into the game, and stays for 10 seconds.
- **Power-Up 2:** Allows recovery from enemy attacks, spawns 15 seconds into the game, and stays for 15 seconds.

### Flood Fill Algorithm

When the player reaches grass or concrete from dirt, the path is turned to grass. The flood fill algorithm (DFS) then converts all connected dirt areas to grass, except areas occupied by enemies.

## Contributing

We welcome contributions! Please fork the repository and submit a pull request.

## License

This project is licensed under the MIT License.
