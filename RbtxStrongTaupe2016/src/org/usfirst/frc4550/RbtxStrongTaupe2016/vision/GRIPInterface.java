package org.usfirst.frc4550.RbtxStrongTaupe2016.vision;

import java.io.IOException;

public class GRIPInterface {
	Process gripInstance;
	ProcessBuilder grip;
	boolean hasFailed;
	public GRIPInterface(String filename){
		grip = new ProcessBuilder("/usr/local/frc/JRE/bin/java -jar ", "grip " + filename + ".jar");
		try {
			gripInstance = grip.start();
			hasFailed = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			hasFailed = true;
		}
		
	}
	public void stop(){
		gripInstance.destroy();
	}
	public void start(){
		try {
			gripInstance = grip.start();
			hasFailed = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			hasFailed = true;
		}
	}
	public boolean isAlive(){
		return gripInstance.isAlive();
	}
	public boolean isFailed(){
		return hasFailed;
	}
}
