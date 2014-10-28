package edu.hkust.leap.transformer.AddInterestEvent;

import java.util.LinkedList;

import edu.hkust.leap.transformer.AnalysisOptions;
import edu.hkust.leap.transformer.Visitor;
import edu.hkust.leap.transformer.contexts.*;
import edu.hkust.leap.utils.Parameters;
import edu.hkust.leap.utils.Util;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Value;
import soot.jimple.*;
import soot.tagkit.LineNumberTag;
import soot.util.Chain;

public class AddInterestEventVisitor extends Visitor {

    public AddInterestEventVisitor(Visitor visitor) {
        super(visitor);
    }
    public void visitStmtAssign(SootMethod sm, Chain units, AssignStmt assignStmt) {
        nextVisitor.visitStmtAssign(sm, units, assignStmt);
    }
    public void visitStmtEnterMonitor(SootMethod sm, Chain units, EnterMonitorStmt enterMonitorStmt) 
    {
    	Visitor.sharedaccessnum++;
    	Visitor.totalaccessnum++; 	
    	Visitor.instrusharedaccessnum++;
    	
    	Value op = enterMonitorStmt.getOp();
    	Type type = op.getType();
    	String sig = type.toString();
    	Value memory = StringConstant.v(sig);
    	
		Visitor.addCallAccessSyncObjInstance(sm, units, enterMonitorStmt, "enterMonitorAfter", op,memory, false);
		if(Parameters.isReplay)
			units.remove(enterMonitorStmt);
		
        nextVisitor.visitStmtEnterMonitor(sm, units, enterMonitorStmt);
    }

    public void visitStmtExitMonitor(SootMethod sm, Chain units, ExitMonitorStmt exitMonitorStmt) {
    	Visitor.sharedaccessnum++;
    	Visitor.totalaccessnum++; 	
    	Visitor.instrusharedaccessnum++;
    	
    	Value op = exitMonitorStmt.getOp();
    	Type type = op.getType();
    	String sig = type.toString();
    	Value memory = StringConstant.v(sig);
    	
		Visitor.addCallAccessSyncObjInstance(sm, units, exitMonitorStmt, "exitMonitorBefore", op,memory, true);
		
		if(Parameters.isReplay)
			units.remove(exitMonitorStmt);
		
        nextVisitor.visitStmtExitMonitor(sm, units, exitMonitorStmt);
    }

