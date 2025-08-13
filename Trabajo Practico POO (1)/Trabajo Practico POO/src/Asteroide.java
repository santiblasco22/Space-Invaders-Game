import javax.swing.*;
import java.awt.*;

public class Asteroide {
    int x, y, ancho, alto;
    int velocidad;
    private Image imagen;

    public Asteroide(int x, int y, int ancho, int alto, int velocidad, Image imagen) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.velocidad = velocidad;
        this.imagen = imagen;
    }

    public void mover() {
        y += velocidad;
    }

    public Rectangle getLimites() {
        int margenX = (int)(ancho * 0.2);
        int margenY = (int)(alto * 0.2);
        return new Rectangle(x + margenX, y + margenY, ancho - 2 * margenX, alto - 2 * margenY);
    }

    public void dibujar(Graphics g, JPanel panel) {
        if (imagen != null) {
            g.drawImage(imagen, x, y, ancho, alto, panel);
        } else {
            g.setColor(Color.GRAY);
            g.fillOval(x, y, ancho, alto);
        }
    }
}