package AirHockeyRG.server; 

import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import javax.swing.JOptionPane;

public class ServerForGame {

	private ServerSocket serverSocket;
	private Socket socket;
	private PrintWriter printWriter;
	private BufferedReader bufferedReader;
	private String yourName, opponentName, oppColorChoice;
	
	
	public ServerForGame(String yourName){
		this.yourName = yourName;
	}
	
	
	public void connected(){
		
		try{
			
			
			serverSocket = new ServerSocket(63400);
			socket = serverSocket.accept();
			printWriter = new PrintWriter(socket.getOutputStream(), true);
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printWriter.println(yourName);
			opponentName = bufferedReader.readLine();
			
		}catch(IOException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			JOptionPane.showMessageDialog(null, "Error with server connection!", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	
	public String getName(){return yourName;}
	public String getEnemyName(){return opponentName;}
	public String getOppColorChoice(){return oppColorChoice;}
	
	
	public void send(String input){
		printWriter.println(input);}
	
	
	public String readPosition(){
		
		try{
			return bufferedReader.readLine();
		}catch(IOException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			JOptionPane.showMessageDialog(null, "Error with Buffered Reader!", "ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	public String getColor(String yourColor){
		try{
			printWriter.println(yourColor);
			return (oppColorChoice = bufferedReader.readLine());
		}catch(IOException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			JOptionPane.showMessageDialog(null, "Error with PW or BR!", "ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
}
