package org.usfirst.frc3550.RbtxStrongTaupe2016.commands;

import org.usfirst.frc3550.RbtxStrongTaupe2016.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * This command drives the robot with a given speed using a tank drive
 * It was created to test the group command feature. See RbtxAutoCompoundCommand
 */
public class RbtxAutoForwardCommand extends Command {

    public RbtxAutoForwardCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.deplacement);
    	//setTimeout(15);
    }
    //setTimeout(15);
    // Called just before this Command runs the first time
    protected void initialize() {
    	setTimeout(2);
    	//Robot.deplacement.driveAuto(-0.6, -0.6);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.deplacement.driveTank(-0.6, -0.6);
    	//Robot.deplacement.driveAuto(-0.6, -0.6);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.deplacement.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
