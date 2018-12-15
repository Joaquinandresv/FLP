package flp.csa;

import java.util.Scanner;

public class Solution {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el nombre del archivo.");
		String file = scanner.next();
		Model modelo = new Model(file);
		SimulatedAnnealing sa = new SimulatedAnnealing(modelo);
		sa.mostrarSolucionFinal();
	}

}
