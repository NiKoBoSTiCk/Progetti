package poo.sudoku;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class SudokuGUI {
	public static void main(String []args){
		FrameGUI fc = new FrameGUI();
		fc.setVisible(true);	      
	}
}

class FrameGUI extends JFrame{
	private static final long serialVersionUID = 1L;
	private Imposta imp = null;
	private JTextField[][] matrice;
	private JTextField campoTesto;
	private JMenuItem apri, salva, salvaConNome, esci, info, imposta, impostaManualmente, risolvi, nuovo;
	private JButton prev, next;
	private Color rosso, tomato, bianco;
	private File fileDiSalvataggio = null;
	private String titolo = "Sudoku GUI";
	private int SolN = 0;
	private Sudoku board = null;
	private boolean eOK = false;

	public FrameGUI(){
		getContentPane().setLayout(null);
		setTitle(titolo);		
		setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );       
		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				if(consensoUscita()) System.exit(0);
			}
		} );
		setVisible(true);
		AscoltatoreEventiAzione listener=new AscoltatoreEventiAzione();		
		rosso = new Color(255, 0, 0);
		tomato = new Color(255, 99, 71);
		bianco = new Color(255, 255, 255);
		
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
		fileMenu.add(esci);//programma ritardato 
		JMenu commandMenu = new JMenu("Comandi");
		menuBar.add(commandMenu);       
		impostaManualmente = new JMenuItem("Imposta Manualmente");
		impostaManualmente.setBackground(tomato);
		impostaManualmente.addActionListener(listener);
		commandMenu.add(impostaManualmente);
		imposta = new JMenuItem("Imposta");
		imposta.setBackground(tomato);
		imposta.addActionListener(listener);
		commandMenu.add(imposta);
		risolvi = new JMenuItem("Risolvi");
		risolvi.setBackground(tomato);
		risolvi.setEnabled(false);
		risolvi.addActionListener(listener);
		commandMenu.add(risolvi);
		nuovo = new JMenuItem("Nuovo");
		nuovo.setBackground(tomato);
		nuovo.addActionListener(listener);
		commandMenu.add(nuovo);

		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		info = new JMenuItem("Info");
		info.setBackground(tomato);
		info.addActionListener(listener);
		helpMenu.add(info);

		prev = new JButton();
		prev.setText("Precedente");
		prev.setBounds(500, 210, 150, 30);
		prev.addActionListener(listener);
		prev.setEnabled(false);
		getContentPane().add(prev);		
		next = new JButton();
		next.setText("Successivo");	
		next.setBounds(500, 240, 150, 30);		
		next.addActionListener(listener);
		next.setEnabled(false);
		getContentPane().add(next);
		
		board = new Sudoku();
		matrice = new JTextField[9][9];
		for(int i=0; i<9; i++)
			for(int j=0; j<9; j++){
				campoTesto = new JTextField("");
				getContentPane().add(campoTesto);
				campoTesto.setBounds(20+50*j, 20+50*i, 50, 50);				
				campoTesto.setHorizontalAlignment(JTextField.CENTER);				
				matrice[i][j] = campoTesto;
			}
		for(int i=0; i<9; i++){
			JLabel numeroRiga = new JLabel(""+i);
			getContentPane().add(numeroRiga);
			numeroRiga.setBounds(8, 18+50*i, 40, 50);
		}
		for(int j=0; j<9; j++){
			JLabel numeroColonna = new JLabel(""+j);
			getContentPane().add(numeroColonna);
			numeroColonna.setBounds(40+50*j, 0, 10, 20);
		}
		pack();
		setLocation(700,200);
		setSize(700, 560); 
	}//costruttore

	private class Imposta extends JFrame implements ActionListener{	
		private static final long serialVersionUID = 1L;
		private JTextField riga, colonna, valore;
		private boolean rigaOK, colonnaOK, valoreOK;
		private int i, j, v;
		private JButton ok;
		private String titolo = "Imposta Manualmente";
		
		public Imposta(){
			setTitle(titolo);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);       
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e){
					Imposta.this.setVisible(false);
				}
			});			
			rigaOK = false; colonnaOK = false; valoreOK = false;   	     
			JPanel p = new JPanel();
			p.setLayout(new GridLayout(4,1));
			JPanel p1 = new JPanel();
			p1.setLayout(new GridLayout(1,1));
			p1.add(new JLabel("Riga", JLabel.CENTER));
			p1.add(riga = new JTextField(""));
			riga.setHorizontalAlignment(JTextField.CENTER);
			p.add(p1);
			JPanel p2 = new JPanel();
			p2.setLayout(new GridLayout(1,1) );
			p2.add(new JLabel("Colonna", JLabel.CENTER));
			p2.add(colonna = new JTextField(""));
			colonna.setHorizontalAlignment(JTextField.CENTER);
			p.add(p2);
			JPanel p3 = new JPanel();
			p3.setLayout(new GridLayout(1,1));
			p3.add(new JLabel("Valore", JLabel.CENTER));
			p3.add(valore = new JTextField("")); 
			valore.setHorizontalAlignment(JTextField.CENTER);
			p.add(p3);
			JPanel p4 = new JPanel();
			ok = new JButton("OK");
			ok.setBackground(rosso);
			p4.add(ok);
			p.add(p4);
			add(p, BorderLayout.NORTH);	 
			pack();

			riga.addActionListener(this);
			colonna.addActionListener(this);
			valore.addActionListener(this);
			ok.addActionListener(this); 		 

			setLocation(300,450);
			setSize(400,210);
		}//costruttore

		public void actionPerformed(ActionEvent e){
			if(e.getSource()==riga){
				Imposta.this.riga = riga;
				if(riga.getText().matches("[0-8]")) rigaOK = true;
				else {
					JOptionPane.showMessageDialog(null, "Impostazione non valida", "ERRORE", JOptionPane.ERROR_MESSAGE);
					riga.setText("");
				} 			
			}//riga

			if(e.getSource()==colonna){
				Imposta.this.colonna = colonna; 
				if(colonna.getText().matches("[0-8]")) colonnaOK = true;
				else {
					JOptionPane.showMessageDialog(null, "Impostazione non valida", "ERRORE", JOptionPane.ERROR_MESSAGE);
					colonna.setText("");
				}
			}//colonna

			if(e.getSource()==valore){
				Imposta.this.valore = valore; 
				if(valore.getText().matches("[1-9]")) valoreOK = true;
				else {
					JOptionPane.showMessageDialog(null, "Impostazione non valida", "ERRORE", JOptionPane.ERROR_MESSAGE);
					valore.setText("");
				}
			}//valore 

			else if(e.getSource()==ok){
				if(verificaUscita()){
					i = Integer.parseInt(riga.getText());
					j = Integer.parseInt(colonna.getText());
					v = Integer.parseInt(valore.getText());
					if(board.assegnabile(i,  j,  v)) {
						board.imposta(i, j, v);
						matrice[i][j].setText(String.valueOf(v));
						matrice[i][j].setBackground(tomato);
					}
					else JOptionPane.showMessageDialog(null, "Impostazione non valida", "ERRORE", JOptionPane.ERROR_MESSAGE);
					rigaOK = false;
					colonnaOK = false;
					valoreOK = false;  				 
					riga.setText("");
					colonna.setText("");
					valore.setText("");
					risolvi.setEnabled(true);
					risolvi.setBackground(rosso);
				}  			 
			}//ok
		}//actionPerformed

		private boolean verificaUscita(){
			return rigaOK && colonnaOK && valoreOK;
		}//verificaUscita

	}//Imposta

	private boolean consensoUscita(){
		int option = JOptionPane.showConfirmDialog(null, "Vuoi salvare prima di uscire ?",
				"", JOptionPane.YES_NO_OPTION);
		return option==JOptionPane.NO_OPTION;
	}//consensoUscita

	private class AscoltatoreEventiAzione implements ActionListener{
		public void actionPerformed(ActionEvent e){
			if(e.getSource()==impostaManualmente) {
				if(imp==null) imp = new Imposta();
				imp.setVisible(true);
			}//impostaManualmente

			else if(e.getSource()==risolvi) {
				bloccaMatrice();
				imposta.setEnabled(false);
				imposta.setBackground(bianco);
				impostaManualmente.setEnabled(false);
				impostaManualmente.setBackground(bianco);
				risolvi.setEnabled(false);
				risolvi.setBackground(bianco);
				board.risolvi();
				if(board.numeroSoluzioni()==0) JOptionPane.showMessageDialog(null,"Nessuna Soluzione!", 
						"ERRORE", JOptionPane.ERROR_MESSAGE); 
				else {
					setMatrice(board.getSoluzione(0));
					if(board.numeroSoluzioni()>1) {
						next.setEnabled(true);
						next.setBackground(rosso);
					}
				}
				SolN = 0;
			}//risolvi
			
			else if(e.getSource()==imposta) {
				try {	
					board = new Sudoku(convertiInMatriceImpostazione());
					impostaMatrice(board.getMatriceImpostata());	
					risolvi.setEnabled(true);
					risolvi.setBackground(tomato);
				}catch(Exception ex) {
					JOptionPane.showMessageDialog(null,"Impostazione non valida!", "ERRORE", JOptionPane.ERROR_MESSAGE); 
				}
			}//imposta

			else if(e.getSource()==nuovo) {
				svuotaMatrice();
				board = new Sudoku();
				setBottoni();
			}//nuovo

			else if(e.getSource()==prev) {
				SolN--;
				if(eOK) { 
					next.setEnabled(true);
					next.setBackground(rosso);
					setMatrice(board.getSoluzione(SolN));
					if(SolN==0) {
						prev.setEnabled(false);
						prev.setBackground(bianco);
						next.setEnabled(true); 
						next.setBackground(rosso);
					}
				}
			}//prev

			else if(e.getSource()==next) {
				SolN++;
				if(SolN<board.numeroSoluzioni()) {
					eOK = true; 
					prev.setEnabled(true); 
					prev.setBackground(rosso);
					setMatrice(board.getSoluzione(SolN));
				}	
				if(SolN==board.numeroSoluzioni()-1) {
					next.setEnabled(false);
					next.setBackground(bianco);
				}
			}//next

			else if(e.getSource()==apri){
				board = new Sudoku();
				JFileChooser chooser = new JFileChooser();
				if(chooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
					if(!chooser.getSelectedFile().exists())
						JOptionPane.showMessageDialog(null,"File inesistente.", "ERRORE", JOptionPane.ERROR_MESSAGE); 						
					else{	
						fileDiSalvataggio = chooser.getSelectedFile();
						setTitle(titolo + " - " + fileDiSalvataggio.getName());
						try{
							board.ripristina(fileDiSalvataggio.getAbsolutePath());
							impostaMatrice(board.getMatriceImpostata());
							setBottoni();							
							sbloccaMatrice();
						}catch(IOException ex){
							JOptionPane.showMessageDialog(null, "File malformato!", "ERRORE", JOptionPane.ERROR_MESSAGE);
							svuotaMatrice();
						}
					}
				}  
				else JOptionPane.showMessageDialog(null, "Apertura annullata", "", JOptionPane.PLAIN_MESSAGE);
			}//apri

			else if(e.getSource()==salva){
				JFileChooser chooser = new JFileChooser();
				try{
					if(fileDiSalvataggio!=null){
						int ans = JOptionPane.showConfirmDialog(null,"Sovrascrivere " + fileDiSalvataggio.getAbsolutePath() + " ?");
						if(ans==0) 
							board.salva(fileDiSalvataggio.getAbsolutePath());
						else JOptionPane.showMessageDialog(null,"Salvataggio annullato", "", JOptionPane.PLAIN_MESSAGE);
						return;
					}
					if(chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION){
						fileDiSalvataggio = chooser.getSelectedFile();
						setTitle(titolo + " - "+fileDiSalvataggio.getName());
					}
					if(fileDiSalvataggio!=null) 
						board.salva(fileDiSalvataggio.getAbsolutePath());
					else JOptionPane.showMessageDialog(null,"Salvataggio annullato", "", JOptionPane.PLAIN_MESSAGE);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}//salva

			else if(e.getSource()==salvaConNome){
				JFileChooser chooser=new JFileChooser();
				try{
					if(chooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION ){
						fileDiSalvataggio = chooser.getSelectedFile();
						setTitle(titolo+" - "+fileDiSalvataggio.getName());
					}
					if(fileDiSalvataggio!=null)
						board.salva(fileDiSalvataggio.getAbsolutePath());					
					else
						JOptionPane.showMessageDialog(null,"Salvataggio annullato", "", JOptionPane.PLAIN_MESSAGE);
				}catch(Exception ex){
					ex.printStackTrace();
				}  			   
			}//salvaConNome

			else if(e.getSource()==esci){
				if(consensoUscita()) System.exit(0);
			}//esci
			
			else if(e.getSource()==info){
				JOptionPane.showMessageDialog( null, "Sudoku GUI\n" + 
					"Comandi>Imposta -imposta i vincoli di gioco\n "+ 
					"Comandi>Risolvi -risolvi il gioco\n" + 
					"Comandi>Nuovo   -inizia un nuovo gioco\n",								
					"About", JOptionPane.PLAIN_MESSAGE );
			}//info
		}//actionPerformed
	}//AscoltatoreEventiAzione

	private void setMatrice(String[][] m){
		for(int i=0;i<9;i++)
			for(int j=0;j<9;j++)
				matrice[i][j].setText(m[i][j]);
	}//setMatrice
	
	private void setBottoni() {
		imposta.setEnabled(true);
		imposta.setBackground(tomato);
		impostaManualmente.setEnabled(true);
		impostaManualmente.setBackground(tomato);
		risolvi.setEnabled(false);
		risolvi.setBackground(tomato);
		prev.setEnabled(false); 
		prev.setBackground(bianco);
		next.setEnabled(false); 
		next.setBackground(bianco);
	}//setBot
	
	private int[][] getMatrice(){
		int[][] ret = new int[9][9];
		for(int i=0; i<9; i++)
			for(int j=0; j<9; j++) {
				if(matrice[i][j].getText().equals("")) ret[i][j] = 0;
				else ret[i][j] = Integer.parseInt(matrice[i][j].getText());
			}
		return ret;
	}//getMatrice
	
	private void bloccaMatrice() {
		for(int i=0; i<9; i++)
			for(int j=0; j<9; j++)
				matrice[i][j].setEditable(false);
	}//bloccaMatrice
	
	private void sbloccaMatrice() {
		for(int i=0; i<9; i++)
			for(int j=0; j<9; j++)
				matrice[i][j].setEditable(true);
	}//bloccaMatrice
	
	private int[][] convertiInMatriceImpostazione(){
		int[][] m = getMatrice();
		int[][] ret = new int[contaElementi()][3];
		int z = 0;
		for(int i=0; i<9; i++)
			for(int j=0; j<9; j++)
				if(m[i][j]!=0) {
					ret[z][0] = i; 
					ret[z][1] = j;
					ret[z][2] = m[i][j];
					z++;
				}	
		return ret;
	}//convertiInMatriceImpostazione
	
	private int contaElementi(){
		int ret = 0;
		for(int i=0; i<9; i++)
			for(int j=0; j<9; j++)
				if(!matrice[i][j].getText().equals(""))
					ret++;
		return ret;
	}//contaElementi
	
	private void svuotaMatrice() {
		for(int i=0; i<9; i++)
			for(int j=0; j<9; j++) {
				matrice[i][j].setText("");
				matrice[i][j].setEditable(true);
				matrice[i][j].setBackground(bianco);
			}
	}//svuotaMatrice
	
	private void impostaMatrice(int[][] m) {
		for(int i=0; i<9; i++)
			for(int j=0; j<9; j++) {
				if(m[i][j]!=0) {
					matrice[i][j].setText(String.valueOf(m[i][j]));
					matrice[i][j].setBackground(tomato);
				}
				else {
					matrice[i][j].setText("");	
					matrice[i][j].setBackground(bianco);
				}
			}
	}//impostaMatrice
}//SudokuGUI



