package org.usfirst.frc3550.RbtxStrongTaupe2016.subsystems;

import org.usfirst.frc3550.RbtxStrongTaupe2016.RobotMap;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc3550.RbtxStrongTaupe2016.Robot;
import org.usfirst.frc3550.RbtxStrongTaupe2016.commands.*;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 *The RbtxDeplacementSubsystem incorporates the sensors and actuators attached to
 * the robots chassis. These include four drive motors, a left and right encoder
 * and a gyro. It reads in information about it's speed and position.
 *
 */
public class RbtxDeplacementSubsystem extends PIDSubsystem {

	private static final double Kp_gyro    = .008; 
	private static final double Ki_gyro    = 0.000; 
	private static final double Kd_gyro    = 0.00;
	
	private double         angleSetpoint   = 0.0;  // gyro setpoint angle to ensure the robot drives straight
    //final double                   pGain   = .009; //gain proportionnel du gyro
	 //gyro calibration constant, may need to be adjusted; 
    //gyro value of 360 is set to correspond to one full revolution
    final double voltsPerDegreePerSecond = .0125; //change from 0.009 to 0.0085

	public static final double   TOUR      = 0.325; // sonar output voltage when the robot is at the shooting position from the target
	public static final double   TOUR2     = 0.8; // for testing purpose only
	
	
	public static final double SLOWSPEED    = 0.75;  //drive motor slow speed
	public static final boolean SENSITIVITY = true; // drive motor sensibility
	
	// Subsystem devices
	private RobotDrive  m_drive        = RobotMap.drive;
	private AnalogInput m_rangefinder  = RobotMap.forwardSonar;
	private AnalogGyro  m_gyro         = RobotMap.gyro;
	private Encoder     m_leftEncoder  = RobotMap.encodeurDeplacementGauche;
	private Encoder     m_rightEncoder = RobotMap.encodeurDeplacementDroit;
	private Servo       m_panServo     = RobotMap.servoPan;
	private Servo       m_TiltServo    = RobotMap.servoTilt;
	
	
	
	// Initialize your subsystem here
	public RbtxDeplacementSubsystem() {
		super("RbtxDeplacementSubsystem", Kp_gyro, Ki_gyro, Kd_gyro);
		setAbsoluteTolerance(0.2);
		getPIDController().setContinuous(false);
		LiveWindow.addActuator("RbtxDeplacementSubsystem", "GyPID Controller", getPIDController());
		// Use these to get going:
		getPIDController().setSetpoint(angleSetpoint);
		// setSetpoint() - Sets where the PID controller should move the system
		// to
		// enable(); //Enables the PID controller.
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		//setDefaultCommand(new RbtxArcadeDriveCommand());
		setDefaultCommand(new RbtxGyroDriveCommand());
		//setDefaultCommand(new RbtxTankDriveCommand());
	}

 // Gyro PID part of the subsystem
	/**
     * Use the gyroscope as the PID sensor. This method is automatically
     * called by the subsystem.
     * 
	 * @return The current angle of the drivetrain.
	 */
	protected double returnPIDInput() {
		// Return your input value for the PID loop
		// e.g. a sensor, like a potentiometer:
		// yourPot.getAverageVoltage() / kYourMaxVoltage;
		//double currentHeading = m_gyro.getAngle();
		SmartDashboard.putNumber("gyro input position", m_gyro.getAngle());
		return m_gyro.getAngle();
		
	}
	
	 /**
     * Use the drive train motors  as the PID output. This method is automatically called by
     * the subsystem.
     */
	protected void usePIDOutput(double output) {
		// Use output to drive your system, like a motor
		// e.g. yourMotor.set(output);
		
		//double turningValue =  (angleSetpoint - m_gyro.getAngle())*pGain;
		//double turningValue =  (angleSetpoint - m_gyro.getAngle());
		if (Robot.oi.getgamePadPiloteY() <= 0){
			//forwards
			m_drive.drive(Robot.oi.getgamePadPiloteY(), output);
			//m_drive.drive(0.5, output); // test for an autonomous mode with gyro
			SmartDashboard.putNumber("Y value1", Robot.oi.getgamePadPiloteY());
			SmartDashboard.putNumber("gyro position1", output);
		}
		else {
			//backwards
			m_drive.drive(Robot.oi.getgamePadPiloteY(), -output);
			//m_drive.drive(0.5, -output); // test for an autonomous mode with gyro
			SmartDashboard.putNumber("Y value2", Robot.oi.getgamePadPiloteY());
			SmartDashboard.putNumber("gyro position2", -output);
		}
	}
	
//End of the Gyro PID part of the subsystem
	
	
	/**
	 * The log method puts interesting information to the SmartDashboard.
	 */
	public void log() {
		SmartDashboard.putNumber("Left Distance", m_leftEncoder.getDistance());
		SmartDashboard.putNumber("Right Distance", m_rightEncoder.getDistance());
		SmartDashboard.putNumber("Left Speed", m_leftEncoder.getRate());
		SmartDashboard.putNumber("Right Speed", m_rightEncoder.getRate());
		SmartDashboard.putNumber("Gyro", m_gyro.getAngle());
	}
	
