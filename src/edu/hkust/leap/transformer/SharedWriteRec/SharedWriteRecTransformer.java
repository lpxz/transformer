package edu.hkust.leap.transformer.SharedWriteRec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

//import edu.hkust.leap.tloax.XFieldThreadEscapeAnalysis;
import edu.hkust.leap.transformer.Visitor;
import edu.hkust.leap.transformer.AddInterestEvent.AddInterestEventRV;
import edu.hkust.leap.transformer.AddInterestEvent.AddInterestEventVisitor;
import edu.hkust.leap.utils.Parameters;
import edu.hkust.leap.utils.Util;

import soot.Body;
import soot.IntType;
import soot.Local;
import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.toolkits.thread.ThreadLocalObjectsAnalysis;
import soot.jimple.toolkits.thread.mhp.UnsynchronizedMhpAnalysis;
import soot.jimple.toolkits.thread.mhp.pegcallgraph.PegCallGraph;
import soot.util.Chain;

public class SharedWriteRecTransformer extends SceneTransformer
{
	private   Visitor visitor;
    public  void setVisitor(Visitor pvisitor) {
        visitor = pvisitor;
    }
    
    
    public SharedWriteRecTransformer()
	{
        SharedWriteRecRV vv = new SharedWriteRecRV(null);
        SharedWriteRecVisitor pv = new SharedWriteRecVisitor(vv);
        vv.setNextVisitor(pv);
        setVisitor(pv);
	}
    
	protected void internalTransform(String pn, Map map)
	{
		//SynchObliviousMhpAnalysis
		Visitor.tlo = new ThreadLocalObjectsAnalysis(new UnsynchronizedMhpAnalysis());
		//Visitor.ftea = new XFieldThreadEscapeAnalysis();
		//Visitor.pecg = new PegCallGraph(Scene.v().getCallGraph());
		
		Iterator<SootClass> classIt = Scene.v().getApplicationClasses().iterator();
		while (classIt.hasNext()) 
		{
			SootClass sc =  classIt.next();
			String scname = sc.getName();
			//System.out.println("scname: "+scname);

			if(!Util.shouldInstruThisClass(scname)) 
				continue;
		         		        	  		       		        	  
			Iterator<SootMethod> methodIt = sc.getMethods().iterator();
     	  
			while (methodIt.hasNext()) 
			{
				SootMethod sm = methodIt.next();	

				String smname = sm.getName();
				if(!Util.shouldInstruThisMethod(smname))
					continue;
				
				if(sm.isAbstract() || sm.isNative())
					continue;

				try
				{
					Body body = sm.retrieveActiveBody();
					Chain units = body.getUnits();
			        visitor.visitMethodBegin(sm, units);
			        Iterator stmtIt = units.snapshotIterator();
			        while (stmtIt.hasNext()) {
			            Stmt s = (Stmt) stmtIt.next();
//			            System.out.println(s);
			            
			            visitor.visitStmt(sm, units, s);
			        }
			        visitor.visitMethodEnd(sm, units);
			        body.validate();
				}catch(Exception e)
				{
					continue;
				}
			}
		}
	}
}
