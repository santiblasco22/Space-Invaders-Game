import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders");
        SpaceInvadersGame juego = new SpaceInvadersGame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(juego);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}