	// Start of sensor readings

	/**
	 * @return The Ultrasonic getting the distance from the target.
	 */
	
	public double getDistanceToTarget(){
		   return m_rangefinder.getVoltage();	
	}
	
	/**
	 * @return The Ultrasonic getting the distance from the target.
	 */
	
	public void resetSonar(){
		 m_rangefinder.resetAccumulator();	
	}
	
	/**
	 * @return The getGyroPidOutput wich is the ratevalue from the PID
	 * This method is used in RbtxAutoDriveWithEncoderAndGyroCommand
	 */
	
	public double getGyroAngle(){
		   return m_gyro.getAngle();	
	}
	
	/**
	 * @return The encoder getting the distance and speed of left side of the drivetrain.
	 */
	public Encoder getLeftEncoder() {
		return m_leftEncoder;
	}

	/**
	 * @return The encoder getting the distance and speed of right side of the drivetrain.
	 */
	public Encoder getRightEncoder() {
		return m_rightEncoder;
	}
	
	/**
	 * Reset the robots sensors to the zero states.
	 */
	public void reset() {
		m_gyro.reset();
		m_leftEncoder.reset();
		m_rightEncoder.reset();
	}

	/**
	 * @return The distance driven (average of left and right encoders).
	 */
	public double getDistance() {
		return ( m_leftEncoder.getDistance() + m_rightEncoder.getDistance())/2;
	}

	//End of sensor readings
	/**
	 * stop method is used to stop the robot 
	 * 
	 * @param no input parameter
	 */
	public void stop() {
		m_drive.drive(0, 0);
	}
	
	/**
	 * driveStraight method is used in autonomous mode to let the robot goes straight
	 * 
	 * @param speed The speed that the robot should drive in the y direction in range [-1.0..1.0]
	 */
	public void driveStraight(double speed) {
		// Configure the RobotDrive to reflect the fact that all our motors are
	    // wired backwards and our drivers sensitivity preferences.
		RobotMap.moteurDeplacementAvantGauche.setInverted(true);
		RobotMap.moteurDeplacementArriereGauche.setInverted(true);
		RobotMap.moteurDeplacementAvantDroite.setInverted(true);
		RobotMap.moteurDeplacementArriereDroite.setInverted(true);
		m_drive.drive(speed, 0);
	}
	
	/**
	 * drive method provides a way to explicitly choose the joystick or gamePad axis in order to operate the robot
	 * 
	 * @param moveValue The speed that the robot should drive in the y direction in range [-1.0..1.0]
	 * @param rotateValue The rate of rotation for the robot that is dependent of the translation. [-1.0..1.0]
	 */
	public void drive(double moveValue, double rotateValue) {
		//double moveValue = Robot.oi.getgamePadPiloteY();
		//double rotateValue = Robot.oi.getgamePadPiloteX();
		
		// Configure the RobotDrive to reflect the fact that all our motors are
		// wired backwards and our drivers sensitivity preferences.
		RobotMap.moteurDeplacementAvantGauche.setInverted(true);
		RobotMap.moteurDeplacementArriereGauche.setInverted(true);
		RobotMap.moteurDeplacementAvantDroite.setInverted(true);
		RobotMap.moteurDeplacementArriereDroite.setInverted(true);
		m_drive.arcadeDrive(moveValue, rotateValue, SENSITIVITY);
	}
	
