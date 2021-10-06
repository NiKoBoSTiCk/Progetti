package it.niko;


import it.niko.model.*;

public class Test2 {
    public static void main(String[] args) {

        Configurazione c = new Configurazione
                .Builder(8, 100, 10, 10)
                .aggiungiSerpente(98, 79)
                .aggiungiSerpente(95, 75)
                .aggiungiSerpente(93, 73)
                .aggiungiSerpente(87, 24)
                .aggiungiSerpente(64, 60)
                .aggiungiSerpente(62, 19)
                .aggiungiSerpente(54, 34)
                .aggiungiSerpente(17, 7)
                .aggiungiScala(80, 100)
                .aggiungiScala(71, 91)
                .aggiungiScala(28, 84)
                .aggiungiScala(51, 67)
                .aggiungiScala(21, 42)
                .aggiungiScala(1, 38)
                .aggiungiScala(9, 31)
                .aggiungiScala(4, 14)
                .caselleSosta(true)
                .aggiungiCasellaSpeciale(99, ECasella.locanda)
                .aggiungiCasellaSpeciale(2, ECasella.panchina)
                .casellePremio(true)
                .aggiungiCasellaSpeciale(90, ECasella.molla)
                .aggiungiCasellaSpeciale(97, ECasella.dadi)
                .doppioSei(true)
                .casellePesca(true, 10)
                .aggiungiCasellaSpeciale(55, ECasella.pesca)
                .aggiungiCasellaSpeciale(3, ECasella.pesca)
                .aggiungiCasellaSpeciale(89, ECasella.pesca)
                .aggiungiCarta(ECarta.divieto)
                .aggiungiCarta(ECarta.molla)
                .aggiungiCarta(ECarta.divieto)
                .aggiungiCarta(ECarta.locanda)
                .aggiungiCarta(ECarta.dadi)
                .aggiungiCarta(ECarta.divieto)
                .aggiungiCarta(ECarta.divieto)
                .aggiungiCarta(ECarta.panchina)
                .aggiungiCarta(ECarta.molla)
                .aggiungiCarta(ECarta.dadi)
                .avanzamentoAutomatico(true)
                .build();

        ScaleESerpenti gioco = new ScaleESerpenti(c);
        System.out.println(gioco.avviaPartita());
    }
}
