import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame {
    private static JFrame frame;

    public static void main(String[] args) {
        frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static class GamePanel extends JPanel implements ActionListener, KeyListener {
        // --- Teacher Prompts Logic ---
        private final int CELL_SIZE = 30;
        private int gridSize = 20; // Starts 20x20
        private final int HUD_HEIGHT = 50;
        private ArrayList<Point> snake;
        private int direction = 1; // 0:Up, 1:Right, 2:Down, 3:Left
        private Timer timer;

        // --- Student Prompts Logic ---
        private Point redFood;
        private Point whiteFood; // Changed from black for visibility
        private Point deathBlock;
        private Point portalA, portalB;
        private int score = 0;
        private int totalEaten = 0;
        private boolean gameOver = false;
        private Random rand = new Random();

        // --- NEW Dynamic Map Colors (Prompt 11) ---
        private Color originalMapColor = new Color(45, 45, 45);
        private Color currentMapColor = originalMapColor;
        private Color[] colorPalette = {
            new Color(45, 45, 45), // Original Gray
            new Color(55, 30, 75), // Deep Purple
            new Color(25, 75, 45), // Forest Green
            new Color(75, 55, 25), // Dark Gold
            new Color(25, 55, 75)  // Deep Blue
        };

        public GamePanel() {
            setFocusable(true);
            addKeyListener(this);
            initGame();
            timer = new Timer(150, this);
            timer.start();
        }

        private void initGame() {
            snake = new ArrayList<>();
            snake.add(new Point(10, 10));
            snake.add(new Point(10, 9));
            snake.add(new Point(10, 8));
            direction = 1;
            score = 0;
            totalEaten = 0;
            gameOver = false;
            currentMapColor = originalMapColor; // Reset color (Prompt 11)
            spawnAllObjects();
        }

        private void spawnAllObjects() {
            redFood = getRandomPoint();
            whiteFood = getRandomPoint();
            deathBlock = getDeathBlockPoint();
            portalA = getRandomPoint();
            portalB = getRandomPoint();
        }

        private Point getRandomPoint() {
            return new Point(rand.nextInt(gridSize), rand.nextInt(gridSize));
        }

        private Point getDeathBlockPoint() {
            Point p;
            do {
                p = getRandomPoint();
            } while (Math.abs(p.x - snake.get(0).x) + Math.abs(p.y - snake.get(0).y) < 3);
            return p;
        }

        // --- Color Changing Logic (Prompt 11) ---
        private void changeMapColor() {
            Color newColor;
            do {
                newColor = colorPalette[rand.nextInt(colorPalette.length)];
            } while (newColor.equals(currentMapColor)); // Don't pick same color twice
            currentMapColor = newColor;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(gridSize * CELL_SIZE, gridSize * CELL_SIZE + HUD_HEIGHT);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // HUD (Prompt 4)
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), HUD_HEIGHT);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Score: " + score + "  Multiplier: x" + (1 + totalEaten/10), 20, 30);

            // Game Area (Prompt 2 + Prompt 11 Color)
            g.translate(0, HUD_HEIGHT);
            g.setColor(currentMapColor);
            g.fillRect(0, 0, gridSize * CELL_SIZE, gridSize * CELL_SIZE);

            // Snake (Prompt 2)
            for (int i = 0; i < snake.size(); i++) {
                g.setColor(i == 0 ? Color.GREEN : new Color(0, 180, 0));
                Point p = snake.get(i);
                g.fillRect(p.y * CELL_SIZE, p.x * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }

            // Food (Prompt 4 & 7)
            g.setColor(Color.RED);
            g.fillOval(redFood.y * CELL_SIZE + 4, redFood.x * CELL_SIZE + 4, CELL_SIZE - 8, CELL_SIZE - 8);
            g.setColor(Color.WHITE);
            g.fillOval(whiteFood.y * CELL_SIZE + 4, whiteFood.x * CELL_SIZE + 4, CELL_SIZE - 8, CELL_SIZE - 8);

            // Death Block (Prompt 5)
            g.setColor(Color.ORANGE);
            g.fillRect(deathBlock.y * CELL_SIZE + 2, deathBlock.x * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);

            // Portals (Prompt 8)
            g.setColor(Color.CYAN);
            g.drawRect(portalA.y * CELL_SIZE + 2, portalA.x * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);
            g.drawRect(portalB.y * CELL_SIZE + 2, portalB.x * CELL_SIZE + 2, CELL_SIZE - 4, CELL_SIZE - 4);

            // Game Over (Prompt 4)
            if (gameOver) {
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, gridSize * CELL_SIZE, gridSize * CELL_SIZE);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                String msg = "GAME OVER: " + score;
                g.drawString(msg, (gridSize * CELL_SIZE)/2 - 100, (gridSize * CELL_SIZE)/2);
                g.setFont(new Font("Arial", Font.PLAIN, 15));
                g.drawString("Press 'R' to Restart", (gridSize * CELL_SIZE)/2 - 60, (gridSize * CELL_SIZE)/2 + 40);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameOver) {
                Point head = new Point(snake.get(0));
                if (direction == 0) head.x--;
                if (direction == 1) head.y++;
                if (direction == 2) head.x++;
                if (direction == 3) head.y--;

                // Portal Teleport (Prompt 8)
                if (head.equals(portalA)) head = new Point(portalB);
                else if (head.equals(portalB)) head = new Point(portalA);

                // Collision Detection (Prompt 4, 5, 6)
                if (head.x < 0 || head.x >= gridSize || head.y < 0 || head.y >= gridSize || 
                    snake.contains(head) || head.equals(deathBlock)) {
                    gameOver = true;
                    timer.stop();
                } else {
                    snake.add(0, head);
                    // Eating Logic (Prompt 7)
                    if (head.equals(redFood)) {
                        score += (1 + totalEaten / 10);
                        totalEaten++;
                        changeMapColor(); // Prompt 11
                        checkExpansion(); // Prompt 9
                        spawnAllObjects();
                    } else if (head.equals(whiteFood)) {
                        score = Math.max(0, score - 1);
                        if (snake.size() > 2) snake.remove(snake.size() - 1);
                        snake.remove(snake.size() - 1);
                        changeMapColor(); // Prompt 11
                        spawnAllObjects();
                    } else {
                        snake.remove(snake.size() - 1);
                    }
                }
            }
            repaint();
        }

        // Map Expansion (Prompt 9)
        private void checkExpansion() {
            if (score > 0 && score % 10 == 0) {
                gridSize += 2;
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                topFrame.pack();
                topFrame.setLocationRelativeTo(null);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            // Direction Controls (Prompt 3)
            if (key == KeyEvent.VK_UP && direction != 2) direction = 0;
            if (key == KeyEvent.VK_RIGHT && direction != 3) direction = 1;
            if (key == KeyEvent.VK_DOWN && direction != 0) direction = 2;
            if (key == KeyEvent.VK_LEFT && direction != 1) direction = 3;
            
            // Manual Reset (Prompt 6)
            if (key == KeyEvent.VK_R) {
                gridSize = 20;
                initGame();
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                topFrame.pack();
                topFrame.setLocationRelativeTo(null);
                timer.start();
            }
        }

        @Override public void keyTyped(KeyEvent e) {}
        @Override public void keyReleased(KeyEvent e) {}
    }
}