	/**
	 * inverseDrive method provides a way to explicitly choose the joystick or gamePad axis in order to operate the robot. 
	 * It provides the pilot with the ability to switch front and rear of the robot for a better maneuverability.
	 * 
	 * @param moveValue The speed that the robot should drive in the y direction in range [-1.0..1.0]
	 * @param rotateValue The rate of rotation for the robot that is dependent of the translation. [-1.0..1.0]
	 */
	public void inverseDrive(double moveValue, double rotateValue) {
		//double moveValue = Robot.oi.getgamePadPiloteY();
		//double rotateValue = Robot.oi.getgamePadPiloteX();
		
		// Configure the RobotDrive to reflect the fact that all our motors are
		// wired backwards and our drivers sensitivity preferences.
		RobotMap.moteurDeplacementAvantGauche.setInverted(false);
		RobotMap.moteurDeplacementArriereGauche.setInverted(false);
		RobotMap.moteurDeplacementAvantDroite.setInverted(false);
		RobotMap.moteurDeplacementArriereDroite.setInverted(false);
		m_drive.arcadeDrive(moveValue, -1 * rotateValue, SENSITIVITY);
	}
	
	/**
	 * driveSlow method provides a way to explicitly choose the joystick or gamePad axis in order to operate the robot
	 * It provides the pilot with the ability to move swiftly in small areas
	 * 
	 * @param moveValue The speed that the robot should drive in the y direction in range [-1.0..1.0]*SLOWSPEED
	 * @param rotateValue The rate of rotation for the robot that is dependent of the translation. [-1.0..1.0]*SLOWSPEED
	 */
	public void driveSlow(double moveValue, double rotateValue) {
		//double moveValue = Robot.oi.getgamePadPiloteY();
		//double rotateValue = Robot.oi.getgamePadPiloteX();
		
		// Configure the RobotDrive to reflect the fact that all our motors are
		// wired backwards and our drivers sensitivity preferences.
		RobotMap.moteurDeplacementAvantGauche.setInverted(true);
		RobotMap.moteurDeplacementArriereGauche.setInverted(true);
		RobotMap.moteurDeplacementAvantDroite.setInverted(true);
		RobotMap.moteurDeplacementArriereDroite.setInverted(true);
		m_drive.arcadeDrive(SLOWSPEED*moveValue, SLOWSPEED*rotateValue, SENSITIVITY);
	}
	
	/**
	 * inverseDriveSlow method provides a way to explicitly choose the joystick or gamePad axis in order to operate the robot. 
	 * It provides the pilot with the ability to switch front and rear of the robot for a better maneuverability
	 * Its purpose is to provide the pilot with the additional ability to to move swiftly in small areas
	 * 
	 * @param moveValue The speed that the robot should drive in the y direction in range [-1.0..1.0]*SLOWSPEED
	 * @param rotateValue The rate of rotation for the robot that is dependent of the translation. [-1.0..1.0]*SLOWSPEED
	 */	
	public void inverseDriveSlow(double moveValue, double rotateValue) {
		//double moveValue = Robot.oi.getgamePadPiloteY();
		//double rotateValue = Robot.oi.getgamePadPiloteX();
		
		// Configure the RobotDrive to reflect the fact that all our motors are
		// wired backwards and our drivers sensitivity preferences.
		RobotMap.moteurDeplacementAvantGauche.setInverted(false);
		RobotMap.moteurDeplacementArriereGauche.setInverted(false);
		RobotMap.moteurDeplacementAvantDroite.setInverted(false);
		RobotMap.moteurDeplacementArriereDroite.setInverted(false);
		m_drive.arcadeDrive(SLOWSPEED*moveValue, -1*SLOWSPEED*rotateValue, SENSITIVITY);
	}
	
	/**
	 * driveTank method provides a way to use gamePad axis in order to operate the robot on a tank drive
	 * This method was developed on Malcom's request
	 * 
	 * @param leftStick The value of the left stick on the gamePadPilote in range [-1.0..1.0]
	 * @param rightStick The value of the right stick on the gamePadPilote in range [-1.0..1.0]
	 */
	public void driveTank(double leftStick, double rightStick) {
		//leftStick = Robot.oi.getgamePadPiloteYLeft();
		//rightStick = Robot.oi.getgamePadPiloteYRight();
		
		// Configure the RobotDrive to reflect the fact that all our motors are
		// wired backwards and our drivers sensitivity preferences.
		RobotMap.moteurDeplacementAvantGauche.setInverted(true);
		RobotMap.moteurDeplacementArriereGauche.setInverted(true);
		RobotMap.moteurDeplacementAvantDroite.setInverted(true);
		RobotMap.moteurDeplacementArriereDroite.setInverted(true);
		//m_drive.arcadeDrive(SLOWSPEED*moveValue, SLOWSPEED*rotateValue, SENSITIVITY);
		m_drive.tankDrive(leftStick, rightStick, SENSITIVITY);
	}
	
