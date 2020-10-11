/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab02_juanjulio_jorgesalazar_camilosinning;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

/**
 *
 * @author camil
 */
public class Simulator extends javax.swing.JFrame {

    /**
     * Creates new form Simulator
     */
    int nodos = -1;
    int maskMode = -1;

    public Simulator() {
        initComponents();
        setExtendedState(Simulator.MAXIMIZED_BOTH);
        dispose();
        setUndecorated(true);
        initialSettings.setVisible(true);
        initialSettings.setLocationRelativeTo(null);


        //ubicar UI

        //ubicar panel
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int sy = screenSize.height;
        int sx = screenSize.width;
        setLayout(null);
        backgroundPanel.setSize(sx, sy);
        backgroundPanel.setLayout(null);
        //Ubicar botones segun resolución de pantalla
        int x = backgroundPanel.getSize().width;
        styleLabel.setLocation(x / 2 - styleLabel.getSize().width / 2, 0);
        numberLabel.setLocation(styleLabel.getLocation().x + styleLabel.getSize().width / 2 - numberLabel.getSize().width / 2, 40);

        //ubicando botones superiores
        int xr = styleLabel.getLocation().x;
        nextButton.setLocation(xr + 150, 30);
        resetButton.setLocation(xr - 120, 30);

        //ubicando boton de close
        closeButton.setLocation(x - closeButton.getSize().width, 0);

        //Botones de reproduccion automatica
        int y = backgroundPanel.getSize().height;
        playStopPanel.setLocation(sx - 15 - playStopPanel.getSize().width, y / 2 - settingsPanel.getHeight() / 2);

        //ubicando settings
        settingsPanel.setLocation(15, y / 2 - settingsPanel.getHeight() / 2);

        //ubicar tablero
        tablero.setLocation(settingsPanel.getLocation().x + settingsPanel.getSize().width + 15, styleLabel.getLocation().y + styleLabel.getSize().height);
        tablero.setSize(sx - tablero.getLocation().x * 2, sy - tablero.getLocation().y - 50);
        //Fin ubicar

    }

    Vértice PTR;

    //Crea el grafo a partir de una matriz y una lista de adyacencia
    public void InicioGrafo(Graphics g, int num_nodos, int mascarilla) {
        int i = 0, j = 0;
        int[][] Adyacencia = new int[num_nodos][num_nodos];

        while (i < num_nodos) {
            while (j < num_nodos) {
                if (i == j) {
                    Adyacencia[i][j] = 0;
                } else {
                    Adyacencia[i][j] = (int) (Math.random() * 2);
                }
                //Un 0 en la matriz significa que no están relacionados y un número entre 1 si existe una arista de i a j
                if (Adyacencia[i][j] == 1) {
                    Adyacencia[i][j] = (int) (Math.random() * 5) + 1;
                }
                j++;
            }
            j = 0;
            i++;
        }
        if (SinNodosAislados(Adyacencia, num_nodos)) {
            InicioGrafo(g, num_nodos, mascarilla);
        }
        GrafoComoLista(g, num_nodos, mascarilla, Adyacencia);
    }

    //Verifica que no haya nodos aislados
    boolean SinNodosAislados(int Matriz[][], int num_nodos) {
        boolean Aislados = false;
        int i = 0, j = 0, aux = 0, acum = 0, acum2 = 0;

        while (i < num_nodos) {
            while (j < num_nodos) {
                acum = Matriz[i][j] + acum;
                j++;
            }
            if (acum == 0) {
                while (aux < num_nodos) {
                    acum2 = Matriz[aux][i] + acum2;
                    aux++;
                }
                if (acum2 == 0) {
                    Aislados = true;
                    return Aislados;
                }
            } else {
                j = 0;
                i++;
            }
        }
        return Aislados;
    }

    //Crea una lista donde cada nodo tendrá las características de los nodos del grafo
    public void GrafoComoLista(Graphics g, int num_nodos, int mascarilla, int Matriz[][]) {
        int i = 0;
        Vértice p;
        int infectado;
        PTR = null;

        while (i < num_nodos) {
            Vértice q = null;
            if (mascarilla == 0 || mascarilla == 1) {
                q = new Vértice(0, mascarilla, i + 1);

                //q.mascarilla = mascarilla;
            } else {
                q = new Vértice(0, (int) (Math.random() * 2), i + 1);
                //q.mascarilla = (int) (Math.random() * 2);
            }
            q.linkIncidentes = null;
            //1 significa que la persona está contagiada
            //0 significa que la persona no está contagiada
            if (PTR == null) {
                PTR = q;
            } else {
                p = PTR;
                while (p.link != null) {
                    p = p.link;
                }
                p.link = q;
                q.link = null;
            }
            i++;
        }
        ListaDeAdyacencia(Matriz, num_nodos, mascarilla);
        infectado = PrimerInfectado(num_nodos, Matriz);
        ActualizaInfectados(g, infectado, Matriz);
    }

