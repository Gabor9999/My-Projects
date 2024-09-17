/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package allatfarm;
import java.io.FileNotFoundException;
/**
 *
 * @author mgabo
 */
public class Main {
    public static void main(String[] args) {
    Database database = new Database();
    try {
        database.read("data.txt");} 
    catch (FileNotFoundException ex) {
        System.out.println("File not found!");
        System.exit(-1);
    } catch (InvalidInputException ex) {
        System.out.println("Invalid input!");
        System.exit(-1);
    }
    database.report();
}
}
