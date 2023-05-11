package Dijkstra_algorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Vtn extends javax.swing.JFrame {
    private static final int numCuadros = 20;
    private Nodo[][] Nodos;
    private JButton[][] Botones;
    private Nodo InicioNodo;
    private Nodo FinNodo;
    private int caminoFinal;
    private int IndiceActual = 0;
    private int PasoActual = 0;
    private Nodo[] CaminoCorto = new Nodo[numCuadros * numCuadros];
    private List<Pasos> pasos = new ArrayList<>();
    private boolean botonDerechoPrecionado = false;
    ButtonHandler handler = new ButtonHandler();
    
    public Vtn() {
        Nodos = new Nodo[numCuadros][numCuadros];
        Botones = new JButton[numCuadros][numCuadros];
        initComponents();
        this.setLocationRelativeTo(null);
        cargarControles();
    }
    
    private void cargarControles(){
        pnlContainer.setLayout(new GridLayout(numCuadros, numCuadros));
        for (int fila = 0; fila < numCuadros; fila++) {
            for (int col = 0; col < numCuadros; col++) {
                Nodos[fila][col] = new Nodo(fila, col);
                Botones[fila][col] = new JButton();
                Botones[fila][col].setSize(500, 500);
                Botones[fila][col].addActionListener(handler);
                if((col <= 20 &&(fila == 0||fila == 19))||(col == 0||col == 19)){
                      Botones[fila][col].setBackground(Color.BLUE);
                }else{
                    Botones[fila][col].setBackground(new java.awt.Color(1, 0, 231));
                }
                final int FilaFinal = fila;
                final int ColumnaFinal = col;
                
                Botones[fila][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            botonDerechoPrecionado = true;
                        }
                    }
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            botonDerechoPrecionado = false;
                        }
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (botonDerechoPrecionado) {
                            Nodo nodo = Nodos[FilaFinal][ColumnaFinal];
                            Botones[FilaFinal][ColumnaFinal].setBackground(Color.BLACK);
                            nodo.setBloque(true);
                        }
                    }
                });
                pnlContainer.add(Botones[fila][col]);
            }
        }
    }
    
    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (int row = 0; row < numCuadros; row++) {
                for (int col = 0; col < numCuadros; col++) {
                    if (e.getSource() == Botones[row][col]) {
                        if (InicioNodo == null) {
                            InicioNodo = Nodos[row][col];
                            Botones[row][col].setBackground(Color.GREEN);
                            System.out.println("se inicializó el btn START con el click izquierdo en: ["+row+","+col+"]");
                        } else if (FinNodo == null) {
                            FinNodo = Nodos[row][col];
                            Botones[row][col].setBackground(Color.RED);
                            System.out.println("se inicializó el btn END con el click izquierdo en: ["+row+","+col+"]");
                            runDijkstra();
                        } else {
                            reset();
                        }
                    }
                }
            }
        }
    }
    
    private void runDijkstra() {
        PriorityQueue<Nodo> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.distancia));
        InicioNodo.distancia = 0;//se utiliza para eliminar el elemento al frente de la cola y devolverlo. 
        queue.add(InicioNodo);
        IndiceActual = 0;
        pasos.clear(); // limpiar la lista de pasos anteriores
        CaminoCorto = new Nodo[numCuadros * numCuadros];
        while (!queue.isEmpty()) {
            Nodo actual = queue.poll(); //Se extrae el nodo con menor distancia de la cola.
            actual.setAbierta(true); //Se abre el nodo con menor distancia
            if (!(actual == InicioNodo || actual == FinNodo)) {
                pasos.add(new Pasos(actual.posFila, actual.posColumna)); // agregar un nuevo paso
            }
            if (actual == FinNodo) {
                getRuta();
                System.out.println("Se encontro el nodo END!!!");
                break;
            }

            for (int[] direction : new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
                int nuevaFila = actual.posFila + direction[0];
                int nuevaColumna = actual.posColumna + direction[1];
                Nodo nodoVecino = Nodos[nuevaFila][nuevaColumna];
                if (nodoVecino.isBloque()) {
                    continue; //continua el ciclo foreach si detecta una barrera
                }
                if (Botones[nuevaFila][nuevaColumna].getBackground() == Color.BLUE) {
                    continue; //continua el ciclo foreach si llega al borde de la matriz
                }
                //Si el nodo vecino aun no se ha visto y la distancia desde el nodo actual hasta el nodo 
                //vecino a traves de la direccion actual es menor que la distancia actual del nodo vecino, el codigo actualiza
                // la distancia del nodo vecino, establece el nodo actual como el nodo anterior del nodo vecino y agrega el nodo vecino
                // a una cola de prioridad para procesar en la siguiente iteracion.
           
                if (!nodoVecino.isAbierta() && actual.distancia + 1 < nodoVecino.distancia) {
                    nodoVecino.distancia = actual.distancia + 1;
                    nodoVecino.anterior = actual;
                    queue.add(nodoVecino);
                }
            }
        }

        Nodo node = FinNodo;
        int i = 0;
        while (node != null) {
            CaminoCorto[i++] = node;
            node = node.anterior;
            caminoFinal=i;
        }
    }
    
    private void getRuta() {
        Nodo actual = FinNodo;
        int indice = 0;
        while (actual.anterior != null) {
            CaminoCorto[indice++] = actual;
            actual = actual.anterior;
        }
        CaminoCorto[indice] = InicioNodo;
        IndiceActual = 0;
    }
    
    private void reset() {// Reiniciar todos los botones a su estado original
        for (int row = 0; row < numCuadros; row++) {
            for (int col = 0; col < numCuadros; col++) {
                Nodos[row][col].setAbierta(false);
                Nodos[row][col].distancia = Integer.MAX_VALUE;
                Nodos[row][col].anterior = null;
                 if((col<=20&&(row==0||row==19))||(col==0||col==19)){
                      Botones[row][col].setBackground(Color.BLUE);
                }else{
                Botones[row][col].setBackground(new java.awt.Color(1, 0, 231));
                }
            }
        }
        InicioNodo = null;
        FinNodo = null;

        IndiceActual = 0;
        PasoActual = 0;
        CaminoCorto = new Nodo[numCuadros * numCuadros];
        pasos.clear();

    }
    
    private void siguienteBtn() {
        for(int i=0;i<=5;i++){
            //System.out.println(caminoFinal);
                if (PasoActual < pasos.size()) {
                    Pasos step = pasos.get(PasoActual++);
                    if (Botones[step.getX()][step.getY()].getBackground() != Color.YELLOW &&Botones[step.getX()][step.getY()].getBackground() != Color.RED) {
                        Botones[step.getX()][step.getY()].setBackground(new java.awt.Color(0, 174, 152)); //Cian
                    }
                }else{
                    next();
                }
        }
    }
    
    private void siguienteAuto() {
        for(int i = 0 ; i <= IndiceActual+pasos.size() ; i++){
            if (PasoActual < pasos.size()) {
                Pasos step = pasos.get(PasoActual++);
                if (Botones[step.getX()][step.getY()].getBackground() != Color.YELLOW &&Botones[step.getX()][step.getY()].getBackground() != Color.RED) {
                    Botones[step.getX()][step.getY()].setBackground(new java.awt.Color(0, 174, 152)); //Cian
                }
            }else{
                next();
                System.out.println(IndiceActual); //imprime el nuemero de nodos de la distancia más corta
            }
        }
    }

    private void anteriorBtn(){
        for(int i = 5 ; i >= 0 ; i--){
            prev();
            if (PasoActual > 0&& IndiceActual==0) {
                Pasos step = pasos.get(--PasoActual);
                Botones[step.getX()][step.getY()].setBackground(new java.awt.Color(1, 0, 231)); //azul marino
            }
        }
    }
    
    public void anteriorAuto(){
        for(int i=IndiceActual+pasos.size();i>=0;i--){
            prev();
            if (PasoActual > 0 && IndiceActual==0) {
                Pasos step = pasos.get(--PasoActual);
                Botones[step.getX()][step.getY()].setBackground(new java.awt.Color(1, 0, 231));       
            }
        }
    }
    
    public void next() {
        if(IndiceActual==caminoFinal-2){
            btnSiguiente.setEnabled(false);
        }else{
            if (IndiceActual <= CaminoCorto.length-1 ) {
                Nodo current = CaminoCorto[++IndiceActual];
                if (Botones[current.posFila][current.posColumna].getBackground() != Color.RED&&Botones[current.posFila][current.posColumna].getBackground() != Color.GREEN) {
                    Botones[current.posFila][current.posColumna].setBackground(Color.YELLOW);
                }
            }
        }
    }
    
    public void prev() {
        if (IndiceActual > 0) {
            Nodo current = CaminoCorto[IndiceActual];
            if (Botones[current.posFila][current.posColumna].getBackground() != Color.RED&&Botones[current.posFila][current.posColumna].getBackground() != Color.GREEN) {
                Botones[current.posFila][current.posColumna].setBackground(new java.awt.Color(0, 174, 152));
                IndiceActual--;
                btnSiguiente.setEnabled(true);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlContainer = new javax.swing.JPanel();
        pnlBtns = new javax.swing.JPanel();
        btnAnterior = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        mnuBar = new javax.swing.JMenuBar();
        Dijkstra = new javax.swing.JMenu();
        mnuIniciar = new javax.swing.JMenuItem();
        mnuAnterior = new javax.swing.JMenuItem();
        Opciones = new javax.swing.JMenu();
        mnuSalir = new javax.swing.JMenuItem();
        mnuLimpiar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(600, 675));
        setResizable(false);
        setSize(new java.awt.Dimension(600, 500));

        pnlContainer.setBackground(new java.awt.Color(0, 0, 0));
        pnlContainer.setLayout(new java.awt.BorderLayout());

        btnAnterior.setText("ANTERIOR");
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });
        pnlBtns.add(btnAnterior);

        btnSiguiente.setText("SIGUIENTE");
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });
        pnlBtns.add(btnSiguiente);

        Dijkstra.setText("Dijkstra");

        mnuIniciar.setText("Iniciar");
        mnuIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIniciarActionPerformed(evt);
            }
        });
        Dijkstra.add(mnuIniciar);

        mnuAnterior.setText("Anterior");
        mnuAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAnteriorActionPerformed(evt);
            }
        });
        Dijkstra.add(mnuAnterior);

        mnuBar.add(Dijkstra);

        Opciones.setText("Opciones");

        mnuSalir.setText("Salir");
        mnuSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSalirActionPerformed(evt);
            }
        });
        Opciones.add(mnuSalir);

        mnuLimpiar.setText("Limpiar");
        mnuLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLimpiarActionPerformed(evt);
            }
        });
        Opciones.add(mnuLimpiar);

        mnuBar.add(Opciones);

        setJMenuBar(mnuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBtns, javax.swing.GroupLayout.DEFAULT_SIZE, 798, Short.MAX_VALUE)
            .addComponent(pnlContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 575, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBtns, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuIniciarActionPerformed
        siguienteAuto();
    }//GEN-LAST:event_mnuIniciarActionPerformed

    private void mnuSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSalirActionPerformed
        int res = JOptionPane.showConfirmDialog(this, "¿Seguro que desea salir?", "EXIT", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_mnuSalirActionPerformed

    private void mnuAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAnteriorActionPerformed
        anteriorAuto();
    }//GEN-LAST:event_mnuAnteriorActionPerformed

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        anteriorBtn();
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        siguienteBtn();
    }//GEN-LAST:event_btnSiguienteActionPerformed

    private void mnuLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLimpiarActionPerformed
        reset();
    }//GEN-LAST:event_mnuLimpiarActionPerformed

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
            java.util.logging.Logger.getLogger(Vtn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Vtn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Vtn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Vtn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Vtn().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu Dijkstra;
    private javax.swing.JMenu Opciones;
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JMenuItem mnuAnterior;
    private javax.swing.JMenuBar mnuBar;
    private javax.swing.JMenuItem mnuIniciar;
    private javax.swing.JMenuItem mnuLimpiar;
    private javax.swing.JMenuItem mnuSalir;
    private javax.swing.JPanel pnlBtns;
    private javax.swing.JPanel pnlContainer;
    // End of variables declaration//GEN-END:variables
}
