package Rbtx.vision;

import java.awt.Graphics;
import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.*;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;
import org.opencv.videoio.VideoCapture;

import Rbtx.vision.ui.OpenCVMatDisplay;

public class main {
	static BufferedImage output;
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		VideoCapture camera = new VideoCapture(0);
		camera.open(0);
	    if(!camera.isOpened()){
	        System.out.println("Camera Error");
	    }
	    else{
	        System.out.println("Camera OK?");
	    }
		Mat srcImage = new Mat();
		camera.read(srcImage);
		Mat hsvImage = new Mat();
		camera.read(hsvImage);
		Mat hsvOverlay = new Mat(3,3,0);
		camera.read(hsvOverlay);
		OpenCVMatDisplay display = new OpenCVMatDisplay(srcImage);
		while (true){
			camera.read(srcImage);
			Imgproc.medianBlur(srcImage, srcImage, 3);
			Imgproc.cvtColor(srcImage, hsvImage, Imgproc.COLOR_BGR2HSV);
			Core.inRange(hsvImage, new Scalar(75, 60, 60), new Scalar(97, 255, 255), hsvOverlay); // Valeur pour le tape
			//Core.multiply(hsvOverlay, new Scalar(0.75, 0.75, 0.75), hsvOverlay);
			//Core.multiply(hsvOverlay, new Scalar(0.3, 1, 1), hsvOverlay);
			//Nous allons utiliser le maintenant inutile hsvImage comme Mat de swap...
			Imgproc.cvtColor(hsvOverlay, hsvImage, Imgproc.COLOR_GRAY2BGR);
			Core.multiply(srcImage, hsvImage, srcImage);
			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			Imgproc.findContours(hsvOverlay, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
			//Dessiner les contours...
			//Core.multiply(srcImage, new Scalar(0,0,0), srcImage);
			for (int i = 0; i < contours.size(); i++) {
				//Trier les contours qui ont une bounding box
				if (Imgproc.contourArea(contours.get(i)) > 2000){
					Rect rect = Imgproc.boundingRect(contours.get(i));
					Imgproc.rectangle(srcImage, new Point(rect.x,rect.y),
					new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(255, 0, 0, 255), 3); 
				}
			    Imgproc.drawContours(srcImage, contours, i, new Scalar(0, 0, 255), -1);
			}
			display.update(srcImage);
		}
	}
}
