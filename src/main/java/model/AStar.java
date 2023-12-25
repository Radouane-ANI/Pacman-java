package model;

import java.util.*;

import config.MazeConfig;
import geometry.IntCoordinates;

public class AStar {

    private static final int[][] DIRECTIONS = new int[][] { { 0, -1 }, { 1, 0 }, { 0, 1 }, { -1, 0 } };
    private static MazeConfig config = null;

    public static Character findPath(MazeConfig mconfig, IntCoordinates start, IntCoordinates end) {
        // Créer une file pour stocker les nœuds à explorer.
        Queue<Node> queue = new PriorityQueue<>();
    
        config = mconfig;
        // Ajouter le nœud de départ à la file.
        Node startNode = new Node(start, 0, start.distance(end), 'a');
        queue.add(startNode);
    
        // Créer un ensemble pour stocker les nœuds déjà explorés.
        Set<IntCoordinates> explored = new HashSet<>();
    
        // Tant que la file n'est pas vide, explorer les nœuds.
        while (!queue.isEmpty()) {
            // Obtenir le nœud en tête de la file.
            Node currentNode = queue.poll();
            
            //System.out.println("Exploring Node: " + currentNode.position + " - Direction: " + currentNode.direction);
    
            // Si le nœud actuel est le nœud de destination, renvoyer le chemin.
            if (currentNode.position.equals(end)) {
                //System.out.println("Path Found!");
                return currentNode.direction;
            }
    
            // Explorer les nœuds voisins.
            for (int[] direction : DIRECTIONS) {
                IntCoordinates neighborPosition = currentNode.position.add(direction[0], direction[1]);
    
                if (isValidCell(currentNode.position, neighborPosition)) {
                    //System.out.println("Exploring Neighbor: " + neighborPosition);
                    
                    if (!explored.contains(neighborPosition)) {
                        Node neighborNode = new Node(neighborPosition, currentNode, currentNode.cost + neighborPosition.distance(end));
                        queue.add(neighborNode);                                
                    }
                }
            }
            // Ajouter le nœud actuel à l'ensemble des nœuds explorés.
            explored.add(currentNode.position);
    
        }
    
        // Si aucun chemin n'est trouvé, renvoyer une liste vide.
        //System.out.println("No Path Found");
        return 'a';
    }
    
    private static boolean isValidCell(IntCoordinates position, IntCoordinates neighborPosition) {
        if (neighborPosition.x() < 0 || neighborPosition.x() >= config.getWidth() || neighborPosition.y() < 0
                || neighborPosition.y() >= config.getHeight()) {
            return false;
        }

        char dir = getDirection(position, neighborPosition);

        switch (dir) {
            case 'n':
                return !config.getCell(position).isnorthWall();
            case 's':
                return !config.getCell(position).issouthWall();
            case 'e':
                return !config.getCell(position).iseastWall();
            case 'w':
                return !config.getCell(position).iswestWall();
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
        private final char direction;

        public Node(IntCoordinates position, int cost, int heuristic, char direction) {
            this.position = position;
            this.cost = cost;
            this.heuristic = heuristic;
            this.direction = direction;
        }

        public Node(IntCoordinates neighborPosition, Node currentNode, int heuristic) {
            this.position = neighborPosition;
            this.cost = currentNode.cost + 1;
            this.heuristic = heuristic;
            if (currentNode.direction == 'a') {
                this.direction = getDirection(currentNode.position, neighborPosition);
            } else {
                this.direction = currentNode.direction;
            }
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