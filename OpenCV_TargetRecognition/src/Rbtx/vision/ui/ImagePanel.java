package Rbtx.vision.ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

class ImagePanel extends JComponent {
    private BufferedImage image;
    public void update(BufferedImage output){
    	this.image = output;
    }
    public ImagePanel(BufferedImage output) {
		this.image = output;
	}
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}