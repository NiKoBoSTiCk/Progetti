package poo.polinomi;

import java.util.StringTokenizer;

public class Monomio implements Comparable<Monomio> {
	private final int coeff, grado;
	
	public Monomio(int coeff, int grado){
		if(grado<0) throw new RuntimeException("Grado negativo");
		this.coeff = coeff; this.grado = grado;
	}//Monomio
	
	public Monomio(Monomio m){
		coeff = m.coeff; grado = m.grado;
	}//Monomio
	
	public int getCoeff(){
		return coeff; 
	}//getCoeff
	
	public int getGrado(){
		return grado; 
	}//getGrado
	
	public Monomio add(Monomio m) {
		if(!this.equals(m)) 
			throw new RuntimeException("Monomi non simili");
		return new Monomio(coeff+m.coeff, grado);
	}//add
	
	public Monomio mul(Monomio m) {
		return new Monomio(coeff*m.coeff, grado+m.grado);
	}//mul
	
	public Monomio mul(int s) {
		return new Monomio(coeff*s, grado);
	}//mul
	
	public Monomio convertiMonomio(String mon) {
		if(mon.matches("\\d+")) //caso a
			return new Monomio(Integer.parseInt(mon), 0);		
		else if(mon.equals("x")) //caso x
			return new Monomio(1, 1);		
		else if(mon.matches("\\d+x")) { //caso ax
			StringTokenizer s = new StringTokenizer(mon,"x");
			int coeff = Integer.parseInt(s.nextToken());
			return new Monomio(coeff, 1);
		}
		else if(mon.matches("x\\^\\d+")) { //caso x^b
			StringTokenizer s = new StringTokenizer(mon,"x^");
			int grad = Integer.parseInt(s.nextToken());
			return new Monomio(1, grad);
		}
		else if(mon.matches("\\d+x\\^\\d+")) { //caso ax^b
			StringTokenizer s = new StringTokenizer(mon,"x^");
			int coeff = Integer.parseInt(s.nextToken());
			int grad = Integer.parseInt(s.nextToken());
			return new Monomio(coeff, grad);
		}
		throw new RuntimeException();
	}//convertiMonomio
	
	public int compareTo(Monomio m) {
		if(grado>m.grado) return -1;
		if(grado<m.grado) return +1;
		return 0;
	}//compareTo
	
	public boolean equals (Object o) { //uguaglianza --> similitudine
		if(!( o instanceof Monomio)) return false;
		if(o==this) return true;
		Monomio m = (Monomio)o;
		return this.grado==m.grado;
	}//equals
	
	public int hashCode() {
		return grado;
	}//hashCode
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if(coeff<0) sb.append('-');
		if(Math.abs(coeff)!=1 || grado==0)
			sb.append(Math.abs(coeff));
		if(coeff!=0 && grado>0) sb.append('x');
		if(coeff!=0 && grado>1){
			sb.append('^');
			sb.append( grado);
		}
		return sb.toString();
	}//toString
}//Monomio