package com.java;


import com.Matlab.server.MatlabServer;

/**
 * Global variables
 * @author Jérémy DEVERDUN
 *
 */
public class ClassManagement {

	public static boolean MatlabServerIsRunning = false;
	public static MatlabServer matlabserver=null;
	public static String matlabserverDirectory = "C:\\Users\\Mobilette\\Documents\\Stage2010-2011\\MATLAB mcode\\programme\\nuclei_segmentation\\etape_3\\daemon\\matlabengine\\distrib\\";
	public static boolean isImageJ=false;
	public static String MCRroot="/usr/local/MATLAB/MATLAB_Compiler_Runtime";
	public static String updateSite="";
}
