import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class GamePanel extends JPanel implements ActionListener {

    static final int ScreenW = 600;
    static final int ScreenH = 600;
    static final int Unit = 25;
    static final int Game = (ScreenW * ScreenH) / (Unit * Unit); 
    static final int Delay = 75;

    final int x[] = new int[Game];
    final int y[] = new int[Game];

    int bodyParts = 6;
    int appleEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;

    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(ScreenW, ScreenH));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(Delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        super.paint(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, Unit, Unit);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillOval(x[i], y[i], Unit, Unit);
                } else {
                    g.setColor(new Color(45, 100, 0));
                    g.fillRect(x[i], y[i], Unit, Unit);  
                }
            }

            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont()); 
            g.drawString("Score " + appleEaten,
                    (ScreenW - metrics.stringWidth("Score " + appleEaten)) / 2,
                    g.getFont().getSize()); 
        } else {
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (ScreenW / Unit)) * Unit;
        appleY = random.nextInt((int) (ScreenH / Unit)) * Unit;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U' -> y[0] = y[0] - Unit;
            case 'D' -> y[0] = y[0] + Unit;
            case 'L' -> x[0] = x[0] - Unit;
            case 'R' -> x[0] = x[0] + Unit;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            appleEaten++;  
            newApple();
        }
    }

    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && y[0] == y[i]) {
                running = false;
            }
        }

        if (x[0] < 0 || x[0] >= ScreenW || y[0] < 0 || y[0] >= ScreenH) { 
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont()); 
        g.drawString("Score " + appleEaten,
                (ScreenW - metrics1.stringWidth("Score " + appleEaten)) / 2,
                g.getFont().getSize()); 

        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over",
                (ScreenW - metrics2.stringWidth("Game Over")) / 2,
                ScreenH / 2); 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') direction = 'D';
                    break;
            }
        }
    }
}
