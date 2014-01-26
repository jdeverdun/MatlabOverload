package com.java.io;

import ij.IJ;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Class to work with files
 * @author Jérémy DEVERDUN
 *
 */
public class FileParser {
	
	
	/**
	 * Replace backlash in a string
	 * @param myStr : the string where \ must be replaced 
	 * @return the new String
	 */
	public static String backlashReplace(String myStr){
	    final StringBuilder result = new StringBuilder();
	    final StringCharacterIterator iterator = new StringCharacterIterator(myStr);
	    char character =  iterator.current();
	    while (character != CharacterIterator.DONE ){
	     
	      if (character == '\\') {
	         result.append("\\\\");
	      }
	       else {
	        result.append(character);
	      }

	      
	      character = iterator.next();
	    }
	    return result.toString();
	  }
	
	/**
	 * Recursive directory list
	 * @param dir : directory to list
	 * @return number of folder in the directory
	 */
	public static int recursiveDirList(File dir){
		int count=0;
		String[] f=dir.list();
		for(int i=0;i<f.length;i++){
			File tmp=(new File(dir.getAbsolutePath()+File.separator+f[i]));
			if(tmp.isDirectory()) count+=1+recursiveDirList(tmp);
		}
		return count;
	}
	
	/**
	 * Retrieve text from a file
	 * @param f : file to read
	 * @return the content of the file
	 */
	public static String getTextFromFile(String f) {
		File file = new File(f);
	    int ch;
	    StringBuffer strContent = new StringBuffer("");
	    FileInputStream fin = null;
	    try {
	      fin = new FileInputStream(file);
	      while ((ch = fin.read()) != -1)
	        strContent.append((char) ch);
	      fin.close();
	    } catch (Exception e) {
	      System.out.println(e);
	      IJ.showMessage(e.toString());
	    }
		return strContent.toString();
	}

	/**
	 * Delete specified directory
	 * @param dir : directory to delete
	 * @return boolean : true if succeed, false otherwise
	 */
	public static boolean deleteDir(File dir){
		if (dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i=0; i<children.length; i++) {
	            boolean success = deleteDir(new File(dir, children[i]));
	            if (!success) {
	                return false;
	            }
	        }
	    }

	    // The directory is now empty so delete it
	    return dir.delete();
	}
	
	/**
	 * Write text to specified file
	 * @param text : text to write
	 * @param chemin : path of the file
	 */
	public static void writeText(String text,String chemin){
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter(chemin.replaceAll("%20", "\\ "));
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(text);
			  //Close the output stream
			  out.close();
	  }catch (Exception e){//Catch exception if any
		  System.err.println("Error: " + e.getMessage());
	  }
	}
	
	/**
	 * Add text in existing file
	 * @param text : text to add
	 * @param chemin : path of the file
	 */
	public static void appendText(String text,String chemin){
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter(chemin.replaceAll("%20", "\\ "),true);
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(text);
			  //Close the output stream
			  out.close();
			  fstream.close();
	  }catch (Exception e){//Catch exception if any
		  System.err.println("Error: " + e.getMessage());
	  }
	}
	
	/**
	 * Convert Image to BufferedImage
	 * @param image
	 * @return BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image image) {
	    if (image instanceof BufferedImage) {
	        return (BufferedImage)image;
	    }

	    // This code ensures that all the pixels in the image are loaded
	    image = new ImageIcon(image).getImage();

	    // Determine if the image has transparent pixels; for this method's
	    // implementation, see Determining If an Image Has Transparent Pixels
	    boolean hasAlpha = false;

	    // Create a buffered image with a format that's compatible with the screen
	    BufferedImage bimage = null;
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    try {
	        // Determine the type of transparency of the new buffered image
	        int transparency = Transparency.OPAQUE;
	        if (hasAlpha) {
	            transparency = Transparency.BITMASK;
	        }

	        // Create the buffered image
	        GraphicsDevice gs = ge.getDefaultScreenDevice();
	        GraphicsConfiguration gc = gs.getDefaultConfiguration();
	        bimage = gc.createCompatibleImage(
	            image.getWidth(null), image.getHeight(null), transparency);
	    } catch (HeadlessException e) {
	        // The system does not have a screen
	    }

	    if (bimage == null) {
	        // Create a buffered image using the default color model
	        int type = BufferedImage.TYPE_INT_RGB;
	        if (hasAlpha) {
	            type = BufferedImage.TYPE_INT_ARGB;
	        }
	        bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
	    }

	    // Copy image to buffered image
	    Graphics g = bimage.createGraphics();

	    // Paint the image onto the buffered image
	    g.drawImage(image, 0, 0, null);
	    g.dispose();

	    return bimage;
	}
	
	/**
	 * Select file or folder 
	 * @param txt : title of the dialog box
	 * @return path of the file selected
	 */
	public static String selectFolder(String txt){
		JFileChooser chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle(txt);
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    //
	    // disable the "All files" option.
	    //
	    chooser.setAcceptAllFileFilterUsed(false);
	    //    
	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
	      System.out.println("getCurrentDirectory(): " 
	         +  chooser.getCurrentDirectory());
	      System.out.println("getSelectedFile() : " 
	         +  chooser.getSelectedFile());
	      return chooser.getSelectedFile().getAbsolutePath();
	      }
	    else {
	      System.out.println("No Selection ");
	      return "";
	      }
	}
	
	/**
	 * Create checksum for a file
	 * @param filename : path to the file 
	 * @return byte checksum
	 * @throws Exception
	 */
	public static byte[] createChecksum(String filename) throws  Exception{
		  InputStream fis =  new FileInputStream(filename);
		
		  byte[] buffer = new byte[1024];
		  MessageDigest complete = MessageDigest.getInstance("MD5");
		  int numRead;
		  do {
		   numRead = fis.read(buffer);
		   if (numRead > 0) {
		     complete.update(buffer, 0, numRead);
		     }
		   } while (numRead != -1);
		  fis.close();
		  return complete.digest();
	}
	

	/**
	 * Retrieve MD5 checksum for a file
	 * @param filename : path to the file
	 * @return MD5 checksum
	 * @throws Exception
	 */
	public static String getMD5Checksum(String filename) throws Exception {
	  byte[] b = createChecksum(filename);
	  String result = "";
	  for (int i=0; i < b.length; i++) {
	    result +=
	       Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
	   }
	  return result;
	}
}
