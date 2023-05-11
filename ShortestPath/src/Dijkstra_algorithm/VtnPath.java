package Dijkstra_algorithm;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class VtnPath extends javax.swing.JFrame {
    JButton[][] btnsTablero;
    int filas = 20;
    int columnas = 20;
    Tablero tablero;
    int posFilaStart, posColumnaStart, posFilaEnd, posColumnaEnd;
    
    public VtnPath() {
        initComponents();
        this.setLocationRelativeTo(null);
        nuevoTablero();
    }

    public void nuevoTablero(){
        removerTablero();
        cargarControles();
        maquinasDeEstado();
        repaint();
        this.setLocationRelativeTo(null);
    }
    
    void removerTablero(){
        if (btnsTablero != null){
            for (int i = 0; i < btnsTablero.length; i++) {
                for (int j = 0; j < btnsTablero[i].length; j++) {
                    if (btnsTablero[i][j] != null){
                        pnlTablero.remove(btnsTablero[i][j]);
                    }
                }
            }
        }
    }
    
    private void cargarControles(){
        pnlTablero.setLayout(new GridLayout(filas, columnas));
        btnsTablero = new JButton[filas][columnas];
        for (int i = 0; i < btnsTablero.length; i++) {
            for (int j = 0; j < btnsTablero[i].length; j++) {
                btnsTablero[i][j] = new JButton();
                btnsTablero[i][j].setName(i +"," +j);
                btnsTablero[i][j].setSize(500, 500);
                btnsTablero[i][j].setBackground(Color.BLUE);
                
                btnsTablero[i][j].addMouseListener(new MouseAdapter(){
                    @Override
                    public void mousePressed(MouseEvent e){
                        mouseDetector(e);
                    }
                });
                pnlTablero.add(btnsTablero[i][j]);
            }
        }
    }
    
    private void maquinasDeEstado(){
        tablero = new Tablero(filas, columnas);
        tablero.setEventoNodosHermanos(new Consumer<Nodo>(){
            @Override
            public void accept(Nodo t){
                btnsTablero[t.getPosFila()][t.getPosColumna()].setBackground(Color.ORANGE);
            }
        });
        tablero.setEventoDeshacer(new Consumer<Nodo>(){
            @Override
            public void accept(Nodo t){
                btnsTablero[t.getPosFila()][t.getPosColumna()].setBackground(Color.BLUE);
                System.out.println("DESHACER: ["+t.getPosFila()+","+t.getPosColumna()+"]");
            }
        });
        tablero.setEventoStartBtn(new Consumer<Nodo>(){
            @Override
            public void accept(Nodo t){
                btnsTablero[t.getPosFila()][t.getPosColumna()].setBackground(Color.GREEN);
                //tablero.setStartBtn(t.getPosFila(), t.getPosColumna());
                System.out.println("se inicializó el btn START con el click izquierdo en: ["+t.getPosFila()+","+t.getPosColumna()+"]");
            }
        });
        tablero.setEventoEndBtn(new Consumer<Nodo>(){
            @Override
            public void accept(Nodo t){
                btnsTablero[t.getPosFila()][t.getPosColumna()].setBackground(Color.RED);
                //tablero.setEndBtn(t.getPosFila(), t.getPosColumna());
                System.out.println("se inicializó el btn END con el click derecho en: ["+t.getPosFila()+","+t.getPosColumna()+"]");
            }
        });
        tablero.setEventoBloqueo(new Consumer<Nodo>(){
            @Override
            public void accept(Nodo t){
                btnsTablero[t.getPosFila()][t.getPosColumna()].setBackground(Color.BLACK);
                System.out.println("se colocó una BARRERA en: ["+t.getPosFila()+","+t.getPosColumna()+"]");
            }
        });
        //tablero.imprimirTablero();
    }
    
    private void mouseDetector(MouseEvent e){
        JButton btn = (JButton)e.getSource();
        String[] coordenada = btn.getName().split(",");
        String[] coordenadaStart = btn.getName().split(",");
        String[] coordenadaEnd = btn.getName().split(",");
        int posFila, posColumna;
        switch (e.getButton()){
            case MouseEvent.BUTTON1:
                if (e.isControlDown()) {
                    posFila = Integer.parseInt(coordenada[0]);
                    posColumna = Integer.parseInt(coordenada[1]);
                    tablero.blockBtn(posFila, posColumna);
                } else{
                    posFilaStart = Integer.parseInt(coordenadaStart[0]);
                    posColumnaStart = Integer.parseInt(coordenadaStart[1]);
                    posFila = Integer.parseInt(coordenada[0]);
                    posColumna = Integer.parseInt(coordenada[1]);
                    tablero.startBtn(posFila, posColumna);
                }
                break;
            case MouseEvent.BUTTON3:
                posFilaEnd = Integer.parseInt(coordenadaEnd[0]);
                posColumnaEnd = Integer.parseInt(coordenadaEnd[1]);
                posFila = Integer.parseInt(coordenada[0]);
                posColumna = Integer.parseInt(coordenada[1]);
                //tablero.getPosEnd(posFilaEnd, posColumnaEnd);
                tablero.endBtn(posFila, posColumna);
                break;
        }
    }
    
    private void btnIniciar(){
        System.out.println("la posicion del boton start es: ["+posFilaStart+","+posColumnaStart+"]");
        tablero.floodAlgorithm(posFilaStart, posColumnaStart);
    }
    
/*
    private void dijkstra(int start) {
        shortestDistances[start] = 0;
        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingInt(i -> shortestDistances[i]));
        queue.add(start);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            if (visited[current]) {
                continue;
            }
            visited[current] = true;

            for (int i = 0; i < distances.length; i++) {
                if (distances[current][i] > 0 && !visited[i]) {
                    int distance = shortestDistances[current] + distances[current][i];
                    if (distance < shortestDistances[i]) {
                        shortestDistances[i] = distance;
                        previous[i] = current;
                        queue.add(i);
                    }
                }
            }
        }
    }
    
    private void highlightPath(int end) {
        int current = end;
        while (current != startNode) {
            int previousNode = previous[current];
        }
    }
*/

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTablero = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        mnuAlgoritmo = new javax.swing.JMenu();
        mnuIniciar = new javax.swing.JMenuItem();
        mnuReiniciar = new javax.swing.JMenuItem();
        mnuOpciones = new javax.swing.JMenu();
        mnuSalir = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Algoritmo de Dijkstra");
        setPreferredSize(new java.awt.Dimension(600, 600));
        setResizable(false);
        setSize(new java.awt.Dimension(600, 500));

        pnlTablero.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout pnlTableroLayout = new javax.swing.GroupLayout(pnlTablero);
        pnlTablero.setLayout(pnlTableroLayout);
        pnlTableroLayout.setHorizontalGroup(
            pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 792, Short.MAX_VALUE)
        );
        pnlTableroLayout.setVerticalGroup(
            pnlTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 601, Short.MAX_VALUE)
        );

        mnuAlgoritmo.setText("Algoritmo");

        mnuIniciar.setText("Iniciar");
        mnuIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuIniciarActionPerformed(evt);
            }
        });
        mnuAlgoritmo.add(mnuIniciar);

        mnuReiniciar.setText("Reiniciar");
        mnuReiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuReiniciarActionPerformed(evt);
            }
        });
        mnuAlgoritmo.add(mnuReiniciar);

        menuBar.add(mnuAlgoritmo);

        mnuOpciones.setText("Opciones");

        mnuSalir.setText("Salir");
        mnuSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSalirActionPerformed(evt);
            }
        });
        mnuOpciones.add(mnuSalir);

        menuBar.add(mnuOpciones);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTablero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTablero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuReiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuReiniciarActionPerformed
        nuevoTablero();
    }//GEN-LAST:event_mnuReiniciarActionPerformed

    private void mnuSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSalirActionPerformed
        int res = JOptionPane.showConfirmDialog(this, "¿Seguro que desea salir?", "Exit", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_mnuSalirActionPerformed

    private void mnuIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuIniciarActionPerformed
        btnIniciar();
    }//GEN-LAST:event_mnuIniciarActionPerformed

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
            java.util.logging.Logger.getLogger(VtnPath.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VtnPath.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VtnPath.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VtnPath.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VtnPath().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu mnuAlgoritmo;
    private javax.swing.JMenuItem mnuIniciar;
    private javax.swing.JMenu mnuOpciones;
    private javax.swing.JMenuItem mnuReiniciar;
    private javax.swing.JMenuItem mnuSalir;
    private javax.swing.JPanel pnlTablero;
    // End of variables declaration//GEN-END:variables
}
