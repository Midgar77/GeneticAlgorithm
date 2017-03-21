import java.util.ArrayList;
import java.util.Random;

public class Lion extends Sprite{

	private Individual individual;
	private ArrayList<Lion> lions;	//includes itself
	private Gazelle closestGazelle;
	private Hunter closestHunter;
	private Lion closestFrozenLion;
	private Lion closestFreeLion;
	private int timeSteps;
	private int gazelleCaptured;
	private int teammatesFreed;
	private int timeFrozen;
	private Random rnd;
	private boolean frozen;
	
	/*
	 * 
	 * Move towards nearest uncaptured gazelle (not any gazelle)
	 * 
	 */

	public Lion(Individual individual, Grid grid, int x, int y, int tag, int seed){
		super(grid, "L", x, y, 0, 0, tag, seed);
		this.individual = individual;
		lions = null;
		setClosestGazelle(null);
		setClosestHunter(null);
		setClosestFrozenLion(null);
		setClosestFreeLion(null);
		timeSteps = 1;
		setFrozen(false);
		this.setGazelleCaptured(0);
		this.setTeammatesFreed(0);
		this.setTimeFrozen(0);
		rnd = new Random(seed);
	}

	public Lion(Grid grid, int x, int y, int tag, int seed){
		super(grid, "L", x, y, 0, 0, tag, seed);
		lions = null;
		setClosestGazelle(null);
		setClosestHunter(null);
		setClosestFrozenLion(null);
		setClosestFreeLion(null);
		timeSteps = 1;
		setFrozen(false);
		this.setGazelleCaptured(0);
		this.setTeammatesFreed(0);
		this.setTimeFrozen(0);
		rnd = new Random(seed);
	}

	public void updateSpriteData(){
		lions = grid.getLions();
		setClosestGazelle(grid.closestGazelle(this));
		setClosestHunter(grid.closestHunter(this));
		setClosestFrozenLion(grid.closestFrozenLion(this));
		setClosestFreeLion(grid.closestLion(this));
	}

	public void update(){
		updateSpriteData();
		updateSpecialCollisions();
		timeSteps--;
		if(timeSteps <= 0)		//Only reselect direction after the timeSteps has expired. Shouldnt be less than zero unless an issue
			setMoveDirections();
	}


	/*
	 * Special collision handling case: being adjacent to a hunter makes this Lion freeze (and not move).
	 * Being adjacent to a frozen lion frees the lion, but neither lions move.
	 */
	public void updateSpecialCollisions(){
		Sprite[] adjSprites = new Sprite[8];
		adjSprites[0] = grid.getCell(x, y-1);
		adjSprites[1] = grid.getCell(x, y+1);
		adjSprites[2] = grid.getCell(x-1, y);
		adjSprites[3] = grid.getCell(x+1, y);
		adjSprites[4] = grid.getCell(x-1, y-1);
		adjSprites[5] = grid.getCell(x+1, y-1);
		adjSprites[6] = grid.getCell(x-1, y+1);
		adjSprites[7] = grid.getCell(x+1, y+1);

		/*
		 * Currently a lion is frozen or freed if a hunter or non-frozen teammate (respectively) is an adjacent cell.
		 * This may change later to forcing a hunter or teammate lion to want to move into a lion's cell to freeze/free it.
		 */
		
		//Free any adjacent frozen lions
		for(int i = 0; i < adjSprites.length; i++){
			if(adjSprites[i] != null && adjSprites[i].getType().equals("L") && ((Lion)adjSprites[i]).isFrozen()){
				((Lion)adjSprites[i]).setFrozen(false);
				setTeammatesFreed(getTeammatesFreed() + 1);
			}
		}
	
		//If a lion tries to move into a hunter's cell, it freezes
		if(grid.getCell(this.nextMove()[0], this.nextMove()[1]) != null && grid.getCell(this.nextMove()[0], this.nextMove()[1]).getType().equals("H")){
			frozen = true;
		}

	}


	@Override
	public void move() {
			if (canMove()){
				x = x + xa;
				y = y + ya;
			}
	}

	@Override
	public boolean canMove(){
		if(frozen)
			setTimeFrozen(getTimeFrozen() + 1);
		if (x+xa >= 0 && x+xa < grid.getWidth() && y+ya >= 0 && y+ya < grid.getHeight())	//regardless of canMove and frozen ivars, if next move is out of bounds, cant move
			return canMove && !frozen;	//must be able to move AND not be frozen
		else
			return false;
	}

