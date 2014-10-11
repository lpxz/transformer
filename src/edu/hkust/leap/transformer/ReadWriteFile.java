/**
 * 
 */
package edu.hkust.leap.transformer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Peng Liu from Purdue
 *
 * <lpxz.ust.hk@gmail.com>
 */
public class ReadWriteFile {
	
	public static  void write2File(String towrite, String file) {
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter(file);
			  BufferedWriter out = new BufferedWriter(fstream);
			  out.write(towrite);
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
           
	}
	
	public static  void write2File(List<String> towrite, String file) {
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter(file);
			  BufferedWriter out = new BufferedWriter(fstream);
			  for(String tmp : towrite)
			  {
				  out.write(tmp+"\n");
			  }
			  
			  //Close the output stream
			  out.close();
			  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
			  }
           
	}
	
	
	public static Object load(String filename)
	{
		Object obj = null;
		ObjectInputStream in =null;
		try
    	{
			in = new ObjectInputStream(
					new FileInputStream(filename));
			obj = in.readObject();
			System.err.println("load object from " + filename);
			
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}finally
    	{
    		try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return obj;
    	}
		
	}
	
	
	public static List<String> readLineByLine(File file)
	throws IOException
	{

		List<String> ret = new ArrayList<String>(); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));
		try {			

			String line;
//			StringBuffer buffer = new StringBuffer();
//			  buffer.delete(0, buffer.length());
			while (true) {
				line = reader.readLine();
//				System.out.println(line);
				if(line == null) break;
				ret.add(line);
			}
			
		} finally {
			reader.close();
			return ret;
		}
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
