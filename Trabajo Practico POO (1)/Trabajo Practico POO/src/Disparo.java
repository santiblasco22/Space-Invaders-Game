import java.awt.*;

public class Disparo implements Disparable {
    boolean esDelEnemigo;
    private int x, y, velocidad = 8;
    private final int ancho = 5, alto = 10;

    public Disparo(int x, int y) { this(x, y, false); }

    public Disparo(int x, int y, boolean esDelEnemigo) {
        this.x = x;
        this.y = y;
        this.esDelEnemigo = esDelEnemigo;
    }

    public void mover() { y += esDelEnemigo ? velocidad : -velocidad; }
    public Rectangle getLimites() { return new Rectangle(x, y, ancho, alto); }

    public void dibujar(Graphics g) {
        g.setColor(esDelEnemigo ? Color.RED : Color.YELLOW);
        g.fillRect(x, y, ancho, alto);
    }
}