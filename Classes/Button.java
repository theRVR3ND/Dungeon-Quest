//A button capable of being clicked and drawn with Graphics

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Button{

	private BufferedImage img;							//Image of this button
	private boolean mouseOver;							//Is this mouse on top of this button?
	private boolean down;								//Is this buttons clicked ("down")?
	private int x,											//x position of this button
					y;											//y position of this button

	//--Initialize--//

	public Button(String imgName, int x, int y){
		img = DungeonQuest.loadImage(imgName);
		mouseOver = false;
		down = false;
		this.x = x;
		this.y = y;
	}
	
	public Button(BufferedImage img, int x, int y){
		this.img = img;
		mouseOver = false;
		down = false;
		this.x = x;
		this.y = y;
	}
	
	//--Graphics--//
	
	public void draw(Graphics g){
		if(! mouseOver && ! down)
			g.drawImage(img, x, y, null);
		else//When mouse over, expand slightly (by 5%)
			g.drawImage(img,
							x - (int)(img.getWidth() * 0.035), 
							y - (int)(img.getHeight() * 0.035), 
							(int)(img.getWidth() * 1.07), 
							(int)(img.getHeight() * 1.07), null);
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
	
	public void checkMouseOver(int x, int y){
		if(this.x <= x && this.x + img.getWidth() >= x &&
			this.y <= y && this.y + img.getHeight() >= y)
			mouseOver = true;
		else
			mouseOver = false;
	}
	
	public boolean checkClick(int x, int y){
		if(this.x <= x && this.x + img.getWidth() >= x &&
			this.y <= y && this.y + img.getHeight() >= y){
			down = !down;//Change state of button
			return true;
		}else
			return false;
	}
}