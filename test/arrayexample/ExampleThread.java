package arrayexample;
public class ExampleThread extends Thread                                  
{                                                                            
                                                                    
    public void run()                                               
    {           
    	try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	callee();
    	
    }

	private synchronized void callee() {
		Example.intarray[0]=0;    	
		
	}                                                                      
}
