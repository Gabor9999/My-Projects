/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rpg2;

/**
 *
 * @author mgabo
 */
public class Character {
    protected String name;
    protected int HP;
    protected int attack;
    
    public void attack(Character character) {
        character.HP -= this.attack;
    }
    
    public void attacked(Character character) {
        applyDamageFrom(character);
    }
    
    public boolean isAlive() {
        return (HP > 0);
    }
    
    protected void applyDamageFrom(Character character) {
        this.HP -= character.attack;
    }

    public Character(String name, int HP, int attack) {
        this.name = name;
        this.HP = HP;
        this.attack = attack;
    }
}
