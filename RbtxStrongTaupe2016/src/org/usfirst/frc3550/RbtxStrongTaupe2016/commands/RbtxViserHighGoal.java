package org.usfirst.frc3550.RbtxStrongTaupe2016.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

import org.usfirst.frc3550.RbtxStrongTaupe2016.Robot;
import org.usfirst.frc3550.RbtxStrongTaupe2016.subsystems.*;
import org.usfirst.frc3550.RbtxStrongTaupe2016.commands.*;

/**
 *
 */
public class RbtxViserHighGoal extends Command {
	
	public final double HIGH_GOAL_CENTER_HEIGHT = 2.33; // Height in meters.
	public final double HIGH_GOAL_WIDTH = 60; //Width in centimet
	public final double ONE_METER_AT_ONE_METER_DISTANCE = 586; //One meter at one meter distance in pixels
	public final double OPTIMAL_FIRING_DISTANCE = 0.6;
	public final double GYRO_HEADING_FRONT = 200;
	public final double ACCEPTABLE_HIGHGOAL_WIDTH_TO_HEIGHT_RATIO = 0.55; // Ratio de hauteur vs largeur du high-goal
	// accepable, sachant que 0,7 est parfait et que 0.5 est le minimum absolu.
	public final double CAMERA_HORIZONTAL_FOV = 47; //L'angle de vision de la Axis Camera M1011 est de 47 degrees
	
	double[][] highgoalCenter;
	NetworkTable highgoalTable;
	double[] highgoalError;
	double[] defaultValue = new double[0];
	double[] chosenHighgoalCenter;
	double[] chosenHighgoalDimensions;
	double chosenHighgoalArea;
	boolean isFinished;
	
	double gyroAngle;
	
