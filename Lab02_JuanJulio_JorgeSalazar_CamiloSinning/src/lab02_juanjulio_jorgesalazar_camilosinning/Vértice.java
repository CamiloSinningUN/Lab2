/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab02_juanjulio_jorgesalazar_camilosinning;

/**
 *
 * @author Juan David
 */
public class Vértice {

    boolean enfermo; //Varible que determina el contagio o no de una persona
    boolean mascarilla; //Varible que determina el uso o no de la mascarilla por parte de una persona
    int[] Aristas = new int[2];
    

    public Vértice(boolean enfermo, boolean mascarilla) {
        this.enfermo = enfermo;
        this.mascarilla = mascarilla;
    }

}
