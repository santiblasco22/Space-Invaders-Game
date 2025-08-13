import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Enemigo extends Entidad {
    protected int velocidad = 2;
    protected int vida = 1;
    private Image imagenEnemigo;
    private static final Random random = new Random();
    private int direccionX = random.nextBoolean() ? 1 : -1;
    private int contadorDisparo = 0;
    public int dx = 0;
    public int dy = 0;
    public boolean tieneDireccion = false;

    public Enemigo(int x, int y, int ancho, int alto, Color color) {
        super(x, y, ancho, alto, color);
        imagenEnemigo = new ImageIcon(getClass().getResource("/enemigos.png")).getImage();
        asignarDireccionAleatoria();
    }

    private void asignarDireccionAleatoria() {
        Random random = new Random();
        dx = random.nextBoolean() ? 1 : -1;
        dy = random.nextBoolean() ? 1 : -1;
        tieneDireccion = true;
    }

    public void mover(int anchoPanel) {
        y += velocidad;
        x += direccionX * 2;
        if (x <= 0 || x + ancho >= anchoPanel) {
            direccionX *= -1;
        }
    }

    public boolean listoParaDisparar() {
        contadorDisparo++;
        if (contadorDisparo > 60 + random.nextInt(60)) {
            contadorDisparo = 0;
            return true;
        }
        return false;
    }

    public boolean recibirDanio() {
        vida--;
        return vida <= 0;
    }

    @Override
    public void dibujar(Graphics g) {
        g.drawImage(imagenEnemigo, x, y, ancho, alto, null);
    }
}