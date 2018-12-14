package flp.csa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Model {
	private int[] X; // VARIABLE BINARIA DE DECISIÓN
	private int[][] Y; // VARIABLE BINARIA DE DECISION
	private int I; //Cantidad total de clientes
	private int J; //Cantidad total de facilities (posibles locations)
	private int[] capacity; //Capacidad de facility
	private int[] fixed_cost; //Costo asociado de abrir una facility en el lugar j
	private int[] demand; //Demanda de cliente i
	private double[][] demand_cost; //Costo asociado a suplir la demanda de un cliente desde un sitio j
	private double solution = 0;
	
	/* Función a minimizar 
	 * min f = sumatoria(fixed_cost[j]*X[j]) + sumatoria(sumatoria(demand[i]*demand_cost[i][j]*Y[i][j]))
	 * */
	/* Restricciones 
	 * (1) sumatoria(Y[i][j]) = 1 Hace que se cumpla la demanda de todos los clientes
	 * (2) sumatoria(demand[i]*Y[i][j] <= capacity[i]*X[j]) upper bound constraint
	 * (3) Y[i][j] - X[j] <= 0 capacity constraint of facility
	 * (4) X[j] perteneciente {0,1}
	 * (5) Y[i][j] perteneciente {0,1}
	 * */
	
	public Model(String file) {
		readFile(file);
	}
	
	private void readFile(String file) {
		// TODO Auto-generated method stub
		BufferedReader br = null;
		FileReader fr = null;
		int contador = 0;
		int contadorNuevo = 0;
		
		try {

			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String sCurrentLine;
			int i = 0;
			int z = 0;
			int j = 0;
			
			while((sCurrentLine = br.readLine()) != null) {
				if(contador == 0) {
					String[] split = sCurrentLine.split(" ");
					J = Integer.parseInt(split[1]);
					I = Integer.parseInt(split[2]);
					capacity = new int[J];
					fixed_cost = new int[J];
					demand = new int[I];
					demand_cost = new double[I][J];
					X = new int[J];
					Y = new int[I][J];
					contador++;
				}else if(contador > 0 && contador <= 100) {
					String[] split = sCurrentLine.split(" ");
					String auxiliar = split[2];
					auxiliar = auxiliar.replaceAll("\\.0*$", "");
					capacity[i] = Integer.parseInt(split[1]);
					fixed_cost[i] = Integer.parseInt(auxiliar);
					i++;
					if(contador == 100) {
						i = 0;
					}
					contador++;
				}else if(contador > 100) {
					if(contadorNuevo == 0) {
						demand[i] = (int) Double.parseDouble(sCurrentLine);
						contadorNuevo++;
						i++;
					}else if(contadorNuevo > 0 && contadorNuevo <= 16) {
						String[] split = sCurrentLine.split(" ");
						for(int x = 1 ; x < split.length ; x++ ) {
							demand_cost[z][j] = Double.parseDouble(split[x]);
							j++;
						}
						contadorNuevo++;
						if(contadorNuevo == 16) {
							contadorNuevo = 0;
							j = 0;
							z++;
						}
					}
				}
			}

		} catch (IOException e) {

			e.printStackTrace();
		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}
	public double funcion_objetivo() {
        double sum1 = 0;
        double sum2 = 0;
        double result = 0;

        for (int j = 0; j < J; j++) {
            sum1 += fixed_cost[j] * X[j];
        }

        for (int i = 0; i < I; i++) {
            for (int j = 0; j < J; j++) {
                sum2 += demand[i] * demand_cost[i][j] * Y[i][j];
            }
        }

        return result = sum1 + sum2;
	}
	
	public int getI() {
		return I;
	}

	public void setI(int i) {
		I = i;
	}

	public int getJ() {
		return J;
	}

	public void setJ(int j) {
		J = j;
	}

	public int[] getCapacity() {
		return capacity;
	}

	public void setCapacity(int[] capacity) {
		this.capacity = capacity;
	}

	public int[] getFixed_cost() {
		return fixed_cost;
	}

	public void setFixed_cost(int[] fixed_cost) {
		this.fixed_cost = fixed_cost;
	}

	public int[] getDemand() {
		return demand;
	}

	public void setDemand(int[] demand) {
		this.demand = demand;
	}

	public double[][] getDemand_cost() {
		return demand_cost;
	}

	public void setDemand_cost(double[][] demand_cost) {
		this.demand_cost = demand_cost;
	}
	
	public int[] getX() {
		return X;
	}

	public void setX(int[] x) {
		X = x;
	}

	public int[][] getY() {
		return Y;
	}

	public void setY(int[][] y) {
		Y = y;
	}
	
	public void iniciarRandom() {
		
		/** SE ABREN Y CIERRAN FACILITIES DE MANERA ALEATORIA **/
		int[] xs = new int[this.getJ()];	
		for(int j = 0 ; j < this.getJ() ; j++) { 
			int random = (Math.random()<0.5)?0:1;
			xs[j] = random;
		}
		this.setX(xs);
		
		/** LAS FACILITIES ABIERTAS SE LES ASIGNA CLIENTES A CUMPLIR SU DEMANDA **/
		/** SIEMPRE Y CUANDO NO SE EXECEDA DE LA CAPACIDAD TOTAL DE LA FACILITY **/
		int[][] ys = new int[this.getI()][this.getJ()];
		int[] capacidad_f = this.getCapacity(); /** CAPACIDAD DE CADA FACILITY **/
		int[] demanda_c = this.getDemand(); /** DEMANDA DE CADA CLIENTE */
		int[] demanda_total = new int[this.getJ()];
		int visitados;
		int pos_ult_cliente_atendido = 0;

		for(int i = 0 ; i < this.getI() ; i++) {
			visitados = 0;
			for(int j = 0 ; j < this.getJ() ; j++) {
				if(xs[j] == 1) {
					int random = (Math.random()<0.5)?0:1;
					if(random == 1 && visitados < 1) {
						demanda_total[j] = demanda_total[j] + demanda_c[i];
						if(demanda_total[j] <= capacidad_f[j]) {
							ys[i][j] = 1;
							visitados++;
						}else {
							ys[i][j] = 0;
						}
					}else {
						ys[i][j] = 0;
					}
				}else {
					ys[i][j] = 0;
				}
			}
		}
		this.setY(ys);
		
	}

	public double createInitialSolution(Model modelo) {
		double sumatoria1 = 0, sumatoria2 = 0;
		int[] xs = new int[modelo.getJ()];
		int[] fixed = modelo.getFixed_cost();
		
		for(int j = 0 ; j < modelo.getJ() ; j++) { 
			sumatoria1 = sumatoria1 + (fixed[j] * xs[j]); /*Sumatoria Fixed_cost x X */
		}
		
		int[][] ys = new int[modelo.getI()][modelo.getJ()];
		int[] demand = modelo.getDemand();
		double[][] demand_cost = modelo.getDemand_cost();
		
		for(int i = 0 ; i < modelo.getI() ; i++) {
			for(int j = 0 ; j < modelo.getJ() ; j++) {
				sumatoria2 = sumatoria2 + (demand[i] * demand_cost[i][j] * ys[i][j]); /*Sumatoria demand x demand_cost x Y */
			}
		}
		
		solution = sumatoria1 + sumatoria2; /*Función a minimizar*/
		
		return solution;
	}
	
	public void generateNewSolution(Model modelo) {
		
		/** LAS FACILITIES ABIERTAS SE LES ASIGNA CLIENTES A CUMPLIR SU DEMANDA **/
		/** SIEMPRE Y CUANDO NO SE EXECEDA DE LA CAPACIDAD TOTAL DE LA FACILITY **/
		int[] xs = this.getX();
		int[][] ys = new int[this.getI()][this.getJ()];
		int[] capacidad_f = this.getCapacity(); /** CAPACIDAD DE CADA FACILITY **/
		int[] demanda_c = this.getDemand(); /** DEMANDA DE CADA CLIENTE */
		int[] demanda_total = new int[this.getJ()];
		int visitados;
		int pos_ult_cliente_atendido = 0;

		for(int i = 0 ; i < this.getI() ; i++) {
			visitados = 0;
			for(int j = 0 ; j < this.getJ() ; j++) {
				if(xs[j] == 1) {
					int random = (Math.random()<0.5)?0:1;
					if(random == 1 && visitados < 1) {
						demanda_total[j] = demanda_total[j] + demanda_c[i];
						if(demanda_total[j] <= capacidad_f[j]) {
							ys[i][j] = 1;
							visitados++;
						}else {
							ys[i][j] = 0;
						}
					}else {
						ys[i][j] = 0;
					}
				}else {
					ys[i][j] = 0;
				}
			}
		}
		this.setY(ys);
	}
	
	public boolean checkFeasibilities() {
		//RESTRICCIONES (3) Y (5)
		//REVIAR RESTRICCION (3) MEJOR
		/** RESTRICCIÓN(3) DEMANDA, TODA DEMANDA DE CADA CLIENTE DEBE SER ATENDIDA **/
		boolean flag1 = false;
		int clientes_atendidos = 0;
		int[][] ys = this.getY();
		for(int i = 0 ; i < this.getI() ; i++) {
			innerloop:
			for(int j = 0 ; j < this.getJ() ; j++) {
				if(ys[i][j] == 1) {
					clientes_atendidos++;
					break innerloop;
				}
			}
		}
		
		/** SI LOS CLIENTES ATENDIDOS ES ENREALIDAD EL NUMERO TOTAL DE CLIENTES */
		/** TODOS LOS CLIENTES ATENDIDOS */
		if(clientes_atendidos == this.getI()) {
			flag1 = true;
		}else {
			flag1 = false;
		}
		
		/** RESTRICCIÓN(5) CAPACIDAD DE LAS FACILITIES **/
		boolean flag2 = false;
		int[] capacidad_f = this.getCapacity();
		int[] demanda_cliente = this.getDemand();
		int demanda_facility;
		int j;
		int suma;
		/** ACA RECORRO POR FACILITIES PRIMERO, PARA VER SI CADA UNA DE LAS FACILITIES NO SOBREPASA SU CAPACIDAD*/
		loop:
		for(j = 0 ; j < this.getJ() ; j++) { /** FACILITY **/
			demanda_facility = 0;
			suma = 0;
			for(int i = 0 ; i < this.getI() ; i++) { /** CLIENT **/
				suma = demanda_facility + demanda_cliente[i];
				if(ys[i][j] == 1 && suma <= capacidad_f[j]) { 
				/**Se checkea que no se sobre pase la capacidad de demanda maxima de la facility */
					demanda_facility = demanda_facility + demanda_cliente[i];
				}
			}
			//System.out.println("DEMANDA DE LA FACILITIY ["+j+"] : "+demanda_facility);
			if(demanda_facility > capacidad_f[j]) { /**EN EL CASO DE QUE SE SOBREPASE LA DEMANDA */
				break loop;
			}/** EN CASO DE QUE NO SOBREPASE, SE SIGUE EL LOOP */
		}
		/** SI SE CUMPLE ESTE IF, SE DICE QUE EL LOOP SE CUMPLE */
		/** POR ENDE SE CUMPLIRÍA QUE TODAS LAS FACILITIES NO SOBREPASAN SU CAPACIDAD MAXIMA */
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
}