    //Función que da al azar el primer infectado
    int PrimerInfectado(int num_nodos, int Matriz[][]) {
        int infectado, j = 0, acum = 0;

        infectado = (int) (Math.random() * num_nodos) + 1;
        while (j < num_nodos) {
            acum = Matriz[infectado - 1][j] + acum;
            j++;
        }
        if (acum == 0) {
            PrimerInfectado(num_nodos, Matriz);
        }
        return infectado;
    }

    //Función que actualiza la lista con los infectados
    public void ActualizaInfectados(Graphics g, int infectado, int Matriz[][]) {
        Vértice p;

        p = PTR;
        while (p.num != infectado) {
            p = p.link;
        }
        p.enfermo = 1;
        Graficar.GraficarInicio(g, Matriz,tablero);
    }

    //Crea una multilista con los grafos y sus conexiones a partir de la lista ya creada
    public void ListaDeAdyacencia(int Matriz[][], int num_nodos, int mascarilla) {
        Vértice p;
        int i = 0, j = 0;

        p = PTR;
        while (i < num_nodos) {
            while (j < num_nodos) {
                if (Matriz[i][j] > 0) {
                    Vértice q = new Vértice(0, mascarilla, j);
                    while ((p != null) && (p.num != (i + 1))) {
                        p = p.link;
                    }
                    if ((p != null) && (p.linkIncidentes == null)) {
                        p.linkIncidentes = q;

                        q.linkIncidentes = null;
                    } else {
                        while ((p != null) && (p.linkIncidentes != null)) {

                            p = p.linkIncidentes;
                        }
                        if (p != null) {
                            p.linkIncidentes = q;
                            q.linkIncidentes = null;
                        }

                    }
                }
                j++;
            }
            i++;
        }
    }

    //Se encarga de generar las iteraciones en simulador y actualizar 

    public void Iteracion(Graphics g, int Matriz[][]) {
        Vértice p;
        p = PTR;
        while (p != null && p.enfermo == 0) {
            p = p.link;
        }

        ProximosEnfermos(g, p, Matriz);
    }

    //Calcula el próximo contagiado en caso de que lo haya según las probabilidades dadas por el lab
    public void ProximosEnfermos(Graphics g, Vértice p, int Matriz[][]) {
        Vértice aux;

        if (p.linkIncidentes != null) {
            aux = p.linkIncidentes;
            while (aux != null && aux.enfermo == 1) {
                aux = aux.linkIncidentes;
            }
            if (aux == null) {
                if (p.link != null) {
                    p = p.link;
                    while (p != null && p.enfermo == 0) {
                        p = p.link;
                    }
                    if (p == null) {
                        //Se terminó la simulación
                        //Se puede crear un JOptionPane o algo
                    } else {
                        ProximosEnfermos(g, p, Matriz);
                    }
                } else {
                    //Se terminó la simulación
                    //Se puede crear un JOptionPane o algo
                }
            } else {
                CalculaProbabilidades(g, p, aux, Matriz);
            }
        }
    }

