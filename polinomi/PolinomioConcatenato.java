package poo.polinomi;
import java.util.*;

public class PolinomioConcatenato extends PolinomioAstratto {
	private static class Nodo{
		Monomio info;
		Nodo next;
	}//Nodo	
	private Nodo testa = null;

	public PolinomioConcatenato(){};
	public PolinomioConcatenato(Polinomio p) {
		for(Monomio m: p) add(m);
	}
	
	@Override
	public PolinomioConcatenato crea() { 
		return new PolinomioConcatenato(); 
	}//create

 	public void add(Monomio m) {
		if(m.getCoeff()==0) return;
		Nodo cor = testa, pre = null;
		while(cor!=null && cor.info.compareTo(m)<0) {
			pre = cor; 
			cor = cor.next;
		}
		if(cor!=null && cor.info.equals(m)) {
			cor.info = cor.info.add(m);
			if(cor.info.getCoeff()==0) {
				if(cor==testa) testa = cor.next;
				else pre.next = cor.next;
			}
		}
		else {
			Nodo nuovo = new Nodo();
			nuovo.info = m; nuovo.next = cor;
			if(cor==testa) testa = nuovo;
			else pre.next = nuovo;
		}
	}//add
 	
	public Iterator<Monomio> iterator() {
		return new Iteratore();
	}//iterator

	protected PolinomioConcatenato create() {
		return new PolinomioConcatenato();
	}//create

	private class Iteratore implements Iterator<Monomio> {
		Nodo pre = null, cor = null;
		public boolean hasNext() {
			if(cor==null) return testa!=null;
			return cor.next!=null;
		}//hasNext
		
		public Monomio next() {
			if(!hasNext()) throw new NoSuchElementException();
			if(cor==null) cor = testa;
			else {
				pre = cor; 
				cor = cor.next; 
			}
			return cor.info;
		}//next
		
		public void remove() {
			if(cor==pre) throw new IllegalStateException();
			if(cor==testa) testa = testa.next;
			else pre.next = cor.next;
			cor = pre;
		}//remove
	}//Iteratore
}//PolinomioConcatenato
