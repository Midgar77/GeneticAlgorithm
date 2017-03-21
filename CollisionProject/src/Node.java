import java.util.ArrayList;
import java.util.List;

public class Node {
	private Sprite sprite;
	private Node parent;
	private List<Node> children;
	private int height;


	public Node(Sprite s){
		this.sprite = s;
		height = 0;
		children = new ArrayList<Node>();
	}

	//Updates current height based on height of the child (if child does not have any children, its height would be 0, thus making this height 1 if child is its only child)
	public void addChild(Node child){
		children.add(child);
		if(child.height >= height)
			height = child.height + 1;
		child.parent = this;
	}

	//Returns the child with largest height. Assumes there is at least one child
	public Node getHighestChild(){
		Node highest = children.get(0);
		for(Node n : children)
			if(n.height > highest.height)
				highest = n;
		return highest;
	}

	//Tells the sprite to move (should only be called after collision handling determines it is ok)
	public void move(Grid grid){
		//System.out.println("Moving " + sprite + " at " + sprite.x + " " + sprite.y + " to " + sprite.nextMove()[0] + " " + sprite.nextMove()[1]);
		grid.moveSprite(sprite);
	}

	public String toString(){
		return sprite.getType()+height;
	}
	
	public Sprite getSprite(){
		return sprite;
	}
	
	public List<Node> getChildren(){
		return children;
	}



	public static void main(String[] args) {
		Grid grid = new Grid(20,20);
		/*
		grid.addSprite(new Gazelle(grid, 0, 0, 1, 1));
		grid.addSprite(new Gazelle(grid, 2, 2, 1, 1));
		grid.addSprite(new Gazelle(grid, 4, 4, 1, 1));
		grid.addSprite(new Gazelle(grid, 6, 6, 1, 1));
		grid.addSprite(new Gazelle(grid, 8, 8, 1, 1));
		grid.addSprite(new Gazelle(grid, 10, 10, 1, 1));

		CollisionTree t = new CollisionTree(null);

		List<Sprite> sprites = grid.getSprites();
		Node temp = new Node(sprites.get(0));
		t.getRoot().addChild(temp);

		Node temp2 = new Node(sprites.get(1));
		temp.addChild(temp2);
		temp.addChild(new Node(sprites.get(2)));
		temp = temp2;
		temp2 = new Node(sprites.get(3));
		temp.addChild(temp2);

		temp = new Node(sprites.get(4));
		t.getRoot().addChild(temp);
		temp.addChild(new Node(sprites.get(5)));

		t.moveLongestPath(grid);

		for(Sprite s : grid.getSprites())
			System.out.println(s.getX() + "  " + s.getY());
			
			
			*/

	}
}