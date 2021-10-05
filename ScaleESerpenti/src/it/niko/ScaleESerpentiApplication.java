package it.niko;

import it.niko.model.ECasella;
import it.niko.model.TabelloneNaive;

import javax.sound.sampled.Line;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Map;

public class ScaleESerpentiApplication {

    public static void main(String[] args) {
        JFrame f = new JFrame();
        Toolkit infoGrafiche = f.getToolkit();
        Dimension risoluzione = infoGrafiche.getScreenSize();

        TabelloneNaive tabellone = new TabelloneNaive(100, 10, 10);
        tabellone.aggiungiScala(5, 67);

        //menu
        JMenuBar menuBar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu configura = new JMenu("Configura Gioco");
        JMenuItem salva = new JMenuItem("Salva");
        JMenuItem carica = new JMenuItem("Carica");
        JMenuItem setTabellone = new JMenuItem("Tabellone");
        JMenuItem setVarianti = new JMenuItem("Varianti");
        file.add(salva);
        file.add(carica);
        configura.add(setTabellone);
        configura.add(setVarianti);
        menuBar.add(file);
        menuBar.add(configura);

        JPanel pannelloPrincipale = new JPanel();
        pannelloPrincipale.setLayout(new BorderLayout());
        
        //log gioco
        JPanel log = new JPanel();
        log.setLayout(new BorderLayout());
        log.add(new JLabel("LOG"), BorderLayout.NORTH);
        log.add(new JScrollPane(new JLabel("test")), BorderLayout.CENTER);
        pannelloPrincipale.add(log, BorderLayout.EAST);

        //legenda
        JPanel legenda = new JPanel();
        legenda.setLayout(new BorderLayout());
        legenda.add(new JLabel("Legenda"), BorderLayout.NORTH);
        pannelloPrincipale.add(legenda, BorderLayout.WEST);

        //tabellone
        JPanel tabellonePanel = tabellone.getGraphics();
        tabellonePanel.setPreferredSize(new Dimension(700, 700));
        pannelloPrincipale.add(tabellonePanel, BorderLayout.CENTER);


        JPanel bottoni = new JPanel();
        JButton prossimoTurno = new JButton("Prossimo Turno");
        JButton auto = new JButton("Auto");
        bottoni.add(prossimoTurno);
        bottoni.add(auto);
        pannelloPrincipale.add(bottoni, BorderLayout.SOUTH);

        f.add(menuBar, BorderLayout.NORTH);
        f.add(pannelloPrincipale);
        f.setBounds(risoluzione.width/8, risoluzione.height/7, 0, 0);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setTitle("Scale e Serpenti");
        f.setVisible(true);
    }



}
