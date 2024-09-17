/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rpg2;

/**
 *
 * @author mgabo
 */
public class Dragon extends Character{
    protected int ATTACK_THRESHOLD;
    
    @Override
        protected void applyDamageFrom(Character character){
            if (character.attack > ATTACK_THRESHOLD) {
                this.HP -= character.attack;
            }
        }

    public Dragon(int ATTACK_THRESHOLD, String name, int HP, int attack) {
        super(name, HP, attack);
        this.ATTACK_THRESHOLD = ATTACK_THRESHOLD;
    }
}
