package edu.hkust.leap.syncMethodDesugar;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.plaf.basic.BasicTreeUI.TreeCancelEditingAction;

import polyglot.ast.New;
import polyglot.frontend.VisitorPass;


import soot.Body;
import soot.BodyTransformer;
import soot.EntryPoints;
import soot.G;
import soot.Local;
import soot.LongType;
import soot.Modifier;
import soot.Pack;
import soot.PackManager;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Transform;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.ClassConstant;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NopStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThrowStmt;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JNopStmt;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.internal.RValueBox;

import soot.jimple.toolkits.thread.synchronization.LockAllocationBodyTransformer;
import soot.options.Options;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.graph.ZonedBlockGraph;
import soot.toolkits.graph.pdg.EnhancedUnitGraph;
import soot.toolkits.graph.pdg.RegionAnalysis;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.Pair;
import soot.util.Chain;


// the handling of method exits follows the strategy in MethodInstr.java from project icse2014.
public class MethodLocker {
	// phase: production level, haha
	// ===========================
	public static SootClass mainClass_lockClass =null;
	public static boolean globalLockPrepared = false;
	public static SootField globalLockObj = null;
	public static Local localFromGlobal = null;
	public static Stmt newPrep = null;
	public static Stmt newEntermonitor = null;
	public static Stmt after = null;
	public static Stmt newGotoExitmonitor=null;
	public static Stmt newGotoStmt= null;
	public static Stmt lastEnd=null;
	public static Stmt newExceptionalExitmonitor=null;
	public static Stmt exceptionalEnd= null;
	public static Stmt theStmtAfterEnterMonitor= null;
	
	public static Set<Stmt > earlyEnds = new HashSet<Stmt>();// why does this exist???
	
	public static int counter =0;
	
	
	//=============================do not need resetting
	public static int throwableNum=0;

	// ===========================

	protected static final boolean optionOpenNesting = false;
	private static final String LOCALLOCK = "locallock";
	private static final String LOCALTHISLOCK = "localthislock";
	public static HashMap methodToExcUnitGraph = new HashMap();// run only once
	// after all
	public static HashMap methodToFlowSet = new HashMap();// run only once after

	// all
	//!!!!!!!!!!!!
	// needs to insert newPrep.clone() in front of exitmonitor in the 
	//exception branch!, or else if the exception direct control to the throw, maybe the 
	// localSharedS is not yet initialized!!!
	public static void main(String[] args) throws IOException {}

	// List entryPoints=EntryPoints.v().methodsOfApplicationClasses();
	// List mainEntries = new ArrayList();
	// for(int i=0;i< entryPoints.size(); i++)
	// {
	// if(entryPoints.get(i).toString().contains("main"))
	// {
	// mainEntries.add(entryPoints.get(i));
	// }
	// }
	//
	// soot.Scene.v().setEntryPoints(mainEntries); // process : app and its
	// reachable methods.




	public static void addlock(SootMethod sm) {

		 PatchingChain<Unit>units = sm.getActiveBody().getUnits();

		
		
		resetFlags();
		monitorenter(sm);
		monitorexit(sm);
		
		
		Body result = sm.getActiveBody();
		{
			result.validateLocals();
			result.validateTraps();
			result.validateUnitBoxes();
			//result.validateUses();
	            result.validateValueBoxes();
	            try {
	            	 result.checkInit();
				} catch (Exception e) {
					 result.checkInit();
				}
		}
		
	}



	private static void resetFlags() {
		mainClass_lockClass =null;
		globalLockPrepared = false;
		globalLockObj = null;
		localFromGlobal = null;
		newPrep = null;
		newEntermonitor = null;
		after = null;
		newGotoExitmonitor = null;
		newGotoStmt= null;
		lastEnd=null;
		earlyEnds = new HashSet<Stmt>();
		newExceptionalExitmonitor=null;
		exceptionalEnd=null;
		theStmtAfterEnterMonitor=null;

	}





	// ========================================
	private static void monitorenter(SootMethod sm) {
		
		// Monitorexit needs the newPrep! should not be null
		
		JimpleBody body = (JimpleBody)sm.getActiveBody();
		PatchingChain<Unit> units  = body.getUnits();
		Local  left = null;
		Value right =null;
		if(sm.isStatic())
		{
				String classname = sm.getDeclaringClass().getName();
				String constclas = classname.replace('.', '/');
				 right = ClassConstant.v(constclas);
				  left = Jimple.v().newLocal(LOCALLOCK+sm.getName(), RefType.v("java.lang.Class"));//!!problematic!!
				if(!body.getLocals().contains(left))
				{
					body.getLocals().add(left);
				}
			
			
			Stmt prepare  = Jimple.v().newAssignStmt(left, right);
			newPrep = prepare;//!!!!!!!!!
			newEntermonitor = Jimple.v().newEnterMonitorStmt(left);



			 theStmtAfterEnterMonitor = body.getFirstNonIdentityStmt();
			units.insertBeforeNoRedirect(newPrep, theStmtAfterEnterMonitor);
			units.insertBeforeNoRedirect(newEntermonitor, theStmtAfterEnterMonitor);


			
			
		}
		else {
				right = body.getThisLocal();		 
                left = Jimple.v().newLocal(LOCALTHISLOCK+sm.getName(), RefType.v("java.lang.Object"));//!!problematic!!
				if(!body.getLocals().contains(left))
				{
					body.getLocals().add(left);
				}
							
				
				Stmt prepare  = Jimple.v().newAssignStmt(left, right);
				newPrep = prepare;//!!!!!!!!!
							
				
				newEntermonitor = Jimple.v().newEnterMonitorStmt(left);
              theStmtAfterEnterMonitor = body.getFirstNonIdentityStmt();
				units.insertBeforeNoRedirect(newPrep, theStmtAfterEnterMonitor);
				units.insertBeforeNoRedirect(newEntermonitor, theStmtAfterEnterMonitor);
			
		}
	
	    if(newPrep == null || theStmtAfterEnterMonitor==null)
	    {
	    	throw new RuntimeException();
	    }
	    localFromGlobal =  (Local)((JAssignStmt)newPrep).getLeftOp();	
	
	}