    public void visitInstanceInvokeExpr(SootMethod sm, Chain units, Stmt s, InstanceInvokeExpr invokeExpr, InvokeContext context) 
    {
            String sigclass = invokeExpr.getMethod().getDeclaringClass().getName();//+"."+invokeExpr.getMethod().getName();
            Value memory = StringConstant.v(sigclass);
            
            Value base = invokeExpr.getBase();
            String sig = invokeExpr.getMethod().getSubSignature();
            SootMethod callee = invokeExpr.getMethod();
            
            
            // let us do not support hashmap/hashset invocations now. 
            if(AnalysisOptions.modelJDKcalls){
                if(JDKOpType(callee).equals(Parameters.READ)||JDKOpType(callee).equals(Parameters.WRITE) )
                {
                	visitDS(sm,units,s, invokeExpr, JDKOpType(callee));// the context is not important, the op type matters
                }
            }
        
            
            if (sig.equals("void wait()")||sig.equals("void wait(long)") || sig.equals("void wait(long,int)")) 
            {        	
          
        		Visitor.addCallAccessSyncObjInstance(sm, units, s, "waitAfter", base,memory, false);
            	Visitor.instrusharedaccessnum++;
            	Visitor.sharedaccessnum++;
            	Visitor.totalaccessnum++;
            	
            	if(Parameters.isReplay)
        			units.remove(s);
            	
            	

            //} else if (sig.equals("void wait(long)") || sig.equals("void wait(long,int)")) {

            } else if (sig.equals("void notify()")) 
            {
        		Visitor.addCallAccessSyncObjInstance(sm, units, s, "notifyBefore", base,memory, true);
            	Visitor.instrusharedaccessnum++;
            	Visitor.sharedaccessnum++;
            	Visitor.totalaccessnum++;
            	
            	if(Parameters.isReplay)
        			units.remove(s);
            } else if (sig.equals("void notifyAll()")) 
            {
        		Visitor.addCallAccessSyncObjInstance(sm, units, s, "notifyAllBefore", base,memory, true);
            	Visitor.instrusharedaccessnum++;
            	Visitor.sharedaccessnum++;
            	Visitor.totalaccessnum++;
            	
            	if(Parameters.isReplay)
        			units.remove(s);
            }
            else if (sig.equals("void start()") && isThreadSubType(invokeExpr.getMethod().getDeclaringClass())) 
            {
        		Visitor.addCallstartRunThreadBefore(sm, units, s, "startRunThreadBefore", invokeExpr.getBase());
            }
            else if ((sig.equals("void join()") || sig.equals("void join(long)") || sig.equals("void join(long,int)"))
                    && isThreadSubType(invokeExpr.getMethod().getDeclaringClass()))
            {
        		Visitor.addCallJoinRunThreadAfter(sm, units, s, "joinRunThreadAfter", invokeExpr.getBase());
            }
        
        nextVisitor.visitInstanceInvokeExpr(sm, units, s, invokeExpr, context);
    }

    
//    public void visitFakedInstanceFieldRef(SootMethod sm, Chain units, Stmt s, InstanceInvokeExpr invokeExpr) 
//    { // x.set() <=> x.fakedf= y !
//    	Visitor.totalaccessnum++;
//
//    	Value base = invokeExpr.getBase();
//    	String sig = Util.getSig4JDKDataStructureFakedField(base);
//
//		Value memory = StringConstant.v(sig);
//
//		if(Visitor.sharedVariableWriteAccessSet.contains(sig))
//		{	
//			String methodname = "readBeforeFakedInstance"; 
//			SootMethod callee = invokeExpr.getMethod();
//			if (JDKOpType(callee).equals(Parameters.WRITE)) 
//	        {
//				methodname = "writeBeforeFakedInstance";
//	        }
////			else if(instanceFieldRef.getField().getType() instanceof ArrayType)// nonsense
////			{
////				
////				Stmt nextStmt =  (Stmt)units.getSuccOf(s);
////				if(s instanceof AssignStmt && nextStmt instanceof AssignStmt)
////				{
////					AssignStmt assgnStmt = (AssignStmt) s;
////					AssignStmt assgnNextStmt = (AssignStmt) nextStmt;
////					if(assgnNextStmt.getLeftOp().toString().contains(assgnStmt.getLeftOp().toString()))
////			        {
////						methodname = "writeBeforeInstance";
////			        }
////				}  
////			}
//				
//		    Visitor.addCallAccessSPEFakedInstance(sm,units, s, methodname, base, memory, true);
//			Visitor.sharedaccessnum++;
//		    Visitor.instrusharedaccessnum++;        	
//		}
////        nextVisitor.visitInstanceFieldRef(sm, units, s, instanceFieldRef, context); // this is a side effect call, no need to call any htinkg further
//    }
    
