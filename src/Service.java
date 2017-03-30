import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import fr.paris8.iut.info.stare.StareAgent;

/*
 * All the treatment from the client will be done in this class.
 * As far as there is enough resources, Service will create another thread to treat an OntologyData.
 *  
 */

public class Service {

	private Socket connexion;
	private ObjectInputStream fromServer;
	private ObjectOutputStream toServ;
	private ArrayList<ThreadClient> All_threads;
	private boolean running =true;


	public Service(Socket co){
		this.connexion=co;
		try {
			this.fromServer=new ObjectInputStream(this.connexion.getInputStream());
			this.toServ=new ObjectOutputStream(this.connexion.getOutputStream());
			do {
				this.All_threads=new ArrayList<ThreadClient>();
				this.launchThread();
				if (this.All_threads.size()!=0){
					this.waitThread();
				}
			}while(running);
			//this.sendResult(); 
			this.connexion.close();
		}catch(SocketTimeoutException e ){
			System.out.println("Timeout Exception");
			try {
				this.connexion.close();
			} catch (IOException e1) {
				System.out.println("Trouble in closing socket");
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
		}
		catch(SocketException e ){
			System.out.println("Socket Exception");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void launchThread(){
		
		/*
		 *
		 * Start All threads
		 *
		 */

		int i=0;
		boolean trying=true;
		do{
			try{
				//Set Ressource
				Ressource r ;
				//Define OS
				String os=System.getProperty("os.name");
				Os User=null;
				if(os.toLowerCase().contains("linux")){
					User=new Linux(os);
				}else if (os.toLowerCase().contains("windows")){
					User = new Windows(os);
				}else if (os.toLowerCase().contains("mac")){
					User=new Mac(os);
				}
				else{
					System.out.println("OS NOT SUPPORTED");
					System.exit(0);
				}
				//Complete ressource
				String hostname=System.getProperty("user.name");
				int cores = Runtime.getRuntime().availableProcessors();
				String architecture=System.getProperty("os.arch");
				r = new Ressource(hostname,User.getFrequency(),User.getMemoryRate(),User.getMemoryAvailable(),cores,os,architecture);
				System.out.println("Attempt n° :"+(this.All_threads.size()+1)+"\tRessource:"+r);
				Object obj;
				try {
					obj = this.fromServer.readObject();
					if(obj==null){
						try {
							this.toServ.writeObject(false);
							this.connexion.close();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
					if(obj.getClass().equals(String.class)){

						if(obj.equals(Message.data_empty)){
							System.out.println(Message.data_empty);
							trying=false;
							running = false;
						}else if (obj.equals(Message.stop)){
							this.stopAllThreads();
							running = false;
						}
						else{
							System.out.println("Unexpected Message"+obj);
						}
						
					} else if(obj.getClass().equals(StareAgent.class)){
						StareAgent data= (StareAgent) obj;
						try {
							try {
								ThreadClient tc=new ThreadClient(String.valueOf(i), data);
								this.All_threads.add(tc);
								//System.out.println("THREAD SIZE"+this.All_threads.size());
								if(r.getFrequency()>=0.8){
									trying=false;
									this.toServ.writeObject(Message.cpu_max);
									System.out.println("Max Thread Created :"+this.All_threads.size());
								}else{
									this.toServ.writeObject(Message.request);
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}catch (OutOfMemoryError e){
							trying=false;
							try {
								this.toServ.writeObject(Message.ram_max);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							System.out.println("Number of threads created :"+i);
						}
					}
				}catch (ClassNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();

				} catch(SocketException e){
					System.out.println("Interrupted Connexion");
				}
			}catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			i++;
		}while(trying);
		System.out.println("END LAUNCH");

	}
	public void waitThread(){
		System.out.println("######### Waiting Treatment #########");
		for(ThreadClient th:this.All_threads){
			try {
				th.join();
				if(th.getFound()){
					System.out.println("~~ True found ~~");
					this.sendResult(th);

				}

			}
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}




	public void stopAllThreads(){
		All_threads.stream().forEach(t ->t.interrupt());
	}



	public void sendResult(ThreadClient th){
		try {
			this.toServ.writeObject(Message.finish);
			Boolean result=th.getFound();
			this.running=false;
			this.toServ.writeObject(result);
			this.toServ.writeObject(th.getData().getOntologyData());
			try {
				Object test;
				do{
					test = this.fromServer.readObject();
				}while(!test.equals(Message.ack));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			this.toServ.flush();
		} catch (IOException e) {
			System.out.println("Error : Cannot Send Results");
			e.printStackTrace();
		}
	}
}
