import java.util.Random;

public class Hunter extends Sprite{
	
	private final double MOVE_TOWARDS_LION_RATIO = 0.30;

	public Hunter(Grid grid, int x, int y, int xa, int ya, int tag, int seed){
		super(grid, "H", x, y, xa, ya, tag, seed);
	}


	@Override
	public void move() {
		Random r = new Random(seed);
		
		//Hunters move towards lions based on MOVE_TOWARDS_LION_RATIO
		if(r.nextDouble() < MOVE_TOWARDS_LION_RATIO){
			int[] moveDir = moveDirectionCoords(closestLion);
			xa = moveDir[0];
			ya = moveDir[1];
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
		checkFreezeLion();
		updateClosestLion();
	}

	//Lion is frozen if a hunter tries to move into its cell
	public void checkFreezeLion(){
		if(grid.getCell(this.nextMove()[0], this.nextMove()[1]) != null && grid.getCell(this.nextMove()[0], this.nextMove()[1]).getType().equals("L")){
			((Lion)grid.getCell(this.nextMove()[0], this.nextMove()[1])).setFrozen(true);
		}
	}

	public String toString(){
		if(tag < 10)
			return "H"+tag+" ";
		else
			return "H"+tag;
	}

}
