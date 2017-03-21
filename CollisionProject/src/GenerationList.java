import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GenerationList implements Serializable {

	/*
	 * 
	 * Used to store information for each generation such as Population and Generation number (used as Random seed).
	 * This is primarily used to easily store this info in files.
	 * This is just a wrapper object for an ArrayList of Populations where the index is the generation - 1.
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private ArrayList<Population> generations;
	private ArrayList<Integer> seeds;
	private String date;

	public GenerationList(ArrayList<Population> gens, ArrayList<Integer> seeds){
		this.setSeeds(new ArrayList<Integer>(seeds));
		this.setGenerations(new ArrayList<Population>(gens));
		setDate(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(System.currentTimeMillis()));
	}

	public GenerationList(){
		this.generations = new ArrayList<Population>();
		this.seeds = new ArrayList<Integer>();
		setDate(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(System.currentTimeMillis()));
	}

	public ArrayList<Population> getGenerations() {
		return generations;
	}

	//1-indexed, not 0. Enter 1 to get first gen
	public Population getGeneration(int gen){
		return generations.get(gen-1);
	}

	//1-indexed, not 0. Enter 1 to get first gen
	public int getSeed (int gen){
		return seeds.get(gen-1);
	}

	public void setGenerations(ArrayList<Population> generations) {
		this.generations = generations;
	}

	public void addGeneration(Population p, int seed){
		Population pop = new Population(p, generations.size()+1);
		generations.add(pop);
		seeds.add(seed);
	}


	public String toString(){
		String result = date + "\n";
		for(int i = 0; i < generations.size(); i++)
			result += "\nGeneration " + ((Integer)(i+1)) + ": \n" + generations.get(i).toString() + "\n";

		return result;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public ArrayList<Integer> getSeeds() {
		return seeds;
	}

	public void setSeeds(ArrayList<Integer> seeds) {
		this.seeds = seeds;
	}

}
