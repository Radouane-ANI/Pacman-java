package model;

import java.util.*;

import config.MazeConfig;
import geometry.IntCoordinates;

public class AStar {

    private static final int[][] DIRECTIONS = new int[][] { { 0, -1 }, { 1, 0 }, { 0, 1 }, { -1, 0 } };
    private static MazeConfig config = null;

    public static List<Character> findPath(MazeConfig mconfig, IntCoordinates start, IntCoordinates end) {
        long time = System.nanoTime();
        // Créer une file d'attente pour stocker les nœuds à explorer.
        Queue<Node> queue = new PriorityQueue<>();

        config = mconfig;
        // Ajouter le nœud de départ à la file d'attente.
        Node startNode = new Node(start, 0, start.distance(end));
        queue.add(startNode);

        // Créer un ensemble pour stocker les nœuds déjà explorés.
        Set<Node> explored = new HashSet<>();

        // Tant que la file d'attente n'est pas vide, explorer les nœuds.
        while (!queue.isEmpty()) {
            // Obtenir le nœud en tête de la file d'attente.
            Node currentNode = queue.poll();

            // Si le nœud actuel est le nœud de destination, renvoyer le chemin.
            if (currentNode.position.equals(end)) {
                return currentNode.path;
            } // System.out.println(currentNode.cost);
              // System.out.println(currentNode.heuristic);

            // Explorer les nœuds voisins.
            for (int[] direction : DIRECTIONS) {
                IntCoordinates neighborPosition = currentNode.position.add(direction[0], direction[1]);

                if (isValidCell(currentNode.position, neighborPosition)) { // Vérifier que le nœud voisin
                                                                                       // est valide.
                    // Si le nœud voisin est valide et n'a pas encore été exploré, l'ajouter à la
                    // file d'attente.
                    if (!explored.contains(neighborPosition)) {
                        Node neighborNode = new Node(neighborPosition, currentNode.cost + 1,
                                currentNode.cost + neighborPosition.distance(end));
                        neighborNode.path.addAll(currentNode.path);
                        neighborNode.path.add(getDirection(currentNode.position, neighborPosition));
                        queue.add(neighborNode);

                    }
                }
            }
            // Ajouter le nœud actuel à l'ensemble des nœuds explorés.
            explored.add(currentNode);

            if(System.nanoTime()-time>10000000){
                return new ArrayList<>();
            }
        }

        // Si aucun chemin n'est trouvé, renvoyer une liste vide.
        return new ArrayList<>();
    }

    private static boolean isValidCell(IntCoordinates currentPosition, IntCoordinates neighborPosition) {
        if (neighborPosition.x() < 0 || neighborPosition.x() >= config.getWidth() || neighborPosition.y() < 0
                || neighborPosition.y() >= config.getHeight()) {
            return false;
        }
        char dir = getDirection(currentPosition, neighborPosition);

        switch (dir) {
            case 'n':
                return !config.getCell(currentPosition).northWall();
            case 's':
                return !config.getCell(currentPosition).southWall();
            case 'e':
                return !config.getCell(currentPosition).eastWall();
            case 'w':
                return !config.getCell(currentPosition).westWall();
            default:
                return false;
        }
    }

    private static Character getDirection(IntCoordinates start, IntCoordinates end) {
        int dx = end.x() - start.x();
        int dy = end.y() - start.y();

        if (dx > 0) {
            return 'e';
        } else if (dx < 0) {
            return 'w';
        } else if (dy > 0) {
            return 's';
        } else {
            return 'n';
        }
    }

    private static class Node implements Comparable<Node> {
        private final IntCoordinates position;
        private final int cost;
        private final int heuristic;
        private final List<Character> path;

        public Node(IntCoordinates position, int cost, int heuristic) {
            this.position = position;
            this.cost = cost;
            this.heuristic = heuristic;
            this.path = new ArrayList<>();
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.f(), other.f());
        }

        private int f() {
            return this.cost + this.heuristic;
        }
    }
}