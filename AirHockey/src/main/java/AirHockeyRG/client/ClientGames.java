package AirHockeyRG.client;

import AirHockeyRG.model.HockeyStick;
import AirHockeyRG.model.Spacer;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.Cursor;
import java.awt.Robot;
import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.awt.Toolkit;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.Point;

import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.util.Objects;

import javax.swing.JOptionPane;

public class ClientGames {
	private String colorChoice, oppColorChoice;
	private JFrame jFrame;
	private Spacer spacer;
	private JLabel Goal, enemyGoal, Score, enemyScore;
	private HockeyStick userSpacer, enemySpacer;
	private int yourNumGoals = 0, enemyNumGoals = 0;
	private int yourPaddleX, yourPaddleY;
	private Cursor blankCursor;
	private Robot robot;
	private ClientForGame clientForGame;

	public ClientGames(ClientForGame clientForGame, String colorChoice){

		this.colorChoice = colorChoice;
		this.clientForGame = clientForGame;
		oppColorChoice = clientForGame.getEnemyColor(colorChoice);
		setUpConfig();
	}

	public void setUpConfig(){
		try{
			this.robot = new Robot();
		}catch(AWTException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			JOptionPane.showMessageDialog(null, "Could not instantiate robot!", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		
		jFrame = new JFrame("Air Hockey");
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setResizable(false);
		jFrame.setLayout(null);
		jFrame.addMouseMotionListener(new MouseListener());

		BufferedImage cursorImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		this.blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		jFrame.setCursor(blankCursor);

		spacer = new Spacer();
		spacer.setBounds(222- spacer.getRadius(), 286- spacer.getRadius(), 30, 30);
		ImageIcon puckIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("AirHockeyRG/photo/puck.png")));
		spacer.setIcon(puckIcon);

		userSpacer = new HockeyStick();
		ImageIcon userPaddleIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("AirHockeyRG/photo/redPaddle.png")));
		userSpacer.setIcon(userPaddleIcon);
		userSpacer.setBounds(222- userSpacer.getRadius(), 286+200- userSpacer.getRadius(), 50, 50);

		enemySpacer = new HockeyStick();
		ImageIcon opponentPaddleIcon = new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("AirHockeyRG/photo/bluePaddle.png")));
		enemySpacer.setIcon(opponentPaddleIcon);
		enemySpacer.setBounds(222- enemySpacer.getRadius(), 286-255- enemySpacer.getRadius(), 50, 50);

		Goal = new JLabel();
		Goal.setOpaque(true);
		Goal.setBackground(Color.red);
		Goal.setBounds(157, 522, 130, 50);

		
		enemyGoal = new JLabel();
		enemyGoal.setOpaque(true);
		enemyGoal.setBackground(Color.blue);
		enemyGoal.setBounds(157, 0, 130, 50);

		Score = new JLabel(clientForGame.getEnemyName() + ": " + yourNumGoals, JLabel.CENTER);
		Score.setOpaque(false);
		Score.setFont(new Font("Arial Bold", Font.BOLD, 15));
		oppColorChoice = clientForGame.getEnemyColorChoice();
		Score.setForeground(Color.red);
		Score.setBounds(15, 532, 90, 30);


		enemyScore = new JLabel(clientForGame.getName() + ": " + enemyNumGoals, JLabel.CENTER);
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
		jFrame.add(userSpacer);
		jFrame.add(enemySpacer);
		jFrame.add(Score);
		jFrame.add(enemyScore);
		jFrame.add(enemySpacer);
		jFrame.add(Goal);
		jFrame.add(enemyGoal);
		jFrame.add(midline);
		jFrame.add(back);
		jFrame.add(walls);
		jFrame.setSize(450, 600);
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
		setMousePosition();
		run();
	}
	

	public void setMousePosition(){
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = ((int)dimension.getWidth() / 2);
		int y = ((int)dimension.getHeight() / 2) - 186 - userSpacer.getRadius();
		robot.mouseMove(x, y);
	}


	public void run(){
		int puckX = 0, puckY = 0, userPaddleX = 0, userPaddleY = 0;
		while(yourNumGoals < 7 && enemyNumGoals < 7){
			try{
				clientForGame.send(yourPaddleX + " " + yourPaddleY);
			}catch(Exception e){
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				JOptionPane.showMessageDialog(null, "Error with Print Writer!", "ERROR", JOptionPane.ERROR_MESSAGE);
			}

			String input = null;
			try{
				input = clientForGame.readPosition();
			}catch(Exception e){
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				JOptionPane.showMessageDialog(null, "Error with Buffered Reader!", "ERROR", JOptionPane.ERROR_MESSAGE);
			}

			assert input != null;
			if(input.equals("User Goal")){
				goal(enemyGoal);
			}else if(input.equals("Opponent Goal")){
				goal(Goal);
			}else{

				puckX = Integer.parseInt(input.substring(0, input.indexOf(" ")));
				input = input.substring(input.indexOf(" ") + 1);
				puckY = Integer.parseInt(input.substring(0, input.indexOf(" ")));
				input = input.substring(input.indexOf(" ") + 1);


				userPaddleX = Integer.parseInt(input.substring(0, input.indexOf(" ")));
				input = input.substring(input.indexOf(" ") + 1);
				userPaddleY = Integer.parseInt(input);
			}
			

			userSpacer.setBounds(userPaddleX, userPaddleY, 50, 50);
			spacer.setBounds(puckX, puckY, 30, 30);
			enemySpacer.setBounds(yourPaddleX, yourPaddleY, 50, 50);


			try{
				Thread.sleep(10);
			}catch(InterruptedException ignored){}

		}

		JOptionPane.showMessageDialog(null, "Game Over!");
	}
	

	public void goal(JLabel goal){
		
		spacer.setVisible(false);
		spacer.setBounds(0, 0, 30, 30);
		Color normalColor = goal.getBackground();
		

		if(goal.equals(Goal)){
			enemyNumGoals++;
		}else{
			yourNumGoals++;
		}

		enemyScore.setText(clientForGame.getName() + ": " + enemyNumGoals);
		Score.setText(clientForGame.getEnemyName() + ": " + yourNumGoals);
		
		for(int x = 0; x < 25; x++){
			if((x % 2) == 0){
				goal.setBackground(Color.white);
			}else{
				goal.setBackground(normalColor);
			}
			try{
				Thread.sleep(100);
			}catch(InterruptedException e) {
				throw new RuntimeException(e);
			}
		}	
		
		goal.setBackground(normalColor); 
		
		
		spacer.setVisible(true);
		spacer.setBounds(222-15, 286-15, 30, 30);
		userSpacer.setBounds(222-25, 286+200-25, 50, 50);
		enemySpacer.setBounds(222-25, 286-200-25, 50, 50);
		setMousePosition();
	}

	
	private class MouseListener implements MouseMotionListener{

		public void mouseDragged(MouseEvent e){
			mouseEvent(e);
		}

		public void mouseMoved(MouseEvent e){
			mouseEvent(e);
		}

		public void mouseEvent(MouseEvent e){
			
			int mouseX = e.getPoint().x;
			int mouseY = e.getPoint().y;
			
			
			if((mouseX - userSpacer.getRadius()) >= 50 && (mouseX + userSpacer.getRadius()) <= 394 && (mouseY - userSpacer.getDiameter()) >= 50 && mouseY<286){
				jFrame.setCursor(blankCursor);
				yourPaddleX = mouseX - userSpacer.getRadius();
				yourPaddleY = mouseY - userSpacer.getDiameter();
			}else{ 
				Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
				jFrame.setCursor(cursor);
			}
		}
	}
}