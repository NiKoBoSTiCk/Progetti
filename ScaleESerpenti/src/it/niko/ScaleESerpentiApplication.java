package it.niko;

import javax.swing.*;
import java.awt.*;

public class ScaleESerpentiApplication {
    public static void main(String[] args) {

        JFrame f = new JFrame();

        Toolkit infoGrafiche = f.getToolkit();
        Dimension risoluzione = infoGrafiche.getScreenSize();

        JMenuBar menuBar = new JMenuBar();

        JMenu gioca = new JMenu("Gioca");
        JMenu configura = new JMenu("Configura Varianti");

        JMenuItem avviaPartita = new JMenuItem("Inizia Partita");
        JMenuItem salva = new JMenuItem("Salva");
        JMenuItem carica = new JMenuItem("Carica");

        JMenuItem setTabellone = new JMenuItem("Tabellone");
        JMenuItem serpente = new JMenuItem("Serpente");
        JMenuItem scala = new JMenuItem("Scala");
        JMenuItem dadoSingolo = new JMenuItem("Dado Singolo");
        JMenuItem lancioSoloDado = new JMenuItem("Lancio Di Un Solo Dado");
        JMenuItem doppioSei = new JMenuItem("Doppio Sei");
        JMenuItem caselleSosta = new JMenuItem("Caselle Sosta");
        JMenuItem casellePremio = new JMenuItem("Caselle Premio");
        JMenuItem casellePesca = new JMenuItem("Caselle Pesca");
        JMenuItem mazzo = new JMenuItem("Carte Mazzo");
        JMenuItem carteDivieto = new JMenuItem("Carte Divieto");

        gioca.add(avviaPartita);
        gioca.add(salva);
        gioca.add(carica);

        configura.add(setTabellone);
        configura.add(serpente);
        configura.add(scala);
        configura.add(dadoSingolo);
        configura.add(lancioSoloDado);
        configura.add(doppioSei);
        configura.add(caselleSosta);
        configura.add(casellePremio);
        configura.add(casellePesca);
        configura.add(mazzo);
        configura.add(carteDivieto);

        menuBar.add(gioca);
        menuBar.add(configura);

        int R = 10;
        int C = 10;
        int x = 1;
        boolean alternati = true;
        GridLayout tabelloneLayout = new GridLayout(R,C);
        JButton[][] caselle = new JButton[R][C];
        JPanel tabellone = new JPanel();
        tabellone.setLayout(tabelloneLayout);

        for(int i=R-1; i>=0; i--) {
            if(alternati) {
                for (int j = 0; j < C; j++) {
                    caselle[i][j] = new JButton("" + x);
                    x++;
                }
                alternati = false;
            }
            else {
                for (int j = C - 1; j >= 0; j--) {
                    caselle[i][j] = new JButton("" + x);
                    x++;
                }
                alternati = true;
            }
        }
        for(int i=0; i<R; i++){
            for(int j=0; j<C; j++){
                tabellone.add(caselle[i][j]);
            }
        }

        f.add(menuBar, BorderLayout.NORTH);
        f.add(tabellone);
        f.setBounds(risoluzione.width/4, risoluzione.height/5, 0, 0);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}
