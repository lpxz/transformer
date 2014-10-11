package edu.hkust.leap.syncMethodDesugar;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import soot.Scene;
import soot.SootClass;
import soot.SootMethodRef;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.jimple.IdentityStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.toolkits.callgraph.Units;
import soot.util.Chain;

public class InstrumentUtility {

     static public String observerClass;
     public static void setObserverClass(String para)
     {
    	 observerClass = para;
     }
    
    static private int counter = 0;
    
    public static int getAndIncCounter() {
        return counter++;
    }
    
	    public static void addCallLast(Chain units, String methodName) {
	        SootMethodRef mr;

	        mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName + "()>").makeRef();
	        units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr)), units.getLast());
	    }

	    public static void addCallLastWithObject(Chain units, String methodName, Value v) {
	        SootMethodRef mr;
	        mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName + "(java.lang.Object)>").makeRef();
	        units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, v)), units.getLast());
	    }


	    public static void addCall(Chain units, Stmt s, String methodName, boolean before) {
	        SootMethodRef mr;

	        mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName + "()>").makeRef();
	        if (before) {
	            units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr)), s);
	        } else {
	            units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr)), s);
	        }
	    }

	    public static void addCallWithObject(Chain units, Stmt s, String methodName, Value v, boolean before) {
	        SootMethodRef mr;

	         

	        mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName + "(java.lang.Object)>").makeRef();
	        if (before) {
	            units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr,  v)), s);	            
	        } else {
	            units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr,  v)), s);
	        }
	    }

	    public static Stmt addCallWithInt(Chain units, Stmt s, String methodName, Value v, boolean before) {
	        SootMethodRef mr;
	        Stmt ret = null; 

	         

	        mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName + "(int)>").makeRef();
	        if (before) {
	        	ret= Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr,  v));
	            units.insertBefore(ret, s);
	            return ret;
	        } else {
	        	ret = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr,  v));
	        	
	            units.insertAfter(ret, s);
	            return ret;
	        }
	    }

	    public static void addCallWithObjectInt(Chain units, Stmt s, String methodName, Value v1, Value v2, boolean before) {
	        SootMethodRef mr;

	         

	        LinkedList args = new LinkedList();
	        args.addLast(v1);
	        args.addLast(v2);
	        mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName + "(java.lang.Object,int)>").makeRef();
	        if (before) {
	            units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        } else {
	            units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        }
	    }

	    public static void addCallWithIntObject(Chain units, Stmt s, String methodName, Value v1, Value v2, boolean before) {
	        SootMethodRef mr;

	         

	        LinkedList args = new LinkedList();
	        args.addLast(v1);
	        args.addLast(v2);
	        mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName + "(int,java.lang.Object)>").makeRef();
	        if (before) {
	            units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        } else {
	            units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        }
	    }
	    
	    public static void addCallWithIntString(Chain units, Stmt s, String methodName, Value v1, Value v2, boolean before) {
	        SootMethodRef mr;

	         

	        LinkedList args = new LinkedList();
	        args.addLast(v1);
	        args.addLast(v2);
	        mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName + "(int,java.lang.String)>").makeRef();
	        if (before) {
	            units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        } else {
	            units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        }
	    }
	    
	    public static Stmt addCallWithIntStringStringString(Chain units, Stmt s, String methodName, Value v1, Value v2,  Value v3, Value v4, boolean before) {
	        SootMethodRef mr;     
	        Stmt ret = null;

	        LinkedList args = new LinkedList();
	        args.addLast(v1);
	        args.addLast(v2);
	        args.addLast(v3);
	        args.addLast(v4);
	        mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName + "(int,java.lang.String,java.lang.String,java.lang.String)>").makeRef();
	        if (before) {
	        	ret = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args));
	            units.insertBefore(ret, s);
	            return ret;
	        } else {
	        	ret = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args));
	            units.insertAfter(ret, s);
	            return ret;
	        }
	    }
	    

	    public static void addCallWithObjectObject(Chain units, Stmt s, String methodName, Value v1, Value v2, boolean before) {
	        SootMethodRef mr;

	         

	        LinkedList args = new LinkedList();
	        args.addLast(v1);
	        args.addLast(v2);
	        mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName + "(java.lang.Object,java.lang.Object)>").makeRef();
	        if (before) {
	            units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        } else {
	            units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        }
	    }

	    public static void addCallWithObjectString(Chain units, Stmt s, String methodName, Value v1, Value v2, boolean before) {
	        SootMethodRef mr;

	         

	        LinkedList args = new LinkedList();
	        args.addLast(v1);
	        args.addLast(v2);
	        mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName + "(java.lang.Object,java.lang.String)>").makeRef();
	        if (before) {
	            units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        } else {
	            units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        }
	    }

	    public static void addCallWithObjectStringStringInt(Chain units, Stmt s, String methodName,
	                                                    Value v1, Value v2, Value v3, Value v4, boolean before) {
	        SootMethodRef mr;

	         

	        LinkedList args = new LinkedList();
	        args.addLast(v1);
	        args.addLast(v2);
	        args.addLast(v3);
	        args.addLast(v4);
	        mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName
	                + "(java.lang.Object,java.lang.String,java.lang.String,int)>").makeRef();
	        if (before) {
	            units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        } else {
	            units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        }
	    }

	    public static void addCallWithObjectStringString(Chain units, Stmt s, String methodName,
	                                                 Value v1, Value v2, Value v3, boolean before) {
	        SootMethodRef mr;

	         

	        LinkedList args = new LinkedList();
	        args.addLast(v1);
	        args.addLast(v2);
	        args.addLast(v3);
	        mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName
	                + "(java.lang.Object,java.lang.String,java.lang.String)>").makeRef();
	        if (before) {
	            units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        } else {
	            units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        }
	    }
	    
	    public static void addCallWithString(Chain units, Stmt s, String methodName,
                Value v1, boolean before) {
				SootMethodRef mr;
				
				 
				
				LinkedList args = new LinkedList();
				args.addLast(v1);
				mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName
				+ "(java.lang.String)>").makeRef();
				if (before) {
				units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
				} else {
				units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
				}
        }
	    
	    
