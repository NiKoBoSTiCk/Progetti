package it.niko;

import it.niko.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScaleESerpentiApplication extends JFrame implements ActionListener {
  //  private static JFrame f;
    private ScaleESerpenti game;
    private Toolkit infoGrafiche;
    private Dimension risoluzione;
    private Tabellone tabellone;
    private Configurazione config;
    private JMenuBar menuBar;
    private JMenu file;
    private JMenu configura;
    private JMenuItem salva;
    private JMenuItem carica;
    private JMenuItem setTabellone;
    private JMenuItem setVarianti;
    private JPanel pannelloPrincipale;
    private JPanel pannelloLog;
    private JPanel pannelloTabellone;
    private JPanel pannelloBottoni;
    private JScrollBar scrollBarLog;
    private JLabel labelLog;
    private JButton prossimoTurno;
    private JButton auto;
    private StringBuilder log;

    public ScaleESerpentiApplication() {
        //   f = new JFrame();
        game = new ScaleESerpenti();
        tabellone = new TabelloneNaive(100, 10, 10);
        config = null;
        log = new StringBuilder();
        log.append("ciao");
        log.append("prova");
        //    infoGrafiche = f.getToolkit();
        infoGrafiche = this.getToolkit();
        risoluzione = infoGrafiche.getScreenSize();
        menuBar = new JMenuBar();
        file = new JMenu("File");
        configura = new JMenu("Configura Gioco");
        salva = new JMenuItem("Salva");
        carica = new JMenuItem("Carica");
        setTabellone = new JMenuItem("Tabellone");
        setVarianti = new JMenuItem("Varianti");
        pannelloPrincipale = new JPanel();
        pannelloLog = new JPanel();
        scrollBarLog = new JScrollBar();
        labelLog = new JLabel(log.toString());
        pannelloTabellone = tabellone.draw();
        pannelloBottoni = new JPanel();
        prossimoTurno = new JButton("Prossimo Turno");
        auto = new JButton("Auto");

        salva.addActionListener(this);
        carica.addActionListener(this);
        setTabellone.addActionListener(this);
        setVarianti.addActionListener(this);
        prossimoTurno.addActionListener(this);
        auto.addActionListener(this);

        //menu
        file.add(salva);
        file.add(carica);
        configura.add(setTabellone);
        configura.add(setVarianti);

        //menu bar
        menuBar.add(file);
        menuBar.add(configura);

        //log turni gioco
        pannelloLog.add(labelLog, BorderLayout.CENTER);
        pannelloLog.setLayout(new BorderLayout());
        pannelloLog.add(new JLabel("Log Turni"), BorderLayout.NORTH);
        pannelloLog.add(scrollBarLog);

        //tabellone
        pannelloTabellone.setPreferredSize(new Dimension(700, 700));

        //bottoni
        pannelloBottoni.add(prossimoTurno);
        pannelloBottoni.add(auto);

        //pannello principale
        pannelloPrincipale.setLayout(new BorderLayout());
        pannelloPrincipale.add(pannelloTabellone, BorderLayout.CENTER);
        pannelloPrincipale.add(pannelloLog, BorderLayout.EAST);
        pannelloPrincipale.add(pannelloBottoni, BorderLayout.SOUTH);

        //    f.add(menuBar, BorderLayout.NORTH);
        //    f.add(pannelloPrincipale);
        //    f.setBounds(risoluzione.width/8, risoluzione.height/7, 0, 0);
        //    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //    f.pack();
        //    f.setTitle("Scale E Serpenti");
        //    f.setVisible(true);
        add(menuBar, BorderLayout.NORTH);
        add(pannelloPrincipale);
        setBounds(risoluzione.width/8, risoluzione.height/7, 0, 0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setTitle("Scale E Serpenti");
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) {
        ScaleESerpentiApplication app = new ScaleESerpentiApplication();
    }
}
