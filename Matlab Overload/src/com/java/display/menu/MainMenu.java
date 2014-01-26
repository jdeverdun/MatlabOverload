package com.java.display.menu;

import ij.IJ;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import com.java.MatlabClient;
import com.java.io.FileParser;
import com.java.modeles.MatlabFunction;
import com.java.modeles.Worker;

/**
 * Main menu of IJ interface 
 * will display available MATLAB
 * functions.
 * @author Jérémy DEVERDUN
 *
 */
public class MainMenu extends JMenuBar {

	private static final long serialVersionUID = 1L;
	public MainMenu() {
		super();
		initializeMenu();
	}
	
	/**
	 * Check for the M-files to add to the menu 
	 * in the IJ matlab_plugins directory
	 */
	public void initializeMenu() {
		String ijdir=IJ.getDirectory("imagej");
		System.out.println(ijdir);
		File[] listfile=(new File(ijdir+File.separator+"matlab_plugins")).listFiles();
		for(File f:listfile){
			if(f.isDirectory()){
				JMenu mntm = new JMenu(f.getName());
				add(mntm);
				File[] listfilem=(new File(f.getAbsolutePath())).listFiles();
				for(File f2:listfilem){
					if(f2.isFile() && f2.getName().contains(".m")){
						String[] txt;
						try {
							txt = FileParser.getTextFromFile(f2.getCanonicalPath()).split("\n");
							if(txt[0].split("@main").length>1){
								MatlabFunction mf=new MatlabFunction(f2.getName().substring(0, f2.getName().lastIndexOf(".")), f2);
								mntm.add(mf);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
					
			}
		}
	}
}

