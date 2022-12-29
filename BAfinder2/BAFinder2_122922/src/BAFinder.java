//combine leucine and isoleucine

import java.awt.Desktop;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutionException;
import org.apache.commons.io.FileUtils;
import java.math.BigDecimal;
import java.time.LocalTime; 
import java.time.format.*;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import java.nio.file.*;




public class BAFinder extends SwingWorker<String, Object> {
	
    public static BAFinder_GUI.GUIInput GI;
	
	public BAFinder(BAFinder_GUI.GUIInput GU)
	{
		GI=GU;
	}
	public static int progress;
	public static String message; 
	public int current;
	public int length;
    
	//accurate calculation of substraction
	 public static double sub(double v1, double v2) {
 
		  BigDecimal b1 = new BigDecimal(Double.toString(v1));
	      BigDecimal b2 = new BigDecimal(Double.toString(v2));
	      return b1.subtract(b2).doubleValue();
    }
	
	public static class XCMS
	{
		String mode;
		int pcnum;
		double mz;
		double rt;
		double intensity[];
		List<Integer> peakid;
		
		XCMSpeak samplertinfo[];
		boolean passMS1filter;
		List<MSMS> MS2;
		MSMS selectedMS2;
		
		XCMS(String o, int p, double m,double r, List<Integer> x,double[] in)
		{
			this.mode=o;
			this.pcnum=p;
			this.mz=m;
			this.rt=r;
			this.peakid=x;
			this.intensity=in;	
		}
		XCMS()
		{
			
		}
		 
		
		 void addinfo(XCMSpeak[] a)
		 {
			 this.samplertinfo=a;
		 }
		 
		 void MS1filtered(boolean t)
		 {
			 this.passMS1filter=t;
		 }
		 
		 void initiateMS2()
		 {
			 MS2=new ArrayList<MSMS>();
		 }
		 void addMS2(MSMS m)
		 {
			 MS2.add(m);
		 }
		 void selectMS2(MSMS m)
		 {
			 selectedMS2=m;
		 }
	}
	
	public static class XCMSpeak
	{

		int peakid;
		double rt;
		double rtmin;
		double rtmax;
		int sampleid;
	
		
		XCMSpeak(int p, double r,double rmin, double rmax,int s)
		{
			
			this.peakid=p;
			this.rt=r;
			this.rtmin=rmin;
			this.rtmax=rmax;
			this.sampleid=s;
		
			
		}
	
	}
	
	public static class mzlib
	{
		double mz;
		String id;
		mzlib(double m, String i)
		{
			this.mz=m;
			this.id=i;
		}
	}
	
	
	public static class userconju
	{
		double mass;
		String id;
		userconju(String i,double m)
		{
			this.mass=m;
			this.id=i;
		}
	}
	
	public static class AABAlib
	{
	    String type;
	    double prediff;
	    ArrayList<Double> promz;
	    AABAlib(String i,double m, ArrayList<Double> f)
		{
	    	this.type=i;
			this.prediff=m;
			this.promz=f;
			
		}
	    
	}
	
	public static class MS1
	{
		XCMS peak;
		List<String> compound;
		List<String> adduct;
		double mzerror;
	
		 MS1(XCMS p, List<String> c,List<String> a, double e)
			{
				this.peak=p;
				this.compound=c;
				this.adduct=a;
				this.mzerror=e;
			}
		
		 
	}
	
	public static class MSMS
	{
		int fileorder;
		String title;
		String mode;
		double rt;
	    double pepmass;
	    double intensity;
	    String charge;
		int peaknum;
		int normalizepeaknum;
		List<String> spectra;
		List<double[]> fragment;
		List<double[]> sqrtfragment;
		List<double[]> normalizefragment;
		String name;
		String adduct;
		String hit;
		String hitadduct;
		double hitpremz;
		double dotproduct;
		int preinlib;
		String modifiedhit;
		String modifiedhittype;
		String modifiedhitadduct;
		double modifieddotproduct;
		double modifiedtopNdotproduct;
		String type;
		String hittype;
		
		
		MSMS(int f,String p1,  String m,double r,double p, double i,String ch,int a, List<String> sp)
		{
			this.fileorder=f;
			this.title=p1;
			this.mode=m;
			this.rt=r;
			this.pepmass=p;
			this.intensity=i;
			this.charge=ch;
			this.peaknum=a;
			this.spectra=sp;
			
		}
		MSMS(String n, String t,String a, String m,double p, List<double[]> s)
		{
			this.name=n;
			this.type=t;
			this.adduct=a;
			this.mode=m;
			this.pepmass=p;
			this.sqrtfragment=s;
		}
		
		
		 void addfragment(List<double[]> f)
		 {
			 this.fragment=f;
		 }
		 void sqrtfragments(List<double[]> f)
		 {
			 this.sqrtfragment=f;
		 }
		 void normalizefragments(List<double[]> f)
		 {
			 this.normalizefragment=f;
		 }
		 void normalizepeaknum(int p)
		 {
			 this.normalizepeaknum=p;
		 }
	
		 void libmatch(String h,String t,String a, double d, double hp)
		 {
			 this.hit=h;
			 this.hittype=t;
			 this.hitadduct=a;
			 this.dotproduct=d;
			 this.hitpremz=hp;
		 }
		 void modifiedlibmatch(String h,String t,String a, double d)
		 {
			 this.modifiedhit=h;
			 this.modifiedhittype=t;
			 this.modifiedhitadduct=a;
			 this.modifieddotproduct=d;
		 }
		 void modifiedtopNscore(double d)
		 {
			 modifiedtopNdotproduct=d;
		 }
		 void inlib(int i)
		 {
			 this.preinlib=i;
		 }
	}
	
	public static class group
	{
		XCMS peak;
		String compound;
		String adduct;
		List<MSMS> MS2;
		MSMS selectedMS2;
		double feature;
		double lowfeature;
		double ratio;
		String comment;
		String MS2same500;
		String modifiedMS2same500;
		double mzerror;
		
		String modifiedhit;
		String modifiedhittype;
		String modifiedhitadduct;
		double modifieddotproduct;
		double modifiedtopNdotproduct;
		String type;
		String hittype;
		
		int modifiedinlib;
	
		
		 group(XCMS p, String c,String a)
		{
			this.peak=p;
			this.compound=c;
			this.adduct=a;
		}
		 
		 group(XCMS p, String c,String a, double m)
			{
				this.peak=p;
				this.compound=c;
				this.adduct=a;
				this.mzerror=m;
			}
		 
		group(XCMS p)
		{
			this.peak=p;
		}
		 
		 void initiateMS2()
		 {
			 MS2=new ArrayList<MSMS>();
		 }
		 void addMS2(MSMS m)
		 {
			 MS2.add(m);
		 }
		 void selectMS2(MSMS m)
		 {
			 selectedMS2=m;
		 }
		 void addfeature(double s)
		 {
			 feature=s;
		 }
		  void addratio(double s)
		  {
			  ratio=s;
		  }
		 void addlowfeature(double s)
		 {
			 lowfeature=s;
		 }
		 void addcomment(String c)
		 {
			 comment=c;
		 }
		 void MS2match(String m)
		 {
			 MS2same500=m;
		 }
		 void modifiedMS2match(String m)
		 {
			 modifiedMS2same500=m;
		 }
		 void modifiedlibmatch(String h,String t,String a, double d)
		 {
			 this.modifiedhit=h;
			 this.modifiedhittype=t;
			 this.modifiedhitadduct=a;
			 this.modifieddotproduct=d;
		 }
		 void modifiedtopNscore(double d)
		 {
			 modifiedtopNdotproduct=d;
		 }
		 void inlib(int i)
		 {
			 this.modifiedinlib=i;
		 }
	
	}

	public static class ingroup
	{
		int peaknum;
		String id;
		
		 ingroup(int p, String i)
		{
			this.peaknum=p;
			this.id=i;
		}
	}
	
	
	public static class std
	{
		String species;
		double rt;
		String name;
		double rtwinleft;
		double rtwinright;
		boolean customized;
		
	     std(String c, double r, String n)
		{
	       
			this.species=c;
			this.rt=r;
			this.name=n;
			this.customized=false;
		
		}
	     std(String c, double r, String n,double l, double i)
			{
		       
				this.species=c;
				this.rt=r;
				this.name=n;
				this.rtwinleft=l;
				this.rtwinright=i;
				this.customized=true;
			
			}
}

	public static class rtresult
	{
		String std;
		private double rterror;
		
		rtresult(String s, double r)
		{
			this.std=s;
			this.rterror=r;
		}
		public double getRterror()
		{
			return rterror;
		}
	}
	
	
	
	
	//calculate dot product score
	public static double dotproduct(List<double[]> unknown, List<double[]> lib,double mztol)
	{
		double LU=0;
    	double L2=0;
    	double U2=0;
    	double dotproduct=0;
    	
    	int[] lfragmentmatched=new int[lib.size()];
    	for(int s=0;s<unknown.size();s++)
    	{   
    		int ufragmentmatched=0;
    		for (int u=0;u<lib.size();u++)
    	{
    		if(Math.abs(unknown.get(s)[0]-lib.get(u)[0])<mztol)
    		{
    			double L=lib.get(u)[1];
    			double U=unknown.get(s)[1];
    			LU=LU+L*U;
    			L2=L2+L*L;
    			U2=U2+U*U;
    			ufragmentmatched++;
    			lfragmentmatched[u]++;
    		}
    	}
    		if(ufragmentmatched==0)
    		{
    			double U=unknown.get(s)[1];
    			U2=U2+U*U;
    		}
    			
    	}
    	for (int u=0;u<lib.size();u++)
    	{
    		if(lfragmentmatched[u]==0)
    		{
    			double L=lib.get(u)[1];
    			L2=L2+L*L;
    		}
    	}
    	dotproduct=LU/(Math.sqrt(L2)*Math.sqrt(U2))*1000;
    	return dotproduct;
	}
	
	//remove a product ion from MS/MS spectra
	//update 120222:input: normalizedfragment, output: after product ion removed, find new basepeak, normalize to it*999, sqrt (optional y, or n)
	public static List<double[]> removefragment(List<double[]> original,double mz, double mztol)
	{
		
		//This is a shallow copy, operation to passed will still affect original: List<double[]> passed=original;
		//a deep copy is need to copy each object in the arraylist, see below
		List<double[]> passed=new ArrayList<double[]>();
		for(int s=0;s<original.size();s++)
		{
			double[] temp=new double[2];
			for(int t=0;t<2;t++)
		{
			temp[t]=original.get(s)[t];
		}
			passed.add(temp);
		}
		List<double[]> modified=new ArrayList<double[]>();
		
		double basepeak=0.0;
		for(int s=0;s<passed.size();s++)
		{
			if(Math.abs(passed.get(s)[0]-mz)<mztol)
			{	
			}
			else
				modified.add(passed.get(s));	
		}
		for(int t=0;t<modified.size();t++)
		{
			if(modified.get(t)[1]>basepeak)
				basepeak=modified.get(t)[1];
		}
		for(int j=0;j<modified.size();j++)
		{
			modified.get(j)[1]=modified.get(j)[1]/basepeak*999;
			modified.get(j)[1]=Math.sqrt(modified.get(j)[1]);
		}
		return modified;
	}
	//remove top n most abundant peaks from MS/MS spectra
	public static List<double[]> removetopNfragment(List<double[]> original,int n, boolean sqrt)
	{
		List<double[]> passed=new ArrayList<double[]>();
		for(int s=0;s<original.size();s++)
		{
			double[] temp=new double[2];
			for(int t=0;t<2;t++)
		{
			temp[t]=original.get(s)[t];
		}
			passed.add(temp);
		}
		List<double[]> modified=new ArrayList<double[]>();
		//first find index of top n peaks
		double max=0.0;
		int maxindex=0;
		double basepeak=0.0;
		int[] topNindex = new int[n];
		double[] topNintensity = new double[n];
		int s,i,j;
		for(s=0;s<passed.size();s++)
		{
			if(passed.get(s)[1]>max)
			{
				max=passed.get(s)[1];
				maxindex=s;
			}
		}
		topNindex[0]=maxindex;
		topNintensity[0]=max;
		for(i=1;i<n;i++)
		{
		 max=0;
		 maxindex=0;
		 for(j=0;j<passed.size();j++)
		 {
		   if(passed.get(j)[1]>max && passed.get(j)[1]<topNintensity[i-1])
		   {
		   max=passed.get(j)[1];
		   maxindex=j;
		   }
		 }
		   topNintensity[i]=max;
		   topNindex[i]=maxindex;
		}  
		  
		//remove top n peaks and export
		for(int t=0;t<passed.size();t++)
		{
			boolean ok=true;
			for (int m=0;m<n;m++)
			{
			if(t==topNindex[m])
			{
				ok=false;
			}
			}
			
			if(ok==true)
			 modified.add(passed.get(t));	
		}
		for(int t=0;t<modified.size();t++)
		{
			if(modified.get(t)[1]>basepeak)
				basepeak=modified.get(t)[1];
		}
		for(int k=0;k<modified.size();k++)
		{
			modified.get(k)[1]=modified.get(k)[1]/basepeak*999;
			if(sqrt==true)
			modified.get(k)[1]=Math.sqrt(modified.get(k)[1]);
		}
		
		return modified;
	}
	
	
	public static double getMedian(List<Double> sets)
	{
	    Collections.sort(sets);

	    double middle = sets.size()/2;
	        if (sets.size()%2 == 0) {
	           middle = (sets.get(sets.size()/2) + sets.get(sets.size()/2 - 1))/2;
	        } else {
	            middle = sets.get(sets.size()/2);
	        }
	      return middle;
	}
	
	public static String Frequent(String array[]) 
	{ 
		// Insert all unique strings and update count if a string is not unique.
		Map<String,Integer> hshmap = new HashMap<String, Integer>(); 
		for (String str : array) 
		{ 
			if(!(str==null) && !(str==""))
			{
			if (hshmap.keySet().contains(str)) // if already exists then update count. 
				hshmap.put(str, hshmap.get(str) + 1); 
			else
				hshmap.put(str, 1); // else insert it in the map.
			}
		} 
    	// Traverse the map for the maximum value.
		String maxStr = ""; 
    	int maxVal = 0; 
		for (Map.Entry<String,Integer> entry : hshmap.entrySet()) 
		{ 
			String key = entry.getKey(); 
			Integer count = entry.getValue(); 
			if (count > maxVal) 
			{ 
				maxVal = count; 
				maxStr = key; 
			} 
      		// Condition for the tie.
			else if (count == maxVal){ 
				if (key.length() < maxStr.length())
					maxStr = key; 
			}
		} 
		return maxStr;
	} 
	
	
		
