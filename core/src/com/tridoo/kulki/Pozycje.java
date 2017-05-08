package com.tridoo.kulki;


import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;

public class Pozycje {

    public static Vector2 PRZYRZAD1=new Vector2(-16,9);
    public static Vector2 PRZYRZAD2=new Vector2(-16,6);
    public static Vector2 PRZYRZAD3=new Vector2(-16,2);
    public static Vector2 PRZYRZAD4=new Vector2(-16,-2);
    public static Vector2 PRZYRZAD5=new Vector2(-16,-6);
    public static Vector2 PRZYRZAD6=new Vector2(-16,-10);


    HashMap<Integer, ArrayList> kubki = new HashMap<Integer, ArrayList>();
    HashMap<Integer, ArrayList> kulki = new HashMap<Integer, ArrayList>();

    public Pozycje(){
        ArrayList<Vector2> pozycjeKubkow1 = new ArrayList<Vector2>();
        ArrayList<Vector2> pozycjeKubkow2 = new ArrayList<Vector2>();
        ArrayList<Vector2> pozycjeKubkow3 = new ArrayList<Vector2>();
        ArrayList<Vector2> pozycjeKubkow4 = new ArrayList<Vector2>();
        ArrayList<Vector2> pozycjeKubkow5 = new ArrayList<Vector2>();
        ArrayList<Vector2> pozycjeKubkow6 = new ArrayList<Vector2>();
        ArrayList<Vector2> pozycjeKubkow7 = new ArrayList<Vector2>();
        ArrayList<Vector2> pozycjeKubkow8 = new ArrayList<Vector2>();

        pozycjeKubkow1.add(new Vector2(10, -4.5f));
        pozycjeKubkow2.add(new Vector2(2, -5f));
        pozycjeKubkow2.add(new Vector2(-10, -8f));
        pozycjeKubkow3.add(new Vector2(12, -9f));
        pozycjeKubkow3.add(new Vector2(-11, -4f));
        pozycjeKubkow4.add(new Vector2(-11, -9f));
        pozycjeKubkow4.add(new Vector2(9, -9f));
        pozycjeKubkow5.add(new Vector2(4, -9f));
        pozycjeKubkow5.add(new Vector2(-11, -4f));
        pozycjeKubkow6.add(new Vector2(-11.5f, -3.5f));
        pozycjeKubkow6.add(new Vector2(4, -9.5f));

        pozycjeKubkow7.add(new Vector2(5.5f, 4f));
        pozycjeKubkow7.add(new Vector2(-9, -5f));

        kubki.put(1, pozycjeKubkow1);
        kubki.put(2, pozycjeKubkow2);
        kubki.put(3, pozycjeKubkow3);
        kubki.put(4, pozycjeKubkow4);
        kubki.put(5, pozycjeKubkow5);
        kubki.put(6, pozycjeKubkow6);
        kubki.put(7, pozycjeKubkow7);
        kubki.put(8, pozycjeKubkow8);


        ArrayList<Vector2> pozycjeKulek1 = new ArrayList<Vector2>();
        ArrayList<Vector2> pozycjeKulek2 = new ArrayList<Vector2>();
        ArrayList<Vector2> pozycjeKulek3 = new ArrayList<Vector2>();
        ArrayList<Vector2> pozycjeKulek4 = new ArrayList<Vector2>();
        ArrayList<Vector2> pozycjeKulek5 = new ArrayList<Vector2>();
        ArrayList<Vector2> pozycjeKulek6 = new ArrayList<Vector2>();
        ArrayList<Vector2> pozycjeKulek7 = new ArrayList<Vector2>();
        ArrayList<Vector2> pozycjeKulek8 = new ArrayList<Vector2>();
        pozycjeKulek1.add(new Vector2(-12.5f , 9.75f));
        pozycjeKulek2.add(new Vector2(-9f , 9f));
        pozycjeKulek2.add(new Vector2(-12.5f , 7.75f));
        pozycjeKulek3.add(new Vector2(-12.5f , 9.75f));
        pozycjeKulek3.add(new Vector2(-9f , 7f));
        pozycjeKulek4.add(new Vector2(-12.5f , 9.75f));
        pozycjeKulek4.add(new Vector2(-11f , 7.5f));
        pozycjeKulek5.add(new Vector2(-12.5f , 9.75f));
        pozycjeKulek5.add(new Vector2(-10f , 7.25f));
        pozycjeKulek6.add(new Vector2(-12.5f , 9.75f));
        pozycjeKulek6.add(new Vector2(-10.5f , 7.25f));

        pozycjeKulek7.add(new Vector2(-12.5f , 9.75f));
        pozycjeKulek7.add(new Vector2(-9f , 7f));

        kulki.put(1, pozycjeKulek1);
        kulki.put(2, pozycjeKulek2);
        kulki.put(3, pozycjeKulek3);
        kulki.put(4, pozycjeKulek4);
        kulki.put(5, pozycjeKulek5);
        kulki.put(6, pozycjeKulek6);
        kulki.put(7, pozycjeKulek7);
        kulki.put(8, pozycjeKulek8);

    }
}
