package doubledimarrayexample;
public class ExampleThread extends Thread                                  
{                                                                            
                                                                    
    public void run()                                               
    {           
    	try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Example.intarray[0][0]=0;    	
    }                                                                      
}
