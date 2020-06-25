package poo.polinomi;
import java.util.*;

public abstract class PolinomioAstratto implements Polinomio{	
	public Polinomio convertiPolinomio(String polinomio) {
		Polinomio p = crea();
		StringTokenizer st = new StringTokenizer(polinomio,"+-",true);
		while(st.hasMoreTokens()) {
			Monomio m = new Monomio(0, 1);
			String t = st.nextToken();
			if(!t.equals("+") && !t.equals("-")) {
				m = m.convertiMonomio(t);
				p.add(m);
			}
			else if(t.equals("-")) {
				String monomio = st.nextToken();
				m = m.convertiMonomio(monomio);
				p.add(new Monomio(m.getCoeff()*-1, m.getGrado()));
			}
			else if(t.equals("+")) {
				String monomio = st.nextToken();
				m = m.convertiMonomio(monomio);
				p.add(m);
			}
		}
		return p;
	}//convertiPolinomio

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Monomio> it = iterator();
		boolean flag = true;
		while(it.hasNext()) {
			Monomio m=it.next();
			if(m.getCoeff()>0 && !flag) sb.append('+');
			sb.append(m);
			if(flag) flag = !flag;
		}
		return sb.toString();
	}//toString
	
	public boolean equals(Object o) {
		if(!(o instanceof Polinomio)) return false;
		if(o==this) return true;
		Polinomio p = (Polinomio)o;
		if(this.size()!=p.size()) return false;
		Iterator<Monomio> it=this.iterator();
		for(Monomio m: p) {
			Monomio q=it.next();
			if(m.getCoeff()!=q.getCoeff() ||
			   m.getGrado()!=q.getGrado()) return false;
		}
		return true;
	}//equals
	
	public int hashCode() {
		int p = 17, hash = 0;
		for(Monomio m: this) {
			int hc = (String.valueOf(m.getCoeff())+String.valueOf(m.getGrado())).hashCode();
			hash = hash*p+hc;
		}
		return hash;
	}//hashCode	
}//PolinimoAstratto