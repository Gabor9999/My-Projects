/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beadando;

/**
 *
 * @author mgabo
 */
public class Szabalyos_Hatszog extends Sikidom{

    public Szabalyos_Hatszog(Pont p, double a) {
        super(p, a);
    }
    
    @Override
    public double Tavolsag(Pont fixpont) {
        Pont A = new Pont((this.p.x - (this.a/2)),(this.p.y - ((Math.sqrt(3)/2)*this.a)));
        Pont B = new Pont((this.p.x + (this.a/2)),(this.p.y - ((Math.sqrt(3)/2)*this.a)));
        Pont C = new Pont((this.p.x + this.a),this.p.y);
        Pont D = new Pont((this.p.x + (this.a/2)),(this.p.y + ((Math.sqrt(3)/2)*this.a)));
        Pont E = new Pont((this.p.x - (this.a/2)),(this.p.y + ((Math.sqrt(3)/2)*this.a)));
        Pont F = new Pont((this.p.x - this.a),this.p.y);

        //Mivel síkon vagyunk az első két komponens 0
        double Ap = Vec_szorzat_3(new Pont(B.x - A.x,B.y - A.y),new Pont(fixpont.x - A.x,fixpont.y - A.y));
        double Bp = Vec_szorzat_3(new Pont(C.x - B.x,C.y - B.y),new Pont(fixpont.x - B.x,fixpont.y - B.y));
        double Cp = Vec_szorzat_3(new Pont(D.x - C.x,D.y - C.y),new Pont(fixpont.x - C.x,fixpont.y - C.y));
        double Dp = Vec_szorzat_3(new Pont(E.x - D.x,E.y - D.y),new Pont(fixpont.x - D.x,fixpont.y - D.y));
        double Ep = Vec_szorzat_3(new Pont(F.x - E.x,F.y - E.y),new Pont(fixpont.x - E.x,fixpont.y - E.y));
        double Fp = Vec_szorzat_3(new Pont(A.x - F.x,A.y - F.y),new Pont(fixpont.x - F.x,fixpont.y - F.y));
        
        if ((Ap >= 0 && Bp >= 0 && Cp >= 0 && Dp >= 0 && Ep >= 0 && Fp >= 0) || (Ap <= 0 && Bp <= 0 && Cp <= 0 && Dp <= 0 && Ep <= 0 && Fp <= 0)) {
            return 0;
        }
        else {
            if (fixpont.x < E.x && fixpont.y > F.y) {
                Pont mpXY = Pont_es_oldal_mpXY(F,E,fixpont);
                if (mpXY.x < E.x && mpXY.x > F.x && mpXY.y < E.y && mpXY.y > F.y) {
                    return Vektor_hossza(mpXY,fixpont);
                }
            }
            if (fixpont.x < E.x && fixpont.y < F.y) {
                Pont mpXY = Pont_es_oldal_mpXY(F,A,fixpont);
                if (mpXY.x < A.x && mpXY.x > F.x && mpXY.y > A.y && mpXY.y < F.y) {
                    return Vektor_hossza(mpXY,fixpont);
                }
            }
            if (fixpont.x < B.x && fixpont.y < B.y) {
                Pont mpXY = Pont_es_oldal_mpXY(B,A,fixpont);
                if (mpXY.x < B.x && mpXY.x > A.x && mpXY.y == B.y) {
                    return Vektor_hossza(mpXY,fixpont);
                }
            }
            if (fixpont.x < D.x && fixpont.y > D.y) {
                Pont mpXY = Pont_es_oldal_mpXY(D,E,fixpont);
                if (mpXY.x < D.x && mpXY.x > E.x && mpXY.y == E.y) {
                    return Vektor_hossza(mpXY,fixpont);
                }
            }
            if (fixpont.x > D.x && fixpont.y > C.y) {
                Pont mpXY = Pont_es_oldal_mpXY(D,C,fixpont);
                if (mpXY.x < C.x && mpXY.x > D.x && mpXY.y > C.y && mpXY.y < D.y) {
                    return Vektor_hossza(mpXY,fixpont);
                }
            }
            if (fixpont.x > B.x && fixpont.y < C.y) {
                Pont mpXY = Pont_es_oldal_mpXY(C,B,fixpont);
                if (mpXY.x < C.x && mpXY.x > B.x && mpXY.y < C.y && mpXY.y > B.y) {
                    return Vektor_hossza(mpXY,fixpont);
                }
            }
            
            double oldal_A = Vektor_hossza(A,fixpont);
            double oldal_B = Vektor_hossza(B,fixpont);
            double oldal_C = Vektor_hossza(C,fixpont);
            double oldal_D = Vektor_hossza(D,fixpont);
            double oldal_E = Vektor_hossza(E,fixpont);
            double oldal_F = Vektor_hossza(F,fixpont);
            return Math.min(Math.min(Math.min(oldal_A, oldal_B),Math.min(oldal_C,oldal_D)),Math.min(oldal_E, oldal_F));
    }
}

    @Override
    public String toString() {
        return "- Szabályos Hatszög, középpontja: " + this.p.x + "," + this.p.y + " , oldala: " + this.a;
    }
    
}
