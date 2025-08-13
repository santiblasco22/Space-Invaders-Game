import java.awt.*;

public interface Disparable {
    void mover();
    void dibujar(Graphics g);
    Rectangle getLimites();
}