	public void setMoveDirections(){
		int[] moveData = getMoveData();
		//System.out.println("Lion " + toString() + " moving, steps: " + moveData[0] + "," + moveData[1] + ": " + moveData[2]);
		this.setXa(moveData[0]);
		this.setYa(moveData[1]);
		this.setTimeSteps(moveData[2]);
	}
	
	
	
	
	/*
	 * Returns an int array with info on direction and time for individual's movement.
	 * First element is xa, second element is ya, and third element is number of steps to step in those directions.
	 * 
	 * 
	 * Last two digits is the time (binary to decimal) to move in the selected direction. This will be multiplied by a multiplier, probably 2 or 3
	 * First sixteen digits: 4 groups of 4 binary digits
	 * 4 groups: move towards nearest gazelle, nearest hunter, nearest trapped teammate lion, or nearest non-trapped teammate lion
	 * A group is selected based on roulette wheel selection where each group's decimal value (convert the 4-digit binary to decimal) is the group's chance of being selected
	 * So groups with higher values have higher, but not gauranteed, chances of being selected
	 * 
	 * 
	 * Algorithm will:
	 * 1. Use chromosome to decide which direction to move for how many steps
	 * 2. Use the 'closest_X' ivar from param lion to determine actual coordinates to use for direction chosen in step 1
	 * 3. Return data in 3 value int array: {xa, ya, time steps}
	 * 
	 * This method will be called everytime the 'time' value is up
	 * 
	 */
	public int[] getMoveData(){
		//System.out.println("Seed: " + seed);
		int[] genes = individual.getChromosome().getGenes();
		int[] data = new int[3];

		//Interpret first 16 digits to 4 decimal values
		int[] vals = new int[4];
		vals[0] = Integer.parseInt((Integer.toString(genes[0])+Integer.toString(genes[1])+Integer.toString(genes[2])+Integer.toString(genes[3])), 2);
		vals[1] = Integer.parseInt((Integer.toString(genes[4])+Integer.toString(genes[5])+Integer.toString(genes[6])+Integer.toString(genes[7])), 2);
		vals[2] = Integer.parseInt((Integer.toString(genes[8])+Integer.toString(genes[9])+Integer.toString(genes[10])+Integer.toString(genes[11])), 2);
		vals[3] = Integer.parseInt((Integer.toString(genes[12])+Integer.toString(genes[13])+Integer.toString(genes[14])+Integer.toString(genes[15])), 2);

		//Select group and assign chaseSprite (sprite to move towards)
		int selectedGroup = rouletteWheelSelection(vals);	//0-3 selected group
		Sprite chaseSprite = null;
		if(selectedGroup == 0 && closestGazelle == null){
			System.out.println("SIMULATION OVER, NO GAZELLE LEFT!");
			System.exit(0);
		}

		if(selectedGroup == 2)
			chaseSprite = closestFrozenLion;   

		//There may not be any frozen lions in simulation, so have to pick new direction.
		while(chaseSprite == null){
			selectedGroup = rouletteWheelSelection(vals);
			if(selectedGroup == 1) chaseSprite = closestHunter;
			else if(selectedGroup == 0) chaseSprite = closestGazelle;
			else chaseSprite = closestFreeLion;
		}



		//Get xa and ya vals based on where the nearest sprite is that was chosen to move towards
		int[] moveCoords = moveDirectionCoords(chaseSprite);

		data[0] = moveCoords[0];
		data[1] = moveCoords[1];

		//timeSteps is (decimal value of last two genes) * 2 + 1
		data[2] =  2 * Integer.parseInt((Integer.toString(genes[16])+Integer.toString(genes[17])), 2) + 1;
		return data;
	}


	//Roulette wheel selection to select chromosome group for choosing direction
	//Takes the 4 groups' values in int array and returns an int index that is the selected group based on roulette wheel
	public int rouletteWheelSelection(int[] vals){
		
		int total = 1;
		for(int i = 0; i < vals.length; i++){
			total += vals[i];
		}
		
		int selected = rnd.nextInt(total);
		
		//System.out.println("Selected group: " + selected);
		total = 0;
		for(int i = 0; i < vals.length; i++){
			if(selected >= total && selected < total+vals[i])
				return i;
			total += vals[i];
		}
		return vals[vals.length-1];
	}
	
	
	
	
	
	

	@Override
	public String toString(){
		return (frozen ? "F" : "L") + tag + (tag < 10 ? " " : "");	//L for lion, F for frozen lion
	}


	/*    GETTERS AND SETTERS    */

	public Individual getIndividual(){
		return this.individual;
	}

	public void setIndividual(Individual i){
		this.individual = i;
	}

	public int getTimeSteps(){
		return timeSteps;
	}

	public void setTimeSteps(int timeSteps){
		this.timeSteps = timeSteps;
	}

	public Lion getClosestFreeLion() {
		return closestFreeLion;
	}

	public void setClosestFreeLion(Lion closestFreeLion) {
		this.closestFreeLion = closestFreeLion;
	}

	public Lion getClosestFrozenLion() {
		return closestFrozenLion;
	}

	public void setClosestFrozenLion(Lion closestFrozenLion) {
		this.closestFrozenLion = closestFrozenLion;
	}

	public Hunter getClosestHunter() {
		return closestHunter;
	}

	public void setClosestHunter(Hunter closestHunter) {
		this.closestHunter = closestHunter;
	}

	public Gazelle getClosestGazelle() {
		return closestGazelle;
	}

	public void setClosestGazelle(Gazelle closestGazelle) {
		this.closestGazelle = closestGazelle;
	}

	public boolean isFrozen() {
		return frozen;
	}

	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}

	public int getGazelleCaptured() {
		return gazelleCaptured;
	}

	public void setGazelleCaptured(int gazelleCaptured) {
		this.gazelleCaptured = gazelleCaptured;
	}

	public int getTeammatesFreed() {
		return teammatesFreed;
	}

	public void setTeammatesFreed(int teammatesFreed) {
		this.teammatesFreed = teammatesFreed;
	}

	public int getTimeFrozen() {
		return timeFrozen;
	}

	public void setTimeFrozen(int timeFrozen) {
		this.timeFrozen = timeFrozen;
	}
}
