package microbench;

public class BenchMain {
	
	static int NumOfThread=10;
	static int TotalOperation=1000000;
	static int SPENumber = 1000;

	public static void main( String[] args)
	{
		if(args.length>0)
		{
			SPENumber = Integer.valueOf(args[0]).intValue();
			if(args.length>1)
			{
				NumOfThread = Integer.valueOf(args[1]).intValue();
				if(args.length>2)
				{
					TotalOperation = Integer.valueOf(args[2]).intValue();
				}
			}
			
			
		}
		
		try
		{
			BenchThread[] T = new BenchThread[NumOfThread];
			
	        for (int k=0;k<NumOfThread;k++) 
	        {
	        	BenchSharedObject s = new BenchSharedObject(SPENumber);
	        	T[k] = new BenchThread(s,TotalOperation/NumOfThread);
	        }
	        
			long st,et;
			st=System.currentTimeMillis();
	  		
			for (int k=0;k<NumOfThread;k++)
			{
				T[k].start();
			}
			
			for (int k=0;k<NumOfThread;k++)
			{
				T[k].join();
			}
			
	        et=System.currentTimeMillis();
			System.out.println(et-st);	
		}catch(Exception e){}
	}
}