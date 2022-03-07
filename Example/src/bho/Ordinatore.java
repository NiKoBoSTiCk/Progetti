package bho;

import java.util.Scanner;
import java.util.StringTokenizer;

public class Ordinatore {
    public static void main(String[] args) {
        System.out.println("Inserisci un array di numeri separati dallo spazio.");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        StringTokenizer st = new StringTokenizer(input, " ");
        int dim = st.countTokens();
        int [] array = new int[dim];
        int posizione = 0;
        while(st.hasMoreTokens()) {
            String numero = st.nextToken();

            if(numero.matches("[0-9]*")){
                int n = Integer.parseInt(numero);
                array[posizione] = n;
                posizione++;
            }
        }
        //Insertion Sort
        for( int i=0; i < array.length-1; i++) {
            int pos=i+1;
            for( int j=i-1; j >= 0; j-- ) {
                if( array[j] > array[pos] ) {
                    int tmp=array[pos];
                    array[pos]=array[j];
                    array[j]=tmp;
                    pos=j;
                }
            }
        }
        for(int i=0; i<dim; i++){
            System.out.print(array[i] + " ");
        }
    }
}
