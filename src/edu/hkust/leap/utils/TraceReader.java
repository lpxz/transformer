package edu.hkust.leap.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.GZIPInputStream;





public class TraceReader {

	//public static  HashMap<String,Vector<Long>> objectMap = null;
	public static Vector<Long>[] accessVector = null;
	public static HashMap<String,Long> threadNameToIdMap = new HashMap<String,Long>();
//	public static Vector<Long> nanoTimeDataVec = null;
//	public static Vector<Long> nanoTimeThreadVec = null;
//	
	static Date traceFileDate;



	/**
	 * Read traceItem from serialized information in file
	 * 
	 */
	public static void readTrace(int type, String traceFileName) throws Exception{

		Reader fr = null;
		//Object object=null;
		try {
			File traceFile = new File(traceFileName);
			

			if (traceFileName.endsWith(".gz")) {
				fr = new InputStreamReader(new GZIPInputStream(
						new FileInputStream(traceFile)));
			} else {
				fr = new FileReader(traceFileName);
			}
		
			switch (type)
			{
				case 1:
					accessVector = (Vector<Long>[]) Serializer.loadObject(fr);
					//printAccessVector();
					break;
				case 2:
					threadNameToIdMap = (HashMap<String,Long>) Serializer.loadObject(fr);
					break;
		
				default:
					break;
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Date getTraceFileDate() {
		return traceFileDate;
	}
}
