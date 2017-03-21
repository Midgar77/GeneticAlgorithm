import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javafx.application.Application;

public class GenerationRunner {
	
	private static final int GENERATION = 499;
	private static final int CHROMOSOME_NUM = 0;


	public static void main(String[] args) {
		runSimulation(CHROMOSOME_NUM);
		//runAllSimulations();
		//testGens();
	}
	
	public static void testGens(){
		GenerationList list = loadGenerations();
		System.out.println(list);
	}
	
	//Runs all simulations for entire population in GENERATION
	public static void runAllSimulations(){
		GenerationList list = loadGenerations();
		Population pop = list.getGeneration(GENERATION);
		System.out.println(pop);
		for(int i = 0; i < pop.size(); i++){
			runSimulation(i);
		}
	}
	
	//Runs the simulation of the chromosome index in Population for GENERATION
	public static void runSimulation(int chromosomeNum){
		
		GenerationList list = loadGenerations();
		Population pop = list.getGeneration(GENERATION);
		int seed = list.getSeed(GENERATION);
		
		//Lion placement and Grid needs to match FitnessCalculator
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
			sprites.get(i).setIndividual(new Individual(pop.getIndividual(chromosomeNum)));
		
		//System.out.println("Running for chromosome: " + pop.getIndividual(chromosomeNum));
		
		Simulation sim = new Simulation(grid, sprites, seed);
		grid.setSimulation(sim);
		sim.runGeneration();
	}

	
	public static GenerationList loadGenerations(){
		// read object from file
		FileInputStream fis;
		try {
			fis = new FileInputStream("C:/Users/Anthony/Desktop/GAoutput.txt");
			ObjectInputStream ois = new ObjectInputStream(fis);
			GenerationList result = (GenerationList) ois.readObject();
			ois.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
