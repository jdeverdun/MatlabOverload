package com.Matlab.modeles;

import ij.IJ;
import ij.ImagePlus;
import ij.measure.ResultsTable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.java.modeles.ImagePlusExtended;
import com.java.modeles.Worker;

/**
 * JAVA Object sending back from MATLAB to JAVA
 * with the output of the function
 * @author Jérémy DEVERDUN
 *
 */
public class MatlabResponse implements Cloneable,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1263491097158374017L;
	private HashMap argout;
	private String comment;
	private ImagePlusExtended impe;
	
	/**
	 * 
	 * @param e : ImagePlusExtended
	 * @param argout : argout in a hashmap
	 * @param c : additional comments
	 */
	public MatlabResponse(ImagePlusExtended e,HashMap argout,String c){
		setImpe(e);
		setArgout(argout);
		setComment(c);
	}
	
	/**
	 * 
	 * @param argout : argout in a hashmap
	 * @param c : additional comments
	 */
	public MatlabResponse(HashMap argout,String c){
		setArgout(argout);
		setComment(c);
	}
	
	/**
	 * 
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * 
	 * @return comment
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * 
	 * @param argout
	 */
	public void setArgout(HashMap argout) {
		this.argout = argout;
	}
	
	/**
	 * 
	 * @return argout
	 */
	public HashMap getArgout() {
		return argout;
	}
	
	/**
	 * 
	 */
	public Object clone(){
		  try{
			MatlabResponse cloned = (MatlabResponse)super.clone();
		  	cloned.argout = (HashMap)argout.clone();
		  	cloned.comment=(String)comment;
		  	return cloned;
		  }
		  catch(CloneNotSupportedException e){
			  System.out.println(e);
		  	return null;
		  }
	}
	
	/**
	 * 
	 * @param impe
	 */
	public void setImpe(ImagePlusExtended impe) {
		System.out.println(impe.getWidth());
		this.impe = impe;
	}
	
	/**
	 * 
	 * @return impe
	 */
	public ImagePlusExtended getImpe() {
		return impe;
	}
	
	
	/**
	 * Display results in IJ ResultsTable
	 * @param args
	 */
	public void displayData(String[][] args) {
		if(this.getArgout()==null) return;
		ResultsTable rt=new ResultsTable();
		for(int ia=0;ia<args.length;ia++){
			String nom=args[ia][0];
			if(args[ia][1].equals("value")){
				if(rt.getCounter()==0) rt.incrementCounter();
				rt.addValue(nom, ((Double)this.getArgout().get(nom)).doubleValue());
			}else{
				if(args[ia][1].equals("array")){
					ResultsTable rtarray=new ResultsTable();
					if(this.getArgout().get(nom) instanceof double[]){
						for(int i=0;i<((double[])this.getArgout().get(nom)).length;i++){
							rtarray.incrementCounter();
							rtarray.addValue(0, ((double[])this.getArgout().get(nom))[i]);
						}
					}else{
						if(this.getArgout().get(nom) instanceof double[][]){
							for(int i=0;i<((double[][])this.getArgout().get(nom))[0].length;i++){
								rtarray.incrementCounter();
								for(int j=0;j<((double[][])this.getArgout().get(nom)).length;j++){
									rtarray.addValue(j, ((double[][])this.getArgout().get(nom))[j][i]);
								}
							}
						}
					}
					rtarray.show(args[ia][0]);
				}else{
					if(args[ia][1].equals("string")){
						IJ.log(args[ia][0]+" : "+(String)this.getArgout().get(nom));
					}
				}
			}
		}
		if(rt.getCounter()!=0) rt.show("Results");
		else rt=null;
	}
	
	/**
	 * Retrieve dimensions of an array
	 * @param array
	 * @return number of dimensions
	 */
	public static int getDim(Object array) {
	    int dim = 0;
	    Class cls = array.getClass();
	    while (cls.isArray()) {
	        dim++;
	        cls = cls.getComponentType();
	    }
	    return dim;
	}
}