		public static void calculate(BAFinder_GUI.GUIInput GI)  throws IOException, FileNotFoundException,IllegalArgumentException{
	

       
		String pfolder = GI.Pdir;
		String nfolder = GI.Ndir;
		
		String MSP =GI.MS2lib;
		String rt=GI.rtlib;
		double mztol=GI.mzwindow;
		double rttol=GI.Rtwindow;
		String export=GI.output;
		String addconju=GI.conju;
		
		
		
		
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
		  
		 
		  
		//String addconju="";
		//String addconju="G:/BAFinder/3. Library/Customized_conjugate_update_102622.txt";
		//System.out.println(addconju);
		
		
		File pdir = new File(pfolder);
		File ndir = new File(nfolder);
		File pXCMSalign=new File(pfolder+"/alignment.csv");
		File pXCMSpeak=new File(pfolder+"/peak.csv");
		File nXCMSalign=new File(nfolder+"/alignment.csv");
		File nXCMSpeak=new File(nfolder+"/peak.csv");
		Collection<File> plistcsv = FileUtils.listFiles(pdir, new String[]{"csv"}, true);
		Collection<File> nlistcsv = FileUtils.listFiles(ndir, new String[]{"csv"}, true);
		if (!(plistcsv.size()==2 && nlistcsv.size()==2))
		{
			 JOptionPane.showMessageDialog(null,
	   	   			    "Error: Input data folder doesn't have the required two csv files",
	   	   			    "Error Message",
	   	   			    JOptionPane.ERROR_MESSAGE);
	     		 throw new IllegalArgumentException();
		}
		for (File file : plistcsv) 
        {
			BufferedReader pcsvReader = new BufferedReader(new FileReader(file));
			String prow = pcsvReader.readLine();
			String pheader[]=prow.split(",");
			if(pheader[1].contains("mz"))
			{
				pXCMSpeak=file;
			}
			else if (pheader[1].contains("Row.names"))
			{
				pXCMSalign=file;
			}
			else
			{
				 JOptionPane.showMessageDialog(null,
		   	   			    "Error: Incorrect header in pos csv files",
		   	   			    "Error Message",
		   	   			    JOptionPane.ERROR_MESSAGE);
					pcsvReader.close();
		     		 throw new IllegalArgumentException();
				
			}
				
			pcsvReader.close();
        }
		
		for (File file : nlistcsv) 
        {
			BufferedReader ncsvReader = new BufferedReader(new FileReader(file));
			String nrow = ncsvReader.readLine();
			String nheader[]=nrow.split(",");
			if(nheader[1].contains("mz"))
			{
				nXCMSpeak=file;
			}
			else if (nheader[1].contains("Row.names"))
			{
				nXCMSalign=file;
			}
			else
			{
				 JOptionPane.showMessageDialog(null,
		   	   			    "Error: Incorrect header in neg csv files",
		   	   			    "Error Message",
		   	   			    JOptionPane.ERROR_MESSAGE);
					ncsvReader.close();
		     		 throw new IllegalArgumentException();
				
			}
				
			ncsvReader.close();
        }
		
		
		FileWriter g;
		g = new FileWriter(new File(export+"/Processing_details.csv"));
		
		//FileWriter f = new FileWriter(new File(export+"/Final_summary.csv"));
		BufferedWriter f=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				export+"/Final_summary.csv"), "UTF-8"));
		FileWriter em = new FileWriter(new File(export+"/representative MS2.mgf"));
		//FileWriter emo = new FileWriter(new File(export+"/representative MS2 other.mgf"));
		//FileWriter fo = new FileWriter(new File(export+"/Other precursor match MS2 lib.csv"));
		
		//Read Customized_conjugate.txt to get id and mass of user-defined conjugate
		
		 int cus=0;
		 ArrayList<userconju> cuslib = new ArrayList<userconju>();
		if(!(addconju==null) && !(addconju==""))
		{
	
		String[] es=new String [2];
		Scanner scannerex1 =new Scanner(new FileReader(addconju));
		int exm=0;
		while(scannerex1.hasNextLine())
		{
		   String temp1=scannerex1.nextLine();
		   if(!temp1.isEmpty())
		   {
			   es=temp1.split("\t");
			   cuslib.add(new userconju(es[0],Double.parseDouble(es[1])));	  
		       exm++;
		   }
		}
		scannerex1.close();
		
		}
		cus=cuslib.size();
		 
		//System.out.println(cus);
		
	     //Read mzlib.txt from scr/resources path to get mzlib[] pmz,nmz and String[][] equvalent
		////mzlib_IS_C24_only_no_M_H-SO3_only_GTBA_2H_GBA-S-Gly
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("mzlib.txt"); 
		BufferedReader libReader1 = new BufferedReader(new InputStreamReader(in));
		
		
		//String BAmz="G:\\software\\EclispeEE-workspace\\BAFinder_GUI\\src\\resources\\mzlib_IS_C24_only_no_M_H-SO3_only_GTBA_2H_GBA-S-Gly.txt";
		//BufferedReader libReader1 = new BufferedReader(new FileReader(BAmz));
		//number of lines, start from 0
		int pcountlib=0;
		int ncountlib=0;
		int allcountlib=0;
		String tlib1;
		while ((tlib1=libReader1.readLine()) != null) {
			allcountlib++;
		    String[] t1 = tlib1.split("\t");
		    if(t1[0].equals("Neg"))
		    	ncountlib++;
		    else if (t1[0].equals("Pos"))
		    	pcountlib++;
		}
		libReader1.close();
		in.close();
		

		
		
		
		//if customized conjugate exist, add cus*10(1OH,1O,2OH,1O1OH.2O,3OH,1O2OH,2O1OH,3O,4OH) mz for neg and cus*20 for pos([M+H]+,[M+H-H2O]+)

		mzlib[] pmz=new mzlib[pcountlib+cus*20];
		mzlib[] nmz=new mzlib[ncountlib+cus*10];
		//System.out.println(ncountlib+cus*10);
		//String [][] eq=new String[allcountlib][2];
		String [][] eq=new String[allcountlib+cus*30][2];
		String tlib2;
		//BufferedReader libReader2 = new BufferedReader(new InputStreamReader(in));
		//BufferedReader libReader2 = new BufferedReader(new FileReader(BAmz));
		int tp=0;
		int tn=0;
		int ta=0;
		InputStream in2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("mzlib.txt"); 
		BufferedReader libReader2 = new BufferedReader(new InputStreamReader(in2));
		while ((tlib2 = libReader2.readLine()) != null) {
		    String[] t2 = tlib2.split("\t");
		    if(t2[0].equals("Pos"))
		    {
		    pmz[tp]=new mzlib(Double.parseDouble(t2[2]),t2[1]);
		    tp++;
		    }
		    else if (t2[0].equals("Neg"))
		    {
		    nmz[tn]=new mzlib(Double.parseDouble(t2[2]),t2[1]);
		    tn++;
		    }
		    eq[ta][0]=t2[1];
		    eq[ta][1]=t2[3];
		    ta++;
		}
		
		libReader2.close();
		in2.close();
		
		//if customized conjugate exist, add cus+BA [M-H]-,[M+H]+,[M+H-H2O]+ to pmz, nmz and eq
		//String[] BAframe= {"1OH","1O","2OH","1O1OH","2O","3OH","1O2OH","2O1OH","3O","4OH"};
		String[] BAframe= {"1O","1O1OH","2O","1O2OH","2O1OH","3O","1OH","2OH","3OH","4OH"};
		//equavlent BAframe for first above BA
		String[] poseq={"2OH","3OH","1O2OH","4OH","1O3OH","2O2OH","1OH","2OH","3OH","4OH"};
		String[] poseqadd1={"[M+H-H2O]+","[M+H-H2O]+","[M+H-H2O]+","[M+H-H2O]+","[M+H-H2O]+","[M+H-H2O]+","[M+H]+","[M+H]+","[M+H]+","[M+H]+"};
		String[] poseqadd2= {"[M+H-2H2O]+",
				"[M+H-2H2O]+",
				"[M+H-2H2O]+",
				"[M+H-2H2O]+",
				"[M+H-2H2O]+",
				"[M+H-2H2O]+",
				"[M+H-H2O]+",
				"[M+H-H2O]+",
				"[M+H-H2O]+",
				"[M+H-H2O]+"};



		//Double[] BAmass= {376.2977,374.2821,392.2926,390.277,388.2614,408.2876,406.2719,404.2563,402.2407,424.2825};
		Double[] BAmass= {374.2821,390.277,388.2614,406.2719,404.2563,402.2407,376.2977,392.2926,408.2876,424.2825};
		if(cus>0)
		{
			int padd=0;
			int nadd=0;
			for(int i=0;i<BAframe.length;i++)
				for(int j=0;j<cus;j++)
			{
				//the unknown in compound name was related to feature detection and modified MS2 search, be careful when editing it 
				int addcount=nadd+padd;
				nmz[ncountlib+nadd]=new mzlib(BAmass[i]+cuslib.get(j).mass-18.0105642-1.0073,"unknown:"+BAframe[i]+"_BA-"+cuslib.get(j).id+" [M-H]-");
				
				pmz[pcountlib+padd]=new mzlib(BAmass[i]+cuslib.get(j).mass-18.0105642+1.0073,"unknown:"+BAframe[i]+"_BA-"+cuslib.get(j).id+" [M+H]+");
				eq[allcountlib+addcount][0]=nmz[ncountlib+nadd].id;
				eq[allcountlib+addcount][1]=nmz[ncountlib+nadd].id;
				eq[allcountlib+addcount+1][0]=pmz[pcountlib+padd].id;
				eq[allcountlib+addcount+1][1]="unknown:"+poseq[i]+"_BA-"+cuslib.get(j).id+" "+poseqadd1[i];
				nadd++;
				padd++;
				pmz[pcountlib+padd]=new mzlib(BAmass[i]+cuslib.get(j).mass-18.0105642*2+1.0073,"unknown:"+BAframe[i]+"_BA-"+cuslib.get(j).id+" [M+H-H2O]+");
				eq[allcountlib+addcount+2][0]=pmz[pcountlib+padd].id;
				eq[allcountlib+addcount+2][1]="unknown:"+poseq[i]+"_BA-"+cuslib.get(j).id+" "+poseqadd2[i];
				
				padd++;
			}
			
			//System.out.println(padd);
		}
		
		
		//read AABA-pos from resources path
		InputStream inAA = Thread.currentThread().getContextClassLoader().getResourceAsStream("AABA_pos.txt"); 
		BufferedReader libReaderAA = new BufferedReader(new InputStreamReader(inAA));
		//String inAA="G:\\software\\EclispeEE-workspace\\debug_AABA_103122_aaba_pos\\resource\\AABA_pos.txt";
		//BufferedReader libReaderAA = new BufferedReader(new FileReader(inAA));
		
		
		ArrayList<AABAlib> AA = new ArrayList<AABAlib>();
		String AAlib;
		
		while ((AAlib=libReaderAA.readLine()) != null) {
			
		    String[] t2 = AAlib.split("\t");
		    ArrayList<Double> fraglist=new ArrayList<Double>();
		    System.out.println(t2[0]+" "+t2[1]);
		    
		    for(int i=2;i<t2.length;i++)
		    {
		    	fraglist.add(Double.parseDouble(t2[i]));
		    	System.out.println(t2[i]);
		    }
		    AA.add(new AABAlib(t2[0],Double.parseDouble(t2[1]),fraglist));
		}
		libReaderAA.close();
		inAA.close();
		
		System.out.println("before read XCMS output");
		 System.out.println(dtf.format(LocalTime.now()));
		
		
		//read XCMS output in pos mode, keep those with m/z within tolerance
	
		
		BufferedReader csvReaderp = new BufferedReader(new FileReader(pXCMSalign));
		String prow = csvReaderp.readLine();
		String pheader[]=prow.split(",");
		//compare pos to neg to see if they are consistent
		BufferedReader csvReadern = new BufferedReader(new FileReader(nXCMSalign));
		String nrow = csvReadern.readLine();
         String nheader[]=nrow.split(",");  
     	if(pheader.length!=nheader.length) {
     		  JOptionPane.showMessageDialog(null,
   	   			    "Error: Input XCMS alignment files of pos and neg mode were not consistent in sample numbers.",
   	   			    "Error Message",
   	   			    JOptionPane.ERROR_MESSAGE);
     		  System.out.println("Error: Input XCMS alignment files of pos and neg mode were not consistent in sample numbers");
     		 csvReaderp.close();
     		 csvReadern.close();
     		 f.close();
     		 throw new IllegalArgumentException();
    		  
     		
     	}
     	
		int sample=pheader.length-12;
		String[] psamplename=new String[sample];
		for (int c=0;c<sample;c++)
		{
			psamplename[c]=pheader[c+12];
		}
		
		
		List<XCMS> pdata=new ArrayList<>();
		int pmzmatch=0;
		List<MS1> pMS1match=new ArrayList<>();
		
		//count the lines in pos csv file
		Path palign = Paths.get(pXCMSalign.getPath());
		double plines=0;
		plines = (double)Files.lines(palign).count();
		
		int paprogress=0;
		
		while ((prow = csvReaderp.readLine()) != null) {
			
		    String[] temp = prow.split(",");
		    XCMS pnewpeak=new XCMS();
		    double pdatamz=Double.parseDouble(temp[2]);
			pmzmatch=0;
			
		    for (int k=0;k<pmz.length;k++)
		    {
		    	List<String> pmzhit=new ArrayList<>();
		    	List<String> pmzadduct=new ArrayList<>();
		    	double pmzerr=0;
		    	
		    	if(Math.abs(pdatamz-pmz[k].mz)<=mztol)
		    	{
		    		pmzhit.add(pmz[k].id.split(" ")[0]);
		    		pmzadduct.add(pmz[k].id.split(" ")[1]);
		    		pmzmatch++;
		    		pmzerr=Math.abs(pdatamz-pmz[k].mz);
		    	
		    	if(pmzmatch>0)
		    	{	
		    		if(pmzmatch==1)
		    		{
		    		 double[] inten=new double[sample];
		    		 for(int t=0;t<sample;t++)
		 		    {
		    			 if(temp[t+12].equals("NA"))
		    			 {
		    			    inten[t]=0.0;
		    			 }
		    			 else
		    			  inten[t]=Double.parseDouble(temp[t+12]);
		 		    }
		    		 int featurecount=Integer.parseInt(temp[0].replace("\"", ""));
		    		 double rtinmin=Double.parseDouble(temp[5])/60;
		    		 List<Integer> peakidx=new ArrayList<>();
		    		 String groupid[]=temp[10].replace("\"", "").split(";");
		    		 for(int d=0;d<groupid.length;d++)
		    		 {
		    			//sometimes 1e+05 exist in the peakid, which are recognized as double, need to convert to int using int()
		    			 peakidx.add((int)(Double.parseDouble(groupid[d])));
		    			 //peakidx.add(Integer.parseInt(groupid[d].replace("\"", "")));
		    		 }
		    		 
		    		 pnewpeak=new XCMS("pos",featurecount,pdatamz,rtinmin,peakidx,inten);
		    		 pdata.add(pnewpeak);
		    		 
		    		}
		    		
		    		pMS1match.add(new MS1(pnewpeak,pmzhit,pmzadduct,pmzerr));
		    		}
		    	}
		    	
		    }
		    paprogress++;
		    progress=(int)Math.ceil(paprogress/plines*99);	
		 
		    message="step 1/6: "+String.format("matching mz(pos) %d%%.\n", progress);
		}
		csvReaderp.close();
		
		progress=0;
		 message="step 2/6: "+String.format("matching mz(neg) %d%%.\n", progress);
		
		//Read peak-picking data to get the left and right rt boundary of each peak
		BufferedReader csvReaderp1 = new BufferedReader(new FileReader(pXCMSpeak));
		String prow2 = csvReaderp1.readLine();
		List<XCMSpeak> pdetail=new ArrayList<>();
		List<String> bufferp=new ArrayList<>();
		while ((prow2 = csvReaderp1.readLine()) != null) {
			bufferp.add(prow2);
		}
		csvReaderp1.close();
		
		for(int i=0;i<pdata.size();i++)
		{
			XCMSpeak[] pinfogroup=new XCMSpeak[sample];
			
	    	for (int t=0;t<pdata.get(i).peakid.size();t++)
	    	{
	    		int query=pdata.get(i).peakid.get(t);
	    		String[] temp=bufferp.get(query-1).split(",");
	    		double rtmed=Double.parseDouble(temp[4])/60;
	 		    double rtmin=Double.parseDouble(temp[5])/60;
	 		    double rtmax=Double.parseDouble(temp[6])/60;
	 		    int sampleorder=Integer.parseInt(temp[11]);
	 		   // System.out.println(query);
	 		    XCMSpeak pinfo=new XCMSpeak(query,rtmed,rtmin,rtmax,sampleorder);
	 		    if(pinfogroup[sampleorder-1]==null)
	 		    pinfogroup[sampleorder-1]=pinfo;
	 		    else
	 		    	pinfogroup[sampleorder-1]=null;
	 		  
	    	}
	    	pdata.get(i).addinfo(pinfogroup);
	    	//if(pdata.get(i).samplertinfo[4].size()>0)
	    	//System.out.println(pdata.get(i).pcnum+" "+pdata.get(i).samplertinfo[4].get(0).peakid+" "+pdata.get(i).samplertinfo[4].get(0).rt);
		}
		
		System.out.println("after read XCMS pos");
		 System.out.println(dtf.format(LocalTime.now()));
		//read XCMS output in neg mode, keep those with m/z within tolerance
		
		
		
		String[] nsamplename=new String[sample];
		for (int c=0;c<sample;c++)
		{
			nsamplename[c]=nheader[c+12];
		}
		
		List<XCMS> ndata=new ArrayList<>();
		int nmzmatch=0;
		List<MS1> nMS1match=new ArrayList<>();
		//count the lines in neg csv file
				Path nalign = Paths.get(nXCMSalign.getPath());
				double nlines=0;
				nlines = (double)Files.lines(nalign).count();
				
				int naprogress=0;
				
		while ((nrow = csvReadern.readLine()) != null) {
		    String[] temp = nrow.split(",");
		    XCMS nnewpeak=new XCMS();
		    double ndatamz=Double.parseDouble(temp[2]);
			nmzmatch=0;
			
		    for (int k=0;k<nmz.length;k++)
		    {
		    	List<String> nmzhit=new ArrayList<>();
		    	List<String> nmzadduct=new ArrayList<>();
		    	double nmzerr=0;
		    	
		    	if(Math.abs(ndatamz-nmz[k].mz)<=mztol)
		    	{
		    		nmzhit.add(nmz[k].id.split(" ")[0]);
		    		nmzadduct.add(nmz[k].id.split(" ")[1]);
		    		nmzmatch++;
		    		nmzerr=Math.abs(ndatamz-nmz[k].mz);
		    	
		    	if(nmzmatch>0)
		    	{	
		    		if(nmzmatch==1)
		    		{
		    		 double[] inten=new double[sample];
		    		 for(int t=0;t<sample;t++)
		 		    {
		    			 if(temp[t+12].equals("NA"))
		    			 {
		    			    inten[t]=0.0;
		    			 }
		    			 else
		    			  inten[t]=Double.parseDouble(temp[t+12]);
		 		    }
		    		 int featurecount=Integer.parseInt(temp[0].replace("\"", ""));
		    		 double rtinmin=Double.parseDouble(temp[5])/60;
		    		 List<Integer> peakidx=new ArrayList<>();
		    		 String groupid[]=temp[10].replace("\"", "").split(";");
		    		 for(int d=0;d<groupid.length;d++)
		    		 {
		    			 
		    			//sometimes 1e+05 exist in the peakid, which are recognized as double, need to convert to int using int()
		    			peakidx.add((int)(Double.parseDouble(groupid[d])));
		    			 
		    		 }
		    		
		    		nnewpeak=new XCMS("neg",featurecount,ndatamz,rtinmin,peakidx,inten);
		    		ndata.add(nnewpeak);
		    		 
		    		}
		    		
		    		nMS1match.add(new MS1(nnewpeak,nmzhit,nmzadduct,nmzerr));
		    		}
		    	}
		    	
		    }
		    naprogress++;
		    progress=(int)Math.ceil(naprogress/nlines*99);	
		   
		    message="step 2/6: "+String.format("matching mz(neg) %d%%.\n", progress);
		    	
		}
		csvReadern.close();
	    
		//Read peak-picking data to get the left and right rt boundary of each peak
				BufferedReader csvReadern1 = new BufferedReader(new FileReader(nXCMSpeak));
				String nrow2 = csvReadern1.readLine();
				
				List<String> buffern=new ArrayList<>();
				while ((nrow2 = csvReadern1.readLine()) != null) {
					buffern.add(nrow2);
				}
				csvReadern1.close();
				
				for(int i=0;i<ndata.size();i++)
				{
					XCMSpeak[] ninfogroup=new XCMSpeak[sample];
			    	for (int t=0;t<ndata.get(i).peakid.size();t++)
			    	{
			    		int query=ndata.get(i).peakid.get(t);
			    		String[] temp=buffern.get(query-1).split(",");
			    		double rtmed=Double.parseDouble(temp[4])/60;
			 		    double rtmin=Double.parseDouble(temp[5])/60;
			 		    double rtmax=Double.parseDouble(temp[6])/60;
			 		    int sampleorder=Integer.parseInt(temp[11]);
			 		   // System.out.println(query);
			 		    XCMSpeak ninfo=new XCMSpeak(query,rtmed,rtmin,rtmax,sampleorder);
			 		    if(ninfogroup[sampleorder-1]==null)
			 		    ninfogroup[sampleorder-1]=ninfo;
			 		    else
			 		    	ninfogroup[sampleorder-1]=null;
			 		  
			    	}
			    	ndata.get(i).addinfo(ninfogroup);
			    	//if(ndata.get(i).samplertinfo[4].size()>0)
			    	//System.out.println(ndata.get(i).pcnum+" "+ndata.get(i).samplertinfo[4].get(0).peakid+" "+ndata.get(i).samplertinfo[4].get(0).rt);
				}
				
				System.out.println("after read XCMS neg");
				 System.out.println(dtf.format(LocalTime.now()));
	    
				//combine pos and neg results
			    List<MS1> allMS1match=new ArrayList<>();
			    allMS1match.addAll(nMS1match);
			    allMS1match.addAll(pMS1match);
			    
			    //List<XCMS> alldata=new ArrayList<>();
			   /// alldata.addAll(pdata);
			    //alldata.addAll(ndata);
			    
			    //group pos/neg adducts
			    
			    ArrayList<ArrayList<group>> gr = new ArrayList<ArrayList<group>>(); 
			    List<ingroup> exist=new ArrayList<>();
			
			    int gn=0;
			    int same=0;
			    int pass=0;
			    for (int o=0;o<nMS1match.size();o++)
				  {
			        
			    	 for(int e=0;e<allMS1match.get(o).compound.size();e++)
			    	 {
			    		 pass=0;
			    		 if(exist.size()>0)
			    		 {
			    		 for(int a=0;a<exist.size();a++)
						    	if(o==exist.get(a).peaknum && allMS1match.get(o).compound.get(e).equals(exist.get(a).id) )
						    	{
						    	 pass++;
						    	 break;
						    	}
			    		 }
			    	
			    		 if(pass==0)
			    		 {
						    		same=0;
						  //compare pos first, if pos match, same>0, then add
						    	
						 for (int q=allMS1match.size()-1;q>=o+1;q--)
						 
					  {
						  if(Math.abs(allMS1match.get(o).peak.rt-allMS1match.get(q).peak.rt)<=rttol)
							
							{
								
								for(int b=0;b<allMS1match.get(q).compound.size();b++)
									if(allMS1match.get(o).compound.get(e).equals(allMS1match.get(q).compound.get(b)))
										//avoid duplicate neg adduct
										if(!allMS1match.get(o).adduct.get(e).equals(allMS1match.get(q).adduct.get(b)))
						  {
							
							  same++;
							  
							  if(same>0)
							  {
							  if(gr.size()<=gn)
							  {
							  gr.add(new ArrayList<group>());
							  }
							  gr.get(gn).add(new group(allMS1match.get(q).peak,allMS1match.get(q).compound.get(b),allMS1match.get(q).adduct.get(b),allMS1match.get(q).mzerror));
							  
							  //only neg mode
							  if(q<nMS1match.size())
							  {
							  exist.add(new ingroup(q,allMS1match.get(q).compound.get(b)));
							  }
							  }
						  }
								
							}
					  }
						        //remove same>0 to see the result of only neg
								if(same>0)
								{
									gr.get(gn).add(new group(allMS1match.get(o).peak,allMS1match.get(o).compound.get(e),allMS1match.get(o).adduct.get(e),allMS1match.get(o).mzerror));
									gn++;
								}
								else
								{
									if(gr.size()<=gn)
									  {
									  gr.add(new ArrayList<group>());
									  }
									gr.get(gn).add(new group(allMS1match.get(o).peak,allMS1match.get(o).compound.get(e),allMS1match.get(o).adduct.get(e),allMS1match.get(o).mzerror));
									gn++;
								}
								
							
					  
			    		 } 
						  
					  }
			    	 
			    	 progress=(int)Math.ceil(o/(double)nMS1match.size()*99);
			    	 message="step 3/6: "+String.format("grouping pos&neg %d%%.\n", progress);
					  
				  }
			    
			      //remove duplicate peaks：same compound, same adduct, but different peak number
			      //pick the one with smaller m/z and rt error
			    
			      for(int r=0;r<gr.size();r++)
			    	  for (int s=0;s<gr.get(r).size();s++)
			    		  for (int t=s+1;t<gr.get(r).size();t++)
			    	  {
			    		 if(gr.get(r).get(s).adduct.equals(gr.get(r).get(t).adduct))
			    		 {
			    			 
			    			if(gr.get(r).get(s).mzerror<(mztol/2) && gr.get(r).get(t).mzerror<(mztol/2))
			    			{
			    				List<Double> temprt=new ArrayList<>();
			    				for (int a=0;a<gr.get(r).size();a++)
			    				{
			    					if(a!=s && a!=t)
			    					{
			    						temprt.add(gr.get(r).get(a).peak.rt);
			    					}
			    				}
			    				//get the median from arraylist
			    				double rtmedian=getMedian(temprt);
			    				if(Math.abs(gr.get(r).get(s).peak.rt-rtmedian)<Math.abs(gr.get(r).get(t).peak.rt-rtmedian))
			    				{
			    					 gr.get(r).remove(t);
			    				}
			    				else
			    				 {
				    				 gr.get(r).remove(s);
				    				 
				    			 }
			    				
			    			}
			    			else
			    			{
			    			 if(gr.get(r).get(s).mzerror<gr.get(r).get(t).mzerror)
			    			 {
			    				 gr.get(r).remove(t);
			    				 
			    			 }
			    			 else
			    			 {
			    				 gr.get(r).remove(s);
			    				 
			    			 }
			    			}
			    			 t--;
			    		 }
			    	  }
			    	  
			  	System.out.println("after group pos and neg");
				 System.out.println(dtf.format(LocalTime.now()));
				 
			      //Apply MS1 filter
			       double[][] nmax=new double[gr.size()][sample];
			       double[][] pmax=new double[gr.size()][sample];
			       double[][] pmax_nmax_ratio=new double[gr.size()][sample];
			       String[][] nmaxion=new String[gr.size()][sample];
			       String[][] pmaxion=new String[gr.size()][sample];
			       int[] pcountion=new int[gr.size()];
			       int[] ncountion=new int[gr.size()];
			       int[] ncountM=new int[gr.size()];
			       int[] pcountM=new int[gr.size()];

			       String[][] pmax_nmax_ratio_bad_ok=new String[gr.size()][sample]; 
			
			       int[][] pcountionsample=new int[gr.size()][sample];
			       int[][] ncountionsample=new int[gr.size()][sample];
			       int[] pmaxion_ok_count=new int[gr.size()];
			       int[] nmaxion_ok_count=new int[gr.size()];
			       int[] pmax_nmax_ratio_bad_count=new int[gr.size()];
			       int[] pmaxion_bad_count=new int[gr.size()];
			       int[] nmaxion_bad_count=new int[gr.size()];
			       int[] half_adduct_count=new int[gr.size()];
			       String[] passMS1=new String[gr.size()];
			       List<Integer> MS1filtered_gr_order=new ArrayList<Integer>();
			       int[] gr_MS1filtered_order=new int[gr.size()];
			       List<String> groupnmaxion=new ArrayList<String>();
			       List<Double> groupnmaxionrt=new ArrayList<Double>();
			       
			       //calculate average rt for each group and count adduct number
			       double[] avgRT=new double[gr.size()];
			       
			       for(int r=0;r<gr.size();r++)
			       {   
			    	   double total=0;
			    	   int padduct=0;
			    	   int nadduct=0;
			    	   for (int s=0;s<gr.get(r).size();s++)
			    	   {
			    		   total=total+gr.get(r).get(s).peak.rt;
			    		   if(gr.get(r).get(s).peak.mode.equals("neg") )
			    	       {
			    			     nadduct++;    
			    			     //group must contain at least one [M-H]- or [M-2H]2-
			    			     if(gr.get(r).get(s).adduct.equals("[M-H]-") || gr.get(r).get(s).adduct.equals("[M-2H]2-"))
			    			     {
			    			    	 ncountM[r]++;
			    			     }
			    		   }
			    		   if(gr.get(r).get(s).peak.mode.equals("pos") )
						   {
					             padduct++;
					           //determine whether pos features are all low-intensity adducts like -Gly or -Tau
					             if(!gr.get(r).get(s).adduct.contains("-Gly") && !gr.get(r).get(s).adduct.contains("-Tau"))
			    			     {
					            	 pcountM[r]++;
			    			     }
					            
					             
						   }
			    		   
			    	   }
			    	   avgRT[r]=total/gr.get(r).size();
			    	   ncountion[r]=nadduct;
			    	   pcountion[r]=padduct;
			       }
			       
			       //if pos features are all low-intensity adducts like -Gly or -Tau, remove those pos features
			       //only keep neg features
			       for(int r=0;r<gr.size();r++)
			       {   
			    	   ArrayList<group> temp = new ArrayList<group>();
			    	   if(ncountion[r]>0 && pcountion[r]>0 && pcountM[r]==0)
			    	   {
			    		   for (int s=0;s<gr.get(r).size();s++)
				    	   {
			    			   if(gr.get(r).get(s).peak.mode.equals("neg") )
				    	       {
			    				   temp.add(gr.get(r).get(s));
				    	       }
				    	   }
			    		   //replace gr(r) with temp
			    		   gr.set(r,temp);
			    	   }
			       }
			       
			       ArrayList<ArrayList<group>> MS1filtered = new ArrayList<ArrayList<group>>(); 
			       
			       
			      // get the max intensity and adduct type for each peak group
			      for(int h=0;h<gr.size();h++)
			      {
			    	  
			    	  pmaxion_ok_count[h]=0;
			    	  nmaxion_ok_count[h]=0;
			    	  pmaxion_bad_count[h]=0;
			    	  nmaxion_bad_count[h]=0;
			    	  
			    	  for (int n=0;n<sample;n++)
			    	  {
			    		  
			        		int pduplicate=0;
			        		int nduplicate=0;
			        		double ntmax=0;
			        		double ptmax=0;
			        		String ntadduct="";
			        		String ptadduct="";
			        		int nexist=0;
			        		int pexist=0;
			        		
		        	    for(int m=0;m<gr.get(h).size();m++)
		        		
		        	{
		        		if(gr.get(h).get(m).peak.mode.equals("neg") && nduplicate==0)
		        		{
		        		   ntmax=gr.get(h).get(m).peak.intensity[n];
		        		   if(ntmax>0)
		        		   {
		        			   ntadduct=gr.get(h).get(m).adduct;
		        		   }
		        		   else
		        			   ntadduct="";
		        		   nduplicate++;
		        		   if(gr.get(h).get(m).peak.intensity[n]>0)
		        			   nexist++;
		        		}
		        		else if (gr.get(h).get(m).peak.mode.equals("pos") && pduplicate==0)
		        		{
		        		  ptmax=gr.get(h).get(m).peak.intensity[n];
		        		  if(ptmax>0)
		        		  {
		          		   ptadduct=gr.get(h).get(m).adduct;
		        		  }
		        		  else
		        			 ptadduct="";
		          		     pduplicate++;
		          		 if(gr.get(h).get(m).peak.intensity[n]>0)
		      			   pexist++;
		        		}
		        		else if (gr.get(h).get(m).peak.mode.equals("neg") && nduplicate>0)
		        		{
		        			if(gr.get(h).get(m).peak.intensity[n]>ntmax)
		        			{
		        		     ntmax=gr.get(h).get(m).peak.intensity[n];
		          		     ntadduct=gr.get(h).get(m).adduct;
		        			}
		        			 if(gr.get(h).get(m).peak.intensity[n]>0)
		          			   nexist++;
		        		}
		        		else if (gr.get(h).get(m).peak.mode.equals("pos") && pduplicate>0)
		        		{
		        			if(gr.get(h).get(m).peak.intensity[n]>ptmax)
		        			{
		        		     ptmax=gr.get(h).get(m).peak.intensity[n];
		          		     ptadduct=gr.get(h).get(m).adduct;
		          		    
		        			}
		        			 if(gr.get(h).get(m).peak.intensity[n]>0)
		            			   pexist++;
		        		}   
		                
		        	}
		        	    nmax[h][n]=ntmax;
		        	    pmax[h][n]=ptmax;
		        	    nmaxion[h][n]=ntadduct;
		        	    pmaxion[h][n]=ptadduct;
		        	    ncountionsample[h][n]=nexist;
		        	    pcountionsample[h][n]=pexist;
		        	    
		        	   
		        	    //only count samples with at least half numbers of adducts detected
		        	   if((double)(ncountionsample[h][n]+pcountionsample[h][n])/(ncountion[h]+pcountion[h])>=0.5)
		        	    {
		        	    	 half_adduct_count[h]++;
		        	    	
		        	    	 if(pmax[h][n]>0 && nmax[h][n]>0)
		             	    {
		             	    pmax_nmax_ratio[h][n]=pmax[h][n]/nmax[h][n];
		             	    //if the ratio of pos/neg top intensity >2, ratio bad
		             	    if(pmax_nmax_ratio[h][n]>2)
		             	    {
		             	    	pmax_nmax_ratio_bad_ok[h][n]="x";
		             	    	pmax_nmax_ratio_bad_count[h]++;
		             	    }
		             	    }
		        	    	 else if (pmax[h][n]>0 && nmax[h][n]==0)
		        	    	 {
		        	    		 pmax_nmax_ratio_bad_ok[h][n]="x";
		              	    	 pmax_nmax_ratio_bad_count[h]++;
		        	    	 }
		        	    	 else if (pmax[h][n]==0 && nmax[h][n]>0)
		        	    	 {
		        	    		 pmax_nmax_ratio[h][n]=pmax[h][n]/nmax[h][n];
		        	    	 }
		        	   
		        	    }
		        	   
			    	  } 
			    	  
		      	    
		      	       try
		      	       {
		      	    	 //number of samples with more than half adduct>0 && Among those sample,<0.5 have bad ratio
		      	    	//caution: at least one of number must be double when int/int, or the result will be int
			    	   if( half_adduct_count[h]>0 && (double)pmax_nmax_ratio_bad_count[h]/half_adduct_count[h]<0.5 && ncountM[h]>0)
			    	    {
			    	        
			    		    //group without pos feature is ok
			    	    	passMS1[h]="";
			    	    	

			    	  }
			    	   else if(half_adduct_count[h]>0 && (double)pmax_nmax_ratio_bad_count[h]/half_adduct_count[h]>=0.5 && ncountM[h]>0)
			    	   {
			    		   //remove all positive features, only keep the negative one
			    		   passMS1[h]="neg only";
			    		   for (int s=0;s<gr.get(h).size();s++)
			    		   {
			    			   
			    			   if(gr.get(h).get(s).peak.mode.equals("pos"))
			    			   {
			    				   gr.get(h).remove(s);
			    				   s--;
			    			   }
			    			   pcountion[h]=0;
			    			   pmax_nmax_ratio_bad_count[h]=0;
			    			   
			    			   for (int n=0;n<sample;n++)
						    	  {
			    			        pcountionsample[h][n]=0;
			    			        pmax_nmax_ratio[h][n]=0;
			    			        pmax_nmax_ratio_bad_ok[h][n]=null;
						    	  }
			    			   
			    		   }
			    		   
			    	   }
			    	   else
			    		   passMS1[h]="x";
		      	       }
		      	     catch (ArithmeticException e) {
		      	         System.out.println ("Can't be divided by Zero"+e);}
		      	       
			      }
			      
			        
			         System.out.println("after MS1 filter");
					 System.out.println(dtf.format(LocalTime.now()));
			         
			         //remove groups with only neg peak which were duplicated in other groups with multiple peaks
			         //fixed on 10/24/22: problem: two neg only were both marked "duplicate"
			         for(int h=0;h<gr.size();h++)
			        	 if(passMS1[h].equals("neg only"))
			        	 {
			        		 int quit=0;
			           for(int q=0;q<gr.size() && q!=h && !passMS1[q].equals("neg only");q++) 
			        	 for (int s=0;s<gr.get(h).size();s++)  	
				         {
			        		 
			        		 //for(int q=0;q<gr.size();q++)
			        			 //if(q!=h && !(passMS1[q].equals("neg only")))
			        			 {
			        				 for (int t=0;t<gr.get(q).size();t++)
			        					 if(gr.get(q).get(t).peak.mode.equals("neg"))
			        		 {
			        						 if(gr.get(q).get(t).peak.pcnum==gr.get(h).get(s).peak.pcnum)
			        						 {
			        							 //if removed, all array about group need to be adjusted
			        							 //gr.remove(h);
			        							 //h--;
			        							 passMS1[h]="duplicate";
			        							 quit++;
			        							 break;
			        						 }
			        						 
			        		 }
			        				 if(quit>0)
		    						 {
		    							break;
		    						 }
	        			 
			        			 }
			        		 if(quit>0)
    						 {
    							break;
    						 }
				         }
				         }
			         
			         
			         for(int h=0;h<gr.size();h++)
			        	 if(!passMS1[h].equals("x") && !passMS1[h].equals("duplicate"))
			        	 {
			        		 MS1filtered.add(gr.get(h));
				    	    	//get the most frequently observed adduct for neg mode
				    	    	String negtopadduct=Frequent(nmaxion[h]);
				    	    	groupnmaxion.add(negtopadduct);
				    	    	for(int m=0;m<gr.get(h).size();m++)
				    	    	{
				    	    		gr.get(h).get(m).peak.MS1filtered(true);
				    	    		if(gr.get(h).get(m).adduct.equals(negtopadduct))
				    	    		{
				    	    			groupnmaxionrt.add(gr.get(h).get(m).peak.rt);
				    	    		}
				    	    	}
				    	    	MS1filtered_gr_order.add(h);
				    	    	gr_MS1filtered_order[h]=MS1filtered.size()-1;
			        	 }
			        	 
		       
			      
			         
			         //Pick all MS/MS meet the mz RT critera of filtered peak groups
				        //pos mode
				        int porder=0;
				        
				        Collection<File> pMGFFiles = FileUtils.listFiles(pdir, new String[]{"mgf"}, true);
				        int psum=pMGFFiles.size();
				        //for (File file : pdirMGF.listFiles()) 
				        /*
				        for (File file : pMGFFiles) 
				        {
				        	psum++;
				        }
				        */
	                    if(psum!=sample)
	                    {
	                    	JOptionPane.showMessageDialog(null,
	        	  	   			    "The number of MGF files in positive mode doesn't match with the sample numbers in XCMS output, please check.",
	        	  	   			    "Error Message",
	        	  	   			    JOptionPane.ERROR_MESSAGE);
	                    	f.close();
	                    	throw new IllegalArgumentException();
	                    }
	                    //count number of pos MS2 for progress monitor
	                    int MS2posall=0;
	                    int MS2posnow=0;
	                    for (File file : pMGFFiles) 
				        {
				 
				            BufferedReader mgfReaderp0 = new BufferedReader(new FileReader(file));
				            String temp;
				           
				             while ((temp = mgfReaderp0.readLine()) != null) 
							{
				            	 if(temp.equals("END IONS") )
					            	{
				            		 MS2posall++;
					            	}
							}
				             mgfReaderp0.close();
				        }
	                    
				       // for (File file : pdirMGF.listFiles()) 
				        for (File file : pMGFFiles) 
				        {
				 
				            BufferedReader mgfReaderp = new BufferedReader(new FileReader(file));
				            String temp;
				           
				             while ((temp = mgfReaderp.readLine()) != null) 
							{
				            	
				            	   double rtmin=0;
								   double pm=0;
								   double signal=0;
								   String origin="";
								   String charge="";
								   int pnum=0;
								   String scan="";
								   List<String> spec = new ArrayList<String>(); 
								   
				            	while(!temp.equals("END IONS") )
				            	{
							
							   
							   if(temp.matches("^(TITLE=).*" ) ) 
							   {
								   origin=temp.replace("TITLE=","");;	 
								   MS2posnow++;
					               progress=(int)Math.ceil(MS2posnow/(double)MS2posall*99);
							       message="step 4/6: "+String.format("picking MS2(pos) %d%%.\n", progress);
							   }
							   else if(temp.matches("^(SCANS=).*" ) ) 
							   {
								   scan=temp;
							   }
							   else if(temp.matches("^(RTINSECONDS=).*" )  ) 
							   {
								   rtmin=Double.parseDouble(temp.split("=")[1])/60; 
							   }
							   else if(temp.matches("^(PEPMASS=).*" ) ) 
							   {
								   pm=Double.parseDouble(temp.split("=|\\s+")[1]);
								   if(temp.split("=|\\s+").length>2)
								   {
									   signal=Double.parseDouble(temp.split("=|\\s+")[2]);
								   }
								   else
									   signal=0;
							   }
							   else if(temp.matches("^(CHARGE=).*" ) ) 
							   {
								   charge=temp.split("=")[1];
							   }
							   else if (temp.matches("^(\\p{Digit}).*" ) )
							   {
								 
								   spec.add(temp);
								   pnum++;
							   }
							   temp=mgfReaderp.readLine();
							   if(temp==null)
							   break;
					            //System.out.println(temp);
					           
				            }
				            	 if(temp==null)
									   break;
				            	 
				            
						            
				              for(int j=0;j<pdata.size();j++)
				              {
				            	  if(pdata.get(j).passMS1filter==true)
				            	  {
				            	  if(Math.abs(pm-pdata.get(j).mz)<mztol)
				            	  {
				            		 
				            		  if(pdata.get(j).samplertinfo[porder]!=null)
				            		  {
				            				  if(rtmin>pdata.get(j).samplertinfo[porder].rtmin && rtmin<pdata.get(j).samplertinfo[porder].rtmax)
				            				  {
				            					  MSMS matched=new MSMS(porder,origin+" "+scan,"pos",rtmin,pm,signal,charge,pnum,spec);
							    			      if(pdata.get(j).MS2==null)
							    			    	  pdata.get(j).initiateMS2();
							    			     
							    			      pdata.get(j).addMS2(matched);
				            				  }
				            			  }
				            		  }
				            	  
				              }
				            	  
				              }
							}
							mgfReaderp.close();
				            porder++;
				            
				            
				        }
				        
				        System.out.println("after Pick MS2 pos");
						 System.out.println(dtf.format(LocalTime.now()));
				         
               
				        //neg mode
				        int norder=0;
				        Collection<File> nMGFFiles = FileUtils.listFiles(ndir, new String[]{"mgf"}, true);
				        int nsum=nMGFFiles.size();
	                    if(nsum!=sample)
	 	                    {
	 	                    	JOptionPane.showMessageDialog(null,
	 	        	  	   			    "The number of MGF files in negative mode doesn't match with the sample numbers in XCMS output, please check.",
	 	        	  	   			    "Error Message",
	 	        	  	   			    JOptionPane.ERROR_MESSAGE);
	 	                    	f.close();
	 	                    	throw new IllegalArgumentException();
	 	                    	
	 	                    }
	                    //count number of neg MS2 for progress monitor
	                    int MS2negall=0;
	                    int MS2negnow=0;
	                    for (File file : nMGFFiles) 
				        {
				 
				            BufferedReader mgfReadern0 = new BufferedReader(new FileReader(file));
				            String temp;
				           
				             while ((temp = mgfReadern0.readLine()) != null) 
							{
				            	 if(temp.equals("END IONS") )
					            	{
				            		 MS2negall++;
					            	}
							}
				             mgfReadern0.close();
				        }
	                    
				        for (File file : nMGFFiles) {
				 
				            BufferedReader mgfReadern = new BufferedReader(new FileReader(file));
				            String temp;
				           
				             while ((temp = mgfReadern.readLine()) != null) 
							{
				            	
				            	   double rtmin=0;
								   double pm=0;
								   double signal=0;
								   String origin="";
								   String scan="";
								   String charge="";
								   int pnum=0;
								   List<String> spec = new ArrayList<String>(); 
								   
				            	while(!temp.equals("END IONS") )
				            	{
							
							   
							   if(temp.matches("^(TITLE=).*" ) ) 
							   {
								   origin=temp.replace("TITLE=","");
								   MS2negnow++;
					               progress=(int)Math.ceil(MS2negnow/(double)MS2negall*99);
						            message="step 5/6: "+String.format("picking MS2(neg) %d%%.\n", progress);

							   }
							  else if(temp.matches("^(SCANS=).*" ) ) 
							   {
								   scan=temp;
							   }

							   else if(temp.matches("^(RTINSECONDS=).*" )  ) 
							   {
								   rtmin=Double.parseDouble(temp.split("=")[1])/60; 
							   }
							   else if(temp.matches("^(PEPMASS=).*" ) ) 
							   {
								   pm=Double.parseDouble(temp.split("=|\\s+")[1]);
								   if(temp.split("=|\\s+").length>2)
								   {
									   signal=Double.parseDouble(temp.split("=|\\s+")[2]);
								   }
								   else
									   signal=0;
							   }
							   else if(temp.matches("^(CHARGE=).*" ) ) 
							   {
								   charge=temp.split("=")[1].replace("+","-");
							   }
							   else if (temp.matches("^(\\p{Digit}).*" ) )
							   {
								 
								   spec.add(temp);
								   pnum++;
							   }
							   temp=mgfReadern.readLine();
							   if(temp==null)
								   break;
				            }
				            	
				               if(temp==null)
									   break;
				               
				               
				              for(int j=0;j<ndata.size();j++)
				              {
				            	  if(ndata.get(j).passMS1filter==true)
				            	  {
				            	  if(Math.abs(pm-ndata.get(j).mz)<mztol)
				            	  {
				            		 
				            		  if(ndata.get(j).samplertinfo[norder]!=null)
				            		  {
				            				  if(rtmin>ndata.get(j).samplertinfo[norder].rtmin && rtmin<ndata.get(j).samplertinfo[norder].rtmax)
				            				  {
				            					  MSMS matched=new MSMS(norder,origin+" "+scan,"neg",rtmin,pm,signal,charge,pnum,spec);
							    			      if(ndata.get(j).MS2==null)
							    			    	  ndata.get(j).initiateMS2();
							    			     
							    			      ndata.get(j).addMS2(matched);
				            				  }
				            			  }
				            		  }
				            	  
				              }
				            	  
				              }
							}
							mgfReadern.close();
				            norder++;
				            
				        }
				        
				        System.out.println("after Pick MS2 neg");
						 System.out.println(dtf.format(LocalTime.now()));
				      //select the MS2 from samples
				      
				      //criteria: 1.Filter pos/neg matrix to get sample without bad pos/neg ratios
				        //2. Rank the filtered sample from large to small adduct number
				       //3.If multiple meet criteria, rank the one with the most abundant peak

				        
				        int[] pickMS2pos=new int[MS1filtered.size()];
				        int[] pickMS2neg=new int[MS1filtered.size()];
				        
				        for(int p=0;p<MS1filtered.size();p++)
				        {
				        	 int link=MS1filtered_gr_order.get(p);
					    	  for (int q=0;q<MS1filtered.get(p).size();q++)	
					    	  {
					    		
					    		  if(!(MS1filtered.get(p).get(q).peak.MS2==null))
					    		  {
					    			  int selected=0;
					    			  ArrayList<Integer> samplesource = new ArrayList<Integer>();
					    			  
						    		  int maxadduct=10;
					    			  int count=0;
					    			  
					    			  //remove duplicate sample name from MSMS
					    			  
					    			  for(int s=0;s<MS1filtered.get(p).get(q).peak.MS2.size();s++)
					    			  {
					    			 	samplesource.add(MS1filtered.get(p).get(q).peak.MS2.get(s).fileorder);
					    			  }
					    			  List<Integer> uniquesouce=samplesource.stream().distinct().collect(Collectors.toList());
					    			  
					    			  //get the max number of adducts
					    			  for(int x=0;x<uniquesouce.size();x++)
					    			  {
					    				int r=uniquesouce.get(x);
					    			
					    				
					    				 // if(pmax_nmax_ratio[link][r]<2)
					    				if(pmax_nmax_ratio_bad_ok[link][r]==null)
					    			  {  
					    				  if(count==0)
					    				  {
					    					  maxadduct=ncountionsample[link][r]+pcountionsample[link][r];
					    					  
					    				  }
					    				  else
					    				  {
					    					  if(ncountionsample[link][r]+pcountionsample[link][r]>maxadduct)
					    					  {
					    						  maxadduct=ncountionsample[link][r]+pcountionsample[link][r];
					    					  }	
					    				  }
  
					    				  count++; 
					    			  }
					    			  }
					    			  //among samples with max adduct number, get the sample with highest intensity
					    			  //don't forget to exclude the bad ratio again
					    			  int count2=0;
					    			  double maxintensity=10000;
					    			  int pickintensity=0;
					    			
					    			  for(int x=0;x<uniquesouce.size();x++)
					    			  {
					    				int r=uniquesouce.get(x);
					    			  
					    			  if(pmax_nmax_ratio_bad_ok[link][r]==null)
					    			  {
					    			  if(ncountionsample[link][r]+pcountionsample[link][r]==maxadduct)
					    			  {
					    				  if(count2==0)
					    				  {
					    					 maxintensity=MS1filtered.get(p).get(q).peak.intensity[r];
					    					  pickintensity=r;
					    				  }
					    				  else if (MS1filtered.get(p).get(q).peak.intensity[r]>maxintensity)
					    				  {
					    					  maxintensity=MS1filtered.get(p).get(q).peak.intensity[r];
					    					  pickintensity=r;
					    				  }
					    				  count2++;
					    			  }
					    			  }
					    			  }
					    			  //get the MS/MS closest to RT apex (sometimes weired)
					    			  //change to highest MS2 precursor abundance
					    			  //update 110322: due to no MS2 precursor abundance in Rawconverter(MSconvert has wrong pepmass)
					    			  //change back to closest to RT(apex)
					    			  int count3=0;
					    			  double signalmax=100;
					    			  double min=1;
					    			  for(int s=0;s<MS1filtered.get(p).get(q).peak.MS2.size();s++)
					    			  {
					    				  int r=MS1filtered.get(p).get(q).peak.MS2.get(s).fileorder;
					    				  
					    				  if(r==pickintensity)
                                          {
					    				  if(count3==0)
					    				  {
					    					  //min=Math.abs(MS1filtered.get(p).get(q).peak.MS2.get(s).rt-MS1filtered.get(p).get(q).peak.rt);
					    					  min=Math.abs(MS1filtered.get(p).get(q).peak.MS2.get(s).rt-MS1filtered.get(p).get(q).peak.samplertinfo[r].rt);
					    					  selected=s;
					    				  }
					    				  
					    				  else if(Math.abs(MS1filtered.get(p).get(q).peak.MS2.get(s).rt-MS1filtered.get(p).get(q).peak.samplertinfo[r].rt)<min)
					    					  {
					    						  min=Math.abs(MS1filtered.get(p).get(q).peak.MS2.get(s).rt-MS1filtered.get(p).get(q).peak.samplertinfo[r].rt);
					    						  selected=s;
					    					  }
					    					  
					    					  
					    					/*  
					    				  else if(Math.abs(MS1filtered.get(p).get(q).MS2.get(s).rt-MS1filtered.get(p).get(q).peak.rt)<min)
					    					  {
					    						  min=Math.abs(MS1filtered.get(p).get(q).MS2.get(s).rt-MS1filtered.get(p).get(q).peak.rt);
					    						  selected=s;
					    					  }
					    					  */
					    					  
					    					
					    				  
					    				  count3++; 
                                          }
					    				  
					    				  /*
					    				  if(r==pickintensity)
					    				  {
					    					  if(count3==0)
					    					  {
					    						  signalmax=MS1filtered.get(p).get(q).peak.MS2.get(s).intensity;
					    						  selected=s;
					    					  }
					    					  else if(MS1filtered.get(p).get(q).peak.MS2.get(s).intensity>signalmax)
					    					  {
					    						  signalmax=MS1filtered.get(p).get(q).peak.MS2.get(s).intensity;
					    						  selected=s;
					    					  }
					    					 
					    					  count3++;
					    				  }
					    				   */
					    			  }
		
					    			  if(count3>0)
					    		
					    			  {
					    			  MS1filtered.get(p).get(q).selectMS2(MS1filtered.get(p).get(q).peak.MS2.get(selected));
					    			  if(MS1filtered.get(p).get(q).peak.mode.equals("pos"))
					    			  pickMS2pos[p]++;
					    			  else if(MS1filtered.get(p).get(q).peak.mode.equals("neg"))
					    				  pickMS2neg[p]++;
					    			  List<double[]> fra=new ArrayList<double[]>();
					    			  
					    			  em.write("BEGIN IONS"+"\n");
									  em.write("TITLE=Group_"+MS1filtered_gr_order.get(p)+";"+MS1filtered.get(p).get(q).compound+" "+MS1filtered.get(p).get(q).adduct+";"+MS1filtered.get(p).get(q).peak.mode+"_"+MS1filtered.get(p).get(q).peak.pcnum+"\n");
									  em.write("RTINSECONDS="+MS1filtered.get(p).get(q).selectedMS2.rt*60 +"\n");
									   em.write("PEPMASS="+MS1filtered.get(p).get(q).selectedMS2.pepmass+" "+MS1filtered.get(p).get(q).selectedMS2.intensity +"\n");
									   em.write("RAWFILE="+MS1filtered.get(p).get(q).selectedMS2.title+"\n");
									   if(!MS1filtered.get(p).get(q).selectedMS2.charge.equals(""))
									   {
										   em.write("CHARGE="+ MS1filtered.get(p).get(q).selectedMS2.charge+"\n");
									   }
									   for (int c=0;c<MS1filtered.get(p).get(q).selectedMS2.peaknum;c++)
									   {
										   em.write(MS1filtered.get(p).get(q).selectedMS2.spectra.get(c)+"\n");
										   fra.add(new double[] {Double.parseDouble(MS1filtered.get(p).get(q).selectedMS2.spectra.get(c).split(" ")[0]),Double.parseDouble(MS1filtered.get(p).get(q).selectedMS2.spectra.get(c).split(" ")[1])});
										  
									   }
					    			  em.write("END IONS"+"\n");
					    			  MS1filtered.get(p).get(q).selectedMS2.addfragment(fra);
					    			 
					    			  }
					    		  }
					    		 
					    		  
					    	  }
				        }
				                    em.close();
				        
				        

		    
				        
			 System.out.println("Finish representaive MS/MS selection");		   
            
			 //read  lib MSP file, sqrt its fragment intensity
		        List<MSMS> lib = new ArrayList<MSMS>();
		        FileReader ref=new FileReader(MSP);
				 Scanner scanner0 =new Scanner(ref);
				 int libcount=0;
				
					while(scanner0.hasNextLine())
						{
						   String temp0=scanner0.nextLine();
						   if(temp0.toUpperCase().matches("^(NAME:).*" ) ) 
						   {
						   libcount++;
						   }
						}
					scanner0.close();
					
					Scanner scanner1 = new Scanner(new FileReader(MSP));
					String t[];
					String temp1=scanner1.nextLine();
					for(int q=0;q<libcount;q++)
					{
				    	 
				    	 String name="";
				    	 String adduct="";
				    	 String mode="";
				    	 String type="";
				    	 double precursor=0.0;
				    	 List<double[]> lfra=new ArrayList<double[]>();;
				    	 int match=0;
						  while(temp1.trim().length()!=0&&scanner1.hasNextLine())
						  {	  
							  if(temp1.toUpperCase().matches("^(NAME:).*" ) ) 
							  {
								  name=temp1.split("\\s+")[1];
								  adduct=temp1.split("\\s+")[2];
								  if(adduct.contains("]+"))
								  {
									  mode="pos";
								  }
								  else if (adduct.contains("]-") || adduct.contains("]2-"))
									  mode="neg";
							  }
							  if(temp1.matches("^(Comments:).*" ) ) 
							  {
								  type=temp1.split("\\s+")[1];
							  }
							  if(temp1.matches("^(PrecursorMZ:).*" ) ) 
							  {
								  precursor=Double.parseDouble(temp1.split("\\s+")[1]);  
							  }
							  
							  {
							  if(temp1.matches("^(\\p{Digit}).*"  ) ) 
							   {
								   t=temp1.split("\\s+");
								   double mz=Double.parseDouble(t[0]);
								   double intensity=Double.parseDouble(t[1]);
								   lfra.add(new double[] {mz,Math.sqrt(intensity)});
							   }
							  }
							  temp1=scanner1.nextLine();
						  }
						
							  lib.add(new MSMS(name,type,adduct,mode,precursor,lfra));
							 // System.out.println(name+";"+adduct+";"+mode+";"+precursor);
						  
						  //this part is essential or it won't process the second spectra
						  if(scanner1.hasNextLine())
						  {
							  temp1=scanner1.nextLine();
						  }
			           
					}	
					scanner1.close();
				
					  //prep MS2 spectra for MS2 lib matching: MS1filtered
			          //normalize peak intensity by base peak intensity and make base peak intensity 999
			           //filter out peaks with relatively intensity <3% of base peak
			           //sqrt the intensity 
					 for(int p=0;p<MS1filtered.size();p++)
			         {
						 for (int q=0;q<MS1filtered.get(p).size();q++)	
					        {
							 if(!(MS1filtered.get(p).get(q).selectedMS2==null))
					        	{
					            	 
					            	 //find base peak in MS2
					            	 MS1filtered.get(p).get(q).addfeature(0);
					            	 double basepeak=MS1filtered.get(p).get(q).selectedMS2.fragment.get(0)[1];
					            	 for(int k=1;k<MS1filtered.get(p).get(q).selectedMS2.peaknum;k++)
					            	 {
					            		 if(MS1filtered.get(p).get(q).selectedMS2.fragment.get(k)[1]>basepeak)
					            			 basepeak=MS1filtered.get(p).get(q).selectedMS2.fragment.get(k)[1];
					            	 }
					            	 
					            	 List<double[]> norm1000fra=new ArrayList<double[]>();
					            	 List<double[]> sqrtfra=new ArrayList<double[]>();
					            	 int normpeaknum=0;
					            	 for(int j=0;j<MS1filtered.get(p).get(q).selectedMS2.peaknum;j++)
				            		 {
					            		//update 120222: remove the criteria of 0.03 for low abundant peaks in modified MS2
				            				// if(MS1filtered.get(p).get(q).selectedMS2.fragment.get(j)[1]/basepeak>0.03)
				                          {
				            					 norm1000fra.add(new double[] {MS1filtered.get(p).get(q).selectedMS2.fragment.get(j)[0],MS1filtered.get(p).get(q).selectedMS2.fragment.get(j)[1]/basepeak*999});
				            					 
				            					 sqrtfra.add(new double[] {MS1filtered.get(p).get(q).selectedMS2.fragment.get(j)[0],Math.sqrt(MS1filtered.get(p).get(q).selectedMS2.fragment.get(j)[1]/basepeak*999)});
				            					 normpeaknum++;
				            					 /*
				            					 if(MS1filtered.get(p).get(q).compound.contains("2OH_BA-C8H17") &&  MS1filtered.get(p).get(q).adduct.contains("+") )
				            					 {
				            						System.out.println(MS1filtered.get(p).get(q).selectedMS2.fragment.get(j)[0]+" "+MS1filtered.get(p).get(q).selectedMS2.fragment.get(j)[1]+" "+basepeak+" "+MS1filtered.get(p).get(q).selectedMS2.fragment.get(j)[1]/basepeak*999) ;
				            						
				            					 }
				            					 */
				                          }
				            		 }
					            	 MS1filtered.get(p).get(q).selectedMS2.normalizefragments(norm1000fra);
					            	 MS1filtered.get(p).get(q).selectedMS2.sqrtfragments(sqrtfra);
					            	 MS1filtered.get(p).get(q).selectedMS2.normalizepeaknum(normpeaknum);
					            	 
					            	
					            	 
					            	 
					        }
			         }
			         }
					
		  
							//calculate dot product score using sqrtfragment
								//export the hit with highest score
					 for(int p=0;p<MS1filtered.size();p++)
				        	for (int q=0;q<MS1filtered.get(p).size();q++)	
				        {
				        	if(!(MS1filtered.get(p).get(q).selectedMS2==null))
				        	{
				        		double dotproduct=0.0;
				        		double maxdp=0.0;
				        		int hitnum=0;
				        		for(int r=0;r<lib.size();r++)
				        	{
				            if(lib.get(r).mode.equals(MS1filtered.get(p).get(q).selectedMS2.mode))
				           {
				            	if(Math.abs(lib.get(r).pepmass-MS1filtered.get(p).get(q).selectedMS2.pepmass)<mztol)
				            {
				            	dotproduct=dotproduct(MS1filtered.get(p).get(q).selectedMS2.sqrtfragment,lib.get(r).sqrtfragment,mztol);
				            	if(hitnum==0)
				            	{
				            		maxdp=dotproduct;
				            		MS1filtered.get(p).get(q).selectedMS2.libmatch(lib.get(r).name,lib.get(r).type,lib.get(r).adduct,maxdp,lib.get(r).pepmass);
				            	}
				            		else 
				            		{
				            			if(dotproduct>maxdp)
				            			{
				            				maxdp=dotproduct;
				            			    MS1filtered.get(p).get(q).selectedMS2.libmatch(lib.get(r).name,lib.get(r).type,lib.get(r).adduct,maxdp,lib.get(r).pepmass);
				            			}
				            		}	
				            		hitnum++;
				            }
				           
				        	}
				        }
				        		if(hitnum>0)
				        		{
				        			MS1filtered.get(p).get(q).selectedMS2.inlib(1);
				        		}
				        }
				        }
					 
	    	 System.out.println("Finish MS/MS library search: direct match");	
	    	   System.out.println("after MS2 direct match");
				 System.out.println(dtf.format(LocalTime.now()));

	    	//if TBA or GBA or other amino acid conjugates (AaBA) was not in lib, remove its characteristic fragment such as m/z 126 or 76 in pos mode, change precursor to match with BA in lib
	    	 //add user-defined conjugates 11/30/22->add equivalent to new conjugate
			//most unconjugated BA in MS2 lib don't have [M+H]+ adduct therefore precursor could not match with new conjugate
			//for those, allow other adduct to match 12/1/22 
		
	    	 for(int p=0;p<MS1filtered.size();p++)
		        	for (int q=0;q<MS1filtered.get(p).size();q++)	
		        {
		        	if(!(MS1filtered.get(p).get(q).selectedMS2==null))
		        	{
		        	    if(MS1filtered.get(p).get(q).selectedMS2.preinlib==0)
		        	    {
		        	    	double dotproduct=0.0;
		        	    	double dotproducttopN=0.0;
			        		double maxdp=0.0;
			        		int hitnum=0;
			        	
			        		
		        	    	if (MS1filtered.get(p).get(q).adduct.contains("]+"))
				            {
		        	    		double modifiedpre=0;
		        	    		List<double[]> modifiedMS2=new ArrayList<double[]>();
		        	    		//List<double[]> modifiedMS2noSqrt=new ArrayList<double[]>();
		        	    		List<double[]> modifiedMS2topN=new ArrayList<double[]>();
		        	    		List<double[]> modifiedMS2topNnoSqrt=new ArrayList<double[]>();
		        	    		
				            	if(MS1filtered.get(p).get(q).compound.contains("GBA"))
				            	{
				            		modifiedpre=MS1filtered.get(p).get(q).selectedMS2.pepmass-57.021;
				            		modifiedMS2=removefragment(MS1filtered.get(p).get(q).selectedMS2.normalizefragment,76.0398,mztol);
				            	}
				            	else if(MS1filtered.get(p).get(q).compound.contains("TBA"))
				            	{
				            		modifiedpre=MS1filtered.get(p).get(q).selectedMS2.pepmass-107.004;
				            		modifiedMS2=removefragment(MS1filtered.get(p).get(q).selectedMS2.normalizefragment,126.0225,mztol);
				            	}
				            	else if (MS1filtered.get(p).get(q).compound.contains("-BA"))
				            	{
				            		String temp="";
				            		for(int i=0;i<AA.size();i++)
				            		{
				            			if(MS1filtered.get(p).get(q).compound.contains(AA.get(i).type))
				            			{
				            				modifiedpre=MS1filtered.get(p).get(q).selectedMS2.pepmass-AA.get(i).prediff;
				            				temp=MS1filtered.get(p).get(q).compound.replace(AA.get(i).type+"-BA","BA");
				            				for(int m=0;m<AA.get(i).promz.size();m++)
				            				{
				            					
				            					modifiedMS2=removefragment(MS1filtered.get(p).get(q).selectedMS2.normalizefragment,AA.get(i).promz.get(m),mztol);
				            					
				            				}
				            				break;
				            			}
				            		}
				            		
				            		for(int r=0;r<lib.size();r++)
						        	{
						            if(lib.get(r).mode.equals(MS1filtered.get(p).get(q).selectedMS2.mode))
						            	if(Math.abs(lib.get(r).pepmass-modifiedpre)<mztol || temp.equals(lib.get(r).type))
						            {
						            		
						            		dotproduct=dotproduct(modifiedMS2,lib.get(r).sqrtfragment,mztol);
							            	if(hitnum==0)
							            	{
							            		maxdp=dotproduct;
							            		MS1filtered.get(p).get(q).modifiedlibmatch(lib.get(r).name,lib.get(r).type,lib.get(r).adduct,maxdp);
							            		
							            	}
							            		else 
							            		{
							            			if(dotproduct>maxdp)
							            			{
							            				maxdp=dotproduct;
							            			    MS1filtered.get(p).get(q).modifiedlibmatch(lib.get(r).name,lib.get(r).type,lib.get(r).adduct,maxdp);
							            			    
							            			}
							            		}	
							            		hitnum++;
						            }
						        	}
				            		
				            	}
				            	else if(MS1filtered.get(p).get(q).compound.contains("unknown"))
				            	{
				            
				            		String temp="";
				            		 for(int i=0;i<cuslib.size();i++)
				            		 {
				            			 if(MS1filtered.get(p).get(q).compound.contains(cuslib.get(i).id))
					            			{
				            				    
					            				modifiedpre=MS1filtered.get(p).get(q).selectedMS2.pepmass-cuslib.get(i).mass+18.0106;
					            				modifiedMS2=removefragment(MS1filtered.get(p).get(q).selectedMS2.normalizefragment,cuslib.get(i).mass+1.0073,mztol);	
					            				temp=MS1filtered.get(p).get(q).compound.replace("unknown:","").replace("BA-"+cuslib.get(i).id,"BA");
					            				/*
					            				if(MS1filtered.get(p).get(q).compound.contains("2OH_BA-C8H17") &&  MS1filtered.get(p).get(q).adduct.contains("+") )
					            				{
									            	 for(int y=0;y<modifiedMS2.size();y++)
									            	 {
									            		 System.out.println(modifiedMS2.get(y)[0]+" "+modifiedMS2.get(y)[1]);
									            	 }
									            		
					            				}
					            				*/
					            				modifiedMS2topN=removetopNfragment(MS1filtered.get(p).get(q).selectedMS2.normalizefragment,5,true);
					            				modifiedMS2topNnoSqrt=removetopNfragment(MS1filtered.get(p).get(q).selectedMS2.normalizefragment,5,false);
					            				
					            				
		    	            					 break;
					            			}
				            		 }
				            		 
				            		 
				            		 
				            			for(int r=0;r<lib.size();r++)
							        	{
							            if(lib.get(r).mode.equals(MS1filtered.get(p).get(q).selectedMS2.mode))
							            	if(Math.abs(lib.get(r).pepmass-modifiedpre)<mztol || temp.equals(lib.get(r).type))
							            {
							            		
							            		dotproduct=dotproduct(modifiedMS2,lib.get(r).sqrtfragment,mztol);
							            		dotproducttopN=dotproduct(modifiedMS2topN,lib.get(r).sqrtfragment,mztol);
								            	if(hitnum==0)
								            	{
								            		maxdp=dotproduct;
								            		MS1filtered.get(p).get(q).modifiedlibmatch(lib.get(r).name,lib.get(r).type,lib.get(r).adduct,maxdp);
								            		MS1filtered.get(p).get(q).modifiedtopNscore(dotproducttopN);
								            	}
								            		else 
								            		{
								            			if(dotproduct>maxdp)
								            			{
								            				maxdp=dotproduct;
								            			    MS1filtered.get(p).get(q).modifiedlibmatch(lib.get(r).name,lib.get(r).type,lib.get(r).adduct,maxdp);
								            			    MS1filtered.get(p).get(q).modifiedtopNscore(dotproducttopN);
								            			}
								            		}	
								            		hitnum++;
							            }
							        	}

				            	}
				            	
				            if(!MS1filtered.get(p).get(q).compound.contains("unknown") && !MS1filtered.get(p).get(q).compound.contains("-BA"))
				        	for(int r=0;r<lib.size();r++)
				        	{
				            if(lib.get(r).mode.equals(MS1filtered.get(p).get(q).selectedMS2.mode))
				            {
				            	if(Math.abs(lib.get(r).pepmass-modifiedpre)<mztol)
				            {
				            	dotproduct=dotproduct(modifiedMS2,lib.get(r).sqrtfragment,mztol);
				            	if(hitnum==0)
				            	{
				            		maxdp=dotproduct;
				            		MS1filtered.get(p).get(q).modifiedlibmatch(lib.get(r).name,lib.get(r).type,lib.get(r).adduct,maxdp);
				            	}
				            		else 
				            		{
				            			if(dotproduct>maxdp)
				            			{
				            				maxdp=dotproduct;
				            			    MS1filtered.get(p).get(q).modifiedlibmatch(lib.get(r).name,lib.get(r).type,lib.get(r).adduct,maxdp);
				            			}
				            		}	
				            		hitnum++;
				            }
				            	
				            		
				           
				        	}
				        }
				        		if(hitnum>0)
				        		{
				        			MS1filtered.get(p).get(q).inlib(1);
				        		}
				        		
				            }

		        	    }
		        	    
		        	}
		        
		        }
			  
          	
			
			 
          	 System.out.println("Finish MS/MS library search: modified match");
          	 System.out.println("after MS2 modified match");
			 System.out.println(dtf.format(LocalTime.now()));
          	 
          	//compare equvalent of MS2 hit and mz annotation and get those with dot product>500
			 int[] psumlib=new int[MS1filtered.size()];
			 int[] nsumlib=new int[MS1filtered.size()];
			 int[] psumlibmodified=new int[MS1filtered.size()];
			
			 //only for [M-H]-,[M-2H]2-,[M+H]+,[M+NH4]+,[M+H-nH2O]+
			 int[] MHpsumlib=new int[MS1filtered.size()];
			 int[] MHnsumlib=new int[MS1filtered.size()];
			 int[] MHpsumlibmodified=new int[MS1filtered.size()];
			

			 for(int p=0;p<MS1filtered.size();p++)
			 {
		    	  for (int q=0;q<MS1filtered.get(p).size();q++)	
		    	  {
		    		  String MS2eq="ms2";
		    		  String mzeq="mz";
		    		  String adduct=MS1filtered.get(p).get(q).adduct;
		    		  String MS2noadduct="ba";
		    		  String mznoadduct="mba";
		    		  //direct match
		    		  
		    		  if(MS1filtered.get(p).get(q).selectedMS2==null || MS1filtered.get(p).get(q).selectedMS2.hit==null  || MS1filtered.get(p).get(q).selectedMS2.dotproduct==0)
		    		  {
		    			  MS1filtered.get(p).get(q).MS2match("");
		    		  }
		    		  else
		    		  {
		    			  
		    					  for(int h=0;h<eq.length;h++)
		    					  {
		    						  if((MS1filtered.get(p).get(q).selectedMS2.hittype+" "+MS1filtered.get(p).get(q).selectedMS2.hitadduct).equals(eq[h][0]))
		    						 {
		    							  MS2eq=eq[h][1];
		    						 }
		    					  
		    			  }
		    			  for (int n=0;n<eq.length;n++) 
		    			  {
		    				  if((MS1filtered.get(p).get(q).compound+" "+MS1filtered.get(p).get(q).adduct).equals(eq[n][0]))
		    						  mzeq=eq[n][1];
		    			  }
		    			  if(MS2eq.equals(mzeq) && MS1filtered.get(p).get(q).selectedMS2.dotproduct>500)
		    			  {
		    				  MS1filtered.get(p).get(q).MS2match("y");
		    				  if(MS1filtered.get(p).get(q).adduct.contains("]+"))
		    				  {
		    					  psumlib[p]++;
		    					  if( !adduct.contains("-SO3") && !adduct.contains("-GlcA") && !adduct.contains("-Hex"))
		    					  {
		    						  MHpsumlib[p]++;
		    					  }
		    				  }
		    				  else {
		    					  nsumlib[p]++;
		    					  if(adduct.equals("[M-H]-")||adduct.equals("[M-2H]2-"))
		    						  MHnsumlib[p]++;
		    				  }
		    			  }
		    			  else
		    				  MS1filtered.get(p).get(q).MS2match("n");
		    		  }
		    		  
		    		  
		    		  //modified MS2
		    			  if(MS1filtered.get(p).get(q).selectedMS2==null || MS1filtered.get(p).get(q).modifiedhit==null  || MS1filtered.get(p).get(q).modifieddotproduct==0)
                                MS1filtered.get(p).get(q).modifiedMS2match("");
		    			  else if (MS1filtered.get(p).get(q).modifiedinlib==1)
		    			  {
		    			 
		    					  for(int h=0;h<eq.length;h++)
		    					  {
		    						  if((MS1filtered.get(p).get(q).modifiedhittype+" "+MS1filtered.get(p).get(q).modifiedhitadduct).equals(eq[h][0]))
		    						 {
		    							  MS2eq=eq[h][1];
		    							  MS2noadduct=MS2eq.split("\\s+")[0];
		    						 }
		    					  }
		    			
		    			  for (int n=0;n<eq.length;n++) 
		    			  {
		    				  if((MS1filtered.get(p).get(q).compound+" "+MS1filtered.get(p).get(q).adduct).equals(eq[n][0]))
		    				  {
		    					      if(eq[n][1].contains("GBA") || eq[n][1].contains("TBA"))
		    						  mzeq=eq[n][1].replace("GBA","BA").replace("TBA","BA");
		    					      else if (eq[n][1].contains("-BA") )
		    					      {
		    					    	  for(int i=0;i<AA.size();i++)
		    			            		{
		    			            			if(eq[n][1].contains(AA.get(i).type))
		    			            			{
		    			            				mzeq=eq[n][1].replace(AA.get(i).type+"-BA","BA");
		    			            				mznoadduct=mzeq.split("\\s+")[0];
		    			            				break;
		    			            			}
		    			            		}
		    					      }
		    					      else if (eq[n][1].contains("unknown") )
		    					      {
		    					    	  for(int i=0;i<cuslib.size();i++)
		    					    	  {
		    					    		  if(eq[n][1].contains(cuslib.get(i).id))
		    			            			{
		    					    			  mzeq=eq[n][1].replace("unknown:","").replace("BA-"+cuslib.get(i).id,"BA");
		    			            			  mznoadduct=mzeq.split("\\s+")[0];
		    					    			  break;
		    			            			}
		    		
		    					    	  }
		    					    	  
		    					      }
		    					      
		    							  
		    				  }
		    				         
		    			  }
		    			  if((MS2eq.equals(mzeq) || ((MS1filtered.get(p).get(q).compound.contains("unknown")||MS1filtered.get(p).get(q).compound.contains("-BA")) && MS2noadduct.equals(mznoadduct))) && (MS1filtered.get(p).get(q).modifieddotproduct>500 || (MS1filtered.get(p).get(q).compound.contains("unknown") && MS1filtered.get(p).get(q).modifiedtopNdotproduct>500)))
		    			  {
		    				  MS1filtered.get(p).get(q).modifiedMS2match("y");
		    				  psumlibmodified[p]++;
		    				 
		    				  if( !adduct.contains("-SO3") && !adduct.contains("-GlcA") && !adduct.contains("-Hex"))
		    				  {
		    					  MHpsumlibmodified[p]++;
		    				  }
		    			  }
		    			  else
		    				  MS1filtered.get(p).get(q).modifiedMS2match("n");
		    		  }
		    			 
		    		  
		    	  }
		    	  progress=(int)Math.ceil(p/(double)MS1filtered.size()*99);
		         message="step 6/6: "+String.format("analyzing MS2 %d%%.\n", progress);
		    	  
			 }
			 
			//search characteristic product ion and neutral loss in selected MS2
			//exist or non-exist, ignore intensity threshold for simplification
	          double[] psumfeature=new double[MS1filtered.size()];
	          double[] nsumfeature=new double[MS1filtered.size()];
	        
			  //only for [M-H]-,[M-2H]2-,[M+H]+,[M+NH4]+,[M+H-nH2O]+
			  double[] MHpsumfeature=new double[MS1filtered.size()];
			  double[] MHnsumfeature=new double[MS1filtered.size()];
			
			//Report the peak with intensity ratio of 145/159>1->12-OH
			  double[] pratio=new double[MS1filtered.size()];
	          
	         for(int p=0;p<MS1filtered.size();p++)
	         {
	        	
	        	 nsumfeature[p]=0;
	        	 psumfeature[p]=0;
	        	 pratio[p]=0;
	        	 
	        	for (int q=0;q<MS1filtered.get(p).size();q++)	
	        {
	        	 String type=MS1filtered.get(p).get(q).compound.split("_")[1];
	             String adduct=MS1filtered.get(p).get(q).adduct;	
	             double pre=MS1filtered.get(p).get(q).peak.mz;
	             if(!(MS1filtered.get(p).get(q).selectedMS2==null))
	        	{
	            	 
	            	
	            	 MS1filtered.get(p).get(q).addfeature(0);
	            	 if(adduct.contains("]+")) 
	            	 {	 
	            	//calculate 145/159
	            		 if(Math.abs(pre-355.2631)<mztol || Math.abs(pre-373.2737)<mztol)
	            		 {
	            		double inten145=0;
	            		double inten159=0;
	            		
	            	  for(int j=0;j<MS1filtered.get(p).get(q).selectedMS2.normalizepeaknum;j++)
	            		 {
	            			 if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-144.0939-1.0073)<mztol)
	            				 inten145= MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[1];
	            			 else if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-158.1095-1.0073)<mztol)
	            				 inten159= MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[1];
	            		 }
	            	  if(inten145>0 && inten159>0)
	            	  {
	            		  double ratio=inten145/inten159;
	            		  MS1filtered.get(p).get(q).addratio(ratio); 
	            		  if(ratio>1)
	            		  {
	            		   pratio[p]=1;
	            		  }
	            	  }
	            		 }
	                 
	            	 if(type.contains("GBA"))
	            	 {
	            		 for(int j=0;j<MS1filtered.get(p).get(q).selectedMS2.normalizepeaknum;j++)
	            		 {
	            			 if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-76.0399)<mztol)
	            				 
	            					 MS1filtered.get(p).get(q).addfeature(1);
                             
	            		 }
	            	 }
	            	 else if(type.contains("TBA"))
	            	 {
	            		 for(int j=0;j<MS1filtered.get(p).get(q).selectedMS2.normalizepeaknum;j++)
	            		 {
	            			 if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-126.0225)<mztol)
	            				
	            					 MS1filtered.get(p).get(q).addfeature(1);
	            		 }
	            		 
	            	 }
	            	 else if(type.contains("-BA"))
	            	 {
	            		 
	            		 for(int i=0;i<AA.size();i++)
		            		{
		            			if(type.contains(AA.get(i).type))
		            			{
		            				 int countmatch=0;
		            				 for(int j=0;j<MS1filtered.get(p).get(q).selectedMS2.normalizepeaknum;j++)
		    	            		 {
		            					 for(int m=0;m<AA.get(i).promz.size();m++)
		            					 {
		    	            			 if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-AA.get(i).promz.get(m))<mztol)
		    	            				countmatch++;		
		    	            		     }
		    	            		 }
		            				 //updated 12/12/22: >1/2 of feature detected->OK,round up, i.e. 3/2=2
			            				// if(countmatch==AA.get(i).promz.size())
			            				 if(countmatch>(int)Math.ceil(AA.get(i).promz.size()/2.0))
			            				 MS1filtered.get(p).get(q).addfeature(1);
		            				 		            			}
		            		}

	            	 }
	            	 else if(MS1filtered.get(p).get(q).compound.contains("unknown"))
	            	 {
	            		 boolean pfinished = false;
	            		 for(int i=0;i<cuslib.size() && !pfinished;i++)
	            		 {
	            			 if(type.equals("BA-"+cuslib.get(i).id))
	            			 {
	            				 for(int j=0;j<MS1filtered.get(p).get(q).selectedMS2.normalizepeaknum;j++)
	    	            		 {
	    	            			 if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-cuslib.get(i).mass-1.0073)<mztol)
	    	            			 {
	    	            				 
	    	            					 MS1filtered.get(p).get(q).addfeature(1);
	    	            					 pfinished=true;
	    	            					 break;
	                                
	    	            			 }
	    	            		 }
	            			 }
	            		 }
	            	 }
	            	 
	            
	            	 }
	            	 else if(type.equals("BA") ||type.equals("BA-S") ||type.equals("GBA")|| type.equals("TBA")||type.equals("TBA-S")||type.equals("GBA-S")||type.equals("TBA-GlcA")||type.equals("GBA-GlcA")||type.equals("BA-GlcA")||type.equals("BA-Hex")||type.equals("TBA-Hex")||type.equals("GBA-Hex"))
	            	 {
	            	 
	            	 if(type.equals("BA-S") && adduct.equals("[M-H]-"))
	            	 {
	            		 for(int j=0;j<MS1filtered.get(p).get(q).selectedMS2.normalizepeaknum;j++)
	            		 {
	            			 if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-96.9596)<mztol)
	            			 {
	            				 
	            					 MS1filtered.get(p).get(q).addfeature(1);
                            
	            			 }
	            		 }
	            	 }
	            	 if((type.equals("GBA") && adduct.equals("[M-H]-"))||(type.equals("GBA-S") && adduct.equals("[M-H-SO3]-"))||(type.equals("GBA-GlcA") && adduct.equals("[M-H]-")) ||(type.equals("GBA-Hex") && adduct.equals("[M-H]-")))
	            	 {
	            		 for(int j=0;j<MS1filtered.get(p).get(q).selectedMS2.normalizepeaknum;j++)
	            		 {
	            			 if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-74.0242)<mztol)
	            			 {
	            				 
	            					 MS1filtered.get(p).get(q).addfeature(1);
                            
	            			 }
	            		 }
	            	 }
	            	 
	            	 
	            	 if((type.equals("TBA") && adduct.equals("[M-H]-"))||(type.equals("TBA-S") && adduct.equals("[M-H-SO3]-")))
	            	 {
	            		 double a=0,b=0,c=0;
	            		 double la=0,lb=0,lc=0;
	            		 for(int j=0;j<MS1filtered.get(p).get(q).selectedMS2.normalizepeaknum;j++)
	            		 {
	            			 
	            			 if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-106.9803)<mztol)
	            			 { 
	            					 a=1;
                             
	            			 }
	            			 else if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-124.0068)<mztol)
	            			 { 
	            					 b=1;
                            
	            			 }
	            			 else if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-79.9568)<mztol)
	            			 { 
	            					 c=1;
                           
	            			 }
	            			 
	            				
	            		 }
	            		 if(a+b+c>=2)
	            			 MS1filtered.get(p).get(q).addfeature(1);
	            		
	            	 }
	            	 
	              	 if(type.equals("TBA-S") && adduct.equals("[M-2H]2-"))
	            	 {
	            		 int a=0,b=0,c=0,d=0;
	            		 double lc=0,ld=0;
	            		 for(int j=0;j<MS1filtered.get(p).get(q).selectedMS2.normalizepeaknum;j++)
	            		 {
	            			 
	            			 if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-106.9803)<mztol)
	            			 { 
	            					 a=1;
                             
	            			 }
	            			 else if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-124.0068)<mztol)
	            			 { 
	            					 b=1;
                            
	            			 }
	            			 else if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-79.9568)<mztol)
	            			 { 
	            					 c=1;
                            
	            			 }
	            			 else if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-96.9596)<mztol)
	            			 { 
	            			
	            			 }
	            			 
	            				
	            		 }
	            		 if(a+b+c+d>=3)
	            			 MS1filtered.get(p).get(q).addfeature(1);
	            		
	            	 }
	              	if(type.equals("TBA-S") && adduct.equals("[M-H]-"))
	            	 {
	            		 int a=0,b=0,c=0,d=0;
	            		
	            		 for(int j=0;j<MS1filtered.get(p).get(q).selectedMS2.normalizepeaknum;j++)
	            		 {
	            			 
	            			 if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-106.9803)<mztol)
	            			 {
	            					 a=1;
                            
	            			 }
	            			 else if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-124.0068)<mztol)
	            			 { 
	            					 b=1;
                          
	            			 }
	            			 else if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-79.9568)<mztol)
	            			 { 
	            					 c=1;
                           
	            			 }
	            			 else if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.pepmass-MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-79.9568)<mztol)
	            			 { 
	            					 d=1;
                          
	            			 }
	            			 
	            				
	            		 }
	            		 if(a+b+c+d>=3)
	            			 MS1filtered.get(p).get(q).addfeature(1);
	            		
	            	 }
	              	if((type.equals("GBA-S") && adduct.equals("[M-H]-"))||(type.equals("GBA-S") && adduct.equals("[M-2H]2-")))
	            	 {
	            		 int a=0,b=0;
	            		 double la=0,lb=0;
	            		 for(int j=0;j<MS1filtered.get(p).get(q).selectedMS2.normalizepeaknum;j++)
	            		 {
	            			 
	            			 if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-74.0242)<mztol)
	            			 { 
	            					 a=1;
                           
	            			 }
	            			 else if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-96.9596)<mztol)
	            			 {
	            					 b=1;
                           
	            			 }
	            				
	            		 }
	            		 if(a+b==2)
	            			 MS1filtered.get(p).get(q).addfeature(1);
	            		 
	            	 } 		
	              	if(type.equals("BA-GlcA") && adduct.equals("[M-H]-"))
	            	 {
	            		 for(int j=0;j<MS1filtered.get(p).get(q).selectedMS2.normalizepeaknum;j++)
	            		 {
	            			 if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.pepmass-MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-176.0321)<mztol)
	            			 { if(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[1]>200)
                            {
	            					 MS1filtered.get(p).get(q).addfeature(1);
	            					 MS1filtered.get(p).get(q).addcomment("Maybe 24-GlcA");
                            }
	            			 else 
                            {
	            					 MS1filtered.get(p).get(q).addfeature(1);
	            					 MS1filtered.get(p).get(q).addcomment("Maybe 3-GlcA");
                            }
	            			 }
	            			 
	            		 }
	            	 }
	              	if(type.equals("TBA-GlcA") && adduct.equals("[M-H]-"))
	            	 {
	            		 int a=0,b=0,c=0,d=0;
	            		
	            		 for(int j=0;j<MS1filtered.get(p).get(q).selectedMS2.normalizepeaknum;j++)
	            		 {
	            			 
	            			 if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-106.9803)<mztol)
	            			 { 
	            					 a=1;
                          
	            			 }
	            			 else if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-124.0068)<mztol)
	            			 { 
	            					 b=1;
                         
	            			 }
	            			 else if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-79.9568)<mztol)
	            			 { 
	            					 c=1;
                          
	            			 }
	            			 else if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.pepmass-MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-176.0321)<mztol)
	            			 { 
	            					 d=1;
                          
	            			 }
	            			 
	            				
	            		 }
	            		 if(a+b+c>=2 && d==1)
	            			 MS1filtered.get(p).get(q).addfeature(1);
	            		
	            	 }
	              	if(type.equals("BA-Hex") && adduct.equals("[M-H]-"))
	            	 {
	            		 for(int j=0;j<MS1filtered.get(p).get(q).selectedMS2.normalizepeaknum;j++)
	            		 {
	            			 if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.pepmass-MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-162.0528)<mztol)
	            			 
	            					 MS1filtered.get(p).get(q).addfeature(1);
	            				
	            			 
	            		 }
	            	 }
	              	if(type.equals("TBA-Hex") && adduct.equals("[M-H]-"))
	            	 {
	            		 int a=0,b=0,c=0,d=0;
	            		 for(int j=0;j<MS1filtered.get(p).get(q).selectedMS2.normalizepeaknum;j++)
	            		 {
	            			 
	            			 if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-106.9803)<mztol)
	            			 { 
	            					 a=1;
                          
	            			 }
	            			 else if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-124.0068)<mztol)
	            			 { 
	            					 b=1;
                         
	            			 }
	            			 else if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-79.9568)<mztol)
	            			 {
	            					 c=1;
                         
	            			 }
	            			 else if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.pepmass-MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-162.0528)<mztol)
	            			 { 
	            					 d=1;
                         
	            			 }
	            			 
	            				
	            		 }
	            		 if(a+b+c+d==4)
	            			 MS1filtered.get(p).get(q).addfeature(1);
	            	 }
	            	if(type.equals("BA") && adduct.equals("[M-H]-"))
	            	{
	            		MS1filtered.get(p).get(q).addfeature(999);
	            	}
	            	 }
	            	 else if(MS1filtered.get(p).get(q).compound.contains("unknown"))
	            	 {
	            		 boolean finished = false;
	            		 for(int i=0;i<cuslib.size() && !finished;i++)
	            		 {
	            			 if(type.equals("BA-"+cuslib.get(i).id))
	            			 {
	            				 for(int j=0;j<MS1filtered.get(p).get(q).selectedMS2.normalizepeaknum;j++)
	    	            		 {
	    	            			 if(Math.abs(MS1filtered.get(p).get(q).selectedMS2.normalizefragment.get(j)[0]-cuslib.get(i).mass+1.0073)<mztol)
	    	            			 {
	    	            				 
	    	            					 MS1filtered.get(p).get(q).addfeature(1);
	    	            					 finished=true;
	    	            					 break;
	                                
	    	            			 }
	    	            		 }
	            			 }
	            		 }
	            	 }
	        	}
	             if(adduct.contains("]+"))
	             {
	             psumfeature[p]=psumfeature[p]+MS1filtered.get(p).get(q).feature;
	            
	             
	              if(!adduct.contains("-Gly") && !adduct.contains("-Tau") && !adduct.contains("-SO3") && !adduct.contains("-GlcA") && !adduct.contains("-Hex"))
	              {
	            	  MHpsumfeature[p]=MHpsumfeature[p]+MS1filtered.get(p).get(q).feature;
		             
	              }
	             }
	             else
	            	 
	            	 {
	            	 nsumfeature[p]=nsumfeature[p]+MS1filtered.get(p).get(q).feature;
	            
	            	 if(adduct.equals("[M-H]-")||adduct.equals("[M-2H]2-"))
	            	 {
	            		 MHnsumfeature[p]=MHnsumfeature[p]+MS1filtered.get(p).get(q).feature;
		            	 
	            	 }
	            	 }
	       }
	        	//progress=(int)Math.ceil(p/(double)MS1filtered.size()*99);
	        	// message="step 6/6: "+String.format("analyzing MS2 %d%%.\n", progress);
	        	
	         }
	         
	         System.out.println("Finish MS2Analyzer");
	    	
			 System.out.println(dtf.format(LocalTime.now()));
         	 
	         //get confidence level
			 int[] confidence=new int[MS1filtered.size()];
			 for(int p=0;p<MS1filtered.size();p++)
			 {
				 if(psumlib[p]>0 && nsumlib[p]>0 )
				 {
					 confidence[p]=1;
				 }
				 else if(psumlib[p]>0 && (nsumfeature[p]>0 ))
				 {
					 confidence[p]=2;
				 }
				 //10/31/22 modified
				 //else if (nsumfeature[p]>0  && psumfeature[p]>0   && psumlibmodified[p]>0)
				 else if ((nsumfeature[p]>0 || nsumlib[p]>0)  && psumfeature[p]>0   && psumlibmodified[p]>0)
					
				 {	
					 confidence[p]=3;
				 }
				
				 //else if (((nsumfeature[p]>0 && nsumfeature[p]<999) && (psumfeature[p]>0 && psumfeature[p]<999)) || ( (nsumlib[p]>0 ||  nsumfeature[p]>0)  && (pcountion[MS1filtered_gr_order.get(p)]==0 || pickMS2pos[p]==0 )))
				 else if ((nsumfeature[p]>0 && nsumfeature[p]<999)  || nsumlib[p]>0 )
				 {
					 confidence[p]=4;
				 }
				 else
					 confidence[p]=0;
				 
			 }
			
			//distinguish between multiple annotation for same peak
			 int keepduplicate[]=new int[MS1filtered.size()];
			 String coelute[]=new String[MS1filtered.size()];
			 
			
			 for(int p=0;p<MS1filtered.size();p++)
			 {
				 if(confidence[p]>0)
				 {
				 for(int q=p+1;q<MS1filtered.size();q++)
				 {
					 if(confidence[q]>0)
					 {
					 for(int r=0;r<MS1filtered.get(p).size();r++)
					 {
						if(MS1filtered.get(p).get(r).peak.mode.equals("neg"))
						{
						 for(int s=0;s<MS1filtered.get(q).size() ;s++)
						 {
							 if(MS1filtered.get(q).get(s).peak.mode.equals("neg"))
		        {
		        	if(MS1filtered.get(p).get(r).peak.pcnum==MS1filtered.get(q).get(s).peak.pcnum)
		        	{
		        		if(MS1filtered.get(p).get(r).adduct.equals(MS1filtered.get(q).get(s).adduct))
		        		{
		    				//case 1: isomers such as 1OH_GBA-GlcA VS 1O1OH_GBA-Hex
						    //same adduct, different compound
							//keep the one with positive MS/MS lib hit;if both has, keep the one with higher confidence level, if both don't, remove both
		        			if(keepduplicate[p]>-1)
		        			{
		        			if(psumlib[p]>0 || (psumfeature[p]>0 && psumlibmodified[p]>0))
		        			{
		        				keepduplicate[p]=1;
		        			}
		        			else keepduplicate[p]=-1;
		        			}
		        			
		        			if(keepduplicate[q]>-1)
		        			{
		        			if(psumlib[q]>0 || (psumfeature[q]>0 && psumlibmodified[q]>0))
		        			{
		        				keepduplicate[q]=1;
                            }
		        			else keepduplicate[q]=-1;
		        			}
		        			
		        			if(keepduplicate[p]==1 && keepduplicate[q]==1)
		        			{
		        				if(confidence[p]>confidence[q])
		        				{
		        					keepduplicate[p]=-1;
		        					keepduplicate[q]=1;
		        				}
		        				else if(confidence[p]<confidence[q])
		        				{
		        					keepduplicate[p]=1;
		        					keepduplicate[q]=-1;
		        				}
		        				
		        			}
		        			
		        		}
		        		//case 2: add S or GlcA or Hex
		        		//different adduct, different compound
		        		//check the ratio of [M-H]- of TBA-S to [M-H]- of TBA, if the ratio is ok, keep TBA-S only, if not, keep both
		        		else
		        		{
		        			double[] MH_loss_ratio=new double[sample];
		        		
		        			int MH=0,MHa=0;
		        			int loss=0,lossa=0;
		        			
		        			if(MS1filtered.get(p).get(r).adduct.equals("[M-H]-"))
		        				{
		        				 MH=q;loss=p;
	        				     MHa=s;lossa=r;
		        				}
		        			else
		        				{   
		        				      MH=p;
	        				          loss=q;
	        				          MHa=r;
	        				          lossa=s;
		        				}
		        			int lMH=MS1filtered_gr_order.get(MH);
		        			int lloss=MS1filtered_gr_order.get(loss);
		        			int badratio=0;
		        			int oksample=0;
		        			coelute[MH]="";
		        			coelute[loss]="";
		        			
		        			
		        				for(int a=0;a<MS1filtered.get(MH).size();a++)
		        				{
		        					if(MS1filtered.get(MH).get(a).adduct.equals("[M-H]-") || MS1filtered.get(MH).get(a).adduct.equals("[M-2H]2-") )
		        					{
		        					for(int v=0;v<sample;v++)
		        					{
		        						if((double)(ncountionsample[lMH][v]+pcountionsample[lMH][v])/(ncountion[lMH]+pcountion[lMH])>=0.5)
		        						{
		        							
		        							if(MS1filtered.get(MH).get(a).peak.intensity[v]>0)
		        							{
		        								oksample++;
		        								MH_loss_ratio[v]=MS1filtered.get(loss).get(lossa).peak.intensity[v]/MS1filtered.get(MH).get(a).peak.intensity[v];
		        								if(MS1filtered.get(MH).get(MHa).compound.contains("-S") && MS1filtered.get(MH).get(MHa).adduct.equals("[M-H-SO3]-") )
		        								{
		        									if(MS1filtered.get(MH).get(a).compound.contains("GBA-S"))
		        									{
		        										if(MH_loss_ratio[v]>0.05)
		        											badratio++;
		        									}
		        									else if (MS1filtered.get(MH).get(a).compound.contains("TBA-S"))
		        									{
		        										if(MH_loss_ratio[v]>1)
		        											badratio++;
		        									}
		        									else
		        									{
		        										if(MS1filtered.get(MH).get(a).adduct.equals("[M-H]-"))
		        										if(MH_loss_ratio[v]>0.01)
		        											badratio++;
		        									}
		        								}
		        								if(MS1filtered.get(MH).get(MHa).compound.contains("-S") && MS1filtered.get(MH).get(MHa).adduct.equals("[M-H-H2O-SO3]-") )
		        								{
		        									if(MS1filtered.get(MH).get(a).compound.contains("GBA-S") || MS1filtered.get(MH).get(a).compound.contains("TBA-S"))
		        										{if(MH_loss_ratio[v]>0.05)
		        											badratio++;}
		        									else
		        									{
		        										if(MS1filtered.get(MH).get(a).adduct.equals("[M-H]-"))
			        										if(MH_loss_ratio[v]>0.05)
			        											badratio++;
		        									}
		        								}
		        								if(MS1filtered.get(MH).get(MHa).compound.contains("GlcA") && MS1filtered.get(MH).get(MHa).adduct.equals("[M-H-GlcA]-") )
		        								{
		        										if(MH_loss_ratio[v]>0.1)
		        											badratio++;
		        								}
		        							}
		        						}
		        					}
		        				
		        					if(oksample>0)
		        					{
		        					if(badratio/oksample<=0.5)
		        					{
		        						keepduplicate[loss]=-1;
		        					}
		        					else
		        					{
		        				        //keep both co-eluting groups
		        						//reassign confidence for MH 
		        						//only count for [M+H]+,[M+NH4]+,[M+H-nH2O]+ for pos mode
		        						//and [M-H]-,[M-2H]2- in neg mode since others may come from its coeluting compounds
		        						if(MHpsumlib[MH]>0 && MHnsumlib[MH]>0 )
		       						 {
		       							 confidence[MH]=1;
		       						 }
		       						 else if(MHpsumlib[MH]>0 && MHnsumfeature[MH]>0 )
		       						 {
		       							 confidence[MH]=2;
		       						 }
		       						 else if (MHnsumfeature[MH]>0  && MHpsumfeature[MH]>0  && MHpsumlibmodified[MH]>0)
		       						 {	
		       							 confidence[MH]=3;
		       						 }
		        				
		       						//else if (((MHnsumfeature[MH]>0 && MHnsumfeature[MH]<999) && (MHpsumfeature[MH]>0 && MHpsumfeature[MH]<999)) || ( (MHnsumlib[MH]>0 || MHnsumfeature[MH]>0) && (pcountion[MS1filtered_gr_order.get(p)]==0 || pickMS2pos[p]==0 )))
		        					else if ((MHnsumfeature[MH]>0 && MHnsumfeature[MH]<999) || MHnsumlib[MH]>0)
		        					{
		       							 confidence[MH]=4;
		       						 }
		       						 else
		       							 confidence[MH]=0;
		        						
		        					 if(confidence[MH]>0)
		        					 {
		        						 //add coeluting info
		        						 coelute[MH]=MS1filtered_gr_order.get(MH)+" "+MS1filtered_gr_order.get(loss);
			        					 coelute[loss]=MS1filtered_gr_order.get(MH)+" "+MS1filtered_gr_order.get(loss);
		        					 }
		        					
		        					}
		        					
		        					}
		        				}
		        				}
		        		}
		        	}
		        		}
		        		}
		        	}
		        }
					 }
				 }
		        }
			 }
			 
			
			 
			 System.out.println("Finish distinguish isomers");
			 System.out.println(dtf.format(LocalTime.now()));
			 
			//match std RT
			List<rtresult>[] rtmatch = new ArrayList[MS1filtered.size()];
			Arrays.setAll(rtmatch, element -> new ArrayList<>());
			 String[] mzRT=new String[MS1filtered.size()];
			 	
			 if(!(rt==null) & !(rt==""))
			 {
			 FileReader file2=new FileReader(rt);
			
			 Scanner scanner2 =new Scanner(file2);
			 int k=0;
			 	 
			 //Count the number of line in the input: k
				while(scanner2.hasNextLine())
					{
					   String temp2=scanner2.nextLine();
					   if(!temp2.isEmpty())
					   {
					   k++;
					   }
					}
				scanner2.close();
	
	            	
				//Store the species, RT and name from ref
				std[] rtlib=new std[k];
				
				
				Scanner scanner3 =new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream(rt), "UTF-8")));
				// BufferedReader file2= new BufferedReader(new InputStreamReader(new FileInputStream(rt), "UTF-8"));
				int n=0;
				while(scanner3.hasNextLine())
				{
				   String temp3=scanner3.nextLine().replace("\"", "");
				   if(!temp3.isEmpty())
				   {
					
					   String[] te=temp3.split("\t");
					   if(te.length!=3 && te.length!=5 )
					   {
						   JOptionPane.showMessageDialog(null,
				  	   			    "Incorrect format of RT library, please check the number of columns",
				  	   			    "Error Message",
				  	   			    JOptionPane.ERROR_MESSAGE);
						   throw new IllegalArgumentException();
					   }
					   else 
					   {
						try
						{
						if(te.length==3)
					   {
					   rtlib[n]=new std(te[0],Double.parseDouble(te[1]),te[2]);	
					
					   }
					   else if(te.length==5)
					   {
						   rtlib[n]=new std(te[0],Double.parseDouble(te[1]),te[2],Double.parseDouble(te[3]),Double.parseDouble(te[4]));		
						
					   }
					   }
						catch(NumberFormatException ne)
						{
							JOptionPane.showMessageDialog(null,
				  	   			    "Incorrect format of RT library, please check the keywords order",
				  	   			    "Error Message",
				  	   			    JOptionPane.ERROR_MESSAGE);
							  throw new IllegalArgumentException();
						}
					   }
				       n++;
				   }
				}
			scanner3.close();
			 
			
			
			 for(int a=0;a<MS1filtered.size();a++)
			 {
				 
				 for(int b=0;b<k;b++)
			 {
					 double rttolleft=0-rttol;
					 double rttolright=rttol;
				
				 if(MS1filtered.get(a).get(0).compound.equals(rtlib[b].species))
				 {
					 if(rtlib[b].customized)
					 {
					  rttolleft=rtlib[b].rtwinleft;
					  rttolright=rtlib[b].rtwinright;
					 }
					 
					//110822:even rounding up, 11.49-11.44->0.0500000000071 because double cannot store exact numbers, use BigDecimal instead
					    double rtdiff=sub(Math.round(groupnmaxionrt.get(a) * 100.0) / 100.0,rtlib[b].rt);
					
					 if(rtdiff>=rttolleft && rtdiff<=rttolright)
					 {
						 
						 rtmatch[a].add(new rtresult(rtlib[b].name,rtdiff));
						
					 }
					 
					
				 }
					 
			 }
				 
			 }
			 }
			 
			 
			
			 
			 //for feature matched multiple stds, sort from smallest rt error and report
			 for(int a=0;a<MS1filtered.size();a++)
			 {
				 if(rtmatch[a].size()==0)
				 {
					 mzRT[a]="";
				 }
				 else if (rtmatch[a].size()==1)
				 {
					 mzRT[a]=rtmatch[a].get(0).std;
				 }
				 else
				 { 
					 rtmatch[a].sort(Comparator.comparing(rtresult::getRterror));
					 mzRT[a]=rtmatch[a].get(0).std;
					 for(int i=1;i<rtmatch[a].size();i++)
						 mzRT[a]=mzRT[a]+ " or "+rtmatch[a].get(i).std;
				 }
			 }
			

			  //output grouping results: all
			 
		        g.write("groupnum,XCMSfeature,RT(min),m/z,compound,adduct,confidence,pass MS1 filters,MS2 feature,145/159(if>1),comment,PrecursorInMS2lib,MS2 lib hit, dot product score,hit same dot product>500,MS2 modified lib hit, modifit dot product, exclude top 5 dot product, modified same dot>500,MS2 count,selected MS2 source,");
			      for(int u=0;u<sample-1;u++)
			        {
			        	g.write(nsamplename[u].replace(".mzML","").replace("neg_","")+",");
			        }
			      g.write(nsamplename[sample-1]+"\n");
			      for(int p=0;p<gr.size();p++)
			    	  for (int q=0;q<gr.get(p).size();q++)	  
			        {
			    		  int s=gr_MS1filtered_order[p];
			    		  g.write(p+","+gr.get(p).get(q).peak.mode+"_"+gr.get(p).get(q).peak.pcnum+","+gr.get(p).get(q).peak.rt+","+gr.get(p).get(q).peak.mz+","+gr.get(p).get(q).compound+","+gr.get(p).get(q).adduct+",");
			    		  if(passMS1[p].equals("x") || passMS1[p].equals("duplicate") )
			    			  g.write(","+passMS1[p]+",,,,,,,,,,,,,,");
			    		  else
			    		  {
			    			 if((confidence[s]>0 && keepduplicate[s]>-1))
			    			 g.write("L"+confidence[s]+","+passMS1[p]+",");
			    			 else
			    				 g.write(","+passMS1[p]+",");
			    		 
			    		  
			    		 
			    		  
			    		  g.write(MS1filtered.get(s).get(q).feature+",");
			    		  if(MS1filtered.get(s).get(q).ratio>1)
			    		  {
			    		  //two decimals
			    		  g.write((double) Math.round(MS1filtered.get(s).get(q).ratio*100)/100+",");
			    		  }
			    		  else
			    			  g.write(",");  
			    		  
			    		  if(MS1filtered.get(s).get(q).comment==null && (coelute[s]==null || coelute[s].equals("")) )
			    		  {
			    			  g.write(",");
			    			  
			    		  }
			    		  else if (MS1filtered.get(s).get(q).comment!=null && (coelute[s]==null || coelute[s].equals("")))
			    			  g.write(MS1filtered.get(s).get(q).comment+",");
			    		  else if  (MS1filtered.get(s).get(q).comment==null && (coelute[s]!=null && !coelute[s].equals("")))
			    			  g.write("coelute "+coelute[s]+",");
			    		  else if  (MS1filtered.get(s).get(q).comment!=null && (coelute[s]!=null && !coelute[s].equals("")))
			    			  g.write(MS1filtered.get(s).get(q).comment+";coelute "+coelute[s]+",");
			    		  
			    		  
			    		  if(MS1filtered.get(s).get(q).selectedMS2==null)
			    			  g.write(",");
			    		  else
			    		  {
			    			  if(MS1filtered.get(s).get(q).selectedMS2.preinlib==1)
			    				  g.write("y,");
			    			  else
			    				  g.write(",");
			    		  }
			    		  
			    		  if(MS1filtered.get(s).get(q).selectedMS2==null || MS1filtered.get(s).get(q).selectedMS2.hit==null || MS1filtered.get(s).get(q).selectedMS2.dotproduct==0)
			    		  {
			    			  g.write(",,,");
			    			  
			    		  }
			    		  else 
			    			  g.write("\""+MS1filtered.get(s).get(q).selectedMS2.hit+" "+MS1filtered.get(s).get(q).selectedMS2.hitadduct+ "\""+","+(double) Math.round(MS1filtered.get(s).get(q).selectedMS2.dotproduct*100)/100+ ","+MS1filtered.get(s).get(q).MS2same500+",");
			    		  
			    		  if(MS1filtered.get(s).get(q).selectedMS2==null || MS1filtered.get(s).get(q).modifiedhit==null || MS1filtered.get(s).get(q).modifieddotproduct==0)
			    		  {
			    			  g.write(",,,,");
			    			  
			    		  }
			    		  else 
			    			  g.write("\""+MS1filtered.get(s).get(q).modifiedhit+" "+MS1filtered.get(s).get(q).modifiedhitadduct+ "\""+ ","+(double) Math.round(MS1filtered.get(s).get(q).modifieddotproduct*100)/100+ ","+(double) Math.round(MS1filtered.get(s).get(q).modifiedtopNdotproduct*100)/100+","+MS1filtered.get(s).get(q).modifiedMS2same500+",");
			    		  
			    		  if(MS1filtered.get(s).get(q).peak.MS2==null)
			    		  {
			    			  g.write("0,");
			    			  
			    		  }
			    		  else
			    			  g.write(MS1filtered.get(s).get(q).peak.MS2.size()+",");
			    		  
			    		  if(MS1filtered.get(s).get(q).selectedMS2==null)
			    		  {
			    			  g.write(",");
			    		  }
			    		  else
			    			  {
			    			  g.write(MS1filtered.get(s).get(q).selectedMS2.title.replace(","," ")+",");
			    			  
			    			  }
			    		  }
			    		  for (int r=0;r<sample-1;r++)
			    		  {
			    			  g.write(gr.get(p).get(q).peak.intensity[r]+",");
			    		  }
			    		  g.write(gr.get(p).get(q).peak.intensity[sample-1]+"\n");
			        }
			      
			        
			      g.close();
			      
			    //clean summary
				   f.write("groupnum,RT(min),annotation,confidence,std,neg MS2 hit(unique),fragment ratio,comments,neg_MS2_feature,pos_MS2_feature,neg_MS2_lib,pos_MS2_lib,pos_MS2_modified_lib,neg_adduct_sum,pos_adduct_sum,top_neg_adduct,");
				   for(int v=0;v<sample;v++)
			        {
			        	f.write(nsamplename[v].replace(".mzML","").replace("neg_","")+",");
			        }	
				   f.write("\n");
				   for(int p=0;p<MS1filtered.size();p++)
			        {
			        	int link=MS1filtered_gr_order.get(p);
			        	
			        	if(!mzRT[p].equals("") || (confidence[p]>0 && keepduplicate[p]>-1))
			        	{
			        	f.write(link+","+Math.round(groupnmaxionrt.get(p) * 100.0) / 100.0+",");

			        		f.write(MS1filtered.get(p).get(0).compound+",");
			        	
			            if(confidence[p]>0)
			            {
			        	f.write("L"+confidence[p]+",");
			            }
			            else
			            	f.write(",");
			            
			            f.write("\""+ mzRT[p]+"\""+",");
			        	
			        	//if(mzRT[p].equals(""))
			        	
			        	int MS2comment=0;
			        	if(MS1filtered.get(p).get(0).compound.equals("2OH_BA") || MS1filtered.get(p).get(0).compound.equals("1O1OH_BA") || MS1filtered.get(p).get(0).compound.equals("2O_BA") || MS1filtered.get(p).get(0).compound.equals("3OH_BA") || MS1filtered.get(p).get(0).compound.equals("1O2OH_BA") || MS1filtered.get(p).get(0).compound.equals("2O1OH_BA") || MS1filtered.get(p).get(0).compound.equals("3O_BA") || MS1filtered.get(p).get(0).compound.equals("4OH_BA"))
			        	{
			        		for(int q=0;q<MS1filtered.get(p).size();q++)
					        {
			        			if(!(MS1filtered.get(p).get(q).selectedMS2==null || MS1filtered.get(p).get(q).selectedMS2.hit==null || MS1filtered.get(p).get(q).selectedMS2.dotproduct==0))
			        				if(MS1filtered.get(p).get(q).selectedMS2.hitadduct.equals("[M-H]-"))
			        				  if(MS1filtered.get(p).get(q).selectedMS2.dotproduct>700)
			        				  
			        				{
			        					  if(MS1filtered.get(p).get(0).compound.equals("2OH_BA") || MS1filtered.get(p).get(0).compound.equals("1O1OH_BA") )
			        					  {
			        						  if(MS1filtered.get(p).get(q).selectedMS2.hit.contains("DCA") && !(MS1filtered.get(p).get(q).selectedMS2.hit.contains("CDCA")) && !(MS1filtered.get(p).get(q).selectedMS2.hit.contains("UDCA")) && !(MS1filtered.get(p).get(q).selectedMS2.hit.contains("MDCA")) && !(MS1filtered.get(p).get(q).selectedMS2.hit.contains("HDCA")))
			        						  {
			        					       f.write("\""+MS1filtered.get(p).get(q).selectedMS2.hit+"\""+",");
			        					       MS2comment++;
			        						  }
			        						  
			        					  }
			        					  else
			        					  {
			        						  f.write("\""+MS1filtered.get(p).get(q).selectedMS2.hit+"\""+",");
			        						  MS2comment++;
			        					  }
			        					  
			        					 
			        					  break;
			        				}
			        				  
			        				 
					        }
			        		
			        	}
			        	if(MS2comment==0)
			        	{
			        		f.write(",");
			        	}
			        	/*
			        	if(coelute[p]==null || coelute[p].equals("") )
			        		f.write(",");
			        	else
			        		f.write("coelute "+coelute[p]+" ");
			        		*/
			        	if(pratio[p]==1)
			        		f.write("145/159>1(355),");
			        	else
			        		f.write(",");
			        	
			        	int comment=0;
			        	for(int q=0;q<MS1filtered.get(p).size();q++)
				        {
			        		if(MS1filtered.get(p).get(q).comment!=null)
				    		  {
				    			 f.write(MS1filtered.get(p).get(q).comment+",");
				    			 comment++;
				    			 break;
				    		  }

				        }
			        	if(comment==0)
			        		f.write(",");
			        	
			        	
			        	if(pickMS2neg[p]==0)
			        	{
			        		f.write(",");
			        	}
			        	else
			        		f.write(nsumfeature[p]+",");
			        	
			        	if(pickMS2pos[p]==0)
			        	{
			        		f.write(",");
			        	}
			        	else
			        		f.write(psumfeature[p]+",");
			        	
			        	if(pickMS2neg[p]==0)
			        	{
			        		f.write(",");
			        	}
			        	else
			        		f.write(nsumlib[p]+",");
			        	if(pickMS2pos[p]==0)
			        	{
			        		f.write(",,");
			        	}
			        	else
			        		f.write(psumlib[p]+","+psumlibmodified[p]+",");
			        	f.write(ncountion[link]+","+ pcountion[link]+","+groupnmaxion.get(p)+",");
			        	for(int q=0;q<sample;q++)
				        {
			        		for (int z=0;z<MS1filtered.get(p).size();z++)	
			        		{
			        			if(MS1filtered.get(p).get(z).adduct.equals(groupnmaxion.get(p)))
			        			{
			        				f.write(MS1filtered.get(p).get(z).peak.intensity[q]+","); 
			        			}
			        		}
				        }
			        	f.write("\n");
			        	}
			        }
				   f.close();	
			
		   
		  
		   
	        System.out.println("END");
	        System.out.println(dtf.format(LocalTime.now()));
	        progress=100;
	        Desktop.getDesktop().open(new File(export));	        
	}

	@Override
	protected String doInBackground() throws Exception {
		// TODO Auto-generated method stub
		 
		  calculate(GI);
		  
		  return null;
			
	}
	
	 @Override
	    public void done() {
		 try{
			 //Thread.currentThread().sleep(1);
			
			  String meaningOfLife = get(); 
	        }
		 //// if SomeException was thrown by the background task, it's wrapped into the ExecutionException
		    catch(ExecutionException e){
		    	Throwable cause = e.getCause(); 
		    	 if (cause instanceof IOException) {
		    		    e.printStackTrace();
		 	           JOptionPane.showMessageDialog(null,
		 	   			    "Input/Output Exception: Please make sure you select the correct file/folder and the file is not used by another process.",
		 	   			    "Error Message",
		 	   			    JOptionPane.ERROR_MESSAGE);
		 	              Thread.currentThread().interrupt();
		 	    	       return;      
		            }
		    	 else if(cause instanceof IllegalArgumentException) { // the wrapped throwable is a runtime exception or an error
		    		    Thread.currentThread().interrupt();
	 	    	       return; 
		    		 // TODO handle any other exception as you want to
		            }
	        
	        }
		 catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
				
			  return; 
	            // TODO handle the case where the background task was interrupted as you want to
	        }
		 catch (java.util.concurrent.CancellationException  ce) {
			Thread.currentThread().interrupt();
			return; 
	          
	        }
	    
		 
	 }

}


