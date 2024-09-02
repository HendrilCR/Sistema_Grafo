import Estruturas.*;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

import java.util.Scanner;

public class Main{
    public static Grafo grafo;

    public static void main(String args[]){

        //Grafo de testes
        grafo = new Grafo();

        grafo.inserirVertice(1);
        grafo.inserirVertice(2);
        grafo.inserirVertice(3);
        grafo.inserirVertice(4);
        grafo.inserirVertice(5);

        grafo.inserirAresta(1,2,1);
        grafo.inserirAresta(2,3,1);
        grafo.inserirAresta(2,4,1);
        grafo.inserirAresta(5,4,1);
        grafo.inserirAresta(3,5,10);

        menu();





    }

    public static void menu(){
        Scanner s1 = new Scanner(System.in);
        boolean sair = false;

        while(!sair){
            System.out.println("=".repeat(27));
            System.out.println("| Gerenciamento de Grafos |");
            System.out.println("=".repeat(27));

            System.out.println("[1] Inserir Vértice");
            System.out.println("[2] Inserir Aresta");
            System.out.println("[3] Remover Vértice");
            System.out.println("[4] Remover Aresta");
            System.out.println("[5] Visualizar Grafo");
            System.out.println("[6] Informar Grau de um Vértice");
            System.out.println("[7] Verificar se o Grafo é Conexo");
            System.out.println("[8] Converter para Matriz de Adjacência");
            System.out.println("[9] Caminhamento em Amplitude (BFS)");
            System.out.println("[10] Caminhamento em Profundidade (DFS)");
            System.out.println("[11] Caminho Mínimo (Dijkstra)");
            System.out.println("[12] Árvore Geradora Mínima (Prim)");
            System.out.println("[0] Sair");

            int opcao = s1.nextInt();

            switch(opcao){
                case 0:
                    System.out.println("Saindo...");
                    sair = true;
                    break;
                case 1:
                    System.out.print("Insira o valor do vértice: ");
                    int valor = s1.nextInt();

                    grafo.inserirVertice(valor);

                    break;
                case 2:
                    System.out.print("Insira o valor do vértice de origem: ");
                    int valorOrigem = s1.nextInt();

                    System.out.print("Insira o valor do vértice de destino: ");
                    int valorDestino = s1.nextInt();

                    System.out.print("Insira o valor do peso da aresta: ");
                    int pesoAresta = s1.nextInt();

                    grafo.inserirAresta(valorOrigem,valorDestino,pesoAresta);
                    System.out.println("Aresta de "+valorOrigem+" a "+valorDestino+" adicionado!");

                    break;
                case 3:
                    System.out.print("Insira o valor do vértice a ser removido: ");
                    valor = s1.nextInt();

                    grafo.removerVertice(valor);
                    break;
                case 4:
                    System.out.print("Insira o vertice de origem da aresta: ");
                    int origem = s1.nextInt();

                    System.out.print("Insira o vertice de destino da aresta: ");
                    int destino = s1.nextInt();

                    grafo.removerAresta(origem,destino);

                    break;
                case 5:
                    Visual.visualizarGrafo(grafo.adjacencias);
                    break;
                case 6:
                    System.out.print("Insira o vertice para encontrar o grau: ");
                    int vertice = s1.nextInt();
                    System.out.println("Grau: "+grafo.getGrau(vertice));
                    break;
                case 9:
                    System.out.print("Insira o valor do vértice de origem: ");
                    valorOrigem = s1.nextInt();
                    System.out.print("Insira o valor do vértice de destino: ");
                    valorDestino = s1.nextInt();

                    grafo.buscaAmplitude(valorOrigem,valorDestino);

                    break;
                case 10:
                    System.out.print("Insira o valor do vértice de origem: ");
                    valorOrigem = s1.nextInt();
                    System.out.print("Insira o valor do vértice de destino: ");
                    valorDestino = s1.nextInt();

                    grafo.buscaProfundidade(valorOrigem,valorDestino);

                    break;
                case 7:
                    System.out.println("Grafo é conexo: "+grafo.grafoConexo());
                    break;
                case 11:
                    System.out.print("Insira o valor do vértice de origem: ");
                    valorOrigem = s1.nextInt();
                    System.out.print("Insira o valor do vértice de destino: ");
                    valorDestino = s1.nextInt();

                    System.out.println("Caminho utilizando Dijkstra: "+grafo.dijkstra(valorOrigem,valorDestino));
                    break;
                case 8:
                    grafo.mostrarMatriz();
                    break;
                case 12:
                    grafo.gerarMST();
                    break;
                default:
                    System.out.println("Insira uma opção válida.");
                    break;
            }
        }

    }

}


class Visual extends JPanel {
    private Map<Integer, List<Aresta>> adjacencias;
    private Map<Integer, Point> coordenadas;

    static public void visualizarGrafo(Map adjacencias) {
        JFrame frame = new JFrame("Ver Grafo");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new Visual(adjacencias));
        frame.setSize(800, 800);
        frame.setVisible(true);
    }

    public Visual(Map<Integer, List<Aresta>> adjacencias) {
        this.adjacencias = adjacencias;
        this.coordenadas = new HashMap<>();
        gerarCoordenadas();
    }

    private void gerarCoordenadas() {
        int radius = 150;
        int centerX = 300;
        int centerY = 300;
        int n = adjacencias.size();
        int i = 0;

        for (Integer vertice : adjacencias.keySet()) {
            int x = (int) (centerX + radius * Math.cos(2 * Math.PI * i / n));
            int y = (int) (centerY + radius * Math.sin(2 * Math.PI * i / n));
            coordenadas.put(vertice, new Point(x, y));
            i++;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,g.getClipBounds().width, g.getClipBounds().height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.BOLD, 12));



        for (Map.Entry<Integer, List<Aresta>> entry : adjacencias.entrySet()) {
            int origem = entry.getKey();
            Point p1 = coordenadas.get(origem);

            //Desenha as arestas na tela como linhas :D
            for (Aresta aresta : entry.getValue()) {
                Point p2 = coordenadas.get(aresta.destino);
                g.setColor(Color.darkGray);
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
                g.setColor(Color.WHITE);
                g.drawString("Visualização do Grafo",4,16);
                g.drawString("Sistema por Hêndril C. Ribeiro",4,32);
                g.drawString("Vértices: "+String.valueOf(Main.grafo.numVertices),4,48);
                g.drawString(String.valueOf(aresta.peso), (p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
            }
        }

        //Desenha as vértices na tela :D
        for (Map.Entry<Integer, Point> entry : coordenadas.entrySet()) {
            int vertice = entry.getKey();
            Point p = entry.getValue();
            g.setColor(Color.WHITE);
            g.fillOval(p.x - 10, p.y - 10, 30, 30);
            g.setColor(Color.DARK_GRAY);
            g.drawString(String.valueOf(vertice), p.x, p.y + 10);
        }


    }
}