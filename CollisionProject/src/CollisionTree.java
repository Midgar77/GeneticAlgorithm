import java.util.ArrayList;
import java.util.List;

public class CollisionTree {

	/*
	 * 
	 * A Tree structure (not binary) that maintains the height of each node of the tree (0-based) and represents a collision chain.
	 * Node B is a child of Node A iff the sprite of Node B wants to move into the sprite of Node A's location.
	 * If the Tree's root is a Node not null, then the root is a Node with a sprite that is remaining stationary (xa,ya = 0, frozen lion, near boundaries).
	 * This means that all nodes' sprites will have to remain stationary.
	 * Otherwise, root will be null, and the sprites of the Nodes in the longest path of the tree will be able to move. 
	 * 
	 */

	private Node root;
	private int size;

	public CollisionTree(Sprite rootSprite) {
		root = new Node(rootSprite);
	}

	//Links current root as child to param parent so that this tree becomes a subtree of the tree that parent is in
	public void becomeSubtree(Node parent){
		parent.addChild(root);
	}

	//Finds the longest path in the tree and tells each of the nodes in that path to move
	public void moveLongestPath(Grid grid){
		Node temp = root;
		if(temp.getSprite() != null)
 			return;	//If root of tree is not null, than there are no empty spaces and all sprites in Tree cannot move!
		
		
		//Root of tree should not have to move since it is either null or it would just be a stationary sprite
		while(!temp.getChildren().isEmpty()){
			temp = temp.getHighestChild();
			temp.move(grid);
		}
	}

	public Node getRoot(){
		return root;
	}

	public void setRoot(Node root){
		this.root = root;
	}

	public int getSize(Node root, int size){
		List<Node> children = root.getChildren();
		if(children.isEmpty()) return size;	//base case
		for(Node n : children)
			size = getSize(n, size+1);
		return size;
	}

	//NEEDS FIXED
	public String toString(){
		String ans = "";
		if(root.getSprite() == null) ans += "Null: ";
		else ans += root.toString() + ": ";

		ans += "with " + getSize(root, 1) + " nodes";
		return ans;
	}
	
	
	public static void main(String[] args) {
		CollisionTree t = new CollisionTree(null);
		t.root.addChild(new Node(null));
		System.out.println(t);
		Node curr = new Node(null);
		t.root.addChild(curr);
		System.out.println(t);
		curr.addChild(new Node(null));
		curr.addChild(new Node(null));
		curr.addChild(new Node(null));
		Node n1 = new Node(null);
		curr.addChild(n1);
		n1.addChild(new Node(null));
		n1.addChild(new Node(null));
		t.root.addChild(new Node(null));
		System.out.println(t);
	}

}