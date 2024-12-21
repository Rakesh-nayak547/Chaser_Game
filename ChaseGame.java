/*import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ChaseGame extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PLAYER_SIZE = 30;
    private static final int AI_SIZE = 30;
    private int playerX = 100;
    private int playerY = 100;
    private int aiX = 400;
    private int aiY = 300;
    private Timer timer;
    private int score = 0;

    public ChaseGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        timer = new Timer(20, this); // Timer set to update every 20 milliseconds
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw player and AI characters
        g.setColor(Color.BLUE);
        g.fillRect(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE); // Player rectangle

        g.setColor(Color.RED);
        g.fillRect(aiX, aiY, AI_SIZE, AI_SIZE); // AI rectangle

        // Display the score at the top left
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Move AI towards player
        if (aiX < playerX) {
            aiX += 1; // Move right
        } else if (aiX > playerX) {
            aiX -= 1; // Move left
        }

        if (aiY < playerY) {
            aiY += 1; // Move down
        } else if (aiY > playerY) {
            aiY -= 1; // Move up
        }

        // Collision detection
        if (playerX < aiX + AI_SIZE && playerX + PLAYER_SIZE > aiX &&
            playerY < aiY + AI_SIZE && playerY + PLAYER_SIZE > aiY) {
            timer.stop(); // Stop the game
            JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score);
            System.exit(0); // Exit the game
        }

        score++; // Increment score as time goes on
        repaint(); // Repaint the screen with updated positions
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Player movement with arrow keys
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (playerY > 0) {
                    playerY -= 5; // Move player up
                }
                break;
            case KeyEvent.VK_DOWN:
                if (playerY < HEIGHT - PLAYER_SIZE) {
                    playerY += 5; // Move player down
                }
                break;
            case KeyEvent.VK_LEFT:
                if (playerX > 0) {
                    playerX -= 5; // Move player left
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (playerX < WIDTH - PLAYER_SIZE) {
                    playerX += 5; // Move player right
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

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
    }//single chaser with no obstacle
    
}

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
    
    private int playerX = 100;
    private int playerY = 100;
    private List<Point> aiChasers = new ArrayList<>();
    private List<Rectangle> obstacles = new ArrayList<>();
    private Timer timer;
    private int score = 0;
    
    public ChaseGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
        
        // Initialize AI Chasers (let's add 3 chasers for now)
        for (int i = 0; i < 3; i++) {
            aiChasers.add(new Point(WIDTH / 2 + i * 50, HEIGHT / 2 + i * 50));
        }

        // Create random obstacles
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            int x = rand.nextInt(WIDTH - OBSTACLE_SIZE);
            int y = rand.nextInt(HEIGHT - OBSTACLE_SIZE);
            obstacles.add(new Rectangle(x, y, OBSTACLE_SIZE, OBSTACLE_SIZE));
        }
        
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
        // Move each AI chaser towards the player
        for (Point ai : aiChasers) {
            if (ai.x < playerX) {
                ai.x += 1;
            } else if (ai.x > playerX) {
                ai.x -= 1;
            }

            if (ai.y < playerY) {
                ai.y += 1;
            } else if (ai.y > playerY) {
                ai.y -= 1;
            }
        }

        // Collision detection with AI chasers
        for (Point ai : aiChasers) {
            if (playerX < ai.x + AI_SIZE && playerX + PLAYER_SIZE > ai.x &&
                playerY < ai.y + AI_SIZE && playerY + PLAYER_SIZE > ai.y) {
                gameOver();
                return;
            }
        }

        // Collision detection with obstacles
        for (Rectangle obstacle : obstacles) {
            if (obstacle.intersects(new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE))) {
                gameOver();
                return;
            }
        }

        // Increment score over time
        score++;
        repaint();
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score);
        System.exit(0);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (playerY > 0) {
                    playerY -= 5; // Move player up
                }
                break;
            case KeyEvent.VK_DOWN:
                if (playerY < HEIGHT - PLAYER_SIZE) {
                    playerY += 5; // Move player down
                }
                break;
            case KeyEvent.VK_LEFT:
                if (playerX > 0) {
                    playerX -= 5; // Move player left
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (playerX < WIDTH - PLAYER_SIZE) {
                    playerX += 5; // Move player right
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

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
}// multiple chaser  with  obstacle
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
    private static final int INITIAL_AI_SPEED = 1;
    private static final int AI_SPEED_INCREASE_INTERVAL = 5000; // Speed increases every 5 seconds
    
    private int playerX = 100;
    private int playerY = 100;
    private List<Point> aiChasers = new ArrayList<>();
    private List<Rectangle> obstacles = new ArrayList<>();
    private Timer timer;
    private int score = 0;
    private int aiSpeed = INITIAL_AI_SPEED;
    private long lastSpeedIncreaseTime = System.currentTimeMillis();
    
    public ChaseGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        // Initialize AI Chasers (let's add 3 chasers for now)
        for (int i = 0; i < 3; i++) {
            aiChasers.add(new Point(WIDTH / 2 + i * 50, HEIGHT / 2 + i * 50));
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
        // Move each AI chaser towards the player
        for (Point ai : aiChasers) {
            if (ai.x < playerX) {
                ai.x += aiSpeed;
            } else if (ai.x > playerX) {
                ai.x -= aiSpeed;
            }

            if (ai.y < playerY) {
                ai.y += aiSpeed;
            } else if (ai.y > playerY) {
                ai.y -= aiSpeed;
            }
        }

        // Collision detection with AI chasers
        for (Point ai : aiChasers) {
            if (playerX < ai.x + AI_SIZE && playerX + PLAYER_SIZE > ai.x &&
                playerY < ai.y + AI_SIZE && playerY + PLAYER_SIZE > ai.y) {
                gameOver();
                return;
            }
        }

        // Collision detection with obstacles
        for (Rectangle obstacle : obstacles) {
            if (obstacle.intersects(new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE))) {
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

        repaint();
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score);
        System.exit(0);
    }

    private void createRandomObstacles(int numObstacles) {
        Random rand = new Random();
        for (int i = 0; i < numObstacles; i++) {
            int x = rand.nextInt(WIDTH - OBSTACLE_SIZE);
            int y = rand.nextInt(HEIGHT - OBSTACLE_SIZE);
            obstacles.add(new Rectangle(x, y, OBSTACLE_SIZE, OBSTACLE_SIZE));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (playerY > 0) {
                    playerY -= 5; // Move player up
                }
                break;
            case KeyEvent.VK_DOWN:
                if (playerY < HEIGHT - PLAYER_SIZE) {
                    playerY += 5; // Move player down
                }
                break;
            case KeyEvent.VK_LEFT:
                if (playerX > 0) {
                    playerX -= 5; // Move player left
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (playerX < WIDTH - PLAYER_SIZE) {
                    playerX += 5; // Move player right
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

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
    }//multiple chaser gradually speed up
}
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
    private static final int INITIAL_AI_SPEED = 1;
    private static final int AI_SPEED_INCREASE_INTERVAL = 5000; // Speed increases every 5 seconds
    
    private int playerX = 100;
    private int playerY = 100;
    private List<Point> aiChasers = new ArrayList<>();
    private List<Rectangle> obstacles = new ArrayList<>();
    private Timer timer;
    private int score = 0;
    private int aiSpeed = INITIAL_AI_SPEED;
    private long lastSpeedIncreaseTime = System.currentTimeMillis();
    
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

        // Initialize AI Chasers (let's add 3 chasers for now)
        for (int i = 0; i < 3; i++) {
            aiChasers.add(new Point(WIDTH / 2 + i * 50, HEIGHT / 2 + i * 50));
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
        if (moveUp && playerY > 0) {
            playerY -= 5; // Move player up
        }
        if (moveDown && playerY < HEIGHT - PLAYER_SIZE) {
            playerY += 5; // Move player down
        }
        if (moveLeft && playerX > 0) {
            playerX -= 5; // Move player left
        }
        if (moveRight && playerX < WIDTH - PLAYER_SIZE) {
            playerX += 5; // Move player right
        }

        // Move each AI chaser towards the player with obstacle collision detection
        for (Point ai : aiChasers) {
            if (ai.x < playerX && !willCollide(ai.x + aiSpeed, ai.y)) {
                ai.x += aiSpeed;
            } else if (ai.x > playerX && !willCollide(ai.x - aiSpeed, ai.y)) {
                ai.x -= aiSpeed;
            }

            if (ai.y < playerY && !willCollide(ai.x, ai.y + aiSpeed)) {
                ai.y += aiSpeed;
            } else if (ai.y > playerY && !willCollide(ai.x, ai.y - aiSpeed)) {
                ai.y -= aiSpeed;
            }
        }

        // Collision detection with AI chasers
        for (Point ai : aiChasers) {
            if (playerX < ai.x + AI_SIZE && playerX + PLAYER_SIZE > ai.x &&
                playerY < ai.y + AI_SIZE && playerY + PLAYER_SIZE > ai.y) {
                gameOver();
                return;
            }
        }

        // Collision detection with obstacles
        for (Rectangle obstacle : obstacles) {
            if (obstacle.intersects(new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE))) {
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

        repaint();
    }

    private boolean willCollide(int x, int y) {
        Rectangle nextPosition = new Rectangle(x, y, AI_SIZE, AI_SIZE);
        for (Rectangle obstacle : obstacles) {
            if (nextPosition.intersects(obstacle)) {
                return true; // Collision detected with obstacle
            }
        }
        return false; // No collision detected
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Your score: " + score);
        System.exit(0);
    }

    private void createRandomObstacles(int numObstacles) {
        Random rand = new Random();
        for (int i = 0; i < numObstacles; i++) {
            int x = rand.nextInt(WIDTH - OBSTACLE_SIZE);
            int y = rand.nextInt(HEIGHT - OBSTACLE_SIZE);
            obstacles.add(new Rectangle(x, y, OBSTACLE_SIZE, OBSTACLE_SIZE));
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
    }//chaser may freeze in random position
}
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
    private static final int INITIAL_AI_SPEED = 1;
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
        if (moveUp && playerY > 0) {
            playerY -= 5; // Move player up
        }
        if (moveDown && playerY < HEIGHT - PLAYER_SIZE) {
            playerY += 5; // Move player down
        }
        if (moveLeft && playerX > 0) {
            playerX -= 5; // Move player left
        }
        if (moveRight && playerX < WIDTH - PLAYER_SIZE) {
            playerX += 5; // Move player right
        }

        // Move each AI chaser randomly
        for (Point ai : aiChasers) {
            moveRandomly(ai);
        }

        // Collision detection with AI chasers
        for (Point ai : aiChasers) {
            if (playerX < ai.x + AI_SIZE && playerX + PLAYER_SIZE > ai.x &&
                playerY < ai.y + AI_SIZE && playerY + PLAYER_SIZE > ai.y) {
                gameOver();
                return;
            }
        }

        // Collision detection with obstacles
        for (Rectangle obstacle : obstacles) {
            if (obstacle.intersects(new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE))) {
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

    private void moveRandomly(Point ai) {
        int direction = rand.nextInt(4); // Randomly choose a direction
        switch (direction) {
            case 0: // Move up
                if (ai.y > 0 && !willCollide(ai.x, ai.y - aiSpeed)) {
                    ai.y -= aiSpeed;
                }
                break;
            case 1: // Move down
                if (ai.y < HEIGHT - AI_SIZE && !willCollide(ai.x, ai.y + aiSpeed)) {
                    ai.y += aiSpeed;
                }
                break;
            case 2: // Move left
                if (ai.x > 0 && !willCollide(ai.x - aiSpeed, ai.y)) {
                    ai.x -= aiSpeed;
                }
                break;
            case 3: // Move right
                if (ai.x < WIDTH - AI_SIZE && !willCollide(ai.x + aiSpeed, ai.y)) {
                    ai.x += aiSpeed;
                }
                break;
        }
    }

    private boolean willCollide(int x, int y) {
        Rectangle nextPosition = new Rectangle(x, y, AI_SIZE, AI_SIZE);
        for (Rectangle obstacle : obstacles) {
            if (nextPosition.intersects(obstacle)) {
                return true; // Collision detected with obstacle
            }
        }
        return false; // No collision detected
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
    }// chaser cannot move here only vibrate
    
}
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
    private static final int INITIAL_AI_SPEED = 1;
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

    // Store directions for each chaser
    private int[] chaserDirectionX;
    private int[] chaserDirectionY;

    public ChaseGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        // Initialize AI Chasers (3 chasers starting from random positions)
        for (int i = 0; i < 3; i++) {
            aiChasers.add(new Point(rand.nextInt(WIDTH - AI_SIZE), rand.nextInt(HEIGHT - AI_SIZE)));
        }

        // Store random directions for each chaser
        chaserDirectionX = new int[aiChasers.size()];
        chaserDirectionY = new int[aiChasers.size()];
        for (int i = 0; i < aiChasers.size(); i++) {
            setRandomDirection(i);
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
        if (moveUp && playerY > 0) {
            playerY -= 5; // Move player up
        }
        if (moveDown && playerY < HEIGHT - PLAYER_SIZE) {
            playerY += 5; // Move player down
        }
        if (moveLeft && playerX > 0) {
            playerX -= 5; // Move player left
        }
        if (moveRight && playerX < WIDTH - PLAYER_SIZE) {
            playerX += 5; // Move player right
        }

        // Move each AI chaser randomly
        for (int i = 0; i < aiChasers.size(); i++) {
            moveRandomly(i);
        }

        // Collision detection with AI chasers
        for (Point ai : aiChasers) {
            if (playerX < ai.x + AI_SIZE && playerX + PLAYER_SIZE > ai.x &&
                playerY < ai.y + AI_SIZE && playerY + PLAYER_SIZE > ai.y) {
                gameOver();
                return;
            }
        }

        // Collision detection with obstacles
        for (Rectangle obstacle : obstacles) {
            if (obstacle.intersects(new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE))) {
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

    private void moveRandomly(int chaserIndex) {
        Point ai = aiChasers.get(chaserIndex);

        // Move the chaser in the set random direction
        ai.x += chaserDirectionX[chaserIndex] * aiSpeed;
        ai.y += chaserDirectionY[chaserIndex] * aiSpeed;

        // If the chaser hits the screen edge or an obstacle, change direction
        if (ai.x <= 0 || ai.x >= WIDTH - AI_SIZE || willCollide(ai.x, ai.y)) {
            chaserDirectionX[chaserIndex] = rand.nextInt(3) - 1; // -1, 0, or 1
        }
        if (ai.y <= 0 || ai.y >= HEIGHT - AI_SIZE || willCollide(ai.x, ai.y)) {
            chaserDirectionY[chaserIndex] = rand.nextInt(3) - 1; // -1, 0, or 1
        }
    }

    private boolean willCollide(int x, int y) {
        Rectangle nextPosition = new Rectangle(x, y, AI_SIZE, AI_SIZE);
        for (Rectangle obstacle : obstacles) {
            if (nextPosition.intersects(obstacle)) {
                return true; // Collision detected with obstacle
            }
        }
        return false; // No collision detected
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

    private void setRandomDirection(int chaserIndex) {
        chaserDirectionX[chaserIndex] = rand.nextInt(3) - 1; // -1, 0, or 1
        chaserDirectionY[chaserIndex] = rand.nextInt(3) - 1; // -1, 0, or 1
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
    }//chaser does not chase the player
}
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
        if (moveUp && playerY > 0) {
            playerY -= 5; // Move player up
        }
        if (moveDown && playerY < HEIGHT - PLAYER_SIZE) {
            playerY += 5; // Move player down
        }
        if (moveLeft && playerX > 0) {
            playerX -= 5; // Move player left
        }
        if (moveRight && playerX < WIDTH - PLAYER_SIZE) {
            playerX += 5; // Move player right
        }

        // Move each AI chaser towards the player
        for (Point ai : aiChasers) {
            moveChaserTowardsPlayer(ai);
        }

        // Collision detection with AI chasers
        for (Point ai : aiChasers) {
            if (playerX < ai.x + AI_SIZE && playerX + PLAYER_SIZE > ai.x &&
                playerY < ai.y + AI_SIZE && playerY + PLAYER_SIZE > ai.y) {
                gameOver();
                return;
            }
        }

        // Collision detection with obstacles
        for (Rectangle obstacle : obstacles) {
            if (obstacle.intersects(new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE))) {
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

    private void moveChaserTowardsPlayer(Point ai) {
        // Move chaser towards the player's position
        if (ai.x < playerX) {
            ai.x += aiSpeed; // Move right
        } else if (ai.x > playerX) {
            ai.x -= aiSpeed; // Move left
        }

        if (ai.y < playerY) {
            ai.y += aiSpeed; // Move down
        } else if (ai.y > playerY) {
            ai.y -= aiSpeed; // Move up
        }

        // Prevent chasers from moving through obstacles
        if (willCollide(ai.x, ai.y)) {
            // Change direction if a collision is detected
            ai.x -= aiSpeed;
            ai.y -= aiSpeed;
        }
    }

    private boolean willCollide(int x, int y) {
        Rectangle nextPosition = new Rectangle(x, y, AI_SIZE, AI_SIZE);
        for (Rectangle obstacle : obstacles) {
            if (nextPosition.intersects(obstacle)) {
                return true; // Collision detected with obstacle
            }
        }
        return false; // No collision detected
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
    }// here chaser cannot go through the obstacle
}
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
        if (moveUp && playerY > 0) {
            playerY -= 5; // Move player up
        }
        if (moveDown && playerY < HEIGHT - PLAYER_SIZE) {
            playerY += 5; // Move player down
        }
        if (moveLeft && playerX > 0) {
            playerX -= 5; // Move player left
        }
        if (moveRight && playerX < WIDTH - PLAYER_SIZE) {
            playerX += 5; // Move player right
        }

        // Move each AI chaser towards the player
        for (Point ai : aiChasers) {
            moveChaserTowardsPlayer(ai);
        }

        // Collision detection with AI chasers
        for (Point ai : aiChasers) {
            if (playerX < ai.x + AI_SIZE && playerX + PLAYER_SIZE > ai.x &&
                playerY < ai.y + AI_SIZE && playerY + PLAYER_SIZE > ai.y) {
                gameOver();
                return;
            }
        }

        // Collision detection with obstacles (only for player)
        for (Rectangle obstacle : obstacles) {
            if (obstacle.intersects(new Rectangle(playerX, playerY, PLAYER_SIZE, PLAYER_SIZE))) {
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
    }// here game  is almost complete
    
}  */

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
 