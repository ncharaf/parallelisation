import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


/*
 * 
 * This class will establish a connection between the client and the server.
 * Then, it will create a Service, that will deal with all the treatments.
 * 
 */

public class Client{

	private Socket connexion;
	private Service s;



	public Client(int port,String adressIP){
		int timeoutTime=5;
		try {
			this.connexion=new Socket(adressIP,port);
			this.connexion.setSoTimeout(timeoutTime*1000);
			this.s=new Service(this.connexion);
			this.closeConnexion();

		}catch (SocketException e ){
			System.out.println("Server is not running");
		}catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}


	public Socket getSocket(){
		return this.connexion;
	}



	public void closeConnexion(){
		try {
			this.connexion.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public Service getService(){
		return this.s;
	}



}
