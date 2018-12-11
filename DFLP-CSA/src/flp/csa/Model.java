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

}
