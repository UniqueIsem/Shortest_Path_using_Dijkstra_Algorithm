package Dijkstra_algorithm;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Tablero {
    int filas;
    int columnas;
    Nodo[][] nodo;
    boolean startAsignado = false;
    boolean endAsignado = false;
    boolean endEncontrado = false;
    
    private Consumer<Nodo> eventoDeshacer;
    private Consumer<Nodo> eventoStartBtn;
    private Consumer<Nodo> eventoEndBtn;
    private Consumer<Nodo> eventoBloqueo;
    private Consumer<Nodo> eventoNodosHermanos;
    
    public Tablero(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        inicializarCasillas();
    }
    
    public void inicializarCasillas(){
        nodo = new Nodo[this.filas][this.columnas];
        for (int i = 0; i < nodo.length ; i++) {
            for (int j = 0; j < nodo[i].length ; j++) {
                nodo[i][j] = new Nodo(i, j);
                nodo[i][j].setValue(0);
            }
        }
    }
    
    public void startBtn(int fila, int columna){
        if (!startAsignado) {
            startAsignado = true;
            this.nodo[fila][columna].setValue(1);
            this.nodo[fila][columna].setStart(true);
            //this.nodo[posFila][posColumna].setAbierta(true);
            eventoStartBtn.accept(this.nodo[fila][columna]);
        }else if (this.nodo[fila][columna].isStart()){
            startAsignado = false;
            this.nodo[fila][columna].setValue(0);
            this.nodo[fila][columna].setStart(false);
            //this.nodo[fila][columna].setAbierta(false);
            eventoDeshacer.accept(this.nodo[fila][columna]);
        }
    }
    
    public void endBtn(int fila, int columna){
        if (!endAsignado) {
            endAsignado = true;
            this.nodo[fila][columna].setValue(1);
            this.nodo[fila][columna].setEnd(true);
            //this.nodo[fila][columna].setAbierta(true);
            eventoEndBtn.accept(this.nodo[fila][columna]);
        }else if (this.nodo[fila][columna].isEnd()){
            endAsignado = false;
            this.nodo[fila][columna].setValue(0);
            this.nodo[fila][columna].setEnd(false);
            //this.nodo[fila][columna].setAbierta(false);
            eventoDeshacer.accept(this.nodo[fila][columna]);
        }
    }
    
    public void blockBtn(int posFila, int posColumna){
        this.nodo[posFila][posColumna].setValue(999);
        this.nodo[posFila][posColumna].setBloque(true);
        this.nodo[posFila][posColumna].setAbierta(true);
        eventoBloqueo.accept(this.nodo[posFila][posColumna]);
    }
    
    public void setStartBtn(int posFila, int posColumna){
        nodo[posFila][posColumna].setStart(true);
    }
    
    public void setEndBtn(int posFila, int posColumna){
        nodo[posFila][posColumna].setEnd(true);
    }
    
    public void marcarCasillaAbierta(int posFila, int posColumna){
        if (!this.nodo[posFila][posColumna].isAbierta()) {
            this.nodo[posFila][posColumna].setAbierta(true);
        }
    }
    
    /*public int[] getPosEnd(int posFilaEnd, int posColumnaEnd){
        int[] posEnd = new int[2];
        posEnd[0] = posFilaEnd;
        posEnd[1] = posColumnaEnd;
        return posEnd;
    }*/
    
    public void floodAlgorithm(int fila, int columna){
        marcarCasillaAbierta(fila, columna);
        List<Nodo> nodosHermanosList = obtenerNodosHermanos(fila, columna);//se obtienen los nodos hermanos en una lista
        Nodo[] nodosHermanos = nodosHermanosList.toArray(new Nodo[nodosHermanosList.size()]);//se convierten en array
        //for (Nodo nodo: nodosHermanos){
        for (int i=0 ; i < 4; i++){
            while (!nodosHermanos[i].isAbierta()){
                if (!nodosHermanos[i].isBloque() && !nodosHermanos[i].isEnd()) {
                    nodosHermanos[i].setAbierta(true);
                    nodosHermanos[i].setActual(true);
                    nodosHermanos[i].setValue(1); //CONSOLA
                    eventoNodosHermanos.accept(this.nodo[nodosHermanos[i].getPosFila()][nodosHermanos[i].getPosColumna()]); //pinta los nodos hermanos de naranja
                    imprimirTablero();
                    floodAlgorithm(nodosHermanos[i].getPosFila(),nodosHermanos[i].getPosColumna());
                } else if (nodosHermanos[i].isEnd()) {
                    System.out.println("END ENCONTRADO!!!");
                    nodosHermanos[i].setAbierta(true);
                    i = 4;
                    break;
                }
            }
            //}
        }
        //dijkstraAlgorithm();
    }
    
    public void dijkstraAlgorithm(){
        //imprimirTablero();
    }
    
    private List<Nodo> obtenerNodosHermanos(int posFila, int posColumna){
        List<Nodo> listaNodos = new ArrayList<Nodo>();
        for (int i = 0; i < 4; i++) {
            int tmpPosFila = posFila;
            int tmpPosColumna = posColumna;
            switch(i){
                case 0: tmpPosFila--;
                    break; //Arriba
                case 1: tmpPosColumna++;
                    break; //Derecha
                case 2: tmpPosFila++;
                    break; //Abajo
                case 3: tmpPosColumna--;
                    break; //Izquierda
            }
            if (tmpPosFila >= 0 && tmpPosFila < this.nodo.length && tmpPosColumna >= 0 && tmpPosColumna < this.nodo[0].length) {
                listaNodos.add(this.nodo[tmpPosFila][tmpPosColumna]);
            }
        }
        return listaNodos;
    }

    public void setEventoNodosHermanos(Consumer<Nodo> eventoNodosHermanos) {
        this.eventoNodosHermanos = eventoNodosHermanos;
    }
    
    public void setEventoStartBtn(Consumer<Nodo> eventoStartBtn) {
        this.eventoStartBtn = eventoStartBtn;
    }

    public void setEventoEndBtn(Consumer<Nodo> eventoEndBtn) {
        this.eventoEndBtn = eventoEndBtn;
    }

    public void setEventoBloqueo(Consumer<Nodo> eventoBloqueo) {
        this.eventoBloqueo = eventoBloqueo;
    }
    
    public void imprimirTablero(){ 
       for (int i = 0; i < nodo.length ; i++) {
            for (int j = 0; j < nodo[i].length ; j++) {
                System.out.print(nodo[i][j].getValue() == 0 ? ". " : "1 "); //CONSOLA
            }
            System.out.println("");
        }
        System.out.println("");
    }

    /**
     * @param eventoDeshacer the eventoDeshacer to set
     */
    public void setEventoDeshacer(Consumer<Nodo> eventoDeshacer) {
        this.eventoDeshacer = eventoDeshacer;
    }
}

    