package AirHockeyRG.view; 

import AirHockeyRG.client.ClientForGame;
import AirHockeyRG.server.IP;
import AirHockeyRG.server.ServerForGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu {

	private IP ip;
	private JFrame mainMenu;
	private JButton firstButton;
	private JButton SecondButton;
	private String[] IPs;
	private Font font;
	private boolean flag = false;
	private char button = '-';
	private ServerForGame serverForGame;
	private ClientForGame clientForGame;
	
	public MainMenu(){
		
		this.font = new Font("High Tower Text", Font.PLAIN, 66); 
		IPConfig(); 
		setUpMainMenu(); 
	}
	
	public void setUpMainMenu(){

		
		Listener listener = new Listener();

		
		mainMenu = new JFrame("Menu");
		mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainMenu.setResizable(false);
		mainMenu.setLayout(new GridLayout(2, 0));

		
		firstButton = new JButton("Создать");
		firstButton.setBackground(Color.black);
		firstButton.setFont(font);
		firstButton.addActionListener(listener); 

		
		SecondButton = new JButton("Присоедениться");
		SecondButton.setBackground(Color.black);
		SecondButton.setFont(font);
		SecondButton.addActionListener(listener); 

		
		mainMenu.add(firstButton);
		mainMenu.add(SecondButton);
		mainMenu.setSize(620, 420);
		mainMenu.setLocationRelativeTo(null);
		mainMenu.setVisible(true);

		
		this.IPs = ip.getIP();

		
	}
	
	public boolean flag(){return flag;}
	public char gButton(){return button;}
	public ServerForGame serverForGame(){return this.serverForGame;}
	public ClientForGame clientForGame(){return this.clientForGame;}
	
	public void IPConfig(){
		
		this.ip = new IP();
		ip.config();
	}
	
	
	private class Listener implements ActionListener{
		
		public void actionPerformed(ActionEvent event){
			
			if(event.getSource().equals(firstButton)){ 
				
				String yourName = JOptionPane.showInputDialog(null, "Введите имя", "Имя", JOptionPane.QUESTION_MESSAGE);

				serverForGame = new ServerForGame(yourName);
				
				mainMenu.setTitle("Подключение...");
				firstButton.setText("Подключение. . .");
				SecondButton.setText("Поиск игроков. . .");
				SecondButton.setFont(new Font("High Tower Text", Font.PLAIN, 44));
				firstButton.setEnabled(false);
				SecondButton.setEnabled(false);
				mainMenu.update(mainMenu.getGraphics());
				
				serverForGame.connected();

				mainMenu.setTitle("Подключено!");
				firstButton.setText("Игрок найден!");
				SecondButton.setText("Имя игрока: " + serverForGame.getEnemyName());
				mainMenu.update(mainMenu.getGraphics());
				try{
					Thread.sleep(5000);
				}catch(InterruptedException interrupted) {
					throw new RuntimeException(interrupted);
				}

				mainMenu.setVisible(false);
				flag = true;
				button = 'h';
				
			}else if(event.getSource().equals(SecondButton)){ 
				
				String yourName = JOptionPane.showInputDialog(null, "Введите имя", "Имя", JOptionPane.QUESTION_MESSAGE);
				clientForGame = new ClientForGame(IPs, yourName);
				
				mainMenu.setTitle("Подключение...");
				firstButton.setText("Подключение. . .");
				SecondButton.setText("Поиск игроков. . .");
				SecondButton.setFont(new Font("High Tower Text", Font.PLAIN, 44));
				firstButton.setEnabled(false);
				SecondButton.setEnabled(false);
				mainMenu.update(mainMenu.getGraphics());
				
				
				clientForGame.connect();
				
				if(clientForGame.isConnected()){
					mainMenu.setTitle("Подключено!");
					firstButton.setText("Игрок найден!!");
					SecondButton.setText("Имя игрока: " + clientForGame.getEnemyName());
					mainMenu.update(mainMenu.getGraphics());

					try{
						Thread.sleep(5000);
					}catch(InterruptedException interrupted) {
						throw new RuntimeException(interrupted);
					}
					mainMenu.setVisible(false);
					flag = true;
					button = 'j';
				
				}else{
					mainMenu.setTitle("Проблемы с подключением...");
					firstButton.setText("Игрок не найден");
					firstButton.setFont(new Font("High Tower Text", Font.PLAIN, 44));
					SecondButton.setText("Попробуйте снова");
					SecondButton.setFont(new Font("High Tower Text", Font.PLAIN, 25));
					mainMenu.update(mainMenu.getGraphics());
				}
			}
		}
	}
}
