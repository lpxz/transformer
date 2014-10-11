package edu.hkust.leap.transformer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;



import pldi.locking.MethodLocker;

import soot.Body;
import soot.BodyTransformer;
import soot.FastHierarchy;
import soot.Modifier;
import soot.Pack;
import soot.PackManager;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.internal.JRetStmt;
import soot.jimple.internal.JReturnStmt;
import soot.jimple.internal.JReturnVoidStmt;
import edu.hkust.leap.transformer.AddInterestEvent.AddInterestEventTransformer;
import edu.hkust.leap.transformer.SharedWriteRec.SharedWriteRecTransformer;
import edu.hkust.leap.utils.Parameters;
import edu.hkust.leap.utils.Util;

public class TransformPreprocess {

	/**
	 * @param args
	 */
	public static HashSet<String> usedJDKOps = new HashSet<String>();
	public static HashSet<String> randomOps = new HashSet<String>();
	public static void main(String[] args) {
		System.out.println("loading the general arguments from leap.transformer.arg:");
		String arglineFileName = Util.getTransformerArgFile();
		String argline =Util.getArgLine(arglineFileName);
		String[] argline_items = argline.split(" ");
		String outputFormat= argline_items[0];
		String cpath= argline_items[1];
		String mainClass = argline_items[2];	
		
		String intermediateCP = Util.getIntermediateCP();
		

		String argsOfmtrt = "-f "
				+ "c"         // force to generate the class files
				+ " -pp -cp "
				+ cpath
				+ " -main-class "
				+ mainClass
				+ " "
				+ mainClass
				+ " -d "
				+ intermediateCP
				+" " + TransformMain.excludeInclude;

		String interString = argsOfmtrt;
		String[] finalArgs = interString.split(" ");
		soot.Main.v().processCmdLine(finalArgs);
		setWPOptions();
		Visitor.setObserverClass(Parameters.RECORDCLASS);
		Scene.v().loadClassAndSupport(Visitor.observerClass);// must be in front
																// of
																// loadNecessary..
		Scene.v().loadNecessaryClasses();
		Pack jtp = PackManager.v().getPack("jtp");
		jtp.add(new Transform("jtp.SM2SB",
				new BodyTransformer() {
					
					@Override
					protected void internalTransform(Body b, String phaseName, Map options) {
						  SootMethod sm = b.getMethod();
						if(!Util.shouldInstruThisMethod(sm.getName()))
							return;
						Scene.v().getApplicationClasses();
						SootClass thisClass = sm.getDeclaringClass();
						String scname = thisClass.getName();					
						if(!Util.shouldInstruThisClass(scname)) 
							return;
						
				
					  if(sm.isSynchronized())
					  {
						  
						  {
							  sm.setModifiers(sm.getModifiers() & ~Modifier.SYNCHRONIZED);
							  if(sm.hasActiveBody())
							  {
								 Body body = sm.getActiveBody();
								 PatchingChain<Unit> units = body.getUnits();
								 Stmt firstNon =body.getFirstNonIdentityStmt();
								 if(firstNon instanceof JReturnStmt || firstNon instanceof JReturnVoidStmt)
								 {// the empty body incurs problems during the lock injection.
									 Stmt nop=Jimple.v().newNopStmt();
									 units.insertBefore(nop, firstNon);									 
								 }								 
							  }							  
							  MethodLocker.addlock(sm);
						  }
						
					  }
					  if(sm.hasActiveBody())
					  {
						  Body body =sm.getActiveBody();
						 Iterator<Unit> it = body.getUnits().iterator();
					     while (it.hasNext()) {
							Unit unit = (Unit) it.next();
							Stmt stmt = (Stmt ) unit;
							if(stmt.containsInvokeExpr())
							{
								InvokeExpr ie =stmt.getInvokeExpr();
							    if(ie.getMethod().getDeclaringClass().getName().equals("java.util.Random")
							    		&&ie.getMethod().getName().startsWith("next"))
							    {
							    	randomOps.add(sm.getDeclaringClass().getName() + " "+ sm.getName());
							    }
							    SootClass sc = ie.getMethod().getDeclaringClass();
							    SootClass colClass = Scene.v().loadClassAndSupport("java.util.Collection");
							    FastHierarchy fh = Scene.v().getFastHierarchy();
							    boolean  assignable = fh.canStoreType(sc.getType(), colClass.getType());
							    if(assignable)
							    {
							    	usedJDKOps.add("insert2(READOrWRITE," + "\""+ sc.getName() + "\""+
							    			"," + "\""+ ie.getMethod().getName() + "\""+");");
							    }
							}
							
						}
					  }
						
					}
				}));
		

		PackManager.v().runPacks();// 1
		PackManager.v().writeOutput();
		System.err.println("CONTAINS RANDOM INPUT, SUBSTITUTE MANUALLY IT TO FIXED VALUE FOR EVAL (no need anymore. I fixed the seed, heihei):");
		for(String ritem  :randomOps)
		{
			System.err.println(ritem);
		}
		  
		System.err.println("===================================");
		System.err.println("used JDK classes, add them   to the class: edu.hkust.leap.utils.Parameters!");
		for(String item : usedJDKOps)
		{
			System.err.println(item);
		}
		
		
		String string = "# SPENO  mainclass args (to added manually)\r\n" +
		outputFormat  + " " + intermediateCP + " " +mainClass;
        String transformargfileName = Util.getTransformerArgFile();
        Util.writeArgLine(transformargfileName, string);
		

	}

	public static void setWPOptions() {
		TransformMain.setWPOptions();
	}
}