    /**
	 * @param sm
	 * @param units
	 * @param s
	 * @param invokeExpr
	 */
	public void visitDS(SootMethod sm, Chain units, Stmt s, InstanceInvokeExpr invokeExpr, String type) {

	     if(AnalysisOptions.modelJDKcalls)
	     {
	    	 Visitor.totalaccessnum++;    	

	     	Value base = invokeExpr.getBase();
	     	
	 		String sig = Util.getSig4DS(base);
	 		Value memory = StringConstant.v(sig);		
	 		
	 		if(Visitor.sharedVariableWriteAccessSet.contains(sig))// ok
	 		{	
	 			String methodname = null; 
	 			Value v=null;
	 			if (type.equals(Parameters.READ)) 
	 	        {
	 				methodname = "readBeforeDSElem"; 
	 				if(s instanceof AssignStmt)
	 				{
	 					v= ((AssignStmt)s).getLeftOp();
	 				}else {
						v = NullConstant.v();
					}
	 				
	 	        }	else {
	 	        	methodname = "writeBeforeDSElem";
	 	        	try {
						v=invokeExpr.getArg(1);// (k,v)
					} catch (Exception e) {
						v=NullConstant.v();
					}
				}		
	 				
	 			
	 			
	 			
	 			LinkedList args = new LinkedList();
	 			args.addLast(base);
	 			args.addLast(IntConstant.v(getSPEIndex(memory)));
	 			args.addLast(getTIDLocal(sm));
	 			args.addLast(StringConstant.v( sm.getDeclaringClass().getName()));		
	 			LineNumberTag tag = (LineNumberTag)s.getTag("LineNumberTag");
	 			if(tag!=null)
	 			{
	 				args.addLast(IntConstant.v(tag.getLineNumber()));
	 			}
	 			else {
	 				args.addLast(IntConstant.v(-1));
	 			}
	 			
	 			args.addLast(invokeExpr.getArg(0));
	 			

	 			
	 			 if(v.getType() instanceof PrimType)
	 		    {
	 		    	Value staticInvoke;

	 		    	if(v.getType() instanceof BooleanType)
	 		    	{
	 		    		args.addLast(v);
	 		 			SootMethodRef mr = Scene.v().getMethod(
	 		 					"<" + observerClass + ": void " + methodname
	 		 							+ "(java.lang.Object,int,long,java.lang.String,int,java.lang.Object,boolean)>").makeRef();
	 		 			units.insertBefore(Jimple.v().newInvokeStmt(
	 		 						Jimple.v().newStaticInvokeExpr(mr, args)), s);
	 		    		
	 		    	}
	 		    	else if(v.getType() instanceof ByteType)
	 		    	{ 
	 		    		args.addLast(v);
	 		    		SootMethodRef mr = Scene.v().getMethod(
	 		 					"<" + observerClass + ": void " + methodname
	 		 							+ "(java.lang.Object,int,long,java.lang.String,int,java.lang.Object,byte)>").makeRef();
	 		 			units.insertBefore(Jimple.v().newInvokeStmt(
	 		 						Jimple.v().newStaticInvokeExpr(mr, args)), s);
	 		    		
	 		    	}
	 		    	else if(v.getType() instanceof CharType)
	 		    	{
	 		    		args.addLast(v);
	 		    		SootMethodRef mr = Scene.v().getMethod(
	 		 					"<" + observerClass + ": void " + methodname
	 		 							+ "(java.lang.Object,int,long,java.lang.String,int,java.lang.Object,char)>").makeRef();
	 		 			units.insertBefore(Jimple.v().newInvokeStmt(
	 		 						Jimple.v().newStaticInvokeExpr(mr, args)), s);
	 		    		
	 		    	}
	 		    	else if(v.getType() instanceof DoubleType)
	 		    	{
	 		    		args.addLast(v);
	 		    		SootMethodRef mr = Scene.v().getMethod(
	 		 					"<" + observerClass + ": void " + methodname
	 		 							+ "(java.lang.Object,int,long,java.lang.String,int,java.lang.Object,double)>").makeRef();
	 		 			units.insertBefore(Jimple.v().newInvokeStmt(
	 		 						Jimple.v().newStaticInvokeExpr(mr, args)), s);
	 		    		
	 		    	}
	 		    	else if(v.getType() instanceof FloatType)
	 		    	{
	 		    		args.addLast(v);
	 		    		SootMethodRef mr = Scene.v().getMethod(
	 		 					"<" + observerClass + ": void " + methodname
	 		 							+ "(java.lang.Object,int,long,java.lang.String,int,java.lang.Object,float)>").makeRef();
	 		 			units.insertBefore(Jimple.v().newInvokeStmt(
	 		 						Jimple.v().newStaticInvokeExpr(mr, args)), s);
	 		    		
	 		    	}
	 		    	else if(v.getType() instanceof IntType)
	 		    	{
	 		    		args.addLast(v);
	 		    		SootMethodRef mr = Scene.v().getMethod(
	 		 					"<" + observerClass + ": void " + methodname
	 		 							+ "(java.lang.Object,int,long,java.lang.String,int,java.lang.Object,int)>").makeRef();
	 		 			units.insertBefore(Jimple.v().newInvokeStmt(
	 		 						Jimple.v().newStaticInvokeExpr(mr, args)), s);
	 		    		
	 		    	}
	 		    	else if(v.getType() instanceof LongType)
	 		    	{
	 		    		args.addLast(v);
	 		    		SootMethodRef mr = Scene.v().getMethod(
	 		 					"<" + observerClass + ": void " + methodname
	 		 							+ "(java.lang.Object,int,long,java.lang.String,int,java.lang.Object,long)>").makeRef();
	 		 			units.insertBefore(Jimple.v().newInvokeStmt(
	 		 						Jimple.v().newStaticInvokeExpr(mr, args)), s);
	 		    		
	 		    	}
	 		    	else//if (v.getType() instanceof ShortType)
	 		    	{
	 		    		args.addLast(v);
	 		    		SootMethodRef mr = Scene.v().getMethod(
	 		 					"<" + observerClass + ": void " + methodname
	 		 							+ "(java.lang.Object,int,long,java.lang.String,int,java.lang.Object,short)>").makeRef();
	 		 			units.insertBefore(Jimple.v().newInvokeStmt(
	 		 						Jimple.v().newStaticInvokeExpr(mr, args)), s);
	 		    		
	 		    	}
	 		    }
	 		    else
	 		    {
	 		    	args.addLast(v);
	 		    	SootMethodRef mr = Scene.v().getMethod(
			 					"<" + observerClass + ": void " + methodname
			 							+ "(java.lang.Object,int,long,java.lang.String,int,java.lang.Object,java.lang.Object)>").makeRef();
			 			units.insertBefore(Jimple.v().newInvokeStmt(
			 						Jimple.v().newStaticInvokeExpr(mr, args)), s);	
	 		    }
	 			 
	 			
//	 		    Visitor.addCallAccessSPEArrayElem(sm,units, s, methodname, base, memory, true,  arrayRef);
	 			Visitor.sharedaccessnum++;
	 		    Visitor.instrusharedaccessnum++;        	
	 		}
	     }
	    	
	              	
	    
		
		
	}
	public static String JDKOpType(SootMethod sm) {
    	SootClass sClass = sm.getDeclaringClass();
    	String classname = sClass.getName();
    	String methodname  = sm.getName();
    	return Parameters.readOrWrite(classname, methodname);

	}
	public void visitStaticInvokeExpr(SootMethod sm, Chain units, Stmt s, StaticInvokeExpr invokeExpr, InvokeContext context) {
        
    	nextVisitor.visitStaticInvokeExpr(sm, units, s, invokeExpr, context);
    }

