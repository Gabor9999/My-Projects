/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package rpg2;

/**
 *
 * @author mgabo
 */
public class RPG2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainCharacter MC = new MainCharacter (3,"Gaben",500,10);
        RedDragon Drag = new RedDragon("Dr",4000,60);
        Fighter F = new Fighter("Fi",200,20);
        
        int e = 1;
        while() {
            MC.attacked(Drag);
            System.out.println(e);
            e++;
        }
    }
    
}
