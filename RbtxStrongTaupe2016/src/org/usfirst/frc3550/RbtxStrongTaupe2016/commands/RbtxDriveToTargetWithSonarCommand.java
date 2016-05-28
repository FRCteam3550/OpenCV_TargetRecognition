package org.usfirst.frc3550.RbtxStrongTaupe2016.commands;

import org.usfirst.frc3550.RbtxStrongTaupe2016.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *This command drives the robot tower the target using a sonar
 *It uses a local PID controller with the pidInput provided by the sonar
 *The PID output is provided to the driveStraight method of the displacement subsystem
 *The sonar is declared in the RbtxDeplacementSubsystem
 *The PID setpoint is the distance (volt unit as read from the sonar) to the target
 *The setpoint can vary depending of the actual distance (see IO file)
 */
public class RbtxDriveToTargetWithSonarCommand extends Command {
	
	private PIDController PIDSonar;
	double setpoint;
	
	private static final double Kp_sonar = 1.98; // 2.35 3
	private static final double Ki_sonar = 0.0058; // 0.01
	private static final double Kd_sonar = 0.0001;
	
	public static final double TOUR = 0.25;
	public static final double TOUR2 = 0.8;
	
	private double currentVoltage;

    public RbtxDriveToTargetWithSonarCommand(double setpoint) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.deplacement);
    	this.setpoint = setpoint;
    	
    	PIDSonar = new PIDController(Kp_sonar, Ki_sonar, Kd_sonar,
                new PIDSource() {
                    PIDSourceType m_sourceType = PIDSourceType.kDisplacement;

                    public double pidGet() {
                    	currentVoltage = Robot.deplacement.getDistanceToTarget();
                        return Robot.deplacement.getDistanceToTarget();
                    }

                    @Override
                    public void setPIDSourceType(PIDSourceType pidSource) {
                      m_sourceType = pidSource;
                    }

                    @Override
                    public PIDSourceType getPIDSourceType() {
                        return m_sourceType;
                    }
                },
                new PIDOutput() { public void pidWrite(double output) {
                	
                	double speed = output;
            		if (speed > 0.8)
            			speed = 0.8;
            		if (currentVoltage <= TOUR+0.04)
            			speed = 0.5;
            		
            		//Robot.deplacement.driveStraight(speed);
            		Robot.deplacement.drive(speed,(Robot.deplacement.getGyroAngle()*.009));
                    
                }});
    	PIDSonar.setAbsoluteTolerance(0.01);
    	//PIDSonar.setContinuous(false);
    	//PIDSonar.setSetpoint(setpoint);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    
    	PIDSonar.reset();
    	PIDSonar.setSetpoint(setpoint);
    	PIDSonar.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//these values are for debugging purpose only
    	//SmartDashboard.putNumber("position PIDSonar lue: ", PIDSonar.get());
    	//SmartDashboard.putNumber("position globale: ", Robot.deplacement.getPosition());
    	//SmartDashboard.putNumber("setpoint PIDSonar: ", setpoint);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	SmartDashboard.putNumber("difference PIDSonar : ", Math.abs(PIDSonar.get()-setpoint));
    	return Math.abs(Robot.deplacement.getDistanceToTarget()-setpoint) < 0.02;
    }

    // Called once after isFinished returns true
    protected void end() {
    	PIDSonar.disable();
    	PIDSonar.disable();
    	Robot.deplacement.driveStraight(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
