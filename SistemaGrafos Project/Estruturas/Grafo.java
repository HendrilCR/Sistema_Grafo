package Estruturas;

import java.awt.*;
import javax.swing.*;
import java.util.List;
import java.util.*;

public class Grafo {
    public Map<Integer, List<Aresta>> adjacencias;
    public int numVertices;

    public Grafo() {
        adjacencias = new HashMap<>();
        numVertices = 0;
    }

    public void inserirVertice(int id) {
        if (!adjacencias.containsKey(id)) {
            adjacencias.put(id, new ArrayList<>());
            numVertices++;
            System.out.println(id + " adicionado!");
        } else {
            System.out.println("Este vértice já existe.");
        }
    }

    public void inserirAresta(int origem, int destino, int peso) {
        adjacencias.get(origem).add(new Aresta(origem,destino, peso));
        adjacencias.get(destino).add(new Aresta(destino,origem, peso));
    }

    public void removerVertice(int id) {
        adjacencias.values().forEach(list -> list.removeIf(aresta -> aresta.destino == id));
        if (adjacencias.remove(id) == null) {
            System.out.println("Vértice não existe.");
        }
        numVertices--;
    }

    public void removerAresta(int origem, int destino) {
        adjacencias.get(origem).removeIf(aresta -> aresta.destino == destino);
        adjacencias.get(destino).removeIf(aresta -> aresta.destino == origem);
    }

    public int getGrau(int vertice) {
        if (!adjacencias.containsKey(vertice)) {
            System.out.println("Vertice " + vertice + " nao existe.");
            return -1;
        }
        return adjacencias.getOrDefault(vertice, Collections.emptyList()).size();
    }

    public void mostrarMatriz() {
        List<Integer> vertices = new ArrayList<>(adjacencias.keySet());
        Collections.sort(vertices);

        int tamanho = vertices.size();
        int[][] matriz = new int[tamanho][tamanho];

        for (int i = 0; i < tamanho; i++) {
            for (Aresta aresta : adjacencias.get(vertices.get(i))) {
                int j = vertices.indexOf(aresta.destino);
                matriz[i][j] = aresta.peso;
            }
        }
        System.out.print("   ");
        for (int i = 0; i < tamanho; i++) {
            System.out.print(vertices.get(i) + " ");
        }

        System.out.println();
        for (int i = 0; i < tamanho; i++) {
            System.out.print(vertices.get(i) + "  ");
            for (int j = 0; j < tamanho; j++) {
                System.out.print(matriz[i][j] + " ");
            }
            System.out.println();
        }

    }

    public void buscaProfundidade(int verticeRaiz, int verticeAlvo) {
        Set<Integer> visitados = new HashSet<>();
        List<Integer> caminho = new ArrayList<>();
        boolean encontrado = dfs(verticeRaiz, verticeAlvo, visitados, caminho);

        if (encontrado) {
            System.out.println("Caminho: " + caminho);
        } else {
            System.out.println("Não existe um caminho entre " + verticeRaiz + " e " + verticeAlvo);
        }
    }

    private boolean dfs(int verticeAtual, int verticeAlvo, Set<Integer> visitados, List<Integer> caminho) {
        visitados.add(verticeAtual);
        caminho.add(verticeAtual);

        if (verticeAtual == verticeAlvo) {
            return true;
        }

        for (Aresta aresta : adjacencias.getOrDefault(verticeAtual, Collections.emptyList())) {
            if (!visitados.contains(aresta.destino)) {
                if (dfs(aresta.destino, verticeAlvo, visitados, caminho)) {
                    return true;
                }
            }
        }

        caminho.remove(caminho.size() - 1);
        return false;
    }

    public void buscaAmplitude(int verticeRaiz, int verticeAlvo) {
        Set<Integer> visitados = new HashSet<>();
        Map<Integer, Integer> antecessor = new HashMap<>();
        Queue<Integer> fila = new LinkedList<>();

        fila.add(verticeRaiz);
        visitados.add(verticeRaiz);
        antecessor.put(verticeRaiz, null);

        while (!fila.isEmpty()) {
            int verticeAtual = fila.poll();

            if (verticeAtual == verticeAlvo) {
                imprimirCaminho(verticeAlvo, antecessor);
                return;
            }

            for (Aresta aresta : adjacencias.getOrDefault(verticeAtual, Collections.emptyList())) {
                if (!visitados.contains(aresta.destino)) {
                    visitados.add(aresta.destino);
                    antecessor.put(aresta.destino, verticeAtual);
                    fila.add(aresta.destino);
                }
            }
        }

        System.out.println("Caminho não encontrado entre " + verticeRaiz + " e " + verticeAlvo);
    }

