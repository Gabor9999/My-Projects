/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package allatfarm;

/**
 *
 * @author mgabo
 */
public class Lo extends Allat {

    public Lo(String name, int suly) {
        super(name, suly);
    }
    
    @Override
    public boolean Korosan_sovany() {
        if (this.suly < 60) {
            return true;
        }
        else {
            return false;
        }
    }
}
