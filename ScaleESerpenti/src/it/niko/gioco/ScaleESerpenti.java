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
    private final int[] divieti;
    private final int numGiocatori;
    private final int numDadi;
    private final boolean soloDado;
    private final boolean doppioSei;
    private final Random random;
    private final Mazzo mazzo;
    private boolean partitaTerminata;

    public ScaleESerpenti(int lunghezzaP, int numGiocatori,
                          HashMap<Integer, Integer> serpenti,
                          HashMap<Integer, Integer> scale,
                          HashMap<Integer, String> caselleSpeciali,
                          Mazzo mazzo,
                          int numDadi, boolean soloDado, boolean doppioSei){
        this.lunghezzaP = lunghezzaP;
        this.serpenti = serpenti;
        this.scale = scale;
        this.caselleSpeciali = caselleSpeciali;
        this.numGiocatori = numGiocatori;
        this.posizioni = new int[numGiocatori];
        this.soste = new int[numGiocatori];
        this.divieti = new int[numGiocatori];
        this.numDadi = numDadi;
        this.random = new Random();
        this.mazzo = mazzo;
        this.soloDado = soloDado;
        this.doppioSei = doppioSei;
        this.partitaTerminata = false;

        for(int i=0; i<numGiocatori; i++) {
            posizioni[i]=1; soste[i]=0; divieti[i]=0;
        }
    }

    public void prossimoTurno(){
        if(partitaTerminata) throw new RuntimeException("La partita è già terminata");

        for(int i=0; i<numGiocatori; i++){
            int lancio = turnoGiocatore(i);

            if(posizioni[i] == lunghezzaP) {
                partitaTerminata = true;
                break;
            }

            if(doppioSei && numDadi == 2 && lancio == 12){
                turnoGiocatore(i);
            }
        }
    }

    private int turnoGiocatore(int giocatore){

        if(soste[giocatore] != 0){
            if(divieti[giocatore] != 0){
                divieti[giocatore]--;
                soste[giocatore] = 0;
            }
            else{
                soste[giocatore]--;
                return 0;
            }
        }

        int lancio = lanciaDadi(posizioni[giocatore], soloDado);
        int nuovaPos = posizioni[giocatore] + lancio;

        while(caselleSpeciali.containsKey(nuovaPos)){
            int vecchiaPos = nuovaPos;
            nuovaPos = casellaSpeciale(nuovaPos, lancio, giocatore);
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

    private int casellaSpeciale(int nuovaPos, int lancio, int giocatore){
        switch (caselleSpeciali.get(nuovaPos)) {
            case "panchina" -> soste[giocatore] = 1;
            case "locanda" -> soste[giocatore] = 3;
            case "dadi" -> nuovaPos += lanciaDadi(nuovaPos, soloDado);
            case "molla" -> nuovaPos += lancio;
            case "pesca" -> nuovaPos += pesca(nuovaPos, lancio, giocatore);
        }
        return nuovaPos;
    }

    private int pesca(int nuovaPos, int lancio, int giocatore){
        String carta = mazzo.pescaCarta();
        switch (carta){
            case "panchina" -> soste[giocatore] = 1;
            case "locanda" -> soste[giocatore] = 3;
            case "dadi" -> nuovaPos += lanciaDadi(nuovaPos, soloDado);
            case "molla" -> nuovaPos += lancio;
            case "divieto" -> divieti[giocatore] += 1;
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
