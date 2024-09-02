package Estruturas;


public class Aresta {
    public int destino,origem,peso;

    public Aresta(int origem,  int destino, int peso) {
        this.destino = destino;
        this.origem = origem;
        this.peso = peso;
    }
}