	// ===========================================
	private static void monitorexit(SootMethod sm ) {
		// inject after end, I do not know about the early ends... It is usually
		// useless
		// only for the case: exit is immediately following enter, succeed for
		// sure.
		//earlyendExit();// not implemented
		
		JimpleBody bb =(JimpleBody) sm.getActiveBody();
		PatchingChain<Unit> units = bb.getUnits();
		Set<Stmt> rets  = new HashSet<Stmt>();
		Iterator<Unit> it = bb.getUnits().iterator();
		while (it.hasNext()) {
			Unit unit = (Unit) it.next();
			if(unit instanceof ReturnVoidStmt || unit instanceof ReturnStmt)
			{
				rets.add((Stmt) unit );
			}
		}
		for(Stmt ret: rets)
		{
			standardGoToExit(ret , bb);
		
		}

		 exceptionalExit( bb); // from first to last. syntactical scope


	}

	private static void earlyendExit() {
	}

	private static void standardGoToExit( Stmt end, JimpleBody bb) {
		PatchingChain<Unit> units = bb.getUnits();
		

//		Stmt tmp = (Stmt) newPrep.clone();
//		units.insertBefore(tmp, after);
		//=========================================================
		newGotoExitmonitor = Jimple.v().newExitMonitorStmt(localFromGlobal);
		units.insertBefore(newGotoExitmonitor, end);


		//=========================================================
		 newGotoStmt = Jimple.v().newGotoStmt(end);
		units.insertBeforeNoRedirect(newGotoStmt, end);
		//!!!
		// if you use insertBefore, all goto-> end will redirect to newlyadded
		// but newlyadded also goto-> end, then, 
		// newlyadded goto-> end, redirect to newlyadded. 
		// newlyadded goto-> newlyadded
		after = end;

	}

	private static void exceptionalExit(  JimpleBody bb) {
		  Unit first = newEntermonitor;
		  
		PatchingChain<Unit> units = bb.getUnits();
	
        lastEnd = (Stmt)bb.getUnits().getLast();
		//=====================
		// Add throwable
		Local throwableLocal = Jimple.v().newLocal("throwableLocal" + (throwableNum++), RefType.v("java.lang.Throwable"));
		bb.getLocals().add(throwableLocal);
		// Add stmts
	
		
		
		
		
		
		
		
		// catch, newprep, exit, throw
		Stmt newThrow = Jimple.v().newThrowStmt(throwableLocal);
		units.insertAfter(newThrow, lastEnd);
		
		//============================
		newExceptionalExitmonitor = Jimple.v().newExitMonitorStmt(localFromGlobal);
		units.insertAfter(newExceptionalExitmonitor, lastEnd);
//		Unit tmpPrep=null;
//		if(newPrep!=null)  {
//			tmpPrep  = (Unit)newPrep.clone();
//			
//		}
//		else {
//			
//			throw new RuntimeException();
//		}
//		
//	       Value monitorLocal = ((JAssignStmt)newPrep).getLeftOp();		
//		   newExceptionalExitmonitor = Jimple.v().newExitMonitorStmt((Local) monitorLocal);
//
//		units.insertAfter(newExceptionalExitmonitor, lastEnd);// do not reuse the last exit monitor!, patchingchain will complain
//		units.insertAfter(tmpPrep, lastEnd);// do not reuse the last exit monitor!, patchingchain will complain
		//=============================
		
		Stmt newCatch = Jimple.v().newIdentityStmt(throwableLocal, Jimple.v().newCaughtExceptionRef());
		units.insertAfter(newCatch, lastEnd);
		SootClass throwableClass = Scene.v().loadClassAndSupport("java.lang.Throwable");
		bb.getTraps().addFirst(Jimple.v().newTrap(throwableClass, newExceptionalExitmonitor, newThrow, newCatch));
		bb.getTraps().addFirst(Jimple.v().newTrap(throwableClass,first  , lastEnd, newCatch));// bb.getFirstNonIdentityStmt()
		// do not start from the identitystmt, because the localthislocal initialized after it may not be initialized due to the exceptional jumping.
		// newEntermonitor is not so precise as theStmtAfterEnterMonitor (refer to the original one genereated by soot)
		exceptionalEnd =newThrow;

	}

}
