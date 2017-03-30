/*
 * Main to create and start a new client.
 */

public class MainClient {


	public static void main(String[] args) {

		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		@SuppressWarnings("unused")
		Client client = new Client(10000,"192.168.1.66");


	}





}




