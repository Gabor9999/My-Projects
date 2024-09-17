/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beadando;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.InputMismatchException;
/**
 *
 * @author mgabo
 */
public class Database {
    private final ArrayList<Sikidom> sikidomok;
    protected Pont fixpont;

    public Database() {
        sikidomok = new ArrayList<>();
    }

    public void read(String filename) throws FileNotFoundException, InvalidInputException {
        Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));
        System.out.println("Add meg a fix pontot: (x, y) ");
        try {
            Scanner inPont = new Scanner(System.in);
            fixpont = new Pont(inPont.nextDouble(),inPont.nextDouble());
        } catch (InputMismatchException e) {
            System.out.println("InputMismatchException: A konzolbemenet nem megfelelő formátumban van megadva.");
            System.exit(1);
        }
        int sikidomokSzama = sc.nextInt();
        while (sc.hasNext()) {
            Sikidom sikidom;
            double x = 0; 
            double y = 0;
            double a = 0;
            switch (sc.next()) {
                case "K":
                    try {
                        x = sc.nextDouble();
                        y = sc.nextDouble();
                        a = sc.nextDouble();
                        if (a <= 0) {
                            System.out.println("A síkidom ezen paramétere nem lehet 0 vagy annál kisebb, mert nem létezik ilyen oldalhossz vagy sugár.");
                            throw new NumberFormatException();
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("InputMismatchException: A fájlbemenet elemei között van, amely nem megfelelő formátumban van megadva.");
                        System.exit(1);
                    }
                    sikidom = new Kor(new Pont(x,y),a);
                    break;
                case "N":
                    try {
                        x = sc.nextDouble();
                        y = sc.nextDouble();
                        a = sc.nextDouble();
                        if (a <= 0) {
                            System.out.println("A síkidom ezen paramétere nem lehet 0 vagy annál kisebb, mert nem létezik ilyen oldalhossz vagy sugár.");
                            throw new NumberFormatException();
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("InputMismatchException: A fájlbemenet elemei között van, amely nem megfelelő formátumban van megadva.");
                        System.exit(1);
                    }
                    sikidom = new Negyzet(new Pont(x,y),a);
                    break;
                //T mint Triangle
                case "T":
                    try {
                        x = sc.nextDouble();
                        y = sc.nextDouble();
                        a = sc.nextDouble();
                        if (a <= 0) {
                            System.out.println("A síkidom ezen paramétere nem lehet 0 vagy annál kisebb, mert nem létezik ilyen oldalhossz vagy sugár.");
                            throw new NumberFormatException();
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("InputMismatchException: A fájlbemenet elemei között van, amely nem megfelelő formátumban van megadva.");
                        System.exit(1);
                    }
                    sikidom = new Szabalyos_Haromszog(new Pont(x,y),a);
                    break;
                case "H":
                    try {
                        x = sc.nextDouble();
                        y = sc.nextDouble();
                        a = sc.nextDouble();
                        if (a <= 0) {
                            System.out.println("NumberFormatException: A síkidom ezen paramétere nem lehet 0 vagy annál kisebb, mert nem létezik ilyen oldalhossz vagy sugár.");
                            throw new NumberFormatException();
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("InputMismatchException: A fájlbemenet elemei között van, amely nem megfelelő formátumban van megadva.");
                        System.exit(1);
                    }
                    sikidom = new Szabalyos_Hatszog(new Pont(x,y),a);
                    break;
                default:
                    throw new InvalidInputException();
            }
            sikidomok.add(sikidom);
        }
    }
    
    public void report() {
        System.out.println("A legközelebbi sikidom(ok) a ponthoz: ");
        double minTav = -1;
        ArrayList<String> result = new ArrayList<String>();
        result.add("Nincs megadva sikidom");
        for (Sikidom v : sikidomok) {
            double tav = v.Tavolsag(fixpont);
            if (minTav == -1) {
                minTav = tav;
                result.clear();
                result.add(v.toString());
            }
            else if (tav == minTav) {
                result.add(v.toString());
            }
            else if (tav < minTav) {
                minTav = tav;
                result.clear();
                result.add(v.toString());
            }
            }
        for (int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i));
        }
    }
        

    public void sclear() {
        sikidomok.clear();
    }
}
