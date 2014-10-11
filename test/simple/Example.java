package simple;

import java.util.Random;

//import edu.ust.record.output;
public class Example 
{
	public static int haha = 0;
    public static void main (String [] args) 
    {	

        Thread t1 = new ExampleThread();    
        Thread t2 = new ExampleThread();
		
        
        funcA();
        
        t2.start ();
		t1.start();
		
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
    	}
    
    public static  synchronized void funcA()
    {
    	Random rand = new Random();
    	if(rand.nextInt(5)>3)
    	 System.out.println("hahhe");
    	else {
			System.out.println("bad");
			throw new RuntimeException();
			
		}
    }
}