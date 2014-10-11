package doubledimarrayexample;
public class ExampleThread2 extends Thread                                  
{                                                                            
                                                                    
    public void run()                                               
    {      
    	int v = Example.intarray[0][0];  	
    	System.out.println(8/v);
    }                                                                      
}
