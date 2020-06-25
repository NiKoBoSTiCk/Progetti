package poo.polinomi;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.swing.*;
import poo.util.BufferLimitato;
/**
 * Polinomio GUI è un'interfaccia grafica che gestisce le operazioni tra polinomi.
 * Permette l'inserimento e la rimozione dei polinomi con la conseseguente aggiunta o rimozione
 * in un pannello scrollabile di elementi CheckBox aggiornati dinamicamente. 
 * Le operazioni disponibili sono di valore, somma, sottrazione, moltiplicazione e derivata
 * @author Salvatore Romanello matricola 200711
 */
public class PolinomioGUI {
	public static void main(String []args){
		FrameGUI fc = new FrameGUI();
		fc.setVisible(true);	      
	}
}

class FrameGUI extends JFrame{
	private static final long serialVersionUID = 1L;
	private static String ePolinomio = "([\\-\\+]?(\\d*x(\\^\\d+)?|\\d+))([\\+\\-](\\d*x(\\^\\d+)?|\\d+))*";
	private String titolo = "Polinomio GUI";
	private AggiungiPolinomiAlCheckBox AP = null;
	private File fileDiSalvataggio = null;
	private JPanel pannello;
	private JScrollPane pannelloscroll;
	private JMenuItem aggiungiPolinomi, rimuoviPolinomi, apri, salva, salvaConNome, esci, info, 
		    valore, somma, sottrai, moltiplica, derivata, cronologia, svuota;	
	private Color rosso, tomato, bianco;
	private LinkedList<JCheckBox> elencoC;
	private LinkedList<String> elencoP;
	private BufferLimitato<String> bl;

