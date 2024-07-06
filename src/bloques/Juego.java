package bloques;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Juego extends JFrame implements KeyListener, Runnable {

    private JPanel fondo;
    private static JLabel labelPuntaje;
    private static JLabel labelTiempo;
    private static JLabel labelBolas;
    private int puntaje;
    private int bolasDisponibles;
    private String tiempo;
    private Barra barra;

    private Pelota pelota;
    private ArrayList<Pelota> pelotas;
    private static boolean funcionando;
    private static final int ANCHO = 600;
    private static final int ALTO = 800;
    private Thread hiloJuego;
    private static final int ACTUALIZACION = 60;
    private long referenciaReloj = System.nanoTime();
    private long tiempoReloj = 0;
    private int contadorBloques = 0;
    private int contadorBolas = 0;
    private int contadorBloquesTotal = 0;

    private ArrayList<Bloque> bloques;

    public Juego() {
        inicializarAtributos();
        this.setVisible(true);
        this.setBounds(0, 0, ANCHO, ALTO);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(3);
        this.setLayout(null);
        this.addKeyListener(this);

        labelPuntaje.setBounds(10, 5, 100, 30);
        labelPuntaje.setFont(new Font("Arial", Font.BOLD, 20));
        labelPuntaje.setForeground(Color.yellow);
        labelPuntaje.setText(String.valueOf(puntaje));
        fondo.add(labelPuntaje);

        labelBolas.setBounds(this.getWidth() / 2 - 100, 5, 200, 30);
        labelBolas.setHorizontalAlignment(JLabel.CENTER);
        labelBolas.setFont(new Font("Arial", Font.BOLD, 20));
        labelBolas.setForeground(Color.yellow);
        labelBolas.setText("bolas restantes: " + bolasDisponibles);
        fondo.add(this.labelBolas);

        labelTiempo.setBounds(this.getWidth() - 65, 5, 50, 30);
        labelTiempo.setHorizontalAlignment(JLabel.TRAILING);
        labelTiempo.setFont(new Font("Arial", Font.BOLD, 20));
        labelTiempo.setForeground(Color.yellow);
        labelTiempo.setText(tiempo);
        fondo.add(this.labelTiempo);

        fondo.setBackground(Color.BLACK);
        fondo.setSize(this.getSize());
        fondo.setLayout(null);
        this.add(fondo);

        fondo.add(barra);
        fondo.add(pelota);
        pintarBloques();

        hiloJuego.start();

    }

    public static JLabel getLabelPuntaje() {
        return labelPuntaje;
    }

    public static void setLabelPuntaje(JLabel labelPuntaje) {
        Juego.labelPuntaje = labelPuntaje;
    }

    public static JLabel getLabelTiempo() {
        return labelTiempo;
    }

    public static void setLabelTiempo(JLabel labelTiempo) {
        Juego.labelTiempo = labelTiempo;
    }

    public static JLabel getLabelBolas() {
        return labelBolas;
    }

    public static void setLabelBolas(JLabel labelBolas) {
        Juego.labelBolas = labelBolas;
    }

    private void inicializarAtributos() {

        fondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.yellow);
                g.fillRect(10, 40, ANCHO - 25, 10);
                g.fillRect(10, ALTO - 50, ANCHO - 25, 10);
                g.fillRect(10, 40, 10, ALTO - 90);
                g.fillRect(ANCHO - 25, 40, 10, ALTO - 90);
            }
        };
        labelPuntaje = new JLabel();
        labelTiempo = new JLabel();
        labelBolas = new JLabel();
        puntaje = 0;
        tiempo = "0";
        bolasDisponibles = 5;
        pelotas = new ArrayList<>();
        bloques = new ArrayList<>();

        for (int i = 50; i < 240; i += 25) {
            for (int j = 100; j < 300; j += 25) {
                bloques.add(new Bloque(i * 2, j, Color.yellow));
            }
        }
        pelota = new Pelota(new Dimension(ANCHO, ALTO));
        barra = new Barra(ANCHO, ALTO, pelota);
        funcionando = true;
        hiloJuego = new Thread(this);

    }

    public static void main(String[] args) {
        new Juego();

    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_SPACE) {
            if (pelota.isMuerta()) {
                if (bolasDisponibles != 0) {
                    pelota.lanzarPelota(Barra.getMovimiento());
                    bolasDisponibles--;
                    labelBolas.setText("Bolas disponibles: " + bolasDisponibles);
                    pelota.setMuerta(false);
                }
            }
        }
        barra.movimiento(ke);
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        barra.detener(ke);
    }

    private void rebotarBarra() {
        if (pelota.getY() + 10 == barra.getY()
                && (pelota.getX() >= barra.getX() - 5
                && pelota.getX() + 10 <= barra.getX()
                + Barra.getDimensionBarra() * Barra.getBloque() + 5)) {
            if (pelota.getDireccion().equals("SO")) {
                if (Barra.getMovimiento().equals("derecha")) {
                    pelota.cambiarDireccion("x");
                }
            } else if (Barra.getMovimiento().equals("izquierda")) {
                pelota.cambiarDireccion("x");
            }
            pelota.cambiarDireccion("y");

            contadorBloques = 0;
        }
    }

    private void actualizarTiempo() {

        tiempoReloj = System.nanoTime();
        if (tiempoReloj - referenciaReloj > 1000000000) {
            referenciaReloj = tiempoReloj;

            tiempo = String.valueOf(Integer.parseInt(tiempo) + 1);
            labelTiempo.setText(tiempo);
        }
    }

    private void destruirBloque() {

        for (Bloque bloque : bloques) {

            if (pelota.getX() == bloque.getX() + 40
                    && pelota.getY() == (bloque.getY() + 10)) {
                if (bloque.isVisible()) {
                    pelota.chocarEsquina("SE");
                    bloque.setVisible(false);
                    sumarPuntaje();
                    break;
                }
            } else if (pelota.getX() + 10 == bloque.getX()
                    && pelota.getY() == (bloque.getY() + 10)) {
                if (bloque.isVisible()) {
                    pelota.chocarEsquina("SO");
                    bloque.setVisible(false);
                    sumarPuntaje();
                    break;
                }
            } else if (pelota.getX() == bloque.getX() + 40
                    && pelota.getY() + 10 == bloque.getY()) {
                if (bloque.isVisible()) {
                    pelota.chocarEsquina("NE");
                    bloque.setVisible(false);
                    sumarPuntaje();
                    break;
                }
            } else if (pelota.getX() + 10 == bloque.getX()
                    && pelota.getY() + 10 == (bloque.getY())) {
                if (bloque.isVisible()) {
                    pelota.chocarEsquina("NO");
                    bloque.setVisible(false);
                    sumarPuntaje();
                    break;
                }
            } else if (pelota.getX() == bloque.getX() + 40
                    && ((pelota.getY() + 1 >= bloque.getY()
                    && pelota.getY() <= bloque.getY() + 10)
                    || (pelota.getY() + 9 >= bloque.getY()
                    && pelota.getY() + 9 <= bloque.getY() + 10))) {
                if (bloque.isVisible()) {
                    System.out.println("bloque1");
                    pelota.cambiarDireccion("x");
                    bloque.setVisible(false);
                    sumarPuntaje();
                    break;
                }
            } else if (pelota.getY() == (bloque.getY() + 10)
                    && (((pelota.getX() + 1) >= bloque.getX()
                    && pelota.getX() <= bloque.getX() + 40)
                    || ((pelota.getX() + 9) >= bloque.getX()
                    && (pelota.getX() + 9) <= (bloque.getX() + 40)))) {
                if (bloque.isVisible()) {
                    System.out.println("bloque4");
                    pelota.cambiarDireccion("y");
                    bloque.setVisible(false);
                    sumarPuntaje();
                    break;
                }
            } else if ((pelota.getX() + 9) == bloque.getX()
                    && ((pelota.getY() + 1 >= bloque.getY()
                    && pelota.getY() <= bloque.getY() + 10)
                    || (pelota.getY() + 9 >= bloque.getY()
                    && pelota.getY() + 9 <= bloque.getY() + 10))) {
                if (bloque.isVisible()) {
                    System.out.println("bloque2");
                    System.out.println("xPelota " + (pelota.getX() + 10));
                    System.out.println("xBloque " + bloque.getX());
                    System.out.println("YPelota " + pelota.getY());
                    System.out.println("YBloque " + (bloque.getY() + 10));

                    pelota.cambiarDireccion("x");
                    bloque.setVisible(false);
                    sumarPuntaje();
                    break;
                }
            } else if (pelota.getY() + 10 == bloque.getY()
                    && ((pelota.getX() + 1 >= bloque.getX()
                    && pelota.getX() <= bloque.getX() + 40)
                    || (pelota.getX() + 9 >= bloque.getX()
                    && pelota.getX() + 9 <= bloque.getX() + 40))) {
                if (bloque.isVisible()) {
                    System.out.println("bloque3");
                    pelota.cambiarDireccion("y");
                    bloque.setVisible(false);
                    sumarPuntaje();
                    break;
                }
            }
        }
    }

    @Override
    public void run() {

        final int NS_POR_SEGUNDO = 1000000000;
        final int APS_OBJETIVO = 30;
        final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETIVO;

        long referenciaActualizacion = System.nanoTime();

        double timpoTranscurrido;
        double delta = 0;

        while (funcionando) {
            actualizarTiempo();
            final long inicionBucle = System.nanoTime();

            timpoTranscurrido = inicionBucle - referenciaActualizacion;

            referenciaActualizacion = inicionBucle;
            delta += timpoTranscurrido / NS_POR_ACTUALIZACION;

            while (delta >= 1) {
                barra.funcionar();
                if (pelota.isMuerta()) {
                    if (bolasDisponibles == 0) {
                        perderJuego();

                    } else {
                        pelota.posicionInicial();
                    }
                }
                pelota.movimiento();
                destruirBloque();
                rebotarBarra();
                delta--;
            }
        }
    }

    private void pintarBloques() {
        for (Bloque bloque : bloques) {
            fondo.add(bloque);
        }
    }

    private void sumarPuntaje() {

        contadorBloques++;
        contadorBloquesTotal++;
        System.out.println(contadorBloques);
        puntaje += 100 * contadorBloques;
        labelPuntaje.setText(String.valueOf(puntaje));
        if (contadorBloquesTotal == bloques.size()) {
            ganarJuego();
        }

    }

    private void reiniciarJuego() {
        for (Bloque bloque : bloques) {
            bloque.setVisible(true);
        }
        puntaje = 0;
        tiempo = "0";
        bolasDisponibles = 5;

        labelBolas.setText("bolas restantes: " + bolasDisponibles);
        labelPuntaje.setText(String.valueOf(puntaje));
        labelTiempo.setText(tiempo);
        pelota.posicionInicial();

        funcionando = true;
    }

    private void perderJuego() {
        funcionando = false;
        JOptionPane.showMessageDialog(null, "¡Perdiste..., \n Tu puntaje es: " + puntaje);
        reiniciarJuego();
    }

    private void ganarJuego() {
        funcionando = false;
        JOptionPane.showMessageDialog(null, "¡GANASTE, \nFELICITACIONES!\n Tu puntaje es: " + puntaje);
        reiniciarJuego();
    }
}
