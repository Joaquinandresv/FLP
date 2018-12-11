package flp.csa;

public class SimulatedAnnealing {
    /*Performs simulated annealing by calling functions to calculate
    energy and make moves on a state.  The temperature schedule for
    annealing may be provided manually or estimated automatically.
    */
	
	static double INITIAL_TEMPERATURE = 25000.0;
	static double COOLING_STEPS = 50000;
	static double COOLING_FACTOR = 0.999997;
	static int updates = 100;
	private double current_solution = 0;
	private double best_solution = 0;
	
	public SimulatedAnnealing(Model modelo) {
		ILSA(modelo); //Internal layer subalgorithm
		ELSA(modelo); //External layer subalgorithm
	}


	private void ILSA(Model modelo) {
		// TODO Auto-generated method stub
		
	}
	
	private void ELSA(Model modelo) {
		// TODO Auto-generated method stub
		
	}

	public double getCurrent_solution() {
		return current_solution;
	}

	public void setCurrent_solution(double current_solution) {
		this.current_solution = current_solution;
	}

	public double getBest_solution() {
		return best_solution;
	}

	public void setBest_solution(double best_solution) {
		this.best_solution = best_solution;
	}

}
