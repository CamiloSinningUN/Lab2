/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab02_juanjulio_jorgesalazar_camilosinning;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author camil
 */
public class Graficar {

    public static void GraficarNodo(Graphics g, int diametro, int x, int y, int num) {
        // dibujar circulo
        g.setColor(Color.blue);
        g.drawOval(x - diametro / 2, y - diametro / 2, diametro, diametro);

        // poner texto en el circulo
        FontMetrics fm = g.getFontMetrics();
        double textWidth = fm.getStringBounds(num + "", g).getWidth();
        g.setColor(Color.black);
        g.drawString(num + "", (int) (x - textWidth / 2), (int) (y + fm.getMaxAscent() / 2));
    }

    public static void DibujarFlecha(Graphics g, int x0, int y0, int x1, int y1) {
        double alfa = Math.atan2(y1 - y0, x1 - x0);
        g.drawLine(x0, y0, x1, y1);
        int k = 8;
        int xa = (int) (x1 - k * Math.cos(alfa + 1));
        int ya = (int) (y1 - k * Math.sin(alfa + 1));
        g.drawLine(xa, ya, x1, y1);
        xa = (int) (x1 - k * Math.cos(alfa - 1));
        ya = (int) (y1 - k * Math.sin(alfa - 1));
        g.drawLine(xa, ya, x1, y1);
    }

    public static int ConexionesSalidaNodo(int nodo, int Matriz[][]) {
        int num = 0;
        for (int i = 0; i < Matriz.length; i++) {
            if (Matriz[nodo - 1][0] != 0) {
                num++;
            }
        }
        return num;
    }

    public static int ConexionesEntradaNodo(int nodo, int Matriz[][]) {
        int num = 0;
        for (int i = 0; i < Matriz.length; i++) {
            if (Matriz[i][nodo - 1] != 0) {
                num++;
            }
        }
        return num;
    }

    public static void GraficarConexiones(Graphics g, int nodo, int Matriz[][]) {
        int Particiones = ConexionesEntradaNodo(nodo, Matriz) + ConexionesSalidaNodo(nodo, Matriz);
        double grados = 2*Math.PI/Particiones;
        double GradoActual = 0;
        while(GradoActual<=360){           
            //poner las flechas distribuidas
            GradoActual = GradoActual + Particiones;
        }
    }

}
