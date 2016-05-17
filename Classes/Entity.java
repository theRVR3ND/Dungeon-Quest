//Any hero or monster

import java.awt.Graphics;

public abstract class Entity{
	
	private final String name;				//Name of this entity
	private byte r, c;						//Position in panel's board
	private int x, y;							//Position on screen (for gliding)
	
	//--Initialize--//
	
	public Entity(){
		name = "";
		r = 0;
		c = 0;
		x = c * 60 + Panel.boardX + 22;
		y = r * 60 + Panel.boardY + 17;
	}
	
	//ARGS: name is name of Entity, (r, c) is position of Entity
	public Entity(String name, byte r, byte c){
		this.name = name;
		this.r = r;
		this.c = c;
		x = c * 60 + Panel.boardX + 22;
		y = r * 60 + Panel.boardY + 17;
	}
	
	public Entity(String name){
		this.name = name;
		r = 0;
		c = 0;
		x = Panel.boardX + 22;
		y = Panel.boardY + 22;
	}
	
	//--Graphics--//
	
	public void draw(String imgDir, Graphics g){
		//Glide towards wanted location
   	if(x < c * 60 + Panel.boardX + 22)
			x++;
		else if(x > c * 60 + Panel.boardX + 22)
			x--;
   	else if(y < r * 60 + Panel.boardY + 17)
			y++;
		else if(y > r * 60 + Panel.boardY + 17)
			y--;
			
		g.drawImage(DungeonQuest.loadImage(imgDir), x, y, null);
   }
	
	//--Mutate--//
	
	//pre: 0 <= r < 13, 0 <= c < 9
   //post: Sets Hero position to (r, c), also sets x and y
   public void setPosition(byte r, byte c){
      this.r = r;
      this.c = c;
      
		x = c * 60 + Panel.boardX + 22;
		y = r * 60 + Panel.boardY + 17;
   }
	
	//pre: 
	//post: Changes r and c based on rMove and cMove, but does not change x, y graphic position
	public void move(byte rMove, byte cMove){
		r += rMove;
		c += cMove;
	}
	
	//--Access--//
	
	//pre:
	//post:
	public String getName(){
		return name;
	}
	
	//pre:
	//post: Returns true if (x, y) position have "glided" to match (r, c)
	public boolean doneGliding(){
		return x == c * 60 + Panel.boardX + 22 && y == r * 60 + Panel.boardY + 17;
	}
	
	//pre:
	//post: Returns row position of Hero on Panel grid 
	public byte getRow(){
		return r;
	}
	
  	//pre:
	//post: Returns column position of Hero on Panel grid 
	public byte getColumn(){
		return c;
	}
}