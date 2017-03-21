import java.util.ArrayList;

public class HomogeneousFitnessCalculator extends FitnessCalculator{

	/**
	 * 
	 * Runs the simulation of homogeneous lions all with the input Chromosome c. Results of the simulation determine the fitness calculated for chromosome
	 * 
	 */


	@Override
	public double calculate(Chromosome c, int seed) {

		Grid grid = new Grid(20, 20);
		ArrayList<Lion> sprites = new ArrayList<Lion>();
		sprites.add(new Lion(grid, 1, 1, 1, seed));
		sprites.add(new Lion(grid, 1, 18, 2, seed));
		sprites.add(new Lion(grid, 5, 5, 3, seed));
		sprites.add(new Lion(grid, 10, 10, 4, seed));
		sprites.add(new Lion(grid, 15, 15, 5, seed));
		sprites.add(new Lion(grid, 5, 15, 6, seed));
		sprites.add(new Lion(grid, 15, 5, 7, seed));
		sprites.add(new Lion(grid, 19, 10, 8, seed));

		for(int i = 0; i < sprites.size(); i++)
			sprites.get(i).setIndividual(new Individual(c));

		Simulation sim = new Simulation(grid, sprites, seed);
		grid.setSimulation(sim);
		sim.runGeneration();
		
		
		//Incentive for being close to Gazelle, but try not to do this unless needed.
		//Amount of credit for altruism is the distance from the closest hunter (higher risk)
		
		double fitness = 1000-(grid.getTotalTimeFrozen()/10.0)+(grid.getGazelleCaptured()*1000);
		
		//System.out.println(c + " assigned fitness: " + fitness);
		return fitness;

	}
}
