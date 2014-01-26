package com.java.modeles;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Class to save all data required
 * for the matlabengine (command, args...)
 * @author Jérémy DEVERDUN
 *
 */
public class Worker  implements Cloneable,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6441644225708240188L;
	protected String method;
	protected String[] args;
	protected String[] output;
	protected String[][] fullargout;
	protected ArrayList<String> imageToShow;
	private byte[] buffer=new byte[65536];
	public Worker(){
		setMethod("");
		setArgs(new String[]{""});
		imageToShow=new ArrayList<String>();
	}
	
	/**
	 * 
	 * @param m : m-file
	 * @param a : argin
	 * @param o : argout
	 */
	public Worker(String m,String[] a,String[] o){
		setMethod(m);
		setArgs(a);
		setOutput(o);
		imageToShow=new ArrayList<String>();
	}
	
	/**
	 * 
	 * @param m : m-file 
	 * @param a : argin
	 */
	public Worker(String m,String[] a){
		setMethod(m);
		setArgs(a);
		setOutput(new String[]{""});
	}
	
	/**
	 * 
	 * @param m : m-file
	 */
	public Worker(String m){
		setMethod(m);
		setArgs(null);
		imageToShow=new ArrayList<String>();
	}
	
	/**
	 * 
	 * @param method
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	
	/**
	 * 
	 * @return method
	 */
	public String getMethod() {
		return method;
	}
	
	/**
	 * 
	 * @param args
	 */
	public void setArgs(String[] args) {
		this.args = args;
	}
	
	/**
	 * 
	 * @return args
	 */
	public String[] getArgs() {
		return args;
	}
	
	/**
	 * Format args to be understood by the matlabengine
	 * @return formatted args (String)
	 */
	public String getFormattedArgs() {
		String res="";
		int count=0;
		for(String e:args){
			if(count==0) res+=e;
			else res+=","+e;
			count++;
		}
		return res;
	}
	
	/**
	 * 
	 */
	public Object clone(){
		  try{
			Worker cloned = (Worker)super.clone();
		  	cloned.args = (String[])args.clone();
		  	cloned.output = (String[])output.clone();
		  	cloned.imageToShow = (ArrayList<String>)imageToShow.clone();
		  	return cloned;
		  }
		  catch(CloneNotSupportedException e){
			  System.out.println(e);
		  	return null;
		  }
	}
	
	/**
	 * Set argout
	 * @param output
	 */
	public void setOutput(String[] output) {
		this.output = output;
	}
	
	/**
	 * 
	 * @return argout
	 */
	public String[] getOutput() {
		return output;
	}
	
	/**
	 * Format argout to be understood by the matlabengine
	 * @return formatted argout (String)
	 */
	public String getFormattedOutput() {
		String res="[";
		int count=0;
		for(String e:output){
			if(count==0) res+=e;
			else res+=","+e;
			count++;
		}
		res+="]=";
		return res;
	}
	
	/**
	 * 
	 * @param imageOutputList
	 */
	public void setImageToShow(ArrayList<String> imageOutputList) {
		this.imageToShow=imageOutputList;
	}
	
	/**
	 * 
	 * @return list of image to show
	 */
	public ArrayList<String> getImageToShow() {
		return imageToShow;
	}
	
	/**
	 * 
	 * @param buffer
	 */
	public void setBuffer(byte[] buffer) {
		this.buffer = buffer;
	}
	
	/**
	 * 
	 * @return buffer
	 */
	public byte[] getBuffer() {
		return buffer;
	}
	
	/**
	 * Set argout with their type
	 * @param fullargin
	 */
	public void setFullArgout(String[][] fullargin) {
		this.fullargout = fullargin;
	}
	
	/**
	 * Get argout with their type
	 * @return fullargout
	 */
	public String[][] getFullargout() {
		return fullargout;
	}
}
