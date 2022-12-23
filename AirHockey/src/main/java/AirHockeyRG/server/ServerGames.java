package AirHockeyRG.server; 

import AirHockeyRG.model.HockeyStick;
import AirHockeyRG.model.Spacer;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Point;
import java.awt.Font;
import java.awt.Robot;
import java.awt.Cursor;
import java.awt.AWTException;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Objects;

import javax.swing.JOptionPane;


public class ServerGames {
	private String c1;
	private String c2;
	private JFrame jFrame;
	private Spacer spacer;
	private JLabel goal;
	private JLabel enemyGoal;
	private JLabel score;
	private JLabel enemyScore;
	private HockeyStick enemyStick;
	private HockeyStick stic;
	private double speed;
	private double dir;
	private double x;
	private double y;
	private double dirx;
	private int stickX;
	private int stickY;
	private int userSpacerX;
	private int userSpacerY;
	private int opponentSpacerX;
	private int opponentSpacerY;
	private int scoreNow = 0;
	private int enemyScorenow = 0;
	private int speedNow = 0;
	private int enemySpeedNow = 0;
	private Point xy = new Point(0, 0);
	private Cursor cursor;
	private Robot robot;
	private ServerForGame serverForGame;
	private final double dr = 1.0035;
	private final double ds = 1.00022;
	private final int diametr = 50;
	
	public ServerGames(ServerForGame serverForGame, String colorChoice){
		this.c1 = colorChoice;
		this.serverForGame = serverForGame;
		c2 = serverForGame.getColor(colorChoice);
		setUpServerGame();
	}
	
