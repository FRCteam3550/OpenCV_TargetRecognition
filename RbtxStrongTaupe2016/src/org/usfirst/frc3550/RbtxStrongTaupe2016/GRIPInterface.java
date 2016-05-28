package org.usfirst.frc3550.RbtxStrongTaupe2016;

import java.io.IOException;

public class GRIPInterface {
	GRIPInterface(String filename){
		ProcessBuilder p = new ProcessBuilder("/usr/local/frc/JRE/bin/java " + filename + ".grip");
		Process grip;
		try {
			grip = p.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
