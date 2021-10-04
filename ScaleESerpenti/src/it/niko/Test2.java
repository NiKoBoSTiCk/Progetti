package it.niko;


import it.niko.model.*;

public class Test2 {
    public static void main(String[] args) {

        ScaleESerpenti gioco = new ScaleESerpenti();

        gioco.carica("configurazioneStandard");
        gioco.avviaPartitaAutomatico();
    }
}
