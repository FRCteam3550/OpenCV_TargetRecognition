package org.usfirst.frc3550.RbtxStrongTaupe2016.commands;


import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc3550.RbtxStrongTaupe2016.Robot;

/**
 * This command drives the robot over a given distance with simple proportional
 * control This command will drive a given distance limiting to a maximum speed.
 */
public class RbtxDriveToDistanceWithEncoders extends Command {
	
	private double driveForwardSpeed;
	private double distance;
	private double error;
	private final double TOLERANCE = .1;
	private final double KP = -1.0 / 5.0;

	public RbtxDriveToDistanceWithEncoders() {
		this(10, 0.5);
	}

	public RbtxDriveToDistanceWithEncoders(double dist) {
		this(dist, 0.5);
	}

    public RbtxDriveToDistanceWithEncoders(double dist, double maxSpeed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.deplacement);
    	distance = dist;
    	driveForwardSpeed = maxSpeed;
    }


	protected void initialize() {
		Robot.deplacement.getRightEncoder().reset();
		setTimeout(2);
	}

	protected void execute() {
		error = (distance - Robot.deplacement.getRightEncoder().getDistance());
		if (driveForwardSpeed * KP * error >= driveForwardSpeed) {
			Robot.deplacement.driveTank(driveForwardSpeed, driveForwardSpeed);
		} else {
			Robot.deplacement.driveTank(driveForwardSpeed * KP * error,
					driveForwardSpeed * KP * error);
		}
	}

	protected boolean isFinished() {
		return (Math.abs(error) <= TOLERANCE) || isTimedOut();
	}

	protected void end() {
		Robot.deplacement.stop();
	}

	protected void interrupted() {
		end();
	}
}
