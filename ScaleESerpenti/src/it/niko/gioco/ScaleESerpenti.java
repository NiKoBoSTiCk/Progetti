package it.niko.gioco;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ScaleESerpenti {
    private final int lunghezzaP;
    private final Map<Integer, Integer> serpenti, scale;
    private final Map<Integer, String> caselleSpeciali;
    private final int[] posizioni;
    private final int[] soste;
    private final int numGiocatori;
    private final int numDadi;
    private final boolean soloDado;
    private final boolean doppioSei;
    private final Random random;

    public ScaleESerpenti(int lunghezzaP, int numGiocatori,
                          HashMap<Integer, Integer> serpenti,
                          HashMap<Integer, Integer> scale,
                          HashMap<Integer, String> caselleSpeciali,
                          int numDadi, boolean soloDado, boolean doppioSei){
        this.lunghezzaP = lunghezzaP;
        this.serpenti = serpenti;
        this.scale = scale;
        this.caselleSpeciali = caselleSpeciali;
        this.numGiocatori = numGiocatori;
        this.posizioni = new int[numGiocatori];
        this.soste = new int[numGiocatori];
        this.numDadi = numDadi;
        this.random = new Random();
        this.soloDado = soloDado;
        this.doppioSei = doppioSei;

        for(int i=0; i<numGiocatori; i++) {
            posizioni[i]=1; soste[i]=0;
        }
    }

    public void prossimoTurno(){

        for(int i=0; i<numGiocatori; i++){
            int lancio = turnoGiocatore(i);

            if(posizioni[i] == lunghezzaP) {
                break;
            }

            if(doppioSei && numDadi == 2 && lancio == 12){
                turnoGiocatore(i);
            }
        }
    }

    private int turnoGiocatore(int giocatore){

        if(soste[giocatore]!=0){
            soste[giocatore]--;
            return 0;
        }

        int lancio = lanciaDadi(posizioni[giocatore], soloDado);
        int nuovaPos = posizioni[giocatore] + lancio;

        while(caselleSpeciali.containsKey(nuovaPos)){
            int vecchiaPos = nuovaPos;
            nuovaPos = controllaCaselleSpeciali(nuovaPos, lancio, giocatore);
            if(vecchiaPos == nuovaPos) break;
        }

        if(nuovaPos > lunghezzaP){
            nuovaPos = lunghezzaP - (nuovaPos - lunghezzaP);
        }

        if(serpenti.containsKey(nuovaPos))
            posizioni[giocatore] = serpenti.get(nuovaPos);
        else posizioni[giocatore] = scale.getOrDefault(nuovaPos, nuovaPos);
        return lancio;
    }

    private int controllaCaselleSpeciali(int nuovaPos, int lancio, int giocatore){
        switch (caselleSpeciali.get(nuovaPos)) {
            case "panchina" -> soste[giocatore] = 1;
            case "locanda" -> soste[giocatore] = 3;
            case "dadi" -> nuovaPos += lanciaDadi(nuovaPos, soloDado);
            case "molla" -> nuovaPos += lancio;
        }
        return nuovaPos;
    }

    private int lanciaDadi(int posGiocatore, boolean soloDado) {
        int lancio = 0;
        if(posGiocatore >= lunghezzaP - 6 && soloDado)
            return random.nextInt(5) + 1;
        for(int i=0; i<numDadi; i++){
            lancio += random.nextInt(5) + 1;
        }
        return lancio;
    }

}
