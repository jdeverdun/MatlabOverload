package com.java.modeles;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;

import com.mathworks.toolbox.javabuilder.MWNumericArray;

import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;


/**
 * Class with args for sending Image command to matlabengine
 * @author Jérémy DEVERDUN
 *
 */
public class ImageWorker extends Worker implements Cloneable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4698130243584272452L;
	private Dimension[] dimImages2send;
	private ArrayList<String> images2send,images2retrieve;
	private int nbimagesOut,nbimagesIn;
	public boolean modifyCurrentImage;
	public ImageWorker(){
		super();
	}
	/**
	 * @see Worker
	 */
	public ImageWorker(String m,String[] a,String[] o,ImagePlus i){
		super(m,a,o);
	}
	
	/**
	 * 
	 * @param function
	 * @param imageInputList
	 * @param imageOutputList
	 * @param formattedArgs
	 * @param formattedArgout
	 */
	public ImageWorker(String function, ArrayList<String> imageInputList,
			ArrayList<String> imageOutputList, String[] formattedArgs,
			String[] formattedArgout) {
		super(function,formattedArgs,formattedArgout);
		images2send=new ArrayList<String>();
		setImages2send(imageInputList);
		setImages2retrieve(imageOutputList);
		
	}
	
	/**
	 * 
	 * @param function
	 * @param imageInputList
	 * @param imageOutputList
	 * @param formattedArgs
	 * @param formattedArgout
	 * @param tmp
	 */
	public ImageWorker(String function, ArrayList<String> imageInputList,
			ArrayList<String> imageOutputList, String[] formattedArgs,
			String[] formattedArgout, String[][] tmp) {
		super(function,formattedArgs,formattedArgout);
		images2send=new ArrayList<String>();
		setImages2send(imageInputList);
		setImages2retrieve(imageOutputList);
		setFullArgout(tmp);
	}
	/**
	 * 
	 * @return boolean
	 */
	public boolean isModifyCurrentImage() {
		return modifyCurrentImage;
	}
	
	/**
	 * 
	 * @param modifyCurrentImage
	 */
	public void setModifyCurrentImage(boolean modifyCurrentImage) {
		this.modifyCurrentImage = modifyCurrentImage;
	}
	
	/**
	 * 
	 */
	public Object clone(){
		ImageWorker cloned = (ImageWorker)super.clone();

		cloned.args = (String[])args.clone();
		cloned.nbimagesIn = (int)nbimagesIn;
		cloned.nbimagesOut = (int)nbimagesOut;
		cloned.dimImages2send = (Dimension[])dimImages2send.clone();
		return cloned;
	}
	
	/**
	 * 
	 * @param dimImages2send
	 */
	public void setDimImages2send(Dimension[] dimImages2send) {
		this.dimImages2send = dimImages2send;
	}
	
	/**
	 * 
	 * @return dimImages2send
	 */
	public Dimension[] getDimImages2send() {
		return dimImages2send;
	}
	public void setImages2send(ArrayList<String> images2send) {
		this.images2send = images2send;
		System.out.println("nb :"+this.images2send.size());
		this.nbimagesIn=this.images2send.size();
		dimImages2send=new Dimension[nbimagesIn];
		int count=0;
		for(String e:this.images2send){
			ImagePlus imp=WindowManager.getImage(e);
			Dimension tmp=new Dimension(imp.getWidth(), imp.getHeight());
			dimImages2send[count++]=tmp;
		}
	}
	
	/**
	 * 
	 * @return list of images2send
	 */
	public ArrayList<String> getImages2send() {
		return images2send;
	}
	
	/**
	 * 
	 * @param images2retrieve
	 */
	public void setImages2retrieve(ArrayList<String> images2retrieve) {
		this.images2retrieve = images2retrieve;
		this.nbimagesOut=this.images2retrieve.size();
	}
	
	/**
	 * 
	 * @return images2retrieve
	 */
	public ArrayList<String> getImages2retrieve() {
		return images2retrieve;
	}
	
	/**
	 * 
	 * @param nbimageOut
	 */
	public void setNbimagesOut(int nbimageOut) {
		this.nbimagesOut = nbimageOut;
	}
	
	/**
	 * 
	 * @return nbimageOut
	 */
	public int getNbimagesOut() {
		return nbimagesOut;
	}
	
	/**
	 * 
	 * @param nbimagesIn
	 */
	public void setNbimagesIn(int nbimagesIn) {
		this.nbimagesIn = nbimagesIn;
	}
	
	/**
	 * 
	 * @return nbimagesin
	 */
	public int getNbimagesIn() {
		return nbimagesIn;
	}



	
}
