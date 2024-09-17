/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author mgabo
 */
public class Hallgato {
    private String name;
    private String nationality;
    private float average;
    
    public Hallgato(String name_,String nationality_, int ave_){
        this.name = name_;
        this.nationality = nationality_;
        this.average = ave_;
    }
    
    public String getName() {
        return name;
    }
    
    public String getNationality() {
        return nationality;
    }
    
    public float getAve() {
        return average;
    }

    @Override
    public String toString() {
        return "Hallgato{" + "name=" + name + ", nationality=" + nationality + ", average=" + average + '}';
    }
    
    public void setName(String name_) {
        this.name = name_;
    }
    
    public void setNationality(String nat_) {
        this.nationality = nat_;
    }
    
    public void setAge(float ave_) {
        this.average = ave_;
    }
    
}
