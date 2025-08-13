import javax.swing.*;
import java.awt.*;

public class PowerUp {
    public enum Tipo { ESCUDO }

    int x, y, ancho, alto, velocidad;
    Tipo tipo;
    private Image imagen;

    public PowerUp(int x, int y, Tipo tipo, Image imagen) {
        this.x = x;
        this.y = y;
        this.ancho = 30;
        this.alto = 30;
        this.velocidad = 3;
        this.tipo = tipo;
        this.imagen = imagen;
    }

    public void mover() {
        y += velocidad;
    }

    public Rectangle getLimites() {
        return new Rectangle(x, y, ancho, alto);
    }

    public void dibujar(Graphics g, JPanel panel) {
        if (imagen != null)
            g.drawImage(imagen, x, y, ancho, alto, panel);
        else {
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, ancho, alto);
        }
    }
}