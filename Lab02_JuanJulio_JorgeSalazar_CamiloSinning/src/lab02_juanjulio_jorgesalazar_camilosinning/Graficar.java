/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab02_juanjulio_jorgesalazar_camilosinning;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
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

    public static void DibujarFlecha(Graphics g, int peso, int x0, int y0, int x1, int y1) {
        double alfa = Math.atan2(y1 - y0, x1 - x0);
        g.setColor(Color.black);
        //linea principal
        g.drawLine(x0, y0, x1, y1);
        //flecha
        int k = 8;
        int xa = (int) (x1 - k * Math.cos(alfa + 1));
        int ya = (int) (y1 - k * Math.sin(alfa + 1));
        g.drawLine(xa, ya, x1, y1);
        xa = (int) (x1 - k * Math.cos(alfa - 1));
        ya = (int) (y1 - k * Math.sin(alfa - 1));
        g.drawLine(xa, ya, x1, y1);
        //peso
        g.setColor(Color.red);
        g.drawString(peso + "", x0 - x1, y0 - y1);
    }

    public static void GraficarInicio(Graphics g, int Matriz[][],JPanel j) {
        int EntradaSalida = 0;
        int Entrada = 0;
        int Salida = 0;
        for (int i = 0; i < Matriz.length; i++) {
            if ((Matriz[0][i] != 0) && (Matriz[i][0] != 0)) {
                EntradaSalida++;
            } else if ((Matriz[0][i] != 0) && (Matriz[i][0] == 0)) {
                Entrada++;
            } else if((Matriz[0][i] == 0) && (Matriz[i][0] != 0)) {
                Salida++;
            }
        }
        int Particiones = EntradaSalida + Salida + Entrada; //depende de si es el mismo nodo el de salida y entrada o no
        
        //dibujar nodo 1      
        GraficarNodo(g,40, j.getSize().width/2,j.getSize().height/2,1);      
        for (int i = 0; i < Matriz.length; i++) {
            if ((Matriz[0][i] != 0) && (Matriz[i][0] != 0)) {
                //EntradaSalida
            } else if ((Matriz[0][i] != 0) && (Matriz[i][0] == 0)) {
                //Salida
            } else {
                //Entrada
            }
        }
        double grados = 2 * Math.PI / Particiones;
        double GradoActual = 0;
        while (GradoActual <= 2 * Math.PI) {
            //poner las flechas distribuidas
            GradoActual = GradoActual + grados;
        }
    }

}
