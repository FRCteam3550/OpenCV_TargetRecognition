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
import org.opencv.videoio.Videoio;

import Rbtx.vision.ui.OpenCVMatDisplay;
import Rbtx.vision.ui.OpenCVSliderDisplay;

public class main {
	static BufferedImage output;
	static double timeSinceLastUpdate;
	
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		VideoCapture camera = new VideoCapture(0);
		camera.open(0);
	    if(!camera.isOpened()){
	        System.out.println("Camera Error");
	    }
	    else{
	        System.out.println("Camera OK!");
	    }
	    camera.set(Videoio.CAP_PROP_FRAME_COUNT, 30);
		Mat srcImage = new Mat();
		camera.read(srcImage);
		Mat hsvImage = new Mat();
		camera.read(hsvImage);
		Mat hsvOverlay = new Mat(3,3,0);
		camera.read(hsvOverlay);
		OpenCVMatDisplay display = new OpenCVMatDisplay(srcImage);
		OpenCVSliderDisplay sliders = new OpenCVSliderDisplay();
		double time = System.nanoTime();
		while (true){
			time = System.nanoTime() / 1000000;
			camera.read(srcImage);
			//Imgproc.blur(srcImage, srcImage, new Size(3, 3));
			Imgproc.cvtColor(srcImage, hsvImage, Imgproc.COLOR_BGR2HSV);
			Core.inRange(hsvImage, sliders.getSliderValue(0), sliders.getSliderValue(1), hsvOverlay); // Valeur pour le tape
			//Core.multiply(hsvOverlay, new Scalar(0.75, 0.75, 0.75), hsvOverlay);
			//Core.multiply(hsvOverlay, new Scalar(0.3, 1, 1), hsvOverlay);
			//Nous allons utiliser le maintenant inutile hsvImage comme Mat de swap...
			Imgproc.cvtColor(hsvOverlay, hsvImage, Imgproc.COLOR_GRAY2BGR);
			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			Imgproc.findContours(hsvOverlay, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
			//Core.multiply(srcImage, new Scalar(0,0,0), srcImage);
//			//Appliquer le masque...
//			//Imgproc.cvtColor(hsvOverlay, hsvOverlay, Imgproc.COLOR_GRAY2BGR);
//			//Core.bitwise_and(srcImage, hsvOverlay, srcImage);
			List<MatOfInt> convexhulls = new ArrayList<MatOfInt>(contours.size());
			List<Double> orientations = new ArrayList<Double>();
//			//Dessiner les rectangles
			for (int i = 0; i < contours.size(); i++) {
//				//Trier les contours qui ont une bounding box
				convexhulls.add(i, new MatOfInt(6));
				if (Imgproc.contourArea(contours.get(i)) > 2000){
					Imgproc.convexHull(contours.get(i), convexhulls.get(i));
					double contourSolidity = Imgproc.contourArea(contours.get(i))/Imgproc.contourArea(convexhulls.get(i));
					Imgproc.drawContours(srcImage, contours, i, new Scalar(255, 255, 255), -1);
					MatOfPoint2f points = new MatOfPoint2f(contours.get(i).toArray());
					RotatedRect rRect = Imgproc.minAreaRect(points);

			        Point[] vertices = new Point[4];  
			        rRect.points(vertices);  
			        for (int j = 0; j < 4; j++){ //Dessiner un rectangle avec rotation..
			            Imgproc.line(srcImage, vertices[j], vertices[(j+1)%4], new Scalar(0,255,0), 10);
			        }
					orientations.add(3.0);
					System.out.println(contourSolidity);
				}
			}
			sliders.updateTextField();
			display.update(srcImage);
			timeSinceLastUpdate = System.nanoTime() / 1000000 - time;
			System.out.println(timeSinceLastUpdate);
		}
	}
}
