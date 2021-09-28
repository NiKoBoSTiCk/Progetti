package it.niko.model;
import java.io.*;
import java.util.ArrayDeque;
import java.util.Queue;

public class ScaleESerpenti implements Serializable {
    private static ScaleESerpenti INSTANCE = null;
    private TabelloneIF tabellone;
    private Queue<Giocatore> giocatori;
    private Mazzo mazzo;
    private int numeroDadi;
    private boolean DoppioSei;

    public ScaleESerpenti() {}

    public static synchronized ScaleESerpenti getInstance() {
        if(INSTANCE == null){
            INSTANCE = new ScaleESerpenti();
        }
        return INSTANCE;
    }

    public void nuovaPartita(int fine, int numeroDadi) {
        tabellone = new Tabellone(fine, numeroDadi);
        this.numeroDadi = numeroDadi;
        this.giocatori = new ArrayDeque<>();
        this.mazzo = new Mazzo();
        this.DoppioSei = false;
    }

    public void avviaPartita() {
        if(giocatori.isEmpty())
            throw new IllegalStateException("Aggiungi dei giocatori per iniziare una partita!");

        while(!tabellone.eTerminata()) {
            Giocatore giocatore = giocatori.poll();

            assert giocatore != null;
            if(giocatore.getSoste() != 0) {
                if(giocatore.getDivieti() != 0) {
                    giocatore.usaDivieto();
                    mazzo.aggiungiCarta("divieto");
                    giocatore.scontaSosta();
                    int lancio = tabellone.turno(giocatore);
                    if(DoppioSei && numeroDadi == 2 && lancio == 12)
                        tabellone.turno(giocatore);
                    giocatori.offer(giocatore);
                }
                else {
                    giocatore.scontaSosta();
                    giocatori.offer(giocatore);
                }
            }
            else {
                int lancio = tabellone.turno(giocatore);
                if(DoppioSei && numeroDadi == 2 && lancio == 12)
                    tabellone.turno(giocatore);
                giocatori.offer(giocatore);
            }
        }
    }

    public void prossimoTurno() {
        if(giocatori.isEmpty())
            throw new IllegalStateException("Aggiungi dei giocatori per iniziare una partita!");

        try {
            Giocatore giocatore = giocatori.poll();
            int lancio = tabellone.turno(giocatore);
            if(DoppioSei && numeroDadi == 2 && lancio == 12)
                tabellone.turno(giocatore);
            giocatori.offer(giocatore);
        }
        catch(IllegalStateException e){
            System.out.println(e.getMessage());
        }
    }

    public void salvaPartita(String nomeFile) {
        ObjectOutputStream oss;
        try{
            oss = new ObjectOutputStream(new FileOutputStream(nomeFile));
            oss.writeObject(INSTANCE);
            oss.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void caricaPartita(String nomeFile) {
        ObjectInputStream ois;
        try{
            ois = new ObjectInputStream(new FileInputStream(nomeFile));
            INSTANCE = (ScaleESerpenti) ois.readObject();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void abilitaDoppioSei() {
        this.DoppioSei = true;
    }

    public void aggiungiGiocatore(String nome) {
        giocatori.add(new Giocatore(nome));
    }

    public void aggiungiScala(int inizio, int fine) {
        if(tabellone == null)
            throw new IllegalStateException("Crea una nuova partita per aggiungere una scala");
        tabellone = new Scala(tabellone, inizio, fine);
    }

    public void aggiungiSerpente(int inizio, int fine) {
        if(tabellone == null)
            throw new IllegalStateException("Crea una nuova partita per aggiungere un serpente");
        tabellone = new Serpente(tabellone, inizio, fine);
    }

    public void aggiungiPremio(int pos, String tipo) {
        if(tabellone == null)
            throw new IllegalStateException("Crea una nuova partita per aggiungere un premio");
        tabellone = new Premio(tabellone, pos, tipo);
    }

    public void aggiungiSosta(int pos, String tipo) {
        if(tabellone == null)
            throw new IllegalStateException("Crea una nuova partita per aggiungere una sosta");
        tabellone = new Sosta(tabellone, pos, tipo);
    }

    public void aggiungiPesca(int pos) {
        if(tabellone == null)
            throw new IllegalStateException("Crea una nuova partita per aggiungere una sosta");
        tabellone = new Pesca(tabellone, pos, mazzo);
    }

    public void modificaMazzo(String carta){
        mazzo.aggiungiCarta(carta);
    }

    public static void main(String[] args) {
        ScaleESerpenti game = new ScaleESerpenti();

        game.nuovaPartita(100, 2);

        game.aggiungiGiocatore("Salvatore");
        game.aggiungiGiocatore("bho");

        game.abilitaDoppioSei();
        game.aggiungiSerpente(54, 3);
        game.aggiungiScala(2, 88);
        game.modificaMazzo("divieto");
        game.aggiungiPremio(45, "molla");

        game.prossimoTurno();
        game.prossimoTurno();
        game.prossimoTurno();
        game.prossimoTurno();
        game.prossimoTurno();

        game.avviaPartita();
    }
}
