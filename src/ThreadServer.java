import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

import fr.paris8.iut.info.stare.OntologyData;
import fr.paris8.iut.info.stare.StareAgent;

/*
 * This class will send the data to the client and catch results
 */

public class ThreadServer extends Thread {


	private Socket connexion;
	private ObjectOutputStream toClient ;
	private ObjectInputStream fromClient ;
	private OntologyData d ;
	private Collection<OntologyData> dataList;


	public ThreadServer(Socket connexion) {
		super();
		this.connexion = connexion;
		this.dataList = new ArrayList<OntologyData>();
		try {
			this.toClient = new ObjectOutputStream(connexion.getOutputStream());
			this.fromClient = new ObjectInputStream(connexion.getInputStream());
		} catch (IOException e) {
			System.out.println("Error creating Stream");
			//e.printStackTrace();
		}
		this.start();
	}


	@Override 
	public void run(){
		System.out.println("Client Ip Address : "+this.connexion.getInetAddress().getHostAddress());
		boolean running=true;
		boolean sending = true;
		do {
			if (sending) {	
				try {
					this.d =MainServer.getOntologyData();
					if(this.d==null){
						this.toClient.writeObject(Message.data_empty);
					}else{
						this.dataList.add(this.d);
						this.toClient.writeObject(new StareAgent(this.d));
						MainServer.stored.put(this.d, this.connexion.getInetAddress());
					}
					//System.out.println(MainServer.stored.values());
					//System.out.println("Size MAP:"+MainServer.stored.size());
					this.toClient.flush();
				} catch (IOException e) {
					//System.out.println("Error SocketStream");
				}
			}

			/*
			 * Read Result
			 */
			
			try{
				//System.out.println("Waiting ...for Message");
				Object  obj=this.fromClient.readObject();
				//System.out.println("Object Received by The Server:"+obj.getClass().getSimpleName());
				//System.out.println("Client :"+this.connexion.getInetAddress().getHostAddress()+"\tsays:"+obj);
				obj=(String )obj;
				//System.out.println("MEssageé"+obj.equals(Message.finish));
				if(obj.getClass().equals(String.class)){
					//	System.out.println("Switch ");
					switch(obj.toString()){
					//if(message.equals (Message.finish)){
					case Message.finish:
						//System.out.println("FINISH");
						//MainServer.working=false;
						Object obj2=this.fromClient.readObject();
						//System.out.println("MESSAGE ENVOYé"+Message.ack);
						this.toClient.writeObject(Message.ack);
						//System.out.println("OBJ 2:"+obj2.getClass().getSimpleName());
						//System.out.println("FINISH2");
						//System.out.println("Running:"+running);
						if(obj2.getClass().equals(Boolean.class)){
							Boolean result_client=(Boolean) obj2;
							if(result_client){
								MainServer.working=false;
								running=false;
								MainServer.result=result_client;
								Object obj3=this.fromClient.readObject();
								if(obj3.getClass().equals(OntologyData.class)){
									MainServer.addResult((OntologyData) obj3);
									MainServer.closeServer();
								}
								//System.out.println("RESULT:"+result_client);
								MainServer.result=result_client;
								//System.out.println("MAIN RESULT"+MainServer.result);
								/*
								}else{
									this.toClient.writeObject(obj2);
								}*/
							}
						}
						break;

					case Message.ram_max:
						sending = false;
						break;		
					case Message.cpu_max:
						sending = false;
						break;							
					case Message.request:
						break;
					default:
						System.out.println("UNEXPECTED MESSAGE");
						break;
					}
				}


			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				running=false;
			} catch (IOException e) {
				System.out.println("Interrupted Connexion");
				//System.out.println("SIZE BEFORE"+MainServer.stored.size());
				for (OntologyData toremove : dataList) {
					MainServer.stored.remove(toremove);
				}
				//System.out.println("SIZE AFTER"+MainServer.stored.size());

				//e.printStackTrace();
				running=false;

			}
		}while(running);



	}


	public ObjectOutputStream getToClient() {
		return toClient;
	}


	public Socket getSocket() {
		return this.connexion;
	}

	public OntologyData getOntologyData(){
		return this.d;
	}

}
