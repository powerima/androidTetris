/*
 * 	2012. 11. 22
 * 
 *  Tetris 플레이어정보
 * 
 */
package android.tetris;

public class PlayerInfo {
	String name;
	int id;
	int level;
	int score;	
	
	boolean isGrid;
	boolean isColorful;	//게임 진행을 위한 정보	
	int nextShape;		//다음 블럭
	int nextRotation;	//다음 블럭의 회전모양
	
	

	PlayerInfo(){
		
	}
	
	PlayerInfo(String name, boolean isColorful, boolean isGrid){
		this.name = name;
		this.isColorful = isColorful;
		this.isGrid = isGrid;
	}
	
	PlayerInfo(int id, String name, int level, int score){
		this.id = id;
		this.name = name;
		this.level = level;
		this.score = score;
	}
	
		
	public int getId() {
		return id;
	}

	public boolean isGrid() {
		return isGrid;
	}
	public void setGrid(boolean isGrid) {
		this.isGrid = isGrid;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNextShape() {
		return nextShape;
	}

	public void setNextShape(int nextShape) {
		this.nextShape = nextShape;
	}

	public int getNextRotation() {
		return nextRotation;
	}

	public void setNextRotation(int nextRotation) {
		this.nextRotation = nextRotation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isColorful() {
		return isColorful;
	}

	public void setColorful(boolean isColorful) {
		this.isColorful = isColorful;
	}
	
}
