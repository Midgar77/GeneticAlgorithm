import java.io.Serializable;
import java.util.Random;

public class Chromosome implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Chromosome class to represent an individual Chromosome in a genetic algorithm.
	 * Date: January 6, 2017
	 * Author: Anthony Groves
	 * 
	 */


	/* Notes:
	 * Sequence of actions
	 * Repeated blocks of 18, chosen based on sensor inputs
	 * 
	 * Criterion for how far others are.
	 * Select type, select distance, and then default of dont care.
	 * < 5 sections
	 * 
	 *  Bits for scale (distance), for care/dont care, less than/greater than
	 *  Care about closest gazelle IF hunter is ....
	 * 
	 * -----------------------------------------------------------------
	 * 
	 * Plan: 
	 * Have blocks of the 18 bits, chosen based on sensor input.
	 * Sensor input: Which types and how far they are
	 * 
	 * Assign blocks of the 18 bits based on sensor input:
	 * Ex: If nearest Gazelle is < 3 units away & nearest Hunter is > 3 units away, execute Block 2.
	 * Sensor input range:
	 *    Type: Nearest Frozen Lion, Nearest Gazelle, Nearest Hunter, Nearest Unfrozen Lion
	 *    Distances: Very Close, Close, Medium distance, Far
	 *    (Look at simulation to determine the values of these categories)
	 * 
	 * 
	 */

	private double fitness;
	private int[] genes;
	private Random rnd;

	//Creates random chromosome
	public Chromosome(int geneLength){
		genes = new int[geneLength];
		rnd = new Random();
		for(int i = 0; i < genes.length; i++) 
			genes[i] = rnd.nextInt(2);
		
		this.fitness = 0;
	}

	//constructor for custom set of genes
	public Chromosome(int[] inputGenes){
		genes = inputGenes.clone();  
		this.fitness = 0;

	}

	//Copy constructor
	public Chromosome(Chromosome c){
		genes = new int[c.size()];
		for(int i = 0; i < genes.length; i++)
			genes[i] = c.getGene(i);
		rnd = new Random();
		this.fitness = c.getFitness();
	}


	public int getGene(int index) {
		return genes[index];
	}

	public void setGene(int index, int value) {
		genes[index] = value;
	}

	public int size() {
		return genes.length;
	}

	public int[] getGenes(){
		return genes;
	}


	public double getFitness() {
		return fitness;
	}


	//Calls the fitness calculator to calculate its fitness
	public void calculateFitness(FitnessCalculator calc, int seed){
		this.fitness = calc.calculate(this, seed);
	}


	//Performs crossover of this Chromosome and other Chromosome and returns the changed other Chromosome
	//Currently, this only does a crossover with first half of the chromosomes
	public Chromosome crossover(Chromosome other){
		int index = size()/2;
		for(int i = 0; i < index; i++){
			int temp = other.getGene(i);
			other.setGene(i, this.getGene(i));
			this.setGene(i, temp);
		}
		return other;
	}

	//Mutates each gene based on mutation rate
	public void mutate(double mutationRate){

		for(int i = 0; i < size(); i++){
			double randomSelection = Math.random();	//does not use Random object because it is seeded for simulation, mutation is not connected to simulations
			if(randomSelection < mutationRate){
				mutateGene(i);
			}
		}
	}


	public void mutateGene(int geneIndex){
		setGene(geneIndex, getGene(geneIndex) == 0 ? 1 : 0);
	}


	@Override
	public String toString() {
		String chromosomeString = "";
		for (int i = 0; i < size(); i++) {
			chromosomeString += getGene(i) + " ";
		}
		return "[ "+ chromosomeString + "]  with fitness: " + fitness;
	}
}
