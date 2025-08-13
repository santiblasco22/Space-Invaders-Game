import java.awt.*;

public abstract class Entidad {
    protected int x, y, ancho, alto;
    protected Color color;

    public Entidad(int x, int y, int ancho, int alto, Color color) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.color = color;
    }

    public Rectangle getLimites() {
        int margen = 10;
        return new Rectangle(x + margen, y + margen, ancho - 2 * margen, alto - 2 * margen);
    }

    public abstract void dibujar(Graphics g);
}