	public void setUpServerGame(){
		speed = 0;
		dirx = 1;
		dir = 1;
		
		try{
			this.robot = new Robot();
		}catch(AWTException e){}
		
		jFrame = new JFrame("Air Hockey");
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setResizable(false);
		jFrame.setLayout(null);
		jFrame.addMouseMotionListener(new MouseListener());
		
		BufferedImage cursorImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		this.cursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		jFrame.setCursor(cursor);
		
		spacer = new Spacer();
		spacer.setBounds(222- spacer.getRadius(), 286- spacer.getRadius(), 30, 30);
		ImageIcon puckIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("AirHockeyRG/photo/puck.png")));
		spacer.setIcon(puckIcon);
		
		stic = new HockeyStick();
		ImageIcon userPaddleIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("AirHockeyRG/photo/redPaddle.png")));
		stic.setIcon(userPaddleIcon);
		stic.setBounds(222- stic.getRadius(), 286+200- stic.getRadius(), diametr, diametr);
		
		enemyStick = new HockeyStick();
		ImageIcon opponentPaddleIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("AirHockeyRG/photo/bluePaddle.png")));
		enemyStick.setIcon(opponentPaddleIcon);
		enemyStick.setBounds(222- enemyStick.getRadius(), 286-200- enemyStick.getRadius(), diametr, diametr);
		
		goal = new JLabel();
		goal.setOpaque(true);
		goal.setBackground(Color.red);
		goal.setBounds(157, 522, 130, 50);
		
		enemyGoal = new JLabel();
		enemyGoal.setOpaque(true);
		c2 = serverForGame.getOppColorChoice();
		enemyGoal.setBackground(Color.blue);
		enemyGoal.setBounds(157, 0, 130, 50);
		
		score = new JLabel(serverForGame.getName() + ": " + scoreNow, JLabel.CENTER);
		score.setOpaque(false);
		score.setFont(new Font("Arial Bold", Font.BOLD, 15));
		score.setForeground(Color.red);
		score.setBounds(15, 532, 90, 30);
		
		enemyScore = new JLabel(serverForGame.getEnemyName() + ": " + enemyScorenow, JLabel.CENTER);
		enemyScore.setOpaque(false);
		enemyScore.setFont(new Font("Arial Bold", Font.BOLD, 15));
		enemyScore.setForeground(Color.blue);
		enemyScore.setBounds(15, 10, 90, 30);
		
		JLabel back = new JLabel();
		back.setBounds(50, 50, 344, 472);
		ImageIcon icon = new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("AirHockeyRG/photo/white.png")));
		back.setIcon(icon);
		
		
		
		JLabel midline = new JLabel();
		midline.setOpaque(true);
		midline.setBackground(new Color(10,10,10));
		midline.setBounds(50,276,344,20);
		
		JLabel walls = new JLabel();
		walls.setBounds(0, 0, 444, 572);
		ImageIcon wallsIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("AirHockeyRG/photo/back.png")));
		walls.setIcon(wallsIcon);
		
		jFrame.add(spacer);
		jFrame.add(stic);
		jFrame.add(enemyStick);
		jFrame.add(goal);
		jFrame.add(enemyGoal);
		jFrame.add(score);
		jFrame.add(enemyScore);
		jFrame.add(midline);
		jFrame.add(back);
		jFrame.add(walls);
		jFrame.setSize(450, 600);
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
		MouseXY();
		play();
	}
	
	public void MouseXY(){
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = ((int)dimension.getWidth() / 2);
		int y = ((int)dimension.getHeight() / 2) + 186 - stic.getRadius();
		robot.mouseMove(x, y);
	}
	
	public void play(){
		while(scoreNow < 7 && enemyScorenow < 7){
			String input = serverForGame.readPosition();
			if(checkGoal()){
				serverForGame.send("User Goal");
				goal(enemyGoal);
			}else if(checkEnemyGoal()){
				serverForGame.send("Opponent Goal");
				goal(goal);
			}else{
				try{
					serverForGame.send(stickX + " " + stickY + " " + userSpacerX + " " + userSpacerY);
				}catch(Exception e){
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
					JOptionPane.showMessageDialog(null, "Error with Print Writer!", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			opponentSpacerX = Integer.parseInt(input.substring(0, input.indexOf(" ")));
			opponentSpacerY = Integer.parseInt(input.substring(input.indexOf(" ") + 1));
			
			countingEnemySpacerSpeed();
			
			stickX = spacer.getX();
			stickY = spacer.getY();
			
			wallCol();
			move();
			wallCol();
			
			spacer.setBounds(stickX, stickY, 30, 30);
			stic.setBounds(userSpacerX, userSpacerY, diametr, diametr);
			enemyStick.setBounds(opponentSpacerX, opponentSpacerY, diametr, diametr);

			speed /= dr;
			
			try{
				Thread.sleep(10);
			}catch(InterruptedException e) {
				throw new RuntimeException(e);
			}
			

		}
		

		
		JOptionPane.showMessageDialog(null, "Игра Закончена!");
	}
	
	public void countingEnemySpacerSpeed(){
		int opponentPaddleCX  = opponentSpacerX + enemyStick.getRadius();
		int opponentPaddleCY = opponentSpacerY + enemyStick.getRadius();
		Point currentPoint = new Point(opponentPaddleCX, opponentPaddleCY);
		enemySpeedNow = (int)((currentPoint.distance(xy)) / ds);
		xy = currentPoint;
	}
	

	
	public void goal(JLabel goal){
		spacer.setVisible(false);
		spacer.setBounds(0, 0, 30, 30);
		Color normalColor = goal.getBackground();
		
		if(goal.equals(this.goal)){
			enemyScorenow++;
		}else{
			scoreNow++;
		}
		enemyScore.setText(serverForGame.getEnemyName() + ": " + enemyScorenow);
		score.setText(serverForGame.getName() + ": " + scoreNow);
		
		for(int x = 0; x < 25; x++){
			if((x % 2)== 0){
				goal.setBackground(Color.white);
			}else{
				goal.setBackground(normalColor);
			}
			
			try{
				Thread.sleep(100);
			}catch(InterruptedException e){}
		}
		goal.setBackground(normalColor);
		
		
		spacer.setVisible(true);
		spacer.setBounds(222-15, 286-15, 30, 30);
		stic.setBounds(222-25, 286+200-25, 50 ,50);
		enemyStick.setBounds(222-25, 286-200-25, 50, 50);
		speed = 0;
		MouseXY();
	}
	
	public boolean checkGoal(){
		
		if(spacer.getY() <= 50){
			return (spacer.getCX() - spacer.getRadius()) >= 157 && (spacer.getCX() + spacer.getRadius()) <= 287;
		}
			
		return false;
	}
	
	public boolean checkEnemyGoal(){
		
		if((spacer.getY() + spacer.getDiameter()) >= 521){
			return (spacer.getCX() - spacer.getRadius()) >= 157 && (spacer.getCX() + spacer.getRadius() <= 287);
		}
		
		return false;
		
	}
	
	public void step(){
		x = speed * dirx;
		y = speed * dir;
		
		stickX += (int) x;
		stickY += (int) y;
		
		wallCol();
	}
	
	public void move(){
		
		step();
		if(userSpacerCollision()){
			
			dirx = (spacer.getCX() - stic.getCX()) / (Math.sqrt(((spacer.getCX() - stic.getCX()) * (spacer.getCX() - stic.getCX())) + ((spacer.getCY() - stic.getCY()) * (spacer.getCY() - stic.getCY()))));
			dir = (spacer.getCY() - stic.getCY()) / (Math.sqrt(((spacer.getCX() - stic.getCX()) * (spacer.getCX() - stic.getCX())) + ((spacer.getCY() - stic.getCY()) * (spacer.getCY() - stic.getCY()))));
			if(speedNow < 4){
				speed += speedNow;
			}else speed = Math.min(speedNow, 13);
			step();
		}else if(enemySpacerCollision()){
			
			dirx = (spacer.getCX() - enemyStick.getCX()) / (Math.sqrt(((spacer.getCX() - enemyStick.getCX()) * (spacer.getCX() - enemyStick.getCX())) + ((spacer.getCY() - enemyStick.getCY()) * (spacer.getCY() - enemyStick.getCY()))));
			dir = (spacer.getCY() - enemyStick.getCY()) / (Math.sqrt(((spacer.getCX() - enemyStick.getCX()) * (spacer.getCX() - enemyStick.getCX())) + ((spacer.getCY() - enemyStick.getCY()) * (spacer.getCY() - enemyStick.getCY()))));
			if(enemySpeedNow < 4){
				speed += enemySpeedNow;
			}else if(speedNow <= 13){
				speed = enemySpeedNow;
			}else{
				speed = 13;
			}
			step();
		}
		
	}
	
	
	public boolean userSpacerCollision(){
		
		double distance = Math.sqrt((spacer.getCX() - stic.getCX()) * (spacer.getCX() - stic.getCX()) + ((spacer.getCY() - stic.getCY()) * (spacer.getCY() - stic.getCY())));
		return distance <= (spacer.getRadius() + stic.getRadius());
	}
	
	public boolean enemySpacerCollision(){
		
		double distance = Math.sqrt((spacer.getCX() - enemyStick.getCX()) * (spacer.getCX() - enemyStick.getCX()) + ((spacer.getCY() - enemyStick.getCY()) * (spacer.getCY() - enemyStick.getCY())));
		return distance <= (spacer.getRadius() + enemyStick.getRadius());
	}
	
	public void wallCol(){
		
		if(spacer.getX() <= 50 && spacer.getY() <= 50){ 
			dirx = Math.abs(dirx);
			dir = Math.abs(dir);
		}else if(spacer.getX() <= 50 && (spacer.getY() + spacer.getDiameter()) >= 522){ 
			dirx = Math.abs(dirx);
			dir = -Math.abs(dir);
		}else if((spacer.getX() + spacer.getDiameter()) >= 394 && spacer.getY() <= 50){ 
			dirx = -Math.abs(dirx);
			dir = Math.abs(dir);
		}else if((spacer.getX() + spacer.getDiameter()) >= 394 && (spacer.getY() + spacer.getDiameter()) >= 522){ 
			dirx = -Math.abs(dirx);
			dir = -Math.abs(dir);
		}else if(spacer.getX() <= 50 /*left wall border*/){
			dirx = Math.abs(dirx);
		}else if((spacer.getX() + spacer.getDiameter()) >= 394 /*right wall border*/){
			dirx = -Math.abs(dirx);
		}else if(spacer.getY() <= 50 /*top wall border*/){
			dir = Math.abs(dir);
		}else if((spacer.getY() + spacer.getDiameter()) >= 522 /*bottom wall border*/){
			dir = -Math.abs(dir);
		}
	}
	
	private class MouseListener implements MouseMotionListener{

		private Point userPreviousPoint = new Point(0, 0);
		
		public void mouseDragged(MouseEvent e) {
			mouseEvent(e);
		}
		
		public void mouseMoved(MouseEvent e) {
			mouseEvent(e);
		}
		
		public void mouseEvent(MouseEvent e){
			
			int mouseX = e.getPoint().x;
			int mouseY = e.getPoint().y;
			
			if((mouseX - stic.getRadius()) >= 50 && (mouseX + stic.getRadius()) <= 394 && (mouseY - stic.getDiameter()) >= 50 && ((mouseY) >= 332 && (mouseY)<=522)){
				jFrame.setCursor(cursor);
				userSpacerX = mouseX - stic.getRadius();
				userSpacerY = mouseY - stic.getDiameter();
			}else{
				Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
				jFrame.setCursor(cursor);
			}
			
			speedNow = (int)((e.getPoint().distance(userPreviousPoint)) / ds);
			
			userPreviousPoint = e.getPoint();
		}
	}
}