	public FrameGUI(){
		setTitle(titolo);		
		setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );       
		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				if(consensoUscita()) System.exit(0);
			}
		} );	      
		AscoltatoreEventiAzione listener = new AscoltatoreEventiAzione();
		rosso = new Color(255, 0, 0);
		tomato = new Color(255, 99, 71);
		bianco = new Color(255, 255, 255);
		
		pannello = new JPanel();
		pannello.setLayout(new GridLayout(0, 1, 2, 2));        
		pannelloscroll = new JScrollPane(pannello);
		pannelloscroll.setLayout(new ScrollPaneLayout());
		add(pannelloscroll);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(rosso);
		setJMenuBar(menuBar);       
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);      
		apri = new JMenuItem("Apri");
		apri.setBackground(tomato);
		apri.addActionListener(listener);
		fileMenu.add(apri);        
		salva = new JMenuItem("Salva");
		salva.setBackground(tomato);
		salva.addActionListener(listener);
		fileMenu.add(salva);        
		salvaConNome = new JMenuItem("Salva con nome");
		salvaConNome.setBackground(tomato);
		salvaConNome.addActionListener(listener);
		fileMenu.add(salvaConNome);        
		fileMenu.addSeparator();        
		esci = new JMenuItem("Esci");
		esci.setBackground(rosso);
		esci.addActionListener(listener);
		fileMenu.add(esci);

		JMenu commandMenu = new JMenu("Comandi");
		menuBar.add(commandMenu);        
		aggiungiPolinomi = new JMenuItem("Aggiungi Polinomi");
		aggiungiPolinomi.setBackground(tomato);
		aggiungiPolinomi.addActionListener(listener);
		commandMenu.add(aggiungiPolinomi);        
		rimuoviPolinomi = new JMenuItem("Rimuovi Polinomi");
		rimuoviPolinomi.setEnabled(false);
		rimuoviPolinomi.addActionListener(listener);
		commandMenu.add(rimuoviPolinomi);        
		commandMenu.addSeparator();        
		valore = new JMenuItem("Valore");
		valore.setEnabled(false);
		valore.addActionListener(listener);
		commandMenu.add(valore);       
		somma = new JMenuItem("Somma");
		somma.setEnabled(false);
		somma.addActionListener(listener);
		commandMenu.add(somma);        
		sottrai = new JMenuItem("Sottrazione");
		sottrai.setEnabled(false);
		sottrai.addActionListener(listener);
		commandMenu.add(sottrai);        
		moltiplica = new JMenuItem("Moltiplicazione");
		moltiplica.setEnabled(false);
		moltiplica.addActionListener(listener);
		commandMenu.add(moltiplica);                 
		derivata = new JMenuItem("Derivata");
		derivata.setEnabled(false);
		derivata.addActionListener(listener);
		commandMenu.add(derivata);       
		commandMenu.addSeparator();       
		cronologia = new JMenuItem("Cronologia");
		cronologia.setEnabled(false);
		cronologia.addActionListener(listener);
		commandMenu.add(cronologia);        
		svuota = new JMenuItem("Svuota");
		svuota.setEnabled(false);
		svuota.addActionListener(listener);
		commandMenu.add(svuota);

		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		info = new JMenuItem("Info");
		info.setBackground(tomato);
		info.addActionListener(listener);
		helpMenu.add(info);
		
		elencoC = new LinkedList<>();
		elencoP = new LinkedList<>();
		bl = new BufferLimitato<>(2);

		pack();
		setLocation(700,400);
		setSize(500, 300);       
	}

	private class AggiungiPolinomiAlCheckBox extends JFrame implements ActionListener {	
		private static final long serialVersionUID = 1L;
		private JTextField polinomio; 

		public AggiungiPolinomiAlCheckBox() {   		
			setTitle("Aggiungi Polinomi");
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			addWindowListener( new WindowAdapter() {
				public void windowClosing(WindowEvent e){
					setVisible(false);
				}
			} );		
			setSize(400, 150);
			setLocation(900,500);
			setVisible(true);
			sistemaMenu();
			JPanel p = new JPanel();
			p.setLayout(new GridLayout(3,1));
			p.add(new JLabel("Polinomio", JLabel.CENTER));
			p.add(polinomio = new JTextField("",22));
			p.add(new JLabel("es. -3x^3+5x^2-7x+16", JLabel.CENTER));
			add(p, BorderLayout.CENTER);   	        
			polinomio.addActionListener(this);             
		}//costruttore

		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==polinomio){
				if(!polinomio.getText().matches(ePolinomio) || polinomio.getText().equals("")) {
					JOptionPane.showMessageDialog(null,"Polinomio non valido", "ERRORE", JOptionPane.ERROR_MESSAGE);
					polinomio.setText("");
				}
				else {
					try {
						JCheckBox cb;
						Polinomio pol = new PolinomioConcatenato();
						pol = pol.convertiPolinomio(polinomio.getText());
						if(pol.toString().equals("")) {
							cb = new JCheckBox("0");
							elencoP.add("0");
						}
						else {
							cb = new JCheckBox(pol.toString());
							elencoP.add(pol.toString());
						}
						FrameGUI.this.pannello.add(cb);
						FrameGUI.this.pannello.revalidate();
						FrameGUI.this.pannello.repaint();
						elencoC.add(cb);
						sistemaMenu();
						polinomio.setText("");
					}catch(Exception ex) {
						JOptionPane.showMessageDialog(null,"Polinomio non valido", "ERRORE", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}//actionPerformed
	}//AggiungiPolinomiAlCheckBox

	private class AscoltatoreEventiAzione implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==apri){
				JFileChooser chooser = new JFileChooser();
				try{
					if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
						if(!chooser.getSelectedFile().exists())
							JOptionPane.showMessageDialog(null,"File inesistente.",
									"ERRORE", JOptionPane.ERROR_MESSAGE); 						
						else{	
							fileDiSalvataggio = chooser.getSelectedFile();
							FrameGUI.this.setTitle(titolo + " - " + fileDiSalvataggio.getName());
							try{
								ripristina(fileDiSalvataggio.getAbsolutePath());
							}catch(IOException ioe){
								JOptionPane.showMessageDialog(null, "Apertura fallita, il file è malformato.", 
										"ERRORE", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
					else JOptionPane.showMessageDialog(null, "Apertura annullata.", 
							"APRI", JOptionPane.PLAIN_MESSAGE);
				}catch(Exception exc){
					exc.printStackTrace();
				}
			}//apri

			else if(e.getSource()==salva){
				JFileChooser chooser = new JFileChooser();
				try{
					if(fileDiSalvataggio!=null){
						int ans = JOptionPane.showConfirmDialog(null,"Vuoi sovrascrivere " + 
								  fileDiSalvataggio.getAbsolutePath()+" ?");
						if(ans==0)
							salva(fileDiSalvataggio.getAbsolutePath());
						else JOptionPane.showMessageDialog(null,"Salvataggio fallito.", 
								"ERRORE", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if(chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
						fileDiSalvataggio=chooser.getSelectedFile();
						FrameGUI.this.setTitle(titolo + " - " + fileDiSalvataggio.getName());
					}
					if(fileDiSalvataggio!=null)
						salva(fileDiSalvataggio.getAbsolutePath());
					else JOptionPane.showMessageDialog(null,"Salvataggio annullato.", 
								"SALVA", JOptionPane.PLAIN_MESSAGE);
				}catch(Exception exc){
					exc.printStackTrace();
				}
			}//salva

			else if(e.getSource()==salvaConNome){
				JFileChooser chooser = new JFileChooser();
				try{
					if(chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION ){
						fileDiSalvataggio=chooser.getSelectedFile();
						FrameGUI.this.setTitle(titolo + " - " + fileDiSalvataggio.getName());
					}
					if(fileDiSalvataggio!=null)
						salva(fileDiSalvataggio.getAbsolutePath());
					else 
						JOptionPane.showMessageDialog(null,"Salvataggio annulato.", 
							"SALVA CON NOME", JOptionPane.PLAIN_MESSAGE);
				}catch( Exception exc ){
					exc.printStackTrace();
				}  			   
			}//salvaConNome

			else if(e.getSource()==esci){
				if(consensoUscita()) System.exit(0);
			}//esci

			else if(e.getSource()==aggiungiPolinomi){
				if(AP==null) AP=new AggiungiPolinomiAlCheckBox();
				AP.setVisible(true);
			}//aggiungiPolinomi

			else if( e.getSource()==rimuoviPolinomi) {
				if(contaSelezionati()==0) JOptionPane.showMessageDialog(null, "Seleziona qualche Polinomio prima", 
						"ERRORE", JOptionPane.ERROR_MESSAGE);
				else {
					ListIterator<JCheckBox> lit = elencoC.listIterator();
					while(lit.hasNext()) {
						JCheckBox cb = lit.next();
						if(cb.isSelected()) {
							lit.remove(); 
							FrameGUI.this.pannello.remove(cb);
						}
					}	
					bl.clear();
					sistemaMenu();
					FrameGUI.this.pannello.revalidate();
					FrameGUI.this.pannello.repaint();				       
				}			       
			}//rimuoviPolinomi

			else if(e.getSource()==valore) {
				if(contaSelezionati()!=1) JOptionPane.showMessageDialog(null, "Seleziona esattamente un Polinomio",
						"ERRORE", JOptionPane.ERROR_MESSAGE);
				else {
					putSelezionati();
					try {
						String s = JOptionPane.showInputDialog(null, "Inserisci il valore della x");
						double ret = getSelezionato().valore(Integer.parseInt(s));
						JOptionPane.showMessageDialog(null, "Il valore del Polinomio è " + ret);
						bl.clear();
						deseleziona();
						sistemaMenu();
					}catch(Exception ex) {
						JOptionPane.showMessageDialog(null, "Per trovare il valore inserisci un numero valido",
								"ERRORE", JOptionPane.ERROR_MESSAGE);
					}
				}
			}//valore

			else if(e.getSource()==somma){
				if(contaSelezionati()!=2) JOptionPane.showMessageDialog(null, "Seleziona esattamente due Polinomi", 
						"ERRORE", JOptionPane.ERROR_MESSAGE);
				else {
					putSelezionati();		  			   
					Polinomio pol = getSelezionato().add(getSelezionato());
					if(!pol.equals(new PolinomioConcatenato())) {
						JOptionPane.showMessageDialog(null, "Il risultato dell'operazione è " + pol.toString(), 
								"Risultato", JOptionPane.PLAIN_MESSAGE);
						JCheckBox cb = new JCheckBox(pol.toString());
						FrameGUI.this.pannello.add(cb);
						FrameGUI.this.pannello.revalidate();
						FrameGUI.this.pannello.repaint();
						elencoC.add(cb);
						elencoP.add(pol.toString());
						bl.clear();
						deseleziona();
						sistemaMenu();
					}
					else {
						JOptionPane.showMessageDialog(null, "Il risultato dell'operazione è 0", 
								"Risultato", JOptionPane.PLAIN_MESSAGE);
						JCheckBox cb = new JCheckBox("0");
						FrameGUI.this.pannello.add(cb);
						FrameGUI.this.pannello.revalidate();
						FrameGUI.this.pannello.repaint();
						elencoC.add(cb);
						elencoP.add("0");
						bl.clear();
						deseleziona();
						sistemaMenu();
					}		  					
				}
			}//somma

			else if(e.getSource()==sottrai){
				if(contaSelezionati()!=2) JOptionPane.showMessageDialog(null, "Seleziona esattamente due Polinomi", 
						"ERRORE", JOptionPane.ERROR_MESSAGE);
				else {
					putSelezionati();	  			   
					Polinomio t1 = getSelezionato();
					Polinomio t2 = getSelezionato();
					Polinomio t = new PolinomioConcatenato();
					for(Monomio m: t2) t.add(new Monomio(m.getCoeff()*-1, m.getGrado()));
					Polinomio pol = t1.add(t);
					if(!pol.equals(new PolinomioConcatenato())) {
						JOptionPane.showMessageDialog(null, "Il risultato dell'operazione è " + pol.toString(), 
								"Risultato", JOptionPane.PLAIN_MESSAGE);
						JCheckBox cb = new JCheckBox(pol.toString());
						FrameGUI.this.pannello.add(cb);
						FrameGUI.this.pannello.revalidate();
						FrameGUI.this.pannello.repaint();
						elencoC.add(cb);
						elencoP.add(pol.toString());
						bl.clear();
						deseleziona();
						sistemaMenu();
					}
					else {
						JOptionPane.showMessageDialog(null, "Il risultato dell'operazione è 0", 
								"RISULTATO", JOptionPane.PLAIN_MESSAGE);
						JCheckBox cb = new JCheckBox("0");
						FrameGUI.this.pannello.add(cb);
						FrameGUI.this.pannello.revalidate();
						FrameGUI.this.pannello.repaint();
						elencoC.add(cb);
						elencoP.add("0");
						bl.clear();
						deseleziona();
						sistemaMenu();
					}		  				
				}
			}//sottrai

			else if(e.getSource()==moltiplica) {
				if(contaSelezionati()!=2) JOptionPane.showMessageDialog(null, "Seleziona esattamente due Polinomi", 
						"ERRORE", JOptionPane.ERROR_MESSAGE);
				else {
					putSelezionati();	  			 
					Polinomio res = getSelezionato().mul(getSelezionato());
					if(!res.equals(new PolinomioConcatenato())) {
						JOptionPane.showMessageDialog(null, "Il risultato dell'operazione è " + res.toString(), 
								"Risultato", JOptionPane.PLAIN_MESSAGE);
						JCheckBox cb = new JCheckBox(res.toString());
						FrameGUI.this.pannello.add(cb);
						FrameGUI.this.pannello.revalidate();
						FrameGUI.this.pannello.repaint();
						elencoC.add(cb);
						elencoP.add(res.toString());
						bl.clear();
						deseleziona();
						sistemaMenu();
					}
					else {
						JOptionPane.showMessageDialog(null, "Il risultato dell'operazione è 0", 
								"Risultato", JOptionPane.PLAIN_MESSAGE);
						JCheckBox cb = new JCheckBox("0");
						FrameGUI.this.pannello.add(cb);
						FrameGUI.this.pannello.revalidate();
						FrameGUI.this.pannello.repaint();
						elencoC.add(cb);
						elencoP.add("0");
						bl.clear();
						deseleziona();
						sistemaMenu();
					}		  			 
				}
			}//moltiplica

			else if(e.getSource()==derivata) {
				if(contaSelezionati()!=1) JOptionPane.showMessageDialog(null, "Seleziona esattamente un Polinomio",
						"ERRORE", JOptionPane.ERROR_MESSAGE);
				else {
					putSelezionati();		  			 
					Polinomio res = getSelezionato().derivata();
					if(!res.equals(new PolinomioConcatenato())) {
						JOptionPane.showMessageDialog(null, "Il risultato dell'operazione è " + res.toString(), 
								"Risultato", JOptionPane.PLAIN_MESSAGE);
						JCheckBox cb = new JCheckBox(res.toString());
						FrameGUI.this.pannello.add(cb);
						FrameGUI.this.pannello.revalidate();
						FrameGUI.this.pannello.repaint();
						elencoC.add(cb);
						elencoP.add(res.toString());
						bl.clear();
						deseleziona();
						sistemaMenu();
					}
					else {
						JOptionPane.showMessageDialog(null, "Il risultato dell'operazione è 0", 
								"Risultato", JOptionPane.PLAIN_MESSAGE);
						JCheckBox cb = new JCheckBox("0");
						FrameGUI.this.pannello.add(cb);
						FrameGUI.this.pannello.revalidate();
						FrameGUI.this.pannello.repaint();
						elencoC.add(cb);
						elencoP.add("0");
						bl.clear();
						deseleziona();
						sistemaMenu();
					}		  			 
				}
			}//derivata	  		   

			else if(e.getSource()==cronologia) {
				if(!elencoP.isEmpty()) 
					JOptionPane.showMessageDialog(null, elencoP, 
							"Cronologia", JOptionPane.PLAIN_MESSAGE);
				else JOptionPane.showMessageDialog(null, "Cronologia vuota!", 
						"ERRORE", JOptionPane.ERROR_MESSAGE);				    			   
			}//cronologia

			else if(e.getSource()==svuota) {
				if(elencoC.isEmpty() && elencoP.isEmpty()) 
					JOptionPane.showMessageDialog(null, "La cronologia e l'elenco sono vuoti!", 
						"ERRORE", JOptionPane.ERROR_MESSAGE);
				else {
					for(JCheckBox cb:elencoC) FrameGUI.this.pannello.remove(cb);  			   
					FrameGUI.this.pannello.revalidate();
					FrameGUI.this.pannello.repaint();
					elencoC.clear();
					elencoP.clear();
					sistemaMenu();
				}
			}//svuotaElenco

			else if(e.getSource()==info){
				JOptionPane.showMessageDialog( null, "Polinomi GUI\n" + 
					"Comandi>InserisciPolinomi - per inserire nell'elenco dei polinomi \n " + 
					"Comandi>RimuoviPolinomi - per rimuovere dall'elenco i polinomi selezionati\n" + 
					"Comandi>Valore - calcola il valore del polinomio selezionato\n" +
					"Comandi>Somma - calcola la somma tra i due polinomi selezionati\n" + 
					"Comandi>Moltiplicazione - calcola la moltiplicazione tra i due polinomi selezionati\n" + 
					"Comandi>Derivata - calcola la derivata del polinomio selezionato\n" +
					"Comandi>Cronologia - storico degli inserimenti, non risente delle rimozioni\n" +
					"Comandi>SvuotaCronologia - cancella tutti i polinomi dalla cronologia e dall'elenco\n",
					"About", JOptionPane.PLAIN_MESSAGE );
			}//info	
		}//actionPerformed	  	   
	}//AscoltatoreEventiAzione

	private void salva(String nomeFile) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(nomeFile));
		for(String s: elencoP) pw.println(s);
		pw.close();	
	}//salva

	private void ripristina(String nomeFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(nomeFile));
		String linea = null;
		LinkedList<String> tmp = new LinkedList<String>();		 
		boolean okLettura = true;		 
		for(;;){
			linea = br.readLine();
			if(linea==null) break; 			
			try{
				if(linea.matches(ePolinomio) && !linea.isEmpty()) 
					tmp.add(linea);
			}catch(Exception e){
				okLettura = false;
				break;
			}
		}
		br.close(); 		  
		if(okLettura){ 
			for(String polinomio: tmp) {
				JCheckBox cb = new JCheckBox(polinomio);
				FrameGUI.this.pannello.add(cb);
				elencoC.add(cb);
				elencoP.add(polinomio); 
			}
			sistemaMenu();
			FrameGUI.this.pannello.revalidate();
			FrameGUI.this.pannello.repaint();
		}
		else 
			throw new IOException();
	}//ripristina
	
	private void sistemaMenu() {
		if(elencoC.size()==0) {
			rimuoviPolinomi.setEnabled(false);
			rimuoviPolinomi.setBackground(bianco);
			valore.setEnabled(false);
			valore.setBackground(bianco);
			somma.setEnabled(false);
			somma.setBackground(bianco);
			sottrai.setEnabled(false);
			sottrai.setBackground(bianco);
			moltiplica.setEnabled(false);
			moltiplica.setBackground(bianco);
			derivata.setEnabled(false);
			derivata.setBackground(bianco);
			cronologia.setEnabled(false);
			cronologia.setBackground(bianco);
			svuota.setEnabled(false);
			svuota.setBackground(bianco);
		}
		else if(elencoC.size()==1){
			rimuoviPolinomi.setEnabled(true);
			rimuoviPolinomi.setBackground(tomato);
			valore.setEnabled(true);
			valore.setBackground(rosso);
			somma.setEnabled(false);
			somma.setBackground(bianco);
			sottrai.setEnabled(false);
			sottrai.setBackground(bianco);
			moltiplica.setEnabled(false);
			moltiplica.setBackground(bianco);
			derivata.setEnabled(true);
			derivata.setBackground(rosso);
			cronologia.setEnabled(true);
			cronologia.setBackground(tomato);
			svuota.setEnabled(true);
			svuota.setBackground(tomato);
		}
		else if(elencoC.size()>1){
			rimuoviPolinomi.setEnabled(true);
			rimuoviPolinomi.setBackground(tomato);
			valore.setEnabled(true);
			valore.setBackground(rosso);
			somma.setEnabled(true);
			somma.setBackground(rosso);
			sottrai.setEnabled(true);
			sottrai.setBackground(rosso);
			moltiplica.setEnabled(true);
			moltiplica.setBackground(rosso);
			derivata.setEnabled(true);
			derivata.setBackground(rosso);
			cronologia.setEnabled(true);
			cronologia.setBackground(tomato);
			svuota.setEnabled(true);
			svuota.setBackground(tomato);
		}
	}

	private void deseleziona() {
		for(JCheckBox cb: elencoC)
			cb.setSelected(false);
	}//deseleziona

	private int contaSelezionati() {
		int c = 0;
		for(JCheckBox cb: elencoC)
			if(cb.isSelected()) c++;
		return c;
	}//contaSelezionati

	private void putSelezionati() {
		for(JCheckBox cb: elencoC)
			if(cb.isSelected()) 
				bl.put(cb.getText());			 			
	}//pushSelezionati

	private Polinomio getSelezionato() {
		String polinomio = bl.get();
		if(polinomio==null) throw new RuntimeException();
		Polinomio ret = new PolinomioConcatenato();
		return ret.convertiPolinomio(polinomio);
	}//popSelezionati

	private boolean consensoUscita(){
		int option = JOptionPane.showConfirmDialog(null, "Vuoi salvare prima di uscire ?",
				"", JOptionPane.YES_NO_OPTION);
		return option==JOptionPane.NO_OPTION;
	}//consensoUscita
}//FrameGUI


