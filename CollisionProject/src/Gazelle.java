import java.util.Random;

public class Gazelle extends Sprite {

	private final double MOVE_AWAY_LION_RATIO = 0.50;
	
	public Gazelle(Grid grid, int x, int y, int xa, int ya, int tag, int seed){
		super(grid, "G", x, y, xa, ya, tag, seed);
	}


	//Given the param Sprite s, return coords (array with 2 vals x,y) that are facing opposite direction
	//Since Gazelle can only move orthogonally, this is just finding the bigger of the differences in x or y vals between gazelle and lion
	public int[] oppositeDirectionCoords(Sprite s){
		int[] coords = new int[2];
		if(Math.abs(s.getX()-this.getX()) < Math.abs(s.getY()-this.getY())){
			coords[0] = 0;	//Gazelles can only move orthogonally, so either x or y has to be 0
			coords[1] = s.getY() < this.getY() ? 1 : -1;	//If s is above this sprite, move down, otherwise, move up
		}
		else{
			coords[0] = s.getX() < this.getX() ? 1 : -1;
			coords[1] = 0;
		}
		return coords;
	}


	@Override
	public void move() {
		//Gazelle move away from lions based on MOVE_AWAY_LION_RATIO
		Random r = new Random(seed);
		if(r.nextDouble() < MOVE_AWAY_LION_RATIO){
			int[] oppositeDir = oppositeDirectionCoords(closestLion);
			xa = oppositeDir[0];
			ya = oppositeDir[1];
		}
		else{
			xa = r.nextInt(3)-1;
			ya = r.nextInt(3)-1;
		}
		if (canMove()){
			x = x + xa;
			y = y + ya;
		}

	}

	@Override
	public void update(){
		checkCapture();
		updateClosestLion();
	}

	//Captures this gazelle, removing it from the simulation and crediting the appropriate Lions
	public void checkCapture(){
		
		Sprite[] adjSprites = new Sprite[4];
		adjSprites[0] = grid.getCell(x, y-1);
		adjSprites[1] = grid.getCell(x, y+1);
		adjSprites[2] = grid.getCell(x-1, y);
		adjSprites[3] = grid.getCell(x+1, y);

		//Check if Gazelle is captured: All 4 orthogonally adjacent cells must be a Lion or be out of bounds
		boolean isCaptured = true;

		if(!(y-1 < 0 || (adjSprites[0] != null && adjSprites[0].getType().equals("L"))))	//if cell up is not out of bounds or a Lion
			isCaptured = false;
		if(!(y+1 >= grid.getHeight() || (adjSprites[1] != null && adjSprites[1].getType().equals("L"))))	//if cell down is not out of bounds or a Lion
			isCaptured = false;
		if(!(x-1 < 0 || (adjSprites[2] != null && adjSprites[2].getType().equals("L"))))	//if cell left is not out of bounds or a Lion
			isCaptured = false;
		if(!(x+1 >= grid.getWidth() || (adjSprites[3] != null && adjSprites[3].getType().equals("L"))))	//if cell right is not out of bounds or a Lion
			isCaptured = false;

		if(isCaptured){
			//System.out.println("\n\n\n\n\n\nGAZELLE " + toString() + " CAPTURED!!!\n\n\n\n\n");
			//Credit all lions that captured the gazelle
			for(int i = 0; i < adjSprites.length; i++)
				if(adjSprites[i] != null && adjSprites[i].getType().equals("L"))
					((Lion)adjSprites[i]).setGazelleCaptured(((Lion)adjSprites[i]).getGazelleCaptured()+1);

			grid.setGazelleCaptured(grid.getGazelleCaptured()+1);	//Increment total count of gazelle captured
			grid.addRemoveSprite(this);	//set this gazelle to be removed from sim
		}
	}

	public String toString(){
		if(tag < 10)
			return "G"+tag+" ";
		else
			return "G"+tag;
	}
}
