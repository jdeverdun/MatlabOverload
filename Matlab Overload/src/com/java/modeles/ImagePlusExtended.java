package com.java.modeles;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import com.mathworks.toolbox.javabuilder.MWNumericArray;

import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import ij.process.ShortProcessor;


/**
 * ImagePlus class with some extras
 * @author Jérémy DEVERDUN
 *
 */
public class ImagePlusExtended extends ImagePlus implements Cloneable,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4293205434743657859L;
	public ImagePlusExtended(){
		super();
	}
	
	/**
	 * 
	 * @param s : path to image
	 */
	public ImagePlusExtended(String s){
		super(s);
	}
	
	/**
	 * 
	 * @param title
	 * @param byteprocessor
	 */
	public ImagePlusExtended(String title, ByteProcessor byteprocessor) {
		super(title,byteprocessor);
	}
	
	/**
	 * 
	 * @param title
	 * @param shortprocessor
	 */
	public ImagePlusExtended(String title, ShortProcessor shortprocessor) {
		super(title,shortprocessor);
	}
	
	/**
	 * 
	 * @param title
	 * @param floatprocessor
	 */
	public ImagePlusExtended(String title, FloatProcessor floatprocessor) {
		super(title,floatprocessor);
	}
	
	/**
	 * 
	 * @param title
	 * @param imagestack
	 */
	public ImagePlusExtended(String title, ImageStack imagestack) {
		super(title,imagestack);
	}
	
	/**
	 * Convert ImagePlusExtended to ImagePlus
	 * @return
	 */
	public ImagePlus toImagePlus(){
		return (ImagePlus)this;
	}
	
	/**
	 * Retrieve float data of 32 bits image
	 * @return 3D matrix of float data
	 */
	public float[][][] getFloat(){
		ImagePlus imp=this;
		float[][] image_bytes_tmp = new float[imp.getStackSize()][];         
 	    for(int i=0; i<image_bytes_tmp.length; i++){

 			ImageProcessor ip = imp.getImageStack().getProcessor(i+1).duplicate();
 			image_bytes_tmp[i]=(float[])ip.getPixels();
 	    }
 	    
 	    
 	   float[][][] image_bytes = new float[imp.getWidth()][imp.getHeight()][imp.getStackSize()];
 	    for(int z=0; z<imp.getStackSize(); z++){
 	    	for(int j=0; j<imp.getWidth(); j++){
 	    		for(int i=0; i<imp.getHeight(); i++){
    				image_bytes[j][i][z] = image_bytes_tmp[z][i*imp.getWidth()+j];
    			}
    		}
    	}
 	    return image_bytes.clone();
	}
	
	/**
	 * Retrieve byte data of 8 bits image
	 * @param imp : image to analyze
	 * @return byte array with data for each pixel
	 */
	public static byte[] getByteData(ImagePlus imp){
		byte[] imagebyte = null;         
 	    for(int i=0; i<imp.getStackSize(); i++){
 			ByteProcessor fp = (ByteProcessor) imp.getImageStack().getProcessor(i+1).duplicate();
 			if(i==0) imagebyte=(byte[])fp.getPixels();
 			else imagebyte=concatByte(imagebyte, (byte[])fp.getPixels());//(byte[]) ArrayUtils.addAll(imagebyte, (byte[])fp.getPixels());
 	    	System.out.println("slice : "+(i+1)+"/"+imp.getStackSize());
 	    }
 	   
 	    return imagebyte;
	}
	
	/**
	 * Retrieve float data of 32 bits images
	 * @param imp : image to analyze
	 * @return float array with data for each pixel
	 */
	public static float[] getFloatData(ImagePlus imp){
		float[] imagefloat = null;         
 	    for(int i=0; i<imp.getStackSize(); i++){
 			FloatProcessor fp = (FloatProcessor) imp.getImageStack().getProcessor(i+1).duplicate();
 			if(i==0) imagefloat=(float[])fp.getPixels();
 			else imagefloat=(float[]) ArrayUtils.addAll(imagefloat, (float[])fp.getPixels());
 	    	System.out.println("slice : "+(i+1)+"/"+imp.getStackSize());
 	    }
 	   
 	    return imagefloat;
	}
	
	/**
	 * Retrieve short data for 16 bits images 
	 * @param imp : image to analyze
	 * @return short array with data for each pixel
	 */
	public static short[] getShortData(ImagePlus imp) {
		short[] imageshort = null;         
 	    for(int i=0; i<imp.getStackSize(); i++){
 			ShortProcessor fp = (ShortProcessor) imp.getImageStack().getProcessor(i+1).duplicate();
 			if(i==0) imageshort=(short[])fp.getPixels();
 			else imageshort=(short[]) ArrayUtils.addAll(imageshort, (short[])fp.getPixels());
 			System.out.println("slice : "+(i+1)+"/"+imp.getStackSize());
 	    }
 	   
 	    return imageshort;
	}
	
	/**
	 * Concatenate 2 byte array
	 * @param A a byte array
	 * @param B a byte array
	 * @return concatenated byte array
	 */
	public static byte[] concatByte(byte[] A, byte[] B) {
	   byte[] C= new byte[A.length+B.length];
	   System.arraycopy(A, 0, C, 0, A.length);
	   System.arraycopy(B, 0, C, A.length, B.length);

	   return C;
	}
	
	/**
	 * Concatenate 2 short array
	 * @param A a short array
	 * @param B a short array
	 * @return concatenated short array
	 */
	public static short[] concatShort(short[] A, short[] B) {
		   short[] C= new short[A.length+B.length];
		   System.arraycopy(A, 0, C, 0, A.length);
		   System.arraycopy(B, 0, C, A.length, B.length);

		   return C;
	}
	
	/**
	 * Concatenate 2 float array
	 * @param A a float array
	 * @param B a float array
	 * @return concatenated float array
	 */
	public static float[] concatFloat(float[] A, float[] B) {
		   float[] C= new float[A.length+B.length];
		   System.arraycopy(A, 0, C, 0, A.length);
		   System.arraycopy(B, 0, C, A.length, B.length);

		   return C;
	}
	
	/**
	 * Clone an image
	 * Note : useless ATM
	 * @param title
	 * @param object
	 * @param showImage
	 * @return image cloned
	 */
	public static Object createImageForMatlabClient(String title, Object object, boolean showImage) {
        if (object instanceof byte[][]) {
            byte[][] is = (byte[][]) object;
    		return is.clone();
        } 
        else if (object instanceof short[][]) {
            short[][] is = (short[][]) object;
    		return is.clone();
        } 
        else if (object instanceof int[][]) {
            int[][] is = (int[][]) object;
    		return is.clone();
         } 
        else if (object instanceof float[][]) {
            float[][] is = (float[][]) object;
    		return is.clone();
        } 
        else if (object instanceof double[][]) {
            double[][] is = (double[][]) object;
    		return is.clone();
        } 
        else if (object instanceof byte[][][]) {
            byte[][][] is = (byte[][][]) object;
    		return is.clone();
        } 
        else if (object instanceof short[][][]) {
            short[][][] is = (short[][][]) object;
    		return is.clone();
        } 
        else if (object instanceof int[][][]) {
            int[][][] is = (int[][][]) object;
    		return is.clone();
        } 
        else if (object instanceof float[][][]) {
            float[][][] is = (float[][][]) object;
    		return is.clone();
        } 
        else if (object instanceof double[][][]) {
            double[][][] is = (double[][][]) object;
    		return is.clone();
        }
		else {
			System.out.println("Error message: Unknow type of images or volumes.");
			return null;
		}
    }
	
	/**
	 * Create ImagePlusExtended object from primary array (byte, short ....)
	 * @param title : title of the image
	 * @param object : array
	 * @param showImage : display image if true
	 * @return ImagePlusExtended
	 */
	public static ImagePlusExtended createImage(String title, Object object, boolean showImage) {
		ImagePlusExtended imp = null;
        int i = 0;
        if (object instanceof byte[][]) {
            byte[][] is = (byte[][]) object;
            int height = is.length;
            int width = is[0].length;
            ByteProcessor byteprocessor = new ByteProcessor(width, height);
            byte[] bp = (byte[]) byteprocessor.getPixels();
            int h = 0;
            while (h < height) {
                int w = 0;
                while (w < width) {
                    bp[i] = is[h][w];
                    w++;
                    i++;
                }
                i = ++h * width;
            }
            imp = new ImagePlusExtended(title, byteprocessor);
            
        } 
        else if (object instanceof short[][]) {
            short[][] is = (short[][]) object;
            int height = is.length;
            int width = is[0].length;
            ShortProcessor shortprocessor = new ShortProcessor(width, height);
            short[] sp = (short[]) shortprocessor.getPixels();
            int h = 0;
            while (h < height) {
                int w = 0;
                while (w < width) {
                    sp[i] = is[h][w];
                    w++;
                    i++;
                }
                i = ++h * width;
            }
            imp = new ImagePlusExtended(title, shortprocessor);
            
        } 
        else if (object instanceof int[][]) {
            int[][] is = (int[][]) object;
            int height = is.length;
            int width = is[0].length;
            ShortProcessor shortprocessor = new ShortProcessor(width, height);
            short[] sp = (short[]) shortprocessor.getPixels();
            int h = 0;
            while (h < height) {
                int w = 0;
                while (w < width) {
                    sp[i] = (short)is[h][w];
                    w++;
                    i++;
                }
                i = ++h * width;
            }
            imp = new ImagePlusExtended(title, shortprocessor);
         } 
        else if (object instanceof float[][]) {
            float[][] fs = (float[][]) object;
            int height = fs.length;
            int width = fs[0].length;
            FloatProcessor floatprocessor = new FloatProcessor(width, height);
            float[] fp = (float[])floatprocessor.getPixels();
            int h = 0;
            while (h < height) {
                int w = 0;
                while (w < width) {
                    fp[i] = fs[h][w];
                    w++;
                    i++;
                }
                i = ++h * width;
            }
            floatprocessor.resetMinAndMax();
            imp = new ImagePlusExtended(title, floatprocessor);
            
        } 
        else if (object instanceof double[][]) {
            double[][] ds = (double[][]) object;
            int height = ds.length;
            int width = ds[0].length;
            FloatProcessor floatprocessor = new FloatProcessor(width, height);
            float[] fp = (float[]) floatprocessor.getPixels();
            int h = 0;
            while (h < height) {
                int w = 0;
                while (w < width) {
                    fp[i] = (float) ds[h][w];
                    w++;
                    i++;
                }
                i = ++h * width;
            }
            floatprocessor.resetMinAndMax();
            imp = new ImagePlusExtended(title, floatprocessor);
            
        } 
        else if (object instanceof byte[][][]) {
            byte[][][] is = (byte[][][]) object;
            int height = is.length;
            int width = is[0].length;
            int stackSize = is[0][0].length;
            ImageStack imagestack = new ImageStack(width, height);
            for (int sz = 0; sz < stackSize; sz++) {
                ByteProcessor byteprocessor = new ByteProcessor(width, height);
                byte[] bp = (byte[]) byteprocessor.getPixels();
                i = 0;
                int h = 0;
                while (h < height) {
                    int w = 0;
                    while (w < width) {
                        bp[i] = is[h][w][sz];
                        w++;
                        i++;
                    }
                    i = ++h * width;
                }
                imagestack.addSlice("", byteprocessor);
            }
            imp = new ImagePlusExtended(title, imagestack);
            
        } 
        else if (object instanceof short[][][]) {
            short[][][] is = (short[][][]) object;
            int height = is.length;
            int width = is[0].length;
            int stackSize = is[0][0].length;
            ImageStack imagestack = new ImageStack(width, height);
            for (int sz = 0; sz < stackSize; sz++) {
                ShortProcessor shortprocessor  = new ShortProcessor(width, height);
                short[] sp = (short[]) shortprocessor.getPixels();
                i = 0;
                int h = 0;
                while (h < height) {
                    int w = 0;
                    while (w < width) {
                        sp[i] = is[h][w][sz];
                        w++;
                        i++;
                    }
                    i = ++h * width;
                }
                imagestack.addSlice("", shortprocessor);
            }
            imp = new ImagePlusExtended(title, imagestack);
            
        } 
        else if (object instanceof int[][][]) {
            int[][][] is = (int[][][]) object;
            int height = is.length;
            int width = is[0].length;
            int stackSize = is[0][0].length;
            ImageStack imagestack = new ImageStack(width, height);
            for (int sz = 0; sz < stackSize; sz++) {
                ShortProcessor shortprocessor  = new ShortProcessor(width, height);
                short[] sp = (short[]) shortprocessor.getPixels();
                i = 0;
                int h = 0;
                while (h < height) {
                    int w = 0;
                    while (w < width) {
                        sp[i] = (short) is[h][w][sz];
                        w++;
                        i++;
                    }
                    i = ++h * width;
                }
                if (sz == 0)
                    shortprocessor.resetMinAndMax();
                imagestack.addSlice("", shortprocessor);
                
            }
            imp = new ImagePlusExtended(title, imagestack);
            
        } 
        else if (object instanceof float[][][]) {
            float[][][] fs = (float[][][]) object;
            int height = fs.length;
            int width = fs[0].length;
            int stackSize = fs[0][0].length;
            ImageStack imagestack = new ImageStack(width, height);
            for (int sz = 0; sz < stackSize; sz++) {
                FloatProcessor floatprocessor = new FloatProcessor(width, height);
                float[] fp = (float[]) floatprocessor.getPixels();
                i = 0;
                int h = 0;
                while (h < height) {
                    int w = 0;
                    while (w < width) {
                        fp[i] = fs[h][w][sz];
                        w++;
                        i++;
                    }
                    i = ++h * width;
                }
                if (sz == 0)
                    floatprocessor.resetMinAndMax();
                imagestack.addSlice("", floatprocessor);
            }
            imp=new ImagePlusExtended(title, imagestack);
            
        } 
        else if (object instanceof double[][][]) {
            double[][][] ds = (double[][][]) object;
            int height = ds.length;
            int width = ds[0].length;
            int stackSize = ds[0][0].length;
            ImageStack imagestack = new ImageStack(width, height);
            for (int sz = 0; sz < stackSize; sz++) {
                FloatProcessor floatprocessor = new FloatProcessor(width, height);
                float[] fp = (float[]) floatprocessor.getPixels();
                i = 0;
                int h = 0;
                while (h < height) {
                    int w = 0;
                    while (w < width) {
                        fp[i] = (float) ds[h][w][sz];
                        w++;
                        i++;
                    }
                    i = ++h * width;
                }
                if (sz == 0)
                    floatprocessor.resetMinAndMax();
                imagestack.addSlice("", floatprocessor);
            }
            imp=new ImagePlusExtended(title, imagestack);
            
        }
		else {
			System.out.println("MIJ Error message: Unknow type of images or volumes.");
			return null;
		}
		
        if (showImage) {
			imp.show();
			imp.updateAndDraw();
		}
		return imp;
    }
	
	/**
	 * Build an ImagePlus from double or float array
	 * @param imi : double or float array
	 * @param name : name of the image
	 * @param width 
	 * @param height
	 * @return ImagePlus object
	 */
	public static ImagePlus buildImagePlusFromDoubleOrFloatArrays(Object imi, String name, int width, int height){
		float[][] im;
		if(imi instanceof double[][]) im=convertDoublesToFloats((double[][]) imi);
		else im=(float[][]) imi;
		if(im!=null){
			if(im.length==1){
				float[] tmp = new float[im[0].length];
				System.arraycopy(im[0], 0, tmp, 0, im[0].length);
				
				ImagePlus img = new ImagePlus(name, new FloatProcessor(width, height, tmp, null));
				return img;
			}
			else{ //Stack
				ImageStack is = new ImageStack(width, height);
				for(int i=0; i<im.length; i++){
					if(im[i]!=null){
						float[] tmp = new float[im[i].length];
						System.arraycopy(im[i], 0, tmp, 0, im[i].length);
						is.addSlice("lvl "+(i+1), new FloatProcessor(width, height, tmp, null));
					}
				}
				ImagePlus img = new ImagePlus(name, is);
				return img;
			}
		}
		return null;
	}
	
	/**
	 * Convert double array to float array
	 * @param input : 2D double array
	 * @return 2D float array
	 */
	public static float[][] convertDoublesToFloats(double[][] input)
	{
	    if (input == null)
	    {
	        return null; // Or throw an exception - your choice
	    }
	    float[][] output = new float[input.length][input[0].length];
	    for (int i = 0; i < input.length; i++)
	    {
	    	for (int j = 0; j < input[0].length; j++)
		    {
	    		output[i][j] = (float) input[i][j];
		    }
	    }
	    return output;
	}
	
	/**
	 * Build ImagePlus from byte array
	 * @param imi : byte array
	 * @param name : name of the image
	 * @param width 
	 * @param height
	 * @return ImagePlus object
	 */
   public static ImagePlus buildImagePlusFromByteArrays(byte[][] im, String name, int width, int height){
		if(im!=null){
			if(im.length==1){
				byte[] tmp = new byte[im[0].length];
				System.arraycopy(im[0], 0, tmp, 0, im[0].length);
				
				ImagePlus img = new ImagePlus(name, new ByteProcessor(width, height, tmp, null));
				return img;
			}
			else{ //Stack
				ImageStack is = new ImageStack(width, height);
				for(int i=0; i<im.length; i++){
					if(im[i]!=null){
						byte[] tmp = new byte[im[i].length];
						System.arraycopy(im[i], 0, tmp, 0, im[i].length);
						is.addSlice("lvl "+(i+1), new ByteProcessor(width, height, tmp, null));
					}
				}
				ImagePlus img = new ImagePlus(name, is);
				return img;
			}
		}
		return null;
	}

}
