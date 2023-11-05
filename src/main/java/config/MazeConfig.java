package config;

import geometry.IntCoordinates;

import static config.Cell.Content.DOT;
import static config.Cell.*;
import static config.Cell.Content.NOTHING;
import static config.Cell.Content.ENERGIZER; // Ajout de la constante ENERGIZER
import java.io.File ; // Ajout de la classe File
import java.io.FileNotFoundException ; // Ajout de la classe FileNotFoundException pour gérer l'exception si le fichier n'extiste pas
import java.util.Scanner ; // Ajout de la classe Scanner pour lire le fichier
import java.util.ArrayList;
import java.util.HashMap ; // Ajout de la classe HashMap pour stocker les caractères du fichier
import java.util.List;
import java.util.Map;

import config.Cell.Content;

public class MazeConfig {
    public MazeConfig(Cell[][] grid, IntCoordinates pacManPos, IntCoordinates blinkyPos, IntCoordinates pinkyPos,
                      IntCoordinates inkyPos, IntCoordinates clydePos) {
        this.grid = new Cell[grid.length][grid[0].length];
        for (int i = 0; i < getHeight(); i++) {
            if (getWidth() >= 0) System.arraycopy(grid[i], 0, this.grid[i], 0, getHeight());
        }
        this.pacManPos = pacManPos;
        this.blinkyPos = blinkyPos;
        this.inkyPos = inkyPos;
        this.pinkyPos = pinkyPos;
        this.clydePos = clydePos;
    }

    private final Cell[][] grid;
    private final IntCoordinates pacManPos, blinkyPos, pinkyPos, inkyPos, clydePos;
    private static Map<Character, Cell> itemDictionary = new HashMap<>();
    private static void iniDictionary(){
        if(itemDictionary.size() != 0) return;
        itemDictionary.put('A', open(ENERGIZER));
        itemDictionary.put('B', closed(ENERGIZER));
        itemDictionary.put('C', hPipe(ENERGIZER));
        itemDictionary.put('D', vPipe(ENERGIZER));
        itemDictionary.put('E', swVee(ENERGIZER));
        itemDictionary.put('F', nwVee(ENERGIZER));
        itemDictionary.put('G', neVee(ENERGIZER));
        itemDictionary.put('H', seVee(ENERGIZER));
        itemDictionary.put('I', nTee(ENERGIZER));
        itemDictionary.put('J', eTee(ENERGIZER));
        itemDictionary.put('K', sTee(ENERGIZER));
        itemDictionary.put('L', wTee(ENERGIZER));
        itemDictionary.put('M', nU(ENERGIZER));
        itemDictionary.put('N', eU(ENERGIZER));
        itemDictionary.put('O', sU(ENERGIZER));
        itemDictionary.put('P', wU(ENERGIZER));
    }
    public IntCoordinates getPacManPos() {
        return pacManPos;
    }

    public IntCoordinates getBlinkyPos() {
        return blinkyPos;
    }

    public IntCoordinates getPinkyPos() {
        return pinkyPos;
    }

    public IntCoordinates getInkyPos() {
        return inkyPos;
    }

    public IntCoordinates getClydePos() {
        return clydePos;
    }

    public int getWidth() {
        return grid[0].length;
    }

    public int getHeight() {
        return grid.length;
    }

    public Cell getCell(IntCoordinates pos) {
        return grid[Math.floorMod(pos.y(), getHeight())][Math.floorMod(pos.x(), getWidth())];
    }


    // simple example with a square shape
    // TODO: mazes should be loaded from a text file
    public static MazeConfig makeExample1() {
        File maze = new File("src/main/resources/maze.txt") ; // Création d'un objet File qui contient le fichier maze.txt
        iniDictionary();
        try{
            Scanner scanner = new Scanner(maze) ; // Création d'un objet Scanner pour lire le fichier
            int height = scanner.nextInt() ; // Lecture de la hauteur du labyrinthe
            int width = scanner.nextInt() ; // Lecture de la largeur du labyrinthe
            Cell[][] grid = new Cell[height][width] ; // Création d'un tableau de cellules de taille height x width
            int i = 0;  // Compteur pour parcourir les lignes du fichier
            scanner.nextLine();
            while(scanner.hasNextLine()){
                String line = scanner.nextLine();
                int ji = -1;
                for (int j = 0; j < line.length(); j++) {
                    ji++; // index de la colonne
                    char currentChar = line.charAt(j); // caractère courant
                    char nextChar = (j + 1 < line.length()) ? line.charAt(j + 1) : ' '; // caractère suivant
            
                    if (itemDictionary.containsKey(currentChar)) { // Si le caractère courant est présent dans le dictionnaire
                        Cell currentItem = itemDictionary.get(currentChar); // On récupère la cellule correspondante
                        Content nextItem = (nextChar == 'e') ? ENERGIZER : (nextChar == 'd') ? DOT : NOTHING; // On récupère le contenu de la cellule coreespondante au caractère suivant
                        grid[i][ji] = currentItem.updateNextItemType(nextItem); // On met à jour le contenu de la cellule
            
                        if (nextChar == 'e' || nextChar == 'd') {
                            j += 1;
                        }
                    }
                    
                }
                i++;
            }
                return new MazeConfig(grid,
                new IntCoordinates(3, 0),
                new IntCoordinates(0, 3),
                new IntCoordinates(3, 5),
                new IntCoordinates(5, 5),
                new IntCoordinates(5, 1)
                );
            }
        catch (FileNotFoundException e) { // Si le fichier n'existe pas
        return null;

        }
        
    }

//     public List<IntCoordinates> getDotCoordinates() { // on recupere les coordonées ou sont placée les dots
//     List<IntCoordinates> dotCoordinates = new ArrayList<>();
//     for (int i = 0; i < getHeight(); i++) {
//         for (int j = 0; j < getWidth(); j++) {
//             Cell cell = grid[i][j];
//             if (cell.getContent() == Content.DOT) {
//                 dotCoordinates.add(new IntCoordinates(j, i));
//             }
//         }
//     }
//     return dotCoordinates;
// }
}
