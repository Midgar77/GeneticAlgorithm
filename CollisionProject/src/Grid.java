import java.util.ArrayList;
import java.util.List;

public class Grid {
	/**
	 * Represents the Grid, which is a 2D array that can hold Sprites.
	 * 
	 */

	private Sprite[][] grid;
	private List<Sprite> spriteList;
	private ArrayList<Lion> lionList;
	private ArrayList<Sprite> removeList;
	private int height, width;
	private Simulation sim;
	private int gazelleCaptured;

	public Grid(int cellHeight, int cellWidth){
		width = cellWidth;
		height = cellHeight;
		grid = new Sprite[height][width];
		spriteList = new ArrayList<Sprite>();
		lionList = new ArrayList<Lion>();
		removeList = new ArrayList<Sprite>();
		this.setGazelleCaptured(0);
	}

	public void addSprite(Sprite sprite, int c, int r){
		if(this.isFilled(c, r)){
			System.out.println("adding sprite incorrectly: " + this.getCell(c, r));
			System.out.println(sprite);
			System.exit(0);
		}
		grid[r][c] = sprite;
		spriteList.add(sprite);
		if(sprite.getType().equals("L"))
			lionList.add((Lion)sprite);
	}

	//Adds sprite to locations based on that sprite's x and y vals
	public void addSprite(Sprite sprite){
		addSprite(sprite, sprite.getX(), sprite.getY());
	}


	//Returns all the sprites in the simulation
	public List<Sprite> getSprites(){
		return spriteList;
	}

	//Returns all the lions in the simulation
	public ArrayList<Lion> getLions(){
		return lionList;
	}


	//Handles collisions using sprites' nextMove() and current location details
	//Sets a sprite's canMove ivar to false if they are not allowed to move due to collision handling
	//Sets canMove to true if they can move
	public void handleCollisions(){
		TreeCollisionHandler.handleCollisions(this);
	}


	//Returns the distance between two param Sprites
	public double distance(Sprite s1, Sprite s2){
		return Math.sqrt( Math.pow(s1.getX()-s2.getX(), 2) +  Math.pow(s1.getY()-s2.getY(), 2) );
	}


	//Given a Sprite, return the closest Sprite with param type in simulation
	//Only returns null if there are no other sprites that match the type in sim, or type is incorrect
	//Type should be L for lion, F for frozen lion, H for hunter, or G for gazelle
	//TODO: Optimize this. This currently makes runtime O(n^2) because this is O(n) and is usually called for each sprite at every step
	public Sprite closestSprite(Sprite s, String type){
		double minDistance = Integer.MAX_VALUE;
		if(!(type.equals("L") || type.equals("F") || type.equals("H") || type.equals("G")))
			return null;

		Sprite closestSprite = null;
		double distance = 0;

		for(Sprite other : spriteList){
			if(other.getType().equals(type) && !other.equals(s)){
				distance = distance(s, other);
				if(distance < minDistance){
					closestSprite = other;
					minDistance = distance;
				}
			}
		}
		return closestSprite;
	}

	public void addRemoveSprite(Sprite s){
		removeList.add(s);
	}

	public Lion closestLion(Sprite s){
		return (Lion)closestSprite(s, "L");
	}

	public Hunter closestHunter(Sprite s){
		return (Hunter)closestSprite(s, "H");
	}

	public Gazelle closestGazelle(Sprite s){
		return (Gazelle)closestSprite(s, "G");
	}


	public Lion closestFrozenLion(Sprite s){
		return (Lion)closestSprite(s, "F");
	}

	//Updates grid based on all of the sprites' desired new locations
	public void updateGrid(){
		for(Sprite s : spriteList)
			s.update();
		
		for(Sprite s : removeList)
			this.removeSprite(s);
		
		removeList.clear();

		handleCollisions();	//This moves the sprites appropriately
	}


	//Moves sprite to its desired new location (x+xa, y+ya) from its current location (x, y)
	//This assumes collisions have been handled and sprite can successfully move to new location
	public void moveSprite(Sprite s){
		if(this.getCell(s.nextMove()[0], s.nextMove()[1]) != null && this.getCell(s.nextMove()[0], s.nextMove()[1]) != s){
			//System.out.println("BAD COLLISION with " + s + " at "+s.getX()+", "+s.getY());
			//System.out.println("CURRENTLY IN DESIRED CELL: " + this.getCell(s.nextMove()[0], s.nextMove()[1]));
		}
		else{
			grid[s.getY()][s.getX()] = null;
			s.move();
			grid[s.getY()][s.getX()] = s;
			s.setCollisionProcessed(false);
		}
	}
	
	
	//Returns null if no Sprite is in cell
	public Sprite getCell(int c, int r){
		if(r >= this.height || r < 0 || c < 0 || c >= this.width)
			return null;
		return grid[r][c];
	}

	//Probably only used to remove a Gazelle
	public void removeSprite(Sprite s){
		grid[s.getY()][s.getX()] = null;
		spriteList.remove(s);
	}

	public boolean isFilled(int c, int r){
		return getCell(c,r) != null;
	}

	public int getHeight(){
		return height;
	}

	public int getWidth(){
		return width;
	}

	public String toString(){
		String result = "";
		int count = 0;

		for(int i = 0; i < height; i++){
			if(i==0){
				result += "    ";
				for(int j = 0; j < width; j++){
					if(j < 10) result += j + "   ";
					else result += j + "  ";
				}
				result += "\n";
			}
			for(int j = 0; j < width; j++){
				if(j==0){
					if(i < 10) result += i + "   ";
					else result += i + "  ";
				}
				if(grid[i][j] == null)
					result += "-" + "   ";
				else{
					result += grid[i][j] + " ";
					count++;
				}
			}
			result += "\n";
		}
		System.out.println("Sprite count: " + count);
		return result;
	}

	public Simulation getSimulation() {
		return sim;
	}

	public void setSimulation(Simulation sim) {
		this.sim = sim;
	}

	public int getGazelleCaptured() {
		return gazelleCaptured;
	}

	public void setGazelleCaptured(int gazelleCaptured) {
		this.gazelleCaptured = gazelleCaptured;
	}
	
	public int getTotalTimeFrozen(){
		int sum = 0;
		for(Lion l : lionList)
			sum += l.getTimeFrozen();
		return sum;
	}
}
