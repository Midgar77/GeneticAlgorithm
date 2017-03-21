import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Sprite {

	protected Grid grid;
	protected int width, height;
	protected int x, y, xa, ya;
	protected File img;
	protected boolean canMove;	//Strictly for collision handling. Does not handle out of bounds or frozen lions
	protected Lion closestLion;
	protected String type;
	protected boolean collisionProcessed;
	protected int tag;
	protected int seed;

	BufferedImage sprite;

	public Sprite(Grid grid, String spriteType, int x, int y, int xa, int ya, int tag, int seed) {
		type = spriteType;
		//setSpriteImage();	
		setSpriteData(grid, x, y, xa, ya, tag, seed);
	}

	public void setSpriteData(Grid grid, int x, int y, int xa, int ya, int tag, int seed){
		this.x = x;
		this.y = y;
		this.xa = xa;
		this.ya = ya;
		this.grid = grid;
		canMove = true;
		closestLion = null;
		collisionProcessed = false;
		this.tag = tag;
		this.seed = seed;
	}

	public void setSpriteImage(){
		switch(type) {
		case "L":
			img = new File("sprites/lion.png");
			break;
		case "H":
			img = new File("sprites/hunter.png");
			break;
		case "G":
			img = new File("sprites/gazelle.png");
			break;
		case "F":
			img = new File("sprites/gazelleFrozen.png");	//this should be a frozen lion, not gazelle
			break;
		}
		try {
			sprite = ImageIO.read(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void update(){
		updateClosestLion();
	}

	public void updateClosestLion(){
		this.closestLion = grid.closestLion(this);
	}

	//Given the param Sprite s, return coords (array with 2 vals x,y) that are facing direction of s
	public int[] moveDirectionCoords(Sprite s){
		int[] coords = new int[2];

		if(s == null){
			coords[0] = 0;
			coords[1] = 0;
			return coords;
		}

		coords[0] = s.getX() < this.getX() ? -1 : 1;
		coords[1] = s.getY() < this.getY() ? -1 : 1;
		return coords;
	}

	public void move() {

		if (canMove()){
			x = x + xa;
			y = y + ya;
		}


	}

	//Returns coordinates (array with 2 elements: x and y) of this Sprite's desired next move (y+ya, x+xa)
	public int[] nextMove(){
		int[] nextMove = {x+xa, y+ya};
		int[] stationary = {x,y};
		if(canMove())
			return nextMove;
		else
			return stationary;
	}

	//Returns 2 length int array with x and y value
	public int[] getCoordinates(){
		int[] coords = {x, y};
		return coords;
	}

	public boolean isStationary(){
		int[] coords = nextMove();
		return (coords[0] == 0 && coords[1] == 0) || !canMove;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public int getXa(){
		return xa;
	}

	public int getYa(){
		return ya;
	}

	public void setXa(int xa){
		this.xa = xa;
	}

	public void setYa(int ya){
		this.ya = ya;
	}

	public String getType(){
		return type;
	}

	public int getTag(){
		return tag;
	}
	
	public int getSeed(){
		return seed;
	}

	public void setSeed(int seed){
		this.seed = seed;
	}

	public void setCollisionProcessed(boolean processed){
		this.collisionProcessed = processed;
	}

	public boolean isCollisionProcessed(){
		return this.collisionProcessed;
	}

	public boolean canMove(){
		if (x+xa >= 0 && x+xa < grid.getWidth() && y+ya >= 0 && y+ya < grid.getHeight())	//regardless of canMove ivar, if next move is out of bounds, cant move
			return canMove;
		else
			return false;
	}

	public void setCanMove(boolean canMove){
		this.canMove = canMove;
	}

	public String toString(){
		return "S"+tag;
	}



}