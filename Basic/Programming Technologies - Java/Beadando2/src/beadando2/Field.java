/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beadando2;

/**
 *
 * @author mgabo
 */
public class Field {
    private String character;

    public Field() {
        this.character = "";
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(int i) {
        if (i % 2 == 0) {
            this.character = "X";
        }
        else {
            this.character = "O";
        }
    }
    
    public void setEmpty() {
        this.character = "";
    }
}

