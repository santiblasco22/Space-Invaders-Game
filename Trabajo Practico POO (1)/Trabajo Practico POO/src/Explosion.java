import java.awt.*;
import javax.swing.*;

public class Explosion {
    private int x, y, ancho, alto, duracion;
    private int contador = 0;
    private static final Image imagenExplosion;

    static {
        imagenExplosion = new ImageIcon(Explosion.class.getResource("/explosion.png")).getImage();
    }

    public Explosion(int x, int y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.duracion = 15;
    }

    public boolean estaActiva() {
        return contador < duracion;
    }

    public void actualizar() {
        contador++;
    }

    public void dibujar(Graphics g) {
        if (estaActiva()) {
            g.drawImage(imagenExplosion, x, y, ancho, alto, null);
        }
    }
}