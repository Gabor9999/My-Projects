/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beadando;

/**
 *
 * @author mgabo
 */
public abstract class Sikidom {
    protected Pont p;
    protected double a;

    public Sikidom(Pont p, double a) {
        this.p = p;
        this.a = a;
    }
    
    public double Vec_szorzat_3(Pont U,Pont V) {
        return ((U.x * V.y) - (V.x * U.y));
    }
    public double Vektor_hossza(Pont A,Pont B){
        return Math.sqrt((A.y - B.y) * (A.y - B.y) + (A.x - B.x) * (A.x - B.x));
    }
    public Pont Pont_es_oldal_mpXY(Pont A,Pont B, Pont fixpont){ // 8 , 8,10,12, 2 , 14 new Pont(-4,6),new Pont(-6,2),new Pont(-2,2)
        double inx2 = B.x - A.x; // -2
        double y2 = B.y - A.y; // -4
        double inx;
        double y1;
        if (inx2 == 0) {
            inx = y2 *(-1);  // 4 
            y1 = inx2; // -2
        }
        else {
            inx = y2;  // -4 
            y1 = inx2 * (-1); // 2
        }
        double egyenes_jobboldal = (inx * A.x) + (y1 * A.y); // 28
        double egyenesPont_jobb = inx2 * fixpont.x + y2 * fixpont.y; // 4-8=-4
        double result_x = inx2 + (y2 * (inx / (y1*(-1))));
        double jobboldal = egyenesPont_jobb - (y2 * (egyenes_jobboldal/y1));
        if (y2 != 0) {
            return new Pont((jobboldal/result_x),(egyenes_jobboldal - ((inx*(jobboldal/result_x))))/y1);
        }
        //Ha y1 = 0, tehát az egyenes vízszintes
        else {
        return new Pont((jobboldal/result_x),A.y);
        }
    }
        //0 pont y bemenet debug
    public double Tavolsag(Pont fixpont) {
        return 0;
    }

    @Override
    public String toString() {
        return "Sikidom{" + "x=" + p.x + ", y=" + p.y + ", a=" + a + '}';
    }
    
    
}

