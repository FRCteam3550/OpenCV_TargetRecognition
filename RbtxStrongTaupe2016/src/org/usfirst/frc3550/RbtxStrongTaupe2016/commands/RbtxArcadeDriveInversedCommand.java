package org.usfirst.frc3550.RbtxStrongTaupe2016.commands;

import org.usfirst.frc3550.RbtxStrongTaupe2016.Robot;
import org.usfirst.frc3550.RbtxStrongTaupe2016.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 * this command signifies that the arcade drive is inversed (the front part 
 * of the robot changes roles with the rear part) 
 * 
 */
public class RbtxArcadeDriveInversedCommand extends Command {

    public RbtxArcadeDriveInversedCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.deplacement);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.deplacement.inverseDrive(Robot.oi.getgamePadPiloteY(),Robot.oi.getgamePadPiloteX());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
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
