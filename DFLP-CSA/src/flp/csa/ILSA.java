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
					if(s_prima != null) {
						System.out.println("ENTRE IF OPERADOR 1");
						flag = true;
					}
					break;
				case 2:
					s_prima = operator2(modelo);
					if(s_prima != null) {
						System.out.println("ENTRE IF OPERADOR 2");
						flag = true;	
					}
					break;
			}
		}
		return s_prima;
	}
	public Model getSol() {
		return solucion_vecina;
	}
	public Model operator1(Model modelo) {
		int[][] y = modelo.getY();
		Model modelo_prueba = modelo;
		int[][] ys = modelo_prueba.getY();
		int j_ = 0;
		int j_prima = 0;
		int cliente = -1;
		boolean flag = false;
		
		while(flag == false) {
			
			outerloop1:
			for(int i = cliente+1 ; i < modelo.getI() ; i++) {
				for(int j = 0 ; j < modelo.getJ() ; j++) {
					if(y[i][j] == 0) {
						cliente = i;
						j_prima = j;
						break outerloop1;
					}
				}
			}
			
			outerloop2:
			for(int j = 0 ; j < modelo.getJ() ; j++) {
				if(y[cliente][j] == 1) {
						j_ = j;
						ys[cliente][j_] = 0;
						ys[cliente][j_prima] = 1;
						modelo_prueba.setY(ys);
						break outerloop2;
				}
			}
			//System.out.println("OPERATOR ILSA 1 = 1.");
			if(modelo_prueba.checkFeasibilities() == true) {
				System.out.println("OPERATOR ILSA 1 = ENTRE AL IF");
				flag = true;
				y[cliente][j_] = 0;
				y[cliente][j_prima] = 1;
				modelo.setY(y);
				return modelo;
			}
			//System.out.println("OPERATOR ILSA 1 = 2.");
			System.out.println("cliente "+cliente);
			if(cliente == modelo.getI()-1) {
				return null;
			}
		}		
		return null;
	}
	
	public Model operator2(Model modelo) {
		int[][] y = modelo.getY();
		Model modelo_prueba = modelo;
		int[][] ys = modelo_prueba.getY();
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
					if(y[i][j] == 1 && j != pos[1]) {
						pos_2[0] = i;
						pos_2[1] = j;
						break outerloop2;
					}
				}
			}
			System.out.println("OPERATOR ILSA 2 = 1.");
			/** SE INTERCAMBIAN LOS VALORES */
			System.out.println(ys[pos[0]][pos[1]]);
			System.out.println(ys[pos_2[0]][pos_2[1]]);
			System.out.println(ys[pos[0]][pos_2[1]]);
			System.out.println(ys[pos_2[0]][pos[1]]);
			if(ys[pos[0]][pos_2[1]] == 0 && ys[pos_2[0]][pos[1]] == 0) {
				System.out.println("OPERATOR ILSA 2 = ENTRE AL IF");
				System.out.println("----> "+visit[0]);
				ys[pos[0]][pos[1]] = 0;
				ys[pos_2[0]][pos_2[1]] = 0;
				ys[pos[0]][pos_2[1]] = 1;
				ys[pos_2[0]][pos[1]] = 1;
				modelo_prueba.setY(ys);
				if(modelo_prueba.checkFeasibilities() == true) {
					System.out.println("PASE RESTRICCIONES");
					y[pos[0]][pos[1]] = 0;
					y[pos_2[0]][pos_2[1]] = 0;
					y[pos[0]][pos_2[1]] = 1;
					y[pos_2[0]][pos[1]] = 1;
					modelo.setY(y);
					return modelo;
				}else {
					System.out.println("OPERATOR ILSA 2 = 2.");
					/** SE GUARDA LOS ULTIMOS VISITADOS PARA NO VOLVER A CAER EN LA MISMA POSICION */
						visit[0] = pos[0];
						visit[1] = pos[1];
					}
			}else {
				System.out.println("OPERATOR ILSA 2 = 3.");
			/** SE GUARDA LOS ULTIMOS VISITADOS PARA NO VOLVER A CAER EN LA MISMA POSICION */
				visit[0] = pos[0];
				visit[1] = pos[1];
			}
			System.out.println("POS  i "+pos[0]);
			System.out.println("POS j "+pos[1]);
			System.out.println("POS_2  i "+pos_2[0]);
			System.out.println("POS_2 j "+pos_2[1]);
			System.out.println("VISIT i "+visit[0]);
			System.out.println("VISIT j "+visit[1]);
			if(visit[0] >= modelo.getI()) {
				System.out.println("OPERATOR ILSA 2 = 4.");
				return null;
			}
		}
		System.out.println("OPERATOR ILSA 2 = 5.");
		return null;
	}
}
