/**
 * 
 */
package edu.hkust.leap.transformer;

import java.util.List;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

/**
 * @author Peng Liu from Purdue
 *
 * <lpxz.ust.hk@gmail.com>
 */
public class EntryPoints4Dacapo {

	public static String[] dacapoBenchmarks = {"avrora", "batik", "eclipse", "fop", "h2", "jython",
		                                         "luindex", "lusearch", "pmd",  "sunflow",  "tomcat", "xalan"};
	public static boolean containsDacapo(String subjectname)
	{
		for(String str : dacapoBenchmarks)
		{
			if(subjectname.equals(str))
			{
				return true;
			}
		}
		return false;
	}
	public static void setEntryPoints(String dacapoBenchmarkName) // hardcoded version
	{

//		avrora.Main                             main
//		org.apache.batik.apps.rasterizer.Main   execute  
//		org.eclipse.core.runtime.adaptor.EclipseStarter      run
//		org.apache.fop.cli.Main                         startFOP
//		org.dacapo.h2.TPCC                              iteration
//		org.python.util.jython                       main
//		org.dacapo.luindex.Index                         main
//		org.dacapo.lusearch.Search                       main
//		net.sourceforge.pmd.PMD                          main
//		org.sunflow.Benchmark                           kernelMain
//		org.apache.catalina.startup.Bootstrap         main
//		# if the above fails, try this: org.dacapo.tomcat.Control                     exec
//		org.dacapo.xalan.XSLTBench        createWorkers # create
//		org.dacapo.xalan.XSLTBench        doWork # use threads		
		if(dacapoBenchmarkName!=null&& containsDacapo(dacapoBenchmarkName))
		{
			if(dacapoBenchmarkName.equals("avrora"))
			{
				 List entryPoints = null;
				 entryPoints=Scene.v().getEntryPoints();
				 SootClass c =Scene.v().getSootClass("avrora.Main");
				 SootMethod method = c.getMethodByName("main");		
				 entryPoints.add(method);
				 Scene.v().setEntryPoints(entryPoints);
			}else if (dacapoBenchmarkName.equals("batik")) {
				 List entryPoints = null;
				 entryPoints=Scene.v().getEntryPoints();
				 SootClass c =Scene.v().getSootClass("org.apache.batik.apps.rasterizer.Main");
				 SootMethod method = c.getMethodByName("execute");		
				 entryPoints.add(method);
				 Scene.v().setEntryPoints(entryPoints);				
			}else if (dacapoBenchmarkName.equals("eclipse")) {
				 List entryPoints = null;
				 entryPoints=Scene.v().getEntryPoints();
				 SootClass c =Scene.v().getSootClass("org.eclipse.core.runtime.adaptor.EclipseStarter");
				 
//				 for(SootMethod sm: c.getMethods())
//				 {
//					 if(sm.getName().equals("run"))
//					 {
//						 System.out.println(sm.getSignature());
//						 System.out.println(sm.getSubSignature());
//					 }
//				 }				 
				 
				 SootMethod method = c.getMethod("java.lang.Object run(java.lang.Object)");		
				 entryPoints.add(method);
				 Scene.v().setEntryPoints(entryPoints);					
			}else if (dacapoBenchmarkName.equals("fop")) {
				 List entryPoints = null;
				 entryPoints=Scene.v().getEntryPoints();
				 SootClass c =Scene.v().getSootClass("org.apache.fop.cli.Main");
				 SootMethod method = c.getMethodByName("startFOP");		
				 entryPoints.add(method);
				 Scene.v().setEntryPoints(entryPoints);					
			}else if (dacapoBenchmarkName.equals("h2")) {//possibly missing the thread creation
				List entryPoints = null;
				 entryPoints=Scene.v().getEntryPoints();
				 SootClass c =Scene.v().getSootClass("org.dacapo.h2.TPCC");
				 SootMethod method = c.getMethodByName("iteration");		
				 entryPoints.add(method);
				 Scene.v().setEntryPoints(entryPoints);		
			}else if (dacapoBenchmarkName.equals("jython")) {
				List entryPoints = null;
				 entryPoints=Scene.v().getEntryPoints();
				 SootClass c =Scene.v().getSootClass("org.python.util.jython");
				 SootMethod method = c.getMethodByName("main");		
				 entryPoints.add(method);
				 Scene.v().setEntryPoints(entryPoints);	
			}else if (dacapoBenchmarkName.equals("luindex")) {
				List entryPoints = null;
				 entryPoints=Scene.v().getEntryPoints();
				 SootClass c =Scene.v().getSootClass("org.dacapo.luindex.Index");
				 SootMethod method = c.getMethodByName("main");		
				 entryPoints.add(method);
				 Scene.v().setEntryPoints(entryPoints);	
				
			}else if (dacapoBenchmarkName.equals("lusearch")) {
				
				List entryPoints = null;
				 entryPoints=Scene.v().getEntryPoints();
				 SootClass c =Scene.v().getSootClass("org.dacapo.lusearch.Search");
				 SootMethod method = c.getMethodByName("main");		
				 entryPoints.add(method);
				 Scene.v().setEntryPoints(entryPoints);	
				
			}else if (dacapoBenchmarkName.equals("pmd")) {
				List entryPoints = null;
				 entryPoints=Scene.v().getEntryPoints();
				 SootClass c =Scene.v().getSootClass("net.sourceforge.pmd.PMD");
				 SootMethod method = c.getMethodByName("main");		
				 entryPoints.add(method);
				 Scene.v().setEntryPoints(entryPoints);	
			}else if (dacapoBenchmarkName.equals("sunflow")) {
				List entryPoints = null;
				 entryPoints=Scene.v().getEntryPoints();
				 SootClass c =Scene.v().getSootClass("org.sunflow.Benchmark");
				 SootMethod method = c.getMethodByName("kernelMain");		
				 entryPoints.add(method);
				 Scene.v().setEntryPoints(entryPoints);	
			}else if (dacapoBenchmarkName.equals("tomcat")) {
				List entryPoints = null;
				 entryPoints=Scene.v().getEntryPoints();
				 SootClass c =Scene.v().getSootClass("org.apache.catalina.startup.Bootstrap");
				 SootMethod method = c.getMethodByName("main");		
				 entryPoints.add(method);
				 
				  c =Scene.v().getSootClass("org.dacapo.tomcat.Control");                     
				  method = c.getMethodByName("exec");		
				 entryPoints.add(method);
				 
				 Scene.v().setEntryPoints(entryPoints);	
			}
			else if (dacapoBenchmarkName.equals("xalan")) {
				List entryPoints = null;
				 entryPoints=Scene.v().getEntryPoints();
				 SootClass c =Scene.v().getSootClass("org.dacapo.xalan.XSLTBench");
				 SootMethod method = c.getMethodByName("createWorkers");		
				 entryPoints.add(method);
				 
				  c =Scene.v().getSootClass("org.dacapo.xalan.XSLTBench");
				  method = c.getMethodByName("doWork");		
				 entryPoints.add(method);
				 
				 Scene.v().setEntryPoints(entryPoints);	
			}
			
			 
			 
			 
			 
			 
			 
			 
		}
	
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
