package config;

import geometry.IntCoordinates;

import static config.Cell.Content.DOT;
import static config.Cell.*;
import static config.Cell.Content.NOTHING;
import static config.Cell.Content.ENERGIZER; // Ajout de la constante ENERGIZER
import java.io.File ; // Ajout de la classe File
import java.io.FileNotFoundException ; // Ajout de la classe FileNotFoundException pour gérer l'exception si le fichier n'extiste pas
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.BufferedReader; // Ajout de la classe BufferedReader pour lire le fichier
import config.Cell.Content;
import java.io.IOException; // Ajout de la classe IOException pour gérer l'exception si le fichier n'extiste pas

public class MazeConfig {
    public MazeConfig(Cell[][] grid, IntCoordinates pacManPos, IntCoordinates blinkyPos, IntCoordinates pinkyPos,
                      IntCoordinates inkyPos, IntCoordinates clydePos) {
        this.grid = new Cell[grid.length][grid[0].length];
        
        for (int i = 0; i < getHeight(); i++) {
            
            if (getWidth() >= 0) System.arraycopy(grid[i], 0, this.grid[i], 0, getWidth());
        }
        
        
        this.pacManPos = pacManPos;
        this.blinkyPos = blinkyPos;
        this.pinkyPos = pinkyPos;
        this.inkyPos = inkyPos;
        this.clydePos = clydePos;
    }

