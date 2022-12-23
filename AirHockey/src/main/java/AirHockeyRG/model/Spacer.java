package AirHockeyRG.model; 

import javax.swing.JLabel;

public class Spacer extends JLabel{
	private final int diameter = 30;
	public int getRadius(){return diameter/2;} 
	public int getDiameter(){return diameter;}
	public int getCX(){return (this.getBounds().x + (diameter / 2));} 
	public int getCY(){return (this.getBounds().y + (diameter / 2));}
	public int getX(){return this.getBounds().x;} 
	public int getY(){return this.getBounds().y;} 


}
