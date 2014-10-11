package vectorexample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

//import edu.ust.record.output;
public class Example 
{
	public static Vector  intarray = new  Vector();
    public static void main (String [] args) 
    {
    	//  output.out();
    	intarray.add(1);
    	intarray.add(2);
    
    	
        Thread t1 = new ExampleThread();    
        Thread t2 = new ExampleThread2();   
       
		t1.start();
		 t2.start ();
		
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

    	}
}