    public RbtxViserHighGoal() {
    	requires(Robot.deplacement);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	// Trouver quel highgoal viser. Il faut stocker son centre et choisir a chaque iteration son equivalent
    	highgoalTable = Robot.highgoalTable;
    	highgoalCenter[0] = highgoalTable.getNumberArray("centerX", defaultValue);
    	highgoalCenter[1] = highgoalTable.getNumberArray("centerY", defaultValue);
    	int i = 0;
    	double[] error = new double[highgoalCenter[0].length];
    	double lowestValue = Robot.CAMERA_RESOLUTION[0]+1;
    	int lowestValueIndex = 0;
    	for(double centerX : highgoalCenter[0]){
    		error[i] = Math.abs(centerX - Robot.CAMERA_CENTRE[0]);
    		if(error[i] < lowestValue){
    			lowestValueIndex = i;
    		}
    		i++;
    	}
    	chosenHighgoalCenter[1] = highgoalCenter[1][lowestValueIndex];
    	chosenHighgoalCenter[2] = highgoalCenter[2][lowestValueIndex];
    	chosenHighgoalDimensions[1] = highgoalTable.getNumberArray("width", defaultValue)[lowestValueIndex];
    	chosenHighgoalDimensions[2] = highgoalTable.getNumberArray("height", defaultValue)[lowestValueIndex];
    	chosenHighgoalArea = highgoalTable.getNumberArray("area", defaultValue)[lowestValueIndex];
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// La premiere etape est de trouver le goal qui a ete choisi.
    	// Il faut donc retrouver la cible la plus similaire
    	
    	Robot.highgoalTable = NetworkTable.getTable("GRIP/highgoal");
    	highgoalCenter[0] = highgoalTable.getNumberArray("centerX", defaultValue);
    	highgoalCenter[1] = highgoalTable.getNumberArray("centerY", defaultValue);
    	int i = 0;
    	double[] error = new double[highgoalCenter[0].length];
    	double lowestValue = Robot.CAMERA_RESOLUTION[0]+1;
    	int lowestValueIndex = 0;
    	for(double centerX : highgoalCenter[0]){
    		error[i] = Math.abs(centerX - chosenHighgoalCenter[0]);
    		if(error[i] < lowestValue){
    			lowestValueIndex = i;
    		}
    		i++;
    	}
    	chosenHighgoalCenter[0] = highgoalCenter[1][lowestValueIndex];
    	chosenHighgoalCenter[1] = highgoalCenter[2][lowestValueIndex];
    	chosenHighgoalDimensions[0] = highgoalTable.getNumberArray("width", defaultValue)[lowestValueIndex];
    	chosenHighgoalDimensions[1] = highgoalTable.getNumberArray("height", defaultValue)[lowestValueIndex];
    	chosenHighgoalArea = highgoalTable.getNumberArray("area", defaultValue)[lowestValueIndex];
    	
    	// Maintenant que nous avons retrouve la cible choisie, il faut voir si sa width est dans la bonne proportion vis a vis heigth
    	// Si elle l'est, alors l'angle est correct.
    	
    	boolean isAngleCorrect = true;
    	boolean isOrientationCorrect = false;
    	
    	
    	
    	double angle = chosenHighgoalDimensions[0] / chosenHighgoalDimensions[1];
    	if (chosenHighgoalDimensions[0] / chosenHighgoalDimensions[1] >= ACCEPTABLE_HIGHGOAL_WIDTH_TO_HEIGHT_RATIO){
    		isAngleCorrect = true;
    	}
    	
    	RbtxAutoDriveWithEncoderAndGyroCommand gyroTurn = new RbtxAutoDriveWithEncoderAndGyroCommand(0, 0, 45);
    	RbtxDriveToDistanceWithEncoders drive = new RbtxDriveToDistanceWithEncoders();
    	
//    	if(!isAngleCorrect){
//    		gyroAngle = Robot.deplacement.getGyroAngle();
//    		if (Math.abs(gyroAngle - GYRO_HEADING_FRONT) < 5){
//    			Robot.highgoalTable = NetworkTable.getTable("GRIP/highgoal");
//    	    	highgoalCenter[0] = highgoalTable.getNumberArray("centerX", defaultValue);
//    	    	highgoalCenter[1] = highgoalTable.getNumberArray("centerY", defaultValue);
//    			chosenHighgoalCenter[0] = highgoalCenter[1][lowestValueIndex];
//    	    	chosenHighgoalCenter[1] = highgoalCenter[2][lowestValueIndex];
//    	    	chosenHighgoalDimensions[0] = highgoalTable.getNumberArray("width", defaultValue)[lowestValueIndex];
//    	    	chosenHighgoalDimensions[1] = highgoalTable.getNumberArray("height", defaultValue)[lowestValueIndex];
//    	    	chosenHighgoalArea = highgoalTable.getNumberArray("area", defaultValue)[lowestValueIndex];
//    	    	
//    	    	if (chosenHighgoalDimensions[0] / chosenHighgoalDimensions[1] >= ACCEPTABLE_HIGHGOAL_WIDTH_TO_HEIGHT_RATIO){
//    	    		isAngleCorrect = true;
//    	    	}
//    		}
//    		else{
//    			gyroTurn = new RbtxAutoDriveWithEncoderAndGyroCommand(0, 0, gyroAngle-GYRO_HEADING_FRONT);
//    			gyroTurn.start();
//    			while (gyroTurn.isRunning()){
//    			}
//    	    	highgoalCenter[0] = highgoalTable.getNumberArray("centerX", defaultValue);
//    	    	highgoalCenter[1] = highgoalTable.getNumberArray("centerY", defaultValue);
//    			Robot.highgoalTable = NetworkTable.getTable("GRIP/highgoal");
//    			chosenHighgoalCenter[0] = highgoalCenter[1][lowestValueIndex];
//    	    	chosenHighgoalCenter[1] = highgoalCenter[2][lowestValueIndex];
//    	    	chosenHighgoalDimensions[0] = highgoalTable.getNumberArray("width", defaultValue)[lowestValueIndex];
//    	    	chosenHighgoalDimensions[1] = highgoalTable.getNumberArray("height", defaultValue)[lowestValueIndex];
//    	    	chosenHighgoalArea = highgoalTable.getNumberArray("area", defaultValue)[lowestValueIndex];
//    	    	
//    	    	if (chosenHighgoalDimensions[0] / chosenHighgoalDimensions[1] >= ACCEPTABLE_HIGHGOAL_WIDTH_TO_HEIGHT_RATIO){
//    	    		isAngleCorrect = true;
//    	    	}
//    		}
//    		if (!isAngleCorrect){
//    			if (chosenHighgoalDimensions[0] - Robot.CAMERA_CENTRE[0] < 0){
//    				gyroTurn = new RbtxAutoDriveWithEncoderAndGyroCommand(0, 0, 45);
//    				gyroTurn.start();
//    				while(gyroTurn.isRunning()){}
//    				drive = new RbtxDriveToDistanceWithEncoders(Math.abs(chosenHighgoalDimensions[0] / chosenHighgoalDimensions[1] - 0.7) / 0.5 * 3);
//    				drive.start();
//    				while(drive.isRunning()){}
//    			}
//    			else{
//    				gyroTurn = new RbtxAutoDriveWithEncoderAndGyroCommand(0, 0, -45);
//    				gyroTurn.start();
//    				while(gyroTurn.isRunning()){
//    				}
//    				drive = new RbtxDriveToDistanceWithEncoders(Math.abs(chosenHighgoalDimensions[0] / chosenHighgoalDimensions[1] - 0.7) / 0.5 * 3);
//    				drive.start();
//    				while(drive.isRunning()){}
//    			}
//    		}
//    	}
    	if(!isOrientationCorrect){
    		if (Math.abs(chosenHighgoalCenter[0] - Robot.CAMERA_CENTRE[0]) < 5){
    			isOrientationCorrect = true;
    		}
    		else {
    			gyroTurn = new RbtxAutoDriveWithEncoderAndGyroCommand(0, 0, chosenHighgoalCenter[0] - Robot.CAMERA_CENTRE[0] / Robot.CAMERA_CENTRE[0] * CAMERA_HORIZONTAL_FOV);
    			gyroTurn.start();
    			while(gyroTurn.isRunning()){}
    		}
    	}
    	if(isOrientationCorrect && isAngleCorrect == true){
    		isFinished = true;
    		end();
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