    public void CalculaProbabilidades(Graphics g,Vértice p, Vértice aux, int Matriz[][]) {
        if (p.mascarilla == 0 && aux.mascarilla == 0 && Matriz[p.num - 1][aux.num - 1] > 2) {
            int prob;
            prob = (int) (Math.random() * 100 + 1);
            if (prob <= 80) {
                ActualizaInfectados(g,aux.num, Matriz);
            }
            if (aux.linkIncidentes != null) {
                aux = aux.linkIncidentes;
                while (aux != null && aux.enfermo == 1) {
                    aux = aux.linkIncidentes;
                }

            }
            if (aux == null) {
                if (p.link != null) {
                    p = p.link;
                    while (p != null && p.enfermo == 0) {
                        p = p.link;
                    }
                    if (p == null) {
                        //Se terminó la simulación
                        //Se puede crear un JOptionPane o algo
                    } else {
                        ProximosEnfermos(g, p, Matriz);
                    }
                } else {
                    //Se terminó la simulación
                    //Se puede crear un JOptionPane o algo
                }
            }
        } else if (p.mascarilla == 0 && aux.mascarilla == 0 && Matriz[p.num - 1][aux.num - 1] <= 2) {

        } else if (p.mascarilla == 0 && aux.mascarilla == 1 && Matriz[p.num - 1][aux.num - 1] > 2) {

        } else if (p.mascarilla == 0 && aux.mascarilla == 1 && Matriz[p.num - 1][aux.num - 1] <= 2) {

        } else if (p.mascarilla == 1 && aux.mascarilla == 0 && Matriz[p.num - 1][aux.num - 1] > 2) {

        } else if (p.mascarilla == 1 && aux.mascarilla == 0 && Matriz[p.num - 1][aux.num - 1] <= 2) {

        } else if (p.mascarilla == 1 && aux.mascarilla == 1 && Matriz[p.num - 1][aux.num - 1] > 2) {

        } else if (p.mascarilla == 1 && aux.mascarilla == 1 && Matriz[p.num - 1][aux.num - 1] <= 2) {

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        initialSettings = new javax.swing.JDialog();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        nodosTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        withoutMaskButton = new javax.swing.JButton();
        allMaskButton = new javax.swing.JButton();
        maskRandomButton = new javax.swing.JButton();
        startButton = new javax.swing.JButton();
        closeButton1 = new javax.swing.JButton();
        errorLabel = new javax.swing.JTextField();
        errorLabel1 = new javax.swing.JTextField();
        backgroundPanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        numberLabel = new javax.swing.JLabel();
        styleLabel = new javax.swing.JLabel();
        resetButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        settingsPanel = new javax.swing.JPanel();
        numberNodesLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        allMaskButton1 = new javax.swing.JButton();
        maskRandomButton1 = new javax.swing.JButton();
        withoutMaskButton1 = new javax.swing.JButton();
        tablero = new javax.swing.JPanel(){
            @Override
            public void paint(Graphics g){
                super.paint(g);
                InicioGrafo(g,nodos,maskMode);
            }
        };
        playStopPanel = new javax.swing.JPanel();
        playButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();

        initialSettings.setMinimumSize(new java.awt.Dimension(391, 605));
        initialSettings.setUndecorated(true);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 60)); // NOI18N
        jLabel3.setText("Número de nodos");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 340, -1));

        nodosTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nodosTextField.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        nodosTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodosTextFieldActionPerformed(evt);
            }
        });
        jPanel2.add(nodosTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 310, 30));

        jLabel4.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 70)); // NOI18N
        jLabel4.setText("Modo");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 280, -1, 50));

        withoutMaskButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Whithout MAsk.png"))); // NOI18N
        withoutMaskButton.setToolTipText("Without a mask");
        withoutMaskButton.setBorderPainted(false);
        withoutMaskButton.setContentAreaFilled(false);
        withoutMaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                withoutMaskButtonActionPerformed(evt);
            }
        });
        jPanel2.add(withoutMaskButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, -1, -1));

        allMaskButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/mask2.png"))); // NOI18N
        allMaskButton.setToolTipText("With mask");
        allMaskButton.setBorderPainted(false);
        allMaskButton.setContentAreaFilled(false);
        allMaskButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allMaskButtonActionPerformed(evt);
            }
        });
        jPanel2.add(allMaskButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 340, -1, -1));

        maskRandomButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/aleatorio.png"))); // NOI18N
        maskRandomButton.setToolTipText("Random");
        maskRandomButton.setBorderPainted(false);
        maskRandomButton.setContentAreaFilled(false);
        maskRandomButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maskRandomButtonActionPerformed(evt);
            }
        });
        jPanel2.add(maskRandomButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 340, -1, -1));

        startButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/start1.png"))); // NOI18N
        startButton.setToolTipText("Start");
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });
        jPanel2.add(startButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 460, -1, -1));

        closeButton1.setBackground(new java.awt.Color(255, 255, 255));
        closeButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/close sij mancha.png"))); // NOI18N
        closeButton1.setToolTipText("Close");
        closeButton1.setBorderPainted(false);
        closeButton1.setContentAreaFilled(false);
        closeButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(closeButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 80, -1));

        errorLabel.setEditable(false);
        errorLabel.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 18)); // NOI18N
        errorLabel.setForeground(new java.awt.Color(255, 99, 71));
        errorLabel.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        errorLabel.setBorder(null);
        errorLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        errorLabel.setFocusable(false);
        errorLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                errorLabelMouseClicked(evt);
            }
        });
        errorLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                errorLabelActionPerformed(evt);
            }
        });
        errorLabel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                errorLabelKeyReleased(evt);
            }
        });
        jPanel2.add(errorLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, 190, 30));

        errorLabel1.setEditable(false);
        errorLabel1.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 18)); // NOI18N
        errorLabel1.setForeground(new java.awt.Color(255, 99, 71));
        errorLabel1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        errorLabel1.setBorder(null);
        errorLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        errorLabel1.setFocusable(false);
        errorLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                errorLabel1MouseClicked(evt);
            }
        });
        errorLabel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                errorLabel1ActionPerformed(evt);
            }
        });
        errorLabel1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                errorLabel1KeyReleased(evt);
            }
        });
        jPanel2.add(errorLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 410, 190, 30));

        initialSettings.getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        backgroundPanel.setBackground(new java.awt.Color(255, 255, 255));
        backgroundPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        closeButton.setBackground(new java.awt.Color(255, 255, 255));
        closeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/close.png"))); // NOI18N
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        backgroundPanel.add(closeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(1230, 0, 130, 140));

        numberLabel.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 48)); // NOI18N
        numberLabel.setText("0");
        backgroundPanel.add(numberLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(675, 40, 20, -1));

        styleLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/contador.png"))); // NOI18N
        backgroundPanel.add(styleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 0, 130, 130));

        resetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/reset.png"))); // NOI18N
        resetButton.setToolTipText("Reset");
        resetButton.setBorderPainted(false);
        resetButton.setContentAreaFilled(false);
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        backgroundPanel.add(resetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 30, 110, -1));

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/play.png"))); // NOI18N
        nextButton.setToolTipText("Next");
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        backgroundPanel.add(nextButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 30, 80, -1));

        settingsPanel.setBackground(new java.awt.Color(255, 255, 255));
        settingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Settings", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tw Cen MT Condensed", 0, 24))); // NOI18N
        settingsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        numberNodesLabel.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 24)); // NOI18N
        numberNodesLabel.setForeground(new java.awt.Color(102, 102, 102));
        numberNodesLabel.setText("0");
        settingsPanel.add(numberNodesLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, -1, 30));

        jLabel2.setFont(new java.awt.Font("Tw Cen MT Condensed", 0, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/partes.png"))); // NOI18N
        jLabel2.setToolTipText("Number of nodes");
        settingsPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 110, 90, 80));

        allMaskButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/mask2.png"))); // NOI18N
        allMaskButton1.setToolTipText("With a mask");
        allMaskButton1.setBorderPainted(false);
        allMaskButton1.setContentAreaFilled(false);
        allMaskButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allMaskButton1ActionPerformed(evt);
            }
        });
        settingsPanel.add(allMaskButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 90, 70));

        maskRandomButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/aleatorio.png"))); // NOI18N
        maskRandomButton1.setBorderPainted(false);
        maskRandomButton1.setContentAreaFilled(false);
        maskRandomButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maskRandomButton1ActionPerformed(evt);
            }
        });
        settingsPanel.add(maskRandomButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 90, 70));

        withoutMaskButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Whithout MAsk.png"))); // NOI18N
        withoutMaskButton1.setBorderPainted(false);
        withoutMaskButton1.setContentAreaFilled(false);
        withoutMaskButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                withoutMaskButton1ActionPerformed(evt);
            }
        });
        settingsPanel.add(withoutMaskButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 90, 60));

        backgroundPanel.add(settingsPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 90, 200));

        tablero.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout tableroLayout = new javax.swing.GroupLayout(tablero);
        tablero.setLayout(tableroLayout);
        tableroLayout.setHorizontalGroup(
            tableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1110, Short.MAX_VALUE)
        );
        tableroLayout.setVerticalGroup(
            tableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 450, Short.MAX_VALUE)
        );

        backgroundPanel.add(tablero, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 1110, 450));

        playStopPanel.setBackground(new java.awt.Color(255, 255, 255));
        playStopPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Play/Stop", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tw Cen MT Condensed", 0, 24))); // NOI18N
        playStopPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/play1.png"))); // NOI18N
        playButton.setToolTipText("Play");
        playButton.setBorderPainted(false);
        playButton.setContentAreaFilled(false);
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        playStopPanel.add(playButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 90, 70));

        stopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/stop.png"))); // NOI18N
        stopButton.setToolTipText("Stop");
        stopButton.setBorderPainted(false);
        stopButton.setContentAreaFilled(false);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });
        playStopPanel.add(stopButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 90, 70));

        backgroundPanel.add(playStopPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1260, 270, 90, 200));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(backgroundPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_closeButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stopButtonActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        boolean sw = false, sw1 = false;
        //validaciones
        try {
            errorLabel.setText("");
            nodos = Integer.parseInt(nodosTextField.getText());
            if (nodos < 0) {
                errorLabel.setText("Invalido");
            } else {
                sw = true;
                errorLabel.setText("");
            }
            if (maskMode != -1) {
                sw1 = true;
                errorLabel1.setText("");
            } else {
                errorLabel1.setText("Seleccione un modo");
            }

            if (sw && sw1) {
                initialSettings.dispose();
                setVisible(true);
                if (maskMode == 0) {
                    withoutMaskButton1.setVisible(true);
                    allMaskButton1.setVisible(false);
                    maskRandomButton1.setVisible(false);
                } else if (maskMode == 1) {
                    withoutMaskButton1.setVisible(false);
                    allMaskButton1.setVisible(true);
                    maskRandomButton1.setVisible(false);
                } else {
                    withoutMaskButton1.setVisible(false);
                    allMaskButton1.setVisible(false);
                    maskRandomButton1.setVisible(true);
                }
                numberNodesLabel.setText(nodosTextField.getText());
                nodosTextField.setText("");
                withoutMaskButton.setEnabled(true);
                maskRandomButton.setEnabled(true);
                allMaskButton.setEnabled(true);

            }

        } catch (Exception e) {
            errorLabel.setText("Invalido");
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_startButtonActionPerformed

    private void withoutMaskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_withoutMaskButtonActionPerformed

        withoutMaskButton.setEnabled(false);
        maskRandomButton.setEnabled(true);
        allMaskButton.setEnabled(true);
        maskMode = 0;
        // TODO add your handling code here:
    }//GEN-LAST:event_withoutMaskButtonActionPerformed

    private void maskRandomButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maskRandomButtonActionPerformed

        withoutMaskButton.setEnabled(true);
        maskRandomButton.setEnabled(false);
        allMaskButton.setEnabled(true);
        maskMode = 2;
        // TODO add your handling code here:
    }//GEN-LAST:event_maskRandomButtonActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_playButtonActionPerformed

    private void nodosTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodosTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nodosTextFieldActionPerformed

    private void allMaskButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allMaskButtonActionPerformed

        withoutMaskButton.setEnabled(true);
        maskRandomButton.setEnabled(true);
        allMaskButton.setEnabled(false);
        maskMode = 1;
        // TODO add your handling code here:
    }//GEN-LAST:event_allMaskButtonActionPerformed

    private void closeButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButton1ActionPerformed
        System.exit(0);        // TODO add your handling code here:
    }//GEN-LAST:event_closeButton1ActionPerformed

    private void errorLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_errorLabelMouseClicked

    }//GEN-LAST:event_errorLabelMouseClicked

    private void errorLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_errorLabelActionPerformed

    }//GEN-LAST:event_errorLabelActionPerformed

    private void errorLabelKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_errorLabelKeyReleased

    }//GEN-LAST:event_errorLabelKeyReleased

    private void errorLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_errorLabel1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_errorLabel1MouseClicked

    private void errorLabel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_errorLabel1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_errorLabel1ActionPerformed

    private void errorLabel1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_errorLabel1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_errorLabel1KeyReleased

    private void withoutMaskButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_withoutMaskButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_withoutMaskButton1ActionPerformed

    private void allMaskButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allMaskButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_allMaskButton1ActionPerformed

    private void maskRandomButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maskRandomButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maskRandomButton1ActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed

        setVisible(false);
        initialSettings.setVisible(true);

        // TODO add your handling code here:
    }//GEN-LAST:event_resetButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Simulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Simulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Simulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Simulator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Simulator().setVisible(false);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton allMaskButton;
    private javax.swing.JButton allMaskButton1;
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton closeButton1;
    private javax.swing.JTextField errorLabel;
    private javax.swing.JTextField errorLabel1;
    private javax.swing.JDialog initialSettings;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton maskRandomButton;
    private javax.swing.JButton maskRandomButton1;
    private javax.swing.JButton nextButton;
    private javax.swing.JTextField nodosTextField;
    private javax.swing.JLabel numberLabel;
    private javax.swing.JLabel numberNodesLabel;
    private javax.swing.JButton playButton;
    private javax.swing.JPanel playStopPanel;
    private javax.swing.JButton resetButton;
    private javax.swing.JPanel settingsPanel;
    private javax.swing.JButton startButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JLabel styleLabel;
    private javax.swing.JPanel tablero;
    private javax.swing.JButton withoutMaskButton;
    private javax.swing.JButton withoutMaskButton1;
    // End of variables declaration//GEN-END:variables
}
