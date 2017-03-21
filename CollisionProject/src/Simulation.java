import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {

	/*
	 * 
	 * 
	 * Runs every time a fitness calculator calls it (for each generation).
	 * Amount of time it runs is still not defined. Needs to start with a new world every time.
	 * Input: The list of lion agents to use (created in fitness calculator). Everything else should be instantiated in this class.
	 * Letting fitness calculator handle creating the lion agents allows the fitness calculator to determine if lions are homogeneous or heterogeneous
	 * 
	 * 
	 */
	
	private Grid grid;
	private int step;
	private List<Sprite> spriteList;
	private List<Lion> lionList;
	private int seed;
	private final int MAX_TIME_STEPS = 100;
	private final int GAZELLE_QUANTITY = 14;
	private final int HUNTER_QUANTITY = 1;
	

	public Simulation(Grid grid, ArrayList<Lion> lionList){
		this.grid = grid;
		step = 0;
		this.lionList = new ArrayList<Lion>(lionList);
		this.spriteList = createSprites(lionList);
		
	}
	
	public Simulation(Grid grid, ArrayList<Lion> lionList, int seed){
		this.grid = grid;
		step = 0;
		this.lionList = new ArrayList<Lion>(lionList);
		this.spriteList = createSprites(lionList);
		
	}


	/*
	 * Creates and returns all the sprites (includes the param input Lions) to setup the world
	 * Location of all non-lion sprites are random except they cannot be placed in occupied locations (like the pre-defined Lion start locations)
	 */
	public ArrayList<Sprite> createSprites(ArrayList<Lion> lionList){
		ArrayList<Sprite> sprites = new ArrayList<Sprite>();
		sprites.addAll(lionList);
		for(Sprite s : lionList)
			grid.addSprite(s);
		
		int x,y;
		Random r = new Random(seed);

		//Add hunters to empty spaces
		for(int i = 0; i < HUNTER_QUANTITY; i++){
			do{
				x = r.nextInt(grid.getWidth());
				y = r.nextInt(grid.getHeight());
			}while(grid.isFilled(x, y));
			Hunter h = new Hunter(grid, x, y, r.nextInt(3)-1, r.nextInt(3)-1, i+1, seed);
			sprites.add(h);
			grid.addSprite(h);
		}

		//Add gazelle to empty spaces
		for(int i = 0; i < GAZELLE_QUANTITY; i++){
			do{
				x = r.nextInt(grid.getWidth());
				y = r.nextInt(grid.getHeight());
			}while(grid.isFilled(x, y));
			Gazelle g = new Gazelle(grid, x, y, r.nextInt(3)-1, r.nextInt(3)-1, i+1, seed);
			sprites.add(g);
			grid.addSprite(g);
		}

		return sprites;
	}


	public int getStep(){
		return step;
	}

	public void step(){
		grid.updateGrid();
		step++;
	}


	//Runs the simulation for a generation based on given population
	public void runGeneration(){
		while(step < (MAX_TIME_STEPS)){
			//printLions();
			System.out.println("Step: " + step);
			System.out.println(grid);
			step();
			pause(1000);
		}
	}
	
	//Pauses for amount of parameter milliseconds
	public void pause(int ms){
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public void printLions(){
		for(Lion l : lionList)
			System.out.println("Lion at " + l.getX() + "," + l.getY() + " with xa,ya: " + l.getXa() + "," + l.getYa() + " and time: " + l.getTimeSteps() + ":  " + l.getIndividual().getChromosome());
	}


	/*
	 * THIS METHOD SHOULD BE DEPRECATED (FitnessCalculator should be in charge of creating lions and assigning their individuals)
	 */
	public void assignIndividuals(Population pop){
		int index = 0;
		for(Sprite s: spriteList){
			if(s.toString().equalsIgnoreCase("L")){
				((Lion)s).setIndividual(pop.getIndividual(index));
				index++;
			}
		}
	}


	public static void main(String[] args) {
		
		
		//This is to test stuff. Simulations should usually be ran from a GA using a fitness calculator
		/*
		Grid grid = new Grid(20, 20);
		ArrayList<Lion> sprites = new ArrayList<Lion>();
		sprites.add(new Lion(grid, 14, 14));
		sprites.add(new Lion(grid, 2, 4));
		sprites.add(new Lion(grid, 13, 8));
		sprites.add(new Lion(grid, 18, 2));
		sprites.add(new Lion(grid, 5, 11));
		

		Population pop = new Population(5);
		int[] genes = {0, 1, 1, 0, 1, 1, 1, 1, 1, 0};
		pop.addIndividual(new Individual(new Chromosome(genes)));
		genes[1] = 0;
		pop.addIndividual(new Individual(new Chromosome(genes)));
		genes[0] = 1;
		pop.addIndividual(new Individual(new Chromosome(genes)));
		genes[9] = 1;
		genes[1] = 1;
		pop.addIndividual(new Individual(new Chromosome(genes)));
		genes[8] = 0;
		pop.addIndividual(new Individual(new Chromosome(genes)));

		for(int i = 0; i < sprites.size(); i++)
			sprites.get(i).setIndividual(pop.getIndividual(i));

		Simulation sim = new Simulation(grid, sprites);
		grid.setSimulation(sim);
		
		System.out.println(grid);
		
		
		//sim.runGeneration();
		 * */
		 
	}


}
