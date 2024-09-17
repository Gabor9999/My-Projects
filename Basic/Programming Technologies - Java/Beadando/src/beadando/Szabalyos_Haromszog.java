/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beadando;

/**
 *
 * @author mgabo
 */
public class Szabalyos_Haromszog extends Sikidom{

    public Szabalyos_Haromszog(Pont p, double a) {
        super(p, a);
    }
    
    @Override
    public double Tavolsag(Pont fixpont) {
        Pont A = new Pont((this.p.x - (this.a/2)),(this.p.y - ((Math.sqrt(3)/4)*this.a)));
        Pont B = new Pont((this.p.x + (this.a/2)),(this.p.y - ((Math.sqrt(3)/4)*this.a)));
        Pont C = new Pont(this.p.x,(this.p.y + ((Math.sqrt(3)/4)*this.a)));
        //Mivel síkon vagyunk az első két komponens 0
        
        double Ap = Vec_szorzat_3(new Pont(B.x - A.x,B.y - A.y),new Pont(fixpont.x - A.x,fixpont.y - A.y));
        double Bp = Vec_szorzat_3(new Pont(C.x - B.x,C.y - B.y),new Pont(fixpont.x - B.x,fixpont.y - B.y));
        double Cp = Vec_szorzat_3(new Pont(A.x - C.x,A.y - C.y),new Pont(fixpont.x - C.x,fixpont.y - C.y));
        
        if ((Ap >= 0 && Bp >= 0 && Cp >= 0) || (Ap <= 0 && Bp <= 0 && Cp <= 0)) {
            return 0;
        }
        
        else {
            if (fixpont.x < C.x && fixpont.y > A.y) {
                Pont mpXY = Pont_es_oldal_mpXY(C,A,fixpont);
                if (mpXY.x < C.x && mpXY.x > A.x && mpXY.y < C.y && mpXY.y > A.y) {
                    return Vektor_hossza(mpXY,fixpont);
                }
            }
            if (fixpont.x > C.x && fixpont.y > B.y) {
                Pont mpXY = Pont_es_oldal_mpXY(C,B,fixpont);
                if (mpXY.x < B.x && mpXY.x > C.x && mpXY.y > B.y && mpXY.y < C.y) {
                    return Vektor_hossza(mpXY,fixpont);
                }
            }
            if (fixpont.x < B.x && fixpont.y < B.y) {
                Pont mpXY = Pont_es_oldal_mpXY(B,A,fixpont);
                if (mpXY.x < B.x && mpXY.x > A.x && mpXY.y == B.y) {
                    return Vektor_hossza(mpXY,fixpont);
                }
            }
            double oldal_A = Vektor_hossza(A,fixpont);
            double oldal_B = Vektor_hossza(B,fixpont);
            double oldal_C = Vektor_hossza(C,fixpont);
            return Math.min(Math.min(oldal_A, oldal_B),oldal_C);
        }
    }

    @Override
    public String toString() {
        return "- Szabályos Háromszög középpontja: " + this.p.x + "," + this.p.y + " , oldala: " + this.a;
    }
    
}
