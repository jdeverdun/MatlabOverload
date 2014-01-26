package com.java.modeles;

import ij.IJ;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JMenuItem;

import com.Matlab.server.MatlabServer;
import com.java.ClassManagement;
import com.java.MatlabClient;
import com.java.display.menu.MatlabFunctionCall;
import com.java.io.FileParser;
import com.java.io.FunctionParser;
import com.java.modeles.ImageWorker;
import com.java.modeles.Worker;

/**
 * Class containing all informations about a function
 * @author Jérémy DEVERDUN
 *
 */
public class MatlabFunction extends JMenuItem {
	private File mfile;
	private String[][] argin;
	private boolean isImageFun;
	private ArrayList<String> images2send;
	private String function;
	private boolean iscancel=false;
	private String[][] argout;


	public MatlabFunction(){
		super();
	}
	
	/**
	 * 
	 * @param s name of the function
	 */
	public MatlabFunction(String s){
		super(s);
	}
	
	/**
	 * 
	 * @param s name of the function
	 * @param f m-file
	 */
	public MatlabFunction(String s, File f){
		super(s);
		images2send=new ArrayList<String>();
		this.setFunction(s);
		this.setMfile(f);
		this.setArgin(null);
		this.setImageFun(false);
		setVarFromFile(f);
		final MatlabFunction cur=this;
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Thread monThread=new Thread(){
					public void run(){
						images2send.clear();
						function=FunctionParser.buildFunction(cur.mfile);
						if(!ClassManagement.MatlabServerIsRunning){
							MatlabServer ms=new MatlabServer(ClassManagement.matlabserverDirectory);
							ClassManagement.matlabserver=ms;
							ms.start();
							ClassManagement.MatlabServerIsRunning=true;
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						String[][] tmp=argin;
						MatlabFunctionCall mfcall=new MatlabFunctionCall(argin,cur);
						if(mfcall.toShow()) mfcall.createAndShowGUI();
						while(mfcall.isShowing()){
							try {
								Thread.sleep(100);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
						if(!iscancel){
							isImageFun=false;
							if(argin!=null){
								for(String[] e:argin){
									if(e[1].equals("image")){
										isImageFun=true;
										break;
									}
								}
							}
							MatlabClient mc=new MatlabClient();
							if(isImageFun){
								ImageWorker iw=new ImageWorker(function,images2send,getImageOutputList(),getFormattedArgs(),getFormattedArgout(),argout);
								mc.sendImageCommand(iw);
								iw=null;
							}else{
								Worker w=new Worker(function,getFormattedArgs(),getFormattedArgout());
								mc.sendSimpleCommand(w);
								w=null;
							}
							mc.close();
							function=null;
							isImageFun=false;
						}
						argin=tmp;
					}

				};
				monThread.start();
			}


		});
	}
	
	/**
	 * 
	 * @return iscancel
	 */
	public boolean isCancel() {
		return iscancel;
	}
	
	/**
	 * 
	 * @param iscancel
	 */
	public void setIscancel(boolean iscancel) {
		this.iscancel = iscancel;
	}
	
	/**
	 * 
	 * @return list of output images
	 */
	public ArrayList<String> getImageOutputList() {
		ArrayList<String> list=new ArrayList<String>();
		for(String[] e:argout){
			if(e[1].equals("image")){
				list.add(e[0]);
			}
		}
		return list;
	}
	
	/**
	 * 
	 * @return list of input images
	 */
	public ArrayList<String> getImageInputList() {
		ArrayList<String> list=new ArrayList<String>();
		for(String[] e:argin){
			if(e[1].equals("image")){
				list.add(e[0]);
			}
		}
		return list;
	}
	
	/**
	 * Test if any image argout has name "s"
	 * @param s : name of the argout image to test
	 * @return true if "s" is in the argout list
	 */
	private boolean argoutContains(String s) {
		for(String[] e:argout){
			if(e[1].equals("image") && e[0].equals(s)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Format argin to be understood by the matlabengine
	 * @return formatted argin
	 */
	private String[] getFormattedArgs() {
		if(argin==null) return new String[]{""};
		String[] res=new String[argin.length];
		int count=0;
		for(String[] e:argin){
			if(!e.equals("image")){
				if(e[1].equals("string") || e[1].equals("file") || e[1].equals("select")){
					res[count++]="'"+e[0]+"'";
				}else{
					res[count++]=e[0];
				}
			}
		}
		return res;
	}
	
	/**
	 * Format argout to be understood by the matlabengine
	 * @return formatted argout
	 */
	private String[] getFormattedArgout() {
		if(argout==null) return new String[]{""};
		String[] res=new String[argout.length];
		int count=0;
		for(String[] e:argout){
			res[count++]=""+e[0]+"";
		}
		return res;
	}
	
	/**
	 * Set argin and argout from specified m-file
	 * @param f : m-file
	 */
	public void setVarFromFile(File f){
		String txt=FileParser.getTextFromFile(f.getAbsolutePath());
		String[] lines=txt.split("\n");
		String header=lines[1].substring(1);
		String[] args=lines[2].substring(1).split("@@");
		String[] argouttmp=lines[3].substring(1).split("@@");
		if(lines[2].split("none").length>1){
			this.argin=null;
		}else{
			this.argin=new String[args.length-2][2];
			for(int i=1;i<args.length-1;i++){
				String[] elem=args[i].split(":");
				this.argin[i-1][0]=elem[1];
				this.argin[i-1][1]=elem[0];
			}
		}
		if(lines[3].split("none").length>1){
			this.argout=null;
		}else{
			this.argout=new String[argouttmp.length-2][2];
			for(int i=1;i<argouttmp.length-1;i++){
				String[] elem=argouttmp[i].split(":");			
				this.argout[i-1][0]=elem[1];
				this.argout[i-1][1]=elem[0];
			}
		}
		this.setToolTipText(header);
		
	}
	
	/**
	 * 
	 * @return argout
	 */
	public String[][] getArgout() {
		return argout;
	}
	
	/**
	 * 
	 * @param argout 
	 */
	public void setArgout(String[][] argout) {
		this.argout = argout;
	}
	
	/**
	 * 
	 * @param mfile
	 */
	public void setMfile(File mfile) {
		this.mfile = mfile;
	}
	
	/**
	 * 
	 * @return m-file
	 */
	public File getMfile() {
		return mfile;
	}
	
	/**
	 * 
	 * @param argin
	 */
	public void setArgin(String[][] argin) {
		this.argin = argin;
	}
	
	/**
	 * 
	 * @return argin
	 */
	public String[][] getArgin() {
		return this.argin;
	}
	
	/**
	 * 
	 * @param isImageFun
	 */
	public void setImageFun(boolean isImageFun) {
		this.isImageFun = isImageFun;
	}
	
	/**
	 * 
	 * @return isImageFun
	 */
	public boolean isImageFun() {
		return isImageFun;
	}
	
	/**
	 * 
	 * @return list of images to send
	 */
	public ArrayList<String> getImages2send() {
		return images2send;
	}
	
	/**
	 * 
	 * @param images2send
	 */
	public void setImages2send(ArrayList<String> images2send) {
		this.images2send = images2send;
	}
	
	/**
	 * 
	 * @return function
	 */
	public String getFunction() {
		return function;
	}
	
	/**
	 * 
	 * @param function
	 */
	public void setFunction(String function) {
		this.function = function;
	}
}
