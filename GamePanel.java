import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {
    
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;

    int[] x = new int[GAME_UNITS];
    int[] y = new int[GAME_UNITS];
    int bodyParts = 3;
    int applesEaten = 0;
    int appleX, appleY;
    boolean running = false;
    char direction = 'R';
    Random random;
    Timer timer;

    private JLabel scoreLabel;
    
    JButton restartButton = new JButton("Restart 🌀");

    GamePanel(JLabel scoreLabel) {
        this.scoreLabel = scoreLabel;
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(0, 200, 0));
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        restartButton.setBounds((SCREEN_WIDTH - 150) / 2, 400, 150, 50);
        restartButton.setFocusable(false);
        restartButton.setFont(new Font("Comic Relief", Font.BOLD, 20));
        restartButton.setBackground(Color.CYAN);
        restartButton.setForeground(Color.BLUE);
        restartButton.setOpaque(true);
        restartButton.setContentAreaFilled(true);
        restartButton.setBorderPainted(false);
        restartButton.addActionListener(e -> startGame());

        this.setLayout(null);
        this.add(restartButton);
        
        random = new Random();
        startGame();
    }

    public void resetGame() {
        bodyParts = 3;
        applesEaten = 0;
        direction = 'R';
        scoreLabel.setText("Score: " + applesEaten);
        this.setBackground(new Color(0, 200, 0));

        for(int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }

        if(timer != null)   timer.stop();
    }

    public void startGame() {
        resetGame();

        restartButton.setVisible(false);
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();

        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if(running) {
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i < bodyParts; i++) {
                if(i == 0) {
                    g.setColor(new Color(0, 120, 200));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(Color.BLUE);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        } else
            gameOver(g);
    }

    public void newApple() {
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        if(direction == 'U')    y[0] = y[0] - UNIT_SIZE;
        else if(direction == 'D')   y[0] = y[0] + UNIT_SIZE;
        else if(direction == 'L')   x[0] = x[0] - UNIT_SIZE;
        else if(direction == 'R')   x[0] = x[0] + UNIT_SIZE;
    }

    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            scoreLabel.setText("Score: " + applesEaten);
            newApple();
        }
    }

    public void checkCollisions() {
        for(int i = bodyParts; i > 0; i--) {
            if((x[0] == x[i]) && (y[0] == y[i]))    running = false;
        }

        if(x[0] < 0)    running = false;    //if head touches left border
        if(x[0] >= SCREEN_WIDTH)    running = false; //if head touches right border
        if(y[0] < 0)    running = false;    //if head touches top border
        if(y[0] >= SCREEN_HEIGHT)   running = false; //if head touches bottom border

        if(!running)    timer.stop();
    }

    public void gameOver(Graphics g) {

        this.setBackground(Color.BLACK);

        //Score
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Chalkboard", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        String score = "Score: " + applesEaten;
        g.drawString(score, (SCREEN_WIDTH - metrics1.stringWidth(score)) / 2, g.getFont().getSize());

        //Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font("Chalkboard", Font.BOLD, 40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        String text = "Game Over !! 😢";
        g.drawString(text, (SCREEN_WIDTH - metrics2.stringWidth(text)) / 2, SCREEN_HEIGHT / 2);

        restartButton.setVisible(true);
    }

    @Override 
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override 
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R')    direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L')    direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D')    direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U')    direction = 'D';
                    break;
            }
        }
    }
}
