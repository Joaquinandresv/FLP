package flp.csa;

import java.util.Random;
import java.lang.Math;
public class SimulatedAnnealing {
	
	static double INITIAL_TEMPERATURE = 10000.0;
	static double FINAL_TEMPERATURE = 0.001;
	static int N_1;
	static int N_2;
	static double COOLING_STEPS = 50000;
	static double COOLING_FACTOR = 0.95;
	static int steps = 1;
	static boolean finish = false;
	static Model current_solution;
	static Model best_solution;
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
					System.out.println("PASO 1: Random initialization");
					//Ti = INITIAL_TEMPERATURE;
					Tf = FINAL_TEMPERATURE;
					DTi = COOLING_FACTOR;
					i = 1;
					k = 1;
					modelo.iniciarRandom();
					N_1 = modelo.getJ();
					N_2 = 5*modelo.getI();
					current_solution = modelo;
					System.out.printf("FO inicial: %f",current_solution.funcion_objetivo());
					Ti=current_solution.funcion_objetivo();
					global_optimal_solution = current_solution;
					steps=2;
					break;
				case 2:
					System.out.println("PASO 2: Check feasibilities");
					if(checkFeasibilities(current_solution) == true) {
						steps=3;
						System.out.println("AVANZA AL PASO 3");
						break;
						
					}else {
						System.out.println("SE DEVUELVE AL PASO 1");
						steps=1;
						break;
					}
				case 3:
					System.out.println("PASO 3: External layer neighboring function");
					ELSA elsa = new ELSA(modelo);
					sol_prima = elsa.getSol();
					steps=4;
					break;
				case 4:
					System.out.println("PASO 4");
					k=1;
					current_solution = sol_prima;
					best_solution = current_solution;
					while(k<=N_2){
						ILSA ilsa = new ILSA(current_solution);
						sol_prima_prima = ilsa.getSol();
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
					steps=5;
					break;
				case 5:
					System.out.println("PASO 5");
					if(sol_prima.funcion_objetivo()<global_optimal_solution.funcion_objetivo())global_optimal_solution = sol_prima;
					steps=6;
					break;
				case 6:
					System.out.println("PASO 6");
					if(sol_prima.funcion_objetivo()<current_solution.funcion_objetivo())current_solution = sol_prima;
					steps=7;
					break;
				case 7:
					System.out.println("PASO 7");
					  double phy = Math.random()*1;
                      if(phy < metropolis_condition(sol_prima,current_solution,Ti)) {
                          current_solution= sol_prima;
                      }
                    steps=8;
					break;
					
				case 8:
					System.out.println("PASO 8");
					i++;
					if(i<=N_1)steps=3;
					else steps=9;
					break;
				case 9:
					System.out.println("PASO 9");
					Ti = Ti*DTi;
					steps=10;
					break;
				case 10:
					System.out.println("PASO 10");
					if(Ti>=Tf)
					{
						System.out.printf("temp inicial %f",Ti);
						System.out.printf("FO final: %f",global_optimal_solution.funcion_objetivo());

						i=1;
						steps=3;
					}else finish = true;
					//Mostrar sol optima	
					break;
				default:
					finish = true;
					break;
				}
		}
		System.out.println("GGGG");
	}
	private double metropolis_condition(Model sol_prima, Model current_solution, double Ti) {
       double metropolis_value = Math.exp((-(sol_prima.funcion_objetivo()-current_solution.funcion_objetivo())/Ti));
        return metropolis_value;
    }

	private boolean checkFeasibilities(Model current_solution) {
		//RESTRICCIONES (3) Y (5)
		//REVIAR RESTRICCION (3) MEJOR
		/** RESTRICCIÓN(3) DEMANDA, TODA DEMANDA DE CADA CLIENTE DEBE SER ATENDIDA **/
		boolean flag1 = false;
		int clientes_atendidos = 0;
		int[][] ys = current_solution.getY();
		for(int i = 0 ; i < current_solution.getI() ; i++) {
			innerloop:
			for(int j = 0 ; j < current_solution.getJ() ; j++) {
				if(ys[i][j] == 1) {
					clientes_atendidos++;
					break innerloop;
				}
			}
		}
		
		/** SI LOS CLIENTES ATENDIDOS ES ENREALIDAD EL NUMERO TOTAL DE CLIENTES */
		/** TODOS LOS CLIENTES ATENDIDOS */
		if(clientes_atendidos == current_solution.getI()) {
			flag1 = true;
		}else {
			flag1 = false;
		}
		
		/** RESTRICCIÓN(5) CAPACIDAD DE LAS FACILITIES **/
		boolean flag2 = false;
		int[] capacidad_f = current_solution.getCapacity();
		int[] demanda_cliente = current_solution.getDemand();
		int demanda_facility;
		int j;
		/** ACA RECORRO POR FACILITIES PRIMERO, PARA VER SI CADA UNA DE LAS FACILITIES NO SOBREPASA SU CAPACIDAD*/
		loop:
		for(j = 0 ; j < current_solution.getJ() ; j++) { /** FACILITY **/
			demanda_facility = 0;
			for(int i = 0 ; i < current_solution.getI() ; i++) { /** CLIENT **/
				if(ys[i][j] == 1) {
					demanda_facility = demanda_facility + demanda_cliente[i];
				}
			}
			//System.out.println("DEMANDA DE LA FACILITIY ["+j+"] : "+demanda_facility);
			if(demanda_facility > capacidad_f[j]) { /**EN EL CASO DE QUE SE SOBREPASE LA DEMANDA */
				break loop;
			}/** EN CASO DE QUE NO SOBREPASE, SE SIGUE EL LOOP */
		}
		/** SI SE CUMPLE ESTE IF, SE DICE QUE EL LOOP ASUME CON EXITO */
		/** POR ENDE SE CUMPLIRÍA QUE TODAS LAS FACILITIES NO SOBREPASAN SU CAPACIDAD */
		if(j == 100) {
			flag2 = true;
		}else { /** EN EL OTRO CASO, ALGUNA FACILITY NO CUMPLE CON LA RESTRICCIÓN */
			flag2 = false;
		}
		
		if(flag1 == true && flag2 == true) { /** EN EL CASO QUE SE CUMPLAN LAS DOS RESTRICCIONES **/
			return true;
		}else {
			return false;
		}
	}
	
	private void ELSA(Model modelo) {
	}


	private void ILSA(Model modelo) {
	}

}
