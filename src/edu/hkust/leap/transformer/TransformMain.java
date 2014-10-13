package edu.hkust.leap.transformer;

import edu.hkust.leap.syncMethodDesugar.DesugarSyncMethodTransformer;
import edu.hkust.leap.transformer.AddInterestEvent.AddInterestEventTransformer;
import edu.hkust.leap.transformer.SharedWriteRec.SharedWriteRecTransformer;
import edu.hkust.leap.transformer.SharedWriteRec.SharedWriteRecVisitor;
import edu.hkust.leap.utils.Parameters;
import edu.hkust.leap.utils.Util; //import edu.hkust.leap.transformer.SharedWriteRec.TransformerForInstrumentation;
import soot.*;
import soot.jimple.*;
import soot.jimple.spark.SparkTransformer;
import soot.options.Options;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

public class TransformMain {

	
	    
//	public static final String excludeInclude =  "-i org.apache.derby. -x java. -x javax -x sun -x com -x jrockit"
//				+ " -x edu -x checkers -x org.xmlpull -x org.apache.xml"
//				+ " -x org.apache.xpath -x soot -x org.jgrapht";
	
	public static String dacapoBenchmarkName ;
	public static void main(String[] args) throws Exception{

		// Visitor.setObserverClass("edu.hkust.leap.monitor.Monitor");
		TransformMain processor = new TransformMain();

		if(!edu.hkust.leap.transformer.AnalysisOptions.dacapoBench)
		{
			System.out.println("loading the general arguments from leap.transformer.arg:");
			String arglineFileName = Util.getTransformerArgFile();
			String argline =Util.getArgLine(arglineFileName);
			String[] argline_items = argline.split(" ");
			outputFormat= argline_items[0];
			cpath= argline_items[1];
			mainClass = argline_items[2];	
			Parameters.isRecord = true;
			Parameters.isReplay = false;
			path = Util.getTmpDirectory();// .replace("\\", "\\\\")

			String argsOfmtrt = "-f "
					+ outputFormat
					+ " -cp "
					+System.getProperty("sun.boot.class.path") + ":" + System.getProperty("java.class.path") + ":" +cpath
					+ " -main-class "
					+ mainClass
					+ " "
					+ mainClass
					+ " -d "
					+ path ;
			
			System.out.println("output record version to: " + path);
			processor.transformRecordVersion(argsOfmtrt); //

		}else {
			//-keep-line-number -app -w -p cg.spark enabled -f c -p cg reflection-log:/home/lpxz/work/dacapo/out/avrora-small/refl.log -cp /home/lpxz/pool/jdk1.6.0_26/jre/lib/resources.jar:/home/lpxz/pool/jdk1.6.0_26/jre/lib/rt.jar:/home/lpxz/pool/jdk1.6.0_26/jre/lib/sunrsasign.jar:/home/lpxz/pool/jdk1.6.0_26/jre/lib/jsse.jar:/home/lpxz/pool/jdk1.6.0_26/jre/lib/jce.jar:/home/lpxz/pool/jdk1.6.0_26/jre/lib/charsets.jar:/home/lpxz/pool/jdk1.6.0_26/jre/lib/modules/jdk.boot.jar:/home/lpxz/pool/jdk1.6.0_26/jre/classes:/home/lpxz/eclipse/workspacePA_icse/predict-inst/bin:/home/lpxz/eclipse/workspacePA_icse/predict-engine/bin:/home/lpxz/eclipse/workspacePA_icse/predict-engine/lib/commons-cli-1.2.jar:/home/lpxz/eclipse/workspacePA_icse/predict-engine/lib/ant.jar:/home/lpxz/eclipse/workspacePA_icse/predict-engine/lib/jigsaw-sexpr.jar:/home/lpxz/eclipse/workspacePA_icse/predict-engine/lib/h2-1.3.171.jar:/home/lpxz/eclipse/workspacePA_icse/predict-engine/lib/xercesImpl.jar:/home/lpxz/eclipse/workspacePA_icse/predict-engine/lib/ant-launcher.jar:/home/lpxz/eclipse/workspacePA_icse/predict-inst/lib/soot-jeff.jar:/home/lpxz/work/dacapo/out/avrora-small -main-class Harness Harness -i org.apache -i org.w3c

			dacapoBenchmarkName = args[0];
			
			outputFormat= "c";
			path = Util.getTmpDirectory();// .replace("\\", "\\\\")
			cpath="/home/lpxz/work/dacapo/out/"+dacapoBenchmarkName+"-small";// TODO change folder directory.
			mainClass = "Harness";
	    	String excludeOption = "";
			String includeOption = " -i org.apache -i org.w3c";    		
			String reflString = " -p cg reflection-log:"+cpath+"/refl.log";    		
			String jceJar = "/home/lpxz/java_standard/jre/lib/jce.jar";
			String rtJar = "/home/lpxz/java_standard/jre/lib/rt.jar";
			String interString = "-keep-line-number -app -w -p cg.spark enabled -f " + outputFormat	//-allow-phantom-refs -no-bodies-for-excluded 
				    + reflString
					+ " -cp "
					+ System.getProperty("sun.boot.class.path") + ":" + System.getProperty("java.class.path") + ":" +cpath// powerful classpath.
//					+" -d " + Main.getTempSubDirectory(config.DIR_RECORD)
					+ " -main-class "
					+ mainClass
					+ " "
					+ mainClass
					+excludeOption
					+includeOption
					+ " -d "
					+ path ;
			
			
			
			
			
			
			Object[] finalArgs = interString.split(" ");		
			System.out.println(interString);
			Parameters.isRecord = true;
			Parameters.isReplay = false;
			
			System.out.println("output record version to: " + path);
			processor.transformRecordVersion(interString); //
		}


		
//		Parameters.isRecord = false;       // no need to do this,  too many problems.
//		Parameters.isReplay = true;
//		path = Util.getTmpDirectory();// .replace("\\", "\\\\")
//		System.out.println("output record version to: " + path);
//
//		Visitor.resetParameter();
//		transformReplayVersion();

		System.out.print("*** *** *** *** *** *** *** *** *** *** \n");
		System.out.print("\n*** Total access number: " + Visitor.totalaccessnum);
		System.out.print("\n*** SPE access number: " + Visitor.sharedaccessnum);
		System.out.print("\n*** Instrumented SPE access number: "
				+ Visitor.instrusharedaccessnum);
		System.out.print("\n*** SPE size: " + Visitor.speIndexMap.size());
        
		String string = "# SPENO  mainclass args (to added manually)\r\n" +
		              ""+Visitor.speIndexMap.size()  + " " + mainClass;
		String recordargfileName = Util.getRecordArgFile();
		Util.writeArgLine(recordargfileName, string);
		
		Iterator speSetIt = Visitor.speIndexMap.keySet().iterator();
		System.out.print("\n ");
		System.out.print("\n*** *** *** *** *** *** *** *** *** *** ");
		System.out.print("\n*** SPE name: ");
		while (speSetIt.hasNext()) {
			System.out.print("\n*** " + speSetIt.next());
		}
		
	
	}

