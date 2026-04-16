Teacher -ORGINAL PROMPTS-

prompt 1:I'm starting a Java game project in VS Code using Swing.
notes--Create a single file called SnakeGame.java with a main method that opens a JFrame window (600x600 pixels) titled "Snake". Add a JPanel subclass called GamePanel inside the frame. No game logic yet — just get the window to open.

prompt 2:Add a 20x20 grid to GamePanel.
notes-- Represent the snake as a sequence of grid cells and start it with 3 segments near the center, facing right. Each cell should be drawn as a 30x30 pixel square. Draw the snake in green and the background in dark gray.

prompt 3:Make the snake move automatically using a Swing timer that ticks every 150 milliseconds —
notes--- the snake should advance one cell per tick in its current direction. Add arrow key controls so the player can steer, but don't allow the snake to reverse direction. For now, have the snake wrap around the edges instead of dying. Make sure the panel can receive keyboard input.

prompt 4:Add a food pellet that spawns at a random empty cell.
notes--When the snake eats it, grow by one segment and spawn new food. Add collision detection: hitting a wall or the snake's own body should end the game, stop movement, and show a "Game Over" message with the final score in the center of the screen. Display the current score in the top-left corner during play. When the game is over, let the player press R to reset everything and play again.

STUDENT -MY PROMPTS-

Prompt 5: The Death Block
Add a still 'Death Block' witht he color organge that spawns randomly.
Dev Note: I added a check to make sure this doesn't spawn right on top of the snake's head at the start, otherwise the player dies instantly before they can move.

Prompt 6: Hardcore Mode & Manual Reset
Change the game so touching the border or self results in Game Over. Also, make the 'R' key work at any time, not just when the game is over.

Prompt 7: Red & White Fruit (The Multiplier)
Add two types of food. Red = +1 point and growth. White = -1 point and shrinks the snake.
Dev Note: I had to fix a logic bug here—if the snake eats too many white fruits and shrinks below 2 segments, the game logic breaks. I added a "floor" so it never goes below 2 floors if that makes sense. I also added a multiplier that increases the points you get from Red fruit every 10 successful eats.

Prompt 8: Portals
Add two blue Portal blocks. Entering one teleports the snake to the other.
Dev Note: I made sure the portals don't spawn on top of the food or the Death Block so the player doesn't get "teleport-killed" unfairly.

Prompt 9: Map Expansion
Every 10 points, increase the grid size by 2. The window should resize automatically.
Dev Note (The "Logic Fix"): This was the hardest part. Originally, the food stopped spawning because the game was only checking for exactly 10, 20, or 30 points. If the multiplier skipped those numbers, the food wouldn't respawn. I fixed this by making sure spawnAllObjects() runs every single time food is eaten, regardless of whether the map expands. I also used frame.pack() so the window border doesn't look "glitchy" after it grows.

Prompt 10: Visual Feedback
Every time the snake eats, pick a random background color from a list (Deep Purple, Forest Green, etc.) to make the "level up" feel more rewarding.
