/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rpg;

/**
 *
 * @author mgabo
 */
public class MainCharacter extends Character {
    private double defense;
    
    @Override
        protected void applyDamageFrom(Character character){
            this.HP -= character.attack/this.defense;
        }

    public MainCharacter(double defense, String name, int HP, int attack) {
        super(name, HP, attack);
        this.defense = defense;
    }
    
    
}
