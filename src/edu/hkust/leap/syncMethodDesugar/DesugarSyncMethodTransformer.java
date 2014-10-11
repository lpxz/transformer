/**
 * 
 */
package edu.hkust.leap.syncMethodDesugar;

import java.util.Iterator;
import java.util.Map;

import edu.hkust.leap.utils.Util;
import soot.Body;
import soot.BodyTransformer;
import soot.Modifier;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.internal.JReturnStmt;
import soot.jimple.internal.JReturnVoidStmt;

/**
 * @author Peng Liu from Purdue
 *
 * <lpxz.ust.hk@gmail.com>
 */
public class DesugarSyncMethodTransformer extends BodyTransformer{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}



	  public Stmt getFirstNonIdentityStmt(Body bb)// copy from soot
	    {
	        Iterator it = bb.getUnits().iterator();
	        Object o = null;
	        while (it.hasNext())
	            if (!((o = it.next()) instanceof IdentityStmt))
	                break;
	        if (o == null)
	            throw new RuntimeException("no non-id statements!");
	        return (Stmt)o;
	    }
	  
	
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
				 Stmt firstNon =getFirstNonIdentityStmt(body);
				 if(firstNon instanceof JReturnStmt || firstNon instanceof JReturnVoidStmt)
				 {// the empty body incurs problems during the lock injection.
					 Stmt nop=Jimple.v().newNopStmt();
					 units.insertBefore(nop, firstNon);									 
				 }								 
			  }							  
			  MethodLocker.addlock(sm);
		  }
		
	  }
		
	}

}
