import java.io.Serializable;

public class Individual implements Comparable<Individual>, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Class that represents and individual in a genetic algorithm.
	//Currently, it only holds the chromosome, but this class will eventually be extended to represent the individual agents
	
	private Chromosome chromosome;
	
	public Individual(Chromosome chromosome){
		this.chromosome = chromosome;
	}
	
	
	public Individual(int chromosomeLength, int seed){
		this.chromosome = new Chromosome(chromosomeLength);
	}
	
	
	public Individual(Individual i){
		this.chromosome = new Chromosome(i.getChromosome());
	}
	
	
	public Chromosome getChromosome(){
		return chromosome;
	}
	
	public void setChromosome(Chromosome chromosome){
		this.chromosome = chromosome;
	}
	

	public boolean equals(Object other){
		if(!(other instanceof Individual)) return false;
		if(this.getChromosome().size() != ((Individual)other).getChromosome().size()) return false;
		
		
		for(int i = 0; i < this.getChromosome().size(); i++){
			if(this.getChromosome().getGene(i) != ((Individual)other).getChromosome().getGene(i))
				return false;
		}
		return true;
	}
	
	public int compareTo(Individual other){
		if(this.getChromosome().getFitness() == other.getChromosome().getFitness()) return 0;
		return this.getChromosome().getFitness() > other.getChromosome().getFitness() ? 1 : -1;
	}
	
	public String toString(){
		return chromosome.toString();
	}

}