//	    public static void addCallWithStringInt(Chain units, Stmt s, String methodName,
//                Value v1, Value v2, boolean before) {
//				SootMethodRef mr;
//				
//				 
//				
//				LinkedList args = new LinkedList();
//				args.addLast(v1);
//				args.addLast(v2);
//				mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName
//				+ "(java.lang.String,int)>").makeRef();
//				if (before) {
//				units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
//				} else {
//				units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
//				}
//        }
	    
	    public static void addCallWithStringIntInt(Chain units, Stmt s, String methodName,
                Value v1, Value v2, Value v3, boolean before) {
				SootMethodRef mr;
				
				 
				
				LinkedList args = new LinkedList();
				args.addLast(v1);
				args.addLast(v2);
				args.addLast(v3);
				mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName
				+ "(java.lang.String,int,int)>").makeRef();
				if (before) {
				units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
				} else {
				units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
				}
        }
	    
	    
	    
	    
	    public static void addCallWithStringString(Chain units, Stmt s, String methodName,
                Value v1, Value v2, boolean before) {
				SootMethodRef mr;
					
				LinkedList args = new LinkedList();
				args.addLast(v1);
				args.addLast(v2);
				mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName
				+ "(java.lang.String,java.lang.String)>").makeRef();
				if (before) {
				units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
				} else {
				units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
				}
        }
	    	    
	    
	    
	    public static void addCallWithSixStrings(Chain units, Stmt s, String methodName,
                Value v1, Value v2, Value v3, Value v4, Value v5, Value v6, boolean before) {
				SootMethodRef mr;
					
				LinkedList args = new LinkedList();
				args.addLast(v1);
				args.addLast(v2);
				args.addLast(v3);
				args.addLast(v4);
				args.addLast(v5);
				args.addLast(v6);
				mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName
				+ "(java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String)>").makeRef();
				if (before) {
				units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
				} else {
				units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
				}
        }


	    public static void addCallWithObjectIntString(Chain units, Stmt s, String methodName, Value v1, Value v2, Value v3, boolean before) {
	        SootMethodRef mr;

	         

	        LinkedList args = new LinkedList();
	        args.addLast(v1);
	        args.addLast(v2);
	        args.addLast(v3);
	        mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName
	                + "(java.lang.Object,int,java.lang.String)>").makeRef();
	        if (before) {
	            units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        } else {
	            units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        }
	    }

	    public static void addCallWithIntInt(Chain units, Stmt s, String methodName, Value v1, Value v2, boolean before) {
	        SootMethodRef mr;

	         

	        LinkedList args = new LinkedList();
	        args.addLast(v1);
	        args.addLast(v2);
	        mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName + "(int,int)>").makeRef();
	        if (before) {
	            units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        } else {
	            units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        }
	    }

	    public static void addCallWithObjectIntInt(Chain units, Stmt s, String methodName, Value v1, Value v2, Value v3, boolean before) {
	        SootMethodRef mr;

	         

	        LinkedList args = new LinkedList();
	        args.addLast(v1);
	        args.addLast(v2);
	        args.addLast(v3);
	        mr = Scene.v().getMethod("<" + observerClass + ": void " + methodName + "(java.lang.Object,int,int)>").makeRef();
	        if (before) {
	            units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        } else {
	            units.insertAfter(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mr, args)), s);
	        }
	    }

	    protected static boolean isThreadSubType(SootClass c) {
	        if (c.getName().equals("java.lang.Thread"))
	            return true;
	        if (!c.hasSuperclass()) {
	            return false;
	        }
	        return isThreadSubType(c.getSuperclass());
	    }

	    protected static boolean isRunnableSubType(SootClass c) {
	        if (c.implementsInterface("java.lang.Runnable"))
	            return true;
	        if (c.hasSuperclass())
	            return isRunnableSubType(c.getSuperclass());
	        return false;
	    }

	    protected boolean isSubClass(SootClass c, String typeName) {
	        if (c.getName().equals(typeName))
	            return true;
	        if (c.implementsInterface(typeName))
	            return true;
	        if (!c.hasSuperclass()) {
	            return false;
	        }
	        return isSubClass(c.getSuperclass(), typeName);
	    }

	    
	    
		  public static Stmt getFirstNonIdentityStmt( Chain units) // getLastIdentity does not make sense because some static method does not have identity stmt.
		    {
		        Iterator it = units.iterator();
		        Object o = null;
		        while (it.hasNext())
		            if (!((o = it.next()) instanceof IdentityStmt))
		                break;
		        if (o == null)
		            throw new RuntimeException("no non-id statements!");
		        return (Stmt)o;
		    }
		  
		  public static Set<Unit> retStmts = new HashSet<Unit>();
		  public static Set<Unit> getReturnStmts( Chain<Unit> units) // getLastIdentity does not make sense because some static method does not have identity stmt.
		    {
			  
		       retStmts.clear();		       
			   for(Unit  unit : units)
			   {
				   if(unit instanceof ReturnStmt || unit instanceof ReturnVoidStmt)
				   {
					   retStmts.add(unit);
				   }
			   }
			   return retStmts;
		       
		        
		    }

		/**
		 * @param end
		 * @return
		 */
		public static boolean jumpAwayFromIt(Unit end) {
			if(end.branches() || end instanceof ReturnStmt || end instanceof ReturnVoidStmt || end instanceof ThrowStmt)
      			return true;
			else
				return false;
		}

		
		/**
		 * @param units
		 * @param branchEnd
		 * @return
		 */