	private FileWriter printer;
	public static String cpath;
	public static String path;
	public static String outputFormat;
	public static String mainClass;

	public void print(String str) {
		System.err.println(str);
		try {
			printer.write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	
	private void transformRecordVersion(String interString) throws Exception{
		String[] finalArgs = interString.split(" ");
		
		soot.Main mainObject =soot.Main.v();
		Method[] theMethods = mainObject.getClass().getDeclaredMethods();
		Method theMethod =null; 
		for(Method tmp : theMethods)
		{
			if(tmp.getName().equals("processCmdLine"))
			{
				theMethod = tmp;
			}
		}
		theMethod.setAccessible(true);
		theMethod.invoke(mainObject, new Object[]{finalArgs}); // soot.Main.v().processCmdLine(finalArgs);
		
		
		setWPOptions();
		Visitor.setObserverClass(Parameters.RECORDCLASS);
		Scene.v().loadClassAndSupport(Visitor.observerClass);// must be in front
																// of
																// loadNecessary..
		Scene.v().loadNecessaryClasses();
		
		
		
        EntryPoints4Dacapo.setEntryPoints(dacapoBenchmarkName);
		 
		
		
		
		Pack wjtp = PackManager.v().getPack("wjtp");
		
		if(AnalysisOptions.sharedPhase){
		       wjtp.add(new Transform("wjtp.SharedWriteRec", new SharedWriteRecTransformer()));	       
		}
		
		Pack jtp = PackManager.v().getPack("jtp");		
		if(AnalysisOptions.injectPhase){
			if(dacapoBenchmarkName!=null){
				List<String> result = ReadWriteFile.readLineByLine(new File("./shared/" + dacapoBenchmarkName));// pre-runpack checking
				SharedWriteRecVisitor.sharedVariableWriteAccessSet.addAll(result);
				if(result.size()==0 && !AnalysisOptions.sharedPhase)
				{
					throw new RuntimeException("should have the shared analysis phase or results of shared analysis");
				}
			}
			
			
			jtp.add(new Transform("jtp.desugarSyncMethod", 	new DesugarSyncMethodTransformer()));// sync Method cannot be monitored, change it to sync block.
			jtp.add(new Transform("jtp.Recorder",
							new AddInterestEventTransformer()));
		}

		PackManager.v().runPacks();// 1
//		PackManager.v().writeOutputExcept(excludeList);
		
		if(AnalysisOptions.sharedPhase)
		{
			System.out.println("dumping shared information");
	       List<String> result = new ArrayList<String>();
	       result.addAll(SharedWriteRecVisitor.sharedVariableWriteAccessSet);
	       ReadWriteFile.write2File(result, "./shared/" + dacapoBenchmarkName);
		}
		
		G.reset();

		System.err.println("***** Runtime version generated *****");
	}

//	private void transformReplayVersion() {
//
//		String argsOfmtrt = "-f "
//				+ outputFormat
//				+ " -pp -cp "
//				+ cpath
//				+ " -main-class "
//				+ mainClass
//				+ " "
//				+ mainClass
//				+ " -d "
//				+ path;
//
//		String interString = argsOfmtrt;
//		String[] finalArgs = interString.split(" ");
//		soot.Main.v().processCmdLine(finalArgs);
//
//		Options.v().set_app(true);
//
//		Visitor.setObserverClass(Parameters.REPLAYCLASS);
//		Scene.v().loadClassAndSupport(Visitor.observerClass);
//		Scene.v().loadNecessaryClasses();
//
//		Pack jtp = PackManager.v().getPack("jtp");
//		jtp
//				.add(new Transform("jtp.Replayer",
//						new AddInterestEventTransformer()));
//
//		PackManager.v().runPacks();// 1
//		PackManager.v().writeOutput();
//		G.reset();
//
//		System.err.println("--- Replay version generated ---");
//	}
	
	  public  static LinkedList<String> excludeList = new LinkedList<String> ();
	  public  static LinkedList<String> includeList = new LinkedList<String> ();

	    static
	    {
	    // the following packages are excluded in Soot by default
	    //	java., sun., javax., com.sun., com.ibm., org.xml., org.w3c., apple.awt., com.apple.
	    
	    //IMPORTANT: do not forget  to use writeOutputExcept() to make sure you do not dump the excluded classes. 	
	    excludeList.add("edu.hkust.");
	    excludeList.add("lptools.");
	    
	    excludeList.add("java.");
	    excludeList.add("javax.");
	    excludeList.add("sun.");
	    excludeList.add("sunw.");
	    excludeList.add("com.sun.");
	    excludeList.add("com.ibm.");
	    excludeList.add("com.apple.");
	    excludeList.add("apple.awt.");
//	    excludeList.add("org.xml.");
	    excludeList.add("org.h2.");
	    excludeList.add("jdbm.");
	    excludeList.add("aj.");
	    
	  

	    // avrora:
	    // it conflicts with the use of System.out.println().
	    // not sure what is going on.
	    // guess that it changes the system.out online to some other printstream and enforces the lock protection.
	    // currently, I insert a simple call which dumps the methods to a file, rather than directly using system.out.
	    
	    excludeList.add("org.dacapo.harness.");
	    excludeList.add("org.dacapo.parser.");
	    excludeList.add("org.apache.crimson.");
	    excludeList.add("org.apache.batik.dom.util.");// cool! after banning it, we get batik running now.
	    excludeList.add("org.eclipse.equinox");
	    excludeList.add("org.apache.xmlgraphics.util.Service");
	    excludeList.add("org.apache.derbyTesting.system.oe.");// testing utility


	    // helping type checker to avoid these:
	    PhantomSupport.addPhantom("org.python.core");// phantom means we do not even load the class bodies, avoid type checker errors :)
	    // exclude means we load the classes but do not transform them. 
	    // originally, phantom corresponds to only those loading failures.
	    PhantomSupport.addPhantom("$Proxy$HASHED");


	    
	    
      // great, we handle all dacapo benchmarks now!	    
	    
	    
//	    includeList.add("org.w3c.");//for jigsaw
//	    includeList.add("avrora.");//for avrora
	    
	    includeList.add("org.apache.lucene.");//for lucene
	    includeList.add("org.apache.xalan.");//for lucene
	    includeList.add("org.apache.xml.");//for lucene
	    includeList.add("org.apache.xerces.");//for lucene
	    
	    }
	    

	    public static void setWPOptions() {
			
			soot.Main.v().autoSetOptions();

			  PhaseOptions.v().setPhaseOption("cg.spark", "enabled:true");
		      PhaseOptions.v().setPhaseOption("cg.spark", "propagator:worklist");
		      PhaseOptions.v().setPhaseOption("cg.spark", "simple-edges-bidirectional:false");
		      PhaseOptions.v().setPhaseOption("cg.spark", "on-fly-cg:true");
		      PhaseOptions.v().setPhaseOption("cg.spark", "set-impl:double");
		      PhaseOptions.v().setPhaseOption("cg.spark", "double-set-old:hybrid");
		      PhaseOptions.v().setPhaseOption("cg.spark", "double-set-new:hybrid");
		     
		      

//			 Map map = PhaseOptions.v().getPhaseOptions("cg.spark");
//			 
//			 for(Object ke : map.keySet())
//			 {
//				 System.out.println("key:value " + ke + " "  + map.get(ke));
//				 
//			 }
			 
			 
			 
			 

		      
			
			Options.v().set_exclude(excludeList);
			Options.v().set_include(includeList); 
			
			PhaseOptions.v().setPhaseOption("jb", "enabled:true");
			Options.v().set_keep_line_number(true);
			Options.v().setPhaseOption("jb", "use-original-names:true");
			Options.v().set_whole_program(true);
			Options.v().set_app(true);

		
		}


}
