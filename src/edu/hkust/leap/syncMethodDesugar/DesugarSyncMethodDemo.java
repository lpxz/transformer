package edu.hkust.leap.syncMethodDesugar;



import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

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
import edu.hkust.leap.transformer.TransformMain;
import edu.hkust.leap.transformer.Visitor;
import edu.hkust.leap.transformer.AddInterestEvent.AddInterestEventTransformer;
import edu.hkust.leap.transformer.SharedWriteRec.SharedWriteRecTransformer;
import edu.hkust.leap.utils.Parameters;
import edu.hkust.leap.utils.Util;

public class DesugarSyncMethodDemo {

	/**
	 * @param args
	 */
	public static HashSet<String> usedJDKOps = new HashSet<String>();
	public static HashSet<String> randomOps = new HashSet<String>();
	public static void main(String[] args) throws Exception{
		System.out.println("loading the general arguments from leap.transformer.arg:");
		String arglineFileName = Util.getTransformerArgFile();
		String argline =Util.getArgLine(arglineFileName);
		String[] argline_items = argline.split(" ");

		String argsOfmtrt = "-f J -pp -cp ./bin -main-class simple.Example simple.Example -d ./tmp/jimple/" ;
		

		String interString = argsOfmtrt;
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
		Pack jtp = PackManager.v().getPack("jtp");
		jtp.add(new Transform("jtp.desugarSyncMethod", 	new DesugarSyncMethodTransformer()));
		

		PackManager.v().runPacks();// 1
		PackManager.v().writeOutput();
		System.out.println("ending");

		
	}

	public static void setWPOptions() {
		TransformMain.setWPOptions();
	}
}
