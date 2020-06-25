package poo.polinomi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class PolinomioAL extends PolinomioAstratto {
	private ArrayList<Monomio> lista = new ArrayList<>();
	
	public PolinomioAL() {};	
	public PolinomioAL(Polinomio p) {
		for(Monomio m: p) add(m);
	}
	
	@Override
	public int size() { 
		return lista.size(); 
	}//size

	@Override
	public void add(Monomio m) {
		if(m.getCoeff()==0) return;
		ListIterator<Monomio> lit = lista.listIterator();
		boolean flag = false;
		while(lit.hasNext() && !flag) {
			Monomio m1=lit.next();
			if(m.equals(m1)) {
				Monomio m2 = m.add(m1);
				if(m2.getCoeff()!=0) lit.set(m2);				
				else lit.remove();
				flag = true;
			}
			else if(m1.compareTo(m)>0) {
				lit.previous();
				lit.add(m);
				flag = true;
			}
		}//while
		if(!flag) lit.add(m);
	}//add

	@Override
	public Polinomio crea() {
		return new PolinomioAL();
	}//crea

	@Override
	public Iterator<Monomio> iterator() {
		return lista.iterator();
	}//iterator
}//PolinomioAL
