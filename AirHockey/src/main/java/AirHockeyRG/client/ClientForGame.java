package AirHockeyRG.client; 

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientForGame {
	private Socket socket;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private String[] IPs;
	private boolean connected = false;
	private String yourName, opponentName, opponentColor;
	private final int timeout = 100;


	public ClientForGame(String[] IPs, String name){
		this.IPs = IPs;
		this.yourName = name;
	}
	
	
	public void connect(){
		int cnt = 0;
		for(int x = 0; x < IPs.length; x++){
			try{
				socket = new Socket();
				SocketAddress address = new InetSocketAddress(IPs[x], 63400);
				socket.connect(address, timeout);
				if(socket.isConnected()){
					printWriter = new PrintWriter(socket.getOutputStream(), true);
					bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					opponentName = bufferedReader.readLine();
					printWriter.println(yourName);
					this.connected = true;
					break;
				}
			} catch(IOException e) {

			}
			if(x==IPs.length-1){
				if(cnt == 0){
					x = 0;
					cnt = 1;
				}
			}
		}
	}
	
	
	
	public String getName(){return yourName;}
	public String getEnemyName(){return opponentName;}
	public String getEnemyColorChoice(){return opponentColor;}
	public boolean isConnected(){return this.connected;}
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
	
	public String getEnemyColor(String yourColor){
		
		try{
			opponentColor = bufferedReader.readLine();
			printWriter.println(yourColor);
			return opponentColor;
		}catch(IOException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			JOptionPane.showMessageDialog(null, "Error with PW or BR!", "ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
}
