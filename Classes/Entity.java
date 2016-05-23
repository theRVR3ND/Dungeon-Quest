//Any hero or monster

import java.awt.Graphics;

public abstract class Entity{
	
	private final String name;				//Name of this entity
	private byte health;						//Life value of entity
	private byte r, c;						//Position in panel's board
	private int x, y;							//Position on screen (for gliding)
	
	//--Initialize--//
	
	public Entity(){
		name = "";
		r = 0;
		c = 0;
		health = 1;
		x = c * 60 + Panel.boardX + 22;
		y = r * 60 + Panel.boardY + 17;
	}
	
	//ARGS: name is name of Entity, (r, c) is position of Entity
	public Entity(String name, byte r, byte c){
		this.name = name;
		this.r = r;
		this.c = c;
		health = 1;
		x = c * 60 + Panel.boardX + 22;
		y = r * 60 + Panel.boardY + 17;
	}
	
	//ARGS: name is name of this Entity (Hero or Monster character name)
	public Entity(String name){
		this.name = name;
		r = 0;
		c = 0;
		health = 1;
		x = Panel.boardX + 22;
		y = Panel.boardY + 22;
	}
	
	//--Graphics--//
	
	//pre: g != null, imgDir != null
	/*
		post: Draws Entity at (x, y) in graphics, while changing
				x and y if not matching desired (r, c) position
	*/
	public void draw(String imgDir, Graphics g){
		//Glide towards wanted location
   	if(x < c * 60 + Panel.boardX + 22)
			x++;
		else if(x > c * 60 + Panel.boardX + 22)
			x--;
   	if(y < r * 60 + Panel.boardY + 17)
			y++;
		else if(y > r * 60 + Panel.boardY + 17)
			y--;
			
		g.drawImage(DungeonQuest.loadImage(imgDir + name + ".png"), x, y, null);
   }
	
   //--Access--//
	
	//pre:
	//post: Returns name of this Entity
	public String getName(){
		return name;
	}
	
	public byte getHealth(){
		return health;
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
   
   //pre:
   //post: Returns x location of hero
   public int getX(){
      return x;
   }
   
   //pre:
   //post: Returns y location of hero
   public int getY(){
      return y;
   }
   
	//--Mutate--//
	
	//pre:
	//post: Sets x value to x
	public void setX(int x){
		this.x = x;
	}
	
	//pre:
	//post: Sets y value to y
	public void setY(int y){
		this.y = y;
	}
	
	//pre:
	//post: Sets this.health to health
	public void setHealth(byte health){
		this.health = health;
	}
	
   //pre:
   //post: Changes health value by change
   public void changeHealth(byte change){
		health += change;
   }
	
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
}