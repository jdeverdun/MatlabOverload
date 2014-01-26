package com.Matlab.server;

import ij.IJ;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.java.ClassManagement;
import com.java.io.FileParser;


/**
 * Class to launch matlabengine 
 * @author Jérémy DEVERDUN
 *
 */
public class MatlabServer extends Thread{
	private Process p=null;
	private BufferedReader input=null;
	private String exelocation;
	private String MCRinfo=".MCR.info";
	
	/**
	 * 
	 * @param e : matlabengine location
	 */
	public MatlabServer(String e){
		this.setExelocation(e);
	}
	
	/**
	 * 
	 */
	public void run(){
		boolean askForMCR=false;
		// Pour Linux il faut donner le chemin vers le MCR
		do{
			askForMCR=false;
	    	try {
	    		Process t=launchMatlabEngine();
				setP(t);
				String line="";
				input =
			        new BufferedReader
			          (new InputStreamReader(t.getInputStream()));
			      while ((line = input.readLine()) != null) {
			        System.out.println(line);
			        if(line.split("deployedMCRroot").length>1) askForMCR=true;
			      }
			      input.close();
			      if(askForMCR){
			    	  String txt=FileParser.selectFolder("Where is the MCR");
			    	  ClassManagement.MCRroot=txt;
			    	  FileParser.writeText("MCRroot="+ClassManagement.MCRroot, exelocation+File.separator+MCRinfo);
			      }
			    	  
	    	} catch (IOException e) {
	    		try {
					input.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}while(askForMCR);
	}

	/**
	 * set Process
	 * @param t : process
	 */
	public void setP(Process t){
		this.p=t;
	}
	
	/**
	 * stop the thread safely
	 */
	public void stopThread(){
		this.p.destroy();
		try {
			input.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.stop();
	}
	
	/**
	 * 
	 * @param exelocation
	 */
	public void setExelocation(String exelocation) {
		this.exelocation = exelocation;
	}
	
	/**
	 * 
	 * @return exelocation
	 */
	public String getExelocation() {
		return exelocation;
	}
	
	/**
	 * 
	 * @return Process of the matlabengine
	 * @throws IOException
	 */
	public Process launchMatlabEngine() throws IOException{
		Process t = null;
		if(IJ.isLinux() && ClassManagement.MCRroot.equals("")){
			String info=FileParser.getTextFromFile(exelocation+File.separator+MCRinfo);
			if(info.split("=").length>1)
				ClassManagement.MCRroot=info.split("=")[1];
		}
		if(IJ.is64Bit() || IJ.isWindows() || IJ.isLinux()){
			if(IJ.is64Bit()){
				if(IJ.isWindows()){
					t=Runtime.getRuntime().exec(exelocation+File.separator+"matlabengine64w.exe");
				}else{ 
					if(IJ.isLinux()){
						t=Runtime.getRuntime().exec(exelocation+File.separator+"run_matlabengine64l.sh "+ClassManagement.MCRroot);
					}else{ 
	    				if(IJ.isMacintosh()){
	    					t=Runtime.getRuntime().exec(exelocation+File.separator+"matlabengine64m");
	    				}
					}
				}
			}else{
				if(IJ.isWindows()){
					t=Runtime.getRuntime().exec(exelocation+File.separator+"matlabengine32w.exe");
				}else{
					if(IJ.isLinux()){
	    				t=Runtime.getRuntime().exec(exelocation+File.separator+"run_matlabengine32l.sh "+ClassManagement.MCRroot);
					}else{
						if(IJ.isMacintosh()){
							t=Runtime.getRuntime().exec(exelocation+File.separator+"matlabengine32m");
						}
					}
				}
			}
		}else{
			System.out.println("Unknow OS");
		}   
		return t;
	}
	
	/**
	 * Check for available updates
	 * @return true if any update is available
	 * @throws Exception
	 */
	public boolean checkForUpdate() throws Exception {
		boolean updateAvaible=false;
		String md5="";
		if(IJ.is64Bit()){
			if(IJ.isWindows()){
				md5=FileParser.getMD5Checksum(ClassManagement.updateSite+File.separator+"matlabengine64w.exe");
			}
		}
		return updateAvaible;
	}
}
