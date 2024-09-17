/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package allatfarm;

/**
 *
 * @author mgabo
 */
public class Tehen extends Allat{

    public Tehen(String name, int suly) {
        super(name, suly);
    }
    
    @Override
    public boolean Korosan_sovany() {
        if (this.suly < 100) {
        return true;
        }
        else {
        return false;}
    }
}