    public void visitArrayRef(SootMethod sm, Chain units, Stmt s, ArrayRef arrayRef, RefContext context) {
     if(AnalysisOptions.handlingArray)
     {
    	 Visitor.totalaccessnum++;    	

     	Value base = arrayRef.getBase();
     	
 		String sig = Util.getSig4arrayRef(arrayRef);
 		Value memory = StringConstant.v(sig);		
 		
 		if(Visitor.sharedVariableWriteAccessSet.contains(sig))// ok
 		{	
 			String methodname = null; 
 			Value v=null;
 			if (context == RHSContextImpl.getInstance()) 
 	        {
 				methodname = "readBeforeArrayElem"; 
 				v= ((AssignStmt)s).getLeftOp();
 	        }	else {
 	        	methodname = "writeBeforeArrayElem";
 	        	v= ((AssignStmt)s).getRightOp();
			}		
 				
 			
 			
 			LinkedList args = new LinkedList();
 			args.addLast(base);
 			args.addLast(IntConstant.v(getSPEIndex(memory)));
 			args.addLast(getTIDLocal(sm));
 			args.addLast(StringConstant.v( sm.getDeclaringClass().getName()));		
 			LineNumberTag tag = (LineNumberTag)s.getTag("LineNumberTag");
 			if(tag!=null)
 			{
 				args.addLast(IntConstant.v(tag.getLineNumber()));
 			}
 			else {
 				args.addLast(IntConstant.v(-1));
 			}
 			
 			args.addLast(arrayRef.getIndex());
 			

 			
 			 if(v.getType() instanceof PrimType)
 		    {
 		    	Value staticInvoke;

 		    	if(v.getType() instanceof BooleanType)
 		    	{
 		    		args.addLast(v);
 		 			SootMethodRef mr = Scene.v().getMethod(
 		 					"<" + observerClass + ": void " + methodname
 		 							+ "(java.lang.Object,int,long,java.lang.String,int,int,boolean)>").makeRef();
 		 			units.insertBefore(Jimple.v().newInvokeStmt(
 		 						Jimple.v().newStaticInvokeExpr(mr, args)), s);
 		    		
 		    	}
 		    	else if(v.getType() instanceof ByteType)
 		    	{ 
 		    		args.addLast(v);
 		    		SootMethodRef mr = Scene.v().getMethod(
 		 					"<" + observerClass + ": void " + methodname
 		 							+ "(java.lang.Object,int,long,java.lang.String,int,int,byte)>").makeRef();
 		 			units.insertBefore(Jimple.v().newInvokeStmt(
 		 						Jimple.v().newStaticInvokeExpr(mr, args)), s);
 		    		
 		    	}
 		    	else if(v.getType() instanceof CharType)
 		    	{
 		    		args.addLast(v);
 		    		SootMethodRef mr = Scene.v().getMethod(
 		 					"<" + observerClass + ": void " + methodname
 		 							+ "(java.lang.Object,int,long,java.lang.String,int,int,char)>").makeRef();
 		 			units.insertBefore(Jimple.v().newInvokeStmt(
 		 						Jimple.v().newStaticInvokeExpr(mr, args)), s);
 		    		
 		    	}
 		    	else if(v.getType() instanceof DoubleType)
 		    	{
 		    		args.addLast(v);
 		    		SootMethodRef mr = Scene.v().getMethod(
 		 					"<" + observerClass + ": void " + methodname
 		 							+ "(java.lang.Object,int,long,java.lang.String,int,int,double)>").makeRef();
 		 			units.insertBefore(Jimple.v().newInvokeStmt(
 		 						Jimple.v().newStaticInvokeExpr(mr, args)), s);
 		    		
 		    	}
 		    	else if(v.getType() instanceof FloatType)
 		    	{
 		    		args.addLast(v);
 		    		SootMethodRef mr = Scene.v().getMethod(
 		 					"<" + observerClass + ": void " + methodname
 		 							+ "(java.lang.Object,int,long,java.lang.String,int,int,float)>").makeRef();
 		 			units.insertBefore(Jimple.v().newInvokeStmt(
 		 						Jimple.v().newStaticInvokeExpr(mr, args)), s);
 		    		
 		    	}
 		    	else if(v.getType() instanceof IntType)
 		    	{
 		    		args.addLast(v);
 		    		SootMethodRef mr = Scene.v().getMethod(
 		 					"<" + observerClass + ": void " + methodname
 		 							+ "(java.lang.Object,int,long,java.lang.String,int,int,int)>").makeRef();
 		 			units.insertBefore(Jimple.v().newInvokeStmt(
 		 						Jimple.v().newStaticInvokeExpr(mr, args)), s);
 		    		
 		    	}
 		    	else if(v.getType() instanceof LongType)
 		    	{
 		    		args.addLast(v);
 		    		SootMethodRef mr = Scene.v().getMethod(
 		 					"<" + observerClass + ": void " + methodname
 		 							+ "(java.lang.Object,int,long,java.lang.String,int,int,long)>").makeRef();
 		 			units.insertBefore(Jimple.v().newInvokeStmt(
 		 						Jimple.v().newStaticInvokeExpr(mr, args)), s);
 		    		
 		    	}
 		    	else//if (v.getType() instanceof ShortType)
 		    	{
 		    		args.addLast(v);
 		    		SootMethodRef mr = Scene.v().getMethod(
 		 					"<" + observerClass + ": void " + methodname
 		 							+ "(java.lang.Object,int,long,java.lang.String,int,int,short)>").makeRef();
 		 			units.insertBefore(Jimple.v().newInvokeStmt(
 		 						Jimple.v().newStaticInvokeExpr(mr, args)), s);
 		    		
 		    	}
 		    }
 		    else
 		    {
 		    	args.addLast(v);
 		    	SootMethodRef mr = Scene.v().getMethod(
		 					"<" + observerClass + ": void " + methodname
		 							+ "(java.lang.Object,int,long,java.lang.String,int,int,java.lang.Object)>").makeRef();
		 			units.insertBefore(Jimple.v().newInvokeStmt(
		 						Jimple.v().newStaticInvokeExpr(mr, args)), s);	
 		    }
 			 
 			
// 		    Visitor.addCallAccessSPEArrayElem(sm,units, s, methodname, base, memory, true,  arrayRef);
 			Visitor.sharedaccessnum++;
 		    Visitor.instrusharedaccessnum++;        	
 		}
     }
    	
              	
        nextVisitor.visitArrayRef(sm, units, s, arrayRef, context);
    }


    
    public void visitInstanceFieldRef(SootMethod sm, Chain units, Stmt s, InstanceFieldRef instanceFieldRef, RefContext context) 
    {// should be named as readBeforeInstanceField, for history reason, keep it.
    	Visitor.totalaccessnum++;

    	Value base = instanceFieldRef.getBase();
    	
		String sig = instanceFieldRef.getField().getDeclaringClass().getName()+"."+instanceFieldRef.getField().getName();
		Value memory = StringConstant.v(sig);
		
		if(!instanceFieldRef.getField().isFinal())
		if(Visitor.sharedVariableWriteAccessSet.contains(sig))
		{	
			String methodname = null; 
			
			Value v=null;
			if (context == RHSContextImpl.getInstance()) 
	        {
				methodname = "readBeforeInstance";
				v= ((AssignStmt)s).getLeftOp();
	        }else {
	        	methodname ="writeBeforeInstance";
	        	v= ((AssignStmt)s).getRightOp();
			}

			// set up the call
			LinkedList args = new LinkedList();
			args.addLast(base);
			args.addLast(IntConstant.v(getSPEIndex(memory)));
			args.addLast(getTIDLocal(sm));
			args.addLast(StringConstant.v( sm.getDeclaringClass().getName()));		
			LineNumberTag tag = (LineNumberTag)s.getTag("LineNumberTag");
			if(tag!=null)
			{
				args.addLast(IntConstant.v(tag.getLineNumber()));
			}
			else {
				args.addLast(IntConstant.v(-1));
			}
			

		    if(v.getType() instanceof PrimType)
		    {
		    	Value staticInvoke;

		    	if(v.getType() instanceof BooleanType)
		    	{
		    		args.addLast(v);
		    		SootMethodRef mr = Scene.v().getMethod(
							"<" + observerClass + ": void " + methodname
									+ "(java.lang.Object,int,long,java.lang.String,int,boolean)>").makeRef();
					units.insertBefore(Jimple.v().newInvokeStmt(
								Jimple.v().newStaticInvokeExpr(mr, args)), s);
		    	}
		    	else if(v.getType() instanceof ByteType)
		    	{ 
		    		args.addLast(v);
		    		SootMethodRef mr = Scene.v().getMethod(
							"<" + observerClass + ": void " + methodname
									+ "(java.lang.Object,int,long,java.lang.String,int,byte)>").makeRef();
					units.insertBefore(Jimple.v().newInvokeStmt(
								Jimple.v().newStaticInvokeExpr(mr, args)), s);
		    	}
		    	else if(v.getType() instanceof CharType)
		    	{
		    		args.addLast(v);
		    		SootMethodRef mr = Scene.v().getMethod(
							"<" + observerClass + ": void " + methodname
									+ "(java.lang.Object,int,long,java.lang.String,int,char)>").makeRef();
					units.insertBefore(Jimple.v().newInvokeStmt(
								Jimple.v().newStaticInvokeExpr(mr, args)), s);
		    	}
		    	else if(v.getType() instanceof DoubleType)
		    	{
		    		args.addLast(v);
		    		SootMethodRef mr = Scene.v().getMethod(
							"<" + observerClass + ": void " + methodname
									+ "(java.lang.Object,int,long,java.lang.String,int,double)>").makeRef();
					units.insertBefore(Jimple.v().newInvokeStmt(
								Jimple.v().newStaticInvokeExpr(mr, args)), s);
		    	}
		    	else if(v.getType() instanceof FloatType)
		    	{
		    		args.addLast(v);
		    		SootMethodRef mr = Scene.v().getMethod(
							"<" + observerClass + ": void " + methodname
									+ "(java.lang.Object,int,long,java.lang.String,int,float)>").makeRef();
					units.insertBefore(Jimple.v().newInvokeStmt(
								Jimple.v().newStaticInvokeExpr(mr, args)), s);
		    	}
		    	else if(v.getType() instanceof IntType)
		    	{
		    		args.addLast(v);
		    		SootMethodRef mr = Scene.v().getMethod(
							"<" + observerClass + ": void " + methodname
									+ "(java.lang.Object,int,long,java.lang.String,int,int)>").makeRef();
					units.insertBefore(Jimple.v().newInvokeStmt(
								Jimple.v().newStaticInvokeExpr(mr, args)), s);
		    	}
		    	else if(v.getType() instanceof LongType)
		    	{
		    		args.addLast(v);
		    		SootMethodRef mr = Scene.v().getMethod(
							"<" + observerClass + ": void " + methodname
									+ "(java.lang.Object,int,long,java.lang.String,int,long)>").makeRef();
					units.insertBefore(Jimple.v().newInvokeStmt(
								Jimple.v().newStaticInvokeExpr(mr, args)), s);
		    	}
		    	else//if (v.getType() instanceof ShortType)
		    	{
		    		args.addLast(v);
		    		SootMethodRef mr = Scene.v().getMethod(
							"<" + observerClass + ": void " + methodname
									+ "(java.lang.Object,int,long,java.lang.String,int,short)>").makeRef();
					units.insertBefore(Jimple.v().newInvokeStmt(
								Jimple.v().newStaticInvokeExpr(mr, args)), s);
		    	}
		    }
		    else
		    {
		    	args.addLast(v);
	    		SootMethodRef mr = Scene.v().getMethod(
						"<" + observerClass + ": void " + methodname
								+ "(java.lang.Object,int,long,java.lang.String,int,java.lang.Object)>").makeRef();
				units.insertBefore(Jimple.v().newInvokeStmt(
							Jimple.v().newStaticInvokeExpr(mr, args)), s);
		    }


			

	        
			
// replaced into the call with typed value
//		    Visitor.addCallAccessSPEInstance(sm,units, s, methodname, base, memory, true);
			Visitor.sharedaccessnum++;
		    Visitor.instrusharedaccessnum++;        	
		}
        nextVisitor.visitInstanceFieldRef(sm, units, s, instanceFieldRef, context);
    }

