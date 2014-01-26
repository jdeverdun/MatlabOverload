package com.java.display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.Matlab.server.MatlabServer;
import com.java.ClassManagement;


/**
 * Class to set some global variables
 * and check for updates
 * @author Jérémy DEVERDUN
 *
 */
public class LoadingApp extends JPanel{
	private JProgressBar jp;
	private Image img;
	private Dimension size;
	private JFrame jf;
	private MatlabServer ms;
	
	/**
	 * Load app with specified background image
	 * @param im 
	 */
	public LoadingApp(String im){
		super();
		jp=new JProgressBar();
		jp.setValue(0);
		jp.setMaximum(10);
		add(jp,BorderLayout.SOUTH);
		this.img = new ImageIcon(im).getImage();
	    size = new Dimension(img.getWidth(null), img.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);
	}
	
	/**
	 * Load app
	 */
	public LoadingApp(){
		super();
		setLayout(new BorderLayout(0, 0));
		jp=new JProgressBar();
		jp.setValue(0);
		jp.setMaximum(10);
		add(jp);
	    size = new Dimension(200, 80);
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	}

	/**
	 * Launch & wait for matlabengine
	 */
	public void load() {
		jp.setVisible(true);
		jp.setStringPainted(true);
		createAndShowGui();
		ms=new MatlabServer(ClassManagement.matlabserverDirectory);
		ClassManagement.matlabserver=ms;
		ms.start();
		ClassManagement.MatlabServerIsRunning=true;
		for(int i=0;i<1;i++){
			try {
				Thread.sleep(1000);
				jp.setValue(i+1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		jf.dispose();
	}
	
	/**
	 * Check for available update
	 */
	private void checkForUpdate() {
		boolean doUpdate=false;
		try {
			ms.checkForUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Display GUI
	 */
	private void createAndShowGui(){
		jf=new JFrame("Loading");
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.setSize(size);
		jf.getContentPane().add(this);
	    Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension dim = kit.getScreenSize();
        jf.setLocation(dim.width/2-200/2, dim.height/2-80/2);
		jf.setVisible(true);
		
	}
	
	/**
	 * 
	 */
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}
}
