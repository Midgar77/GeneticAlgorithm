import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import org.jfree.ui.RefineryUtilities;

import javafx.application.Application;

public class SimpleAlgorithm {

	private static final double MUTATION_RATE = 0.05;
	private static final double CHROMOSOME_UNCHANGED_RATE = 0.01;	//rate at which a chromosome is able to avoid crossover
	private static final int MAX_ELITES = 1;	//number of elites to pass onto next generation
	private final static int SOLUTION_FITNESS = 100000;	//Desired fitness to stop program (reached solution). Set to a high number because we do not know this limit yet

	static int POP_SIZE = 64;
	static int CHROM_SIZE = 18;
	static int GENERATIONS = 500;

	public static void main(String[] args) {
		Population pop = new Population(POP_SIZE, CHROM_SIZE, 1);
		System.out.println(pop);
		SimpleAlgorithm.runAlgorithm(pop, GENERATIONS);

	}


	public static void runAlgorithm(Population pop, int maxGeneration){
		Random r = new Random();
		
		int seed = r.nextInt(100000);	//seed created randomly
		pop = new Population(pop, seed);	//put seed in pop
		Population newPop;
		HomogeneousFitnessCalculator fitnessCalc = new HomogeneousFitnessCalculator();	//This is only thing that needs changed when changing fitness function
		ArrayList<Double> totalFitness = new ArrayList<Double>();
		ArrayList<Double> mostFit = new ArrayList<Double>();
		GenerationList list = new GenerationList();

		//Assign initial fitness values
		System.out.println("\n\n\nRunning generation 1");
		pop.assignFitness(fitnessCalc);
		totalFitness.add(pop.totalFitness());
		mostFit.add(pop.getFittest().getChromosome().getFitness());
		list.addGeneration(pop, seed);
		System.out.println(pop);

		for(int gen = 2; gen <= maxGeneration; gen++){
			seed = r.nextInt(100000);
			newPop = new Population(POP_SIZE, seed);
			
			for(int i = 0; i < MAX_ELITES; i++)
				newPop.addIndividual(pop.getNthFittest(i+1));

			while(!newPop.isFilled()){
				Individual[] children = breed(pop);
				children[0].getChromosome().mutate(MUTATION_RATE);
				children[0].getChromosome().calculateFitness(fitnessCalc, seed);
				newPop.addIndividual(children[0]);
				children[1].getChromosome().mutate(MUTATION_RATE);

				if(children[0] != children[1] && !newPop.isFilled()){      //Avoid repeated chromosomes
					children[1].getChromosome().calculateFitness(fitnessCalc, seed);
					newPop.addIndividual(children[1]);
				}
			}

			pop = new Population(newPop, newPop.getSeed());
			totalFitness.add(pop.totalFitness());
			mostFit.add(pop.getFittest().getChromosome().getFitness());
			list.addGeneration(pop, seed);
			//System.out.println(gen + ":\n" + pop);
			

			if(pop.getFittest().getChromosome().getFitness() == SOLUTION_FITNESS){
				System.out.println("FOUND SOLUTION!");
				createGraphs(totalFitness, mostFit);
				break;
			}
		}
		
		saveGenerations(list);
		System.out.println("Solution never found. Fittest Individual: " + pop.getFittest());
		createGraphs(totalFitness, mostFit);
	}


	public static void createGraphs(ArrayList<Double> totalFitness, ArrayList<Double> mostFit){
		//Application.launch(TotalFitnessChart.class, totalFitness.toString());	//creates Total Fitness chart
		Application.launch(FittestChart.class, mostFit.toString());	//creates Highest Fitness chart
	}


	/**
	 * Breeds two individuals by selecting them, possibly mutating them, and adding their 2 children to newPopulation.
	 * First half of chromosome of child is from first Individual, second half from second. 2nd child is vice versa.
	 * Elitism where the same individual is selected twice is allowed based on ELITE_RATE
	 * @param population the population to select individuals from
	 * @return Individual child result from breeding
	 */
	public static Individual[] breed(Population pop){
		int firstIndex = pop.rouletteWheelSelection();
		Individual first = pop.getIndividual(firstIndex);
		int secondIndex = pop.rouletteWheelSelection();
		if(Math.random() > CHROMOSOME_UNCHANGED_RATE){
			while(secondIndex == firstIndex){
				secondIndex = pop.rouletteWheelSelection();
			}
		}
		Individual second = pop.getIndividual(secondIndex);
		Individual[] children = new Individual[2];
		children[0] = new Individual(first.getChromosome().crossover(second.getChromosome()));
		children[1] = new Individual(second.getChromosome().crossover(first.getChromosome()));
		return children;
	}


	public static void saveGenerations(GenerationList list){
		try {
			// write object to file
			FileOutputStream fos = new FileOutputStream("C:/Users/Anthony/Desktop/GAoutput.txt", false);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(list);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static GenerationList loadGenerations(){
		// read object from file
		FileInputStream fis;
		try {
			fis = new FileInputStream("C:/Users/Anthony/Desktop/GAoutput.txt.txt");
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
