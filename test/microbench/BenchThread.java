package microbench;

public class BenchThread extends Thread {
	
	BenchSharedObject s;
	int numOfOperation;
	
	BenchThread(BenchSharedObject s, int numOfOperation)
	{
		this.s = s;
		this.numOfOperation = numOfOperation;
	}
	public void run()
	{
		for(int i=0;i<numOfOperation;i++)
			s.updateData();
	}
}
