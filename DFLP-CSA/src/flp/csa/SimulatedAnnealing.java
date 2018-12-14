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
					System.out.println("PASO 1: Random initialization");
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
					System.out.println("PASO 2: Check feasibilities");
					if(checkFeasibilities(current_solution) == true) {
						steps++;
						System.out.println("AVANZA AL PASO 3");
						break;
						
					}else {
						System.out.println("SE DEVUELVE AL PASO 1");
						steps--;
						break;
					}
				case 3:
					System.out.println("PASO 3: External layer neighboring function");
					ELSA elsa = new ELSA(modelo);
					steps++;
					break;
				case 4:
					System.out.println("PASO 4");
					steps++;
					break;
				default:
					finish = true;
					break;
				}
		}
		System.out.println("GGGG");
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
