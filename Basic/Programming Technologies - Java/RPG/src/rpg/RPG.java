/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package rpg;

/**
 *
 * @author mgabo
 */
public class RPG {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainCharacter MC = new MainCharacter (3,"Gaben",500,10);
        RedDragon Drag = new RedDragon("Dr",4000,50);
        Fighter F = new Fighter("Fi",200,20);
        
        int e = 1;
        while(e != 2) {
            MC.attacked(Drag);
            Drag.attacked(F);
            F.attacked(MC);
            if (MC.isAlive() == false && F.isAlive() == false) {
                System.out.println("Winner is" + Drag.getName());
                break;
            }
        }
    }
    
}
