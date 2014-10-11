/**
 * 
 */
package edu.hkust.leap.syncMethodDesugar;

import java.util.Iterator;
import java.util.Set;

import soot.Body;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootMethod;
import soot.Trap;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.Stmt;
import soot.jimple.StmtBody;
import soot.jimple.StringConstant;
import soot.jimple.internal.JCaughtExceptionRef;
import soot.util.Chain;

/**
 * @author Peng Liu from Purdue
 *
 * <lpxz.ust.hk@gmail.com>
 */
public class MethodInstr {
	// insert methodBegin, methodEnd, methodEndExceptionally.

	/**
	 * @param thisMethod
	 * @param sBody
	 * @param units 
	 * @param isMethodRunnable 
	 * @param isMain 
	 */
	
	public static void addMethodBegin(SootMethod thisMethod, Body sBody, Chain units, int isMethodRunnable, int isMain) {
		 Stmt firstNonIdentity = InstrumentUtility.getFirstNonIdentityStmt(units); 
		 InstrumentUtility.addCallWithStringIntInt(units, firstNonIdentity, "methodBegin", StringConstant.v(thisMethod.getSignature().toString()), 
				 IntConstant.v(isMethodRunnable), IntConstant.v(isMain),
				 true);
		
	}
	

	/**
	 * @param thisMethod
	 * @param sBody
	 * @param units 
	 * @param isMethodRunnable 
	 * @param isMain 
	 */
	  /* make sure that MethodEnd is called even if the method throws an exception

    foo() {
      body;
    }

    to

    foo() {
       try {
          body;
       } catch (java.lang.Throwable __Cal_n) {
          MethodReturn(iid,"foo()");
          throw __Cal_n;
       }
     }

     */

	
	
	public static void addMethodEndExceptionally(SootMethod thisMethod, Body body, Chain units, int isMethodRunnable, int isMain) {
		  Unit first = body.getUnits().getFirst();
		  Unit last= body.getUnits().getLast();
		  String handlerMethod = "methodEndExceptionally";
		  String handlerArg = thisMethod.getSignature().toString();
          addTryCatchHandlerWithString(thisMethod, body, units, first, last,
				handlerMethod, handlerArg, isMethodRunnable, isMain);	
	}


	/**
	 * @param thisMethod
	 * @param body
	 * @param units
	 * @param first
	 * @param last
	 * @param handlerMethod
	 * @param handlerArg
	 */
	private static void addTryCatchHandlerWithString(SootMethod thisMethod,
			Body body, Chain units, Unit first, Unit last,
			String handlerMethod, String handlerArg, int isMethodRunnable, int isMain) {
		Local tmpLocal = Jimple.v().newLocal("exceptionLocal" + thisMethod.getName() + body.getLocalCount(), RefType.v("java.lang.Throwable"));
          body.getLocals().add(tmpLocal);
          Stmt eStmt = Jimple.v().newIdentityStmt(tmpLocal, new JCaughtExceptionRef());
          Trap t = Jimple.v().newTrap(Scene.v().getSootClass("java.lang.Throwable"),
                  (Stmt) first, (Stmt) last, eStmt);
          body.getTraps().addLast(t);
          units.addLast(eStmt);
          // eStmt is now the last unit.
          InstrumentUtility.addCallWithStringIntInt(units, (Stmt) eStmt, handlerMethod, StringConstant.v(handlerArg), IntConstant.v(isMethodRunnable), IntConstant.v(isMain), 
        		  false);
          units.addLast(Jimple.v().newThrowStmt(tmpLocal));
	}


	/**
	 * @param thisMethod
	 * @param sBody
	 * @param units
	 * @param isMethodRunnable 
	 * @param isMain 
	 */
	
	public static void addMethodEnd(SootMethod thisMethod, Body sBody,
			Chain units, int isMethodRunnable, int isMain) {
           // find returns
		Set<Unit> retUnits =InstrumentUtility.getReturnStmts(units);
		for(Unit unit : retUnits)
		{
			  // insert before it.
			  InstrumentUtility.addCallWithStringIntInt(units, (Stmt)unit, "methodEnd", StringConstant.v(thisMethod.getSignature().toString()),
					  IntConstant.v(isMethodRunnable), IntConstant.v(isMain) , true);
		}		
	}

}
