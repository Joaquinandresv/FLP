package flp.csa;

public class ILSA {
	static Model solucion_vecina;
	
	public ILSA(Model modelo) {
		solucion_vecina = selectOperator(modelo);
	}
	
	private Model selectOperator(Model modelo) {
		Model s_prima = null;
		int intento1,intento2;
		boolean flag = false;
		intento1=0; intento2=0;
		while(flag == false) {
			int random = (Math.random()<0.5)?1:2;
			switch(random){
				case 1:
					s_prima = operator1(modelo);
					if(s_prima != null) {
						flag = true;
					}else intento1++;
					if(intento1>0 && intento2>0)return modelo;
					break;
				case 2:
					s_prima = operator2(modelo);
					if(s_prima != null) {
						flag = true;	
					}else intento2++;
					if(intento1>1 && intento2>1)return modelo;
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
			if(modelo_prueba.checkFeasibilities() == true) {
				flag = true;
				y[cliente][j_] = 0;
				y[cliente][j_prima] = 1;
				modelo.setY(y);
				return modelo;
			}
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
		boolean flag = false;

		int i1=0, i2=0, j1=0, j2=0,cont=0;
		while(flag == false) {
			i1=(int) (Math.random() * 1000);
			i2=(int) (Math.random() * 1000);
			j1=(int) (Math.random() * 100) ;
			j2=(int) (Math.random() * 100) ;
			if(ys[i1][j1] == 1 && ys[i2][j2] == 1 && ys[i2][j1] == 0 && ys[i1][j2] == 0) {
				ys[i1][j1] = 0;
				ys[i2][j2] = 0;
				ys[i1][j2] = 1;
				ys[i2][j1] = 1;
				modelo_prueba.setY(ys);
				if(modelo_prueba.checkFeasibilities() == true) {
					y[i1][j1] = 0;
					y[i2][j2] = 0;
					y[i1][j2] = 1;
					y[i2][j1] = 1;
					modelo.setY(y);
					return modelo;
				}
			}else {
				cont++;
			}
			if(cont >5) {
				return null;
			}
		}
		return null;
	}
}
