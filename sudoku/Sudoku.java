package poo.sudoku;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Sudoku {
	private int[][] matrice, matriceImp;
	private List<Impostazione> impostazioni;
	private List<Soluzione> soluzioni;
	
	public Sudoku() {
		matrice = new int[9][9];
		matriceImp = new int[9][9];
		impostazioni = new LinkedList<>();
		soluzioni = new LinkedList<>();
	}//costruttore default
	
	public Sudoku(int[][] imp) {
		if(imp.length>81) throw new IllegalArgumentException("Impostazioni non valide.");
		matrice = new int[9][9];
		matriceImp = new int[9][9];
		impostazioni = new LinkedList<>();
		soluzioni = new LinkedList<>();
		for(int i=0; i<imp.length; i++) {
			if(imp[i].length!=3) throw new IllegalArgumentException("Impostazioni non valide.");
			int riga = imp[i][0];
			int colonna = imp[i][1];
			int valore = imp[i][2];
			imposta(riga, colonna, valore);
		}	
	}//costruttore
	
	public void imposta(int i, int j, int v) {	
		if(!assegnabile(i, j, v)) 
			throw new IllegalArgumentException("Impostazione " +i+ ", " +j+ ", " +v+ " non valida." );
		matrice[i][j] = v;
		matriceImp[i][j] = v;
		impostazioni.add(new Impostazione(i, j, v));
	}//imposta
	
	public void risolvi() {
		colloca(0, 0);
	}//risolvi
	
	private void colloca(int riga, int colonna) { 
		if(matriceImp[riga][colonna]==0 & numeroSoluzioni()<6) {
			for(int v=1; v<=9; v++) 
				if(assegnabile(riga, colonna, v)) { 
					assegna(riga, colonna, v);
					if(riga==8 && colonna==8) scriviSoluzione();
					else if(riga<8 && colonna==8) colloca(riga+1, 0);
					else colloca(riga, colonna+1);
					deassegna(riga, colonna);
				}
		}
		else if(numeroSoluzioni()<6) {
		    if(riga==8 && colonna==8) scriviSoluzione();
		    else if(riga<8 && colonna==8) colloca(riga+1, 0);
			else colloca(riga, colonna+1);
		}		
	}//colloca
	
	public boolean assegnabile(int i, int j, int v) {
		if(v<1 || v>9) return false;
		if(i<0 || j<0 || i>8 || j>8) return false;
		if(matriceImp[i][j]!=0) return false;
		if(!controllaSottomatrice(i, j, v) || !controllaRigheColonne(i, j, v))
			return false;		
		return true;
	}//assegnabile
	
	private boolean controllaRigheColonne(int riga, int colonna, int v) {
		for(int k=0; k<9; k++)
			if(matrice[riga][k]==v || matrice[k][colonna]==v) 
				return false;
		return true;
	}//controllaRigheColonne
	
	private boolean controllaSottomatrice(int riga, int colonna, int v) {
		int r = riga-riga%3;
		int c = colonna-colonna%3;
		for(int i=r; i<r+3; i++)
			for(int j=c; j<c+3; j++)
				if(matrice[i][j]==v)
					return false;
		return true;		
	}//controllaSottomatrice
	
	private void assegna(int i, int j, int v) { 
		if(matriceImp[i][j]!=0) throw new RuntimeException();
		matrice[i][j] = v;
	}//assegna
	
	private void deassegna(int i, int j) {
		if(matriceImp[i][j]!=0) throw new RuntimeException();
		matrice[i][j] = 0;
	}//deassegna
	
	private void scriviSoluzione() {
		String[][] sol = new String[9][9];
		for(int i=0; i<9; i++) 
			for(int j=0; j<9; j++)  
				sol[i][j] = String.valueOf(matrice[i][j]);
		boolean eOK = true;
		for(Soluzione s:soluzioni)
			if(s.getSoluzione().equals(sol)) eOK = false;
		if(eOK) soluzioni.add(new Soluzione(sol));		
	}//scriviSoluzione
	
	public int numeroSoluzioni() {
		return soluzioni.size();
	}//numeroSoluzioni
	
	public String[][] getSoluzione(int n) {
		if(n<0 || n>soluzioni.size()) throw new IllegalArgumentException();
		return soluzioni.get(n).getSoluzione();	
	}//getSoluzione
	
	public int[][] getImpostazioni() {
		int[][] imp = new int[impostazioni.size()][3];
		for(int j=0; j<impostazioni.size(); j++) 
			imp[j] = impostazioni.get(j).getImpostazione();		
		return imp;	
	}//getImpostazioni
	
	public int[][] getMatriceImpostata() {
		int[][] ret = new int[9][9];
		for(int i=0; i<9; i++)
			for(int j=0; j<9; j++)
				ret[i][j] = matriceImp[i][j];
		return ret;
	}//getMatriceImpostata
	
	public String toString() {
		String s = "";
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) 
				s+=(" " + matrice[i][j]);		
			s+=" \n";
		}		
		return s;
	}//toString
	
	public void salva(String nomeFile) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(nomeFile));
		int[][] impostazioni = getImpostazioni();
		for(int i=0; i<impostazioni.length; i++){
			pw.print(impostazioni[i][0] + " ");	
			pw.print(impostazioni[i][1] + " ");
			pw.print(impostazioni[i][2] + "\n");
		}		
		pw.close();	
	}//salva

	public void ripristina(String nomeFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(nomeFile));
		String linea = null;
		LinkedList<String> tmp = new LinkedList<String>();		 
		boolean okLettura = true;		 
		for(;;){
			linea = br.readLine();
			if(linea==null) break; 			
			try{
				if(linea.matches("[0-8] [0-8] [1-9]") && !linea.isEmpty())
					tmp.add(linea);
				else throw new IOException();
			}catch(Exception e){
				okLettura = false;
				break;
			}
		}
		br.close(); 		  
		if(okLettura){ 
			for(String imp: tmp) {
				StringTokenizer st = new StringTokenizer(imp, " ");								
				int riga = Integer.parseInt(st.nextToken());
				int colonna = Integer.parseInt(st.nextToken());
				int valore = Integer.parseInt(st.nextToken());
				if(assegnabile(riga, colonna, valore))
					imposta(riga, colonna, valore);
				else throw new IOException();	
			}
		}
		else throw new IOException();
	}//ripristina
	
	private class Soluzione{
		private String[][] sol;
		public Soluzione(String[][] sol) {
			this.sol=sol;
		}
		public String[][] getSoluzione() {
			return sol;
		}//getSoluzione
		public boolean equals(Object o) {
			if(!(o instanceof Sudoku))
				return false;
			if(o==this) return true;
			Soluzione s = (Soluzione)o;
			for(int i=0; i<9; i++)
				for(int j=0; j<9; j++)
					if(sol[i][j]!=s.getSoluzione()[i][j])
						return false;
			return true;
		}//equals
	}//Soluzione
	
	private class Impostazione {
		private int[] imp;
		public Impostazione(int i, int j, int v) {
			imp = new int[3];
			imp[0]=i;
			imp[1]=j;
			imp[2]=v;
		}
		public int[] getImpostazione() {
			return imp;
		}//getImpostazione
	}//Impostazione
}//Sudoku
