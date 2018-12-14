package flp.csa;

import java.util.Random;

public class ELSA {
	
	public ELSA(Model modelo) {
		Model solucion_vecina = null;
		solucion_vecina = selectOperator(modelo);
	}
	
	private Model selectOperator(Model modelo) {
		Model s_prima = null;
		boolean flag = false;
		while(flag == false) {
			int random = (Math.random()<0.5)?1:3;
			System.out.println("Operador --> "+random);
			switch(random){
				case 1:
					s_prima = operator1(modelo);
					if(s_prima != null) flag = true;
				case 2:
					s_prima = operator2(modelo);
					if(s_prima != null) flag = true;	
				case 3:
					s_prima = operator3(modelo);
					flag = true;
					
			}
		}
		return s_prima;
	}

	public Model operator1(Model modelo) { /** ABRE UNA FACILITY CERRADA **/
		/* CAMBIAR GET J, POR M = ALLOWED MAXIMUM FACILITIES */
		int sumatoria = 0;
		int[] x = modelo.getX();
		for(int j = 0 ; j < modelo.getJ() ; j++) {
			sumatoria = sumatoria + x[j];
		}
		/* CAMBIAR GET J, POR M = ALLOWED MAXIMUM FACILITIES */
		if(sumatoria < modelo.getJ()) {
			Model solucion_vecina = modelo;
			boolean flag = false;
			while(flag  == false) {
				Random r = new Random();
				int low = 0;
				int high = 99;
				int result = r.nextInt(high-low) + low;
				if(x[result] == 0) {
					x[result] = 1;
					solucion_vecina.setX(x);
					actualizarSolucion(solucion_vecina,result);
					return solucion_vecina;
				}
				System.out.println(".");
			}
		}
		return null;
	}

	public Model operator2(Model modelo) { /** CIERRA UNA FACILITY ABIERTA **/
		/* CAMBIAR GET J, POR M = ALLOWED MAXIMUM FACILITIES */
		int sumatoria = 0;
		int[] x = modelo.getX();
		for(int j = 0 ; j < modelo.getJ() ; j++) {
			sumatoria = sumatoria + x[j];
		}
		/* CAMBIAR GET J, POR M = ALLOWED MAXIMUM FACILITIES */
		if(sumatoria >= 1) {
			Model solucion_vecina = modelo;
			boolean flag = false;
			while(flag  == false) {
				Random r = new Random();
				int low = 0;
				int high = 99;
				int result = r.nextInt(high-low) + low;
				if(x[result] == 1) {
					x[result] = 0;
					solucion_vecina.setX(x);
					actualizarSolucion(solucion_vecina,result);
					return solucion_vecina;
				}
				System.out.println(".");
			}
		}
		return null;
	}

	public Model operator3(Model modelo) {
		Model solucion_vecina = modelo;
		int[] x = modelo.getX();
		int pos_cerrada = 0, pos_abierta = 0;
		for(int j = 0 ; j < modelo.getJ() ; j++) {
				if(x[j] == 0) {
					pos_cerrada = j;
					break;
				}
		}
		for(int j = 0 ; j < modelo.getJ() ; j++) {
			if(x[j] == 1) {
				pos_abierta = j;
				break;
			}
		}
		
		x[pos_cerrada] = 1; /**SE INTERCAMBIAN LOS VALORES **/
		x[pos_abierta] = 0; /** SE ABRE FACILITY CERRADA, SE CIERRA FACILITY ABIERTA **/
		solucion_vecina.setX(x);
		actualizarSolucion(solucion_vecina, pos_cerrada, pos_abierta);
		return solucion_vecina;
	}
	
	private void actualizarSolucion(Model solucion_vecina, int pos_j) {
		int[] xs = solucion_vecina.getX();
		int[][] ys = solucion_vecina.getY();
		boolean flag = false;
		if(xs[pos_j] == 0) {
			for(int i = 0 ; i < solucion_vecina.getI() ; i++) {
				ys[i][pos_j] = 0;
			}
		}else {
			while(flag  == false) {
				Random r = new Random();
				int low = 0;
				int high = 999;
				int result = r.nextInt(high-low) + low;
				System.out.println("cliente --> "+result);
				loop:
				if(ys[result][pos_j] == 0) {
					for(int j = 0 ; j < solucion_vecina.getJ() ; j++) {
						if(ys[result][j] == 1) {
							break loop;
						}
					}
					ys[result][pos_j] = 1;
					flag = true;
				}
			}
		}
		solucion_vecina.setY(ys);
	}
	
	private void actualizarSolucion(Model solucion_vecina, int pos_j, int pos_j_prima) {
		int[] xs = solucion_vecina.getX();
		int[][] ys = solucion_vecina.getY();
		boolean flag = false;
		for(int i = 0 ; i < solucion_vecina.getI() ; i++) {
				ys[i][pos_j_prima] = 0;
		}
		while(flag  == false) {
			Random r = new Random();
			int low = 0;
			int high = 99;
			int result = r.nextInt(high-low) + low;
			loop:
			if(ys[result][pos_j] == 0) {
				for(int j = 0 ; j < solucion_vecina.getJ() ; j++) {
					if(ys[result][j] == 1) {
						break loop;
					}
				}
				ys[result][pos_j] = 1;
				flag = true;
			}
			System.out.println("3-");
		}
		solucion_vecina.setY(ys);
	}
}
