package edu.hkust.leap.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import edu.hkust.leap.transformer.Visitor;


public class Parameters 
{
	//====================transformer.parameter
	public static int LOOP_STMT_COUNT = 0;
	public static final String CRASH_ANNOTATION="Crashed_with";
	public static final String IntermediateCP = "IntermediateCP";
	public static final String READ = "READ";
	public static final String WRITE = "WRITE";


//	public static final String CATCH_EXCEPTION_SIG = "<edu.hkust.leap.monitor.Monitor: void crashed(java.lang.Throwable)>";
	public static String CATCH_EXCEPTION_SIG()
	{
	return "<"+Visitor.observerClass+": void crashed(java.lang.Throwable)>";
	}

	public static String OUTPUT_JIMPLE ="jimple";
	public static String PHASE_RECORD ="record";
	public static String PHASE_REPLAY ="replay";
	

	public static boolean shouldInstru = false;
	
	
	public static boolean isMethodPublic=false;
	public static boolean isMethodStatic = false;
	public static boolean isMethodRunnable = false;
	public static boolean isMethodSynchronized = false;
	public static boolean isMethodMain = false;
	
	public static boolean isRecord=true;
	public static boolean isReplay=false;
//	public static boolean isOutputJimple=true;// false;
	
	public static boolean isInnerClass = false;
	public static boolean isAnonymous = false;
	public static boolean isStmtInLoop = false;
		
	public static int lockCount=0;
	
	
	
	//=====================================record.parameter
	public static String OrderDataDir= "OrderData";
	//=====================================replay.parameter
	public static String SRC_DIR ="src";
//	public static int lockCount=0;
	public static String MAIN_THREAD_NAME = "leap-main";
	public static String PHASE_RECORDLP ="recordLP";
	public static String PHASE_REPLAYLP ="replayLP";
	public static String O2STMT= "o2stmt";
	public static String RECORDCLASS= "edu.hkust.leap.monitor.RecordMonitor";
	public static String REPLAYCLASS= "edu.hkust.leap.monitor.ReplayMonitor";
	
	public static String RECORDCLASSLP= "edu.hkust.leap.monitor.RecordMonitorLP";
	public static String REPLAYCLASSLP= "edu.hkust.leap.monitor.ReplayMonitorLP";
	
	public static String HASHPUT= "<java.util.HashMap: java.lang.Object put(java.lang.Object,java.lang.Object)>";
	public static String HASHGET= "<java.util.HashMap: java.lang.Object get(java.lang.Object)>";
	public static  String INTVALUE ="<java.lang.Integer: int intValue()>";
	public static  String VALUEOF = "<java.lang.Integer: java.lang.Integer valueOf(int)>";
	public static String arrayIndex = "ARRAYINDEX";
	public static String fakedJDKDataStructureField= "JDKDataStructureField";
	
	public static HashMap<String, HashSet<String>> fakedRead = new HashMap<String, HashSet<String>>();
	public static HashMap<String, HashSet<String>> fakedWrite = new HashMap<String, HashSet<String>>();

	static
	{
		//=======================please run the transformPreprocess to extract the collection used in the application.
		insert2(fakedRead, "java.util.Vector", "get");
		insert2(fakedRead, "java.util.HashMap", "get");
		insert2(fakedRead, "java.util.HashMap", "size");
		insert2(fakedRead, "java.util.HashSet", "size");
		
		//==================================================
		insert2(fakedWrite, "java.util.Vector", "add");
		insert2(fakedWrite, "java.util.Vector", "set");		
		insert2(fakedWrite, "java.util.HashMap", "put");
		insert2(fakedWrite, "java.util.HashSet", "add");
		insert2(fakedWrite, "java.util.HashSet", "remove");
		
	
		insert2(fakedRead,"java.util.ArrayList","get");
		insert2(fakedWrite,"java.util.Vector","addElement");
		insert2(fakedRead,"java.util.Vector","isEmpty");
		insert2(fakedWrite,"java.util.ArrayList","remove");
		insert2(fakedWrite,"java.util.Vector","remove");
		insert2(fakedRead,"java.util.Vector","elementAt");
		insert2(fakedWrite,"java.util.Vector","add");
		insert2(fakedRead,"java.util.ArrayList","size");
		insert2(fakedRead,"java.util.Collection","iterator");
		insert2(fakedWrite,"java.util.ArrayList","add");
		insert2(fakedRead,"java.util.AbstractList","iterator");
		insert2(fakedRead,"java.util.Vector","size");
		insert2(fakedWrite,"java.util.ArrayList","clear");
		insert2(fakedRead,"java.util.Vector","firstElement");
		insert2(fakedRead,"java.util.Vector","elements");
		//Iterator hasNext next remove
		insert2(fakedRead,"java.util.Iterator","hasNext");
		insert2(fakedWrite,"java.util.Iterator","next");
		insert2(fakedWrite,"java.util.Iterator","remove");
		
		
		
		
		
	}
	public static void insert2(HashMap<String, HashSet<String>> fakedRead2,
			String classname, String methodname) {
		HashSet hs = fakedRead2.get(classname);
		if(hs==null)
		{
			hs = new HashSet<String>();
			fakedRead2.put(classname, hs);
		}
		hs.add(methodname);		
	}
	public static String JDKOpType(String classname,String methodname )
	{
		if(contains(fakedRead, classname, methodname))
		{
			return READ;
		}
		else if (contains(fakedWrite, classname, methodname)) {
			return WRITE;
		}
		else {
			return "";// I do not want to see nullexception!
		}
	}
	private static boolean contains(
			HashMap<String, HashSet<String>> fakedRead, String classname,
			String methodname) {
		HashSet readOps =fakedRead.get(classname);
		if(readOps==null)
		{
			return false;
		}
		else {
			if(!readOps.contains(methodname))
			{
				return false;
			}
			else {
				return true;
			}
		}	
		
	}
	public static void main(String[] args)
	{
		String str = JDKOpType("java.util.Vector", "set");
		System.out.println(str);
		
	}
	
	
}
