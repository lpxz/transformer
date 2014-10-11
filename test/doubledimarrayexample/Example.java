package doubledimarrayexample;
//import edu.ust.record.output;
public class Example 
{
	public static int[][] intarray = new int[2][2];
    public static void main (String [] args) 
    {
    	//  output.out();
    	intarray[0][0] =1;
    	intarray[0][1]=1;
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