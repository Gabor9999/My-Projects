/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beadando;

/**
 *
 * @author mgabo
 */
public class Negyzet extends Sikidom{

    public Negyzet(Pont p, double a) {
        super(p, a);
    }
    
    @Override
    public double Tavolsag(Pont fixpont) {
        Pont A = new Pont((this.p.x - (this.a/2)),(this.p.y - (this.a/2)));
        Pont B = new Pont((this.p.x + (this.a/2)),(this.p.y - (this.a/2)));
        Pont C = new Pont((this.p.x + (this.a/2)),(this.p.y + (this.a/2)));
        Pont D = new Pont((this.p.x - (this.a/2)),(this.p.y + (this.a/2)));
        //Mivel síkon vagyunk az első két komponens 0
        double Ap = Vec_szorzat_3(new Pont(B.x - A.x,B.y - A.y),new Pont(fixpont.x - A.x,fixpont.y - A.y));
        double Bp = Vec_szorzat_3(new Pont(C.x - B.x,C.y - B.y),new Pont(fixpont.x - B.x,fixpont.y - B.y));
        double Cp = Vec_szorzat_3(new Pont(D.x - C.x,D.y - C.y),new Pont(fixpont.x - C.x,fixpont.y - C.y));
        double Dp = Vec_szorzat_3(new Pont(A.x - D.x,A.y - D.y),new Pont(fixpont.x - D.x,fixpont.y - D.y));
        
        if ((Ap >= 0 && Bp >= 0 && Cp >= 0 && Dp >= 0) || (Ap <= 0 && Bp <= 0 && Cp <= 0 && Dp <= 0)) {
            return 0;
        }
        
        else {
            if (fixpont.y > A.y && fixpont.y < D.y) {
                double oldal1 = Math.sqrt((A.x - fixpont.x) * (A.x - fixpont.x));
                double oldal2 = Math.sqrt((D.x - fixpont.x) * (D.x - fixpont.x));
                return Math.min(oldal1,oldal2);
            }
            if (fixpont.x < B.x && fixpont.x > A.x) {
                double oldal3 = Math.sqrt((A.y - fixpont.y) * (A.y - fixpont.y));
                double oldal4 = Math.sqrt((D.y - fixpont.y) * (D.x - fixpont.y));
                return Math.min(oldal3,oldal4);
            }
            else {
            double oldal_A = Vektor_hossza(A,fixpont);
            double oldal_B = Vektor_hossza(B,fixpont);
            double oldal_C = Vektor_hossza(C,fixpont);
            double oldal_D = Vektor_hossza(D,fixpont);
            return Math.min(Math.min(oldal_A, oldal_B),Math.min(oldal_C,oldal_D));}
        }
    }

    @Override
    public String toString() {
        return "- Négyzet, középpontja: " + this.p.x + "," + this.p.y + " , oldala: " + this.a;
    }
    
    
}
