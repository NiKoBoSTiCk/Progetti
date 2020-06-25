package poo.polinomi;
import java.util.*;

public class PolinomioSet extends PolinomioAstratto {
	private Set<Monomio> contenuto = new TreeSet<Monomio>();
	
	public PolinomioSet() {};
	public PolinomioSet(Polinomio p) {
		Iterator<Monomio> it = p.iterator();
		while(it.hasNext()) {
			Monomio m = (Monomio)it.next();
			contenuto.add(m);
		}
	}
	
	public int size() {
		return contenuto.size(); 
	}//size
	
	public PolinomioSet crea() {
		return new PolinomioSet();
	}//create
	
	public Iterator<Monomio> iterator() {
		return contenuto.iterator();
	}//iterator
	
	public void add(Monomio m) {
		if(m.getCoeff()==0) return;
		if(!contenuto.contains(m)) contenuto.add(m);
		else {
			Iterator<Monomio> it = contenuto.iterator();
			Monomio m1 = null;
			while(it.hasNext()){
				m1 = it.next();
				if(m1.equals(m)) {
					it.remove(); break; 
				}
			}
			m1 = m1.add(m);
			if(m1.getCoeff()!=0) contenuto.add(m1);
		}
	}//add
}//PolinomioSet
