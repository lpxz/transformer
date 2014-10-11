package simple;
public class ExampleThread extends Thread                                  
{                                                                            
                                                   
                                                                  
    public void run()                                               
    {
    	synchronized (this) {
    		int x = Example.haha;
       	 
        	Example.haha = x+1;
		}
    	
    	
    }                                                                      
}
