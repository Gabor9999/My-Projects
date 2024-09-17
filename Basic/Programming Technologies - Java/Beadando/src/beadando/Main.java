/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package beadando;

import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author mgabo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Database database = new Database();
        try {
            Scanner filename = new Scanner(System.in);
            database.read(filename.next());} 
        catch (FileNotFoundException ex) {
            System.out.println("File not found!");
            System.exit(-1);
        } 
        catch (InvalidInputException ex) {
            System.out.println("Invalid input!");
            System.exit(-1);
        }
        catch (Exception e) {
            System.out.println("Error!");
            System.exit(-1);
        }
        database.report();
        database.sclear();
        }
    }
