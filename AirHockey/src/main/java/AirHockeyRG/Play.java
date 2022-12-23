package AirHockeyRG; 

import AirHockeyRG.client.ClientGames;
import AirHockeyRG.server.ServerGames;

import AirHockeyRG.view.MainMenu;

import javax.swing.JOptionPane;

public class Play {
	public static String firstColor = "Red";
	public static String SecondColor = "Blue";
	public static String ntclr = "";
	public static String IPComputer = null;
	
	public static void main(String[] args){
		if(args.length == 1) {
			IPComputer = args[0];
		}
		twoPlayer();
	}

	
	public static void twoPlayer(){
		
		
		MainMenu mainMenu = new MainMenu();
		
		while(!mainMenu.flag()){
			
			try{
				Thread.sleep(100);
			}catch(InterruptedException e) {
				throw new RuntimeException(e);
			}
		}



		switch(mainMenu.gButton())
		{
		case 'h': if(!ntclr.equals("")){
			firstColor = ntclr;}
		new ServerGames(mainMenu.serverForGame(), firstColor);
		break;
		case 'j': if(!ntclr.equals("")){
			SecondColor = ntclr;}
		new ClientGames(mainMenu.clientForGame(), SecondColor);
		break;
		default: JOptionPane.showMessageDialog(null, "Error with two-player menu!", "ERROR", JOptionPane.ERROR_MESSAGE);
		break;
		}
		System.exit(0);
	}
}
