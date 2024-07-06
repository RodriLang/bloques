package bloques;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JLabel;

public class Pelota extends JLabel {

    private final Dimension DIMENSION_PANTALLA;
    private static final int DIMENSION = 10;
    private int x;
    private int y;
    private String direccion;
    private boolean muerta;

    public boolean isMuerta() {
        return muerta;
    }

    public void setMuerta(boolean muerta) {
        this.muerta = muerta;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String dir) {
        direccion = dir;
    }

    public Pelota(final Dimension dimensionPantalla) {
        DIMENSION_PANTALLA = dimensionPantalla;
        inicializarAtributos();
        this.setBounds(x, y, DIMENSION, DIMENSION);

    }

    private void inicializarAtributos() {
        muerta = true;
        direccion = "-";
        posicionInicial();
    }


    public void posicionInicial() {
        x = (Barra.getPosicionX() + (Barra.getDimensionBarra() * Barra.getBloque() / 2)) - DIMENSION / 2;
        y = Barra.getPosicionY() - DIMENSION;
//x=0;
//y=0;
        this.setLocation(x, y);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.PINK);
        g.fillArc(0, 0, 10, 10, 0, 360);
    }

    public void actualizarUbicacion(int x, int y) {
        setLocation(x, y);
    }

    public void movimiento() {
        switch (direccion) {
            case "NE":
                x += 5;
                y -= 5;
                break;
            case "SE":
                x += 5;
                y += 5;
                break;
            case "NO":
                y -= 5;
                x -= 5;
                break;
            case "SO":
                x -= 5;
                y += 5;
                break;
            default:
        }
        this.setLocation(x, y);
        rebotarBorde();
    }

    public void rebotarBorde() {

        int bordeE = (int) DIMENSION_PANTALLA.getWidth() - DIMENSION - 25;
        int bordeO = 20;
        int bordeN = 50;
        int bordeS = (int) DIMENSION_PANTALLA.getHeight();

        if (x >= bordeE || x < bordeO) {
            cambiarDireccion("x");
        }
        if (y <= bordeN) {
            cambiarDireccion("y");
        }
        if (y > bordeS) {
            this.muerta = true;
            direccion = "-";
        }
    }
    public void chocarEsquina(String esquina){

        direccion = esquina;
    }

    public void cambiarDireccion(String eje) {
        if (eje.equals("x")) {
            switch (direccion) {
                case "NO":
                    direccion = "NE";
                    break;
                case "NE":
                    direccion = "NO";
                    break;
                case "SO":
                    direccion = "SE";
                    break;
                case "SE":
                    direccion = "SO";
                    break;
            }
        }
        if (eje.equals("y")) {
            switch (direccion) {
                case "NO":
                    direccion = "SO";
                    break;
                case "NE":
                    direccion = "SE";
                    break;
                case "SO":
                    direccion = "NO";
                    break;
                case "SE":
                    direccion = "NE";
                    break;
            }
        }
    }

    public void lanzarPelota(String direccion) {
        if (direccion.equals("derecha")) {
            this.direccion = "NE";
        } else {
            this.direccion = "NO";
        }
    }
}
