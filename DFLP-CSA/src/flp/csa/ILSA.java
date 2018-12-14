package flp.csa;

public class ILSA {
	static Model solucion_vecina;
	public ILSA(Model modelo) {
		solucion_vecina = selectOperator(modelo);
	}
	private Model selectOperator(Model modelo) {
		Model s_prima = null;
		boolean flag = false;
		while(flag == false) {
			int random = (Math.random()<0.5)?1:2;
			System.out.println("Ilsa Operador --> "+random);
			switch(random){
				case 1:
					s_prima = operator1(modelo);
					if(s_prima != null) flag = true;
				case 2:
					s_prima = operator2(modelo);
					if(s_prima != null) flag = true;			
			}
		}
		return s_prima;
	}
	public Model getSol() {
		return solucion_vecina;
	}
	public Model operator1(Model modelo) {
		int[][] y = modelo.getY();
		int[] pos_cerrada = new int[2];
		int[] pos_abierta = new int[2];
		
		outerloop1:
		for(int i = 0 ; i < modelo.getI() ; i++) {
			for(int j = 0 ; j < modelo.getJ() ; j++) {
				if(y[i][j] == 0) {
					pos_cerrada[0] = i;
					pos_cerrada[1] = j;
					break outerloop1;
				}
			}
		}
		
		outerloop2:
		for(int i = 0 ; i < modelo.getI() ; i++) {
			for(int j = 0 ; j < modelo.getJ() ; j++) {
				if(y[i][j] == 1) {
					pos_abierta[0] = i;
					pos_abierta[1] = j;
					break outerloop2;
				}
			}
		}
		
		y[pos_cerrada[0]][pos_cerrada[1]] = 1;
		y[pos_abierta[0]][pos_abierta[1]] = 0;
		modelo.setY(y);
		return modelo;
	}
	
	public Model operator2(Model modelo) {
		int[][] y = modelo.getY();
		int[] pos = new int[2];
		int[] pos_2 = new int[2];
		int[] visit = new int[2];/** GUARDA POSICIONES ULTIMAS VISITADAS */
		boolean flag = false;
		visit[0] = 0; 
		visit[1] = -1;
		while(flag == false) {
			outerloop1:
			/** SE SUMA 1 SOLO AL J, PARA CONTINUAR EN LAS FACILITIES CON EL MISMO CLIENTE */
			for(int i = visit[0] ; i < modelo.getI() ; i++) {
				for(int j = visit[1]+1 ; j < modelo.getJ() ; j++) {
					if(y[i][j] == 1) {
						pos[0] = i;
						pos[1] = j;
						break outerloop1;
					}
				}
			}
			
			outerloop2:
			/** SE SUMA 1 A TODO, PARA ENCONTRAR NUEVO CLIENTE */
			for(int i = pos[0]+1 ; i < modelo.getI() ; i++) {
				for(int j = 0 ; j < modelo.getJ() ; j++) {
					if(y[i][j] == 1 && pos[1] != j) {
						pos_2[0] = i;
						pos_2[1] = j;
						break outerloop2;
					}
				}
			}
			/** SE INTERCAMBIAN LOS VALORES */
			if(y[pos[0]][pos_2[1]] == 0 && y[pos_2[0]][pos[1]] == 0) {
				y[pos[0]][pos[1]] = 0;
				y[pos_2[0]][pos_2[1]] = 0;
				y[pos[0]][pos_2[1]] = 1;
				y[pos_2[0]][pos[1]] = 1;
				flag = true;
			}else {
			/** SE GUARDA LOS ULTIMOS VISITADOS PARA NO VOLVER A CAER EN LA MISMA POSICION */
				visit[0] = pos[0];
				visit[1] = pos[1];
			}
		}
		modelo.setY(y);
		return modelo;
	}
}