	/**
	 * inversedriveTank method provides a way to use gamePad axis in order to operate the robot on a tank drive
	 * This method was developed on Malcom's request
	 * It provides the pilot with the ability to switch front and rear of the robot for a better maneuverability
	 * 
	 * @param leftStick The value of the left stick on the gamePadPilote in range [-1.0..1.0]
	 * @param rightStick The value of the right stick on the gamePadPilote in range [-1.0..1.0]
	 */
	public void inverseTankDrive(double leftStick, double rightStick) {
		//leftStick = Robot.oi.getgamePadPiloteYLeft();
		//rightStick = Robot.oi.getgamePadPiloteYRight();
		
		// Configure the RobotDrive to reflect the fact that all our motors are
		// wired backwards and our drivers sensitivity preferences.
		RobotMap.moteurDeplacementAvantGauche.setInverted(false);
		RobotMap.moteurDeplacementArriereGauche.setInverted(false);
		RobotMap.moteurDeplacementAvantDroite.setInverted(false);
		RobotMap.moteurDeplacementArriereDroite.setInverted(false);
		m_drive.tankDrive(rightStick, leftStick, SENSITIVITY);
	}
	
	/**
	 * SlowTankDrive method provides a way to use gamePad axis in order to operate the robot on a tank drive
	 * This method was developed on Malcom's request
	 * It provides the pilot with the ability to switch front and rear of the robot for a better maneuverability
	 * Its purpose is to provide the pilot with the additional ability to to move swiftly in small areas
	 * 
	 * @param leftStick The value of the left stick on the gamePadPilote in range [-1.0..1.0]*SLOWSPEED
	 * @param rightStick The value of the right stick on the gamePadPilote in range [-1.0..1.0]*SLOWSPEED
	 */
	public void SlowTankDrive(double leftStick, double rightStick) {
		//leftStick = Robot.oi.getgamePadPiloteYLeft();
		//rightStick = Robot.oi.getgamePadPiloteYRight();
		
		// Configure the RobotDrive to reflect the fact that all our motors are
		// wired backwards and our drivers sensitivity preferences.
		RobotMap.moteurDeplacementAvantGauche.setInverted(true);
		RobotMap.moteurDeplacementArriereGauche.setInverted(true);
		RobotMap.moteurDeplacementAvantDroite.setInverted(true);
		RobotMap.moteurDeplacementArriereDroite.setInverted(true);
		//m_drive.arcadeDrive(SLOWSPEED*moveValue, SLOWSPEED*rotateValue, SENSITIVITY);
		m_drive.tankDrive(leftStick*SLOWSPEED, rightStick*SLOWSPEED, SENSITIVITY);
	}
	
	/**
	 * SlowTankDrive method provides a way to use gamePad axis in order to operate the robot on a tank drive
	 * This method was developed on Malcom's request
	 * It provides the pilot with the ability to switch front and rear of the robot for a better maneuverability
	 * Its purpose is to provide the pilot with the additional ability to to move swiftly in small areas
	 * 
	 * @param leftStick The value of the left stick on the gamePadPilote in range [-1.0..1.0]*SLOWSPEED
	 * @param rightStick The value of the right stick on the gamePadPilote in range [-1.0..1.0]*SLOWSPEED
	 */
	public void inverseSlowTankDrive(double leftStick, double rightStick) {
		//leftStick = Robot.oi.getgamePadPiloteYLeft();
		//rightStick = Robot.oi.getgamePadPiloteYRight();
		
		// Configure the RobotDrive to reflect the fact that all our motors are
		// wired backwards and our drivers sensitivity preferences.
		RobotMap.moteurDeplacementAvantGauche.setInverted(false);
		RobotMap.moteurDeplacementArriereGauche.setInverted(false);
		RobotMap.moteurDeplacementAvantDroite.setInverted(false);
		RobotMap.moteurDeplacementArriereDroite.setInverted(false);
		m_drive.tankDrive(rightStick*SLOWSPEED, leftStick*SLOWSPEED, SENSITIVITY);
	}
}
