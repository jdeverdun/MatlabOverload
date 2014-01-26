package com.java.display;


import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;

import com.java.ClassManagement;
import com.java.display.menu.MainMenu;

import ij.IJ;
import ij.plugin.PlugIn;


/**
 * GUI for ImageJ
 * @author Jérémy DEVERDUN
 *
 */
public class MOgui implements PlugIn{
	private JFrame frame;
	private MainMenu menu;
	/**
	 * Stop process on shutdown
	 * @author Jérémy DEVERDUN
	 *
	 */
	class ShutdownThread extends Thread {
        public void run() {
            try{
            	ClassManagement.matlabserver.stopThread();
            }catch(Exception e){
            	e.printStackTrace();
            }
        }
    }
	
	/**
	 * Main function called by ImageJ
	 */
	public void run(String arg0) {
		ClassManagement.isImageJ=true;
		ClassManagement.matlabserverDirectory=IJ.getDirectory("imagej");
		LoadingApp la=new LoadingApp();
		la.load();
		frame=new JFrame("Matlab overload");
		ShutdownThread shut=new ShutdownThread();
	    Runtime.getRuntime().addShutdownHook(shut);
		menu=new MainMenu();
		frame.setJMenuBar(menu);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(new Dimension(400,100));
		frame.setVisible(true);
	}
	
	
	/**
	 * Test main
	 * @param args
	 */
	public static void main(String[] args){
		System.out.println("hello");
	}
	
}
