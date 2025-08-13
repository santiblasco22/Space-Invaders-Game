import java.awt.*;
import javax.swing.*;

public class Jugador extends Entidad {

    public void moverIzquierda() { if (x > 0) x -= 10; }
    public void moverDerecha(int max) { if (x + ancho + 10 <= max) x += 10; }
    public void moverArriba() { if (y > 0) y -= 10; }
    public void moverAbajo(int max) { if (y + alto < max) y += 10; }
    private Image imagenNave;

    public Jugador(int x, int y) {
        super(x, y, 80, 70, Color.GREEN);
        imagenNave = new ImageIcon(getClass().getResource("/naveJugador.png")).getImage();
    }

    @Override
    public void dibujar(Graphics g) {
        g.drawImage(imagenNave, x, y, ancho, alto, null);
    }
}