    private void imprimirCaminho(int verticeAlvo, Map<Integer, Integer> antecessor) {
        List<Integer> caminho = new ArrayList<>();
        for (Integer v = verticeAlvo; v != null; v = antecessor.get(v)) {
            caminho.add(v);
        }
        Collections.reverse(caminho);
        System.out.println("Caminho : " + caminho);
    }

    public boolean grafoConexo() {
        if (adjacencias.isEmpty()) {
            return true;
        }

        Set<Integer> visitados = new HashSet<>();
        Integer verticeInicial = adjacencias.keySet().iterator().next();
        buscarConexoes(verticeInicial, visitados);

        return visitados.size() == adjacencias.size();
    }

    private void buscarConexoes(int verticeAtual, Set<Integer> visitados) {
        visitados.add(verticeAtual);

        for (Aresta aresta : adjacencias.getOrDefault(verticeAtual, Collections.emptyList())) {
            if (!visitados.contains(aresta.destino)) {
                buscarConexoes(aresta.destino, visitados);
            }
        }
    }

    public List<Integer> dijkstra(int origem, int destino) {
        Map<Integer, Integer> dist = new HashMap<>(), prev = new HashMap<>();
        PriorityQueue<ArestaPrioridade> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.distancia));
        pq.add(new ArestaPrioridade(origem, 0));
        dist.put(origem, 0);

        while (!pq.isEmpty()) {
            int atual = pq.poll().vertice;
            if (atual == destino) break;
            for (Aresta a : adjacencias.getOrDefault(atual, Collections.emptyList())) {
                int novaDist = dist.getOrDefault(atual, Integer.MAX_VALUE) + a.peso;
                if (novaDist < dist.getOrDefault(a.destino, Integer.MAX_VALUE)) {
                    dist.put(a.destino, novaDist);
                    prev.put(a.destino, atual);
                    pq.add(new ArestaPrioridade(a.destino, novaDist));
                }
            }
        }

        List<Integer> caminho = new ArrayList<>();
        for (Integer v = destino; v != null; v = prev.get(v)) caminho.add(v);
        Collections.reverse(caminho);
        return caminho.isEmpty() || caminho.get(0) != origem ? Collections.emptyList() : caminho;
    }

    private static class ArestaPrioridade {
        int vertice, distancia;

        ArestaPrioridade(int vertice, int distancia) {
            this.vertice = vertice;
            this.distancia = distancia;
        }
    }

    public void gerarMST() {
        Set<Integer> visitados = new HashSet<>();
        PriorityQueue<Aresta> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a.peso));
        List<Aresta> mst = new ArrayList<>();

        Integer verticeInicial = adjacencias.keySet().iterator().next();
        visitados.add(verticeInicial);
        pq.addAll(adjacencias.get(verticeInicial));

        while (!pq.isEmpty()) {
            Aresta aresta = pq.poll();
            if (visitados.contains(aresta.destino)) continue;

            visitados.add(aresta.destino);
            mst.add(aresta);
            pq.addAll(adjacencias.get(aresta.destino));
        }

        JFrame frame = new JFrame("Árvore Geradora Mínima (Prim)");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 800);
        frame.add(new JPanel() {
            Map<Integer, Point> coordenadas = gerarCoordenadas();

            private Map<Integer, Point> gerarCoordenadas() {
                Map<Integer, Point> coords = new HashMap<>();
                int radius = 150;
                int centerX = 400;
                int centerY = 400;
                int n = adjacencias.size();
                int i = 0;

                for (Integer vertice : adjacencias.keySet()) {
                    int x = (int) (centerX + radius * Math.cos(2 * Math.PI * i / n));
                    int y = (int) (centerY + radius * Math.sin(2 * Math.PI * i / n));
                    coords.put(vertice, new Point(x, y));
                    i++;
                }
                return coords;
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                // Desenha as arestas da MST
                g.setColor(Color.GREEN);
                for (Aresta aresta : mst) {
                    Point p1 = coordenadas.get(aresta.origem);
                    Point p2 = coordenadas.get(aresta.destino);
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }

                // Desenha os vértices
                for (Map.Entry<Integer, Point> entry : coordenadas.entrySet()) {
                    int vertice = entry.getKey();
                    Point p = entry.getValue();
                    g.setColor(Color.WHITE);
                    g.fillOval(p.x - 10, p.y - 10, 20, 20);
                    g.setColor(Color.BLACK);
                    g.drawString(String.valueOf(vertice), p.x - 5, p.y + 5);
                }
            }
        });
        frame.setVisible(true);
    }


}