    private final Cell[][] grid;
    private final IntCoordinates pacManPos, blinkyPos, pinkyPos, inkyPos, clydePos;
    private static Map<Character, Cell.Content> itemDictionary = new HashMap<>();
    private static Map<Character, Boolean> wallDictionary = new HashMap<>();
    private static void itemDictionary(){
        if(itemDictionary.size() != 0) return;
        itemDictionary.put('E', ENERGIZER);
        itemDictionary.put('D', DOT);
        itemDictionary.put(' ', NOTHING);
        itemDictionary.put('J', NOTHING);
        itemDictionary.put('B', NOTHING);
        itemDictionary.put('C', NOTHING);
        itemDictionary.put('I', NOTHING);
        itemDictionary.put('P', NOTHING);
    }
    private static void wallDictionary(){
        if(wallDictionary.size() != 0) return;
        wallDictionary.put('|', true);
        wallDictionary.put('-', true);
        wallDictionary.put(' ', false);
        wallDictionary.put('+', false);
        
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
        
/**
     * Detecte les murs et les items dans une ligne du fichier et les ajoute dans le tableau grid et ajoute les positions des items (Pacman,les fantomes) dans le tableau pos
     * @param width largeur du labyrinthe
     * @param y ordonnée de la ligne qu'on est entrain de lire
     * @param l1 ligne qui contient les murs horizontaux (au dessus des cellules)
     * @param l2 ligne qui contient le contenu des cellules et les murs verticaux
     * @param l3 ligne qui contient les murs horizontaux (en dessous des cellules)
     * @param grid tableau de cellules
     * @param pos tableau des positions de Pacman et les fantomes
     */
    private static void detector(int width,int y ,String l1, String l2, String l3, Cell[][] grid , IntCoordinates[] pos){
        Cell.Content CONTENT = NOTHING;
        boolean north ; boolean east ; boolean south ; boolean west ;
        Cell[] gridx = new Cell[width];
        int j = 0;
        for(int i = 1 ;  i < width*2 ; i+=2){
            if(l2.length() > i){
                switch (l2.charAt(i)) {
                    case 'J':
                        pos[0] = new IntCoordinates(j,y);
                        CONTENT = NOTHING;
                        break;
                    case 'B':
                        pos[1] = new IntCoordinates(j,y);
                        CONTENT = NOTHING;
                        break;
                    case 'P':
                        pos[2] = new IntCoordinates(j,y);
                        CONTENT = NOTHING;
                        break;
                    case 'I':
                        pos[3] = new IntCoordinates(j,y);
                        CONTENT = NOTHING;
                        break;
                    case 'C':
                        pos[4] = new IntCoordinates(j,y);
                        CONTENT = NOTHING;
                        break;
                    default:
                        CONTENT = itemDictionary.get(l2.charAt(i));
                        break;
                }
            } else {
                CONTENT = NOTHING;
            }
            
            //System.out.println("north" + y + " " + j + " " + CONTENT);
            north = wallDictionary.get
            ((l1.length() > i) ? l1.charAt(i) : ' ');
            //System.out.println("east" + y + " " + j + " " + CONTENT);
            east = wallDictionary.get
            ((l2.length() > i+1) ? l2.charAt(i+1) : ' ');
            //System.out.println("south" + y + " " + j + " " + CONTENT);
            south = wallDictionary.get
            ((l3.length() > i)?(l3.charAt(i)) : ' ' );
            //System.out.println("east" + y + " " + j + " " + CONTENT);
            west = wallDictionary.get
            ((l2.length() > i-1) ?  l2.charAt(i-1) : ' ');
            gridx[j] = new Cell(north, east, south, west, CONTENT);
            
            j++;
            
            }
            grid[y] = gridx; 
        }
    /** 
     * Modifie la cellule à la position pos
     * @param pos position de la cellule
     * @param c nouvelle cellule
     */
    public void setCell(IntCoordinates pos, Cell c){
        grid[Math.floorMod(pos.y(), getHeight())][Math.floorMod(pos.x(), getWidth())] = c;
    }
    /**
     * Affiche le tableau grid
     * @param tabCells tableau de cellules
     */
    public static void affichegrid(Cell[][] tabCells){
        for(int i = 0 ; i < tabCells.length ; i++){
            for(int j = 0 ; j < tabCells[0].length ; j++){
                System.out.print(i+""+j+"");
            }
            System.out.println();
        }
    }

    /**
     * Crée un MazeConfig à partir d'un fichier texte
     * en utilisant la fonction detector et en checkant ligne par ligne
     * @return MazeConfig
     */
    public static MazeConfig makeExample1() {
        
        String filePath = "src/main/resources/maze2.txt";
        BufferedReader reader = null;
        itemDictionary();
        wallDictionary();
        IntCoordinates[] pos = new IntCoordinates[5];
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            String[] xy = line.split(" ");
            
            Cell[][] grid = new Cell[Integer.parseInt(xy[0])][Integer.parseInt(xy[1])];
            
            String l1 = reader.readLine();
            String l2 = reader.readLine(); 
            String l3 = reader.readLine(); 
            detector(Integer.parseInt(xy[1]),0, l1, l2, l3, grid,pos);
            
            for(int i = 1 ; i < Integer.parseInt(xy[0]) ; i++){
                l1 = l3; 
                l2 = reader.readLine();  
                l3 = reader.readLine(); 
                
                detector(Integer.parseInt(xy[1]), i, l1, l2, l3, grid,pos);
                
            }
            //affichegrid(grid);
            
                return new MazeConfig(grid,
                pos[0],
                pos[1],
                pos[2],
                pos[3],
                pos[4]);
                
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Crée un MazeConfig à partir d'un fichier texte
     * en utilisant la fonction detector et en checkant ligne par ligne
     * @param filePath chemin du fichier
     * @return MazeConfig
     */
    public MazeConfig makeLabyrinthe(String filePath) {
        
       
        BufferedReader reader = null;
        itemDictionary();
        wallDictionary();
        IntCoordinates[] pos = new IntCoordinates[5];
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            String[] xy = line.split(" ");
            
            Cell[][] grid = new Cell[Integer.parseInt(xy[0])][Integer.parseInt(xy[1])];
            
            String l1 = reader.readLine();
            String l2 = reader.readLine(); 
            String l3 = reader.readLine(); 
            detector(Integer.parseInt(xy[1]),0, l1, l2, l3, grid,pos);
            
            for(int i = 1 ; i < Integer.parseInt(xy[0]) ; i++){
                l1 = l3; 
                l2 = reader.readLine();  
                l3 = reader.readLine(); 
                
                detector(Integer.parseInt(xy[1]), i, l1, l2, l3, grid,pos);
                
            }
            
            
                return new MazeConfig(grid,
                pos[0],
                pos[1],
                pos[2],
                pos[3],
                pos[4]);
                
        }  
            catch (IOException e) {
            System.out.println("Le fichier n'existe pas.");
            e.printStackTrace();
            return null;
            }
        }
    }

