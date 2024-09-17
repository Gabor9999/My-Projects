/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rpg;

/**
 *
 * @author mgabo
 */
public class Berserker extends Orc{
    @Override
    protected void applyDamageFrom(Character character){
        this.HP = character.attack*2;
    }

    public Berserker(String name, int HP, int attack) {
        super(name, HP, attack);
    }
    
    
}
