
package bloques;

import java.awt.Color;
import javax.swing.JLabel;


public class Bloque extends JLabel{
    
    private static final int UNIDAD = 20;

    public Bloque(int x, int y, Color color) {
        this.setBounds(x, y, UNIDAD*2, UNIDAD);
        this.setOpaque(true);
        this.setBackground(color);
    }

}
