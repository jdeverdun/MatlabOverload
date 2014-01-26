package com.java.display.menu;

import ij.IJ;
import ij.WindowManager;

import javax.swing.JPanel;


import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import com.java.modeles.MatlabFunction;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


/**
 * Check the functions args to build
 * the dialog box which will ask the user
 * for them
 * @author Jérémy DEVERDUN
 *
 */
public class MatlabFunctionCall extends JPanel{
	private String[][] args;
	private JTextField[] listtxt;
	private MatlabFunction mfun;
	private JFrame frame;
	private boolean needDisplay=true;
	
	/**
	 * 
	 * @param a list of arguments with type : "matrix", "value", "array", "file", "directory", "image"
	 * @param c MatlabFunction corresponding
	 */
	public MatlabFunctionCall(String[][] a,MatlabFunction c){
		if(a!=null){
			this.setArgs(a);
			listtxt=new JTextField[a.length];
			this.mfun=c;
			this.initButtons();
		}
	}
	
	/**
	 * Build the buttons
	 */
	private void initButtons() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{43, 22, 4, 153, 0, 0};
		gridBagLayout.rowHeights = new int[]{36, 20, 23, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		int tot=0;
		final int nbimagesIn=mfun.getImageInputList().size();
		if(this.getArgs().length==1 && getArgs()[0][1].equals("image")){
			String res[][]=new String[getArgs().length][2];
			res[0][0]=getArgs()[0][0];
			res[0][1]=getArgs()[0][1];
			if(nbimagesIn==1) mfun.getImages2send().add(IJ.getImage().getTitle());
			mfun.setArgin(res);
			needDisplay=false;
		}else{
			needDisplay=true;
			for(int i=0;i<this.getArgs().length;i++){
				if(!(this.getArgs()[i][1].equals("image")) || nbimagesIn>1){
					JLabel lblArg = new JLabel(this.getArgs()[i][0]);
					GridBagConstraints gbc_lblArg = new GridBagConstraints();
					gbc_lblArg.anchor = GridBagConstraints.WEST;
					gbc_lblArg.insets = new Insets(0, 0, 5, 5);
					gbc_lblArg.gridx = 1;
					gbc_lblArg.gridy = i+1;
					add(lblArg, gbc_lblArg);
					
					JLabel label = new JLabel(":");
					GridBagConstraints gbc_label = new GridBagConstraints();
					gbc_label.anchor = GridBagConstraints.WEST;
					gbc_label.insets = new Insets(0, 0, 5, 5);
					gbc_label.gridx = 2;
					gbc_label.gridy = i+1;
					add(label, gbc_label);
					final JTextField txtArgval = new JTextField();
					txtArgval.setText("");
					if((this.getArgs()[i][1].equals("select"))){
						final String[] petStrings =  this.getArgs()[i][0].split("\\/") ;
						final JComboBox petList = new JComboBox(petStrings);
						final String ty=this.getArgs()[i][1];
						txtArgval.setText(petStrings[0]);
						petList.addActionListener(new ActionListener() {
				        	public void actionPerformed(ActionEvent ae) {
				        		txtArgval.setText(petStrings[petList.getSelectedIndex()]);
				        	}
				        });
						
						GridBagConstraints gbc_fileval = new GridBagConstraints();
						gbc_fileval.anchor = GridBagConstraints.NORTH;
						gbc_fileval.fill = GridBagConstraints.HORIZONTAL;
						gbc_fileval.insets = new Insets(0, 0, 5, 0);
						gbc_fileval.gridx = 3;
						gbc_fileval.gridy = i+1;
						add(petList, gbc_fileval);
						txtArgval.setVisible(false);
					}else{
						if(nbimagesIn>1 && (this.getArgs()[i][1].equals("image"))){
							int[] ID=WindowManager.getIDList();
							final String[] windowTitle=new String[ID.length]; 
							int count=0;
							for(int wid:ID){
								windowTitle[count++]=WindowManager.getImage(wid).getTitle();
							}
							final JComboBox winList = new JComboBox(windowTitle);
							final String ty=this.getArgs()[i][1];
							txtArgval.setText(windowTitle[winList.getSelectedIndex()]);
							winList.addActionListener(new ActionListener() {
					        	public void actionPerformed(ActionEvent ae) {
					        		txtArgval.setText(windowTitle[winList.getSelectedIndex()]);
					        	}
					        });
							
							GridBagConstraints gbc_fileval = new GridBagConstraints();
							gbc_fileval.anchor = GridBagConstraints.NORTH;
							gbc_fileval.fill = GridBagConstraints.HORIZONTAL;
							gbc_fileval.insets = new Insets(0, 0, 5, 0);
							gbc_fileval.gridx = 3;
							gbc_fileval.gridy = i+1;
							add(winList, gbc_fileval);
							txtArgval.setVisible(false);
						}
					}
					GridBagConstraints gbc_txtArgval = new GridBagConstraints();
					gbc_txtArgval.anchor = GridBagConstraints.NORTH;
					gbc_txtArgval.fill = GridBagConstraints.HORIZONTAL;
					gbc_txtArgval.insets = new Insets(0, 0, 5, 0);
					gbc_txtArgval.gridx = 3;
					gbc_txtArgval.gridy = i+1;
					add(txtArgval, gbc_txtArgval);
					txtArgval.setColumns(10);
					listtxt[i]=txtArgval;
					if((this.getArgs()[i][1].equals("file")) || (this.getArgs()[i][1].equals("directory"))){
						JButton jbselectfile=new JButton("...");
						final String ty=this.getArgs()[i][1];
						jbselectfile.addActionListener(new ActionListener() {
				        	public void actionPerformed(ActionEvent ae) {
				        		selectfolder(ty,txtArgval);
				        	}
				        });
						
						GridBagConstraints gbc_fileval = new GridBagConstraints();
						gbc_fileval.anchor = GridBagConstraints.NORTH;
						gbc_fileval.fill = GridBagConstraints.HORIZONTAL;
						gbc_fileval.insets = new Insets(0, 0, 5, 0);
						gbc_fileval.gridx = 4;
						gbc_fileval.gridy = i+1;
						add(jbselectfile, gbc_fileval);
					}
				}
				tot=i+1;
			}
			JButton btnOk = new JButton("ok");
			btnOk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					String res[][]=new String[getArgs().length][2];
					for(int i=0;i<listtxt.length;i++){
						if((getArgs()[i][1].equals("image"))){
							res[i][0]=getArgs()[i][0];
							res[i][1]=getArgs()[i][1];
							if(nbimagesIn==1) mfun.getImages2send().add(IJ.getImage().getTitle());
							else{
								System.out.println(listtxt[i].getText());mfun.getImages2send().add(listtxt[i].getText());
							}
						}else{
							res[i][0]=listtxt[i].getText();
							res[i][1]=getArgs()[i][1];
						}
					}
					mfun.setArgin(res);
					frame.dispose();
				}
			});
			GridBagConstraints gbc_btnOk = new GridBagConstraints();
			gbc_btnOk.anchor = GridBagConstraints.NORTHWEST;
			gbc_btnOk.insets = new Insets(0, 0, 0, 5);
			gbc_btnOk.gridx = 1;
			gbc_btnOk.gridy = tot+1;
			add(btnOk, gbc_btnOk);
			
			JButton btnCancel = new JButton("cancel");
			GridBagConstraints gbc_btnCancel = new GridBagConstraints();
			gbc_btnCancel.insets = new Insets(0, 0, 0, 5);
			gbc_btnCancel.anchor = GridBagConstraints.NORTHWEST;
			gbc_btnCancel.gridx = 3;
			gbc_btnCancel.gridy = tot+1;
			add(btnCancel, gbc_btnCancel);
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					mfun.setIscancel(true);
					frame.dispose();
				}
			});
		}
	}
	
	/**
	 * 
	 * @return boolean : true if the user must be prompt for args
	 */
	public boolean toShow(){
		return needDisplay;
	}
	
	
	/**
	 * Display GUI
	 */
	public void createAndShowGUI(){
		if(args!=null){
			frame=new JFrame("Arguments for "+mfun.getMfile().getName());
			frame.getContentPane().setLayout(new BorderLayout());
	        //Create and set up the content pane.
	        this.setOpaque(true); //content panes must be opaque
	        frame.setContentPane(this);
	        Toolkit kit = Toolkit.getDefaultToolkit();
	    	Dimension dim = kit.getScreenSize();
	    	frame.setLocation(dim.width/2-400/2, dim.height/2-400/2);
	        //Display the window.
	        frame.pack();
	        frame.setVisible(true);
		}
	}
	
	/**
	 * Test if the frame is showing
	 */
	@Override
	public boolean isShowing(){
		if(args==null || !needDisplay) return false;
		return frame.isShowing();
	}
	
	/**
	 * Select file or folder
	 * @param t : type "file" or "folder" 
	 * @param jtf : JTextField linked to this arg
	 */
	public void selectfolder(String t,JTextField jtf){
		JFileChooser filechoos = new JFileChooser();
		filechoos.setDialogTitle("Select a "+t);
		if(t.equals("file"))
			filechoos.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		else
			filechoos.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    //
		    // disable the "All files" option.
		    //
		filechoos.setAcceptAllFileFilterUsed(false);
		    //    
		    if (filechoos.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
		      System.out.println("getCurrentDirectory(): " 
		         +  filechoos.getCurrentDirectory());
		      System.out.println("getSelectedFile() : " 
		         +  filechoos.getSelectedFile());
		      jtf.setText(filechoos.getSelectedFile().getAbsolutePath());
		      }
		    else {
		      System.out.println("No Selection ");
		      }
	}
	
	/**
	 * Set argin
	 * @param args : list of argin 
	 */
	public void setArgs(String[][] args) {
		this.args = args;
		
	}
	
	/**
	 * 
	 * @return argin
	 */
	public String[][] getArgs() {
		return args;
	}

}
