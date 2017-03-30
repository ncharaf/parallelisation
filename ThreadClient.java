
import fr.paris8.iut.info.stare.StareAgent;

/*
 * Class that will launch the process in StareAgent and display the execution time.
 */

public class ThreadClient extends Thread {



	private StareAgent data ;
	private Boolean found=false;
	private float execution;




	public ThreadClient(String name,StareAgent d) {
		super(name);
		this.data = d;
		this.start();
	}



	@Override
	public void run(){

		long startTime = System.currentTimeMillis();
		System.out.println("######### Running #########");
		this.found=this.data.process();
		System.out.println("######### Treatement over #########");
		long finalTime= System.currentTimeMillis();
		this.execution=(float) ((finalTime-startTime)*1.0/1000);
		System.out.println("######### Execution Time :"+this.execution+"(s) #########");

	}




	public StareAgent getData(){
		return this.data;
	}


	public boolean getFound(){
		return this.found;
	}








}
