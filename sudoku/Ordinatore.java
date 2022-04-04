package poo.sudoku;

import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Ordinatore {
    public static void main(String args) {
        System.out.println("Inserisci un array di numeri separati dallo spazio.");

        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        StringTokenizer st = new StringTokenizer(input, " ");
        int dim = st.countTokens();
        int [] array = new int[dim + 1];


        while(st.hasMoreTokens()) {
            String numero = st.nextToken();

            if(numero.matches("[0-9]*")){
                int n = Integer.parseInt(numero);

            }
        }

        Arrays.sort(array);

        for(int i=0; i<dim + 1; i++){
            System.out.print(array[i] + " ");
        }

    }
}
