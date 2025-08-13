import java.awt.*;
import javax.swing.*;

public class Boss extends Enemigo{
    private int direccion = 2;
    private int velocidadDisparo = 30;
    private int contadorDisparo = 0;
    private Image imagenBoss;

    public Boss(int x, int y, int nivel) {
        super(x, y, 100, 100, Color.PINK);
        this.vida = 10 + (nivel * 8);
        this.velocidadDisparo = Math.max(10, 40 - nivel * 2);
        imagenBoss = new ImageIcon(getClass().getResource("/monstruo.png")).getImage();
    }

    public void moverInteligente(int jugadorX, int anchoPanel) {
        int centroBoss = x + ancho / 2;
        if (centroBoss < jugadorX) {
            x += Math.min(direccion, jugadorX - centroBoss);
        } else if (centroBoss > jugadorX) {
            x -= Math.min(direccion, centroBoss - jugadorX);
        }
        if (x < 0) x = 0;
        if (x + ancho > anchoPanel) x = anchoPanel - ancho;
    }

    public boolean listoParaDisparar() {
        contadorDisparo++;
        if (contadorDisparo >= velocidadDisparo) {
            contadorDisparo = 0;
            return true;
        }
        return false;
    }

    @Override
    public void dibujar(Graphics g) {
        if (imagenBoss != null) {
            g.drawImage(imagenBoss, x, y, ancho, alto, null);
        } else {
            g.setColor(color);
            g.fillRect(x, y, ancho, alto);
        }
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Boss HP: " + vida, x + 10, y + alto / 2);
    }
}