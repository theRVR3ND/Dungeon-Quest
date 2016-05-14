//A button capable of being clicked and drawn with Graphics

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Button{

	private BufferedImage img;							//Image of this button
	private boolean down;								//Is this buttons clicked ("down")?
	private int x,											//x position of this button
					y;											//y position of this button

	//--Initialize--//

	public Button(String imgName, int x, int y){
		img = DungeonQuest.loadImage(imgName);
		down = false;
		this.x = x;
		this.y = y;
	}
	
	public Button(BufferedImage img, int x, int y){
		this.img = img;
		down = false;
		this.x = x;
		this.y = y;
	}
	
	//--Graphics--//
	
	public void draw(Graphics g){
		if(down)//When mouse over or down
			g.drawImage(img, x, y, null);
		else//If not down, shrink slightly
			g.drawImage(img,
							x + (int)(img.getWidth() * 0.025), 
							y + (int)(img.getHeight() * 0.025), 
							(int)(img.getWidth() * 0.95), 
							(int)(img.getHeight() * 0.95), null);
	}
	
	//--Access--//
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public boolean isDown(){
		return down;
	}
	
	//--Mutate--//
	
	public boolean checkClick(int x, int y){
		if(this.x <= x && this.x + img.getWidth() >= x &&
			this.y <= y && this.y + img.getHeight() >= y){
			down = !down;//Change state of button
			return true;
		}else
			return false;
	}
}