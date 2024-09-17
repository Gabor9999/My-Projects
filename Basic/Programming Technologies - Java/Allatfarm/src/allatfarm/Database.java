/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package allatfarm;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
/**
 *
 * @author mgabo
 */
public class Database {
    private final ArrayList<Allat> allatok;

    public Database() {
        allatok = new ArrayList<>();
    }
    
    public void read(String filename) throws FileNotFoundException, InvalidInputException {
        Scanner sc = new Scanner(new BufferedReader(new FileReader(filename)));
        int allatokSzama = sc.nextInt();
        while (sc.hasNext()) {
            Allat allat;
            switch (sc.next()) {
                case "T":
                    allat = new Tehen(sc.next(),sc.nextInt());
                    break;
                case "E":
                    allat = new Emu(sc.next(),sc.nextInt());
                    break;
                case "L":
                    allat = new Lo(sc.next(),sc.nextInt());
                    break;
                case "K":
                    allat = new Kecske(sc.next(),sc.nextInt());
                default:
                    throw new InvalidInputException();
            }
            int allatokKaja = sc.nextInt();
            for (int i = 0; i < allatokKaja; i++) {
                allat.plusKaja(sc.nextInt());
            }
            allatok.add(allat);
        }
    }
    public void report() {
        System.out.println("Animals in the database:");
        for (Allat v : allatok) {
            System.out.println(v);
        }
        System.out.println("Animals who eat 1kg food: ");
        for (Allat allat: allatok) {
            if (allat.Kg_fogyasztas()) {
                System.out.println(allat);
            }
        }
        System.out.println("Korosan sovany allatok: ");
        for (Allat allat: allatok) {
            if (allat.Korosan_sovany()) {
                System.out.println(allat);
            }
        }
    }
    public void clear() {
        allatok.clear();
    }
}
