/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab02_juanjulio_jorgesalazar_camilosinning;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author camil
 */
public class Graficar {

    public static void GraficarNodo(Graphics g , int diametro, int x, int y, int num) {
        
        g.setColor(Color.black);
        g.drawOval(x, y, diametro, diametro);
        g.drawString(num+"", x+diametro/2-diametro/8, y+diametro/2+diametro/8);
    }
    
    public static void DibujarFlecha(Graphics g, int x0, int y0, int x1, int y1) {
        double alfa = Math.atan2(y1 - y0, x1 - x0);
        g.drawLine(x0, y0, x1, y1);
        int k = 8;
        int xa = (int) (x1 - k * Math.cos(alfa + 1));
        int ya = (int) (y1 - k * Math.sin(alfa + 1));
// Se dibuja un extremo de la dirección de la flecha.
        g.drawLine(xa, ya, x1, y1);
        xa = (int) (x1 - k * Math.cos(alfa - 1));
        ya = (int) (y1 - k * Math.sin(alfa - 1));
// Se dibuja el otro extremo de la dirección de la flecha.
        g.drawLine(xa, ya, x1, y1);
    }
}
