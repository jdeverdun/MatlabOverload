package com.java;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.process.ImageProcessor;

import java.awt.Dimension;
import java.io.*;
import java.net.*;

import com.Matlab.modeles.MatlabResponse;
import com.Matlab.server.MatlabServer;
import com.java.io.CustomObjectInputStream;
import com.java.modeles.ImagePlusExtended;
import com.java.modeles.ImageWorker;
import com.java.modeles.Worker;
import com.mathworks.toolbox.javabuilder.MWNumericArray;


/**
 * Class which will send data to matlabengine (client side)
 * @author Jérémy DEVERDUN
 *
 */
public class MatlabClient {
	private Thread monThread = null;
	private Socket kkSocket = null;
	private ObjectInputStream in = null;
	private int BUFFER_SIZE=65536;

	/**
	 * Instanciate default client with ip : 127.0.0.1 on port 4444 
	 */
	public MatlabClient(){
    	try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
            kkSocket = new Socket("127.0.0.1", 4444);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: 127.0.0.1");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: taranis.");
            System.exit(1);
        }
	}
	
	/**
	 * Sending MATLAB command without additionnal data (images...)
	 * @param w
	 */
    public void sendSimpleCommand(Worker w) {
    	ObjectOutputStream outputStream=null;
		try {
			outputStream = new ObjectOutputStream(kkSocket.getOutputStream());
			outputStream.writeObject(w.clone()); 
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			Object obj;
			try {
				obj = in.readObject();
				if(obj instanceof MatlabResponse){
					System.out.println(((MatlabResponse)obj).getComment());
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
    	 

	}
    
    /**
     * Send MATLAB command with Images
     * @param w
     */
    public void sendImageCommand(ImageWorker w) {
		// 
    	ObjectOutputStream outputStream=null;
    	in = null;
    	byte[] bydata=null;
    	float[] fldata=null;
    	short[] shdata=null;
		try {
			// P1 : sending data
			in = new CustomObjectInputStream((kkSocket.getInputStream()));
			outputStream = new ObjectOutputStream(kkSocket.getOutputStream());
			outputStream.reset();
			outputStream.writeObject(w.clone());
			outputStream.reset();
			outputStream.flush();
			for(String e:w.getImages2send()){
				ImagePlus imp=WindowManager.getImage(e);
				System.out.println("c'est parti!");
				switch(imp.getBitDepth()){
					case 8: 
						bydata=ImagePlusExtended.getByteData(imp);
						outputStream.writeObject(bydata.clone());
						break;
					case 16: 
						shdata=ImagePlusExtended.getShortData(imp);
						outputStream.writeObject(shdata.clone());
						break;
					case 32:
						fldata=ImagePlusExtended.getFloatData(imp);
						outputStream.writeObject(fldata.clone());
						break;
					default:
						IJ.log("Format unsupported");
						fldata=null;
						bydata=null;
						w=null;
						outputStream.flush();
						outputStream.close();
						in.close();
						return;
				}
				outputStream.flush();
				outputStream.reset();
				fldata=null;
				bydata=null;
				shdata=null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			// P2 : receiving results
			Object obj;
			fldata=null;
			shdata=null;
			bydata=null;
			try {
				obj = in.readObject();

				if(obj instanceof MatlabResponse){
					MatlabResponse resp=((MatlabResponse)obj);
					resp.displayData(w.getFullargout());
					if(resp.getComment().equals("image")){
						System.out.println(w.getImages2retrieve().size());
						for(String e:w.getImages2retrieve()){
							Object imageObj=null;
							imageObj = in.readObject();
							System.out.println(e+" - retrieved");
							if(imageObj instanceof byte[][]){
								byte[][] f=(byte[][])imageObj;
								byte[][] f2=new byte[1][f.length*f[0].length];
								for(int j=0;j<f.length;j++)
									for(int k=0;k<f[0].length;k++)
										f2[0][j+(f.length*(k))]=f[j][k];
								System.out.println(f2.length+"-"+f2[0].length);
								ImagePlus impe=ImagePlusExtended.buildImagePlusFromByteArrays(f2, e, f.length, f[0].length);
								impe.show();
							}else{
								if(imageObj instanceof byte[][][]){
									byte[][][] f=(byte[][][])imageObj;
									byte[][] f2=new byte[f[0][0].length][f.length*f[0].length];
									for(int fl=0;fl<f[0][0].length;fl++)
										for(int j=0;j<f.length;j++)
											for(int k=0;k<f[0].length;k++)
												f2[fl][j+(f.length*(k))]=f[j][k][fl];
										System.out.println(f2.length+"-"+f2[0].length);
									ImagePlus impe=ImagePlusExtended.buildImagePlusFromByteArrays(f2, e, f.length, f[0].length);
									impe.show();
								}else{
									if(imageObj instanceof float[][][]){
										float[][][] f=(float[][][])imageObj;
										float[][] f2=new float[f[0][0].length][f.length*f[0].length];
										for(int fl=0;fl<f[0][0].length;fl++)
											for(int j=0;j<f.length;j++)
												for(int k=0;k<f[0].length;k++)
													f2[fl][j+(f.length*(k))]=f[j][k][fl];
											System.out.println(f2.length+"-"+f2[0].length);
										ImagePlus impe=ImagePlusExtended.buildImagePlusFromDoubleOrFloatArrays(imageObj, e, f.length, f[0].length);
										impe.show();
									}else{
										if(imageObj instanceof float[][]){
											float[][] f=(float[][])imageObj;
											float[][] f2=new float[1][f.length*f[0].length];
											for(int j=0;j<f.length;j++)
												for(int k=0;k<f[0].length;k++)
													f2[0][j+(f.length*(k))]=f[j][k];
											System.out.println(f2.length+"-"+f2[0].length);
											ImagePlus impe=ImagePlusExtended.buildImagePlusFromDoubleOrFloatArrays(f2, e, f.length, f[0].length);
											impe.show();
										}
									}
								}
							}
						}
					}
				}						
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			try {
				fldata=null;
				bydata=null;
				shdata=null;
				w=null;
				outputStream.flush();
				outputStream.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    /**
     * Close connexion to matlabengine
     */
	public void close(){
    	try {
			in.close();
			kkSocket.getOutputStream().flush();
			kkSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}      
    }
	
	/**
	 * Test main
	 * @param args
	 */
    public static void main(String[] args){
    	
    	MatlabServer ms=new MatlabServer("C:\\Users\\Mobilette\\Documents\\Stage2010-2011\\MATLAB mcode\\programme\\nuclei_segmentation\\etape_3\\daemon\\matlabengine\\distrib\\matlabengine.exe");
    	ms.start();
    	try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	MatlabClient mc=new MatlabClient();
    	//ImageWorker ic=new ImageWorker("size(currentImage),figure,imshow",new String[]{"currentImage"},new ImagePlusExtended("C:\\Users\\Mobilette\\Desktop\\al1.tif"));
    /*	mc.sendImageCommand((ImageWorker) ic.clone());
    	mc.close();
    	ms.stopThread();
    	//System.out.println(((ImageWorker) ic.clone()).getImp()==null);
    	//mc.sendSimpleCommand(new Worker("figure",new String[]{"1"}));
    	/*try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	mc.sendImage(new Worker("strcmp",new String[]{"a","a"}));*
    	/*for(int i=0;i<10;i++){
    		try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		System.out.println("i=i+1");
    		mc.doProcessInput("i=i+1");
    	}*/
    	//mc.close();
    }
}