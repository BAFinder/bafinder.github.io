import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;



import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.Timer;
import javax.swing.ProgressMonitor;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class BAFinder_GUI {

	protected Shell shlBafinderv;
	private Text mgfpos;
	private Text mgfneg;
	private Text msp;
	private Text rt;
	private Text mzwindow;
	private Text rtwindow;
	private Text outputfolder;
	public Button btnRun;
	
	
	public 	String[] temp=new String[8];
	public static String location = BAFinder_GUI.class.getProtectionDomain().getCodeSource().getLocation().getFile();
	public File p1,p2,p3,p4,p5,p6,p7,p8;
	double mw,rw;
	String pathway1,pathway2,pathway3,pathway4, pathway5,pathway6;
    public static double mzwin,rtwin;
	public GUIInput GI;
	public Timer timer;
	public static ProgressMonitor progressMonitor;
	public BAFinder task;

	  
	  
	  public class GUIInput
	    {
	    	String Pdir;
	    	String Ndir;
	    	String MS2lib;
	    	String rtlib;
	    	String output;
	    	String conju;
	    	double mzwindow;
	    	double Rtwindow;
	    	
	    	GUIInput(String p1, String p2, String p3,String p4,double m, double r, String p5, String p6)
	    	{
	    		this.Pdir=p1;
	    		this.Ndir=p2;
	    		this.MS2lib=p3;
	    		this.rtlib=p4;
	    		this.mzwindow=m;
	    		this.Rtwindow=r;
	    		this.output=p5;
	    		this.conju=p6;
	    	
	    		
	    	}
	    }
	  
	  
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		//SwingUtilities.invokeLater(new Runnable() {
		//public void run() {
		
		 Class c = java.lang.Character.Subset.class;
	      String innerClassName = c.getName();
	      System.out.println("The fully qualified name of the inner class is: " + innerClassName);
		BAFinder_GUI window = new BAFinder_GUI();
		window.open();
		}
		//}); 
		//}
		
			        
	    
	
		   
		   
	       
		 
	 
	/**
	 * Open the window.
	 */
	public void open()  {
		Display display = Display.getDefault();
		createContents();
		shlBafinderv.open();
		shlBafinderv.layout();
		while (!shlBafinderv.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlBafinderv = new Shell();
		shlBafinderv.setSize(561, 572);
		shlBafinderv.setText("BAFinder 2.0");
		
		
		//read the last visited directory from the log file
		
		if(new File(location+"BAFinder_file_directory.txt").isFile())
		{
		   try{
			   
        FileReader fr = new FileReader(new File(location+"BAFinder_file_directory.txt"));
      	Scanner scanner0 =new Scanner(fr);
      	int fn=0;
      	while(scanner0.hasNextLine())
			{
			   temp[fn]=scanner0.nextLine();
			   fn++;	   
      	}
      	scanner0.close();
      	fr.close();
		       }
		       catch (IOException ex)
		       {
			     ex.printStackTrace();
		       }
		}
		
		setTitle("BAFinder");
		
		Menu menu = new Menu(shlBafinderv, SWT.BAR);
		shlBafinderv.setMenuBar(menu);
		
		MenuItem mntmHelp_1 = new MenuItem(menu, SWT.NONE);
		mntmHelp_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			
				String message = "BAFinder ver 2.0"+"\n"
				        + "last updated: Dec.12th, 2022 \n"
				        + "Developer: Yan Ma \n"
				        + "Contact: mayan@nibs.ac.cn";
					 JOptionPane.showMessageDialog(null,message,"About BAFinder",1);
			}
		});
		mntmHelp_1.setText("about");
		
	
		
		
		Label lblMGFpos = new Label(shlBafinderv, SWT.NONE);
		lblMGFpos.setText("Input file folder in pos mode");
		lblMGFpos.setBounds(9, 75, 247, 20);
		
		Label lblMGFneg = new Label(shlBafinderv, SWT.NONE);
		lblMGFneg.setText("Input file folder in neg mode");
		lblMGFneg.setBounds(9, 15, 345, 20);
		
		mgfpos = new Text(shlBafinderv, SWT.BORDER);
		mgfpos.setBounds(9, 101, 525, 26);
		
		mgfneg = new Text(shlBafinderv, SWT.BORDER);
		mgfneg.setBounds(10, 41, 525, 26);
		
		Button btnOpen_mgfpos = new Button(shlBafinderv, SWT.NONE);
		btnOpen_mgfpos.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JFileChooser jFileChooser3;         	
                jFileChooser3 = new JFileChooser();
                if(temp[0]!=null)
                {
                	jFileChooser3.setCurrentDirectory(new File(temp[0]));
                }
                
                jFileChooser3.setDialogType(1);
                
                jFileChooser3.setDialogTitle("Select File Folder");
                jFileChooser3.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
              
                jFileChooser3.setAcceptAllFileFilterUsed(false);
                
                int retVal = jFileChooser3.showOpenDialog(new JFrame());
                switch( retVal )
                {
                    case JFileChooser.CANCEL_OPTION:
                        break;
                    case JFileChooser.APPROVE_OPTION:
                    {
                    	if(jFileChooser3.getSelectedFile().isDirectory()) 
                    	{
                    	mgfpos.setText(jFileChooser3.getSelectedFile().getAbsolutePath());
                    	p3=jFileChooser3.getCurrentDirectory();	
                    	}
                    	//directory=jFileChooser1.getCurrentDirectory().getPath();  
                        break;
                         
                         }
                    }
			}
		});
		btnOpen_mgfpos.setText("Open");
		btnOpen_mgfpos.setBounds(469, 73, 66, 25);
		
		Button btnOpen_mgfneg = new Button(shlBafinderv, SWT.NONE);
		btnOpen_mgfneg.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JFileChooser jFileChooser4;         	
                jFileChooser4 = new JFileChooser();
                if(temp[1]!=null)
                {
                	jFileChooser4.setCurrentDirectory(new File(temp[1]));
                }
                
                jFileChooser4.setDialogType(1);
                
                jFileChooser4.setDialogTitle("Select File Folder");
                jFileChooser4.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                
                jFileChooser4.setAcceptAllFileFilterUsed(false);
                int retVal = jFileChooser4.showOpenDialog(new JFrame());
                switch( retVal )
                {
                    case JFileChooser.CANCEL_OPTION:
                        break;
                    case JFileChooser.APPROVE_OPTION:
                    {
                    	if(jFileChooser4.getSelectedFile().isDirectory()) 
                    	{
                    	mgfneg.setText(jFileChooser4.getSelectedFile().getAbsolutePath());
                    	p4=jFileChooser4.getCurrentDirectory();	
                    	}
                    	//directory=jFileChooser1.getCurrentDirectory().getPath();  
                        break;
                         
                         }
                    }
			}
		});
		btnOpen_mgfneg.setText("Open");
		btnOpen_mgfneg.setBounds(470, 10, 66, 25);
		
		Label lblMSPlib = new Label(shlBafinderv, SWT.NONE);
		lblMSPlib.setText("MS/MS library (MSP)");
		lblMSPlib.setBounds(9, 133, 137, 20);
		
		msp = new Text(shlBafinderv, SWT.BORDER);
		msp.setBounds(9, 159, 525, 26);
		
		Button btnOpen_msp = new Button(shlBafinderv, SWT.NONE);
		btnOpen_msp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JFileChooser jFileChooser5;         	
                jFileChooser5 = new JFileChooser();
                if(temp[2]!=null)
                {
                	jFileChooser5.setCurrentDirectory(new File(temp[2]));
                }
               
                jFileChooser5.setDialogType(1);
                
                jFileChooser5.setDialogTitle("Open File");
                //Only show csv files
                jFileChooser5.setAcceptAllFileFilterUsed(false);
                jFileChooser5.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    public boolean accept(File f) { 
                      if(f.getName().toUpperCase().endsWith(".MSP")||f.isDirectory()){
                        return true;
                      }
                      return false;
                    }
                    public String getDescription() {
	                      return "MSP file(*.msp)";
	                    }
                  });
                int retVal = jFileChooser5.showOpenDialog(new JFrame());
                switch( retVal )
                {
                    case JFileChooser.CANCEL_OPTION:
                        break;
                    case JFileChooser.APPROVE_OPTION:
                    {
                    	msp.setText(jFileChooser5.getSelectedFile().getAbsolutePath());
                    	p5=jFileChooser5.getCurrentDirectory();	
                    	//directory=jFileChooser1.getCurrentDirectory().getPath();  
                        break;
                         
                         }
                    }
			}
		});
		btnOpen_msp.setText("Select");
		btnOpen_msp.setBounds(468, 131, 66, 25);
		
		Label lblRTlib = new Label(shlBafinderv, SWT.NONE);
		lblRTlib.setText("(Optional) retention time library (txt)");
		lblRTlib.setBounds(9, 193, 268, 20);
		
		rt = new Text(shlBafinderv, SWT.BORDER);
		rt.setBounds(9, 219, 525, 26);
		
		Button btnOpen_rt = new Button(shlBafinderv, SWT.NONE);
		btnOpen_rt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JFileChooser jFileChooser6;         	
                jFileChooser6 = new JFileChooser();
                if(temp[3]!=null)
                {
                	jFileChooser6.setCurrentDirectory(new File(temp[3]));
                }
              
                jFileChooser6.setDialogType(1);
                
                jFileChooser6.setDialogTitle("Open File");
                //Only show txt files
                jFileChooser6.setAcceptAllFileFilterUsed(false);
                jFileChooser6.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    public boolean accept(File f) { 
                      if(f.getName().toUpperCase().endsWith(".TXT")||f.isDirectory()){
                        return true;
                      }
                      return false;
                    }
                    public String getDescription() {
	                      return "TXT file(*.txt)";
	                    }
                  });
                int retVal = jFileChooser6.showOpenDialog(new JFrame());
                switch( retVal )
                {
                    case JFileChooser.CANCEL_OPTION:
                        break;
                    case JFileChooser.APPROVE_OPTION:
                    {
                    	rt.setText(jFileChooser6.getSelectedFile().getAbsolutePath());
                    	p6=jFileChooser6.getCurrentDirectory();	
                    	//directory=jFileChooser1.getCurrentDirectory().getPath();  
                        break;
                         
                         }
                    }
			}
		});
		btnOpen_rt.setText("Select");
		btnOpen_rt.setBounds(468, 191, 66, 25);
		
		Label lblmzwindow = new Label(shlBafinderv, SWT.NONE);
		lblmzwindow.setText("m/z tolerance (Da)");
		lblmzwindow.setBounds(10, 264, 137, 20);
		
		mzwindow = new Text(shlBafinderv, SWT.BORDER);
		mzwindow.setBounds(153, 258, 87, 26);
		
		Label lblrtwindow = new Label(shlBafinderv, SWT.NONE);
		lblrtwindow.setText("RT tolerance (min)");
		lblrtwindow.setBounds(261, 264, 137, 20);
		
		rtwindow = new Text(shlBafinderv, SWT.BORDER);
		rtwindow.setBounds(404, 258, 87, 26);
		
		Label lbloutput = new Label(shlBafinderv, SWT.NONE);
		lbloutput.setText("Output directory");
		lbloutput.setBounds(9, 292, 137, 20);
		
		outputfolder = new Text(shlBafinderv, SWT.BORDER);
		outputfolder.setBounds(9, 318, 525, 26);
		
		Button btnExport = new Button(shlBafinderv, SWT.NONE);
		btnExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JFileChooser jFileChooser7;
                jFileChooser7 = new JFileChooser();
                if(temp[4]!=null)
                {
                	jFileChooser7.setCurrentDirectory(new File(temp[4]));
                }
               
                jFileChooser7.setDialogType(JFileChooser.SAVE_DIALOG);
                jFileChooser7.setDialogTitle("Choose a directory to export result files");
                jFileChooser7.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jFileChooser7.setApproveButtonText("Save");
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
                int retVal = jFileChooser7.showSaveDialog(new JFrame());
                switch( retVal )
                {
                    case JFileChooser.CANCEL_OPTION:
                        break;
                    case JFileChooser.APPROVE_OPTION:
                    {
                    	String writePath = jFileChooser7.getSelectedFile().getAbsolutePath();
                    	p7=jFileChooser7.getCurrentDirectory();
                    	outputfolder.setText(writePath);
                         break;
                         
                         }
                    }
			}
		});
		btnExport.setText("Select");
		btnExport.setBounds(468, 290, 66, 25);
		
		btnRun = new Button(shlBafinderv, SWT.NONE);
		btnRun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				btnRun.setEnabled(false);
				
				pathway1=mgfpos.getText();
				pathway2=mgfneg.getText();
				pathway3=msp.getText();
				
				if(rt.getText().isEmpty())
					pathway4="";
				else
				pathway4=rt.getText();
				
	            mzwin=Double.parseDouble(mzwindow.getText());
	            rtwin=Double.parseDouble(rtwindow.getText());
	            pathway5=outputfolder.getText();
	            pathway6=custom.getText();
	            
	            
	          
	         // Write the directory for each file chooser into a log file
	          
	            
				try
	            {
	            
	            FileWriter fw = new FileWriter(new File(location+"BAFinder_file_directory.txt"));
	            fw.write(Paths.get(pathway1).getParent()+"\n");
	            fw.write(Paths.get(pathway2).getParent()+"\n");
	            fw.write(Paths.get(pathway3).getParent()+"\n");
		       	  
		       	  if(pathway4.equals(""))
		       		 fw.write(Paths.get(pathway1).getParent()+"\n");
		       	  else
		       		 fw.write(Paths.get(pathway4).getParent()+"\n");
		       	  
		       	fw.write(Paths.get(pathway5).getParent()+"\n");
		       	
		        if(pathway6.equals(""))
		       		 fw.write(Paths.get(pathway1).getParent()+"\n");
		       	  else
		       		 fw.write(Paths.get(pathway6).getParent()+"\n");
		         
		       	  fw.close();
		            }
		            catch (Exception ec)
		            {
		          	  ec.printStackTrace();
		          	 
		            }

	       	 
                /*
				pathway2 ="G:\\Data\\102422_Ma_Yan_BA_AABA_feces_sample\\BAFinder_rawconverter_neg_human2_dog_rat_110822";
				pathway1 ="G:\\Data\\102422_Ma_Yan_BA_AABA_feces_sample\\BAFinder_rawconverter_pos_human2_dog_rat_110822";
				pathway3 ="G:\\BAFinder\\3. Library\\MSMS_library_with_AABA_110222_correct_combine_Leu_Ile.msp";
				pathway4="G:\\BAFinder\\3. Library\\RT_library_AABA_121622_clean_upload.txt";
				mzwin=0.005;
				rtwin=0.05;
				pathway5="G:\\Data\\102422_Ma_Yan_BA_AABA_feces_sample\\BAFinder_result_122622_repeat";
				//pathway6="G:\\BAFinder\\3. Library\\Customized_conjugate_test_120122.txt";
				pathway6="G:\\BAFinder\\3. Library\\Customized_conjugate_update_121222.txt";
				*/
	            
	            GI=new GUIInput(pathway1,pathway2,pathway3,pathway4,mzwin,rtwin,pathway5,pathway6); 
	            task=new BAFinder(GI);
	            
	            if(timer==null)
	            {
	            try {
	            	SwingUtilities.invokeLater(new Runnable() {
	            		public void run() {	
	            	 JFrame component = new JFrame();
	                  progressMonitor = new ProgressMonitor(component, "", "", 0, 100);
	               	  progressMonitor.setProgress(0);
	               	  timer=new Timer(500, al6);       	 
               	      timer.start();
	               	  task.execute();
	            		 }
	                });
	            }
	            	catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            }
	        
	            //btnRun.setEnabled(true);
	            
	         
			}
			
		});
		btnRun.setText("Run");
		btnRun.setBounds(229, 460, 125, 30);
		
		
		
		Label lblcus = new Label(shlBafinderv, SWT.NONE);
		lblcus.setText("(Optional)custom conjugates import:");
		lblcus.setBounds(9, 394, 311, 20);
		lblcus.setVisible(false);
		
		custom = new Text(shlBafinderv, SWT.BORDER);
		custom.setBounds(9, 420, 525, 26);
		custom.setVisible(false);
		
		Button customimport = new Button(shlBafinderv, SWT.NONE);
		customimport .setText("Select");
		customimport .setBounds(468, 389, 66, 25);
		customimport.setVisible(false);
		
		customimport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JFileChooser jFileChooser8;         	
                jFileChooser8 = new JFileChooser();
                if(temp[5]!=null)
                {
                	jFileChooser8.setCurrentDirectory(new File(temp[5]));
                }
              
                jFileChooser8.setDialogType(1);
                
                jFileChooser8.setDialogTitle("Open File");
                //Only show txt files
                jFileChooser8.setAcceptAllFileFilterUsed(false);
                jFileChooser8.setFileFilter(new javax.swing.filechooser.FileFilter() {
                    public boolean accept(File f) { 
                      if(f.getName().toUpperCase().endsWith(".TXT")||f.isDirectory()){
                        return true;
                      }
                      return false;
                    }
                    public String getDescription() {
	                      return "TXT file(*.txt)";
	                    }
                  });
                int retVal = jFileChooser8.showOpenDialog(new JFrame());
                switch( retVal )
                {
                    case JFileChooser.CANCEL_OPTION:
                        break;
                    case JFileChooser.APPROVE_OPTION:
                    {
                    	custom.setText(jFileChooser8.getSelectedFile().getAbsolutePath());
                    	p7=jFileChooser8.getCurrentDirectory();	
                    	//directory=jFileChooser1.getCurrentDirectory().getPath();  
                        break;
                         
                         }
                    }
			}
		});
		
		Button btnNewButton = new Button(shlBafinderv, SWT.NONE);
		btnNewButton.setBounds(9, 358, 125, 30);
		btnNewButton.setText("Show Advanced");
		
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblcus.setVisible(!lblcus.isVisible());
				customimport.setVisible(!customimport.isVisible());
				custom.setVisible(!custom.isVisible());
				if(lblcus.isVisible())
				{
					btnNewButton.setText("Hide Advanced");
				}
				else
					btnNewButton.setText("Show Advanced");
				
			}
		});
		
		


	}
	
	
    ActionListener al6=new ActionListener()
  	 {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		 
   		 progressMonitor.setProgress(BAFinder.progress);
  	    // String message = String.format("Completed %d%%.\n", BAFinder.progress);
   		
  	     progressMonitor.setNote(BAFinder.message);
  	      if (progressMonitor.isCanceled() || task.isDone()) {
  	    	  
  	    	 if (progressMonitor.isCanceled()) {
  	    		
  		          task.cancel(true);
  	    		 
  		      } 
  	    	 progressMonitor.close();
  	    	 timer.stop();
  	    	 timer=null;
  	    	 Display.getDefault().syncExec(new Runnable() {
  	    	    public void run() {
  	    	        // code that affects the GUI
  	    	    	btnRun.setEnabled(true);
  	    	    }
  	    	});
  	    	 
  	      }
	}
  	 
  	 };
    private Text custom;

	private void setTitle(String string) {
		// TODO Auto-generated method stub
		
	}
}
