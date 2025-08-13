import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SpaceInvadersGame extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Jugador jugador;
    private ArrayList<Enemigo> enemigos;
    private ArrayList<Disparo> disparos;
    private int puntaje = 0, nivel = 1, record = 0, vidas = 3;
    private boolean[] teclas;
    private boolean enJuego = false, gameOver = false, enPausa = false;
    private final Color[] coloresEnemigos = {Color.RED, Color.BLUE, Color.MAGENTA, Color.ORANGE, Color.CYAN};
    private final Random random = new Random();
    private boolean invulnerable = false;
    private int contadorInvulnerable = 0;
    private final int DURACION_INVULNERABILIDAD = 60;
    private Image fondoEspacio;
    private double fondoY = 0;
    private ArrayList<Explosion> explosiones = new ArrayList<>();
    private ArrayList<Asteroide> asteroides = new ArrayList<>();
    private Image imagenAsteroide;
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private Image imagenPowerUpEscudo;
    private Image auraEscudo;
    private boolean escudoActivo = false;
    private int tiempoEscudo = 0;
    private final int DURACION_ESCUDO = 180;

    public SpaceInvadersGame() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        teclas = new boolean[256];
        timer = new Timer(30, this);
        timer.start();
        fondoEspacio = new ImageIcon(getClass().getResource("/espacio.png")).getImage();
        imagenAsteroide = new ImageIcon(getClass().getResource("/asteroide.png")).getImage();
        imagenPowerUpEscudo = new ImageIcon(getClass().getResource("/aura.png")).getImage();
        auraEscudo = new ImageIcon(getClass().getResource("/aura.png")).getImage();
    }

    private void iniciarJuego() {
        jugador = new Jugador(375, 520);
        enemigos = new ArrayList<>();
        disparos = new ArrayList<>();
        puntaje = 0;
        nivel = 1;
        vidas = 3;
        crearEnemigos();
        enJuego = true;
        gameOver = false;
        crearAsteroides();
        escudoActivo = false;
        tiempoEscudo = 0;
        invulnerable = false;
        contadorInvulnerable = 0;
        powerUps.clear();
    }

    private void crearEnemigos() {
        enemigos.clear();
        if (nivel % 3 == 0) enemigos.add(new Boss(300, 100, nivel));
        else {
            int cantidad = switch (nivel) {
                case 1 -> 3;
                case 2 -> 5;
                case 4 -> 6;
                case 5 -> 7;
                case 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 -> 8;
                default -> nivel * 6;
            };
            for (int i = 0; i < cantidad; i++) {
                Color c = coloresEnemigos[random.nextInt(coloresEnemigos.length)];
                enemigos.add(new Enemigo(70 + (i % 10) * 70, 40 + (i / 10) * 60, 40, 40, c));
            }
        }
    }

    private void crearAsteroides() {
        asteroides.clear();
        int cantidad = nivel + 1;
        int velocidadBase = 2 + nivel;

        for (int i = 0; i < cantidad; i++) {
            int ancho = 20 + random.nextInt(40);
            int alto = ancho;
            int x = random.nextInt(getWidth() - ancho);
            int y = -random.nextInt(600);
            int velocidad = velocidadBase + random.nextInt(3);

            asteroides.add(new Asteroide(x, y, ancho, alto, velocidad, imagenAsteroide));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            g.drawImage(fondoEspacio, 0, (int)fondoY, getWidth(), getHeight(), this);
            g.drawImage(fondoEspacio, 0, (int)fondoY - getHeight(), getWidth(), getHeight(), this);
        } catch (Exception e) {
            System.out.println("Error al dibujar fondo: " + e.getMessage());
        }
        if (!enJuego) {
            g.setFont(new Font("Consolas", Font.BOLD, 36));
            g.setColor(Color.WHITE);
            g.drawString("Presiona ENTER para comenzar", 180, 300);
            return;
        }
        if (gameOver) {
            g.setFont(new Font("Consolas", Font.BOLD, 36));
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 290, 250);
            g.setFont(new Font("Consolas", Font.PLAIN, 20));
            g.setColor(Color.WHITE);
            g.drawString("Puntaje obtenido: " + puntaje, 310, 290);
            g.drawString("Record actual: " + record, 310, 320);
            g.drawString("Presiona ENTER para reiniciar", 260, 360);
            return;
        }
        g.setFont(new Font("Consolas", Font.BOLD, 20));
        g.setColor(Color.WHITE);
        g.drawString("Puntaje: " + puntaje + " | Nivel: " + nivel + " | Vidas: " + vidas + " | Record: " + record, 10, 20);
        if (!invulnerable || (contadorInvulnerable % 10 < 5)) {
            jugador.dibujar(g);
            if (escudoActivo && auraEscudo != null) {
                boolean mostrar = true;
                if (DURACION_ESCUDO - tiempoEscudo < 30) {
                    mostrar = (tiempoEscudo % 10 < 5);
                }
                if (mostrar) {
                    int auraX = jugador.x - 10;
                    int auraY = jugador.y - 10;
                    int auraAncho = jugador.ancho + 20;
                    int auraAlto = jugador.alto + 20;
                    g.drawImage(auraEscudo, auraX, auraY, auraAncho, auraAlto, this);
                }
            }
        }
        for (Enemigo enemigo : enemigos) enemigo.dibujar(g);
        for (Disparo disparo : disparos) disparo.dibujar(g);
        for (Asteroide asteroide : asteroides) {
            asteroide.dibujar(g, this);
        }
        for (PowerUp powerUp : powerUps) {
            powerUp.dibujar(g, this);
        }
        for (Explosion explosion : explosiones) {
            explosion.dibujar(g);
        }
        if (enPausa) {
            g.setFont(new Font("Consolas", Font.BOLD, 40));
            g.setColor(new Color(255, 255, 0, 200));
            g.drawString("PAUSA", getWidth() / 2 - 70, getHeight() / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (enPausa) return;
        if (invulnerable) {
            contadorInvulnerable++;
            if (contadorInvulnerable >= DURACION_INVULNERABILIDAD) {
                invulnerable = false;
                contadorInvulnerable = 0;
            }
        }
        if (!enJuego || gameOver) return;

        if (teclas[KeyEvent.VK_LEFT]) jugador.moverIzquierda();
        if (teclas[KeyEvent.VK_RIGHT]) jugador.moverDerecha(getWidth());
        if (teclas[KeyEvent.VK_UP]) jugador.moverArriba();
        if (teclas[KeyEvent.VK_DOWN]) jugador.moverAbajo(getHeight());

        for (Disparo disparo : new ArrayList<>(disparos)) {
            disparo.mover();
            if (disparo.getLimites().y < 0 || disparo.getLimites().y > getHeight()) disparos.remove(disparo);

            if (disparo.getLimites().intersects(jugador.getLimites()) && disparo.esDelEnemigo && !(invulnerable || escudoActivo)) {
                disparos.remove(disparo);
                vidas--;
                if (vidas > 0) {
                    invulnerable = true;
                    contadorInvulnerable = 0;
                } else {
                    gameOver = true;
                    if (puntaje > record) record = puntaje;
                }
            }
        }

        for (Enemigo enemigo : new ArrayList<>(enemigos)) {
            if (enemigo instanceof Boss boss) {
                boss.moverInteligente(jugador.x + jugador.ancho / 2, getWidth());
                if (boss.listoParaDisparar()) {
                    disparos.add(new Disparo(boss.x + boss.ancho / 2 - 2, boss.y + boss.alto, true));
                }
            } else {
                moverEnemigoEnZona(enemigo, getWidth());
            }

            if (enemigo.listoParaDisparar()) {
                disparos.add(new Disparo(enemigo.x + enemigo.ancho / 2 - 2, enemigo.y + enemigo.alto, true));
            }

            if (!(invulnerable || escudoActivo) && enemigo.getLimites().intersects(jugador.getLimites())) {
                vidas--;
                enemigos.remove(enemigo);
                if (vidas > 0) {
                    invulnerable = true;
                    contadorInvulnerable = 0;
                } else {
                    gameOver = true;
                    if (puntaje > record) record = puntaje;
                }
                break;
            }
        }

        for (Disparo disparo : new ArrayList<>(disparos)) {
            for (Enemigo enemigo : new ArrayList<>(enemigos)) {
                if (!disparo.esDelEnemigo && disparo.getLimites().intersects(enemigo.getLimites())) {
                    disparos.remove(disparo);
                    if (enemigo.recibirDanio()) {
                        enemigos.remove(enemigo);
                        puntaje++;
                        int ex = enemigo.x + enemigo.ancho / 2 - 25;
                        int ey = enemigo.y + enemigo.alto / 2 - 25;
                        explosiones.add(new Explosion(ex, ey, 80, 80));
                        if (enemigo instanceof Boss) {
                            vidas++;
                        }
                        if (random.nextDouble() < 0.3) {
                            int px = enemigo.x + enemigo.ancho / 2 - 15;
                            int py = enemigo.y + enemigo.alto / 2 - 15;
                            powerUps.add(new PowerUp(px, py, PowerUp.Tipo.ESCUDO, imagenPowerUpEscudo));
                        }
                    }
                    break;
                }
            }
        }

        if (enemigos.isEmpty()) {
            nivel++;
            crearEnemigos();
            crearAsteroides();
        }
        fondoY += 1 + nivel * 0.4;
        if (fondoY >= getHeight()) fondoY = 0;
        repaint();

        for (Explosion explosion : new ArrayList<>(explosiones)) {
            explosion.actualizar();
            if (!explosion.estaActiva()) {
                explosiones.remove(explosion);
            }
        }
        for (Asteroide asteroide : new ArrayList<>(asteroides)) {
            asteroide.mover();
            if (asteroide.y > getHeight()) {
                asteroide.y = -40;
                asteroide.x = random.nextInt(getWidth() - asteroide.ancho);
            }
            if (!(invulnerable || escudoActivo) && asteroide.getLimites().intersects(jugador.getLimites())) {
                vidas--;
                invulnerable = true;
                contadorInvulnerable = 0;
                asteroide.y = -40;
                asteroide.x = random.nextInt(getWidth() - asteroide.ancho);
                if (vidas <= 0) {
                    gameOver = true;
                    if (puntaje > record) record = puntaje;
                }
            }
        }
        for (PowerUp powerUp : new ArrayList<>(powerUps)) {
            powerUp.mover();
            if (powerUp.y > getHeight()) {
                powerUps.remove(powerUp);
                continue;
            }
            if (powerUp.getLimites().intersects(jugador.getLimites())) {
                if (powerUp.tipo == PowerUp.Tipo.ESCUDO) {
                    escudoActivo = true;
                    tiempoEscudo = 0;
                    invulnerable = true;
                    contadorInvulnerable = 0;
                }
                powerUps.remove(powerUp);
            }
        }
        if (escudoActivo) {
            tiempoEscudo++;
            if (tiempoEscudo >= DURACION_ESCUDO) {
                escudoActivo = false;
                invulnerable = false;
            }
        }
    }

    private void moverEnemigoEnZona(Enemigo enemigo, int limitePantalla) {
        int zonaSuperior = 0;
        int zonaInferior = getHeight() / 2;
        int velocidad = enemigo.velocidad;

        enemigo.x += enemigo.dx * velocidad;
        enemigo.y += enemigo.dy * velocidad;

        if (enemigo.x < 0) {
            enemigo.x = 0;
            enemigo.dx *= -1;
        } else if (enemigo.x + enemigo.ancho > limitePantalla) {
            enemigo.x = limitePantalla - enemigo.ancho;
            enemigo.dx *= -1;
        }

        if (enemigo.y < zonaSuperior) {
            enemigo.y = zonaSuperior;
            enemigo.dy *= -1;
        } else if (enemigo.y + enemigo.alto > zonaInferior) {
            enemigo.y = zonaInferior - enemigo.alto;
            enemigo.dy *= -1;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!enJuego && e.getKeyCode() == KeyEvent.VK_ENTER) {
            iniciarJuego();
        }
        if (e.getKeyCode() == KeyEvent.VK_P && enJuego && !gameOver) {
            enPausa = !enPausa;
            repaint();
            return;
        }
        if (enJuego && !gameOver) {
            teclas[e.getKeyCode()] = true;
            if (e.getKeyCode() == KeyEvent.VK_SPACE)
                disparos.add(new Disparo(jugador.x + jugador.ancho / 2 - 2, jugador.y));
        } else if (gameOver && e.getKeyCode() == KeyEvent.VK_ENTER) iniciarJuego();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (enJuego) teclas[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}