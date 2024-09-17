/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beadando;

/**
 *
 * @author mgabo
 */
public class Kor extends Sikidom{

    public Kor(Pont p, double a) {
        super(p, a);
    }
    
    @Override
    public double Tavolsag(Pont fixpont) {
        return Math.max((Math.sqrt((this.p.y - fixpont.y) * (this.p.y - fixpont.y) + (this.p.x - fixpont.x) * (this.p.x - fixpont.x)) - this.a),0.0);
    }

    @Override
    public String toString() {
        return "- Kör, középpontja: " + this.p.x + "," + this.p.y + " , sugara: " + this.a;
    }
    
}
