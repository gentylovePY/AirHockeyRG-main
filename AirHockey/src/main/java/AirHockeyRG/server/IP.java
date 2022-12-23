package AirHockeyRG.server; 

import AirHockeyRG.Play;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IP {

	private String[] IPs; 
	private final int numberOfComputers = 254;
	
	
	public void config(){
		boolean flag = false;

		if(Play.IPComputer != null){
			flag = true;
		}
		
		try{
			InetAddress localHost = InetAddress.getLocalHost();
			String thisComputerIP = localHost.toString(); 
			thisComputerIP = thisComputerIP.substring(thisComputerIP.indexOf("/") + 1); 
			String baseIP = thisComputerIP.substring(0, thisComputerIP.lastIndexOf(".")+1); 

			if(!flag) {
				IPs = new String[numberOfComputers]; 
			}else{
				IPs = new String[numberOfComputers + 1];
				IPs[numberOfComputers] = Play.IPComputer;
			}
			
			
			for(int x = 0; x < numberOfComputers; x++){
				
				IPs[x] = baseIP + (x+1);
			}

		}catch(UnknownHostException e){ 
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
			JOptionPane.showMessageDialog(null, "Error getting host!", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public String[] getIP(){return IPs;} 
}