    public void visitStaticFieldRef(SootMethod sm, Chain units, Stmt s, StaticFieldRef staticFieldRef, RefContext context) 
    {      
    	Visitor.totalaccessnum++;
        String sig = staticFieldRef.getField().getDeclaringClass().getName()+"."+staticFieldRef.getField().getName();
		Value memory = StringConstant.v(sig);

		if(!staticFieldRef.getField().isFinal())
        if(Visitor.sharedVariableWriteAccessSet.contains(sig))
		{	
			String methodname = null; 
			Value v=null;
			if (context == RHSContextImpl.getInstance()) 
	        {
				methodname = "readBeforeStatic"; 
				v= ((AssignStmt)s).getLeftOp();
	        }else
	        {
	        	methodname = "writeBeforeStatic";
	        	v= ((AssignStmt)s).getRightOp();
	        }
			
			

			

			LinkedList args = new LinkedList();
			args.addLast(IntConstant.v(getSPEIndex(memory)));// static encoding
			args.addLast(getTIDLocal(sm));// the local is monitor variable (statically injected, dynamically computed)
			args.addLast(StringConstant.v(sm.getDeclaringClass().getJavaPackageName() + sm.getDeclaringClass().getName()));		
			LineNumberTag tag = (LineNumberTag)s.getTag("LineNumberTag");
			if(tag!=null)
			{
				args.addLast(IntConstant.v(tag.getLineNumber()));
			}
			else {
				args.addLast(IntConstant.v(-1));
			}
			
			  if(v.getType() instanceof PrimType)
			    {
			    	Value staticInvoke;

			    	if(v.getType() instanceof BooleanType)
			    	{
			    		args.addLast(v);
						SootMethodRef mr = Scene.v().getMethod(
								"<" + observerClass + ": void " + methodname + "(int,long,java.lang.String,int,boolean)>")
								.makeRef();
						units.insertBefore(Jimple.v().newInvokeStmt(
									Jimple.v().newStaticInvokeExpr(mr, args)), s);
			    	}
			    	else if(v.getType() instanceof ByteType)
			    	{ 
			    		args.addLast(v);
			    		SootMethodRef mr = Scene.v().getMethod(
								"<" + observerClass + ": void " + methodname + "(int,long,java.lang.String,int,byte)>")
								.makeRef();
						units.insertBefore(Jimple.v().newInvokeStmt(
									Jimple.v().newStaticInvokeExpr(mr, args)), s);
			    	}
			    	else if(v.getType() instanceof CharType)
			    	{
			    		args.addLast(v);
			    		SootMethodRef mr = Scene.v().getMethod(
								"<" + observerClass + ": void " + methodname + "(int,long,java.lang.String,int,char)>")
								.makeRef();
						units.insertBefore(Jimple.v().newInvokeStmt(
									Jimple.v().newStaticInvokeExpr(mr, args)), s);
			    	}
			    	else if(v.getType() instanceof DoubleType)
			    	{
			    		args.addLast(v);
			    		SootMethodRef mr = Scene.v().getMethod(
								"<" + observerClass + ": void " + methodname + "(int,long,java.lang.String,int,double)>")
								.makeRef();
						units.insertBefore(Jimple.v().newInvokeStmt(
									Jimple.v().newStaticInvokeExpr(mr, args)), s);
			    	}
			    	else if(v.getType() instanceof FloatType)
			    	{
			    		args.addLast(v);
			    		SootMethodRef mr = Scene.v().getMethod(
								"<" + observerClass + ": void " + methodname + "(int,long,java.lang.String,int,float)>")
								.makeRef();
						units.insertBefore(Jimple.v().newInvokeStmt(
									Jimple.v().newStaticInvokeExpr(mr, args)), s);
			    	}
			    	else if(v.getType() instanceof IntType)
			    	{
			    		args.addLast(v);
			    		SootMethodRef mr = Scene.v().getMethod(
								"<" + observerClass + ": void " + methodname + "(int,long,java.lang.String,int,int)>")
								.makeRef();
						units.insertBefore(Jimple.v().newInvokeStmt(
									Jimple.v().newStaticInvokeExpr(mr, args)), s);
			    	}
			    	else if(v.getType() instanceof LongType)
			    	{
			    		args.addLast(v);
			    		SootMethodRef mr = Scene.v().getMethod(
								"<" + observerClass + ": void " + methodname + "(int,long,java.lang.String,int,long)>")
								.makeRef();
						units.insertBefore(Jimple.v().newInvokeStmt(
									Jimple.v().newStaticInvokeExpr(mr, args)), s);
			    	}
			    	else//if (v.getType() instanceof ShortType)
			    	{
			    		args.addLast(v);
			    		SootMethodRef mr = Scene.v().getMethod(
								"<" + observerClass + ": void " + methodname + "(int,long,java.lang.String,int,short)>")
								.makeRef();
						units.insertBefore(Jimple.v().newInvokeStmt(
									Jimple.v().newStaticInvokeExpr(mr, args)), s);
			    	}
			    }
			    else
			    {
			    	
			    	args.addLast(v);
			    	SootMethodRef mr = Scene.v().getMethod(
							"<" + observerClass + ": void " + methodname + "(int,long,java.lang.String,int,java.lang.Object)>")
							.makeRef();
					units.insertBefore(Jimple.v().newInvokeStmt(
								Jimple.v().newStaticInvokeExpr(mr, args)), s);
			    }

			    

				
//		    Visitor.addCallAccessSPEStatic(sm,units, s, methodname, memory, true);
		    Visitor.sharedaccessnum++;
		    Visitor.instrusharedaccessnum++;        	
		}
        nextVisitor.visitStaticFieldRef(sm, units, s, staticFieldRef, context);
    }
}
