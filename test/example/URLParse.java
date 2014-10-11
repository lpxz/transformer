package example;
public class URLParse {
	private String url;                                                    
    static volatile boolean  visited = false;
	URLParse (String url)
	{                                            
	   this.url=url;                                                    
	}                                                                           
	//should be synchronized, but not                                   
	public  void parse(String key)                                            
	{           
		synchronized (key) 
		{
//			try {
//				if(visited==false)
//				{visited = true;
//				this.wait();
//					}
//				else {
//					this.notify();
//				}
//				
//				
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		    
    	String val = getVal(key);
    	if(val.equals("Alice"))
    		replaceVal(key,"A");
    	if(val.equals("Bob"))
    		replaceVal(key,"B");
		}
	}
	private void replaceVal(String key, String newVal)
	{
        int keyPos=url.indexOf(key);                             
        int valPos=url.indexOf("=", keyPos)+1;               
        int ampPos=url.indexOf("&", keyPos);                
        if(ampPos<0) ampPos = url.length(); 	               
        url=url.substring(0, valPos) 	                               
                +newVal+url.substring(ampPos); 	       
    }                                                                          
    private String getVal(String key)
    { 	                       
        int keyPos=url.indexOf(key); 	                       
        int valPos=url.indexOf("=", keyPos)+1; 	       
        int ampPos=url.indexOf("&", keyPos); 	               
        if(ampPos<0) ampPos=url.length();
		
        try 
		{
	   		if(key.equals("key2")) {// thread2, thus, uses the stale index values.
				Thread.sleep(1);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
        return url.substring(valPos,ampPos);                  
	}
    public String getURL()
    {
    	return url;
    }
}