package Rbtx.vision.ui;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.opencv.core.Mat;

public class OpenCVMatDisplay {
	private static BufferedImage output;
	JFrame guiFrame;
	ImagePanel pane;

	public static BufferedImage createBufferedImage(Mat mat) {
	    BufferedImage image = new BufferedImage(mat.width(), mat.height(), BufferedImage.TYPE_3BYTE_BGR);
	    WritableRaster raster = image.getRaster();
	    DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
	    byte[] data = dataBuffer.getData();
	    mat.get(0, 0, data);
	    return image;
	}
	
	public OpenCVMatDisplay(Mat image){
		output = createBufferedImage(image);
		guiFrame = new JFrame("OpenCV GUI");
		pane = new ImagePanel(output);
		guiFrame.setSize(700,700);
		guiFrame.setContentPane(pane);
		guiFrame.pack();
		guiFrame.setSize(600,600);
		guiFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		guiFrame.setVisible(true);
	}
	public void update(Mat image){
		output = createBufferedImage(image);
		pane.update(output);
		guiFrame.repaint();
	}
}
