package org.usfirst.frc3550.RbtxStrongTaupe2016.commands;

import org.usfirst.frc3550.RbtxStrongTaupe2016.Robot;
import org.usfirst.frc3550.RbtxStrongTaupe2016.subsystems.*;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;

/**
 *This command sets and maintains the optimal speed of the shooter motor
 *It uses a local PID controller with the pidInput provided by actual shooter motor
 *The PID output is provided back as a motor setvalue parameter
 *The shooter motor is declared in the RbtxAccelerateurSubsystem
 *The PID setpoint is the optimal launch speed obtained via trial and error
 *It can vary depending on the target distance (to be discussed with the team)
 */
public class RbtxSetShooterMaxSpeedPIDCommand extends Command {
	private PIDController PIDShooterSpeed;
	double setpointMaxSpeed;
	
	private static final double Kp_shooterSpeed = 1.98; // to adjust with the real values
	private static final double Ki_shooterSpeed = 0.0058; // to adjust with the real values
	private static final double Kd_shooterSpeed = 0.0001; // to adjust with real values
	
	
	private double currentSpeed; //for debugging purpose only
	
	

    public RbtxSetShooterMaxSpeedPIDCommand(double setpointMaxSpeed) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.accelerateur);
    	this.setpointMaxSpeed = setpointMaxSpeed;
    	
    	PIDShooterSpeed = new PIDController(Kp_shooterSpeed, Ki_shooterSpeed, Kd_shooterSpeed, 
    		new PIDSource() {
    		PIDSourceType m_sourceType = PIDSourceType.kDisplacement;
    		
			@Override
			public void setPIDSourceType(PIDSourceType pidSource) {
				// TODO Auto-generated method stub
				m_sourceType = pidSource;
				
			}
			
			@Override
			public double pidGet() {
				// TODO Auto-generated method stub
				currentSpeed = Robot.accelerateur.getCurrentShooterSpeed();// for debugging purpose can be deleted once the code is validated
				return Robot.accelerateur.getCurrentShooterSpeed();
			}
			
			@Override
			public PIDSourceType getPIDSourceType() {
				// TODO Auto-generated method stub
				 return m_sourceType;
			}
		}, new PIDOutput() {
			
			@Override
			public void pidWrite(double output) {
				// TODO Auto-generated method stub
			   Robot.accelerateur.setCurrentShooterSpeed(output);	
			}
		});
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	PIDShooterSpeed.reset();
    	PIDShooterSpeed.setSetpoint(setpointMaxSpeed);
    	PIDShooterSpeed.enable();
    	setTimeout(8); //the command runs for 8 seconds but this value will be adjusted through proper testing
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	PIDShooterSpeed.disable();
    	PIDShooterSpeed.disable();
    	Robot.accelerateur.setCurrentShooterSpeed(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
