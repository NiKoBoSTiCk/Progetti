package it.niko;

import it.niko.gioco.Mazzov1;
import it.niko.gioco.ScaleESerpentiMAK1;
import java.util.*;

public class Test {
    public static void main(String[] args) {
        HashMap<Integer, Integer> serpenti = new HashMap<>();
        serpenti.put(98,79);
        serpenti.put(95,75);
        serpenti.put(93,73);
        serpenti.put(87,24);
        serpenti.put(64,60);
        serpenti.put(62,19);
        serpenti.put(54,34);
        serpenti.put(17,7);

        HashMap<Integer, Integer> scale = new HashMap<>();
        scale.put(1,38);
        scale.put(4,14);
        scale.put(9,31);
        scale.put(21,42);
        scale.put(28,84);
        scale.put(51,67);
        scale.put(71,91);
        scale.put(80,100);

        HashMap<Integer, String> caselleSpeciali = new HashMap<>();
        caselleSpeciali.put(25, "molla");
        caselleSpeciali.put(57, "locanda");
        caselleSpeciali.put(61, "dadi");
        caselleSpeciali.put(99, "panchina");
        caselleSpeciali.put(13, "pesca");
        caselleSpeciali.put(70, "pesca");
        caselleSpeciali.put(97, "pesca");

        String[] carte = {"molla", "locanda", "divieto" ,"panchina", "dadi", "molla", "divieto" ,"dadi", "divieto" ,"panchina", "locanda"};
        Collections.shuffle(Arrays.asList(carte));
        Mazzov1 mazzo = new Mazzov1(carte);

        ScaleESerpentiMAK1 gioco = new ScaleESerpentiMAK1(
                100,
                5,
                2,
                serpenti,
                scale,
                caselleSpeciali,
                mazzo,
                true,
                true
        );

        while(!gioco.eTerminata()){
            System.out.println(gioco.prossimoTurno());
        }
    }
}
