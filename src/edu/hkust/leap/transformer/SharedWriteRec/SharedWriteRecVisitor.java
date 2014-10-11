package edu.hkust.leap.transformer.SharedWriteRec;

import edu.hkust.leap.transformer.AnalysisOptions;
import edu.hkust.leap.transformer.Visitor;
import edu.hkust.leap.transformer.AddInterestEvent.AddInterestEventVisitor;
import edu.hkust.leap.transformer.contexts.InvokeContext;
import edu.hkust.leap.transformer.contexts.RHSContextImpl;
import edu.hkust.leap.transformer.contexts.RefContext;
import edu.hkust.leap.utils.Parameters;
import edu.hkust.leap.utils.Util;
import soot.*;
import soot.jimple.*;
import soot.util.*;

public class SharedWriteRecVisitor extends Visitor {

    public SharedWriteRecVisitor(Visitor visitor) {
        super(visitor);
    }
    
    public void visitStmtAssign(SootMethod sm, Chain units, AssignStmt assignStmt) {
        nextVisitor.visitStmtAssign(sm, units, assignStmt);
    }

    public void visitStmtEnterMonitor(SootMethod sm, Chain units, EnterMonitorStmt enterMonitorStmt) {
    
    	nextVisitor.visitStmtEnterMonitor(sm, units, enterMonitorStmt);
    }

    public void visitStmtExitMonitor(SootMethod sm, Chain units, ExitMonitorStmt exitMonitorStmt) {
    	
        nextVisitor.visitStmtExitMonitor(sm, units, exitMonitorStmt);
    }
    
    /** Although synchronized instance method invocation and static method invocation
     *  target at different locks,
     * we still use the same SPE for them
     */
    public void visitInstanceInvokeExpr(SootMethod sm, Chain units, Stmt s, InstanceInvokeExpr invokeExpr, InvokeContext context) {
    	// !!!!!!!!!!!!!
    	SootMethod callee = invokeExpr.getMethod();
    	if(AnalysisOptions.modelJDKcalls){
    		if(JDKOpType(callee).equals(Parameters.WRITE)  )// shared WRITE (write)!
        	{
        		Value base =invokeExpr.getBase();
        		String sig=Util.getSig4JDKDataStructureFakedField(base);
        		 if(!Visitor.tlo.isObjectThreadLocal(base, sm))// judge the base, similar to the array
             	{
                 	sharedVariableWriteAccessSet.add(sig);            	
             	}
        	}
    	}
    	
        nextVisitor.visitInstanceInvokeExpr(sm, units, s, invokeExpr, context);

    }
    
    private  String JDKOpType(SootMethod callee) {
        return AddInterestEventVisitor.JDKOpType(callee);
	}

	public void visitStaticInvokeExpr(SootMethod sm, Chain units, Stmt s, StaticInvokeExpr invokeExpr, InvokeContext context) {
	
    	nextVisitor.visitStaticInvokeExpr(sm, units, s, invokeExpr, context);   
    }


    // a[0] =1; // this is missed, it is just like a.element =1; 
    public void visitArrayRef(SootMethod sm, Chain units, Stmt s, ArrayRef arrayRef, RefContext context) {
    	
        String sig = Util.getSig4arrayRef(arrayRef);

        if (context != RHSContextImpl.getInstance())
        {
        	
            if(!Visitor.tlo.isObjectThreadLocal(arrayRef.getBase(), sm))
        	{
            	sharedVariableWriteAccessSet.add(sig);            	
        	}
        }
        
    	nextVisitor.visitArrayRef(sm, units, s, arrayRef, context);
  
    }

    public void visitInstanceFieldRef(SootMethod sm, Chain units, Stmt s, InstanceFieldRef instanceFieldRef, RefContext context) {
    
        String sig = instanceFieldRef.getField().getDeclaringClass().getName()+"."+instanceFieldRef.getField().getName();
    	

        if (context != RHSContextImpl.getInstance())
        {
            if(!Visitor.tlo.isObjectThreadLocal(instanceFieldRef, sm))// instancefield <=> instance on the sharedness
        	{
            	sharedVariableWriteAccessSet.add(sig);            	
        	}
        }
        nextVisitor.visitInstanceFieldRef(sm, units, s, instanceFieldRef, context);
    }
    
    public void visitStaticFieldRef(SootMethod sm, Chain units, Stmt s, StaticFieldRef staticFieldRef, RefContext context) {
    	
        String sig = staticFieldRef.getField().getDeclaringClass().getName()+"."+staticFieldRef.getField().getName();
    	
        //write static field & handle array ref
        if (context != RHSContextImpl.getInstance()) 
        {
            if(!Visitor.tlo.isObjectThreadLocal(staticFieldRef, sm))
        	{
            	sharedVariableWriteAccessSet.add(sig);
            	
        	}
        }
        nextVisitor.visitStaticFieldRef(sm, units, s, staticFieldRef, context);
    }

}
