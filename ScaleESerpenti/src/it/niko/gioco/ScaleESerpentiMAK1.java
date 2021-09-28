package it.niko.gioco;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ScaleESerpentiMAK1 {
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
    private final Mazzov1 mazzo;
    private int giocatore;
    private boolean partitaTerminata;
    private StringBuilder logTurno;

    public ScaleESerpentiMAK1(int lunghezzaP, int numGiocatori, int numDadi,
                              HashMap<Integer, Integer> serpenti,
                              HashMap<Integer, Integer> scale,
                              HashMap<Integer, String> caselleSpeciali,
                              Mazzov1 mazzo,
                              boolean soloDado, boolean doppioSei){
        this.lunghezzaP = lunghezzaP;
        this.numGiocatori = numGiocatori;
        this.numDadi = numDadi;
        this.serpenti = serpenti;
        this.scale = scale;
        this.caselleSpeciali = caselleSpeciali;
        this.mazzo = mazzo;
        this.soloDado = soloDado;
        this.doppioSei = doppioSei;
        this.posizioni = new int[numGiocatori];
        this.soste = new int[numGiocatori];
        this.divieti = new int[numGiocatori];
        this.random = new Random();
        this.giocatore = 0;
        this.partitaTerminata = false;
        this.logTurno = new StringBuilder();

        for(int i=0; i<numGiocatori; i++) {
            posizioni[i]=1; soste[i]=0; divieti[i]=0;
        }
    }

    public String prossimoTurno(){
        if(partitaTerminata) throw new RuntimeException("La partita è già terminata\n");
        logTurno = new StringBuilder();
        logTurno.append(String.format("Turno giocatore %d\n", giocatore));
        int lancio = turnoGiocatore();
        if(doppioSei && numDadi == 2 && lancio == 12){
            logTurno.append(String.format("Il giocatore %d ha fatto DoppioSei, perciò tira di nuovo\n", giocatore));
            turnoGiocatore();
        }
        logTurno.append(String.format("Il giocatore %d termina il turno in posizione %d\n", giocatore, posizioni[giocatore]));
        if(posizioni[giocatore] == lunghezzaP) {
            logTurno.append(String.format("Il giocatore %d vince.\n", giocatore));
            partitaTerminata = true;
        }
        giocatore = (giocatore + 1) % numGiocatori;
        return logTurno.toString();
    }

    public int getPosizione(int giocatore){
        return posizioni[giocatore];
    }

    public int getSoste(int giocatore){
        return soste[giocatore];
    }

    public int getDivieti(int giocatore){ return divieti[giocatore];
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
            logTurno.append(String.format("Il giocatore %d ha %d soste\n", giocatore, soste[giocatore]));
            if(divieti[giocatore] != 0){
                logTurno.append(String.format("Il giocatore %d usa una carta divieto per evitare la sosta\n", giocatore));
                divieti[giocatore]--;
                soste[giocatore] = 0;
            }
            else{
                soste[giocatore]--;
                logTurno.append(String.format("Il giocatore %d sconta la sosta. Restanti %d\n", giocatore, soste[giocatore]));
                return true;
            }
        }
        return false;
    }

    private int lanciaDadi() {
        int lancio = 0;
        if(soloDado && posizioni[giocatore] >= lunghezzaP - 6) {
            lancio = random.nextInt(6) + 1;
            logTurno.append(String.format("Il giocatore %d lancia i dadi e ottiene %d\n", giocatore, lancio));
            return lancio;
        }
        for(int i=0; i<numDadi; i++){
            lancio += random.nextInt(6) + 1;
        }
        logTurno.append(String.format("Il giocatore %d lancia i dadi e ottiene %d\n", giocatore, lancio));
        return lancio;
    }

    private int muovi(int lancio){
        int nuovaPos = posizioni[giocatore] + lancio;
        logTurno.append(String.format("Il giocatore %d si sposta da %d a %d\n", giocatore, posizioni[giocatore], nuovaPos));
        if(nuovaPos > lunghezzaP){
            nuovaPos = lunghezzaP - (nuovaPos - lunghezzaP);
            logTurno.append(String.format("Il giocatore %d ha superato il limite del tabellone, tornando alla posizione %d\n", giocatore, nuovaPos));
        }
        return nuovaPos;
    }

    private int controllaCasella(int nuovaPos, int lancio){
        while(caselleSpeciali.containsKey(nuovaPos)){
            logTurno.append(String.format("Il giocatore %d è su una casella speciale\n", giocatore));
            int vecchiaPos = nuovaPos;
            nuovaPos = casellaSpeciale(nuovaPos, lancio);
            if(vecchiaPos == nuovaPos) break;
            logTurno.append(String.format("Il giocatore %d si sposta da %d a %d\n", giocatore, vecchiaPos, nuovaPos));
            if(nuovaPos > lunghezzaP){
                nuovaPos = lunghezzaP - (nuovaPos - lunghezzaP);
                logTurno.append(String.format("Il giocatore %d ha superato il limite del tabellone, torna alla posizione %d\n", giocatore, nuovaPos));
            }
        }
        if(serpenti.containsKey(nuovaPos)){
            nuovaPos = serpenti.get(nuovaPos);
            logTurno.append(String.format("Il giocatore %d è sulla testa di un serpente, si sposta nella posizione %d\n", giocatore, nuovaPos));
        }
        else if(scale.containsKey(nuovaPos)){
            nuovaPos = scale.get(nuovaPos);
            logTurno.append(String.format("Il giocatore %d è ai piedi di una scala, si sposta nella posizione %d\n", giocatore, nuovaPos));
        }
        return nuovaPos;
    }

    private int casellaSpeciale(int nuovaPos, int lancio){
        String casellaSpeciale = caselleSpeciali.get(nuovaPos);
        logTurno.append(String.format("Il giocatore %d è su una casella %s\n", giocatore, casellaSpeciale));
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
        logTurno.append(String.format("Il giocatore %d pesca la carta %s\n", giocatore, carta));
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
