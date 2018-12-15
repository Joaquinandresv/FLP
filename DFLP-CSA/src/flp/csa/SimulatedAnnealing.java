package flp.csa;

import java.util.Random;
import java.lang.Math;
public class SimulatedAnnealing {
	
	static double INITIAL_TEMPERATURE = 10000.0;
	static double FINAL_TEMPERATURE = 0.001;
	static int N_1;
	static int N_2;
	static double COOLING_FACTOR = 0.05;
	static int steps = 1;
	static boolean finish = false;
	static Model current_solution;
	static Model sol_prima;
	static Model sol_prima_prima;
	static Model global_optimal_solution;
	int i;
	int k;
	private double Ti;
	private double Tf; 
	private double DTi;
	
	public SimulatedAnnealing(Model modelo) {
		while(finish == false) {
			switch (steps) {
				case 1:
					System.out.println("Random initialization");
					Ti = INITIAL_TEMPERATURE;
					Tf = FINAL_TEMPERATURE;
					DTi = COOLING_FACTOR;
					i = 1;
					k = 1;
					modelo.iniciarRandom();
					N_1 = 5;//modelo.getJ();
					N_2 = 8;//5*modelo.getI();
					current_solution = modelo;
					//System.out.printf("\nFO inicial: %f",current_solution.funcion_objetivo());
					//Ti=current_solution.funcion_objetivo();
					global_optimal_solution = current_solution;
					steps=2;
					break;
				case 2:
					if(current_solution.checkFeasibilities() == true) {
						steps=3;
						break;
						
					}else {
						steps=1;
						break;
					}
				case 3:
					ELSA elsa = new ELSA(modelo);
					sol_prima = elsa.getSol();
					steps=4;
					break;
				case 4:
					/* PASO 4.1 **/
					k=1;
					current_solution = sol_prima;
					while(k<=N_2){ /* VERIFICADOR DEPASO 4.4 */
						/* PASO 4.2 **/
						ILSA ilsa = new ILSA(current_solution);
						sol_prima_prima = ilsa.getSol();
						/* PASO 4.3 **/
						if(sol_prima_prima.funcion_objetivo()<sol_prima.funcion_objetivo()) {
							sol_prima = sol_prima_prima;
						}else {
							double phy = Math.random()*1;
							if(phy < metropolis_condition(sol_prima_prima,sol_prima,Ti)) {
								sol_prima = sol_prima_prima;
							}
						}
						k++;
					}
					/* PASO 4.5 */
					steps=5;
					break;
				case 5:
					if(sol_prima.funcion_objetivo()<global_optimal_solution.funcion_objetivo())global_optimal_solution = sol_prima;
					steps=6;
					break;
				case 6:
					if(sol_prima.funcion_objetivo()<current_solution.funcion_objetivo())current_solution = sol_prima;
					steps=7;
					break;
				case 7:
					  double phy = Math.random()*1;
                      if(phy < metropolis_condition(sol_prima,current_solution,Ti)) {
                          current_solution= sol_prima;
                      }
                    steps=8;
					break;
					
				case 8:
					/* NUEVA ITERACIÓN */
					i++;
					if(i<=N_1)steps=3;
					else steps=9;
					break;
				case 9:
					Ti = Ti*DTi;
					steps=10;
					break;
				case 10:
					if(Ti>=Tf)
					{
						System.out.printf("\nTemperatura -> %f",Ti);
						i=1;
						steps=3;
					}else {
						finish = true;
					}	
					break;
				default:
					finish = true;
					break;
				}
		}
	}

	private double metropolis_condition(Model sol_prima, Model current_solution, double Ti) {
       double metropolis_value = Math.exp((-(sol_prima.funcion_objetivo()-current_solution.funcion_objetivo())/Ti));
        return metropolis_value;
    }
	
	public void mostrarSolucionFinal() {
		System.out.printf("\nOptimal solution: %f",global_optimal_solution.funcion_objetivo());
	}

}
