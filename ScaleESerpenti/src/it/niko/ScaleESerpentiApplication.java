package it.niko;

import javax.swing.*;
import javax.swing.text.IconView;
import java.awt.*;

public class ScaleESerpentiApplication {
    public static void main(String[] args) {

        JFrame f = new JFrame();

        Toolkit infoGrafiche = f.getToolkit();
        Dimension risoluzione = infoGrafiche.getScreenSize();

        JMenuBar menuBar = new JMenuBar();

        JMenu gioca = new JMenu("Gioca");
        JMenu configura = new JMenu("Configura Gioco");

        JMenuItem avviaPartita = new JMenuItem("Inizia Partita");
        JMenuItem salva = new JMenuItem("Salva");
        JMenuItem carica = new JMenuItem("Carica");

        JMenuItem setTabellone = new JMenuItem("Tabellone");
        JMenuItem setVarianti = new JMenuItem("Varianti");

        gioca.add(avviaPartita);
        gioca.add(salva);
        gioca.add(carica);

        configura.add(setTabellone);
        configura.add(setVarianti);

        menuBar.add(gioca);
        menuBar.add(configura);

        BorderLayout frameLayout = new BorderLayout();
        JPanel pannelloPrincipale = new JPanel();
        pannelloPrincipale.setLayout(frameLayout);

        JPanel legenda = new JPanel();
        legenda.add(new JLabel("LEGENDA"));
        JPanel log = new JPanel();
        log.add(new JLabel("LOG"));
        JPanel tabellone = creaTabellone(10, 10);

        pannelloPrincipale.add(legenda, BorderLayout.WEST);
        pannelloPrincipale.add(tabellone, BorderLayout.CENTER);
        pannelloPrincipale.add(log, BorderLayout.EAST);
        f.add(menuBar, BorderLayout.NORTH);
        f.add(pannelloPrincipale);
        f.setBounds(risoluzione.width/4, risoluzione.height/5, 0, 0);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setTitle("Scale e Serpenti");
        f.setVisible(true);
    }

    private static JPanel creaTabellone(int R, int C) {
        JPanel tabellone = new JPanel();
        int x = 0;
        boolean alternati = true;
        GridLayout tabelloneLayout = new GridLayout(R,C);
        JPanel[][] caselle = new JPanel[R][C];
        tabellone.setLayout(tabelloneLayout);

        for(int i=R-1; i>=0; i--) {
            if(alternati) {
                for(int j = 0; j < C; j++) {
                    caselle[i][j] = new JPanel();
                    caselle[i][j].setLayout(new GridLayout(2, 1));
                    JPanel sopra = new JPanel();
                    sopra.add(new JLabel("giocatori"));
                    JPanel sotto = new JPanel();
                    sotto.setLayout(new GridLayout(1, 2));
                    sotto.add(new JButton("effetto"));
                    sotto.add(new JButton("" + ++x));
                    caselle[i][j].add(sopra);
                    caselle[i][j].add(sotto);
                }
                alternati = false;
            }
            else {
                for(int j = C - 1; j >= 0; j--) {
                    caselle[i][j] = new JPanel();
                    caselle[i][j].setLayout(new GridLayout(2, 1));
                    JPanel sopra = new JPanel();
                    sopra.add(new JLabel("giocatori"));
                    JPanel sotto = new JPanel();
                    sotto.setLayout(new GridLayout(1, 2));
                    sotto.add(new JButton("effetto"));
                    sotto.add(new JButton("" + ++x));
                    caselle[i][j].add(sopra);
                    caselle[i][j].add(sotto);
                }
                alternati = true;
            }
        }
        for(int i=0; i<R; i++)
            for(int j=0; j<C; j++)
                tabellone.add(caselle[i][j]);
        return tabellone;
    }
}
