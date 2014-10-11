package vectorexample;
public class ExampleThread extends Thread                                  
{                                                                            
                                                                    
    public void run()                                               
    {           
//    	try {
//			Thread.sleep(1);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	String str = "";
    	for(int i=0; i<500;i++)
    	{
    		str += "" + i;
    	}
    
    	Example.intarray.set(0, 0);// =0;    	
    }                                                                      
}
