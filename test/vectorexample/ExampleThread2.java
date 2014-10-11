package vectorexample;
public class ExampleThread2 extends Thread                                  
{                                                                            
                                                                    
    public void run()                                               
    {      
    	Integer v = (Integer) Example.intarray.get(0);  	
    	System.out.println(8/v.intValue());
    }                                                                      
}
