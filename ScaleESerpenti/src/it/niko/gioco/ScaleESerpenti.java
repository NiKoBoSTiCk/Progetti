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
    private int giocatore;
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
        this.giocatore = 0;
        this.partitaTerminata = false;

        for(int i=0; i<numGiocatori; i++) {
            posizioni[i]=1; soste[i]=0; divieti[i]=0;
        }
    }

    public void prossimoTurno(){
        if(partitaTerminata) throw new RuntimeException("La partita è già terminata\n");
        System.out.format("Turno giocatore %d\n", giocatore);
        int lancio = turnoGiocatore();
        if(doppioSei && numDadi == 2 && lancio == 12){
            System.out.format("Il giocatore %d ha fatto DoppioSei, perciò tira di nuovo\n", giocatore);
            turnoGiocatore();
        }
        System.out.format("Il giocatore %d termina il turno in posizione %d\n", giocatore, posizioni[giocatore]);
        if(posizioni[giocatore] == lunghezzaP) {
            System.out.format("Il giocatore %d vince.\n", giocatore);
            partitaTerminata = true;
        }
        giocatore = (giocatore + 1) % numGiocatori;
    }

    public boolean eTerminata(){
        return partitaTerminata;
    }

    private int turnoGiocatore(){
        if(haSoste()) return 0;
        int lancio = lanciaDadi();
        int nuovaPos = muovi(lancio);
        nuovaPos = controllaCasella(nuovaPos, lancio);
        posizioni[giocatore] = nuovaPos;
        return lancio;
    }

    private boolean haSoste(){
        if(soste[giocatore] != 0){
            System.out.format("Il giocatore %d ha %d soste\n", giocatore, soste[giocatore]);
            if(divieti[giocatore] != 0){
                System.out.format("Il giocatore %d usa una carta divieto per evitare la sosta\n", giocatore);
                divieti[giocatore]--;
                soste[giocatore] = 0;
            }
            else{
                soste[giocatore]--;
                System.out.format("Il giocatore %d sconta la sosta. Restanti %d\n", giocatore, soste[giocatore]);
                return true;
            }
        }
        return false;
    }

    private int lanciaDadi() {
        int lancio = 0;
        if(soloDado && posizioni[giocatore] >= lunghezzaP - 6) {
            lancio = random.nextInt(5) + 1;
            System.out.format("Il giocatore %d lancia i dadi e ottiene %d\n", giocatore, lancio);
            return lancio;
        }
        for(int i=0; i<numDadi; i++){
            lancio += random.nextInt(5) + 1;
        }
        System.out.format("Il giocatore %d lancia i dadi e ottiene %d\n", giocatore, lancio);
        return lancio;
    }

    private int muovi(int lancio){
        int nuovaPos = posizioni[giocatore] + lancio;
        System.out.format("Il giocatore %d si sposta da %d a %d\n", giocatore, posizioni[giocatore], nuovaPos);
        if(nuovaPos > lunghezzaP){
            nuovaPos = lunghezzaP - (nuovaPos - lunghezzaP);
            System.out.format("Il giocatore %d ha superato il limite del tabellone, tornando alla posizione %d\n", giocatore, nuovaPos);
        }
        return nuovaPos;
    }

    private int controllaCasella(int nuovaPos, int lancio){
        while(caselleSpeciali.containsKey(nuovaPos)){
            System.out.format("Il giocatore %d è su una casella speciale\n", giocatore);
            int vecchiaPos = nuovaPos;
            nuovaPos = casellaSpeciale(nuovaPos, lancio);
            if(vecchiaPos == nuovaPos) break;
            System.out.format("Il giocatore %d si sposta da %d a %d\n", giocatore, vecchiaPos, nuovaPos);
            if(nuovaPos > lunghezzaP){
                nuovaPos = lunghezzaP - (nuovaPos - lunghezzaP);
                System.out.format("Il giocatore %d ha superato il limite del tabellone, tornando alla posizione %d\n", giocatore, nuovaPos);
            }
        }
        if(serpenti.containsKey(nuovaPos)){
            nuovaPos = serpenti.get(nuovaPos);
            System.out.format("Il giocatore %d è sulla testa di un serpente, si sposta nella posizione %d\n", giocatore, nuovaPos);
        }
        else if(scale.containsKey(nuovaPos)){
            nuovaPos = scale.get(nuovaPos);
            System.out.format("Il giocatore %d è ai piedi di una scala, si sposta nella posizione %d\n", giocatore, nuovaPos);
        }
        return nuovaPos;
    }

    private int casellaSpeciale(int nuovaPos, int lancio){
        String casellaSpeciale = caselleSpeciali.get(nuovaPos);
        System.out.format("Il giocatore %d è su una casella %s\n", giocatore, casellaSpeciale);
        switch(casellaSpeciale){
            case "panchina" -> soste[giocatore] = 1;
            case "locanda"  -> soste[giocatore] = 3;
            case "dadi"     -> nuovaPos += lanciaDadi();
            case "molla"    -> nuovaPos += lancio;
            case "pesca"    -> nuovaPos += pesca(lancio);
        }
        return nuovaPos;
    }

    private int pesca(int lancio){
        String carta = mazzo.pescaCarta();
        System.out.format("Il giocatore %d pesca la carta %s\n", giocatore, carta);
        switch(carta){
            case "panchina" -> { soste[giocatore] = 1; return 0; }
            case "locanda"  -> { soste[giocatore] = 3; return 0; }
            case "divieto"  -> { divieti[giocatore] += 1; return 0; }
            case "dadi"     -> { return lanciaDadi(); }
            case "molla"    -> { return lancio; }
        }
        throw new IllegalStateException("Carta pescata non valida!");
    }
}
