package edu.hkust.leap.transformer.AddInterestEvent;

import java.util.Iterator;
import java.util.Map;


import edu.hkust.leap.transformer.*;
import edu.hkust.leap.utils.Parameters;
import edu.hkust.leap.utils.Util;
import soot.Body;
import soot.BodyTransformer;
import soot.Modifier;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.util.Chain;

public class AddInterestEventTransformer extends BodyTransformer {
	private Visitor visitor;
	public AddInterestEventTransformer()
	{
        AddInterestEventRV vv = new AddInterestEventRV(null);
        AddInterestEventVisitor pv = new AddInterestEventVisitor(vv);
        vv.setNextVisitor(pv);
        visitor = pv;
	}
	protected void internalTransform(Body body, String pn, Map map) {
		
		Util.resetParameters();
		SootMethod thisMethod = body.getMethod();
		
		/*
		 * if the method is static and also has no para
		 * we need a way to insert tid_method
		 */
//		if(thisMethod.isStatic()&&thisMethod.getParameterCount()==0)
//			return;
		
		if(!Util.shouldInstruThisMethod(thisMethod.getName()))
			return;
		
		SootClass thisClass = thisMethod.getDeclaringClass();
		String scname = thisClass.getName();
		//System.out.println("scname: "+scname);
		if(!Util.shouldInstruThisClass(scname)) 
			return;
				 	
		if(thisMethod.toString().contains("void main(java.lang.String[])"))
		{
			Parameters.isMethodMain = true;
		}
		else if(thisMethod.toString().contains("void run()")&&Util.isRunnableSubType(thisClass))
		{
			Parameters.isMethodRunnable = true;
		}
		if(thisMethod.isSynchronized())
		{
			Parameters.isMethodSynchronized = true;
			System.err.println("what is wrong? Run TransformPreprocess...");
		//	throw new RuntimeException("what is wrong? Run TransformPreprocess...");
		}
			
		Chain units = body.getUnits();
		
		//NO IDEA WHY THIS
		//To enable insert tid
		if(thisMethod.isStatic()&&thisMethod.getParameterCount()==0)
		{
			Stmt nop=Jimple.v().newNopStmt();
			//insert the nop just before the return stmt
			units.insertBefore(nop, units.getFirst());
		}
		
        Iterator stmtIt = units.snapshotIterator();    	       
        while (stmtIt.hasNext()) 
        {
            Stmt s = (Stmt) stmtIt.next();
            visitor.visitStmt(thisMethod, units, s);
        }
        
    	if(Parameters.isMethodMain||Parameters.isMethodRunnable)
    	{
    		if(Parameters.isRecord)
    		{
    			//DO OR DO NOT CATCH EXCEPTION??
    			//Visitor.addCallCatchException(body);
    		}
    		
    		if(Parameters.isMethodMain)
    		{ 
    			Visitor.addCallMainMethodEnterInsert(thisMethod, units);
    			Visitor.addCallMainMethodExitInsert(thisMethod, units);
    		}
    		else
    		{
    			Visitor.addCallRunMethodEnterInsert(thisMethod, units);
    			Visitor.addCallRunMethodExitInsert(thisMethod, units);
    		}
        }
    	

    	
    	body.validate();
	}

}
