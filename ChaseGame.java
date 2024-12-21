
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChaseGame extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PLAYER_SIZE = 30;
    private static final int AI_SIZE = 30;
    private static final int OBSTACLE_SIZE = 40;
    private static final int INITIAL_AI_SPEED = 2;
    private static final int AI_SPEED_INCREASE_INTERVAL = 5000; // Speed increases every 5 seconds
    private static final int OBSTACLE_REMOVE_INTERVAL = 5000; // Remove obstacle every 5 seconds

    private int playerX = 100;
    private int playerY = 100;
    private List<Point> aiChasers = new ArrayList<>();
    private List<Rectangle> obstacles = new ArrayList<>();
    private Timer timer;
    private int score = 0;
    private int aiSpeed = INITIAL_AI_SPEED;
    private long lastSpeedIncreaseTime = System.currentTimeMillis();
    private long lastObstacleRemoveTime = System.currentTimeMillis();
    private Random rand = new Random();

    // Movement flags for multiple directions
    private boolean moveUp = false;
    private boolean moveDown = false;
    private boolean moveLeft = false;
    private boolean moveRight = false;

    public ChaseGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        // Initialize AI Chasers (3 chasers starting from random positions)
        for (int i = 0; i < 3; i++) {
            aiChasers.add(new Point(rand.nextInt(WIDTH - AI_SIZE), rand.nextInt(HEIGHT - AI_SIZE)));
        }

        // Create random obstacles
        createRandomObstacles(5);

        timer = new Timer(20, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw player
        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE);

        // Draw AI chasers
        g.setColor(Color.RED);
        for (Point ai : aiChasers) {
            g.fillRect(ai.x, ai.y, AI_SIZE, AI_SIZE);
        }

        // Draw obstacles
        g.setColor(Color.BLACK);
        for (Rectangle obstacle : obstacles) {
            g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }

        // Display score
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Move player based on movement flags
        if (moveUp && playerY > 0 && !isCollision(playerX, playerY - 5)) {
            playerY -= 5; // Move player up
        }
        if (moveDown && playerY < HEIGHT - PLAYER_SIZE && !isCollision(playerX, playerY + 5)) {
            playerY += 5; // Move player down
        }
        if (moveLeft && playerX > 0 && !isCollision(playerX - 5, playerY)) {
            playerX -= 5; // Move player left
        }
        if (moveRight && playerX < WIDTH - PLAYER_SIZE && !isCollision(playerX + 5, playerY)) {
            playerX += 5; // Move player right
        }

        // Move each AI chaser towards the player
        for (Point ai : aiChasers) {
            moveChaserTowardsPlayer(ai);
        }

        // Collision detection with AI chasers (game over condition)
        for (Point ai : aiChasers) {
            if (playerX < ai.x + AI_SIZE && playerX + PLAYER_SIZE > ai.x &&
                playerY < ai.y + AI_SIZE && playerY + PLAYER_SIZE > ai.y) {
                gameOver();
                return;
            }
        }

        // Increment score over time
        score++;

        // Check if it's time to increase AI speed
        if (System.currentTimeMillis() - lastSpeedIncreaseTime > AI_SPEED_INCREASE_INTERVAL) {
            aiSpeed++;
            lastSpeedIncreaseTime = System.currentTimeMillis();
        }

        // Randomly add obstacles
        if (score % 100 == 0) {
            createRandomObstacles(1);
        }

        // Randomly remove obstacles
        if (System.currentTimeMillis() - lastObstacleRemoveTime > OBSTACLE_REMOVE_INTERVAL) {
            removeRandomObstacle();
            lastObstacleRemoveTime = System.currentTimeMillis();
        }

        repaint();
    }

    private boolean isCollision(int nextX, int nextY) {
        // Check if the player's next position would result in a collision with any obstacle
        for (Rectangle obstacle : obstacles) {
            if (new Rectangle(nextX, nextY, PLAYER_SIZE, PLAYER_SIZE).intersects(obstacle)) {
                return true; // Collision detected
            }
        }
        return false;
    }

    private void moveChaserTowardsPlayer(Point ai) {
        // Calculate the next position based on the player's position
        int nextX = ai.x;
        int nextY = ai.y;

        // Move chaser towards the player on the X axis
        if (ai.x < playerX) {
            nextX += aiSpeed; // Move right
        } else if (ai.x > playerX) {
            nextX -= aiSpeed; // Move left
        }

        // Move chaser towards the player on the Y axis
        if (ai.y < playerY) {
            nextY += aiSpeed; // Move down
        } else if (ai.y > playerY) {
            nextY -= aiSpeed; // Move up
        }

        // Move the chaser freely through obstacles
        ai.x = nextX;
        ai.y = nextY;
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score);
        System.exit(0);
    }

    private void createRandomObstacles(int numObstacles) {
        for (int i = 0; i < numObstacles; i++) {
            int x = rand.nextInt(WIDTH - OBSTACLE_SIZE);
            int y = rand.nextInt(HEIGHT - OBSTACLE_SIZE);
            obstacles.add(new Rectangle(x, y, OBSTACLE_SIZE, OBSTACLE_SIZE));
        }
    }

    private void removeRandomObstacle() {
        if (!obstacles.isEmpty()) {
            int index = rand.nextInt(obstacles.size());
            obstacles.remove(index);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                moveUp = true;
                break;
            case KeyEvent.VK_DOWN:
                moveDown = true;
                break;
            case KeyEvent.VK_LEFT:
                moveLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
                moveRight = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                moveUp = false;
                break;
            case KeyEvent.VK_DOWN:
                moveDown = false;
                break;
            case KeyEvent.VK_LEFT:
                moveLeft = false;
                break;
            case KeyEvent.VK_RIGHT:
                moveRight = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chase Game");
        ChaseGame game = new ChaseGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }




}
 
