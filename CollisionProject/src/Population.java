import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Population implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<Individual> population;
    private int maxSize;
    private int seed;

    // Create a population of Individuals (chromosomes generated randomly) that all have the same length of genes
    public Population(int populationSize, int individualLength, int seed) {
        population = new ArrayList<Individual>();
        maxSize = populationSize;
        this.seed = seed;
        for(int i = 0; i < populationSize; i++){
        	population.add(new Individual(individualLength, seed));
        }
    }
    
    //Creates empty population with its max size as param populationSize
    public Population(int populationSize, int seed){
    	this.seed = seed;
    	maxSize = populationSize;
    	population = new ArrayList<Individual>();
    }
    
    public Population(Population other, int seed){
    	this.seed = seed;
    	population = new ArrayList<Individual>();
        maxSize = other.getMaxSize();
        for(int i = 0; i < other.size(); i++){
        	population.add(new Individual(other.getIndividual(i)));
        }
    }
    
    public void assignFitness(FitnessCalculator calc){
    	for(int i = 0; i < population.size(); i++)
    		population.get(i).getChromosome().calculateFitness(calc, seed);
    }
    
    public void addIndividual(Individual individual){
    	population.add(individual);
    }

    public void setIndividual(int index, Individual individual){
    	population.set(index, individual);
    }
    
    public Individual getIndividual(int index){
    	return population.get(index);
    }
    
    public int getMaxSize(){
    	return maxSize;
    }
    
    public int getSeed(){
    	return seed;
    }
    
    //Sum of all fitnesses of individuals
    public double totalFitness(){
    	double totalFitness = 0;
    	for(Individual individual : population)
    		totalFitness += individual.getChromosome().getFitness();
    	return totalFitness;
    }
    
    
   //Returns int index of the Individual in this population that is selected from population using roulette wheel
    public int rouletteWheelSelection(){
    	if(size() == 0) return -1;
    	
    	double totalFitness = totalFitness();

    	double[] fitnessScale = new double[size()];
    	for(int i = size()-1; i >= 0; i--){
    		totalFitness -= getIndividual(i).getChromosome().getFitness();
    		fitnessScale[i] = totalFitness;
    	}
    	
    	int selectedIndividual = 0;
    	double selected = Math.random() * totalFitness();	//random not connected to simulation
    	
    	for(int i = 0; i < fitnessScale.length-1; i++){
    		if(selected >= fitnessScale[i])
    			selectedIndividual = i;
    	}
    	return selectedIndividual;
    }

    public Individual getFittest() {
    	if(size() < 1) return null;
        Individual fittest = population.get(0);
        for(int i = 1; i < size(); i++){
        	if(getIndividual(i).getChromosome().getFitness() > fittest.getChromosome().getFitness())
        		fittest = getIndividual(i);
        }
        return fittest;
    }
    
    //1-Based
    public Individual getNthFittest(int n){
    	Collections.sort(population);
    	Collections.reverse(population);
    	return getIndividual(n-1);
    }
    
    public boolean isFilled(){
    	return maxSize() <= size();
    }
    
    public int maxSize(){
    	return maxSize;
    }

    public int size() {
        return population.size();
    }
    
    public String toString(){
    	String popString = "";
    	for(Individual i : population)
    		popString += i.toString() + "\n";
    	
    	return popString;
    }
    
    
}