import java.awt.*;
import javax.swing.*;

public class GameFrame extends JFrame {
    GameFrame() {
        JLabel scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Chalkboard", Font.BOLD, 25));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setForeground(Color.CYAN);
        scoreLabel.setOpaque(true);
        scoreLabel.setBackground(Color.BLACK);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GamePanel gamePanel = new GamePanel(scoreLabel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scoreLabel, BorderLayout.NORTH);
        mainPanel.add(gamePanel, BorderLayout.SOUTH);
        
        this.add(mainPanel);
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null); //center the window on the screen
    }
}