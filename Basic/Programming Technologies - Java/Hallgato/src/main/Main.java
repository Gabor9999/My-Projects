/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;
import java.util.ArrayList;

/**
 *
 * @author mgabo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ArrayList<Hallgato> L = new ArrayList<Hallgato>();
        
        Hallgato G = new Hallgato("Gab","HUN",5);
        Hallgato B = new Hallgato("Bar","GER",4);
        Hallgato H= new Hallgato("Hir","GB",3);
        L.add(G);
        L.add(B);
        L.add(H);
        float best = 0;
        
        for(int i = 0;i < L.size();i++){
            if (L.get(i).getAve() > best){
                best = L.get(i).getAve();
            }
        }
        ArrayList<String> Oszt = new ArrayList<String>();
        for(int i = 0;i < L.size();i++){
            if (L.get(i).getAve() >= 4.0){
                Oszt.add(L.get(i).getName());
            }
        }
        
        System.out.println(best);
        for (int e = 0; e < Oszt.size();e++){
            System.out.println(Oszt.get(e));
        }
    }
    
}
