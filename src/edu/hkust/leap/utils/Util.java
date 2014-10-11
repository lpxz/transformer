package edu.hkust.leap.utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

import edu.hkust.leap.transformer.TransformMain;

import soot.RefType;
import soot.SootClass;
import soot.Type;
import soot.Value;
import soot.coffi.element_value;
import soot.jimple.ArrayRef;


public class Util{

	static String[] unInstruClasses = {
		"jrockit.",
			"java.",
			"javax.",
			"xjava.",
			"COM.",
			"com.",
			"cryptix.",
			"sun.",
			"sunw.",
			"junit.",
			"org.junit.",
			"org.xmlpull.",
			"edu.hkust.leap."
	};
	
	static String[] specialAllowedunInstruClasses = {
		"java.util.Vector",

	};



    public static String getSig4arrayRef(ArrayRef arrayRef)
    {
    	Value base = arrayRef.getBase();
    	
		String sig = base.getType().toString() + "."+ Parameters.arrayIndex; 
		return sig;
    	
    }
    
    public static String writeArgLine(String argfileName , String towrite)
    {
    	String toret  = "";
		try{
		    // Open the file that is the first 
		    // command line parameter
			FileOutputStream fstream = new FileOutputStream(argfileName);
		    // Get the object of DataInputStream
			DataOutputStream in = new DataOutputStream(fstream);
			BufferedWriter br = new BufferedWriter(new OutputStreamWriter(in));
            
            br.write(towrite);
			br.flush();
			
		    //Close the input stream
		    in.close();
		    }catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
		    }
		    return toret;
    	
    }
    
    

    public static String getArgLine(String argfileName )
    {
    	String toret  = "";
		try{
		    // Open the file that is the first 
		    // command line parameter
		    FileInputStream fstream = new FileInputStream(argfileName);
		    // Get the object of DataInputStream
		    DataInputStream in = new DataInputStream(fstream);
		        BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		    //Read File Line By Line
		    while ((strLine = br.readLine()) != null)   {
		    	if(strLine.startsWith("#"))
		    	{continue;}
		    	else
		    	{
		    		toret= strLine;
		    	}
		     // System.out.println (strLine);
		    }
		    //Close the input stream
		    in.close();
		    }catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
		    }
		    return toret;
    	
    }
	public static String getTmpDirectory() 
	{
		String tempdir = System.getProperty("user.dir");
		if (!(tempdir.endsWith("/") || tempdir.endsWith("\\"))) {
			tempdir = tempdir + System.getProperty("file.separator");
		}
		tempdir = tempdir+"tmp"+System.getProperty("file.separator");
		
		if(TransformMain.outputFormat.equals("J"))
			tempdir = tempdir+Parameters.OUTPUT_JIMPLE+System.getProperty("file.separator");
		else {
			
		}
		
		if(Parameters.isRecord)
			tempdir = tempdir+Parameters.PHASE_RECORD;
		else
			tempdir = tempdir+Parameters.PHASE_REPLAY;
		
		File tempFile = new File(tempdir);
		if(!(tempFile.exists()))
			tempFile.mkdir();
			
		tempdir = tempdir+System.getProperty("file.separator");
		return tempdir;
	}
	
    public static boolean isRunnableSubType(SootClass c) {
        if (c.implementsInterface("java.lang.Runnable"))
            return true;
        if (c.hasSuperclass())
            return isRunnableSubType(c.getSuperclass());
        return false;
    }
    public static boolean shouldInstruThisClass(String scname)
    {
		for(int k=0;k<specialAllowedunInstruClasses.length;k++)// early return, this section should be in front of the follwoing judgement
		{
			if(scname.startsWith(specialAllowedunInstruClasses[k]))
			{
				return true;
			}
		}
		
		for(int k=0;k<unInstruClasses.length;k++)
		{
			if(scname.startsWith(unInstruClasses[k]))
			{
				return false;
			}
		}
		
		return true;
	}
    public static boolean shouldInstruThisMethod(String smname)
	{    	   	
		if (smname.contains("<clinit>")
			|| smname.contains("<init>"))
    	{
    		return false;
    	}
   		
		return true;
	}
    public static void resetParameters()
    {
  		 Parameters.isMethodRunnable = false;
  		 Parameters.isMethodMain = false;
  		 Parameters.isMethodSynchronized = false;
    }

//=====================================================================

	public static String getTransformerArgFile() 
	{
		String tempdir = System.getProperty("user.dir");
	
		tempdir = tempdir+System.getProperty("file.separator");
		tempdir+= "leap.transformer.arg";
		return tempdir;
	}
	
    
	public static String getRecordArgFile() 
	{
		String tempdir = System.getProperty("user.dir");
		tempdir=tempdir.replace("transformer", "recorder");			
		tempdir = tempdir+System.getProperty("file.separator");
		tempdir+= "leap.recorder.arg";
		return tempdir;
	}







	public static String getOrderDataDirectory() 
	{
		String tempdir = System.getProperty("user.dir");
		if (!(tempdir.endsWith("/") || tempdir.endsWith("\\"))) {
			tempdir = tempdir + System.getProperty("file.separator");
		}
		
		File tempFile = new File(tempdir);
		if(!(tempFile.exists()))
			tempFile.mkdir();
			
		tempdir = tempdir+System.getProperty("file.separator")+
	    Parameters.OrderDataDir+System.getProperty("file.separator");
		
		return tempdir;
	}

	public static String getIntermediateCP() {
		

		String tempdir = System.getProperty("user.dir");
		tempdir = tempdir+System.getProperty("file.separator")+ 
		"tmp"+System.getProperty("file.separator") +
	    Parameters.IntermediateCP+System.getProperty("file.separator");
		
		return tempdir;
	
	}

	public static String getSig4JDKDataStructureFakedField(Value base) {    	
		String sig = base.getType().toString() + "["+ Parameters.fakedJDKDataStructureField+"]"; 
		return sig;
	}

	
	//=========================================================
    private static Properties props;

    public static void loadProperties() {
         props = new Properties();
         String tempdir = System.getProperty("user.dir");
     	
 		tempdir = tempdir+System.getProperty("file.separator");
 		tempdir+= "leap.property";
         try {
              props.load(new FileInputStream(tempdir));
         } catch (Exception e) {
              e.printStackTrace();
         }
    }
    public static String getConfig(String key) {
        return props.getProperty(key);
    }

    public static void main(String[] args)
    {
         loadProperties();
         System.out.println(getConfig("vector"));
    }

}
