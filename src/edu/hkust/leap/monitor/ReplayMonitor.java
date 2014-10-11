package edu.hkust.leap.monitor;

import java.util.HashMap;

public class ReplayMonitor {
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
    
    public static void readBeforeArrayElem(Object o, int iid,long id, String classname , int lineNO) {		
    }
    public static void writeBeforeArrayElem(Object o, int iid,long id, String classname , int lineNO) {
    }
    
    public static void readBeforeInstance(Object o, int iid,long id, String classname , int lineNO) {		
    }
    public static void writeBeforeInstance(Object o, int iid,long id, String classname , int lineNO) {
    }
    public static void readBeforeFakedInstance(Object o, int iid,long id, String classname , int lineNO) {		
    }
    public static void writeBeforeFakedInstance(Object o, int iid,long id,String classname , int lineNO) {
    }

    public static void readBeforeStatic(int iid,long id, String classname , int lineNO) {		
    }
    public static void writeBeforeStatic(int iid,long id, String classname , int lineNO) {
    }
    
    public static void enterMonitorAfter( int iid,long id) {
    }
    public static void exitMonitorBefore(int iid,long id) {
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
