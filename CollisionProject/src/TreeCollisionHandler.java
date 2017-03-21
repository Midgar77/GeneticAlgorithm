import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TreeCollisionHandler {

	//Key is created by using hashKey function that makes an int hash based on x and y
	private static HashMap<Integer, ArrayList<Sprite>> map;	//HashMap that stores the int that is hashed based on coords of each sprite's desired next location as key, sprite list as val
	private static HashSet<Integer> roots;	//HashSet of hash keys of locations of roots


	/*
	 * Used to handle collisions for all sprites in param 'grid'. This CollisionHandler uses the 'Groves Tree-Method' of 2D collision handling.
	 * Will create a CollisionTree for each chain of collisions, and then will call 'moveLongestPath()' on each CollisionTree to tell the sprites they can move (sets canMove to true).
	 */
	public static void handleCollisions(Grid grid){
		List<Sprite> sprites = grid.getSprites();
		map = new HashMap<Integer, ArrayList<Sprite>>();
		roots = new HashSet<Integer>();

		buildHashMap(sprites, grid);
		for(Integer k : map.keySet()){
			int[] coords = reverseHash(k);
			Sprite s = grid.getCell(coords[0], coords[1]);
			if(s == null || s.isStationary()) roots.add(k);
		}


		//System.out.println("Collision chains: " + roots.size());
		//System.out.println(map);

		//System.out.println(roots);
		for(Integer n : roots){
			int[] coords = reverseHash(n);
			CollisionTree t = createCollisionTree(grid, coords[0], coords[1]);
			t.moveLongestPath(grid);
		}

		for(Sprite s : sprites)
			s.setCollisionProcessed(false);

	}

	
	//Builds HashMap AND puts every sprite that is moving into empty cell or staying stationary into the 'roots' arraylist
	//Sprites that remain stationary are NOT added to HashMap (because of the createCollisionTree algorithm)
	public static void buildHashMap(List<Sprite> sprites, Grid grid){
		int key;
		int stationary = 0;
		ArrayList<Sprite> vals;
		for(Sprite s : sprites){
			key = hashKey(s.nextMove());
			if(!s.isStationary()){
				if(map.get(key) == null){
					vals = new ArrayList<Sprite>();
					map.put(key, vals);
				}
				map.get(key).add(s);
			}
			else{
				stationary++;
			}
		}

		//System.out.println("Number of stationary sprites: " + stationary);
	}



	/*Creates and returns a CollisionTree given input: x,y of location of a guaranteed root
	 * There are 2 scenarios for what is in the x,y location:
	 *   1.  is stationary sprite, which means it is in the root of the tree and its children are all sprites wishing to move to its location
	 *   2.  empty location, which means the root is empty and S is a child, and the other children of root are sprites wishing to move into this location
	 */
	public static CollisionTree createCollisionTree(Grid grid, int x, int y){
		CollisionTree t;
		Sprite s = grid.getCell(x, y);

		if(s != null){
			t = new CollisionTree(s);
			s.setCollisionProcessed(true);
		}
		else
			t = new CollisionTree(null);

		int[] location = {x, y};
		Node root = t.getRoot();
		t.setRoot(addChildren(root, location));
		return t;
	}


	//Recursive function that returns given node after adding all the appropriate children to that node, then recurses on each child
	//Base case is that given location of node has no children that can be added to node
	public static Node addChildren(Node root, int[] rootLocation){
		ArrayList<Sprite> children = map.get(hashKey(rootLocation));

		if(children == null || children.isEmpty()) return root;	//base case

		for(Sprite s : children){
			if(!s.isCollisionProcessed()){
				s.setCollisionProcessed(true);
				root.addChild(addChildren(new Node(s), s.getCoordinates()));
			}
		}

		return root;
	}

	public static int hashKey(int[] coords){
		return (coords[0] * 1000) + coords[1];	//Key hash value = (x*1000)+y
	}

	public static int[] reverseHash(int hashKey){
		int[] coords = {hashKey/1000, hashKey%1000};
		return coords;
	}


}
