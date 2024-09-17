/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package allatfarm;
import java.util.ArrayList;
/**
 *
 * @author mgabo
 */
public abstract class Allat {
    String name;
    int suly;
    ArrayList<Integer> etkezesek;

    public Allat(String name, int suly) {
        this.name = name;
        this.suly = suly;
        this.etkezesek = new ArrayList<>();
    }
    
    public boolean Korosan_sovany() {
        if (this.suly < 12) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public boolean Kg_fogyasztas() {
        int osszeg = 0;
        for (int kaja : etkezesek) {
            osszeg += kaja;
        }
        if (osszeg >= 100) {
            return true;
        }
        else{
            return false;
        }
    }
    
    public void plusKaja(int kaja) {
        etkezesek.add(kaja);
    }

    @Override
    public String toString() {
        return "Allat{" + "name=" + name + '}';
    }
    
    
}
