/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rpg2;

/**
 *
 * @author mgabo
 */
public class Defender extends Orc{
    @Override
    protected void applyDamageFrom(Character character){
        this.HP = character.attack/2;
    }

    public Defender(String name, int HP, int attack) {
        super(name, HP, attack);
    }
}
