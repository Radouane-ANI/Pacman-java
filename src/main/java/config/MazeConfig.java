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
    private static Map<Character, String> itemDictionary = new HashMap<>();
    private static Map<Character, Boolean> wallDictionary = new HashMap<>();
    private static void itemDictionary(){
        if(itemDictionary.size() != 0) return;
        itemDictionary.put('E', "ENERGIZER");
        itemDictionary.put('D', "DOT");
        itemDictionary.put(' ', "NOTHING");
        itemDictionary.put('J', "PACMAN");
        itemDictionary.put('B', "BLINKY");
        itemDictionary.put('C', "CLYDE");
        itemDictionary.put('I', "INKY");
        itemDictionary.put('P', "PINKY");
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
    public void detector(String l1, String l2, String l3){
        String CONTENT = "NOTHING";
        for(int i = 1 ;  i < l2.length() ; i++){
             itemDictionary.get(l2.charAt(i));


            }
        }

    public Cell getCell(IntCoordinates pos) {
        return grid[Math.floorMod(pos.y(), getHeight())][Math.floorMod(pos.x(), getWidth())];
    }


    // simple example with a square shape
    // TODO: mazes should be loaded from a text file
    public static MazeConfig makeExample1() {
        
        String filePath = "boulbi.txt";
        BufferedReader reader = null;
        itemDictionary();
        wallDictionary();
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            String[] xy = line.split(" ");
            
            String[][] grid = new String[Integer.parseInt(xy[0])][Integer.parseInt(xy[1])];
           
            String l1 = reader.readLine(); System.out.println(l1);
            String l2 = reader.readLine(); System.out.println(l2);
            String l3 = reader.readLine(); System.out.println(l3);
            while(l3 != null){
                l1 = l3; System.out.println(l1);
                l2 = reader.readLine();  System.out.println(l2);
                l3 = reader.readLine(); if(l3 != null) System.out.println(l3);
            }
                
            
            
            
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }
        finally{                
                return new MazeConfig(grid,
                new IntCoordinates(3, 0),
                new IntCoordinates(0, 3),
                new IntCoordinates(3, 5),
                new IntCoordinates(5, 5),
                new IntCoordinates(5, 1)
                );
            }
        
    }
}

