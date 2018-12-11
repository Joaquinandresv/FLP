package flp.csa;

import java.util.Random;

public class ELSA {

	public Model operator1(Model modelo) { /** ABRE UNA FACILITY CERRADA **/
		/* CAMBIAR GET J, POR M = ALLOWED MAXIMUM FACILITIES */
		int sumatoria = 0;
		int[] x = modelo.getX();
		for(int j = 0 ; j < modelo.getJ() ; j++) {
			sumatoria = sumatoria + x[j];
		}
		/* CAMBIAR GET J, POR M = ALLOWED MAXIMUM FACILITIES */
		if(sumatoria < modelo.getJ()) {
			boolean flag = false;
			while(flag  == false) {
				Random r = new Random();
				int low = 0;
				int high = 99;
				int result = r.nextInt(high-low) + low;
				if(x[result] == 0) {
					x[result] = 1;
					modelo.setX(x);
					return modelo;
				}
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
			boolean flag = false;
			while(flag  == false) {
				Random r = new Random();
				int low = 0;
				int high = 99;
				int result = r.nextInt(high-low) + low;
				if(x[result] == 1) {
					x[result] = 0;
					modelo.setX(x);
					return modelo;
				}
			}
		}
		return null;
	}

	public Model operator3(Model modelo) {
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
		modelo.setX(x);
		return modelo;
	}
}
