package edu.hkust.leap.monitor;

import java.util.HashMap;

public class RecordMonitor {
	public static HashMap o2stmt = new HashMap();

	public synchronized static void crashed(Throwable crashedException) {
	}

	public static void threadStartRun(long threadId)
	{
	}
	public static void threadExitRun(long threadId)
	{	
	}
	public static void mainThreadStartRun(long threadId,String methodName, String[] args)
	{
	}
	public static void mainThreadStopRun(long threadId,String methodName, String[] args)
	{}
	public static void accessSPE(int iid,long id)
	{
		
	}
	
	public static boolean youMayCrash(String methodName, Object objects[],
			String types[],long threadId) {
		return true;
	}
	public static void youAreOK() {
	}
    
    public static void waitBefore(int iid, long id)
    {
    }
    public static void waitAfter(Object o,int iid, long id)
    {	
    }
    public static void notifyBefore(Object o,int iid, long id)
    {	
    }
    public static void notifyAllBefore(Object o,int iid, long id)
    {	
    }
    
    
    public static void readBefore(int iid,long id) {		
    }
    public static void writeBefore(int iid,long id) {
    }

    
    //TODO begin
//  *** "java.lang.Object[].ARRAYINDEX"  has little distinguishing power.
//  *** "java.lang.Object[][].ARRAYINDEX"    
    //deprecated
//  public static void readBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex) {		
//  }
//  public static void writeBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex) {
//  }
  
    public static void readBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex,boolean value) {		
    }
    public static void writeBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex, boolean value) {
    }
    
    public static void readBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex,byte value) {		
    }
    public static void writeBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex, byte value) {
    }
    
    public static void readBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex,char value) {		
    }
    public static void writeBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex, char value) {
    }
    
    public static void readBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex,double value) {		
    }
    public static void writeBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex, double value) {
    }
    
    public static void readBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex,float value) {		
    }
    public static void writeBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex, float value) {
    }
    
    public static void readBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex,int value) {		
    }
    public static void writeBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex, int value) {
    }
    
    public static void readBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex,long value) {		
    }
    public static void writeBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex, long value) {
    }
    
    public static void readBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex,short value) {		
    }
    public static void writeBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex, short value) {
    }
    
    public static void readBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex,Object value) {		
    }
    public static void writeBeforeArrayElem(Object o, int iid,long id, String classname, int lineNO, int arrayindex,  Object value) {
    }
    
    
  
    // deprecated
//    public static void readBeforeInstance(Object o, int iid,long id, String classname, int lineNO) {		
//    }
//    public static void writeBeforeInstance(Object o, int iid,long id, String classname, int lineNO) {
//    }
    
    public static void readBeforeInstance(Object o, int iid,long id, String classname, int lineNO,boolean value) {		
    }
    public static void writeBeforeInstance(Object o, int iid,long id, String classname, int lineNO, boolean value) {
    }
    
    
    public static void readBeforeInstance(Object o, int iid,long id, String classname, int lineNO,byte value) {		
    }
    public static void writeBeforeInstance(Object o, int iid,long id, String classname, int lineNO, byte value) {
    }
    
    public static void readBeforeInstance(Object o, int iid,long id, String classname, int lineNO,char value) {		
    }
    public static void writeBeforeInstance(Object o, int iid,long id, String classname, int lineNO, char value) {
    }
    
    
    public static void readBeforeInstance(Object o, int iid,long id, String classname, int lineNO,double value) {		
    }
    public static void writeBeforeInstance(Object o, int iid,long id, String classname, int lineNO, double value) {
    }
    
    public static void readBeforeInstance(Object o, int iid,long id, String classname, int lineNO,float value) {		
    }
    public static void writeBeforeInstance(Object o, int iid,long id, String classname, int lineNO, float value) {
    }
    
    public static void readBeforeInstance(Object o, int iid,long id, String classname, int lineNO,int value) {		
    }
    public static void writeBeforeInstance(Object o, int iid,long id, String classname, int lineNO, int value) {
    }
    
    public static void readBeforeInstance(Object o, int iid,long id, String classname, int lineNO,long value) {		
    }
    public static void writeBeforeInstance(Object o, int iid,long id, String classname, int lineNO, long value) {
    }
    
    public static void readBeforeInstance(Object o, int iid,long id, String classname, int lineNO,short value) {		
    }
    public static void writeBeforeInstance(Object o, int iid,long id, String classname, int lineNO, short value) {
    }
    
    public static void readBeforeInstance(Object o, int iid,long id, String classname, int lineNO,Object value) {		
    }
    public static void writeBeforeInstance(Object o, int iid,long id, String classname, int lineNO, Object value) {
    }
    
    
    
    //deprecated
