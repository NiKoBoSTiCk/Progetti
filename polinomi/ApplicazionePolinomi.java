package poo.polinomi;

import java.util.LinkedList;
import java.util.Scanner;

public class ApplicazionePolinomi {
	private static String ePolinomio = "([\\-\\+]?(\\d*x(\\^\\d+)?|\\d+))([\\+\\-](\\d*x(\\^\\d+)?|\\d+))*"; 
	private static Scanner sc;
	private static Polinomio pol1;
	private static Polinomio pol2;
	private static LinkedList<Polinomio> storico = new LinkedList<>();
	
	public static void main(String...args) {
	    System.out.println("Applicazione Polinomi");
	    System.out.println();
		System.out.println("Scegli tra: "
				+ "1-PolinomioConcatenato 2-PolinomioSet 3-PolinomioLL 4-PolinomioAL");
		sc = new Scanner(System.in);
		int tipo = 0;
		while(tipo<1 || tipo>4){
			tipo = sc.nextInt();
			if(tipo<1 || tipo>4)
				System.out.print("Digitare un numero da 1 a 4");
		}
		switch(tipo){
			case 1: 
				pol1 = new PolinomioConcatenato();
				pol2 = new PolinomioConcatenato(); break;
			case 2:
				pol1 = new PolinomioSet(); 
				pol2 = new PolinomioSet(); break;
			case 3:
				pol1 = new PolinomioLL();
				pol2 = new PolinomioLL(); break;
			default:
				pol1 = new PolinomioAL();
				pol2 = new PolinomioAL();
		}
    	for(;;) {
    		comandi();
    		System.out.print(">");
			String opzione = sc.nextLine();
			switch(opzione) { 
				case "0": somma();break;
				case "1": sottrai();break;
				case "2": moltiplicazione();break;
				case "3": derivata();break;
				case "4": valore();break;
				case "5": storico();break;
				case "quit": sc.close();esci();
				default: System.out.println("Scelta non valida");
			}	
    	}//ciclo
	}//main
	
	private static void comandi() {
		System.out.println("Scegli l'operazione da eseguire");
		System.out.println("0-somma");
		System.out.println("1-sottrai");
		System.out.println("2-moltiplicazione");
		System.out.println("3-derivata");
		System.out.println("4-valore");
		System.out.println("5-storico");
		System.out.println("quit");
	}//comandi
	
	private static void aggiungiPolinomio1() {
		System.out.println("Inserisci un Polinomio");
		for(;;) {
			System.out.print(">");
			String polinomio1 = sc.nextLine();
			try {
				if(!polinomio1.matches(ePolinomio)) throw new RuntimeException();
				pol1 = pol1.convertiPolinomio(polinomio1);
				storico.add(pol1);
				break;
			}catch(Exception e) {
				System.out.println("Polinomio non valido, riprovare.");
			}
		}
	}//aggiungiPolinomio1
	
	private static void aggiungiPolinomio2() {
		System.out.println("Inserisci il secondo Polinomio");	
		for(;;) {
			System.out.print(">");
			String polinomio2 = sc.nextLine();
			try {
				if(!polinomio2.matches(ePolinomio)) throw new RuntimeException();
				pol2 = pol2.convertiPolinomio(polinomio2);
				storico.add(pol2);
				break;
			}catch(Exception e) {
				System.out.println("Polinomio non valido, riprovare.");
			}
		}
	}//aggiungiPolinomio2
		  	
	private static void somma() {
		aggiungiPolinomio1();		
		aggiungiPolinomio2();
		System.out.println("= " + pol1.add(pol2));		
		storico.add(pol1.add(pol2));		
		prosegui();	
	}//somma
	
	private static void sottrai() {
		aggiungiPolinomio1();		
		aggiungiPolinomio2();
		Polinomio t = new PolinomioConcatenato();
		for(Monomio m:pol2) t.add(new Monomio(m.getCoeff()*-1,m.getGrado()));
		storico.add(t);
		System.out.println("= " + pol1.add(t));		
		storico.add(pol1.add(t));		
		prosegui();	
	}//sottrai
			
	private static void moltiplicazione() {		
		aggiungiPolinomio1();		
		aggiungiPolinomio2();		
		System.out.println("= " + pol1.mul(pol2));			
		storico.add(pol1.mul(pol2));		
		prosegui();		
	}//moltiplicazione
	
	private static void derivata() {		
		aggiungiPolinomio1();
		System.out.println("= " + pol1.derivata());
		storico.add(pol1.derivata());
		prosegui();
	}//derivata
	
	private static void valore() {
		aggiungiPolinomio1();
		double x = 0;
		for(;;) {
			System.out.println("Inserisci il valore di x");
			System.out.print(">");
			try {
				x = Double.parseDouble(sc.nextLine());
				break;
			}catch(Exception e) {
				System.out.println("Valore non valido");
			}
		}
		System.out.println("= " + pol1.valore(x));
		prosegui();
	}//valore
	
	private static void storico() {
		System.out.println(storico);
		prosegui();
	}//storico
	
	private static void prosegui() {
		for(;;) {
			System.out.println("Vuoi continuare? [y/n]");
			System.out.print(">");
			String scelta = sc.nextLine();
			if(scelta.equals("y")) break;
			if(scelta.equals("n")) {
				sc.close();
				esci();	
			}
			System.out.println("Scelta non valida. Riprovare");		
		}		
	}//prosegui
	
	private static void esci() {
		System.out.println("Bye.");
		System.exit(0);
	}//esci
}//ApplicazionePolinomi
