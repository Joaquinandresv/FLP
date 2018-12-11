package flp.csa;

import java.util.Random;

public class SimulatedAnnealing {
	
	static double INITIAL_TEMPERATURE = 25000.0;
	static double FINAL_TEMPERATURE = 50;
	static int N_1 = 1;
	static int N_2 = 1;
	static double COOLING_STEPS = 50000;
	static double COOLING_FACTOR = 0.999997;
	private int solution_elsa = 0;
	private double solution_ilsa = 0;
	static int steps = 1;
	static boolean finish = false;
	static Model current_solution;
	static Model best_solution;
	
	public SimulatedAnnealing(Model modelo) {
		while(finish == false) {
			switch (steps) {
				case 1:
					double Ti = INITIAL_TEMPERATURE;
					double Tf = FINAL_TEMPERATURE;
					double DTi = COOLING_FACTOR;
					int i = N_1;
					int k = N_2;
					modelo.iniciarRandom();
					current_solution = modelo;
					best_solution = current_solution;
					steps++;
					break;
				case 2:
					if(checkFeasibilities(current_solution) == true) {
						steps++;
					}else {
						steps--;
					}
				}
		}
		ELSA elsa = new ELSA(); //External layer subalgorithm
		//elsa.operator1();
		//elsa.operator2();
		//elsa.operator3();
		ILSA(modelo); //Internal layer subalgorithm
	}

	private boolean checkFeasibilities(Model current_solution) {
		//RESTRICCIONES (3) Y (5)
		return false;
	}
	
	private void ELSA(Model modelo) {
	}


	private void ILSA(Model modelo) {
	}

}