//    public static void readBeforeStatic(int iid,long id, String classname, int lineNO) {		
//    }
//    public static void writeBeforeStatic(int iid,long id, String classname, int lineNO) {
//    }
    
    public static void readBeforeStatic(int iid,long id, String classname, int lineNO,boolean value) {		
    }
    public static void writeBeforeStatic(int iid,long id, String classname, int lineNO,boolean value) {
    }
    
    public static void readBeforeStatic(int iid,long id, String classname, int lineNO,byte value) {		
    }
    public static void writeBeforeStatic(int iid,long id, String classname, int lineNO,byte value) {
    }
    
    public static void readBeforeStatic(int iid,long id, String classname, int lineNO,char value) {		
    }
    public static void writeBeforeStatic(int iid,long id, String classname, int lineNO,char value) {
    }
    
    public static void readBeforeStatic(int iid,long id, String classname, int lineNO,double value) {		
    }
    public static void writeBeforeStatic(int iid,long id, String classname, int lineNO,double value) {
    }
    
    
    public static void readBeforeStatic(int iid,long id, String classname, int lineNO,float value) {		
    }
    public static void writeBeforeStatic(int iid,long id, String classname, int lineNO,float value) {
    }
    
    public static void readBeforeStatic(int iid,long id, String classname, int lineNO,int value) {		
    }
    public static void writeBeforeStatic(int iid,long id, String classname, int lineNO,int value) {
    }
    
    public static void readBeforeStatic(int iid,long id, String classname, int lineNO,long value) {		
    }
    public static void writeBeforeStatic(int iid,long id, String classname, int lineNO,long value) {
    }
    
    public static void readBeforeStatic(int iid,long id, String classname, int lineNO,short value) {		
    }
    public static void writeBeforeStatic(int iid,long id, String classname, int lineNO,short value) {
    }
    
    
    public static void readBeforeStatic(int iid,long id, String classname, int lineNO,Object value) {		
    }
    public static void writeBeforeStatic(int iid,long id, String classname, int lineNO,Object value) {
    }
    
    
    
    
    
    
    
    
    
    
    //TODO ends
    
    
    
    
    
    
    
//    public static void readBeforeFakedInstance(Object o, int iid,long id, String classname , int lineNO) {		
//    }
//    public static void writeBeforeFakedInstance(Object o, int iid,long id, String classname , int lineNO) {
//    }
    

    public static void enterMonitorAfter( int iid,long id) {
    }
    public static void exitMonitorBefore(int iid,long id) {
//    	 enterMonitorAfter(iid, id);
    	 
    }
    public static void enterMonitorBefore( int iid,long id) {
    }
    public static void exitMonitorAfter(int iid,long id) {
    }
    public static void enterMonitorBefore(Object o, int iid,long id) {
    }
    public static void enterMonitorAfter(Object o, int iid,long id) {
    }
    public static void exitMonitorBefore(Object o,int iid,long id) {
    }
    public static void exitMonitorAfter(Object o,int iid,long id) {
    }
    public static void startRunThreadBefore(Thread t,long id)
    {	
    }
    public static void joinRunThreadAfter(Thread t,long id)
    {
    }
}
