package com.java.io;

import java.io.File;
import java.io.FileInputStream;
import java.lang.Character.Subset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Class to work with MATLAB functions.
 * Will format the functions.
 * @author Jérémy DEVERDUN
 *
 */
public class FunctionParser {
	
	/**
	 * Build raw code for specified file. Will replace each
	 * call to user defined function by its real source code
	 * @param f path to the m-file to analyse
	 * @return raw code
	 */
	public static String buildFunction(File f){
		String res="";
		String tmp=getTextFromFile(f);
		tmp=tmp.substring(0, tmp.lastIndexOf("\nend"));
		String[] lines=tmp.split("\n");
		
		for(int i=5;i<lines.length;i++){
			String e=lines[i];
			try {
				res+=checkBuildFun(e,f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(File.separator)));
			} catch (Exception e1) {
				res+=e;
				e1.printStackTrace();
			}
		}
		return res;
	}
	
	/**
	 * Check if a line contains a user-defined function
	 * @param e : string to analyze
	 * @param mdirectory : directory of the m-files
	 * @return raw code for the string
	 * @throws Exception
	 */
	private static String checkBuildFun(String e, String mdirectory) throws Exception {
		String res="";
		if(e.contains("%")) e=e.substring(0,e.indexOf("%"));
		if(!e.contains("my_") || e.charAt(0)=='%') return e+"\n";
		String regex="my_.+\\)";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = pattern.matcher(e);
		String fun="";
		while (m.find())
			fun=m.group();	
		if(e.split("=").length>1) res+=e.split("=")[0]+"=";
		String argin=fun.substring(fun.indexOf("(")+1,fun.lastIndexOf(")"));//.split("\\(")[1].split("\\)")[0];
		File mfile=new File(mdirectory+File.separator+fun.split("\\(")[0]+".m");
		if(!mfile.exists()){
			throw new Exception("Function "+fun.split("\\(")[0]+" not find in plugins folder");
		}
		
		res+="exec_fun("+argin+",strcat('"+(buildFunction(mfile).replaceAll("'", "''")).replaceAll("\n","','")+"'));\n";
		return res;
	}
	
	/**
	 * Retrieve content for file
	 * @param f : file to read
	 * @return
	 */
	private static String getTextFromFile(File f) {
		File file = f;
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
	    }
		return strContent.toString();
	}
}
