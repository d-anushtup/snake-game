import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random; 

public class GamePanel extends JPanel implements ActionListener {
    
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;

    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS]; 
    int bodyParts = 6; 
    int applesEaten = 0; 
    int appleX, appleY; 
    char direction = 'R'; //R, L, U, D 
    boolean running = false; 
    Timer timer; 
    Random random; 

    JButton restartButton = new JButton("Restart :)");

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        
        restartButton.setBounds((SCREEN_WIDTH - 100)/2, 400, 150, 50);
        restartButton.setFocusable(false);
        restartButton.setFont(new Font("Comic Relief", Font.BOLD, 15));
        
        restartButton.setBackground(Color.CYAN);
        restartButton.setForeground(Color.BLUE);
        restartButton.setOpaque(true);
        restartButton.setContentAreaFilled(true);
        restartButton.setBorderPainted(false);
        restartButton.setFocusPainted(false);
        
        restartButton.addActionListener(e -> startGame());
        this.setLayout(null);
        this.add(restartButton);
        
        startGame();
    }

    public void resetGame() {
        bodyParts = 6; 
        applesEaten = 0; 
        direction = 'R';

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

            /*
            g.setColor(new Color(40, 40, 40));
            for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }
            */
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i < bodyParts; i++) {
                if(i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.cyan);
            g.setFont(new Font("Chalkboard", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            String score = "Score: " + applesEaten;
            g.drawString(score, (SCREEN_WIDTH - metrics.stringWidth(score)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        
        //move bodyparts: starting from the last
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {

        //check if head collides with body
        for(int i = bodyParts; i > 0; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        //check if head touches left border
        if(x[0] < 0)    running = false;

        //check if head touches right border 
        if(x[0] >= SCREEN_WIDTH) running = false;

        //check if head touches top border 
        if(y[0] < 0)    running = false;

        //check if head touches bottom border
        if(y[0] >= SCREEN_HEIGHT)    running = false;

        if(!running)    timer.stop();
    }

    public void gameOver(Graphics g) {
        
        //Score
        g.setColor(Color.yellow);
        g.setFont(new Font("Chalkboard", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        String score = "Score: " + applesEaten;
        g.drawString(score, (SCREEN_WIDTH - metrics1.stringWidth(score)) / 2, g.getFont().getSize());

        
        //Game Over Text
        String text = "Game Over !! :(";
        g.setColor(Color.red);
        g.setFont(new Font("Chalkboard", Font.PLAIN, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
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