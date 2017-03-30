import fr.paris8.iut.info.stare.DistributedInterface;
import fr.paris8.iut.info.stare.OntologyData;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
//import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;




/*
 * Main to create a Server.
 * The number of client is also defined here by passing them in arguments.
 * First argument correspond to the file (.owl), and the second one correspond to the maximum number of slaves.
 * Each connection from a client will correspond to a unique thread that will manage the client.
 * 
 */


public class MainServer {

	//args[0]=filename args[1]=max agent
	static DistributedInterface data;
	protected static  Boolean working=true;
	protected static  Boolean result=false;
	private  static  ServerSocket serverSocket=null ;
	private static  ArrayList<ThreadServer> All_Clients;
	private static  OntologyData result_found;
	static Map<OntologyData, InetAddress> stored = new HashMap<OntologyData, InetAddress>();
	static Iterator<OntologyData> iter_onto;
	private static boolean closed = false;



	public static void main(String[] args) {
		// Check Args
		if(args.length==2){
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
			int port=10000;
			String file_name=args[0];
			int nbagent=Integer.parseInt(args[1]);
			All_Clients=new ArrayList<ThreadServer>();
			try {
				try{
					try{


						Runtime.getRuntime().addShutdownHook(new Thread(""+All_Clients.size()) {	
							public void run() { 
								if (!closed) {
									if(serverSocket!=null){
										System.out.println("######### Unexpected ServerSocket closure  #########");
										try {
											System.out.println("######### ServerSocket is being closed #########");
											serverSocket.close();
											System.out.println("Number of connected clients : "+this.getName());
											Thread.currentThread().interrupt();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}
							}
						});	

						/*
						 * 
						 * Catch Error File not exists 
						 * if file contains a valid ontology
						 * Verify if Collection of ontology data is not empty
						 */
						File f =new File(file_name);
						if(f.exists() && !f.isDirectory()) { 
							System.out.println("Loading Data ............................");
							data = new DistributedInterface(file_name, nbagent);
							iter_onto =data.getData().iterator();
							System.out.println("Loading Finished ........................");
							serverSocket = new ServerSocket(port);
							System.out.println("######### Server Socket is created #########");
							System.out.println("Running on local Port: "+serverSocket.getLocalPort());
							while(working && All_Clients.size()<nbagent){
								try{
									ThreadServer t1=new ThreadServer(serverSocket.accept());
									All_Clients.add(t1);
									System.out.println("{Number of connexion/thread : "+All_Clients.size()+"}");
								} catch(SocketException e){
									//e.printStackTrace();
								}

							}

							// Join all working threads
							if(!serverSocket.isClosed()){
								System.out.println("######### Waiting for threads to be over #########");
								for(ThreadServer ts:All_Clients){
									try {
										ts.join();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}

							}
							if(result && !closed){
								System.out.println("~~ Ontology found : "+result_found+" ~~");
							}
							if(!serverSocket.isClosed()){

								serverSocket.close();
								System.out.println("######### ServerSocket is closed #########");
							}
						}else  {
							System.out.println("File not found ...............");
						}
					}catch(IllegalArgumentException e){
						System.out.println("Invalid Port number");
					}
				}catch(BindException e){
					System.out.println("Port already being used");
				}


			} catch (IOException e1) {
				//	e1.printStackTrace();
			}


		}
		else{
			System.out.println("Invalid arguments, please put filename as arg[0] ans number of client as arg[1]");
		}
	}


	public synchronized static OntologyData getOntologyData() {

		try{

			OntologyData data =iter_onto.next();
			if (!stored.containsKey(data)){
				return data;	
			} else {
				return null;
			}

		}catch( Exception e  ){
			return null;
		}
	}


	public static void closeServer(){
		System.out.println("~~ Result:"+result+" ~~");
		if(!serverSocket.isClosed()){
			try {
				serverSocket.close();
				closed=true;
				if(result){
					System.out.println("~~ Ontology found : "+result_found+" ~~");
				}
				System.out.println("######### ServerSocket is closed #########");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			All_Clients.stream().forEach(t->{try {
				t.getToClient().writeObject(Message.stop);
				t.getSocket().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}});


		}
		System.exit(0);
	}

	public synchronized static void addResult (OntologyData result){
		result_found=result;
	}

}
