package tenis;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JLabel;

public class Barra extends JLabel {

    private static int dimensionBarra;
    private static String movimiento;
    private static int posicionX;
    private static int posicionY;
    private static int bloque;
    private final int anchoPantalla;
    private final int altoPantalla;
    private Pelota pelota;
    private int reinicioPelota;
    private boolean pelotaAnclada;

    public Barra(int anchoPantalla, int altoPantalla, Pelota pelota) {
        this.anchoPantalla = anchoPantalla;
        this.altoPantalla = altoPantalla;
        this.pelota = pelota;
        inicializarAtributos();

        this.setBackground(Color.GREEN);
        this.setOpaque(true);
        this.setBounds(posicionX, posicionY, bloque * dimensionBarra, bloque);
    }

    private void inicializarAtributos() {
        dimensionBarra = 4;
        movimiento = "";
        bloque = 10;
        posicionX = anchoPantalla / 2 - (dimensionBarra * bloque) / 2;
        posicionY = altoPantalla - bloque * 7;
        reinicioPelota = 0;
        pelotaAnclada = true;
    }

    public static String getMovimiento() {
        return movimiento;
    }

    public static void setMovimiento(String movimiento) {
        Barra.movimiento = movimiento;
    }

    public static int getPosicionY() {
        return posicionY;
    }

    public static void setPosicionY(int posicionY) {
        Barra.posicionY = posicionY;
    }

    public void movimiento(KeyEvent e) {
        if (e.getKeyChar() == 'a' || e.getKeyCode() == KeyEvent.VK_LEFT) {
            movimiento = "izquierda";
        }
        if (e.getKeyChar() == 'd' || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            movimiento = "derecha";
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (pelotaAnclada) {
                pelotaAnclada = false;
                reinicioPelota = 0;
            } else {
                if (pelota.isMuerta()) {
                    pelota.posicionInicial();
                    pelota.setMuerta(false);
                    pelotaAnclada = true;
                    reinicioPelota++;
                    if (pelotaAnclada) {
                        pelotaAnclada = false;
                        reinicioPelota = 0;
                    }
                }
            }
        }
    }

    public void detener(KeyEvent e) {
        if (e.getKeyChar() == 'a' || e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (movimiento.equals("izquierda")) {
                movimiento = "detenida";
            }
        }
        if (e.getKeyChar() == 'd' || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (movimiento.equals("derecha")) {
                movimiento = "detenida";
            }
        }

    }

    public void funcionar() {
        if (pelotaAnclada && reinicioPelota == 0) {
            pelota.posicionInicial();
        }
        this.setLocation(posicionX, posicionY);
        switch (movimiento) {
            case "derecha":
                System.out.println();
                if (posicionX < anchoPantalla - (dimensionBarra * bloque) - bloque - 25) {
                    posicionX += bloque;
                }
                break;
            case "izquierda":
                if (posicionX > 25) {
                    posicionX -= bloque;

                }
                break;
            case "detenida":
                break;
        }

        this.setLocation(posicionX, posicionY);
    }

    public static int getPosicionX() {
        return posicionX;
    }

    public static int getDimensionBarra() {
        return dimensionBarra;
    }

    public static int getBloque() {
        return bloque;
    }

}
