//A button capable of being clicked and drawn with Graphics

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Button{

	private BufferedImage img;							//Image of this button
	private boolean down;								//Is this buttons clicked ("down")?
	private int x,											//x position of this button
					y;											//y position of this button

	//--Initialize--//

	/*
		ARGS: imgName is name of button's image.
		Button is positioned at (x, y) in JFrame.
	*/
	public Button(String imgName, int x, int y){
		img = DungeonQuest.loadImage(imgName);
		down = false;
		this.x = x;
		this.y = y;
	}
	
	/*
		ARGS: img is button's graphical representation. 
		Button is positioned at (x, y) in JFrame.
	*/
	public Button(BufferedImage img, int x, int y){
		this.img = img;
		down = false;
		this.x = x;
		this.y = y;
	}
	
	//--Graphics--//
	
	//pre: g != null
	//post: Draws button at (x, y), with size based on click state
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
	
	//pre:
	//post: Returns x position (in JFrame) of button
	public int getX(){
		return x;
	}
	
	//pre:
	//post: Returns y position (in JFrame) of button
	public int getY(){
		return y;
	}
	
	//pre:
	//post: Returns true if button is clicked "down", false otherwise
	public boolean isDown(){
		return down;
	}
	
	//--Mutate--//
	
	//pre:
	/*
		post: Returns true if (x, y) is inside area of button.
				Changes up/down state of button based on click.
	*/
	public boolean checkClick(int x, int y){
		if(this.x <= x && this.x + img.getWidth() >= x &&
			this.y <= y && this.y + img.getHeight() >= y){
			down = !down;//Change state of button
			return true;
		}else
			return false;
	}
}