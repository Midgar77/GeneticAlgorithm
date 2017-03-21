
public class PackingFitnessCalculator extends FitnessCalculator{

	@Override
	public double calculate(Chromosome c, int seed) {
		//Fitness based on how balanced the binary genes are in chromosome
		int count = 0;
    	for(int i = 0; i < c.size(); i++)
    		count += (c.getGene(i) == 0) ? -1 : 1;
    	
    	return c.size()-Math.abs(count);
	}
	
}