//		public static Stmt straightUp2LastObserverEvent(Chain units, Unit branchEnd) {
//			Stmt ret = (Stmt) branchEnd;
//			Object prec =  units.getPredOf(ret); 
//			while(prec!=null&& ! !jumpAwayFromIt((Unit)prec)&& prec.toString().contains(observerClass))
//			{
//				ret = (Stmt)prec;
//				prec =  units.getPredOf(ret); 
//			}
//			return ret;
//		}
//		  
		
//		public static Stmt straightDown2LastObserverEvent(Chain units, Unit branchEnd) {
//			Stmt ret = (Stmt) branchEnd;
//			Object succ =  units.getSuccOf(ret); 
//			
//			while(succ!=null&& !jumpAwayFromIt((Unit)succ) && succ.toString().contains(observerClass))
//			{
//				ret = (Stmt)succ;
//				succ =  units.getSuccOf(ret); 
//			}
//			return ret;
//		}
		
		 public static Unit insertGotoAfter(Chain unitChain, Unit node, Unit target) {
			    Unit newGoto = Jimple.v().newGotoStmt(target);
			    unitChain.insertAfter(newGoto, node);
			    return newGoto;
			  }
		 
		  public static void redirectBranch(Unit node, Unit oldTarget, Unit newTarget) {
			    Iterator targetIt = node.getUnitBoxes().iterator();
			    while (targetIt.hasNext()) {
			      UnitBox targetBox = (UnitBox)targetIt.next();
			      Unit target = targetBox.getUnit();
			      if (target == oldTarget)
			        targetBox.setUnit(newTarget);
			    }
			  }
		  
	    
}
