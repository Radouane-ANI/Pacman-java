package config;

import geometry.IntCoordinates;

import static config.Cell.Content.DOT;
import static config.Cell.*;
import static config.Cell.Content.NOTHING;
import static config.Cell.Content.ENERGIZER; // Ajout de la constante ENERGIZER
import java.io.File ; // Ajout de la classe File
import java.io.FileNotFoundException ; // Ajout de la classe FileNotFoundException pour gérer l'exception si le fichier n'extiste pas
import java.util.Scanner ; // Ajout de la classe Scanner pour lire le fichier

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
                    ji++;
                    switch (line.charAt(j)) {
                        case 'A':
                            if(j+1 < line.length() && line.charAt(j+1) == 'e'){
                               grid[i][ji] = open(ENERGIZER);
                               j+= 1;
                               break;
                            }

                            if(j+1 < line.length() && line.charAt(j+1) == 'd'){
                                grid[i][ji] = open(DOT);
                                j+= 1;
                                break;
                            }

                            else{
                                grid[i][ji] = open(NOTHING);
                                break;
                            }
                        case 'B':   
                            if(j+1 < line.length() && line.charAt(j+1) == 'e'){
                                grid[i][ji] = closed(ENERGIZER);
                                j+= 1;
                            break;
                            }
                            
                            if(j+1 < line.length() && line.charAt(j+1) == 'd'){
                                grid[i][ji] = closed(DOT);
                                j+= 1;
                                break;
                            }
                            
                            else{
                                grid[i][ji] = closed(NOTHING);
                                break;
                            }
                        case 'C':   
                            if(j+1 < line.length() && line.charAt(j+1) == 'e'){
                                    grid[i][ji] = hPipe(ENERGIZER);
                                    j+= 1;
                                break;
                                }
                                
                            if(j+1 < line.length() && line.charAt(j+1) == 'd'){
                                    grid[i][ji] = hPipe(DOT);
                                    j+= 1;
                                    break;
                                }
                                
                            else{
                                    grid[i][ji] = hPipe(NOTHING);
                                    break;
                                }
                        case 'D':
                            if(j+1 < line.length() && line.charAt(j+1) == 'e'){
                                     grid[i][ji] = vPipe(ENERGIZER);
                                    j+= 1;
                                break;
                                }
                                
                            if(j+1 < line.length() && line.charAt(j+1) == 'd'){
                                    grid[i][ji] = vPipe(DOT);
                                    j+= 1;
                                    break;
                                }
                                
                            else{
                                    grid[i][ji] = vPipe(NOTHING);
                                    break;
                                }
                        case 'E':
                            if(j+1 < line.length() && line.charAt(j+1) == 'e'){
                                    grid[i][ji] = swVee(ENERGIZER);
                                    j+= 1;
                                break;
                                }
                                
                            if(j+1 < line.length() && line.charAt(j+1) == 'd'){
                                    grid[i][ji] = swVee(DOT);
                                    j+= 1;
                                    break;
                                }
                                
                            else{
                                    grid[i][ji] = swVee(NOTHING);
                                    break;
                                } 
                        case 'F':
                            if(j+1 < line.length() && line.charAt(j+1) == 'e'){
                                    grid[i][ji] = nwVee(ENERGIZER);
                                    j+= 1;
                                break;
                                }
                                
                            if(j+1 < line.length() && line.charAt(j+1) == 'd'){
                                    grid[i][ji] = nwVee(DOT);
                                    j+= 1;
                                    break;
                                }
                                
                            else{
                                    grid[i][ji] = nwVee(NOTHING);
                                    break;
                                }
                        case 'G':
                            if(j+1 < line.length() && line.charAt(j+1) == 'e'){
                                    grid[i][ji] = neVee(ENERGIZER);
                                j+= 1;
                                break;
                                }
                                
                            if(j+1 < line.length() && line.charAt(j+1) == 'd'){
                                     grid[i][ji] = neVee(DOT);
                                    j+= 1;
                                    break;
                                }
                                
                            else{
                                    grid[i][ji] = neVee(NOTHING);
                                    break;
                                }
                        case 'H':
                            if(j+1 < line.length() && line.charAt(j+1) == 'e'){
                                    grid[i][ji] = seVee(ENERGIZER);
                                    j+= 1;
                                break;
                                }
                                
                            if(j+1 < line.length() && line.charAt(j+1) == 'd'){
                                    grid[i][ji] = seVee(DOT);
                                    j+= 1;
                                    break;
                                }
                                
                            else{
                                    grid[i][ji] = seVee(NOTHING);
                                    break;
                                }
                        case 'I':
                            if(j+1 < line.length() && line.charAt(j+1) == 'e'){
                                    grid[i][ji] = nTee(ENERGIZER);
                                    j+= 1;
                                break;
                                }
                                
                            if(j+1 < line.length() && line.charAt(j+1) == 'd'){
                                    grid[i][ji] = nTee(DOT);
                                    j+= 1;
                                    break;
                                }
                                
                            else{
                                    grid[i][ji] = nTee(NOTHING);
                                    break;
                                }
                        case 'J':
                            if(j+1 < line.length() && line.charAt(j+1) == 'e'){
                                    grid[i][ji] = eTee(ENERGIZER);
                                    j+= 1;
                                break;
                                }
                                
                            if(j+1 < line.length() && line.charAt(j+1) == 'd'){
                                    grid[i][ji] = eTee(DOT);
                                    j+= 1;
                                    break;
                                }
                                
                            else{
                                    grid[i][ji] = eTee(NOTHING);
                                    break;
                                }
                        case 'K':
                            if(j+1 < line.length() && line.charAt(j+1) == 'e'){
                                    grid[i][ji] = sTee(ENERGIZER);
                                    j+= 1;
                                break;
                                }
                                
                            if(j+1 < line.length() && line.charAt(j+1) == 'd'){
                                    grid[i][ji] = sTee(DOT);
                                    j+= 1;
                                    break;
                                }
                                
                            else{
                                    grid[i][ji] = sTee(NOTHING);
                                    break;
                                }
                        case 'L':
                            if(j+1 < line.length() && line.charAt(j+1) == 'e'){
                                    grid[i][ji] = wTee(ENERGIZER);
                                    j+= 1;
                                break;
                                }
                                
                            if(j+1 < line.length() && line.charAt(j+1) == 'd'){
                                    grid[i][ji] = wTee(DOT);
                                    j+= 1;
                                    break;
                                }
                                
                            else{
                                    grid[i][ji] = wTee(NOTHING);
                                    break;
                                }
                        case 'M':
                            if(j+1 < line.length() && line.charAt(j+1) == 'e'){
                                    grid[i][ji] = nU(ENERGIZER);
                                    j+= 1;
                                break;
                                }
                                
                            if(j+1 < line.length() && line.charAt(j+1) == 'd'){
                                    grid[i][ji] = nU(DOT);
                                    j+= 1;
                                    break;
                                }
                                
                            else{
                                    grid[i][ji] = nU(NOTHING);
                                    break;
                                }
                        case 'N':
                            if(j+1 < line.length() && line.charAt(j+1) == 'e'){
                                    grid[i][ji] = eU(ENERGIZER);
                                    j+= 1;
                                break;
                                }
                                
                            if(j+1 < line.length() && line.charAt(j+1) == 'd'){
                                    grid[i][ji] = eU(DOT);
                                    j+= 1;
                                    break;
                                }
                                
                            else{
                                    grid[i][ji] = eU(NOTHING);
                                    break;
                                }
                        case 'O':
                            if(j+1 < line.length() && line.charAt(j+1) == 'e'){
                                    grid[i][ji] = sU(ENERGIZER);
                                j+= 1;
                                break;
                                }
                                
                            if(j+1 < line.length() && line.charAt(j+1) == 'd'){
                                    grid[i][ji] = sU(DOT);
                                    j+= 1;
                                    break;
                                }
                                
                            else{
                                    grid[i][ji] = sU(NOTHING);
                                    break;
                                }
                        case 'P':
                            if(j+1 < line.length() && line.charAt(j+1) == 'e'){
                                    grid[i][ji] = wU(ENERGIZER);
                                    j+= 1;
                                break;
                                }
                                
                            if(j+1 < line.length() && line.charAt(j+1) == 'd'){
                                    grid[i][ji] = wU(DOT);
                                    j+= 1;
                                    break;
                                }
                                
                            else{
                                    grid[i][ji] = wU(NOTHING);
                                    break;
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
}
